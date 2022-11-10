/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.table.store.file.operation;

import org.apache.flink.connector.file.src.FileSourceSplit;
import org.apache.flink.connector.file.src.reader.BulkFormat;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.store.file.io.DataFileMeta;
import org.apache.flink.table.store.file.io.DataFilePathFactory;
import org.apache.flink.table.store.file.io.RowDataFileRecordReader;
import org.apache.flink.table.store.file.mergetree.compact.ConcatRecordReader;
import org.apache.flink.table.store.file.predicate.Predicate;
import org.apache.flink.table.store.file.schema.RowTypeExtractor;
import org.apache.flink.table.store.file.schema.SchemaManager;
import org.apache.flink.table.store.file.schema.TableSchema;
import org.apache.flink.table.store.file.utils.FileStorePathFactory;
import org.apache.flink.table.store.file.utils.RecordReader;
import org.apache.flink.table.store.format.FileFormat;
import org.apache.flink.table.store.table.source.DataSplit;
import org.apache.flink.table.store.utils.Projection;
import org.apache.flink.table.types.logical.RowType;

import javax.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.flink.table.store.file.predicate.PredicateBuilder.splitAnd;

/** {@link FileStoreRead} for {@link org.apache.flink.table.store.file.AppendOnlyFileStore}. */
public class AppendOnlyFileStoreRead implements FileStoreRead<RowData> {

    private final SchemaManager schemaManager;
    private final long schemaId;
    private final RowType rowType;
    private final RowTypeExtractor rowTypeExtractor;
    private final FileFormat fileFormat;
    private final FileStorePathFactory pathFactory;
    private final Map<Long, BulkFormat<RowData, FileSourceSplit>> schemaReaderFactories;

    private int[][] projection;

    @Nullable private List<Predicate> filters;

    public AppendOnlyFileStoreRead(
            SchemaManager schemaManager,
            long schemaId,
            RowType rowType,
            RowTypeExtractor rowTypeExtractor,
            FileFormat fileFormat,
            FileStorePathFactory pathFactory) {
        this.schemaManager = schemaManager;
        this.schemaId = schemaId;
        this.rowType = rowType;
        this.rowTypeExtractor = rowTypeExtractor;
        this.fileFormat = fileFormat;
        this.pathFactory = pathFactory;
        this.schemaReaderFactories = new HashMap<>();

        this.projection = Projection.range(0, rowType.getFieldCount()).toNestedIndexes();
    }

    public FileStoreRead<RowData> withProjection(int[][] projectedFields) {
        projection = projectedFields;
        return this;
    }

    @Override
    public FileStoreRead<RowData> withFilter(Predicate predicate) {
        this.filters = splitAnd(predicate);
        return this;
    }

    @Override
    public RecordReader<RowData> createReader(DataSplit split) throws IOException {
        DataFilePathFactory dataFilePathFactory =
                pathFactory.createDataFilePathFactory(split.partition(), split.bucket());
        List<ConcatRecordReader.ReaderSupplier<RowData>> suppliers = new ArrayList<>();
        for (DataFileMeta file : split.files()) {
            BulkFormat<RowData, FileSourceSplit> readerFactory =
                    schemaReaderFactories.computeIfAbsent(
                            file.schemaId(),
                            id -> {
                                TableSchema schema = schemaManager.schema(id);
                                RowType rowType = rowTypeExtractor.extract(schema);
                                return fileFormat.createReaderFactory(rowType, projection, filters);
                            });
            suppliers.add(
                    () ->
                            new RowDataFileRecordReader(
                                    dataFilePathFactory.toPath(file.fileName()), readerFactory));
        }

        return ConcatRecordReader.create(suppliers);
    }
}

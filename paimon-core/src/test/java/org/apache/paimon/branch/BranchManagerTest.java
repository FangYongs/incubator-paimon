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

package org.apache.paimon.branch;

import org.apache.paimon.catalog.CatalogContext;
import org.apache.paimon.catalog.FileSystemCatalog;
import org.apache.paimon.catalog.Identifier;
import org.apache.paimon.data.GenericRow;
import org.apache.paimon.fs.FileIO;
import org.apache.paimon.options.Options;
import org.apache.paimon.schema.Schema;
import org.apache.paimon.schema.SchemaManager;
import org.apache.paimon.table.FileStoreTable;
import org.apache.paimon.table.sink.BatchTableCommit;
import org.apache.paimon.table.sink.BatchTableWrite;
import org.apache.paimon.table.sink.BatchWriteBuilder;
import org.apache.paimon.types.DataTypes;
import org.apache.paimon.types.RowKind;
import org.apache.paimon.utils.BranchManager;
import org.apache.paimon.utils.SnapshotManager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.HashMap;

import static org.apache.paimon.CoreOptions.PATH;
import static org.apache.paimon.utils.Preconditions.checkNotNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** Tests for {@link BranchManager}. */
class BranchManagerTest {
    @TempDir Path path;

    @Test
    void testCreateBranch() throws Exception {
        org.apache.paimon.fs.Path catalogPath = new org.apache.paimon.fs.Path(path.toUri());
        Options tableOptions = new Options();
        FileIO fileIO = FileIO.get(catalogPath, CatalogContext.create(tableOptions));

        // Create database and table in file system catalog.
        try (FileSystemCatalog catalog = new FileSystemCatalog(fileIO, catalogPath)) {
            Identifier tableIdentifier = new Identifier("test_database", "test_table");
            catalog.createDatabase(tableIdentifier.getDatabaseName(), true);
            catalog.createTable(
                    tableIdentifier,
                    Schema.newBuilder()
                            .column("a", DataTypes.INT())
                            .column("b", DataTypes.BIGINT())
                            .column("c", DataTypes.INT())
                            .partitionKeys("a")
                            .build(),
                    true);

            // Get table and write data.
            FileStoreTable paimonTable = (FileStoreTable) catalog.getTable(tableIdentifier);
            BatchWriteBuilder batchWrite = paimonTable.newBatchWriteBuilder();
            try (BatchTableWrite writer = batchWrite.newWrite()) {
                for (int i = 0; i < 100; i++) {
                    GenericRow row = new GenericRow(RowKind.INSERT, 3);
                    row.setField(0, i);
                    row.setField(1, (long) i);
                    row.setField(2, i);
                    writer.write(row);
                }
                try (BatchTableCommit commit = batchWrite.newCommit()) {
                    commit.commit(writer.prepareCommit());
                }
            }

            // Create a tag 'test_tag' for the latest snapshot.
            paimonTable.createTag(
                    "test_tag", checkNotNull(paimonTable.snapshotManager().latestSnapshotId()));
            org.apache.paimon.fs.Path paimonTablePath =
                    new org.apache.paimon.fs.Path(Options.fromMap(paimonTable.options()).get(PATH));
            BranchManager branchManager =
                    new BranchManager(
                            FileIO.get(
                                    paimonTablePath,
                                    CatalogContext.create(Options.fromMap(new HashMap<>()))),
                            paimonTablePath);
            // There is no main branch file.
            assertThat(branchManager.getMainBranch()).isEqualTo("");

            // Create a branch for an invalid tag.
            assertThatThrownBy(() -> branchManager.createBranch("tag_not_exist", "new_branch"))
                    .hasMessage(String.format("Tag '%s' doesn't exist.", "tag_not_exist"));

            // Create a branch for 'test_tag', validate the schema and snapshot in the branch.
            branchManager.createBranch("test_tag", "branch_name");
            org.apache.paimon.fs.Path branchPath = branchManager.branchPath("branch_name");
            assertThat(
                            new SchemaManager(
                                            FileIO.get(
                                                    branchPath,
                                                    CatalogContext.create(
                                                            Options.fromMap(new HashMap<>()))),
                                            branchPath)
                                    .latest()
                                    .get())
                    .isEqualTo(
                            new SchemaManager(
                                            FileIO.get(
                                                    paimonTablePath,
                                                    CatalogContext.create(
                                                            Options.fromMap(new HashMap<>()))),
                                            paimonTablePath)
                                    .latest()
                                    .get());
            assertThat(
                            new SnapshotManager(
                                            FileIO.get(
                                                    branchPath,
                                                    CatalogContext.create(
                                                            Options.fromMap(new HashMap<>()))),
                                            branchPath)
                                    .latestSnapshot())
                    .isEqualTo(paimonTable.snapshotManager().latestSnapshot());
        }
    }
}

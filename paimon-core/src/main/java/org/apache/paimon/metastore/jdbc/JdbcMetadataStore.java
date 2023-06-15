package org.apache.paimon.metastore.jdbc;

import org.apache.paimon.metastore.DataLineageEntity;
import org.apache.paimon.metastore.MetadataStore;
import org.apache.paimon.metastore.TableLineageEntity;
import org.apache.paimon.predicate.Predicate;

import javax.annotation.Nullable;
import javax.sql.DataSource;
import java.util.Iterator;
import java.util.Map;

public class JdbcMetadataStore implements MetadataStore {
	private DataSource dataSource;

	public JdbcMetadataStore(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void storeSourceTableLineage(TableLineageEntity entity) {

	}

	@Override
	public void deleteSourceTableLineage(String job) {

	}

	@Override
	public Iterator<TableLineageEntity> sourceTableLineages(@Nullable Predicate predicate) {
		return null;
	}

	@Override
	public void storeSinkTableLineage(TableLineageEntity entity) {

	}

	@Override
	public void deleteSinkTableLineage(String job) {

	}

	@Override
	public Iterator<TableLineageEntity> sinkTableLineages(@Nullable Predicate predicate) {
		return null;
	}

	@Override
	public void storeSourceDataLineage(DataLineageEntity entity) {

	}

	@Override
	public Iterator<DataLineageEntity> sourceDataLineages(@Nullable Predicate predicate) {
		return null;
	}

	@Override
	public void storeSinkDataLineage(DataLineageEntity entity) {

	}

	@Override
	public Iterator<DataLineageEntity> sinkDataLineages(@Nullable Predicate predicate) {
		return null;
	}
}

package org.apache.paimon.metastore;

import org.apache.paimon.annotation.Public;

/**
 * Table lineage entity with database, table and job for table source and sink lineage.
 */
public class TableLineageEntity {
	private String database;
	private String table;
	private String job;

	public String getDatabase() {
		return database;
	}

	public String getTable() {
		return table;
	}

	public String getJob() {
		return job;
	}
}

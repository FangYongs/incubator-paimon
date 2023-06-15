package org.apache.paimon.metastore;

/**
 *
 */
public class DataLineageEntity {
	private final TableLineageEntity tableLineage;
	private final long barrierId;
	private final long snapshotId;

	public DataLineageEntity(TableLineageEntity tableLineage, long barrierId, long snapshotId) {
		this.tableLineage = tableLineage;
		this.barrierId = barrierId;
		this.snapshotId = snapshotId;
	}

	public TableLineageEntity getTableLineage() {
		return tableLineage;
	}

	public long getBarrierId() {
		return barrierId;
	}

	public long getSnapshotId() {
		return snapshotId;
	}
}

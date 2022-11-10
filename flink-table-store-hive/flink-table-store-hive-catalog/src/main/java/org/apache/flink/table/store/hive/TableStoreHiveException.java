package org.apache.flink.table.store.hive;

public class TableStoreHiveException extends RuntimeException {

	public TableStoreHiveException(String message) {
		super(message);
	}

	public TableStoreHiveException(Throwable cause) {
		super(cause);
	}

	public TableStoreHiveException(String message, Throwable cause) {
		super(message, cause);
	}
}

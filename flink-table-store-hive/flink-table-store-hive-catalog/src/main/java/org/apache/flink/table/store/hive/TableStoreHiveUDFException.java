package org.apache.flink.table.store.hive;

public class TableStoreHiveUDFException extends RuntimeException {

	public TableStoreHiveUDFException(String message) {
		super(message);
	}

	public TableStoreHiveUDFException(Throwable cause) {
		super(cause);
	}

	public TableStoreHiveUDFException(String message, Throwable cause) {
		super(message, cause);
	}
}

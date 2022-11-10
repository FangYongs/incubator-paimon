package org.apache.flink.table.store.hive;

public class SqlAlterHiveTable {

	public static final String ALTER_TABLE_OP = "alter.table.op";
	public static final String ALTER_COL_CASCADE = "alter.column.cascade";

	/** Type of ALTER TABLE operation. */
	public enum AlterTableOp {
		CHANGE_TBL_PROPS,
		CHANGE_SERDE_PROPS,
		CHANGE_FILE_FORMAT,
		CHANGE_LOCATION,
		ALTER_COLUMNS
	}
}

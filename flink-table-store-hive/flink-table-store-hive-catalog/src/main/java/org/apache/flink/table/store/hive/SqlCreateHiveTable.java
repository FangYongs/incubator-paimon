package org.apache.flink.table.store.hive;

public class SqlCreateHiveTable {

	public static final String TABLE_LOCATION_URI = "hive.location-uri";
	public static final String TABLE_IS_EXTERNAL = "hive.is-external";
	public static final String PK_CONSTRAINT_TRAIT = "hive.pk.constraint.trait";
	public static final String NOT_NULL_CONSTRAINT_TRAITS = "hive.not.null.constraint.traits";
	public static final String NOT_NULL_COLS = "hive.not.null.cols";


	/** To represent ROW FORMAT in CREATE TABLE DDL. */
	public static class HiveTableRowFormat {

		public static final String SERDE_LIB_CLASS_NAME = "hive.serde.lib.class.name";
		public static final String SERDE_INFO_PROP_PREFIX = "hive.serde.info.prop.";
		public static final String FIELD_DELIM = SERDE_INFO_PROP_PREFIX + "field.delim";
		public static final String COLLECTION_DELIM = SERDE_INFO_PROP_PREFIX + "collection.delim";
		public static final String ESCAPE_CHAR = SERDE_INFO_PROP_PREFIX + "escape.delim";
		public static final String MAPKEY_DELIM = SERDE_INFO_PROP_PREFIX + "mapkey.delim";
		public static final String LINE_DELIM = SERDE_INFO_PROP_PREFIX + "line.delim";
		public static final String SERIALIZATION_NULL_FORMAT =
				SERDE_INFO_PROP_PREFIX + "serialization.null.format";
	}

	/** To represent STORED AS in CREATE TABLE DDL. */
	public static class HiveTableStoredAs {

		public static final String STORED_AS_FILE_FORMAT = "hive.storage.file-format";
		public static final String STORED_AS_INPUT_FORMAT = "hive.stored.as.input.format";
		public static final String STORED_AS_OUTPUT_FORMAT = "hive.stored.as.output.format";
	}
}

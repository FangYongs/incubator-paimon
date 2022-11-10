package org.apache.flink.table.store.hive;

/** Configs for catalog meta-objects in {@link HiveCatalog}. */
public class HiveCatalogConfig {

	// Table related configs
	public static final String COMMENT = "comment";
	public static final String DEFAULT_LIST_COLUMN_TYPES_SEPARATOR = ":";

	// Partition related configs
	public static final String PARTITION_LOCATION = "partition.location";
}

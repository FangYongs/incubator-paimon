package org.apache.paimon.metastore.jdbc;

import org.apache.paimon.options.ConfigOption;

import static org.apache.paimon.options.ConfigOptions.key;

public class JdbcMetadataStoreOptions {
	/**
	 * The jdbc driver class for metadata store.
	 */
	public static ConfigOption<String> METADATA_JDBC_DRIVER =
			key("metadata.jdbc.driver")
					.stringType()
					.noDefaultValue()
					.withDescription("The jdbc driver class for metadata store.");
	/**
	 * The jdbc url for metadata store.
	 */
	public static ConfigOption<String> METADATA_JDBC_URL =
			key("metadata.jdbc.url")
					.stringType()
					.noDefaultValue()
					.withDescription("The jdbc url for metadata store.");

	/**
	 * The jdbc url for metadata store.
	 */
	public static ConfigOption<String> METADATA_JDBC_USERNAME =
			key("metadata.jdbc.username")
					.stringType()
					.noDefaultValue()
					.withDescription("The jdbc username for metadata store.");

	/**
	 * The jdbc url for metadata store.
	 */
	public static ConfigOption<String> METADATA_JDBC_PASSWORD =
			key("metadata.jdbc.password")
					.stringType()
					.noDefaultValue()
					.withDescription("The jdbc password for metadata store.");

	/**
	 * The jdbc url for metadata store.
	 */
	public static ConfigOption<String> METADATA_JDBC_DATABASE =
			key("metadata.jdbc.database")
					.stringType()
					.defaultValue("paimon")
					.withDescription("The jdbc database for metadata store and default database is `paimon_system`");
}

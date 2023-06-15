package org.apache.paimon.metastore.jdbc;

import org.apache.paimon.options.Options;

import javax.sql.DataSource;

public class DataSourceProvider {
	private final Options options;
	public DataSourceProvider(Options options) {
		this.options = options;
	}

	public DataSource create() {
		return null;
	}
}

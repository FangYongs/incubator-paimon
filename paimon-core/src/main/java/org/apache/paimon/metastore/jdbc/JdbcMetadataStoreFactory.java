package org.apache.paimon.metastore.jdbc;

import org.apache.paimon.metastore.MetadataStore;
import org.apache.paimon.metastore.MetadataStoreContext;
import org.apache.paimon.metastore.MetadataStoreFactory;

public class JdbcMetadataStoreFactory implements MetadataStoreFactory {

	public static final String IDENTIFIER = "jdbc";

	@Override
	public String identifier() {
		return IDENTIFIER;
	}

	@Override
	public MetadataStore create(MetadataStoreContext context) {
		return new JdbcMetadataStore(new DataSourceProvider(context.options()).create());
	}
}

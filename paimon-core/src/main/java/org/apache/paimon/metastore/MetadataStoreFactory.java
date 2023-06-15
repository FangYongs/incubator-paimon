package org.apache.paimon.metastore;

public interface MetadataStoreFactory {

	String identifier();

	MetadataStore create(MetadataStoreContext context);
}

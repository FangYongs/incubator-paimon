package org.apache.paimon.metastore;

import org.apache.paimon.annotation.Public;
import org.apache.paimon.options.Options;

import java.util.concurrent.Executor;

@Public
public class MetadataStoreContext {
	private Options options;

	private MetadataStoreContext(Options options) {
		this.options = options;
	}

	public static MetadataStoreContext create(Options options) {
		return new MetadataStoreContext(options);
	}

	public Options options() {
		return options;
	}
}

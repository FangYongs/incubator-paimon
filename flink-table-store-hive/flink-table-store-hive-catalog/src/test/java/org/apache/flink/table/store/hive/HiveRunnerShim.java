package org.apache.flink.table.store.hive;

import com.klarna.hiverunner.builder.HiveShellBuilder;
import com.klarna.hiverunner.config.HiveRunnerConfig;

/** Shim layer for hive runner dependency. */
public interface HiveRunnerShim {

	/** Sets CommandShellEmulation for HiveShellBuilder. */
	void setCommandShellEmulation(HiveShellBuilder builder, HiveRunnerConfig config)
			throws Exception;
}

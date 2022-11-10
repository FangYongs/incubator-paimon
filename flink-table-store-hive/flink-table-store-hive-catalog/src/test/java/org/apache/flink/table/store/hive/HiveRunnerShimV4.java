package org.apache.flink.table.store.hive;

import com.klarna.hiverunner.builder.HiveShellBuilder;
import com.klarna.hiverunner.config.HiveRunnerConfig;

import java.lang.reflect.Method;

/** Shim for hive runner 4.x. */
public class HiveRunnerShimV4 implements HiveRunnerShim {

    @Override
    public void setCommandShellEmulation(HiveShellBuilder builder, HiveRunnerConfig config)
            throws Exception {
        Method method = config.getClass().getDeclaredMethod("getCommandShellEmulator");
        Object emulator = method.invoke(config);
        Class emulatorClz = Class.forName("com.klarna.hiverunner.sql.cli.CommandShellEmulator");
        method = builder.getClass().getDeclaredMethod("setCommandShellEmulation", emulatorClz);
        method.invoke(builder, emulator);
    }
}

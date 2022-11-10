package org.apache.flink.table.store.hive;

import org.apache.flink.table.store.hive.client.HiveShimLoader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Loader to load proper HiveRunnerShim. */
public class HiveRunnerShimLoader {

    private static final Map<String, HiveRunnerShim> hiveRunnerShims = new ConcurrentHashMap<>();

    private HiveRunnerShimLoader() {}

    public static HiveRunnerShim load() {
        String hiveVersion = HiveShimLoader.getHiveVersion();
        return hiveRunnerShims.computeIfAbsent(
                hiveVersion,
                v -> {
                    switch (v) {
                        case HiveShimLoader.HIVE_VERSION_V2_3_0:
                        case HiveShimLoader.HIVE_VERSION_V2_3_1:
                        case HiveShimLoader.HIVE_VERSION_V2_3_2:
                        case HiveShimLoader.HIVE_VERSION_V2_3_3:
                        case HiveShimLoader.HIVE_VERSION_V2_3_4:
                        case HiveShimLoader.HIVE_VERSION_V2_3_5:
                        case HiveShimLoader.HIVE_VERSION_V2_3_6:
                        case HiveShimLoader.HIVE_VERSION_V2_3_7:
                        case HiveShimLoader.HIVE_VERSION_V2_3_8:
                        case HiveShimLoader.HIVE_VERSION_V2_3_9:
                        case HiveShimLoader.HIVE_VERSION_V3_1_0:
                        case HiveShimLoader.HIVE_VERSION_V3_1_1:
                        case HiveShimLoader.HIVE_VERSION_V3_1_2:
                        case HiveShimLoader.HIVE_VERSION_V3_1_3:
                            return new HiveRunnerShimV4();
                        default:
                            throw new RuntimeException("Unsupported Hive version " + v);
                    }
                });
    }
}

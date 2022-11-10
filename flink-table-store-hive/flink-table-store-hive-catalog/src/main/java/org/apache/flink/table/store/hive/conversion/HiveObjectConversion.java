package org.apache.flink.table.store.hive.conversion;

import javax.annotation.Nullable;
import java.io.Serializable;

public interface HiveObjectConversion extends Serializable {

	@Nullable
	Object toHiveObject(@Nullable Object o);
}

package org.apache.paimon.table.system;

import org.apache.paimon.table.ReadonlyTable;
import org.apache.paimon.table.Table;
import org.apache.paimon.table.source.InnerTableRead;
import org.apache.paimon.table.source.InnerTableScan;
import org.apache.paimon.types.RowType;

import java.util.List;
import java.util.Map;

public class CatalogOptionsTable implements ReadonlyTable {
	@Override
	public InnerTableScan newScan() {
		return null;
	}

	@Override
	public InnerTableRead newRead() {
		return null;
	}

	@Override
	public String name() {
		return null;
	}

	@Override
	public RowType rowType() {
		return null;
	}

	@Override
	public List<String> primaryKeys() {
		return null;
	}

	@Override
	public Table copy(Map<String, String> dynamicOptions) {
		return null;
	}
}

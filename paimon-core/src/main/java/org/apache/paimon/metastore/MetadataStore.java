package org.apache.paimon.metastore;

import org.apache.paimon.predicate.Predicate;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Map;

public interface MetadataStore {
	/**
	 * Store the source table and job lineage.
	 *
	 * @param entity the table lineage entity
	 */
	void storeSourceTableLineage(TableLineageEntity entity);

	/**
	 * Delete the source table lineage for given job.
	 *
	 * @param job the job for table lineage
	 */
	void deleteSourceTableLineage(String job);

	/**
	 * Get source table and job lineages.
	 *
	 * @param predicate the predicate for the table lineages
	 * @return the iterator for source table and job lineages
	 */
	Iterator<TableLineageEntity> sourceTableLineages(@Nullable Predicate predicate);

	/**
	 * Store the sink table and job lineage.
	 *
	 * @param entity the table lineage entity
	 */
	void storeSinkTableLineage(TableLineageEntity entity);

	/**
	 * Delete the sink table lineage for given job.
	 *
	 * @param job the job for table lineage
	 */
	void deleteSinkTableLineage(String job);

	/**
	 * Get sink table and job lineages.
	 *
	 * @param predicate the predicate for the table lineages
	 * @return the iterator for source table and job lineages
	 */
	Iterator<TableLineageEntity> sinkTableLineages(@Nullable Predicate predicate);

	/**
	 * Store the source table and job lineage.
	 *
	 * @param entity the data lineage entity
	 */
	void storeSourceDataLineage(DataLineageEntity entity);

	/**
	 * Get source data and job lineages.
	 *
	 * @param predicate the predicate for the table lineages
	 * @return the iterator for source table and job lineages
	 */
	Iterator<DataLineageEntity> sourceDataLineages(@Nullable Predicate predicate);

	/**
	 * Store the source table and job lineage.
	 *
	 * @param entity the data lineage entity
	 */
	void storeSinkDataLineage(DataLineageEntity entity);

	/**
	 * Get sink data and job lineages.
	 *
	 * @param predicate the predicate for the table lineages
	 * @return the iterator for source table and job lineages
	 */
	Iterator<DataLineageEntity> sinkDataLineages(@Nullable Predicate predicate);
}

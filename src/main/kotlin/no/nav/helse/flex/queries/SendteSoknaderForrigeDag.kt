package no.nav.helse.flex.queries

import com.google.cloud.bigquery.BigQuery
import com.google.cloud.bigquery.JobInfo
import com.google.cloud.bigquery.QueryJobConfiguration
import com.google.cloud.bigquery.TableResult

fun BigQuery.finnSisteDagsSendteSoknader(): Map<String, Int> {
    val query =
        """
        SELECT count(*) as antall, soknadstype
        FROM `flex-prod-af40.sykepengesoknad_datastream.public_sykepengesoknad`
        WHERE status = 'SENDT'
        AND DATE(sendt) = DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY)
        GROUP BY soknadstype
        """.trimIndent()

    val queryConfig = QueryJobConfiguration.newBuilder(query).build()
    val queryJob = this.create(JobInfo.newBuilder(queryConfig).build())
    val result: TableResult = queryJob.getQueryResults()

    val ret: HashMap<String, Int> = hashMapOf()
    for (row in result.iterateAll()) {
        ret[row.get("soknadstype").stringValue] = row.get("antall").longValue.toInt()
    }
    return ret.toMap()
}

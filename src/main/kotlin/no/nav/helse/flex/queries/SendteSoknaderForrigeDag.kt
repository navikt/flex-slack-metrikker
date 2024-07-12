package no.nav.helse.flex.queries

import com.google.cloud.bigquery.BigQuery
import com.google.cloud.bigquery.JobInfo
import com.google.cloud.bigquery.QueryJobConfiguration
import com.google.cloud.bigquery.TableResult
import no.nav.helse.flex.slack.BlockElement
import no.nav.helse.flex.slack.MarkdownSection
import no.nav.helse.flex.slack.MarkdownText
import java.util.*
import kotlin.collections.HashMap

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

fun Map<String, Int>.sisteDagsSoknaderTilBlocker(): BlockElement {
    val builder = StringBuilder()
    val totalt = this.values.sum()

    builder.append("\n:rocket: $totalt søknader sendt i går\n\n")
    this.forEach { (key, value) ->
        builder.append("> *${key.lowercase()}*: $value\n")
    }

    return MarkdownSection(text = MarkdownText(text = builder.toString()))
}

package no.nav.helse.flex.queries

import com.google.cloud.bigquery.BigQuery
import com.google.cloud.bigquery.JobInfo
import com.google.cloud.bigquery.QueryJobConfiguration
import com.google.cloud.bigquery.TableResult
import no.nav.helse.flex.slack.BlockElement
import no.nav.helse.flex.slack.MarkdownSection
import no.nav.helse.flex.slack.MarkdownText

fun BigQuery.finnGosysOppgaver(): Map<String, Int> {
    val query =
        """
        SELECT count(*) as antall, status
        FROM `flex-prod-af40.arkivering_oppgave_datastream.public_oppgavestyring`
        WHERE DATE(modifisert) = DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY)
        GROUP BY status
        """.trimIndent()

    val queryConfig = QueryJobConfiguration.newBuilder(query).build()
    val queryJob = this.create(JobInfo.newBuilder(queryConfig).build())
    val result: TableResult = queryJob.getQueryResults()

    val ret: HashMap<String, Int> = hashMapOf()
    for (row in result.iterateAll()) {
        ret[row.get("status").stringValue] = row.get("antall").longValue.toInt()
    }
    return ret.toMap()
}

fun Map<String, Int>.gosysOppgaverTilBlockElement(): BlockElement {
    val totalt = this.filter { it.key.contains("Opprettet") }.values.sum()

    return MarkdownSection(text = MarkdownText(text = ":to-do-list: $totalt oppgaver opprettet i gosys\n"))
}

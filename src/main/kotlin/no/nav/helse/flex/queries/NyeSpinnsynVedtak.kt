package no.nav.helse.flex.queries

import com.google.cloud.bigquery.BigQuery
import com.google.cloud.bigquery.JobInfo
import com.google.cloud.bigquery.QueryJobConfiguration
import com.google.cloud.bigquery.TableResult
import no.nav.helse.flex.slack.BlockElement
import no.nav.helse.flex.slack.MarkdownSection
import no.nav.helse.flex.slack.MarkdownText

fun BigQuery.spinnsynVedtak(): Map<String, Int> {
    val query =
        """
        SELECT count(*) as antall, utbetaling_type
        FROM `flex-prod-af40.spinnsyn_datastream.public_utbetaling`
        WHERE DATE(opprettet) = DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY)
        AND skal_vises_til_bruker is true
        GROUP BY utbetaling_type
        """.trimIndent()

    val queryConfig = QueryJobConfiguration.newBuilder(query).build()
    val queryJob = this.create(JobInfo.newBuilder(queryConfig).build())
    val result: TableResult = queryJob.getQueryResults()

    val ret: HashMap<String, Int> = hashMapOf()
    for (row in result.iterateAll()) {
        ret[row.get("utbetaling_type").stringValue] = row.get("antall").longValue.toInt()
    }
    return ret.toMap()
}

fun Map<String, Int>.spinnsynVedtakTilBlock(): BlockElement {
    val revurderinger = this["REVURDERING"] ?: 0
    val nye = this["UTBETALING"] ?: 0

    val text =
        ":speil: $nye nye vedtak i sendt til bruker i spinnsyn\n" +
            ":recycle: $revurderinger revurderte vedtak sendt til bruker\n"
    return MarkdownSection(text = MarkdownText(text = text))
}

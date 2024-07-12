package no.nav.helse.flex.queries

import com.google.cloud.bigquery.BigQuery
import com.google.cloud.bigquery.JobInfo
import com.google.cloud.bigquery.QueryJobConfiguration
import com.google.cloud.bigquery.TableResult
import no.nav.helse.flex.slack.MarkdownSection
import no.nav.helse.flex.slack.MarkdownText

fun BigQuery.finnForrigeDagsVarsler(): Map<String, Int> {
    val query =
        """
        SELECT count(*) as antall, status
        FROM `flex-prod-af40.inntektsmelding_status_datastream.public_vedtaksperiode_behandling_status`
        WHERE status in (
          'VARSLET_MANGLER_INNTEKTSMELDING_FØRSTE',
          'VARSLET_MANGLER_INNTEKTSMELDING_ANDRE',
          'VARSLET_VENTER_PÅ_SAKSBEHANDLER_FØRSTE',
          'REVARSLET_VENTER_PÅ_SAKSBEHANDLER'
        )
        AND DATE(tidspunkt) = DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY)
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

fun Map<String, Int>.forrigeDagsVarslerTilBlocker(): MarkdownSection {
    fun String.beskrivVarsel(): String =
        when (this) {
            "VARSLET_MANGLER_INNTEKTSMELDING_FØRSTE" -> "varslet manglende inntektsmelding første gang"
            "VARSLET_MANGLER_INNTEKTSMELDING_ANDRE" -> "varslet manglende inntektsmelding andre gang"
            "VARSLET_VENTER_PÅ_SAKSBEHANDLER_FØRSTE" -> "varslet venter på saksbehandler første gang"
            "REVARSLET_VENTER_PÅ_SAKSBEHANDLER" -> "revarslet venter på saksbehandler"
            else -> this
        }

    val builder = StringBuilder()
    val totalt = this.values.sum()

    builder.append("\n:email: $totalt varsler sendt\n\n")
    this.entries.sortedByDescending { it.value }.associate { it.toPair() }.forEach { (key, value) ->
        builder.append("> $value ${key.beskrivVarsel()}\n")
    }

    return MarkdownSection(text = MarkdownText(text = builder.toString()))
}

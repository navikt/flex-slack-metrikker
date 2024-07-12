package no.nav.helse.flex.queries

import com.google.cloud.bigquery.BigQuery
import com.google.cloud.bigquery.JobInfo
import com.google.cloud.bigquery.QueryJobConfiguration
import com.google.cloud.bigquery.TableResult
import no.nav.helse.flex.slack.BlockElement
import no.nav.helse.flex.slack.MarkdownSection
import no.nav.helse.flex.slack.MarkdownText

fun BigQuery.finnForrigeDagsSporsmal(): Map<String, Int> {
    val query =
        """
        SELECT count(*) as antall, sp.tag tag
        FROM `flex-prod-af40.sykepengesoknad_datastream.public_sykepengesoknad` ss, 
        `flex-prod-af40.sykepengesoknad_datastream.public_sporsmal`  sp
        WHERE ss.status = 'SENDT'
        AND DATE(ss.sendt) = DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY)
        AND ss.id = sp.sykepengesoknad_id
        GROUP BY sp.tag
        """.trimIndent()

    val queryConfig = QueryJobConfiguration.newBuilder(query).build()
    val queryJob = this.create(JobInfo.newBuilder(queryConfig).build())
    val result: TableResult = queryJob.getQueryResults()

    val ret: HashMap<String, Int> = hashMapOf()
    for (row in result.iterateAll()) {
        ret[row.get("tag").stringValue] = row.get("antall").longValue.toInt()
    }
    return ret.toMap()
}

fun Map<String, Int>.forrigeDagsSporsmalTilBlocker(): BlockElement {
    val builder = StringBuilder()
    val medlemskapTags = listOf(
        "MEDLEMSKAP_OPPHOLDSTILLATELSE_V2",
        "MEDLEMSKAP_UTFORT_ARBEID_UTENFOR_NORGE",
        "MEDLEMSKAP_OPPHOLD_UTENFOR_NORGE",
        "MEDLEMSKAP_OPPHOLD_UTENFOR_EOS"
    )
    val medlemskap = this.filter { medlemskapTags.contains(it.key) }
    val totalt = medlemskap.values.sum()

    builder.append("\n:norge: $totalt medlemskapspørsmål besvart\n\n")
    medlemskap.entries.sortedByDescending { it.value }.associate { it.toPair() }.forEach { (key, value) ->
        builder.append("> $value ${key.beskrivSporsmalVarsel()}\n")
    }

    return MarkdownSection(text = MarkdownText(text = builder.toString()))
}

fun String.beskrivSporsmalVarsel(): String? {
    when (this) {
        "MEDLEMSKAP_OPPHOLDSTILLATELSE_V2" -> return " spørsmål om oppholdstillatelse"
        "MEDLEMSKAP_UTFORT_ARBEID_UTENFOR_NORGE" -> return " spørsmål om utført arbeid utenfor Norge"
        "MEDLEMSKAP_OPPHOLD_UTENFOR_NORGE" -> return " spørsmål om opphold utenfor Norge"
        "MEDLEMSKAP_OPPHOLD_UTENFOR_EOS" -> return " spørsmål om opphold utenfor EØS"
        else -> return null
    }
}

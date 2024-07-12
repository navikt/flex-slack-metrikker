package no.nav.helse.flex

import com.google.cloud.bigquery.BigQueryOptions
import com.google.cloud.bigquery.JobInfo
import com.google.cloud.bigquery.QueryJobConfiguration
import com.google.cloud.bigquery.TableResult
import no.nav.helse.flex.slack.SlackClient

fun main() {
    try {
        println("Kjører main")
        val slackToken = System.getenv("SLACK_TOKEN")
        val dailySlackChannel = System.getenv("DAILY_SLACK_CHANNEL")
        val projectId = System.getenv("GCP_TEAM_PROJECT_ID")
        if (slackToken == null) {
            println("SLACK_TOKEN is not set")
            return
        }
        if (dailySlackChannel == null) {
            println("DAILY_SLACK_CHANNEL is not set")
            return
        }
        if (projectId == null) {
            println("GCP_TEAM_PROJECT_ID is not set")
            return
        }
        val slackClient = SlackClient(slackToken)

        val bigQuery = BigQueryOptions.newBuilder().setProjectId(projectId).build().service

        val query =
            """
            SELECT count(*) as antall, soknadstype
            FROM `flex-prod-af40.sykepengesoknad_datastream.public_sykepengesoknad`
            WHERE status = 'SENDT'
            AND DATE(sendt) = DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY)
            GROUP BY soknadstype
            """.trimIndent()

        // Konfigurer spørringsjobben
        val queryConfig = QueryJobConfiguration.newBuilder(query).build()

        // Kjør spørringen
        val queryJob = bigQuery.create(JobInfo.newBuilder(queryConfig).build())

        // Vent på at spørringen skal fullføres og få resultatet
        val result: TableResult = queryJob.getQueryResults()

        val msg = StringBuilder()
        // Iterer gjennom resultatene og skriv ut
        for (row in result.iterateAll()) {
            val antall = row.get("antall").longValue
            val soknadstype = row.get("soknadstype").stringValue
            // legg til i msg
            msg.append("Antall: $antall, Søknadstype: $soknadstype\n")
            println("Antall: $antall, Søknadstype: $soknadstype")
        }
        slackClient.postMessage(msg.toString(), dailySlackChannel)
        println("Ferdig med main")
    } catch (e: Exception) {
        println("Noe gikk galt: $e")
    }
}

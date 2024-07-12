package no.nav.helse.flex

import com.google.cloud.bigquery.BigQueryOptions
import no.nav.helse.flex.queries.finnSisteDagsSendteSoknader
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

        val sistesendtesoknader = bigQuery.finnSisteDagsSendteSoknader()
        slackClient.postMessage(sistesendtesoknader.toString(), dailySlackChannel)
        println("Ferdig med main")
    } catch (e: Exception) {
        println("Noe gikk galt: $e")
    }
}

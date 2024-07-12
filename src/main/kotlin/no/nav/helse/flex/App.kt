package no.nav.helse.flex

import com.google.cloud.bigquery.BigQueryOptions
import no.nav.helse.flex.queries.finnSisteDagsSendteSoknader
import no.nav.helse.flex.queries.sisteDagsSoknaderTilBlocker
import no.nav.helse.flex.slack.BlockElement
import no.nav.helse.flex.slack.SlackClient
import org.slf4j.LoggerFactory

val log = LoggerFactory.getLogger("no.nav.helse.flex.App")

fun main() {
    try {
        log.info("Starter flex-slack-metriker")

        val env = hentEnvironment()
        val slackClient = SlackClient(env.slackToken)
        val bigQuery = BigQueryOptions.newBuilder().setProjectId(env.gcpProjectId).build().service

        val blocker = mutableListOf<BlockElement>()

        bigQuery.finnSisteDagsSendteSoknader().sisteDagsSoknaderTilBlocker().also { blocker.add(it) }

        slackClient.postMessage(text = "Gårsdagens metrikker", blocks = blocker, channel = env.dailySlackChannel)
        log.info("Ferdig med flex-slack-metriker")
    } catch (e: Exception) {
        log.error("Noe gikk galt: ${e.message}", e)
    }
}

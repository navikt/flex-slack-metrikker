package no.nav.helse.flex

import com.google.cloud.bigquery.BigQueryOptions
import no.nav.helse.flex.queries.*
import no.nav.helse.flex.slack.*
import org.slf4j.LoggerFactory

val log = LoggerFactory.getLogger("no.nav.helse.flex.App")

fun main() {
    try {
        log.info("Starter flex-slack-metriker")

        val env = hentEnvironment()
        val slackClient = SlackClient(env.slackToken)
        val bigQuery = BigQueryOptions.newBuilder().setProjectId(env.gcpProjectId).build().service

        val blocks = mutableListOf<BlockElement>()
        blocks.add(Header(HeaderText("Gårsdagens metrikker")))
        bigQuery.finnSisteDagsSendteSoknader().sisteDagsSoknaderTilBlocker().also { blocks.add(it) }
        blocks.add(Divider())
        bigQuery.finnForrigeDagsVarsler().forrigeDagsVarslerTilBlocker().also { blocks.add(it) }

        val forrigeDagsSporsmal = bigQuery.finnForrigeDagsSporsmal()
        blocks.add(Divider())
        forrigeDagsSporsmal.medlemskapSporsmalBlock().also { blocks.add(it) }
        blocks.add(Divider())
        forrigeDagsSporsmal.yrkeskadeSpmBlock().also { blocks.add(it) }
        blocks.add(Divider())
        bigQuery.finnGosysOppgaver().gosysOppgaverTilBlockElement().also { blocks.add(it) }
        blocks.add(Divider())
        bigQuery.spinnsynVedtak().spinnsynVedtakTilBlock().also { blocks.add(it) }

        slackClient.postMessage(
            text = "Gårsdagens metrikker",
            blocks = blocks,
            channel = env.dailySlackChannel,
        )
        log.info("Ferdig med flex-slack-metriker")
    } catch (e: Exception) {
        log.error("Noe gikk galt: ${e.message}", e)
    }
}

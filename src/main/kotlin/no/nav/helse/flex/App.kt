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

        val blocker = mutableListOf<BlockElement>()
        blocker.add(Header(HeaderText("Gårsdagens metrikker")))
        bigQuery.finnSisteDagsSendteSoknader().sisteDagsSoknaderTilBlocker().also { blocker.add(it) }
        blocker.add(Divider())
        bigQuery.finnForrigeDagsVarsler().forrigeDagsVarslerTilBlocker().also { blocker.add(it) }
        val forrigeDagsSporsmal = bigQuery.finnForrigeDagsSporsmal()
        blocker.add(Divider())
        forrigeDagsSporsmal.medlemskapSporsmalBlock().also { blocker.add(it) }
        blocker.add(Divider())
        forrigeDagsSporsmal.yrkeskadeSpmBlock().also { blocker.add(it) }
        blocker.add(Divider())
        bigQuery.finnGosysOppgaver().gosysOppgaverTilBlockElement().also { blocker.add(it) }

        slackClient.postMessage(
            text = "Gårsdagens metrikker",
            blocks = blocker,
            channel = env.dailySlackChannel,
        )
        log.info("Ferdig med flex-slack-metriker")
    } catch (e: Exception) {
        log.error("Noe gikk galt: ${e.message}", e)
    }
}

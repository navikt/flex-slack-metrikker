package no.nav.helse.flex

import no.nav.helse.flex.slack.SlackClient

fun main() {
    val slackToken = System.getenv("SLACK_TOKEN")
    val testchannel = System.getenv("TEST_CHANNEL")
    if (slackToken == null) {
        println("SLACK_TOKEN is not set")
        return
    }
    val slackClient = SlackClient(slackToken)
    slackClient.postMessage("hei", testchannel)
    println("hei")
}

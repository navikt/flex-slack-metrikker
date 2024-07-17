package no.nav.helse.flex.slack

import no.nav.helse.flex.objectMapper
import no.nav.helse.flex.serialisertTilString
import org.slf4j.LoggerFactory
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.InetAddress
import java.net.URI
import javax.net.ssl.SSLHandshakeException
import kotlin.time.Duration.Companion.seconds

internal class SlackClient(private val accessToken: String) {
    private companion object {
        private val log = LoggerFactory.getLogger(SlackClient::class.java)
    }

    fun postMessage(
        text: String,
        blocks: List<BlockElement>? = null,
        username: String = "Flexy McMetrics",
        channel: String,
        emoji: String = ":robot_face:",
        threadTs: String? = null,
        broadcast: Boolean = false,
    ): String? {
        val slackTrådParameter =
            if (threadTs != null) {
                mapOf("thread_ts" to threadTs, "reply_broadcast" to broadcast)
            } else {
                emptyMap()
            }
        val blockMap = if (blocks != null) mapOf("blocks" to blocks.serialisertTilString()) else emptyMap()
        val parameters =
            mapOf<String, Any>(
                "channel" to channel,
                "text" to text,
                "icon_emoji" to emoji,
                "username" to username,
            ) + slackTrådParameter + blockMap

        return "https://slack.com/api/chat.postMessage".post(
            objectMapper.writeValueAsString(
                parameters,
            ),
        )?.let {
            objectMapper.readTree(it)["ts"]?.asText()
        }
    }

    private fun String.post(jsonPayload: String): String? {
        var connection: HttpURLConnection? = null
        try {
            connection =
                (URI(this).toURL().openConnection() as HttpURLConnection).apply {
                    requestMethod = "POST"
                    connectTimeout = 3.seconds.inWholeMilliseconds.toInt()
                    readTimeout = 5.seconds.inWholeMilliseconds.toInt()
                    doOutput = true
                    setRequestProperty("Authorization", "Bearer $accessToken")
                    setRequestProperty("Content-Type", "application/json; charset=utf-8")
                    setRequestProperty("User-Agent", "navikt/flex-slack-metrikker")

                    outputStream.use {
                        it.bufferedWriter(Charsets.UTF_8).apply {
                            write(jsonPayload)
                            flush()
                        }
                    }
                }
            val responseCode = connection.responseCode

            if (connection.responseCode !in 200..299) {
                log.error("response from slack: code=$responseCode")
                return null
            }
            val responseBody = connection.inputStream.readText()
            log.debug("response from slack: code=$responseCode")

            return responseBody
        } catch (err: SSLHandshakeException) {
            log.error("feil ved posting til slack: {}", err.message, err)
            val ips = InetAddress.getAllByName("slack.com")
            val ip = ips.joinToString { it.hostAddress }
            log.error("SSL handshake feilet. Slack.com resolvet til: $ip")
        } catch (err: IOException) {
            log.error("feil ved posting til slack: {}", err.message, err)
        } finally {
            connection?.disconnect()
        }

        return null
    }

    private fun InputStream.readText() = use { it.bufferedReader().readText() }
}

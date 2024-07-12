package no.nav.helse.flex

fun hentEnvirnomentVariable(variable: String): String {
    return System.getenv(variable) ?: throw RuntimeException("$variable is not set")
}

fun hentEnvironment(): Environment {
    return Environment(
        slackToken = hentEnvirnomentVariable("SLACK_TOKEN"),
        dailySlackChannel = hentEnvirnomentVariable("DAILY_SLACK_CHANNEL"),
        gcpProjectId = hentEnvirnomentVariable("GCP_TEAM_PROJECT_ID"),
    )
}

data class Environment(
    val slackToken: String,
    val dailySlackChannel: String,
    val gcpProjectId: String,
)

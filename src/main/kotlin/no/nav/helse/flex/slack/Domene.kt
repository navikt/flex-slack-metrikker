package no.nav.helse.flex.slack

data class Block(
    val type: String,
)

data class Field(
    val type: String,
    val text: String,
)

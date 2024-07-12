package no.nav.helse.flex.slack

data class Block(
    override val type: String,
) : BlockElement

data class Field(
    override val type: String,
    val text: String,
) : BlockElement

data class Header(
    val text: HeaderText,
    override val type: String = "header",
) : BlockElement

data class HeaderText(
    val text: String,
    val type: String = "plain_text",
)

data class MarkdownSection(
    override val type: String = "section",
    val text: MarkdownText,
) : BlockElement

data class MarkdownText(
    val type: String = "mrkdwn",
    val text: String,
)

interface BlockElement {
    val type: String
}

data class Divider(
    override val type: String = "divider",
) : BlockElement

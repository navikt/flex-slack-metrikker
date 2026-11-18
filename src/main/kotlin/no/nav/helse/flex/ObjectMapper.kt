package no.nav.helse.flex

import tools.jackson.databind.MapperFeature
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.jsonMapper
import tools.jackson.module.kotlin.kotlinModule

val objectMapper: JsonMapper =
    jsonMapper {
        addModule(kotlinModule())
        enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
    }

fun Any.serialisertTilString(): String = objectMapper.writeValueAsString(this)

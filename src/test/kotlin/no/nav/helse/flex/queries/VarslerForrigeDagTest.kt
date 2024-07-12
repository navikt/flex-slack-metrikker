package no.nav.helse.flex.queries

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

class VarslerForrigeDagTest {
    @Test
    fun testForrigeDagsVarselerTekst() {
        mapOf(
            "VARSLET_MANGLER_INNTEKTSMELDING_FØRSTE" to 3,
            "VARSLET_MANGLER_INNTEKTSMELDING_ANDRE" to 4,
            "VARSLET_VENTER_PÅ_SAKSBEHANDLER_FØRSTE" to 5,
            "REVARSLET_VENTER_PÅ_SAKSBEHANDLER" to 6,
        ).forrigeDagsVarslerTilBlocker().text.text.trim() shouldBeEqualTo
            """
            :email: 18 varsler sendt
            
            > 6 revarslet venter på saksbehandler
            > 5 varslet venter på saksbehandler første gang
            > 4 varslet manglende inntektsmelding andre gang
            > 3 varslet manglende inntektsmelding første gang
            """.trimIndent()
    }
}

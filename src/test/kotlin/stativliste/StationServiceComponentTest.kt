package stativliste

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.common.ConsoleNotifier
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import io.kotlintest.shouldBe
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

internal class StationServiceComponentTest {

    @Test
    fun fetchStations() {
        wiremockServerItem.stubFor(get(urlEqualTo("/info"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(getResourceAsText("/information.json"))))

        wiremockServerItem.stubFor(get(urlEqualTo("/status"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(getResourceAsText("/status.json"))))

        val stationService = StationService(StationServiceConfig(
                wiremockServerItem.baseUrl() + "/info",
                wiremockServerItem.baseUrl() + "/status"
        ))

        val fetchStations = stationService.fetchStations()

        val objectMapper = ObjectMapper()
        val json = objectMapper.writeValueAsString(fetchStations)
        println(json)

        val actualJsonTree = objectMapper.readTree(json)
        val expectedJsonTree = objectMapper.readTree(getResourceAsText("/stations.json"))
        actualJsonTree.shouldBe(expectedJsonTree)
    }

    private fun getResourceAsText(s: String): String {
        return javaClass.getResource(s).readText()
    }

    companion object {
        lateinit var wiremockServerItem: WireMockServer

        @BeforeAll
        @JvmStatic
        fun initClass() {
            wiremockServerItem = WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort().notifier(ConsoleNotifier(true)))
            wiremockServerItem.start()

        }

        @AfterAll
        @JvmStatic
        fun tearDown() {
            wiremockServerItem.stop()
        }

    }


}
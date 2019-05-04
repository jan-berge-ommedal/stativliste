package stativliste

import io.kotlintest.matchers.collections.shouldHaveAtLeastSize
import io.kotlintest.matchers.collections.shouldNotBeEmpty
import org.junit.jupiter.api.Test

internal class StationServiceIntegrationTest {

    @Test
    fun fetchStations() {
        val fetchStations = StationService(StationServiceConfig()).fetchStations()
        println(fetchStations)
        fetchStations.shouldHaveAtLeastSize(200)
        fetchStations.filter { s -> s.availableLocks > 0 }.shouldNotBeEmpty()
        fetchStations.filter { s -> s.availableBikes > 0 }.shouldNotBeEmpty()
    }

}
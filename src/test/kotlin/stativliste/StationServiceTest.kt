package stativliste

import io.kotlintest.matchers.collections.shouldBeEmpty
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test

internal class StationServiceTest {

    @Test
    fun merge() {
        val stationService = StationService(StationServiceConfig())

        val info1 = StationService.InformationDTO("1", "#1")
        val info2 = StationService.InformationDTO("2", "#2")
        val status1 = StationService.StatusDTO("1", 11, 111)
        val status2 = StationService.StatusDTO("2", 22, 222)

        stationService.merge(emptyList(), emptyList()).shouldBeEmpty()
        stationService.merge(emptyList(), listOf(status1, status2)).shouldBeEmpty()
        stationService.merge(listOf(info1), emptyList()).shouldBe(listOf(Station("1", "#1", 0, 0)))
        stationService.merge(listOf(info1), listOf(status1)).shouldBe(listOf(Station("1", "#1", 11, 111)))
        stationService.merge(listOf(info1, info2), listOf(status2, status1)).shouldBe(listOf(
                Station("1", "#1", 11, 111),
                Station("2", "#2", 22, 222)
        ))
    }

}
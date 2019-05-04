package stativliste

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class Api {

    @Autowired
    private lateinit var stationService: StationService

    @GetMapping("/stations")
    internal fun api(): List<Station> {
        return stationService.fetchStations()
    }

}
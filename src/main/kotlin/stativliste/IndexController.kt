package stativliste

import arrow.core.Try
import arrow.core.handleError
import org.slf4j.LoggerFactory.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping

@Controller
class IndexController {

    private val logger = getLogger(IndexController::class.java)

    @Autowired
    private lateinit var stationService: StationService

    @GetMapping("/")
    internal fun index(model: Model): String {
        Try.invoke { stationService.fetchStations() }.handleError { e ->
            logger.error(e.message, e)
            model["error"] = "error occured when fetching stations: ${e.message}"
            emptyList()
        }
                .map { stations -> stations.sortedBy { s -> s.name } }
                .map { stations -> model["stations"] = stations }
        return "index"
    }

}
package stativliste;

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.springframework.core.ParameterizedTypeReference
import org.springframework.core.ResolvableType
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate


@Component
class StationServiceConfig(
        val informationUrl: String = "https://gbfs.urbansharing.com/oslobysykkel.no/station_information.json",
        val statusUrl: String = "https://gbfs.urbansharing.com/oslobysykkel.no/station_status.json"
)

@Component
class StationService(private val stationServiceConfig: StationServiceConfig) {

    private val restTemplate = RestTemplate()

    fun fetchStations(): List<Station> {
        val asyncInformation = fetchAsync(stationServiceConfig.informationUrl, InformationDTO::class.java)
        val asyncStatus = fetchAsync(stationServiceConfig.statusUrl, StatusDTO::class.java)
        return runBlocking { merge(asyncInformation.await(), asyncStatus.await()) }
    }

    fun merge(information: List<InformationDTO>, statuses: List<StatusDTO>): List<Station> {
        return information.map { info ->
            val status = statuses.find { status -> status.station_id == info.station_id } ?: StatusDTO()
            Station(
                    id = info.station_id,
                    name = info.name,
                    availableBikes = status.num_bikes_available,
                    availableLocks = status.num_docks_available
            )
        }
    }

    private fun <DTO> fetch(url: String, responseType: Class<DTO>): List<DTO> {
        val resolvableType = ResolvableType.forClassWithGenerics(StationsResponse::class.java, responseType)
        val parameterizedTypeReference = ParameterizedTypeReference.forType<Any>(resolvableType.type)
        val responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, parameterizedTypeReference)
        val response = responseEntity.body
        return (response as StationsResponse<DTO>).data.stations
    }

    private fun <DTO> fetchAsync(url: String, responseType: Class<DTO>) = GlobalScope.async { fetch(url, responseType) }

    class StationsResponse<DTO>(var data: StationsData<DTO> = StationsData())
    class StationsData<DTO>(var stations: List<DTO> = emptyList())

    class InformationDTO(
            var station_id: String = "",
            var name: String = ""
    )

    class StatusDTO(
            var station_id: String = "",
            var num_bikes_available: Int = 0,
            var num_docks_available: Int = 0
    )

}

const STATION_INFORMATION_URL = "https://gbfs.urbansharing.com/oslobysykkel.no/station_information.json";
const STATION_STATUS_URL = "https://gbfs.urbansharing.com/oslobysykkel.no/station_status.json";

function parseResponse(response) {
    const status = response.status;
    if (status === 200) {
        return response.json();
    } else {
        throw new Error(`Invalid HTTP status code: ${status}`);
    }
}

function fetchIt(url) {
    return fetch(url).then(parseResponse);
}

function mergeStationData(stationInformation, stationStatus) {
    const statusMap = stationStatus.data.stations.reduce((map, status) => {
        return {...map, [status.station_id]: status}
    }, {});
    const stations = stationInformation.data.stations.map(station => {
        return {...station, ...statusMap[station.station_id]};
    });
    stations.sort((a, b) => a.name.localeCompare(b.name));
    return stations;
}

export function fetchData(callback) {
    const promises = [
        STATION_INFORMATION_URL,
        STATION_STATUS_URL,
    ].map(fetchIt);

    Promise.all(promises).then(({0: stationInformation, 1: stationStatus}) => {
        callback({
            stations: mergeStationData(stationInformation, stationStatus)
        })
    }).catch((error) => {
        console && console.error && console.error(error);
        callback({
            error: "" + error
        })
    });
}
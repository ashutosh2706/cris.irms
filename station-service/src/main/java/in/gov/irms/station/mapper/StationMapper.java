package in.gov.irms.station.mapper;

import in.gov.irms.station.dto.StationResponseDTO;
import in.gov.irms.station.model.Station;

public class StationMapper {

    public static StationResponseDTO tostationResponseDto(Station station) {
        return new StationResponseDTO(
                station.getId(),
                station.getStationCode(),
                station.getStationName(),
                station.getStateName(),
                station.getDivision(),
                station.getStationType(),
                station.getElevationFromSea(),
                station.getPlatformCount(),
                station.isBoardingDisabled()
        );
    }

}

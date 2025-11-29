package in.gov.irms.station.mapper;

import in.gov.irms.station.dto.StationResponseDto;
import in.gov.irms.station.model.Station;

public class StationMapper {

    public static StationResponseDto tostationResponseDto(Station station) {
        return new StationResponseDto(
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

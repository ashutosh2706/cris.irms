package in.gov.irms.station.service;

import in.gov.irms.station.dto.StationResponseDto;
import in.gov.irms.station.exception.NoSuchStationException;

import java.util.List;

public interface StationMasterService {
    StationResponseDto getStationByStationCode(String stationCode) throws NoSuchStationException;
    List<StationResponseDto> getAllStation(String stateName);
    StationResponseDto getStationByStationId(long stationId) throws NoSuchStationException;
}

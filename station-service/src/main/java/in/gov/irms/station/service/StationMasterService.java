package in.gov.irms.station.service;

import in.gov.irms.station.dto.BulkStationDetailRequestDTO;
import in.gov.irms.station.dto.StationResponseDTO;
import in.gov.irms.station.exception.NoSuchStationException;

import java.util.List;

public interface StationMasterService {
    StationResponseDTO getStationByStationCode(String stationCode) throws NoSuchStationException;
    List<StationResponseDTO> getAllStation(String stateName);
    StationResponseDTO getStationByStationId(long stationId) throws NoSuchStationException;
    Object getBulkStationDetails(BulkStationDetailRequestDTO requestDto);
}

package in.gov.irms.station.service;

import in.gov.irms.station.dto.BulkStationDetailRequestDTO;
import in.gov.irms.station.dto.StationResponseDTO;
import in.gov.irms.station.exception.NoSuchStationException;
import in.gov.irms.station.mapper.StationMapper;
import in.gov.irms.station.repository.StationMasterRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StationMasterServiceImpl implements StationMasterService {

    private final StationMasterRepository stationMasterRepository;

    public StationMasterServiceImpl(StationMasterRepository stationMasterRepository) {
        this.stationMasterRepository = stationMasterRepository;
    }

    // add redis caching here
    @Override
    public StationResponseDTO getStationByStationCode(String stationCode) throws NoSuchStationException {
        var station = stationMasterRepository.findByStationCode(stationCode).orElseThrow(
                () -> new NoSuchStationException(String.format("No station found for code [%s]", stationCode))
        );
        return StationMapper.tostationResponseDto(station);
    }

    @Override
    public List<StationResponseDTO> getAllStation(String stateName) {
        if(stateName == null || stateName.trim().isEmpty() || stateName.trim().isBlank()) {
            return stationMasterRepository.findAll()
                    .stream()
                    .map(StationMapper::tostationResponseDto)
                    .toList();
        }
        return stationMasterRepository.findAllStationByStateName(stateName.trim().toUpperCase())
                .stream()
                .map(StationMapper::tostationResponseDto)
                .toList();
    }

    // add redis caching
    @Override
    public StationResponseDTO getStationByStationId(long stationId) throws NoSuchStationException {
        var station = stationMasterRepository.findByStationId(stationId).orElseThrow(
                () -> new NoSuchStationException(String.format("No station found for id [%s]", stationId))
        );
        return StationMapper.tostationResponseDto(station);
    }

    @Override
    public Object getBulkStationDetails(BulkStationDetailRequestDTO requestDto) {
        Map<Long, StationResponseDTO> responseDtoMap = new LinkedHashMap<>();
        Collections.sort(requestDto.stationId());
        requestDto.stationId().forEach(stationId -> {
            try {
                var stationInfo = getStationByStationId(stationId);
                responseDtoMap.put(stationId, stationInfo);
            } catch (NoSuchStationException e) {
                responseDtoMap.put(stationId, null);
            }
        });
        return responseDtoMap;
    }
}

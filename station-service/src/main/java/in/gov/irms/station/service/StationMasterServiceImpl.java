package in.gov.irms.station.service;

import in.gov.irms.station.dto.StationResponseDto;
import in.gov.irms.station.exception.NoSuchStationException;
import in.gov.irms.station.mapper.StationMapper;
import in.gov.irms.station.repository.StationMasterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationMasterServiceImpl implements StationMasterService {

    private final StationMasterRepository stationMasterRepository;

    public StationMasterServiceImpl(StationMasterRepository stationMasterRepository) {
        this.stationMasterRepository = stationMasterRepository;
    }

    @Override
    public StationResponseDto getStationByStationCode(String stationCode) throws NoSuchStationException {
        var station = stationMasterRepository.findByStationCode(stationCode).orElseThrow(
                () -> new NoSuchStationException(String.format("No station found for code [%s]", stationCode))
        );
        return StationMapper.tostationResponseDto(station);
    }

    @Override
    public List<StationResponseDto> getAllStation(String stateName) {
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
}

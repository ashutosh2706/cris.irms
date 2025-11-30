package in.gov.irms.station.controller;

import in.gov.irms.station.dto.BulkStationDetailRequestDTO;
import in.gov.irms.station.exception.NoSuchStationException;
import in.gov.irms.station.service.StationMasterService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/station")
public class StationController {

    private final StationMasterService stationMasterService;
    public StationController(StationMasterService stationMasterService) {
        this.stationMasterService = stationMasterService;
    }

    @GetMapping(value = "{stationCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByStationCode(@PathVariable(name = "stationCode") String stationCode) throws NoSuchStationException {
        var response = stationMasterService.getStationByStationCode(stationCode);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAllStation(@RequestParam(name = "state", required = false) String stateName) {
        var response = stationMasterService.getAllStation(stateName);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByStationId(@PathVariable(name = "id") long id) throws NoSuchStationException {
        var response = stationMasterService.getStationByStationId(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(
            value = "/id/bulk",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> findByBulkStationId(@Valid @RequestBody BulkStationDetailRequestDTO requestDto) {
        var response = stationMasterService.getBulkStationDetails(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

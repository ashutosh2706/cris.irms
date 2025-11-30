package in.gov.irms.train.controller;

import in.gov.irms.train.exception.InvalidTrainNumberException;
import in.gov.irms.train.exception.StationServiceException;
import in.gov.irms.train.service.TrainMasterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/train")
public class TrainMasterController {

    private final TrainMasterService trainMasterService;

    public TrainMasterController(TrainMasterService trainMasterService) {
        this.trainMasterService = trainMasterService;
    }

    @GetMapping(value = "/enquiry/{trainNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTrainInfo(@PathVariable(name = "trainNumber") Integer trainNumber) throws InvalidTrainNumberException, StationServiceException {
        var response = trainMasterService.getTrainInfo(trainNumber);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/avlClass/{trainNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAvailableClass(@PathVariable(name = "trainNumber") Integer trainNumber) throws InvalidTrainNumberException {
        var response = trainMasterService.getAvailableClass(trainNumber);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

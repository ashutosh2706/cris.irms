package in.gov.irms.train.controller;

import in.gov.irms.train.dto.DirectTrainsBtwStnReqDTO;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/train/routes")
public class RouteDetailsController {
    // find trains between stations, direct trains,
    // trains on particular days
    @GetMapping(value = "/direct", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDirectTrainsBtwStations(@Valid @RequestBody DirectTrainsBtwStnReqDTO request) {
        return ResponseEntity.ok().build();
    }
}

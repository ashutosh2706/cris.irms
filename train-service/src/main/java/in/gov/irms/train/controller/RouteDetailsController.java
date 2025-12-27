package in.gov.irms.train.controller;

import in.gov.irms.train.dto.AvlTrainsBtwStnEnquiryDTO;
import in.gov.irms.train.exception.InvalidTrainNumberException;
import in.gov.irms.train.exception.StationServiceException;
import in.gov.irms.train.service.RouteDetailService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/train/routes")
public class RouteDetailsController {
    // find trains between stations, direct trains,
    // trains on particular days

    private final RouteDetailService routeDetailService;

    public RouteDetailsController(RouteDetailService routeDetailService) {
        this.routeDetailService = routeDetailService;
    }

    @PostMapping(value = "/direct",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getDirectTrainsBtwStations(@Valid @RequestBody AvlTrainsBtwStnEnquiryDTO request)
            throws StationServiceException, InvalidTrainNumberException {
        return ResponseEntity.ok().body(routeDetailService.getDirectTrainsBtwStations(request));
    }

    @PostMapping(value = "/indirect",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getIndirectTrainsBtwStn(
            @Valid @RequestBody AvlTrainsBtwStnEnquiryDTO request,
            @RequestParam(name = "oneTransferJourney", required = false, defaultValue = "True") Boolean oneTransferJourney
    )
        throws StationServiceException, InvalidTrainNumberException {
        return ResponseEntity.ok().body(routeDetailService.getIndirectTrainBtwStn(request, oneTransferJourney));
    }
}

package in.gov.irms.train.client;

import com.fasterxml.jackson.databind.JsonNode;
import in.gov.irms.train.dto.StationServiceApi;
import in.gov.irms.train.exception.StationServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class StationServiceClient {

    private final String stationServiceBaseUrl;

    private final RestTemplate restTemplate;

    public StationServiceClient(RestTemplate restTemplate, @Value("${app.config.client.station-service-base-url}") String stationServiceBaseUrl) {
        this.restTemplate = restTemplate;
        this.stationServiceBaseUrl = stationServiceBaseUrl;
    }

    public Map<Long, StationServiceApi.StationResponseDTO> getStationDetailByBulkId(StationServiceApi.BulkStationDetailRequestDTO requestDto) throws StationServiceException {
        String requestUrl = stationServiceBaseUrl + "/id/bulk";
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setBearerAuth("");
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<StationServiceApi.BulkStationDetailRequestDTO> request = new HttpEntity<>(requestDto, headers);
        ResponseEntity<Map<Long, StationServiceApi.StationResponseDTO>> response =
                restTemplate.exchange(
                        requestUrl,
                        HttpMethod.POST,
                        request,
                        new ParameterizedTypeReference<>() {}
                );

        if(response.getStatusCode().is5xxServerError()) {
            throw new StationServiceException(String.format("Station Service failed. Status: %s", response.getStatusCode().value()));
        }

        if(response.getStatusCode().is4xxClientError()) {
            throw new StationServiceException(String.format("Station Service failed. Status: %s \n Response: %s", response.getStatusCode().value(), response.getBody()));
        }

        return response.getBody();
    }

    public StationServiceApi.StationResponseDTO getStnDetailByStnCode(String stationCode) throws StationServiceException {
        String requestUrl = stationServiceBaseUrl + "/" + stationCode;
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setBearerAuth("");
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<StationServiceApi.StationResponseDTO> response = restTemplate.exchange(
                requestUrl,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<StationServiceApi.StationResponseDTO>() {}
        );

        if(response.getStatusCode().is5xxServerError()) {
            throw new StationServiceException(String.format("Station Service failed. Status: %s", response.getStatusCode().value()));
        }

        if(response.getStatusCode().is4xxClientError()) {
            throw new StationServiceException(String.format("Station Service failed. Status: %s \n Response: %s", response.getStatusCode().value(), response.getBody()));
        }

        return response.getBody();

    }
}

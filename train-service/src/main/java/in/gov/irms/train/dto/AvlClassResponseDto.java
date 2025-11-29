package in.gov.irms.train.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AvlClassResponseDto(
        @NotBlank @NotNull
        Integer trainNumber,
        List<String> firstAC,
        List<String> secondAC,
        List<String> thirdAC,
        List<String> economyAC,
        @JsonProperty(value = "chair_car")
        List<String> secondSeater,
        List<String> sleeper,
        List<String> general,
        @JsonProperty(value = "ac_chair_car")
        List<String> chairCarAC,
        List<String> others
) {}

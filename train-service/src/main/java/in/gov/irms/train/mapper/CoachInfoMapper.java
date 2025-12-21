package in.gov.irms.train.mapper;

import in.gov.irms.train.dto.AvlClassResponseDto;
import in.gov.irms.train.model.CoachInfo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class CoachInfoMapper {

    public static AvlClassResponseDto toAvlClassResponseDTO(CoachInfo coachInfo) {
        List<String> otherCoaches = new ArrayList<>();
        if (coachInfo.getPantryCar()) otherCoaches.add("PC");
        if (coachInfo.getParcelVan()) otherCoaches.add("HCP");
        return new AvlClassResponseDto(
                coachInfo.getTrainNumber(),
                IntStream.range(1, coachInfo.getFirstAC()+1).mapToObj(i -> "H"+i).toList(),
                IntStream.range(1, coachInfo.getSecondAC()+1).mapToObj(i -> "A"+i).toList(),
                IntStream.range(1, coachInfo.getThirdAC()+1).mapToObj(i -> "B"+i).toList(),
                IntStream.range(1, coachInfo.getEconomyAC()+1).mapToObj(i -> "M"+i).toList(),
                IntStream.range(1, coachInfo.getSecondSeater()+1).mapToObj(i -> "D"+i).toList(),
                IntStream.range(1, coachInfo.getSleeper()+1).mapToObj(i -> "S"+i).toList(),
                IntStream.range(1, coachInfo.getGeneral()+1).mapToObj(i -> "GS"+i).toList(),
                IntStream.range(1, coachInfo.getChairCarAC()+1).mapToObj(i -> "C"+i).toList(),
                otherCoaches
        );
    }

    public static Map<String, List<String>> toAvlClassMap(CoachInfo coachInfo) {
        Map<String, List<String>> avlClassMap = new LinkedHashMap<>();
        var avlClassDTO = toAvlClassResponseDTO(coachInfo);
        avlClassMap.put("firstAC", avlClassDTO.firstAC());
        avlClassMap.put("secondAC", avlClassDTO.secondAC());
        avlClassMap.put("thirdAC", avlClassDTO.thirdAC());
        avlClassMap.put("economyAC", avlClassDTO.economyAC());
        avlClassMap.put("chairCarAC", avlClassDTO.chairCarAC());
        avlClassMap.put("chairCar", avlClassDTO.secondSeater());
        avlClassMap.put("sleeper", avlClassDTO.sleeper());
        avlClassMap.put("general", avlClassDTO.general());
        avlClassMap.put("others", avlClassDTO.others());
        return avlClassMap;
    }

    public static List<String> toAvlClassSimpleList(CoachInfo coachInfo) {
        List<String> list = new ArrayList<>();
        if (coachInfo.getFirstAC() > 0) list.add("1A");
        if (coachInfo.getSecondAC() > 0) list.add("2A");
        if (coachInfo.getThirdAC() > 0) list.add("3A");
        if (coachInfo.getEconomyAC() > 0) list.add("3E");
        if (coachInfo.getSleeper() > 0) list.add("SL");
        if (coachInfo.getSecondSeater() > 0) list.add("2S");
        if (coachInfo.getGeneral() > 0) list.add("GN");
        if (coachInfo.getChairCarAC() > 0) list.add("CC");
        return list;
    }
}

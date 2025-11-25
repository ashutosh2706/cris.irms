package in.gov.irms.train.service;

import in.gov.irms.train.dto.AvlClassEnquiry;
import in.gov.irms.train.exception.InvalidTrainNumberException;
import in.gov.irms.train.repository.CoachInfoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class TrainMasterServiceImpl implements TrainMasterService {

    private final CoachInfoRepository coachInfoRepository;

    public TrainMasterServiceImpl(CoachInfoRepository coachInfoRepository) {
        this.coachInfoRepository = coachInfoRepository;
    }

    @Override
    public AvlClassEnquiry getAvailableClass(Integer trainNumber) throws InvalidTrainNumberException {
        var coachInformation = coachInfoRepository.findCoachInfoByTrainNumber(trainNumber);
        if (coachInformation.isEmpty()) throw new InvalidTrainNumberException(String.format("Train Number: %s is invalid", trainNumber));
        var coachInfo = coachInformation.get();
        List<String> otherCoaches = new ArrayList<>();
        if (coachInfo.getPantryCar()) otherCoaches.add("PC");
        if (coachInfo.getParcelVan()) otherCoaches.add("HCP");
        return new AvlClassEnquiry(
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
}

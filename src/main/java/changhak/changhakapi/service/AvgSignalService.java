package changhak.changhakapi.service;

import changhak.changhakapi.domain.AvgSignal;
import changhak.changhakapi.dto.AvgSignalDTO;
import changhak.changhakapi.dto.Location;
import changhak.changhakapi.repository.AvgSignalRepository;
import changhak.changhakapi.service.logic.Signal2Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AvgSignalService {

    @Autowired
    private AvgSignalRepository avgSignalRepository;

    @Autowired
    private Signal2Location signal2Location;

    public void saveAvgSignal(AvgSignalDTO avgSignalDto) {
        AvgSignal avgSignal = new AvgSignal(avgSignalDto.getCell(), avgSignalDto.getAp(),avgSignalDto.getSignalValue());
        avgSignalRepository.save(avgSignal);
    }

    public List<AvgSignal> getByCell(Long cell) {
        return avgSignalRepository.findByCell(cell);
    }

    public List<AvgSignal> getByCellAndAp(Long cell, String ap) {
        return avgSignalRepository.findByCellAndAp(cell, ap);
    }

    public Location getPosition(Map<String, String> signals) {
        return signal2Location.calc(signals);
    }
}

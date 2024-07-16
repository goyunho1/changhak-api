package changhak.changhakapi.service;

import changhak.changhakapi.domain.AvgSignal;
import changhak.changhakapi.dto.AvgSignalDTO;
import changhak.changhakapi.dto.Location;
import changhak.changhakapi.repository.AvgSignalRepository;
import changhak.changhakapi.service.logic.Signal2Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AvgSignalService {
    private static final Logger logger = LoggerFactory.getLogger(AvgSignalService.class);

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
        logger.info("Received signals: {}", signals);
        Location location = signal2Location.calc(signals);
        logger.info("Calculated location: {}", location);
        return location;
    }
}

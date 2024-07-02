package changhak.changhakapi.service.logic;

import changhak.changhakapi.dto.Location;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
@Component
public class Signal2Location {

    public Location calc(Map<String, String> signals){
        RealTimeProcessor realTimeProcessor = new RealTimeProcessor();

        Map<String, Integer> currentSignals = new HashMap<>();
        for (Map.Entry<String, String> entry : signals.entrySet()) {
            currentSignals.put(entry.getKey(), Integer.parseInt(entry.getValue()));
        }


        String[][] filtered = realTimeProcessor.processData(currentSignals);
        double[] distance = AllDistanceCalculator.allDistance(filtered);
        double[] estimateCoordinates = LocationEstimator.estimateLoc(distance, 3);
        double latitude = estimateCoordinates[0];
        double longitude = estimateCoordinates[1];

        return new Location(latitude, longitude);
    }
}


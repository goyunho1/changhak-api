package changhak.changhakapi.service.logic;

import changhak.changhakapi.dto.Location;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Signal2Location {
    private static final Set<String> TARGET_MAC_PREFIXES = new HashSet<>(Arrays.asList("34:fc", "b4:5d"));

    public Location calc(Map<String, String> signals){
        RealTimeProcessor realTimeProcessor = new RealTimeProcessor();

        Map<String, Integer> currentSignals = new HashMap<>();
        for (Map.Entry<String, String> entry : signals.entrySet()) {
            String macAddress = entry.getKey();
            for (String prefix : TARGET_MAC_PREFIXES) {
                if (macAddress.startsWith(prefix)) {
                    currentSignals.put(macAddress, Integer.parseInt(entry.getValue()));
                }
            }
        }


        String[][] filtered = realTimeProcessor.processData(currentSignals);
        double[] distance = AllDistanceCalculator.allDistance(filtered);
        double[] estimateCoordinates = LocationEstimator.estimateLoc(distance, 3);
        double latitude = estimateCoordinates[0];
        double longitude = estimateCoordinates[1];

        return new Location(latitude, longitude);
    }
}


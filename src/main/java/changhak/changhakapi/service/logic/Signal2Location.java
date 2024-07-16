package changhak.changhakapi.service.logic;

import changhak.changhakapi.dto.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.IntStream;

@Component
public class Signal2Location {

    private static final Logger logger = LoggerFactory.getLogger(Signal2Location.class);
    public Location calc(Map<String, String> signals){
        RealTimeProcessor realTimeProcessor = new RealTimeProcessor();

        Map<String, Integer> currentSignals = new HashMap<>();
        for (Map.Entry<String, String> entry : signals.entrySet()) {
            currentSignals.put(entry.getKey(), Integer.parseInt(entry.getValue()));
        }

        String[][] filtered = realTimeProcessor.processData(currentSignals);
        logger.info("Filtered data: {}", Arrays.deepToString(filtered));

        double[] distance = AllDistanceCalculator.allDistance(filtered);
        logger.info("Calculated distances: {}", Arrays.toString(distance));

        int[] indices = IntStream.range(0, distance.length)
                .boxed()
                .sorted(Comparator.comparingDouble(i -> distance[i]))
                .mapToInt(i -> i)
                .toArray();

        int[] closestIndices = Arrays.copyOf(indices, 3);

        // closestIndices에 66을 더한 값 계산
        int[] adjustedIndices = IntStream.of(closestIndices)
                .map(i -> i + 66)
                .toArray();

        // adjustedIndices 로그 출력
        logger.info("Indices of the smallest 3 distances (adjusted): {}", Arrays.toString(adjustedIndices));



        double[] estimateCoordinates = LocationEstimator.estimateLoc(distance, 3);
        logger.info("Estimated coordinates: {}", Arrays.toString(estimateCoordinates));

        double latitude = estimateCoordinates[0];
        double longitude = estimateCoordinates[1];

        return new Location(latitude, longitude);
    }
}


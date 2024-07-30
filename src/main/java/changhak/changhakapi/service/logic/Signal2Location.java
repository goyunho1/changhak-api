package changhak.changhakapi.service.logic;

import changhak.changhakapi.dto.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.IntStream;

@Component
public class Signal2Location {

    private static final int TOP_N = 10; // 상위 N개의 신호를 선택하기 위한 값
    private static final Logger logger = LoggerFactory.getLogger(Signal2Location.class);
    public Location calc(Map<String, String> signals){
        RealTimeProcessor realTimeProcessor = new RealTimeProcessor();

        Map<String, Integer> currentSignals = new HashMap<>();
        for (Map.Entry<String, String> entry : signals.entrySet()) {
            currentSignals.put(entry.getKey(), Integer.parseInt(entry.getValue()));
        }


//      String[][] filtered = realTimeProcessor.processData(currentSignals);
//      logger.info("Filtered data: {}", Arrays.deepToString(filtered));



        // currentSignals 맵을 정렬된 상위 10개로 필터링하고, 2차원 배열로 변환
        String[][] filtered = currentSignals.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(TOP_N)
                .map(entry -> new String[]{entry.getKey(), String.valueOf(entry.getValue())})
                .toArray(String[][]::new);


        double[] distance = AllDistanceCalculator.allDistance(filtered);

        int[] indices = IntStream.range(0, distance.length)
                .boxed()
                .sorted(Comparator.comparingDouble(i -> distance[i]))
                .mapToInt(i -> i)
                .toArray();

        int[] closestIndices = Arrays.copyOf(indices, 3);

        // closestIndices에 66을 더한 값 계산
        int[] adjustedIndices = IntStream.of(closestIndices)
                .map(i -> i + 1 )
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


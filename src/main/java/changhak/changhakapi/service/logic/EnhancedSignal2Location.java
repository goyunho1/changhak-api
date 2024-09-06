package changhak.changhakapi.service.logic;

import changhak.changhakapi.dto.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.IntStream;

@Component
public class EnhancedSignal2Location {

    private static final int TOP_N = 10; // 상위 N개의 신호를 선택하기 위한 값
    private static final Logger logger = LoggerFactory.getLogger(EnhancedSignal2Location.class);

    private final KalmanFilter kalmanFilterX;
    private final KalmanFilter kalmanFilterY;
    private final LocationEstimator locationEstimator;

    @Autowired
    public EnhancedSignal2Location(LocationEstimator locationEstimator) {
        this.locationEstimator = locationEstimator;
        this.kalmanFilterX = new KalmanFilter(1, 1, 37.63221356558527, 1); // 초기값은 예시
        this.kalmanFilterY = new KalmanFilter(1, 1, 127.07946420260444, 1);

    }

    public Location calc(Map<String, String> signals) {

        Map<String, Integer> currentSignals = new HashMap<>();
        for (Map.Entry<String, String> entry : signals.entrySet()) {
            currentSignals.put(entry.getKey(), Integer.parseInt(entry.getValue()));
        }

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

        int[] adjustedIndices = IntStream.of(closestIndices)
                .map(i -> i + 1 )
                .toArray();

        // adjustedIndices 로그 출력
        logger.info("Indices of the smallest 3 distances (adjusted): {}", Arrays.toString(adjustedIndices));

        double[] estimateCoordinates = locationEstimator.estimateLoc(distance, 3, 4);
        logger.info("Estimated coordinates: {}", Arrays.toString(estimateCoordinates));


        // 칼만 필터를 이용한 위치 추정값 업데이트
        double filteredX = kalmanFilterX.update(estimateCoordinates[0]);
        double filteredY = kalmanFilterY.update(estimateCoordinates[1]);
        logger.info("Filtered coordinates: ({}, {})", filteredX, filteredY);

        double floor = estimateCoordinates[2];

        return new Location(filteredX, filteredY, floor);
    }
}

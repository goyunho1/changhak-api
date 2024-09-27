package changhak.changhakapi.service.logic;

import changhak.changhakapi.dto.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.IntStream;

@Component
public class Signal2Location {

    private static final int TOP_N = 10; // 상위 N개의 신호를 선택하기 위한 값
    private static final int K = 3;
    private static final Logger logger = LoggerFactory.getLogger(Signal2Location.class);
    private final LocationEstimator locationEstimator;
//    private final KalmanFilter kalmanFilterX;
//    private final KalmanFilter kalmanFilterY;

    @Autowired
    public Signal2Location(LocationEstimator locationEstimator) {
        this.locationEstimator = locationEstimator;
//        this.kalmanFilterX = new KalmanFilter(0.5, 1, 37.63221356558527, 1); // 초기값은 예시
//        this.kalmanFilterY = new KalmanFilter(0.5, 1, 127.07946420260444, 1);
    }

    public Location calc(Map<String, String> signals){

        Map<String, Integer> currentSignals = new HashMap<>();
        for (Map.Entry<String, String> entry : signals.entrySet()) {
            currentSignals.put(entry.getKey(), Integer.parseInt(entry.getValue()));
        }

        // currentSignals 맵을 정렬된 상위 10개로 필터링하고, 2차원 배열로 변환
        String[][] filtered = currentSignals.entrySet()                             //<key, value> Set
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())   //value 내림차순 정렬
                .limit(TOP_N)
                //여러개의 1차원 배열 [ap,rssi]을 가지는 스트림
                .map(entry -> new String[]{entry.getKey(), String.valueOf(entry.getValue())})
              //.toArray(size -> new String[size][]); 람다식으로 표현하면,
                .toArray(String[][]::new);                                          //최종적으로 2차원 배열로 변환

        double[] distance = DistanceCalculator.calcAllDistance(filtered);

        // distance.length = numCells = 144
        int[] closestIndices = IntStream.range(0, distance.length)      //0부터 distance.len-1 까지의 IntStream 생성
                .boxed()                                                //int->Integer
                .sorted(Comparator.comparingDouble(i -> distance[i]))   //distance 베열을 기준으로 정렬
                .mapToInt(i -> i)                                       //Integer->int
                .limit(K)
                .toArray();

        // 로그를 위한 배열
        int[] adjustedIndices = IntStream.of(closestIndices)
                .map(i -> i + 1 )
                .toArray();
        logger.info("Indices of the smallest 3 distances (adjusted): {}", Arrays.toString(adjustedIndices));

        double[] estimateCoordinates = locationEstimator.estimateLoc(distance, K);
        logger.info("Estimated coordinates: {}", Arrays.toString(estimateCoordinates));
        logger.info("test");

//        double filteredX = kalmanFilterX.update(estimateCoordinates[0]);
//        double filteredY = kalmanFilterY.update(estimateCoordinates[1]);
//        logger.info("Filtered coordinates: ({}, {})", filteredX, filteredY);

        double floor = estimateCoordinates[2];

        return new Location(estimateCoordinates[0], estimateCoordinates[1], floor);
    }
}
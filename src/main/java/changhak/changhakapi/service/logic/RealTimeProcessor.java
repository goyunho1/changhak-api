package changhak.changhakapi.service.logic;

import java.util.*;

public class RealTimeProcessor {
    private static final double ALPHA = 0.8; // 지수 가중 이동 평균 필터의 알파 값
    private static final int TOP_N = 10; // 상위 N개의 신호를 선택하기 위한 값


    // 각 MAC 주소에 대한 EWMA 값과 데이터 개수를 저장하는 Map
    private Map<String, Double> ewmaMap;
    private Map<String, Integer> countMap;
    private Map<String, Double> biasCorrectedMap;


    public RealTimeProcessor() {
        ewmaMap = new HashMap<>();
        countMap = new HashMap<>();
        biasCorrectedMap = new HashMap<>();
    }


    // 새 데이터 입력을 처리하는 메소드
    public String[][]  processData(Map<String, Integer > currentSignals) {
        // 입력 데이터를 바탕으로 EWMA 값을 업데이트
        Set<Map.Entry<String, Integer >> entries = currentSignals.entrySet();

        for (Map.Entry<String, Integer > entry : entries) {
            String mac = entry.getKey();
            Integer rssi = entry.getValue();

            // 기존 데이터 개수와 EWMA 값을 가져오기
            int count = countMap.getOrDefault(mac, 0);
            double ewma;

            if (count == 0) {
                // 첫 번째 값 초기화
                ewma = (1 - ALPHA) * rssi;
            } else {
                // 기존 EWMA 값 가져오기
                ewma = ewmaMap.get(mac);

                // 지수 가중 이동 평균 계산
                ewma = ewma * ALPHA + rssi * (1 - ALPHA);
            }

            count++;

            // 바이어스 보정 적용
            double biasCorrection = ewma / (1 - Math.pow(ALPHA, count));

            // 업데이트된 값을 Map에 저장
            ewmaMap.put(mac, ewma);
            countMap.put(mac, count);
            biasCorrectedMap.put(mac, biasCorrection);
        }

        // 상위 TOP_N개의 MAC 주소를 RSSI 값에 따라 정렬하여 선택
        List<Map.Entry<String, Double>> sortedList = new ArrayList<>(biasCorrectedMap.entrySet());
        sortedList.sort((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue()));

        String[][] topMacs = new String[TOP_N][2];
        for (int i = 0; i < Math.min(TOP_N, sortedList.size()); i++) {
            String mac = sortedList.get(i).getKey();
            topMacs[i][0] = mac;
            topMacs[i][1] = String.valueOf(biasCorrectedMap.get(mac));
        }

        return topMacs;
    }
}
package changhak.changhakapi.service.logic;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;

@Component
public class DistanceCalculator {
    public double[] calcAllDistance (String[][]currentSignals){

        int NumCells = 144;                                      // 셀의 개수

        String[][] allCellSignals = null;
        try {
            String csvFilePath = "/home/ubuntu/FinalDB_12.csv"; // CSV 파일 경로
            allCellSignals = CSVToArray.readCSVToArray(csvFilePath);
            //String[][] allCellSignals = {
            //    {"101", "00:11:22:33:44:55", "-65"},
            //    {"102", "66:77:88:99:AA:BB", "-70"},
            //    {"103", "FF:EE:DD:CC:BB:AA", "-80"}
            //};
        } catch(Exception e) {
            e.printStackTrace();
        }

        if (allCellSignals == null) {
            throw new RuntimeException("Failed to load cell signals from CSV file.");
        }

        double[] distances = new double[NumCells];

        for (int i = 0; i < NumCells; i++) {                            //cell 당 데이터 개수 : 20
            String[][] cellSignals = Arrays.copyOfRange(allCellSignals, i * 20, (i + 1) * 20);

            distances[i] = calcDistance(cellSignals, currentSignals);   //cellSignals => 20개의 cell, ap(mac), rssi 2차원 배열
                                                                        //currentSignals => 상위 10개 ap,rssi 2차원 배열
        }
        return distances;   //각 cell과의 distance를 담은 배열 (인덱스 0 => 1번 cell)
    }

    private static double calcDistance(String[][] cellSignals, String[][] currentSignals) {
        HashMap<String, Double> cellTable = new HashMap<>();
        for (String[] cellSignal : cellSignals) {
            cellTable.put(cellSignal[1], Double.parseDouble(cellSignal[2])); // cellTable<ap,rssi>, size = 20
        }

        double distance = 0;
        double defaultRssi = -100;
        double sumOfWeights = 0;

        for (String[] currentSignal : currentSignals) {
            String mac = currentSignal[0]; // ap 측정값
            double currentRssi = Double.parseDouble(currentSignal[1]); // rssi 측정값

            double cellRssi = cellTable.containsKey(mac) ? cellTable.get(mac) : defaultRssi;

            double weight = 1 / Math.abs(cellRssi); // cellRssi의 절댓값의 역수
            sumOfWeights += weight; // 모든 cellRssi의 절댓값의 역수의 합

            // 가중치를 적용하여 유클리드 거리 계산
            distance += weight * Math.pow(Math.abs(cellRssi - currentRssi), 2);
        }

        // 가중치를 적용하여 최종 distance 계산
        if (sumOfWeights != 0) {
            distance = Math.sqrt(distance / sumOfWeights); // 가중치의 합으로 나누고 제곱근을 취하기
        }

        return distance; // i번째 cell의 distance
    }

}


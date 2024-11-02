package changhak.changhakapi.service.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;

@Component
public class DistanceCalculator {
    private static final Logger logger = LoggerFactory.getLogger(DistanceCalculator.class);
    public double[] calcAllDistance (String[][]currentSignals){

        int NumCells = 172;                                      // 셀의 개수

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
                                                                        //currentSignals => (//상위 10개) ap,rssi 2차원 배열
        }
        return distances;   //각 cell과의 distance를 담은 배열 (인덱스 0 => 1번 cell)
    }
//
//    private static double calcDistance(String[][] cellSignals, String[][] currentSignals) {
//        HashMap<String, Double> cellTable = new HashMap<>();
//        for (String[] cellSignal : cellSignals) {
//            cellTable.put(cellSignal[1], Double.parseDouble(cellSignal[2]));     //cellTable<ap,rssi>, size = 20
//        }
//
//        double distance = 0;
//       // double defaultRssi = -1000;
//        int count = 0;
//
//        for (String[] currentSignal : currentSignals) {
//            if (count >= 7) {
//                break;  // 매칭된 AP가 10개 이상이면 루프 종료
//            }
//                                                                        //currentSignals => 측정한 rssi값 상위 10개의 ap,rssi 2차원 배열
//            String mac = currentSignal[0];                              //ap 측정값
//            double currentRssi = Double.parseDouble(currentSignal[1]);  //rssi 측정값
//
//            if (cellTable.containsKey(mac)) {
//                double cellRssi = cellTable.get(mac);
//                distance += Math.abs((cellRssi - currentRssi));
//                count ++;
//            }
//        }
//        if (count < 7) {
//            return 1000;
//        }
//
//
//        return distance;    //i번째 cell의 distance
//    }
    private static double calcDistance(String[][] cellSignals, String[][] currentSignals) {
        HashMap<String, Double> cellTable = new HashMap<>();
        for (String[] cellSignal : cellSignals) {
            cellTable.put(cellSignal[1], Double.parseDouble(cellSignal[2]));     //cellTable<ap,rssi>, size = 20
        }

        double distance = 0;
        double defaultRssi = -100;

        for (String[] currentSignal : currentSignals) {                 //currentSignals => 측정한 rssi값 상위 10개의 ap,rssi 2차원 배열
            String mac = currentSignal[0];                              //ap 측정값
            double currentRssi = Double.parseDouble(currentSignal[1]);  //rssi 측정값

            double cellRssi = cellTable.containsKey(mac) ? cellTable.get(mac) : defaultRssi;

            distance += Math.abs(cellRssi - currentRssi);
        }
        return distance;    //i번째 cell의 distance
    }
}


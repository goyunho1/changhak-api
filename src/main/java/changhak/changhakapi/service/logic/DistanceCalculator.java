package changhak.changhakapi.service.logic;

import java.util.Arrays;
import java.util.HashMap;

public class DistanceCalculator {
    public static double[] calcAllDistance (String[][]currentSignals){
        String[][] allCellSignals = null; // 변수 선언 및 초기화
        try {
            String csvFilePath = "/home/ubuntu/FinalDB_12.csv"; // CSV 파일 경로
            allCellSignals = CSVToArray.readCSVToArray(csvFilePath);
        } catch(Exception e) {
            e.printStackTrace();
        }

        if (allCellSignals == null) {
            throw new RuntimeException("Failed to load cell signals from CSV file.");
        }

        int numCells = 144; // 셀의 개수

        double[] distances = new double[numCells];

        for (int i = 0; i < numCells; i++) {
            String[][] cellSignals = Arrays.copyOfRange(allCellSignals, i * 20, (i + 1) * 20); // cell 당 데이터 개수 : 20
            distances[i] = calcDistance(cellSignals, currentSignals);
        }
        return distances;
    }

    public static double calcDistance(String[][] cellSignals, String[][] currentSignals) {
        HashMap<String, Double> cellTable = new HashMap<>();
        for (String[] cellSignal : cellSignals) {
            cellTable.put(cellSignal[1], Double.parseDouble(cellSignal[2]));
        }

        double distance = 0;
        double defaultRssi = -100;

        for (String[] currentSignal : currentSignals) {
            String mac = currentSignal[0];
            double currentRssi = Double.parseDouble(currentSignal[1]);

            double cellRssi = cellTable.containsKey(mac) ? cellTable.get(mac) : defaultRssi;

            distance += Math.abs(cellRssi - currentRssi);
        }

        return distance;
    }

}


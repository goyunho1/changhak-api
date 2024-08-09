package changhak.changhakapi.service.logic;

import java.io.IOException;
import java.util.Arrays;

public class AllDistanceCalculator {
    public static double[] allDistance (String[][]currentSignals){
        String[][] allCellSignals = null; // 변수 선언 및 초기화
        try {
            String csvFilePath = "/home/ubuntu/FinalDB_12.csv"; // CSV 파일 경로
            allCellSignals = CSVToArray2.readCSVToArray(csvFilePath);
        } catch(Exception e) {
            e.printStackTrace();
        }

        if (allCellSignals == null) {
            throw new RuntimeException("Failed to load cell signals from CSV file.");
        }

        int numCells = 134; // 셀의 개수

        double[] distances = new double[numCells];

        for (int i = 0; i < numCells; i++) {
            String[][] cellSignals = Arrays.copyOfRange(allCellSignals, i * 20, (i + 1) * 20); // cell 당 데이터 개수 : 20
            distances[i] = DistanceCalculator.calcDistance(cellSignals, currentSignals);
        }
        return distances;
    }

}


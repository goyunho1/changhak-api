package changhak.changhakapi.service.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;

@Component
public class WCalculator {
    private static final Logger logger = LoggerFactory.getLogger(WCalculator.class);
    public double[] calcAllDistance (String[][]currentSignals,double[] distances){

        int NumCells = 144;                                      // 셀의 개수

        String[][] allCellSignals = null;
        try {
            String csvFilePath = "/home/ubuntu/FinalDB_40.csv"; // CSV 파일 경로
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

        double[] w = new double[NumCells];
        double wSum = 0;
        for (int i = 0; i < NumCells; i++) {
            w[i] = 0.00001;
        }

        for (String[] currentSignal : currentSignals) {
            String mac = currentSignal[0];

            for (int i = 0; i < NumCells; i++) {
                String[][] cellSignals = Arrays.copyOfRange(allCellSignals, i * 40, (i + 1) * 40);
                for (String[] cellSignal : cellSignals) {
                    if (cellSignal[1].equals(mac)) {
                        w[i] += 1;
                    }
                }
            }
        }

        for (double v : w) {
            wSum += (1/v);
        }
        //logger.info("w: {}", Arrays.toString(w));
        double[] d2 = new double[NumCells];
        for (int i = 0; i < NumCells; i++) {
            d2[i] = ((1/w[i])* distances[i]) / wSum;
        }
        //logger.info("d2: {}", Arrays.toString(d2));
        return d2;   //각 cell과의 distance를 담은 배열 (인덱스 0 => 1번 cell)
    }

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


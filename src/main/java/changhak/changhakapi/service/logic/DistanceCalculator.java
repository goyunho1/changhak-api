package changhak.changhakapi.service.logic;

import java.util.HashMap;

public class DistanceCalculator {

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

package changhak.changhakapi.service.logic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CSVToArray {

    public static String[][] readCSVToArray(String csvFilePath) throws IOException {
        List<String[]> allCellSignalsList = new ArrayList<>();
        String line;
        String cvsSplitBy = ",";

        // 파일이 올바르게 열리지 않는 경우를 대비해 인코딩 방식을 명시적으로 지정
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath, StandardCharsets.UTF_8))) {
            // 첫 번째 줄(헤더)은 건너뛰기
            br.readLine();
            while ((line = br.readLine()) != null) {
                // 공백 또는 잘못된 형식의 라인을 처리
                if (line.trim().isEmpty()) continue;

                String[] data = line.split(cvsSplitBy);

                // 데이터의 길이가 예상보다 짧을 경우 예외 처리
                if (data.length < 4) {
                    System.err.println("Warning: Malformed line detected, skipping: " + line);
                    continue;
                }

                // 각 필드의 양끝에 있는 따옴표를 제거
                for (int i = 0; i < data.length; i++) {
                    data[i] = data[i].replaceAll("^\"|\"$", "");
                }

                // 올바른 인덱스의 데이터를 배열에 추가          cell, mac, rssi
                allCellSignalsList.add(new String[]{data[1], data[2], data[3]});

                //List<String[]> allCellSignalsList = [
                //    {"101", "00:11:22:33:44:55", "-65"},
                //    {"102", "66:77:88:99:AA:BB", "-70"},
                //    {"103", "FF:EE:DD:CC:BB:AA", "-80"}
                //];
            }
        }

        // 2차원 배열로 변환
        return allCellSignalsList.toArray(new String[0][0]);

        //String[][] allCellSignals = {
        //    {"101", "00:11:22:33:44:55", "-65"},
        //    {"102", "66:77:88:99:AA:BB", "-70"},
        //    {"103", "FF:EE:DD:CC:BB:AA", "-80"}
        //};
    }
}

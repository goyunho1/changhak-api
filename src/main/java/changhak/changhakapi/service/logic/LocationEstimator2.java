package changhak.changhakapi.service.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.IntStream;
@Service
public class LocationEstimator2 {

    private static final Logger logger = LoggerFactory.getLogger(LocationEstimator2.class);

    // 창학관 x 좌표 배열
    private static double[] x = {
            37.63266876291934, 37.63266197892042, 37.632652940543466, 37.632641653495384,
            37.63263036453167, 37.63262358238455, 37.632614547763524, 37.632605509317244,
            37.63259647657929, 37.6325851875518, 37.63257840343584, 37.63256936684416,
            37.63255807777671, 37.63255129362079, 37.63254226081594, 37.632533226087105,
            37.632521936969226, 37.63251290030045, 37.632503863618346, 37.63249482692293,
            37.632483539670694, 37.63247224856725, 37.632445224777335, 37.63241369798122,
            37.63238442556544, 37.632348391922676, 37.63231686704131, 37.632283087777296,
            37.63225156097451, 37.63221778554324, 37.63218400435744, 37.632154730013106,
            37.63212320703848, 37.632134501925584, 37.63214354050153, 37.632154831524765,
            37.6321661225327, 37.63217741352534, 37.63218870832346, 37.63219549435835,
            37.632204532840504, 37.632227049832494, 37.63221356558527, 37.63223608639586,
            37.63222034776429, 37.63224061611408, 37.63223163676263, 37.63225190130263,
            37.632242925747654, 37.6322519660641, 37.63226325502058, 37.63227229530442,
            37.6322835861329, 37.63229487884558, 37.632303917179904, 37.63231520985887,
            37.63234674053971, 37.632378267424755, 37.63241204677102, 37.63244582611598,
            37.632479603561045, 37.632508876079235, 37.632540404856556, 37.63257643475903,
            37.632607961634555, 37.632639488508794,
            37.63266876291934, 37.63266197892042, 37.632652940543466, 37.632641653495384,
            37.63263036453167, 37.63262358238455, 37.632614547763524, 37.632605509317244,
            37.63259647657929, 37.6325851875518, 37.63257840343584, 37.63256936684416,
            37.63255807777671, 37.63255129362079, 37.63254226081594, 37.632533226087105,
            37.632521936969226, 37.63251290030045, 37.632503863618346, 37.63249482692293,
            37.632483539670694, 37.63247224856725, 37.632445224777335, 37.63241369798122,
            37.63238442556544, 37.632348391922676, 37.63231686704131, 37.632283087777296,
            37.63225156097451, 37.63221778554324, 37.63218400435744, 37.632154730013106,
            37.63212320703848, 37.632134501925584, 37.63214354050153, 37.632154831524765,
            37.6321661225327, 37.63217741352534, 37.63218870832346, 37.63219549435835,
            37.632204532840504, 37.632227049832494, 37.63221356558527, 37.63223608639586,
            37.63222034776429, 37.63224061611408, 37.63223163676263, 37.63225190130263,
            37.632242925747654, 37.6322519660641, 37.63226325502058, 37.63227229530442,
            37.6322835861329, 37.63229487884558, 37.632303917179904, 37.63231520985887,
            37.63234674053971, 37.632378267424755, 37.63241204677102, 37.63244582611598,
            37.632479603561045, 37.632508876079235, 37.632540404856556, 37.63257643475903,
            37.632607961634555, 37.632639488508794,
            37.63218880566136, 37.63219333155779,
            37.63228789494212, 37.63228789494212, 37.63228789494212, 37.63228789494212,
            37.63261468671524,37.63261468671524, 37.63261468671524,
            37.6324362513899,37.6324362513899, 37.6324362513899
    };

    // 창학관 y 좌표 배열
    private static double[] y = {
            127.07916730017462, 127.07920694459204, 127.07924941886608, 127.07928622622583,
            127.07932586582939, 127.07936267795722, 127.07939948768185, 127.07944196190547,
            127.0794759393579, 127.07951557890863, 127.0795552232498, 127.0795948651813,
            127.07963450469958, 127.07967414901546, 127.07970812641501, 127.07974493605751,
            127.07978457553381, 127.07982421740742, 127.07986385927143, 127.07990350112578,
            127.07994030830831, 127.07998277997744, 127.07997425426449, 127.07996289148386,
            127.07994869888049, 127.07994016353868, 127.07992596854429, 127.07991460339166,
            127.07990324066103, 127.07988621105022, 127.07987767816871, 127.07986631787941,
            127.07984929071753, 127.0798011547545, 127.07975868084257, 127.07971620932638,
            127.07967373779728, 127.07963126625532, 127.07958313022421, 127.07954065385412,
            127.0794981798758, 127.07950953282891, 127.07946420260444, 127.07946989106877,
            127.07942739068804, 127.07943307674432, 127.07938775131763, 127.0793991018451,
            127.07934811193516, 127.07930280566582, 127.07926316625968, 127.07921785996763,
            127.07917538829601, 127.0791300843693, 127.0790876102844, 127.07904230633203,
            127.07904800422045, 127.0790593666045, 127.07907073138472, 127.07908209617528,
            127.07909629322536, 127.07911048551274, 127.07911901569645, 127.07913321516978,
            127.07914457762494, 127.07915594008972,
            127.07916730017462, 127.07920694459204, 127.07924941886608, 127.07928622622583,
            127.07932586582939, 127.07936267795722, 127.07939948768185, 127.07944196190547,
            127.0794759393579, 127.07951557890863, 127.0795552232498, 127.0795948651813,
            127.07963450469958, 127.07967414901546, 127.07970812641501, 127.07974493605751,
            127.07978457553381, 127.07982421740742, 127.07986385927143, 127.07990350112578,
            127.07994030830831, 127.07998277997744, 127.07997425426449, 127.07996289148386,
            127.07994869888049, 127.07994016353868, 127.07992596854429, 127.07991460339166,
            127.07990324066103, 127.07988621105022, 127.07987767816871, 127.07986631787941,
            127.07984929071753, 127.0798011547545, 127.07975868084257, 127.07971620932638,
            127.07967373779728, 127.07963126625532, 127.07958313022421, 127.07954065385412,
            127.0794981798758, 127.07950953282891, 127.07946420260444, 127.07946989106877,
            127.07942739068804, 127.07943307674432, 127.07938775131763, 127.0793991018451,
            127.07934811193516, 127.07930280566582, 127.07926316625968, 127.07921785996763,
            127.07917538829601, 127.0791300843693, 127.0790876102844, 127.07904230633203,
            127.07904800422045, 127.0790593666045, 127.07907073138472, 127.07908209617528,
            127.07909629322536, 127.07911048551274, 127.07911901569645, 127.07913321516978,
            127.07914457762494, 127.07915594008972,
            127.0794386860781, 127.07940753625172,
            127.07946711399507,127.07946711399507,127.07946711399507,127.07946711399507,
            127.07919273311532,127.07919273311532,127.07919273311532,
            127.07992043190616,127.07992043190616,127.07992043190616
    };
    public static double[] estimateLoc(double[] distances, int K) {
        double targetX = 37.632605509317244; // 예시 값, 원하는 특정 위치의 위도
        double targetY = 127.07944196190547; // 예시 값, 원하는 특정 위치의 경도

        int[] indices = IntStream.range(0, distances.length)
                .boxed()
                .sorted(Comparator.comparingDouble(i -> distances[i]))
                .mapToInt(i -> i)
                .toArray();

        int[] closestIndices = Arrays.copyOf(indices, K);

        double[] minDistances = new double[K];
        for (int i = 0; i < K; i++) {
            minDistances[i] = distances[closestIndices[i]];
        }

        double totalWeight = 0;
        for (double d : minDistances) {
            totalWeight += 1.0 / d;
        }

        double x_hat = 0;
        double y_hat = 0;

        for (int i = 0; i < K; i++) {
            int index = closestIndices[i];
            x_hat += x[index] * (1.0 / minDistances[i]);
            y_hat += y[index] * (1.0 / minDistances[i]);
        }

        x_hat /= totalWeight;
        y_hat /= totalWeight;

        double distanceError = Math.sqrt(Math.pow(x_hat - targetX, 2) + Math.pow(y_hat - targetY, 2));

        double actualError = distanceError * 88000;

        logger.info("actualError : {}", actualError);

        //double 배열에 담아 보낼거라서 double로 선언
        double floor = 0;
        int a = closestIndices[0] + 1;
        if (a<=132 && a>=67){
            floor = 2;
        }else if (a == 134 || a ==133 ||
                (a>=1 && a<=66)){
            floor = 1;
        }

        return new double[]{x_hat, y_hat, floor};
    }
}

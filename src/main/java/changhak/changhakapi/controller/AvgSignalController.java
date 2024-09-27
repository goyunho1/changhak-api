package changhak.changhakapi.controller;

import changhak.changhakapi.domain.AvgSignal;
import changhak.changhakapi.dto.AvgSignalDTO;
import changhak.changhakapi.dto.Location;
import changhak.changhakapi.service.AvgSignalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController //@Controller (컨트롤러 등록) + @ResponseBody ( 뷰 없을 때 <<API 용도 )
@RequestMapping("/changhak")
public class AvgSignalController {

    private final AvgSignalService avgSignalService;

    @Autowired
    AvgSignalController(AvgSignalService avgSignalService){
        this.avgSignalService = avgSignalService;
    }

    @PostMapping("/add")

    public void addAvgSignal(@RequestBody AvgSignalDTO avgSignalDTO) {
        avgSignalService.saveAvgSignal(avgSignalDTO);
    }

    // 해당 셀 신호 리스트로 get
    @GetMapping("/signals")
    public List<AvgSignal> getSignals(@RequestParam(name = "cell") Long cell) {
        return avgSignalService.getByCell(cell);
    }

    @GetMapping("/signal-values")
    public List<AvgSignal> getSignals(@RequestParam(name = "cell") Long cell,
                                      @RequestParam(name = "ap") String ap) {
        return avgSignalService.getByCellAndAp(cell, ap);
    }

    @GetMapping("/position")        //HTTP 요청 파라미터는 항상 문자열로 전달된다 ( Map<String, Integer> ==> <String, String> )
    public Location getPosition(@RequestParam Map<String, String> signals) {
        return avgSignalService.getPosition(signals);
    }
}

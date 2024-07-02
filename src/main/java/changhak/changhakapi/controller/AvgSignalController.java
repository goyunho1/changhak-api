package changhak.changhakapi.controller;

import changhak.changhakapi.dto.AvgSignalDTO;
import changhak.changhakapi.domain.AvgSignal;
import changhak.changhakapi.dto.Location;
import changhak.changhakapi.service.AvgSignalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/changhak")
public class AvgSignalController {

    @Autowired
    private AvgSignalService avgSignalService;


    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 요청 좀 더 명확한 이름으로 바꿔야 됨
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    // 저장
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

    @GetMapping("/abc")
    public long abc(){
        return 1;
    }

    @GetMapping("/position")
    public Location getPosition(@RequestParam Map<String, String> signals) {

        return avgSignalService.getPosition(signals);
    }
}

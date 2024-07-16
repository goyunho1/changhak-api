package changhak.changhakapi;

import changhak.changhakapi.repository.AvgSignalRepository;
import changhak.changhakapi.service.AvgSignalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class SpringConfig {
    private final AvgSignalRepository avgSignalRepository;

    @Autowired
    public SpringConfig(AvgSignalRepository avgSignalRepository) {
        this.avgSignalRepository = avgSignalRepository;
    }


}

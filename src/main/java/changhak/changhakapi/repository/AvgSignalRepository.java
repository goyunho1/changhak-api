package changhak.changhakapi.repository;

import changhak.changhakapi.domain.AvgSignal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvgSignalRepository extends JpaRepository<AvgSignal, Long> {
    List<AvgSignal> findByCell(Long cell);

    List<AvgSignal> findByCellAndAp(Long cell, String ap);
}

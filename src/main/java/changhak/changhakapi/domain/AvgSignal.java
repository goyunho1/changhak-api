package changhak.changhakapi.domain;

import jakarta.persistence.*;


@Entity
@Table(name = "avg_signal")
public class AvgSignal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long cell;
    private String ap;
    private Long signalValue;


    // 기본 생성자 (필수)
    public AvgSignal() {
    }

    // 필요한 경우 다른 생성자 추가
    public AvgSignal(Long cell, String ap, Long signalValue) {
        this.cell = cell;
        this.ap = ap;
        this.signalValue = signalValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCell() {
        return cell;
    }

    public void setCell(Long cell) {
        this.cell = cell;
    }

    public String getAp() {
        return ap;
    }

    public void setAp(String ap) {
        this.ap = ap;
    }

    public Long getSignalValue() {
        return signalValue;
    }

    public void setSignalValue(Long signalValue) {
        this.signalValue = signalValue;
    }
}

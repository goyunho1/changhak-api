package changhak.changhakapi.dto;

public class AvgSignalDTO {

    private Long cell;
    private String ap;
    private Long signalValue;


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

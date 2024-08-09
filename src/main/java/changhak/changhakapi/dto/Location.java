package changhak.changhakapi.dto;

public class Location {
    private double latitude;
    private double longitude;
    private double floor;

    public Location(double latitude, double longitude,  double floor) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.floor = floor;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    public double getFloor() {
        return floor;
    }

    public void setFloor(double floor) {
        this.floor = floor;
    }

}

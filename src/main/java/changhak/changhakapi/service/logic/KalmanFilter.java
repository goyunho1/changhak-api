package changhak.changhakapi.service.logic;

public class KalmanFilter {
    private double q; // Process noise covariance
    private double r; // Measurement noise covariance
    private double x; // Value
    private double p; // Estimation error covariance
    private double k; // Kalman gain

    public KalmanFilter(double q, double r, double initialEstimate, double initialErrorCovariance) {
        this.q = q;
        this.r = r;
        this.x = initialEstimate;
        this.p = initialErrorCovariance;
    }

    public double update(double measurement) {
        // Prediction update
        p += q;

        // Measurement update
        k = p / (p + r);
        x += k * (measurement - x);
        p *= (1 - k);

        return x;
    }

    public void setProcessNoiseCovariance(double q) {
        this.q = q;
    }

    public void setMeasurementNoiseCovariance(double r) {
        this.r = r;
    }
}

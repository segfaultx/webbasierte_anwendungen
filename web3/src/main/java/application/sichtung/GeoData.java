package application.sichtung;

import java.time.LocalDate;

public class GeoData {
    private LocalDate origDate;
    private double longtitude;
    private double latitude;

    public double getLatitude() {
        return latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public LocalDate getOrigDate() {
        return origDate;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public void setOrigDate(LocalDate origDate) {
        this.origDate = origDate;
    }
}

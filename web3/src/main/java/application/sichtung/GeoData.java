package application.sichtung;

import java.time.LocalDate;

/**
 * geographical information of an image
 */
public class GeoData {
    /**
     * date of creation
     */
    private LocalDate origDate;
    /**
     * longtitude position of creation location
     */
    private double longtitude;
    /**
     * latitude position of creation location
     */
    private double latitude;

    public double getLatitude() {
        return latitude;
    }

    /**
     * 
     * @return
     */
    public double getLongtitude() {
        return longtitude;
    }

    /**
     * 
     * @return
     */
    public LocalDate getOrigDate() {
        return origDate;
    }

    /**
     * 
     * @return
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * 
     * @return
     */
    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    /**
     * 
     * @return
     */
    public void setOrigDate(LocalDate origDate) {
        this.origDate = origDate;
    }
}

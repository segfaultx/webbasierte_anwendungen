package application.sichtung;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;


@Entity
public class Sichtung implements Serializable {
    @Id
    @GeneratedValue
    private long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "{date.error}")
    @NotNull(message = "{datenull.error}")
    private LocalDate date;
    @NotNull(message = "{place.error}")
    @Size(min = 3, max = 80, message = "{place.error}")
    private String place;
    @NotNull
    @Size(min = 3, max = 80, message = "{finder.error}")
    private String finder;
    @NotNull(message = "{seventeennull.error}")
    @Size(min = 3, max = 80, message = "{seventeentoosmall.error}")
    @Siebzehnhaft(message = "{seventeen.error}")
    private String description;
    @Transient
    private String[] radiobtns = {"sighting.morning", "sighting.noon", "sighting.evening"};
    @NotNull(message = "{daytime.error}")
    private String day_time;
    @Transient
    private int[] ratings = {0, 1, 2, 3, 4, 5};
    private int rating = 0;

    public Sichtung() {

    }

    public Sichtung(LocalDate date, String place, String finder, String description, String day_time, int rating) {
        this.date = date;
        this.place = place;
        this.finder = finder;
        this.description = description;
        this.day_time = day_time;
        this.rating = rating;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDay_time() {
        return day_time;
    }

    public String[] getRadiobtns() {
        return radiobtns;
    }

    public void setDay_time(String day_time) {
        this.day_time = day_time;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPlace() {
        return place;
    }

    public int[] getRatings() {
        return ratings;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setFinder(String finder) {
        this.finder = finder;
    }

    public String getFinder() {
        return finder;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRadiobtns(String[] radiobtns) {
        this.radiobtns = radiobtns;
    }

    public String getDescription() {
        return description;
    }

    public String toString() {
        return String.format("Datum: %s Ort: %s Finder: %s Beschreibung: %s", this.date, this.place, this.finder, this.description);
    }
}

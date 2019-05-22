package application.sichtung;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

public class Sichtung {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent
    @NotNull
    private LocalDate date;
    @NotNull
    @Size(min = 3, max = 80, message = "Länge zwischen {min} und {max} nicht erfüllt")
    private String place;
    @NotNull
    @Size(min = 3, max = 80)
    private String finder;
    @NotNull
    @Size(min = 3, max = 80)
    @Siebzehnhaft(message = "Wert muss siebzehnhaft sein!")
    private String description;

    private String[] radiobtns = {"morgens", "mittags", "abends"};
    @NotNull
    private String day_time;
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

    public void setDay_time(String day_time){
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

    public String getDescription() {
        return description;
    }

    public String toString() {
        return String.format("Datum: %s Ort: %s Finder: %s Beschreibung: %s", this.date, this.place, this.finder, this.description);
    }
}

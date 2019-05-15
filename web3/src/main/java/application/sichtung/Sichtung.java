package application.sichtung;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

public class Sichtung {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent
    private LocalDate date;
    private String place;
    private String finder;
    private String description;
    private String[] radiobtns = {"morgens", "mittags", "abends"};
    private String day_time;

    public Sichtung(LocalDate date, String place, String finder, String description, String day_time) {
        this.date = date;
        this.place = place;
        this.finder = finder;
        this.description = description;
        this.day_time = day_time;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
    public String getDay_time(){
        return day_time;
    }
    public String[] getRadiobtns(){
        return radiobtns;
    }
    public void set_Day_time(String day_time){
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

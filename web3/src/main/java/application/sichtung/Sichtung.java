package application.sichtung;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
/** 
 * Describes the occurance af an desireable event discovered by a human being
 * Holds information of the event itself, a description, a rating, ideally an image 
 * as well as the time and place where the event occured, and who discovered it
 */
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
    @JsonIgnore
    private String[] radiobtns = {"sighting.morning", "sighting.noon", "sighting.evening"};
    @NotNull(message = "{daytime.error}")
    private String day_time;
    @Transient
    @JsonIgnore
    private int[] ratings = {0, 1, 2, 3, 4, 5};
    private int rating = 0;

    private double longtitude;
    private double latitude;

    @OneToMany(mappedBy="sichtung", fetch=FetchType.EAGER)
    private List<Comment> commentList;

    /**
     * 
     */
    public Sichtung() {

    }

    /**
     * 
     * @param date
     * @param place
     * @param finder
     * @param description
     * @param day_time
     * @param rating
     */
    public Sichtung(LocalDate date, String place, String finder, String description, String day_time, int rating) {
        this.date = date;
        this.place = place;
        this.finder = finder;
        this.description = description;
        this.day_time = day_time;
        this.rating = rating;
    }

    /**
     * 
     * @param date
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * 
     * @return
     */
    public String getDay_time() {
        return day_time;
    }

    /**
     * 
     * @return
     */
    public String[] getRadiobtns() {
        return radiobtns;
    }

    /**
     * 
     * @param day_time
     */
    public void setDay_time(String day_time) {
        this.day_time = day_time;
    }

    /**
     * 
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * 
     * @param place
     */
    public void setPlace(String place) {
        this.place = place;
    }

    /**
     * 
     * @return
     */
    public String getPlace() {
        return place;
    }

    /**
     * 
     * @return
     */
    public int[] getRatings() {
        return ratings;
    }

    /**
     * 
     * @return
     */
    public int getRating() {
        return rating;
    }

    /**
     * 
     * @param rating
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * 
     * @param finder
     */
    public void setFinder(String finder) {
        this.finder = finder;
    }

    /**
     * 
     * @return
     */
    public String getFinder() {
        return finder;
    }

    /**
     * 
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     */
    public void setRadiobtns(String[] radiobtns) {
        this.radiobtns = radiobtns;
    }

    /**
     * 
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     * 
     */
    public String toString() {
        return String.format("Datum: %s Ort: %s Finder: %s Beschreibung: %s", this.date, this.place, this.finder, this.description);
    }

    /**
     * 
     * @return
     */
    public List<Comment> getCommentList() {
       return this.commentList;
    }

    /**image
     * 
     */
    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    /**
     * 
     * @param longtitude
     */
    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    /**
     * 
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * 
     */
    public double getLongtitude() {
        return longtitude;
    }

    /**
     * 
     */
    public double getLatitude() {
        return latitude;
    }
}

package application.sichtung;

public class Sichtung {
    private String date;
    private String place;
    private String finder;
    private String description;

    public Sichtung(String date, String place, String finder, String description){
        this.date = date;
        this.place = place;
        this.finder = finder;
        this.description = description;
    }
    public void setDate(String date){
        this.date = date;
    }

    public String getDate() {
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
    public String toString(){
        return String.format("Datum: %s Ort: %s Finder: %s Beschreibung: %s", this.date,this.place,this.finder,this.description);
    }
}

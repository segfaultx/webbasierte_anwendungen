package application.sichtung;

import java.util.ArrayList;

public class Sichtungen {
    private ArrayList<Sichtung> sichtungen;

    public Sichtungen() {
        this.sichtungen = new ArrayList<>();
    }

    public void add(Sichtung sichtung) {
        this.sichtungen.add(sichtung);
    }

    public ArrayList<Sichtung> getList() {
        return this.sichtungen;
    }
}

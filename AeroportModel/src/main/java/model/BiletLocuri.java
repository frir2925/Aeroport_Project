package model;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class BiletLocuri extends Entity<Long> {
    private Bilet bilet;
    private int locuri;

    public BiletLocuri(Bilet bilet, int locuri) {
        this.bilet = bilet;
        this.locuri = locuri;
    }

    public Bilet getBilet() {
        return bilet;
    }

    public void setBilet(Bilet bilet) {
        this.bilet = bilet;
    }

    public int getLocuri() {
        return locuri;
    }

    public void setLocuri(int locuri) {
        this.locuri = locuri;
    }
}
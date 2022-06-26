package model;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Zbor extends Entity<Long>{
    private String destinatie;
    private LocalDateTime dataOra;
    private String aeroport;
    private Integer locuri;

    public Zbor() {
    }
    public Zbor(String destinatie, String dataOra, String aeroport, Integer locuri) {
        this.destinatie = destinatie;
        //String str = "2016-03-04 11:30";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        //LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
        this.dataOra = LocalDateTime.parse(dataOra,formatter);
        this.aeroport = aeroport;
        this.locuri = locuri;
    }


    public Zbor(String destinatie, LocalDateTime dataOra, String aeroport, Integer locuri) {
        this.destinatie = destinatie;
        this.dataOra = dataOra;
        this.aeroport = aeroport;
        this.locuri = locuri;
    }

    public Long getIdZbor() {
        return super.getId();
    }

    public void setIdZbor(Long aLong) {
        super.setId(aLong);
    }

    public LocalDateTime getDataOra() { return dataOra; }

    public void setDataOra(LocalDateTime dataOra) { this.dataOra = dataOra; }

    public String getDestinatie() {
        return destinatie;
    }

    public void setDestinatie(String destinatie) {
        this.destinatie = destinatie;
    }

    public String getAeroport() {
        return aeroport;
    }

    public void setAeroport(String aeroport) {
        this.aeroport = aeroport;
    }

    public Integer getLocuri() {
        return locuri;
    }

    public void setLocuri(Integer locuri) {
        this.locuri = locuri;
    }

    @Override
    public String toString() {
        return "Zbor{" +
                "id=" + id +
                ", destinatie='" + destinatie + '\'' +
                ", dataOra=" + dataOra +
                ", aeroport='" + aeroport + '\'' +
                ", locuri=" + locuri +
                '}';
    }

}

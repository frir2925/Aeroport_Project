package model;

import java.time.LocalDateTime;

public class ZborDTO {
    private int id;
    private String destinatie;
    private LocalDateTime dataOra;
    private String aeroport;
    private Integer locuri;


    public ZborDTO(int id, String destinatie, LocalDateTime dataOra, String aeroport, Integer locuri) {
        this.id = id;
        this.destinatie = destinatie;
        this.dataOra = dataOra;
        this.aeroport = aeroport;
        this.locuri = locuri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDestinatie() {
        return destinatie;
    }

    public void setDestinatie(String destinatie) {
        this.destinatie = destinatie;
    }

    public LocalDateTime getDataOra() {
        return dataOra;
    }

    public void setDataOra(LocalDateTime dataOra) {
        this.dataOra = dataOra;
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
}

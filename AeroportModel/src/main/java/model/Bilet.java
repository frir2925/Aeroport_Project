package model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Bilet extends Entity<Long>{
    private String client;
    private List<String> turisti;
    private String adresa;
    private Integer zborId;

    public Bilet(String client, List<String> turisti, String adresa, Integer zborId) {
        this.client = client;
        this.turisti = turisti;
        this.adresa = adresa;
        this.zborId = zborId;
    }



    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public List<String> getTuristi() {
        return turisti;
    }

    public void setTuristi(List<String> turisti) {
        this.turisti = turisti;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public Integer getZborId() { return zborId; }

    public void setZborId(Integer zborId) { this.zborId = zborId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bilet)) return false;
        if (!super.equals(o)) return false;
        Bilet bilet = (Bilet) o;
        return getClient().equals(bilet.getClient()) && getTuristi().equals(bilet.getTuristi()) && getAdresa().equals(bilet.getAdresa());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getClient(), getTuristi(), getAdresa());
    }

    @Override
    public String toString() {
        return "Bilet{" +
                "client='" + client + '\'' +
                ", turisti=" + turisti +
                ", adresa='" + adresa + '\'' +
                ", zborId=" + zborId +
                ", id=" + id +
                '}';
    }
}

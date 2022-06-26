package model;
import java.io.Serializable;
import java.util.Objects;

public class Angajat extends Entity<Long>{
    private String username;
    private String password;

    public Angajat(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Angajat() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Angajat)) return false;
        if (!super.equals(o)) return false;
        Angajat angajat = (Angajat) o;
        return getUsername().equals(angajat.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getUsername());
    }

    @Override
    public String toString() {
        return "Angajat{" +
                "username='" + username + '\'' +
                ", id=" + id +
                '}';
    }

}


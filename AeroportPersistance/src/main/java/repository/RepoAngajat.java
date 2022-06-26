package repository;
import model.*;


public interface RepoAngajat extends Repository<Long, Angajat> {

    public Angajat findOneByUsername(String Username);

}
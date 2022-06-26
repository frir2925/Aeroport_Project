package services;
import model.Angajat;
import model.Bilet;
import model.Zbor;
import java.time.LocalDateTime;
import java.util.List;

public interface ISuperService {

    void login(Angajat angajat, IAeroportObserver client) throws AeroportException;

    void logout(Angajat angajat, IAeroportObserver client) throws AeroportException;

    public Iterable<Angajat> getAllAngajati()throws AeroportException;

    public Iterable<Zbor> getAllZboruri() throws AeroportException;

    public Iterable<Zbor> getZborByDateTimeDestination(LocalDateTime dateTime, String destination)throws AeroportException;
    public Angajat getAngajatByUsername(String username) throws AeroportException;

    public Zbor getZborById(Long id)throws AeroportException;

    public Bilet cumparaBilet(Long zborId, String client, String adresa, int locuri, List<String> turisti)throws AeroportException;

}

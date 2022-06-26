package aeroport.server;

import model.Angajat;
import model.Bilet;

import model.Zbor;
import repository.RepoAngajat;
import repository.RepoBilet;
import repository.RepoZbor;
import repository.database.*;
//import repository.database.AngajatDatabase;
import services.AeroportException;
import services.IAeroportObserver;
import services.ISuperService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ServicesImpl implements ISuperService {
/*
    //interfete
    private AngajatDatabase angajatRepository;
    private ZborDatabase zborRepository;
    private BiletDatabase biletRepository;
    private Map<String, IAeroportObserver> loggedAngajati;

    public ServicesImpl(AngajatDatabase angajatRepository, ZborDatabase zborRepository, BiletDatabase biletRepository) {

        this.angajatRepository = angajatRepository;
        this.zborRepository = zborRepository;
        this.biletRepository = biletRepository;
        loggedAngajati = new ConcurrentHashMap<>();;
    }


 */

    private RepoAngajat angajatRepository;
    private RepoZbor zborRepository;
    private RepoBilet biletRepository;
    private Map<String, IAeroportObserver> loggedAngajati;

    public ServicesImpl(RepoAngajat angajatRepository, RepoZbor zborRepository, RepoBilet biletRepository) {
        this.angajatRepository = angajatRepository;
        this.zborRepository = zborRepository;
        this.biletRepository = biletRepository;
        loggedAngajati = new ConcurrentHashMap<>();
    }





    @Override
    public void login(Angajat angajat, IAeroportObserver obs) throws AeroportException {
        Angajat ang = angajatRepository.findOneByUsername(angajat.getUsername());
        System.out.println(ang);
        if (ang != null){
            if(loggedAngajati.get(angajat.getUsername())!=null)
                throw new AeroportException("User already logged in.");
            loggedAngajati.put(angajat.getUsername(), obs);

        }else
            throw new AeroportException("Authentication failed.");
    }

    @Override
    public void logout(Angajat angajat, IAeroportObserver client) throws AeroportException {
        IAeroportObserver localClient=loggedAngajati.remove(angajat.getUsername());
        if (localClient==null)
            throw new AeroportException("Angajat "+angajat.getUsername()+" is not logged in.");
    }

    @Override
    public Iterable<Angajat> getAllAngajati() throws AeroportException {
        return angajatRepository.findAll();
    }

    @Override
    public Iterable<Zbor> getAllZboruri() throws AeroportException {
        return zborRepository.findAll();
    }

    @Override
    public Iterable<Zbor> getZborByDateTimeDestination(LocalDateTime dateTime, String destination) throws AeroportException {
        return zborRepository.findByDateTimeDestination(dateTime,destination);

    }

    public Angajat getAngajatByUsername(String username){
        return angajatRepository.findOneByUsername(username);
    }

    @Override
    public Zbor getZborById(Long id) throws AeroportException {
        return zborRepository.findOne(id);
    }

    @Override
    public Bilet cumparaBilet(Long zborId, String client, String adresa, int locuri, List<String> turisti) throws AeroportException {
        Zbor zbor = zborRepository.findOne(zborId);
        if (zbor == null)
            return null;
        Bilet bilet = new Bilet(client, turisti, adresa, zborId.intValue());
        biletRepository.save(bilet);

        int new_locuri=zbor.getLocuri()-locuri;
        if (new_locuri==0)
        {
            zborRepository.delete(zbor.getId());
        }
        else
        {
            Zbor newZbor = new Zbor(zbor.getDestinatie(), zbor.getDataOra(), zbor.getAeroport(), new_locuri);
            zborRepository.update(zbor.getId(), newZbor);
            notifyClients();
        }

        return bilet;
    }



//        }
//        else
//            throw new FestivalException("error");

    //}

    //    public Integer getLastID(){
//        int id = 0;
//        for(Bilet b: biletRepository.findAll())
//            if(id < b.getID())
//                id=b.getID();
//        return id;
//    }
    private final int defaultThreadsNo=5;

    public synchronized void notifyClients(){
        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);
        loggedAngajati.forEach((x, y) ->
                executor.execute(() -> {
                    try {
                        System.out.println("Updating show..." + x);
                        y.updateAvailableTickets();
                    } catch (AeroportException e) {
                        e.printStackTrace();
                    }
                }));
        executor.shutdown();
    }

}

package network.rpcprotocol;
import model.Angajat;
import model.Bilet;
import model.BiletLocuri;
import model.Zbor;
import services.AeroportException;
import services.IAeroportObserver;
import services.ISuperService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class ServicesRpcProxy implements ISuperService {
    private String host;
    private int port;

    private IAeroportObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;


    public ServicesRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<Response>();
    }


    public void logout(Angajat angajat, IAeroportObserver client) throws AeroportException {
        Angajat ang = new Angajat(angajat.getUsername(), angajat.getPassword());
        Request req=new Request.Builder().type(RequestType.LOGOUT).data(ang).build();
        sendRequest(req);
        Response response=readResponse();
        closeConnection();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new AeroportException(err);
        }
    }


    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(Request request) throws AeroportException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new AeroportException("Error sending object " + e);
        }

    }

    private Response readResponse() throws AeroportException {
        Response response = null;
        try {
            response = qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void initializeConnection() {
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }


    private void handleUpdate(Response response) throws AeroportException {
        client.updateAvailableTickets();
    }

    private boolean isUpdate(Response response){
        return response.type()== ResponseType.UPDATE_ZBOR;
    } //updats show


    @Override
    public void login(Angajat angajat, IAeroportObserver client) throws AeroportException {
        initializeConnection();
        //Angajat ang = new Angajat(angajat.getUserName(), angajat.getPassword());
        Request req = new Request.Builder().type(RequestType.LOGIN).data(angajat).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.OK){
            this.client=client;
            return;
        }
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            closeConnection();
            throw new AeroportException(err);
        }
    }


    @Override
    public Angajat getAngajatByUsername(String username) throws AeroportException {
        Request req = new Request.Builder().type(RequestType.LOGIN).data(username).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new AeroportException(err);
        }
        return (Angajat) response.data();
    }

    @Override
    public Zbor getZborById(Long id) throws AeroportException {
        Request req = new Request.Builder().type(RequestType.GET_ZBOR_BY_ID).data(id).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.GET_ZBOR_BY_ID) {
            Zbor zb = (Zbor) response.data();
            return zb;
        }
/*
        if (response.type() == ResponseType.GET_ZBOR_BY_ID) {
            String err = response.data().toString();
            throw new AeroportException(err);
        }
        return null;

 */
        return null; //
    }


    @Override
    public Iterable<Zbor> getAllZboruri() throws AeroportException {
        Request req = new Request.Builder().type(RequestType.GET_ALL_ZBORURI).build();
        sendRequest(req);
        Response response = readResponse();
        List<Zbor> zboruri;
        if (response.type() == ResponseType.GET_ALL_ZBORURI) {
            zboruri = (List<Zbor>) response.data();
            return zboruri;
        }

        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new AeroportException(err);
        }
        return null;
    }


    @Override
    public Iterable<Angajat> getAllAngajati() throws AeroportException {

        Request req = new Request.Builder().type(RequestType.GET_ALL_ANGAJATI).build();
        sendRequest(req);
        Response response = readResponse();
        List<Angajat> angajati;
        if (response.type() == ResponseType.GET_ALL_ANGAJATI) {
            angajati = (List<Angajat>) response.data();
            //return List.of(shows);
            return angajati;
        }
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new AeroportException(err);
        }
        return null;
    }



    @Override
    public Iterable<Zbor> getZborByDateTimeDestination(LocalDateTime dateTime, String destination) throws AeroportException {
        //List<Zbor> zboruri = new ArrayList<>();
        Zbor zb = new Zbor(destination, dateTime, "",0);
        //zboruri.add(zb);
        Request req = new Request.Builder().type(RequestType.FIND_TIMEDEST).data(zb).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new AeroportException(err);
        }
        return (Iterable<Zbor>) response.data();}




    @Override
    public Bilet cumparaBilet(Long zborId, String client, String adresa, int locuri, List<String> turisti) throws AeroportException {
        Bilet bilet = new Bilet(client, turisti, adresa, Math.toIntExact(zborId));
        BiletLocuri biletLocuri = new BiletLocuri(bilet,locuri);
        //Request req=new Request.Builder().type(RequestType.ADD_BILET).data(bilet).build();
        Request req=new Request.Builder().type(RequestType.ADD_BILET).data(biletLocuri).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ADD_BILET){

        }
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new AeroportException(err);
        }
        return (Bilet) response.data();
    }




    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Object response = input.readObject();
                    System.out.println("response received " + response);
                    if (isUpdate((Response)response)){
                        handleUpdate((Response)response);
                    }else{

                        try {
                            qresponses.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error " + e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error " + e);
                } catch (AeroportException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
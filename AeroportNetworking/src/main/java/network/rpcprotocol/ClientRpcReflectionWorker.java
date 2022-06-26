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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.List;


public class ClientRpcReflectionWorker implements Runnable, IAeroportObserver {
    private ISuperService server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public ClientRpcReflectionWorker(ISuperService server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                Response response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }


    private Response handleRequest(Request request){
        Response response=null;
        String handlerName="handle"+(request).type();
        System.out.println("HandlerName "+handlerName);
        try {
            Method method=this.getClass().getDeclaredMethod(handlerName, Request.class);
            response=(Response)method.invoke(this,request);
            System.out.println("Method "+ handlerName + " invoked");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return response;
    }


    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        output.writeObject(response);
        output.flush();
    }

    @Override
    public void updateAvailableTickets()  {
        Response response = new Response.Builder().type(ResponseType.UPDATE_ZBOR).build(); //up show
        System.out.println("Dam update la client");
        try {
            sendResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static Response okResponse=new Response.Builder().type(ResponseType.OK).build();


    private Response handleLOGIN(Request request){
        System.out.println("Login request ..."+request.type());

        Angajat angajat = (Angajat)request.data();

        try {
            server.login(angajat, this);
            return okResponse;
        } catch (ArithmeticException | AeroportException e) {
            connected=false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }


    private Response handleGET_ZBOR_BY_ID(Request request){
        System.out.println("Get all shows request ..."+request.type());
        try {
            Long id = (Long) request.data();
            Zbor zbor = server.getZborById(id);
            return new Response.Builder().type(ResponseType.GET_ZBOR_BY_ID).data(zbor).build();
        } catch (AeroportException e) {
            connected=false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }


    private Response handleGET_ALL_ZBORURI(Request request){
        System.out.println("Get all shows request ..."+request.type());
        try {
            Iterable<Zbor> zboruri = server.getAllZboruri();
            return new Response.Builder().type(ResponseType.GET_ALL_ZBORURI).data(zboruri).build();
        } catch (AeroportException e) {
            connected=false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }



    private Response handleFIND_TIMEDEST(Request request){
        System.out.println("Get all shows request ..."+request.type());
        try {
            Zbor zb = (Zbor) request.data();
            List<Zbor> zbor = (List<Zbor>) server.getZborByDateTimeDestination(zb.getDataOra(),zb.getDestinatie());//era zbor
            return new Response.Builder().type(ResponseType.FIND_TIMEDEST).data(zbor).build();
        } catch (AeroportException e) {
            connected=false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }






    private Response handleLOGOUT(Request request){
        System.out.println("Logout request...");
        Angajat udto=(Angajat)request.data();
        try {
            server.logout(udto, this);
            connected=false;
            return okResponse;

        } catch (AeroportException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }


    private Response handleADD_BILET(Request request){
        System.out.println("Buying tickets ...");
        BiletLocuri biletLocuri = (BiletLocuri) request.data();
        //Bilet bilet = (Bilet) request.data();
        Bilet bilet = biletLocuri.getBilet();
        try {
            server.cumparaBilet(Long.valueOf(bilet.getZborId()),bilet.getClient(),bilet.getAdresa(),biletLocuri.getLocuri(),bilet.getTuristi());
            return okResponse;
        } catch (AeroportException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }


    }





    /*

    private Response handleGET_ALL_BILETE(Request request){
        System.out.println("Get all tickets request ..."+request.type());
        try {
            Iterable<Bilet> bilets = server.getAllBilete();
            return new Response.Builder().type(ResponseType.GET_ALL_BILETE).data(bilets).build();
        } catch (FestivalException e) {
            connected=false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }




     */

}


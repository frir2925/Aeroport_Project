package network.utils;


import network.rpcprotocol.ClientRpcReflectionWorker;

import services.ISuperService;

import java.net.Socket;


public class ObjectConcurrentServer extends AbsConcurrentServer {
    private ISuperService server;
    public ObjectConcurrentServer(int port, ISuperService server) {
        super(port);
        this.server = server;
        System.out.println("Aeroport - AeroportRpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientRpcReflectionWorker worker=new ClientRpcReflectionWorker(server, client);
        Thread tw=new Thread(worker);
        return tw;
    }


}

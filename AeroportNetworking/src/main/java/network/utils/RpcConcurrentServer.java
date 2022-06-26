package network.utils;


import network.rpcprotocol.ClientRpcReflectionWorker;

import services.ISuperService;

import java.net.Socket;


public class RpcConcurrentServer extends AbsConcurrentServer {
    private ISuperService server;
    public RpcConcurrentServer(int port, ISuperService server) {
        super(port);
        this.server = server;
        System.out.println("Aeroport - RpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientRpcReflectionWorker worker=new ClientRpcReflectionWorker(server, client);

        Thread tw=new Thread(worker);
        return tw;
    }

    @Override
    public void stop(){
        System.out.println("Stopping services ...");
    }
}

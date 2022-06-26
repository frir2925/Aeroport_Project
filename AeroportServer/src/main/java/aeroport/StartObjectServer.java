package aeroport;
import aeroport.server.ServicesImpl;
import network.utils.AbstractServer;
import network.utils.ObjectConcurrentServer;
import network.utils.ServerException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import repository.database.AngajatDatabase;
import repository.database.BiletDatabase;
import repository.database.ZborDatabase;
import repository.hibernate.AngajatRepo;
import services.ISuperService;
import java.io.IOException;
import java.util.Properties;


public class StartObjectServer {
    private static int defaultPort=55555;
    private static SessionFactory sessionFactory;

///


    static void initialize(){
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try{
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();

        }catch (Exception ex){
            ex.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    static void close() {
        if (sessionFactory != null)
            sessionFactory.close();
    }

    ////


    public static void main(String[] args) {

        Properties serverProps = new Properties();
        try {
            serverProps.load(StartObjectServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find server.properties "+e);
            return;
        }
        //AngajatDatabase angajatRepo = new AngajatDatabase(serverProps);

        initialize();
        AngajatRepo angajatRepo = new AngajatRepo(serverProps, sessionFactory);

        ZborDatabase zborRepo = new ZborDatabase(serverProps);
        BiletDatabase biletRepo = new BiletDatabase(serverProps);

        ISuperService serverImpl = new ServicesImpl(angajatRepo,zborRepo,biletRepo);

        int serverPort = defaultPort;
        try {
            serverPort = Integer.parseInt(serverProps.getProperty("server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: " + serverPort);
        AbstractServer server = new ObjectConcurrentServer(serverPort, serverImpl);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }
    }
}

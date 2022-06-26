
import Controller.AngajatController;
import Controller.LogInController;
import model.Angajat;
import network.rpcprotocol.ServicesRpcProxy;
import services.ISuperService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;


public class StartRpcClientFX extends Application {
    private Stage primaryStage;

    private Angajat ang;

    private String username;
    private static int defaultChatPort = 55555;
    private static String defaultServer = "localhost";


    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartRpcClientFX.class.getResourceAsStream("/client.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find client.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("server.host", defaultServer);
        int serverPort = defaultChatPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        ISuperService server = new ServicesRpcProxy(serverIP, serverPort);

        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("login.fxml"));
        Parent root=loader.load();

        LogInController ctrl = loader.<LogInController>getController();
        ctrl.SetService(server);

        FXMLLoader cloader = new FXMLLoader(
                getClass().getClassLoader().getResource("angajat.fxml"));
        Parent croot=cloader.load();


        AngajatController showsController =
                cloader.<AngajatController>getController();
        showsController.setShowService(server);

        ctrl.setShowsController(showsController);
        ctrl.setParent(croot);


        primaryStage.setTitle("Log in:");
        primaryStage.setScene(new Scene(root, 600, 500));
        primaryStage.show();

    }

}



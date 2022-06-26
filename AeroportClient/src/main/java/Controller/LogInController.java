package Controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Angajat;
import services.AeroportException;
import services.ISuperService;

import java.io.IOException;

public class LogInController {
    private ISuperService server;

    @FXML
    private TextField textFieldUsername;

    @FXML
    private PasswordField textFieldPassword;

    private Angajat ang;

    private AngajatController angajatController;

    Parent mainParent;

    public void setParent(Parent p){
        mainParent=p;
    }

    public void SetService(ISuperService server) {
        this.server = server;
    }

    public void setShowsController (AngajatController angajatController){
        this.angajatController = angajatController;
    }

    @FXML
    public void logIn(ActionEvent actionEvent){
        String nume = textFieldUsername.getText();
        String passwd = textFieldPassword.getText();
        ang = new Angajat(nume, passwd);

        System.out.println("text");

        try{
            server.login(ang, angajatController);
            Stage stage=new Stage();
            stage.setTitle("Welcome back " + ang.getUsername() + "!");
            stage.setScene(new Scene(mainParent));


            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    try {
                        angajatController.logOut();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    System.exit(0);
                }
            });




            stage.show();
            angajatController.setAngajat(ang);
            angajatController.initialize();
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

        }   catch (AeroportException e) {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Logare esuata!","Nu exista utilizator cu acest username sau parola e gresita!");
        }



    }

    @FXML
    private void initialize() {
    }

}

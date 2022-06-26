package Controller;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Angajat;
import model.Bilet;

import model.Zbor;
import model.ZborDTO;
import services.AeroportException;
import services.IAeroportObserver;
import services.ISuperService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class AngajatController implements Initializable, IAeroportObserver {
    //implements Observer<ShowsChangeEvent>
    //private SuperService superService;
    private ISuperService server;
    private String  username;
    private Integer angajatId;

    private Angajat angajat;


    @FXML
    private TextField textBuyer;
    @FXML
    private TextField textNoTickets;

    @FXML
    private Button logOut;

    ///////


    ObservableList<Object> zboruri = FXCollections.observableArrayList();
    ObservableList<Object> zboruriFind = FXCollections.observableArrayList();

    @FXML
    private TextField textFieldDestinatie;

    @FXML
    private ListView<Object> zborList;

    @FXML
    private ListView<Object> zborListFind;

    @FXML
    private TableView<Object> tableZbor;

    @FXML
    private TableView<Object> tableZborFind;

    @FXML
    private TableColumn<Zbor, String> columnId;

    @FXML
    private DatePicker dataPentruFind;

    @FXML
    private TableColumn<Zbor, String> columnDestinatie;

    @FXML
    private TableColumn<Zbor, String> columnDataOra;

    @FXML
    private TableColumn<Zbor, String> columnAeroport;

    @FXML
    private TableColumn<Zbor, Integer> columnLocuri;

    @FXML
    private TableColumn<Zbor, String> columnId1;

    @FXML
    private TableColumn<Zbor, String> columnDestinatie1;

    @FXML
    private TableColumn<Zbor, String> columnDataOra1;

    @FXML
    private TableColumn<Zbor, String> columnAeroport1;

    @FXML
    private TableColumn<Zbor, Integer> columnLocuri1;

    @FXML
    private void close(){
        Platform.exit();
    }

    @FXML
    private TextField textFieldNumePrenumeClient;

    @FXML
    private TextField textFieldAdresaClient;

    @FXML
    private TextField textFieldIdZbor;

    @FXML
    private TextField textFieldNrLocuri;

    @FXML
    private TextField textFieldTurist1;
    @FXML
    private TextField textFieldTurist2;
    @FXML
    private TextField textFieldTurist3;
    @FXML
    private TextField textFieldTurist4;
    @FXML
    private TextField textFieldTurist5;


    ///////


    public void setShowService(ISuperService server) throws AeroportException {
        this.server = server;
        //initTabelZbor();
        //this.username = username;
        //this.angajatId = server.getAngajatByUsername(username).getID();

        //superService.addObserver(this);
        //initModel();
    }


    @FXML
    public void cumparaBilet() throws AeroportException {

        String client=textFieldNumePrenumeClient.getText();
        String adresa=textFieldAdresaClient.getText();


        if (client.equals("") || adresa.equals("") || textFieldNrLocuri.getText().equals("") || textFieldIdZbor.getText().equals(""))
        {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Eroare!","Completati toate datele!");
        }
        else {
            int locuri=Integer.valueOf(textFieldNrLocuri.getText());

            if (locuri>5)
            {
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Eroare!","Se pot cumpara cel mult 5 bulete!");
            }

            else {

                Long zborId = Long.valueOf(textFieldIdZbor.getText());
                List<String> lista1 = new ArrayList<>();
                lista1.add(textFieldTurist1.getText());
                lista1.add(textFieldTurist2.getText());
                lista1.add(textFieldTurist3.getText());
                lista1.add(textFieldTurist4.getText());
                lista1.add(textFieldTurist5.getText());
                Zbor zbor = server.getZborById(zborId);

                if (zbor == null) {
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Eroare!", "Zborul nu exista in baza de date!");
                } else {
                    if (locuri > zbor.getLocuri()) {
                        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Eroare!", "Nu sunt suficiente locuri!");
                    } else {
                        server.cumparaBilet(zborId, client, adresa, locuri, lista1);
                        initTabelZbor();
                    }
                }
            }
        }
    }


    @FXML
    private void initTabelZborFind() throws AeroportException {
        //Persoana pers = persoanaService.getPersoanaByUsername(username);
        //Iterable<Zbor> zbors = superService.getAllZboruri();
        LocalDate aDataTime2 = dataPentruFind.getValue();
        String locatie = textFieldDestinatie.getText();
        if (aDataTime2==null || locatie.equals(""))
        {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Eroare!","Nu ai completat data sau destinatia!");
        }
        else {
            LocalDateTime aDataTime3 = aDataTime2.atTime(0, 0, 0);
            LocalDateTime aDateTime1 = LocalDateTime.of(2022, Month.JULY, 15, 15, 0, 0);
            List<ZborDTO> zborListFind = StreamSupport.stream(server.getZborByDateTimeDestination(aDataTime3, locatie).spliterator(), false)
                    .map(x -> {
                        ZborDTO zbor = new ZborDTO(Math.toIntExact(x.getId()), x.getDestinatie(), x.getDataOra(), x.getAeroport(), x.getLocuri());
                        return zbor;
                    })
                    .collect(Collectors.toList());
            zboruriFind.setAll(zborListFind);
        }

    }


    private void initTabelZbor() throws AeroportException {
        //Persoana pers = persoanaService.getPersoanaByUsername(username);
        //Iterable<Zbor> zbors = superService.getAllZboruri();
        List<ZborDTO> zborList = StreamSupport.stream(server.getAllZboruri().spliterator(), false)
                .map(x-> {
                    ZborDTO zbor = new ZborDTO(Math.toIntExact(x.getId()),x.getDestinatie(),x.getDataOra(),x.getAeroport(),x.getLocuri());
                    return zbor;
                })
                .collect(Collectors.toList());
        zboruri.setAll(zborList);
        System.out.println(zborList);

    }



    @FXML
    public void initialize(){
        columnId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        columnDestinatie.setCellValueFactory(new PropertyValueFactory<>("Destinatie"));
        columnDataOra.setCellValueFactory(new PropertyValueFactory<>("DataOra"));
        columnAeroport.setCellValueFactory(new PropertyValueFactory<>("Aeroport"));
        columnLocuri.setCellValueFactory(new PropertyValueFactory<>("Locuri"));

        tableZbor.setItems(zboruri);




        columnId1.setCellValueFactory(new PropertyValueFactory<>("Id"));
        columnDestinatie1.setCellValueFactory(new PropertyValueFactory<>("Destinatie"));
        columnDataOra1.setCellValueFactory(new PropertyValueFactory<>("DataOra"));
        columnAeroport1.setCellValueFactory(new PropertyValueFactory<>("Aeroport"));
        columnLocuri1.setCellValueFactory(new PropertyValueFactory<>("Locuri"));

        tableZborFind.setItems(zboruriFind);

    }


/*
    @FXML
    private void handleBuyTickets() throws FestivalException {
        Spectacol selected = tableView.getSelectionModel().getSelectedItem(); //luam elementul selectat
        Spectacol spectacol = server.getShowById(selected.getID());

        List<Integer> ids = new ArrayList<>();
        Iterable<Bilet> bilets = server.getAllBilete();
        for (Bilet b : bilets) {
            ids.add(b.getID());
        }
        Integer idBilet = Collections.max(ids) + 1;

        if(selected!=null)
            try{
                String buyerName = textBuyer.getText();
                int noTickets = Integer.parseInt(textNoTickets.getText());
                Bilet bilet = new Bilet(idBilet, spectacol, noTickets, buyerName);
                bilet.setID(idBilet);
                server.addBilet(bilet);

                initModel();
            }
            catch(Exception | FestivalException e){
                MessageAlert.showErrorMessage(null,e.getMessage());
            }
        else{
            MessageAlert.showErrorMessage(null,"Nu ati selectat niciun spectacol!");
        }

    }

 */

    public void logOut() throws IOException {
        try {
            this.server.logout(angajat, this);
            close();
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
//        logOut.getScene().getWindow().hide();
//
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getResource("log_in.fxml"));
//        AnchorPane root = loader.load();
//        Stage stage = new Stage();
//        stage.setScene(new Scene(root, 700, 500));
//        stage.setTitle("Log in:");
//        stage.show();

    }


/*
    @FXML
    public void initModel() throws AeroportException {
        List<Object> list = StreamSupport.stream(server.getAllZboruri().spliterator(), false)
                .map(Zbor::getDestinatie)
                .collect(Collectors.toList());
        zboruri.setAll(list);
        System.out.println(list);
    }


 */
    @Override
    public void updateAvailableTickets() throws AeroportException {
        Platform.runLater(() -> {
            try {
                this.initTabelZbor();
            } catch (AeroportException e) {
                e.printStackTrace();
            }
        });

    }

    public void setAngajat(Angajat ang) throws AeroportException {
        this.angajat = ang;
        initTabelZbor();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        columnId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        columnDestinatie.setCellValueFactory(new PropertyValueFactory<>("Destinatie"));
        columnDataOra.setCellValueFactory(new PropertyValueFactory<>("DataOra"));
        columnAeroport.setCellValueFactory(new PropertyValueFactory<>("Aeroport"));
        columnLocuri.setCellValueFactory(new PropertyValueFactory<>("Locuri"));

        tableZbor.setItems(zboruri);




        columnId1.setCellValueFactory(new PropertyValueFactory<>("Id"));
        columnDestinatie1.setCellValueFactory(new PropertyValueFactory<>("Destinatie"));
        columnDataOra1.setCellValueFactory(new PropertyValueFactory<>("DataOra"));
        columnAeroport1.setCellValueFactory(new PropertyValueFactory<>("Aeroport"));
        columnLocuri1.setCellValueFactory(new PropertyValueFactory<>("Locuri"));

        tableZborFind.setItems(zboruriFind);

    }
}

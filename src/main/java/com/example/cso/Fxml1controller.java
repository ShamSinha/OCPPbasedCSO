package com.example.cso;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Fxml1controller implements Initializable {

    MyClientEndPoint myClientEndPoint = MyClientEndPoint.getInstance() ;

    @FXML public TextField serverSite ;
    @FXML public Button Connect ;
    @FXML public Button Disconnect ;
    @FXML public Button Configure ;
    @FXML public Button Refresh ;
    @FXML public Text sessionId ;

    @FXML
    TableView<ChargingStation> table = new TableView<>();


    @FXML
    TableColumn<ChargingStation, String> nameCol ;
    @FXML
    TableColumn<ChargingStation, String> locationCol ;
    @FXML
    TableColumn<ChargingStation, String> statusCol ;
    @FXML
    TableColumn<ChargingStation, String> userCol ;
    @FXML
    TableColumn<ChargingStation, String> userTransactionIdCol;

    private final ObservableList<ChargingStation> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Disconnect.setVisible(false);
        Connect.setVisible(true);

        nameCol = new TableColumn<ChargingStation, String>();
        locationCol = new TableColumn<ChargingStation, String>();
        statusCol = new TableColumn<ChargingStation, String>();
        userCol = new TableColumn<ChargingStation, String>();
        userTransactionIdCol = new TableColumn<ChargingStation, String>();


        nameCol.setCellValueFactory(
                new PropertyValueFactory<ChargingStation,String>("name")
        );
        locationCol.setCellValueFactory(
                new PropertyValueFactory<ChargingStation,String>("location")
        );
        statusCol.setCellValueFactory(
                new PropertyValueFactory<ChargingStation,String>("status")
        );
        userCol.setCellValueFactory(
                new PropertyValueFactory<ChargingStation,String>("user")
        );
        userTransactionIdCol.setCellValueFactory(
                new PropertyValueFactory<ChargingStation,String>("userTransactionId")
        );
    }

    public void add(){
        data.add(myClientEndPoint.getChargingStation());
        table.setItems(data);
    }


    @FXML protected void setRefresh(ActionEvent event) {
        add();
    }


    @FXML protected void connectButtonAction(ActionEvent event) {
        if(myClientEndPoint.connectToWebSocket(serverSite.getText())){
            Connect.setVisible(false);
            Disconnect.setVisible(true);
            sessionId.setText("Connected to Session" + myClientEndPoint.getSession().getId());
        }
    }

    @FXML protected void disconnectButtonAction(ActionEvent event) throws IOException {
        if(myClientEndPoint.disconnectToWebSocket() ) {
            Disconnect.setVisible(false);
            Connect.setVisible(true);
        }
    }

    @FXML protected void Configure(ActionEvent event) throws IOException {
        Parent configureView = FXMLLoader.load(getClass().getResource("/fxml/CSO2.fxml"));
        Scene scene = new Scene(configureView);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow() ;
        window.setScene(scene);
        window.show();
    }

}

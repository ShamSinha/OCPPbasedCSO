package com.example.cso;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Fxml2controller implements Initializable {

    @FXML public Button SetVariables ;
    @FXML public Button GetVariables ;
    @FXML public Button Reset ;
    @FXML public Button GetMessage ;
    @FXML public ChoiceBox<String> BaseReportType ;
    @FXML public Text description ;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setChoiceForBaseReport() ;
        listenChoiceBoxReport();
    }

    @FXML
    protected void buttonSetVariables(ActionEvent event) throws IOException {
        Parent configureView = FXMLLoader.load(getClass().getResource("/fxml/SetVariables.fxml"));
        Scene scene = new Scene(configureView);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow() ;
        window.setScene(scene);
        window.show();
    }

    @FXML
    protected void buttonGetVariables(ActionEvent event) throws IOException {
        Parent configureView = FXMLLoader.load(getClass().getResource("/fxml/GetVariables.fxml"));
        Scene scene = new Scene(configureView);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow() ;
        window.setScene(scene);
        window.show();
    }

    @FXML
    protected void buttonReset(ActionEvent event) throws IOException {

    }

    @FXML
    protected void setGetMessage(ActionEvent event) throws IOException {
        Parent configureView = FXMLLoader.load(getClass().getResource("/fxml/GetDisplayMessage.fxml"));
        Scene scene = new Scene(configureView);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow() ;
        window.setScene(scene);
        window.show();
    }

    public void setChoiceForBaseReport() {
        BaseReportType.setItems(FXCollections.observableArrayList("ConfigurationInventory" , "FullInventory" , "SummaryInventory")) ;
        BaseReportType.setValue("ConfigurationInventory");
    }
    public void listenChoiceBoxReport(){
        BaseReportType.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> descriptionReport(newValue) ));
    }

    public void descriptionReport(String newValue){
        if(newValue.equals("ConfigurationInventory")){
            description.setText("A (configuration) report that lists all Components/Variables that can be set by the operator.");
        }
        if(newValue.equals("FullInventory")){
            description.setText("A (full) report that lists everything except monitoring settings.");
        }
        if(newValue.equals("SummaryInventory")){
            description.setText("A (summary) report that lists Components/Variables relating to the Charging Station’s current charging availability, and to any existing problem conditions.");
        }
    }


}

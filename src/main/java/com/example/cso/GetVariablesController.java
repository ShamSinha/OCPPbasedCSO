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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GetVariablesController implements Initializable {


    @FXML
    Button addButton = new Button();
    @FXML
    ComboBox<String> componentBox1 ;
    @FXML
    ComboBox<String> variableBox1 ;
    @FXML
    ComboBox<String> attributeTypeBox1 ;

    @FXML
    Text componentDescription1 = new Text();
    @FXML
    Text variableDescription1 = new Text();

    @FXML
    TableView<GetVariableDataType> getVariableTable = new TableView<GetVariableDataType>();

    @FXML
    TableColumn<GetVariableDataType, String> componentCol1 ;
    @FXML
    TableColumn<GetVariableDataType, String> variableCol1 ;
    @FXML
    TableColumn<GetVariableDataType, String> attributeTypeCol1;
    @FXML
    TableColumn<GetVariableDataType, String> attributeValueCol1 ;


    @FXML
    protected void setBack1(ActionEvent event) throws IOException {
        Parent configureView = FXMLLoader.load(getClass().getResource("/fxml/CSO2.fxml"));
        Scene scene = new Scene(configureView);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow() ;
        window.setScene(scene);
        window.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        componentCol1 = new TableColumn<GetVariableDataType, String>();
        variableCol1 = new TableColumn<GetVariableDataType, String>();
        attributeTypeCol1 = new TableColumn<GetVariableDataType, String>();
        attributeValueCol1 = new TableColumn<GetVariableDataType, String>();


        componentCol1.setCellValueFactory(
                new PropertyValueFactory<GetVariableDataType,String>("component")
        );
        variableCol1.setCellValueFactory(
                new PropertyValueFactory<GetVariableDataType,String>("variable")
        );
        attributeTypeCol1.setCellValueFactory(
                new PropertyValueFactory<GetVariableDataType,String>("attributeType")
        );
        attributeValueCol1.setCellValueFactory(
                new PropertyValueFactory<GetVariableDataType,String>("attributeValue")
        );



        set();
    }
    @FXML
    protected void DeleteButton1(ActionEvent event) throws IOException{
        ObservableList<GetVariableDataType> variableSelected , allVariables ;
        allVariables = getVariableTable.getItems() ;
        variableSelected = getVariableTable.getSelectionModel().getSelectedItems() ;

        variableSelected.forEach(allVariables::remove);

    }



    @FXML
    protected void AddButton1(ActionEvent event) throws IOException {
        getVariableTable.getItems().addAll(new GetVariableDataType(componentBox1.getValue(),variableBox1.getValue(),attributeTypeBox1.getValue(),""));

        componentBox1.getSelectionModel().clearSelection();
        variableBox1.getSelectionModel().clearSelection();
        attributeTypeBox1.getSelectionModel().clearSelection();

    }

    private void set(){
        componentBox1.setItems(FXCollections.observableArrayList("AuthCtrlr" , "ClockCtrlr" ,"SecurityCtrlr" ,"TxCtrlr","DisplayMessageCtrlr","ReservationCtrlr","OCPPCommCtrlr" ,"TariffCostCtrlr" ,"DeviceDataCtrlr","SampledDataCtrlr")) ;
        componentBox1.setVisibleRowCount(4);

        componentBox1.setOnAction(event -> {
            if(!componentBox1.getSelectionModel().isEmpty()) {
                switch (componentBox1.getValue()) {
                    case "AuthCtrlr":
                        variableBox1.setItems(FXCollections.observableArrayList("Enabled", "AuthorizeRemoteStart", "LocalPreAuthorize"));
                        break;
                    case "SecurityCtrlr":
                        variableBox1.setItems(FXCollections.observableArrayList("Identity", "BasicAuthPassword", "OrganizationName", "CertificateEntries", "SecurityProfile"));
                        break;
                    case "ClockCtrlr":
                        variableBox1.setItems(FXCollections.observableArrayList("DateTime", "TimeOffset", "TimeSource", "TimeZone"));
                        break;
                    case "TxCtrlr":
                        variableBox1.setItems(FXCollections.observableArrayList("ChargingBeforeAcceptedEnabled", "EVConnectionTimeOut", "StopTxOnEVSideDisconnect", "TxBeforeAcceptedEnabled", "MaxEnergyOnInvalidId", "StopTxOnInvalid", "TxStartPoint", "TxStopPoint"));
                        break;
                    case "DisplayMessageCtrlr":
                        variableBox1.setItems(FXCollections.observableArrayList("Enabled", "Available", "DisplayMessages", "PersonalMessageSize", "SupportedFormats", "SupportedPriorities"));
                        break;
                    case "ReservationCtrlr":
                        variableBox1.setItems(FXCollections.observableArrayList("Enabled", "Available", "NonEvseSpecific"));
                        break;
                    case "OCPPCommCtrlr":
                        variableBox1.setItems(FXCollections.observableArrayList("HeartbeatInterval", "RetryBackOffRepeatTimes", "RetryBackOffRandomRange", "RetryBackOffWaitMinimum", "WebSocketPingInterval", "DefaultMessageTimeout", "ResetRetries", "UnlockOnEVSideDisconnect"));
                        break;
                    case "TariffCostCtrlr":
                        variableBox1.setItems(FXCollections.observableArrayList("TariffFallbackMessage", "TotalCostFallbackMessage", "Currency"));
                        break;
                    case "SampledDataCtrlr":
                        variableBox1.setItems(FXCollections.observableArrayList("Enabled", "Available", "SignReadings", "TxEndedInterval", "TxUpdatedInterval", "TxEndedMeasurands", "TxStartedMeasurands", "TxUpdatedMeasurands"));
                        break;
                    case "DeviceDataCtrlr":
                        variableBox1.setItems(FXCollections.observableArrayList("BytesPerMessage", "ItemsPerMessage", "ValueSize"));
                        break;

                }
            }
            variableBox1.setVisibleRowCount(4);

            if (componentBox1.getSelectionModel().isEmpty()){
                componentDescription1.setText("");
            }
            if (!componentBox1.getSelectionModel().isEmpty()) {
                setDescription(componentBox1.getValue());
            }
        });



        variableBox1.setOnAction(event -> {
            if(!variableBox1.getSelectionModel().isEmpty()) {
                setDescription(variableBox1.getValue());
                attributeTypeBox1.setItems(FXCollections.observableArrayList("Actual","Target","Minset","MaxSet"));

            }
            if (variableBox1.getSelectionModel().isEmpty()){
                variableDescription1.setText("");
                variableBox1.setItems(FXCollections.observableArrayList());
                attributeTypeBox1.setItems(FXCollections.observableArrayList());
            }
        });


    }

    private void setDescription(String s){
        if(s.equals("ClockCtrlr") ){componentDescription1.setText("ClockCtrlr: Provides a means to configure management of time tracking by Charging Station.");}

        if (s.equals("DeviceDataCtrlr")){
            componentDescription1.setText("DeviceDataCtrlr: Responsible for configuration relating to the exchange and storage of Charging Station device model data.");
        }
        if (s.equals("OCPPCommCtrlr")){
            componentDescription1.setText("OCPPCommCtrlr: Responsible for configuration relating to information exchange between Charging Station and CSMS.");}

        if (s.equals("SecurityCtrlr")){
            componentDescription1.setText("SecurityCtrlr: Responsible for configuration relating to security of communications between Charging Station and CSMS.");}

        if(s.equals("TxCtrlr")){
            componentDescription1.setText("TxCtrlr: Responsible for configuration relating to transaction characteristics and behaviour.");}
        if(s.equals("AuthCtrlr")){
            componentDescription1.setText("AuthCtrlr: Responsible for configuration relating to the use of authorization for Charging Station use.");}

        if(s.equals("ReservationCtrlr")){
            componentDescription1.setText("ReservationCtrlr: Responsible for configuration relating to reservations.");}
        if(s.equals("DisplayMessageCtrlr")){
            componentDescription1.setText("DisplayMessageCtrlr: Responsible for configuration relating to the display of messages to Charging Station users.");}

        if(s.equals("TariffCostCtrlr")){
            componentDescription1.setText("TariffCostCtrlr: Responsible for configuration relating to tariff and cost display.");}
    }
}

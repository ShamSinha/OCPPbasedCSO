package com.example.cso;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

public class SetVariablesController implements Initializable {


    @FXML
    Button addButton = new Button();
    @FXML
    ComboBox<String> componentBox ;
    @FXML
    ComboBox<String> variableBox ;
    @FXML
    ComboBox<String> attributeTypeBox ;
    @FXML
    TextField attributeValueField = new TextField() ;
    @FXML
    Text componentDescription = new Text();
    @FXML
    Text variableDescription = new Text();

    @FXML
    TableView<SetVariableDataType> setVariableTable = new TableView<SetVariableDataType>();

    @FXML
    TableColumn<SetVariableDataType, String> componentCol ;
    @FXML
    TableColumn<SetVariableDataType, String> variableCol ;
    @FXML
    TableColumn<SetVariableDataType, String> attributeTypeCol;
    @FXML
    TableColumn<SetVariableDataType, String> attributeValueCol ;

    private final ObservableList<SetVariableDataType> variableData = FXCollections.observableArrayList();

    @FXML
    protected void setBack(ActionEvent event) throws IOException {
        Parent configureView = FXMLLoader.load(getClass().getResource("/fxml/CSO2.fxml"));
        Scene scene = new Scene(configureView);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow() ;
        window.setScene(scene);
        window.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        final ObservableList<SetVariableDataType> variableData = FXCollections.observableArrayList();

        componentCol = new TableColumn<SetVariableDataType, String>();
        variableCol = new TableColumn<SetVariableDataType, String>();
        attributeTypeCol = new TableColumn<SetVariableDataType, String>();
        attributeValueCol = new TableColumn<SetVariableDataType, String>();


        componentCol.setCellValueFactory(
                new PropertyValueFactory<SetVariableDataType,String>("component")
        );
        variableCol.setCellValueFactory(
                new PropertyValueFactory<SetVariableDataType,String>("variable")
        );
        attributeTypeCol.setCellValueFactory(
                new PropertyValueFactory<SetVariableDataType,String>("attributeType")
        );
        attributeValueCol.setCellValueFactory(
                new PropertyValueFactory<SetVariableDataType,String>("attributeValue")
        );



        set();
    }
    @FXML
    protected void DeleteButton(ActionEvent event) throws IOException{
        ObservableList<SetVariableDataType> variableSelected , allVariables ;
        allVariables = setVariableTable.getItems() ;
        variableSelected = setVariableTable.getSelectionModel().getSelectedItems() ;

        variableSelected.forEach(allVariables::remove);

    }



    @FXML
    protected void AddButton(ActionEvent event) throws IOException {
        variableData.add(new SetVariableDataType(componentBox.getValue(),variableBox.getValue(),attributeTypeBox.getValue(),attributeValueField.getText())) ;
        setVariableTable.setItems(variableData);

        componentBox.getSelectionModel().clearSelection();
        variableBox.getSelectionModel().clearSelection();
        attributeTypeBox.getSelectionModel().clearSelection();
        attributeValueField.clear();

    }

    private void set(){
        componentBox.setItems(FXCollections.observableArrayList("AuthCtrlr" , "ClockCtrlr" ,"SecurityCtrlr" ,"TxCtrlr","DisplayMessageCtrlr","ReservationCtrlr","OCPPCommCtrlr" ,"TariffCostCtrlr" ,"DeviceDataCtrlr","SampledDataCtrlr")) ;
        componentBox.setVisibleRowCount(4);

        componentBox.setOnAction(event -> {
            if(!componentBox.getSelectionModel().isEmpty()) {
                switch (componentBox.getValue()) {
                    case "AuthCtrlr":
                        variableBox.setItems(FXCollections.observableArrayList("Enabled", "AuthorizeRemoteStart", "LocalPreAuthorize"));
                        break;
                    case "SecurityCtrlr":
                        variableBox.setItems(FXCollections.observableArrayList("Identity", "BasicAuthPassword", "OrganizationName", "CertificateEntries", "SecurityProfile"));
                        break;
                    case "ClockCtrlr":
                        variableBox.setItems(FXCollections.observableArrayList("DateTime", "TimeOffset", "TimeSource", "TimeZone"));
                        break;
                    case "TxCtrlr":
                        variableBox.setItems(FXCollections.observableArrayList("ChargingBeforeAcceptedEnabled", "EVConnectionTimeOut", "StopTxOnEVSideDisconnect", "TxBeforeAcceptedEnabled", "MaxEnergyOnInvalidId", "StopTxOnInvalid", "TxStartPoint", "TxStopPoint"));
                        break;
                    case "DisplayMessageCtrlr":
                        variableBox.setItems(FXCollections.observableArrayList("Enabled", "Available", "DisplayMessages", "PersonalMessageSize", "SupportedFormats", "SupportedPriorities"));
                        break;
                    case "ReservationCtrlr":
                        variableBox.setItems(FXCollections.observableArrayList("Enabled", "Available", "NonEvseSpecific"));
                        break;
                    case "OCPPCommCtrlr":
                        variableBox.setItems(FXCollections.observableArrayList("HeartbeatInterval", "RetryBackOffRepeatTimes", "RetryBackOffRandomRange", "RetryBackOffWaitMinimum", "WebSocketPingInterval", "DefaultMessageTimeout", "ResetRetries", "UnlockOnEVSideDisconnect"));
                        break;
                    case "TariffCostCtrlr":
                        variableBox.setItems(FXCollections.observableArrayList("TariffFallbackMessage", "TotalCostFallbackMessage", "Currency"));
                        break;
                    case "SampledDataCtrlr":
                        variableBox.setItems(FXCollections.observableArrayList("Enabled", "Available", "SignReadings", "TxEndedInterval", "TxUpdatedInterval", "TxEndedMeasurands", "TxStartedMeasurands", "TxUpdatedMeasurands"));
                        break;
                    case "DeviceDataCtrlr":
                        variableBox.setItems(FXCollections.observableArrayList("BytesPerMessage", "ItemsPerMessage", "ValueSize"));
                        break;

                }
            }
            variableBox.setVisibleRowCount(4);

            if (componentBox.getSelectionModel().isEmpty()){
                componentDescription.setText("");
            }
            if (!componentBox.getSelectionModel().isEmpty()) {
                setDescription(componentBox.getValue());
            }
        });



        variableBox.setOnAction(event -> {
            if(!variableBox.getSelectionModel().isEmpty()) {
                setDescription(variableBox.getValue());
                attributeTypeBox.setItems(FXCollections.observableArrayList("Actual","Target","Minset","MaxSet"));

            }
            if (variableBox.getSelectionModel().isEmpty()){
                variableDescription.setText("");
                variableBox.setItems(FXCollections.observableArrayList());
                attributeTypeBox.setItems(FXCollections.observableArrayList());
            }
        });


    }

    private void setDescription(String s){
        if(s.equals("ClockCtrlr") ){componentDescription.setText("ClockCtrlr: Provides a means to configure management of time tracking by Charging Station.");}

        if (s.equals("DeviceDataCtrlr")){
            componentDescription.setText("DeviceDataCtrlr: Responsible for configuration relating to the exchange and storage of Charging Station device model data.");
        }
        if (s.equals("OCPPCommCtrlr")){
            componentDescription.setText("OCPPCommCtrlr: Responsible for configuration relating to information exchange between Charging Station and CSMS.");}

        if (s.equals("SecurityCtrlr")){
            componentDescription.setText("SecurityCtrlr: Responsible for configuration relating to security of communications between Charging Station and CSMS.");}

        if(s.equals("TxCtrlr")){
            componentDescription.setText("TxCtrlr: Responsible for configuration relating to transaction characteristics and behaviour.");}
        if(s.equals("AuthCtrlr")){
            componentDescription.setText("AuthCtrlr: Responsible for configuration relating to the use of authorization for Charging Station use.");}

        if(s.equals("ReservationCtrlr")){
            componentDescription.setText("ReservationCtrlr: Responsible for configuration relating to reservations.");}
        if(s.equals("DisplayMessageCtrlr")){
            componentDescription.setText("DisplayMessageCtrlr: Responsible for configuration relating to the display of messages to Charging Station users.");}

        if(s.equals("TariffCostCtrlr")){
            componentDescription.setText("TariffCostCtrlr: Responsible for configuration relating to tariff and cost display.");}
    }
}

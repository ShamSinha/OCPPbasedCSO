package com.example.cso;

import javafx.application.Platform;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


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
    @FXML public TextField rfidTokenField ;
    @FXML public TextField rfidUserField ;
    @FXML public ComboBox<String> rfidTypeBox ;
    @FXML public ComboBox<String> rfidStatusBox ;
    @FXML public Button AddRfid ;
    @FXML public Button DeleteRfid ;
    @FXML public Button RefreshRfid ;
    @FXML public Text rfidStatusText ;

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

    @FXML
    TableView<RfidUser> rfidTable = new TableView<>();
    @FXML
    TableColumn<RfidUser, String> rfidTokenCol;
    @FXML
    TableColumn<RfidUser, String> rfidTypeCol;
    @FXML
    TableColumn<RfidUser, String> rfidUserCol;
    @FXML
    TableColumn<RfidUser, String> rfidStatusCol;

    private final ObservableList<ChargingStation> data = FXCollections.observableArrayList();
    private final ObservableList<RfidUser> rfidUsers = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (serverSite.getText() == null || serverSite.getText().trim().length() == 0) {
            serverSite.setText("ws://localhost:8080/CSMSWebsocketServer-1/CSO");
        }
        Disconnect.setVisible(false);
        Connect.setVisible(true);

        nameCol.setCellValueFactory(
                new PropertyValueFactory<ChargingStation,String>("name")
        );
        locationCol.setCellValueFactory(
                new PropertyValueFactory<ChargingStation,String>("location")
        );
        statusCol.setCellValueFactory(
                new PropertyValueFactory<ChargingStation,String>("operationalStatus")
        );
        userCol.setCellValueFactory(
                new PropertyValueFactory<ChargingStation,String>("user")
        );
        userTransactionIdCol.setCellValueFactory(
                new PropertyValueFactory<ChargingStation,String>("userTransactionId")
        );

        rfidTokenCol.setCellValueFactory(new PropertyValueFactory<RfidUser,String>("idToken"));
        rfidTypeCol.setCellValueFactory(new PropertyValueFactory<RfidUser,String>("tokenType"));
        rfidUserCol.setCellValueFactory(new PropertyValueFactory<RfidUser,String>("userName"));
        rfidStatusCol.setCellValueFactory(new PropertyValueFactory<RfidUser,String>("status"));
        rfidTable.setItems(rfidUsers);

        rfidTypeBox.setItems(FXCollections.observableArrayList("ISO14443", "KeyCode"));
        rfidTypeBox.setValue("ISO14443");
        rfidStatusBox.setItems(FXCollections.observableArrayList("Accepted", "Blocked", "Expired", "Invalid", "NoCredit"));
        rfidStatusBox.setValue("Accepted");

        myClientEndPoint.setMessageListener(new CsoMessageListener() {
            @Override
            public void onMessage(final JSONObject message) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        handleCsoMessage(message);
                    }
                });
            }
        });
    }

    public void add(){
        if (myClientEndPoint.getChargingStation() != null) {
            data.add(myClientEndPoint.getChargingStation());
            table.setItems(data);
        }
    }


    @FXML protected void setRefresh(ActionEvent event) {
        add();
    }


    @FXML protected void connectButtonAction(ActionEvent event) {
        if(myClientEndPoint.connectToWebSocket(serverSite.getText())){
            Connect.setVisible(false);
            Disconnect.setVisible(true);
            sessionId.setText("Connected to Session" + myClientEndPoint.getSession().getId());
            setRfidStatus("Connected. Loading RFID users...");
            requestRfidUsers();
        }
    }

    @FXML protected void disconnectButtonAction(ActionEvent event) throws IOException {
        if(myClientEndPoint.disconnectToWebSocket() ) {
            Disconnect.setVisible(false);
            Connect.setVisible(true);
        }
    }

    @FXML protected void setAddRfid(ActionEvent event) {
        if (rfidTokenField.getText() == null || rfidTokenField.getText().trim().length() == 0) {
            setRfidStatus("Enter an RFID UID or PIN.");
            return;
        }
        if (!myClientEndPoint.isConnected()) {
            setRfidStatus("Connect to CSMS first.");
            return;
        }
        try {
            myClientEndPoint.upsertRfidUser(
                    rfidTokenField.getText(),
                    rfidTypeBox.getValue(),
                    rfidUserField.getText(),
                    rfidStatusBox.getValue());
            setRfidStatus("Saving token...");
        } catch (IOException e) {
            setRfidStatus(e.getMessage());
        }
    }

    @FXML protected void setDeleteRfid(ActionEvent event) {
        String token = rfidTokenField.getText();
        RfidUser selected = rfidTable.getSelectionModel().getSelectedItem();
        if ((token == null || token.trim().length() == 0) && selected != null) {
            token = selected.getIdToken();
        }
        if (token == null || token.trim().length() == 0) {
            setRfidStatus("Select or enter a token to delete.");
            return;
        }
        try {
            myClientEndPoint.deleteRfidUser(token);
            setRfidStatus("Deleting token...");
        } catch (IOException e) {
            setRfidStatus(e.getMessage());
        }
    }

    @FXML protected void setRefreshRfid(ActionEvent event) {
        requestRfidUsers();
    }

    private void requestRfidUsers() {
        try {
            myClientEndPoint.requestRfidUsers();
        } catch (IOException e) {
            setRfidStatus(e.getMessage());
        }
    }

    private void handleCsoMessage(JSONObject message) {
        String type = message.optString("type");
        if ("RfidUserResult".equals(type)) {
            setRfidStatus(message.optString("message"));
            return;
        }
        if ("RfidUsers".equals(type)) {
            updateRfidUsers(message);
            return;
        }
        if (CsoOcppStatus.isOcppMessage(message)) {
            setRfidStatus(CsoOcppStatus.text(message));
        }
    }

    private void updateRfidUsers(JSONObject message) {
        rfidUsers.clear();
        try {
            JSONArray users = message.getJSONArray("users");
            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                rfidUsers.add(new RfidUser(
                        user.optString("idToken"),
                        user.optString("tokenType"),
                        user.optString("userName"),
                        user.optString("status"),
                        user.optString("updatedAt")));
            }
            setRfidStatus("Loaded " + users.length() + " RFID users.");
        } catch (JSONException e) {
            setRfidStatus("Could not read RFID list.");
        }
    }

    private void setRfidStatus(String message) {
        rfidStatusText.setText(message == null ? "" : message);
    }

    @FXML protected void Configure(ActionEvent event) throws IOException {
        Parent configureView = FXMLLoader.load(getClass().getResource("/fxml/CSO2.fxml"));
        Scene scene = new Scene(configureView);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow() ;
        window.setScene(scene);
        window.show();
    }

}

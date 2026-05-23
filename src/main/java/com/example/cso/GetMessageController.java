package com.example.cso;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GetMessageController implements Initializable {

    @FXML public Button Back ;
    @FXML public Button GetMessage ;
    @FXML public Button Replace ;
    @FXML public Button Clear ;
    @FXML public RadioButton MessageId ;
    @FXML public RadioButton Priority ;
    @FXML public RadioButton State ;
    ToggleGroup group = new ToggleGroup();

    @FXML public ComboBox<String> comboBox ;
    @FXML public Text description ;
    @FXML public Text statusText ;
    Toggle toggle ;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        MessageId.setToggleGroup(group);
        Priority.setToggleGroup(group);
        State.setToggleGroup(group);
        group.selectedToggleProperty().addListener(((observable, oldValue, newValue) -> processRadioButton(newValue)));
        listenComboBox();
        group.selectToggle(MessageId);
        listenForOcppResponses();

    }

    public void processRadioButton(Toggle togglenew){
        toggle = togglenew ;
        if(togglenew == MessageId ){
            MessageId.requestFocus();
            comboBox.setItems(FXCollections.observableArrayList("Get All Messages","1","2","3","4","5","6","7","8","9","10")) ;
            comboBox.setVisibleRowCount(4);
            comboBox.getSelectionModel().clearSelection();
            comboBox.show();
            comboBox.setPromptText("Please Select MessageId");


        }
        if(togglenew == Priority){
            Priority.requestFocus();
            comboBox.setItems(FXCollections.observableArrayList("AlwaysFront","InFront","NormalCycle")) ;
            comboBox.getSelectionModel().clearSelection();
            comboBox.show();
            comboBox.setPromptText("Please Select Priority");

        }
        if(togglenew == State){
            State.requestFocus();
            comboBox.setItems(FXCollections.observableArrayList("Charging" , "Faulted", "Idle" , "Unavailable")) ;
            comboBox.getSelectionModel().clearSelection();
            comboBox.show();
            comboBox.setPromptText("Please Select State");
        }


    }
    public void listenComboBox(){

        EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(comboBox.getValue() != null ) {
                    setDescription(comboBox.getValue());
                }

                if (comboBox.getSelectionModel().isEmpty()){
                    description.setText("");
                }
            }
        };
        comboBox.setOnAction(eventHandler);

    }

    public void setDescription(String value){

        if (value.equals("AlwaysFront")) {
            description.setText("Show this message always in front. Highest priority, do not cycle with other messages. When a newer message with this MessagePriority is received, this message is replaced. No Charging Station own message may override this message.");
        }
        if (value.equals("InFront")) {
            description.setText("Show this message in front of the normal cycle of messages. When more messages with this priority are to be shown, they SHALL be cycled.");
        }
        if (value.equals("NormalCycle")) {
            description.setText("Show this message in the cycle of messages.");
        }


        if (value.equals("Charging")){
            description.setText("Message only to be shown while the Charging Station is charging.");
        }
        if (value.equals("Faulted")){
            description.setText("Message only to be shown while the Charging Station is in faulted state.");
        }
        if (value.equals("Idle")){
            description.setText("Message only to be shown while the Charging Station is idle(not charging).");
        }
        if (value.equals("Unavailable")){
            description.setText("Message only to be shown while the Charging Station is in unavailable state.");
        }

        if(value.equals("Get All Messages")){
            description.setText("Request all the installed DisplayMessages configured via OCPP in a Charging Station.");
        }
        if (value.equals("1") | value.equals("2") | value.equals("3") |value.equals("4") | value.equals("5") | value.equals("6")|value.equals("7") | value.equals("8") | value.equals("9")|value.equals("10")){
            description.setText("Request Display Messages of Id ="+ value + " which is installed in a Charging Station");
        }


    }

    @FXML
    protected void setBack(ActionEvent event) throws IOException {
        Parent configureView = FXMLLoader.load(getClass().getResource("/fxml/CSO2.fxml"));
        Scene scene = new Scene(configureView);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow() ;
        window.setScene(scene);
        window.show();
    }

    @FXML
    protected void setReplace(ActionEvent event) throws IOException {
        Parent configureView = FXMLLoader.load(getClass().getResource("/fxml/SetDisplayMessage.fxml"));
        Scene scene = new Scene(configureView);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow() ;
        window.setScene(scene);
        window.show();
    }

    @FXML
    protected void setClear(ActionEvent event) throws IOException {
        setStatus("ClearDisplayMessage is not wired in this basic build.");
    }

    @FXML
    protected void setGetMessage(ActionEvent event) throws IOException {
        String value = comboBox.getValue();
        if (value == null || value.trim().length() == 0) {
            setStatus("Choose a display-message filter.");
            return;
        }

        try {
            JSONObject request = new JSONObject()
                    .put("requestId", (int) (System.currentTimeMillis() & 0x7fffffff));
            if (toggle == MessageId && !"Get All Messages".equals(value)) {
                request.put("id", Integer.parseInt(value));
            } else if (toggle == Priority) {
                request.put("priority", value);
            } else if (toggle == State) {
                request.put("state", value);
            }

            MyClientEndPoint.getInstance().sendGetDisplayMessages(request);
            setStatus("Sent GetDisplayMessages request.");
        } catch (JSONException | IOException | NumberFormatException e) {
            setStatus(e.getMessage());
        }
    }

    private void setStatus(String message) {
        statusText.setText(message == null ? "" : message);
    }

    private void listenForOcppResponses() {
        MyClientEndPoint.getInstance().setMessageListener(new CsoMessageListener() {
            @Override
            public void onMessage(final JSONObject message) {
                if (!CsoOcppStatus.isOcppMessage(message)) {
                    return;
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        setStatus(CsoOcppStatus.text(message));
                    }
                });
            }
        });
    }

}

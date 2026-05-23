package com.example.cso;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SetMessageController implements Initializable {

    @FXML private TextField messageIdField;
    @FXML private ComboBox<String> priorityBox;
    @FXML private ComboBox<String> stateBox;
    @FXML private ComboBox<String> formatBox;
    @FXML private TextField languageField;
    @FXML private TextField startField;
    @FXML private TextField endField;
    @FXML private TextField transactionField;
    @FXML private TextArea contentArea;
    @FXML private Text statusText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        priorityBox.setItems(FXCollections.observableArrayList("AlwaysFront", "InFront", "NormalCycle"));
        priorityBox.setValue("AlwaysFront");
        stateBox.setItems(FXCollections.observableArrayList("Charging", "Faulted", "Idle", "Unavailable"));
        stateBox.setValue("Idle");
        formatBox.setItems(FXCollections.observableArrayList("UTF8", "ASCII", "HTML", "URI"));
        formatBox.setValue("UTF8");
        messageIdField.setText("1");
        languageField.setText("en");
        contentArea.setText("Welcome to charger");
        listenForOcppResponses();
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
    protected void setSendMessage(ActionEvent event) {
        if (isBlank(contentArea.getText())) {
            setStatus("Enter message content.");
            return;
        }

        try {
            MyClientEndPoint.getInstance().sendSetDisplayMessage(payload());
            setStatus("Sent SetDisplayMessage to CS01.");
        } catch (JSONException | IOException | NumberFormatException e) {
            setStatus(e.getMessage());
        }
    }

    private JSONObject payload() throws JSONException {
        String endDateTime = value(endField.getText());
        return new JSONObject()
                .put("id", Integer.parseInt(messageIdField.getText().trim()))
                .put("priority", priorityBox.getValue())
                .put("state", stateBox.getValue())
                .put("startDateTime", value(startField.getText()))
                .put("endDateTime", endDateTime)
                .put("endDataTime", endDateTime)
                .put("transactionId", value(transactionField.getText()))
                .put("message", new JSONObject()
                        .put("format", formatBox.getValue())
                        .put("language", value(languageField.getText()))
                        .put("content", value(contentArea.getText())));
    }

    private String value(String text) {
        return text == null ? "" : text.trim();
    }

    private boolean isBlank(String text) {
        return text == null || text.trim().length() == 0;
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

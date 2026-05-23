package com.example.cso;

import org.glassfish.tyrus.client.ClientManager;
import org.json.JSONException;
import org.json.JSONObject ;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

@ClientEndpoint(
        decoders = {MessageDecoder.class},
        encoders = {MessageEncoder.class},
        subprotocols = {"CSOProtocol"}
)

public class MyClientEndPoint {

    private Session session ;
    private static final MyClientEndPoint instance = new MyClientEndPoint(); // Eagerly Loading of single ton instance
    ChargingStation chargingStation;
    private CsoMessageListener messageListener;

    private MyClientEndPoint(){
        // private to prevent anyone else from instantiating
    }

    public static MyClientEndPoint getInstance(){
        return instance;
    }

    public boolean connectToWebSocket(String s)  {

        //WebSocketContainer container = ContainerProvider.getWebSocketContainer();

        URI uri = URI.create(s);
        ClientManager client = new ClientManager();

        try {
            session = client.connectToServer(this,uri) ;
        } catch (DeploymentException | IOException e) {
            e.printStackTrace();
        }


        /*try {
            session = container.connectToServer(this, uri);

        } catch (DeploymentException | IOException e) {
            e.printStackTrace();

        }*/
        return session != null;
    }

    public boolean disconnectToWebSocket() throws IOException {

        if (session != null) {
            session.close();
            session = null;
        }
        return true;
    }

    @OnOpen
    public void onOpen(Session session){

    }

    @OnMessage
    public void onMessage(Session session ,JSONObject jsonObject) throws JSONException {
        if (messageListener != null) {
            messageListener.onMessage(jsonObject);
            return;
        }

        if (!jsonObject.has("name")) {
            return;
        }

        if (chargingStation == null) {
            chargingStation = new ChargingStation("", "", "", "", "");
        }
        chargingStation.setName(jsonObject.getString("name"));
        chargingStation.setLocation(jsonObject.optString("location", ""));
        chargingStation.setOperationalStatus(jsonObject.optString("operationalStatus", ""));
        chargingStation.setUser(jsonObject.optString("user", ""));
        chargingStation.setUserTransactionId(jsonObject.optString("userTransactionId", ""));
        chargingStation = new ChargingStation(chargingStation.getName(), chargingStation.getLocation(), chargingStation.getOperationalStatus() , chargingStation.getUser(), chargingStation.getUserTransactionId());

    }

    @OnClose
    public void onClose(Session session){
        this.session = null;
    }

    public Session getSession() {
        return session;
    }

    public ChargingStation getChargingStation() {
        return chargingStation;
    }

    public void setMessageListener(CsoMessageListener messageListener) {
        this.messageListener = messageListener;
    }

    public boolean isConnected() {
        return session != null && session.isOpen();
    }

    public void requestRfidUsers() throws IOException {
        try {
            send(new JSONObject().put("type", "RfidUsersList"));
        } catch (JSONException e) {
            throw new IOException("Could not build RFID list request.", e);
        }
    }

    public void upsertRfidUser(String idToken, String tokenType, String userName, String status) throws IOException {
        try {
            send(new JSONObject()
                    .put("type", "RfidUserUpsert")
                    .put("idToken", idToken)
                    .put("tokenType", tokenType)
                    .put("userName", userName)
                    .put("status", status));
        } catch (JSONException e) {
            throw new IOException("Could not build RFID save request.", e);
        }
    }

    public void deleteRfidUser(String idToken) throws IOException {
        try {
            send(new JSONObject()
                    .put("type", "RfidUserDelete")
                    .put("idToken", idToken));
        } catch (JSONException e) {
            throw new IOException("Could not build RFID delete request.", e);
        }
    }

    private void send(JSONObject object) throws IOException {
        if (!isConnected()) {
            throw new IOException("Not connected to CSMS.");
        }
        session.getBasicRemote().sendText(object.toString());
    }
}

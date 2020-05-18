package com.example.cso;

import org.json.JSONException;
import org.json.JSONObject ;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

@ClientEndpoint(
        decoders = {MessageDecoder.class},
        encoders = {MessageEncoder.class}

)

public class MyClientEndPoint {

    private Session session ;
    private static final MyClientEndPoint instance = new MyClientEndPoint(); // Eagerly Loading of single ton instance
    ChargingStation chargingStation;

    private MyClientEndPoint(){
        // private to prevent anyone else from instantiating
    }

    public static MyClientEndPoint getInstance(){
        return instance;
    }

    public boolean connectToWebSocket(String s) {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();

        URI uri = URI.create(s);
        try {
            session = container.connectToServer(this, uri);

        } catch (DeploymentException | IOException e) {
            e.printStackTrace();

        }
        return session != null;
    }

    public boolean disconnectToWebSocket() throws IOException {

        session.close();
        return getSession() == null;
    }

    @OnOpen
    public void onOpen(Session session){

    }

    @OnMessage
    public void onMessage(Session session ,JSONObject jsonObject) throws JSONException {

        chargingStation.setName(jsonObject.getString("name"));
        chargingStation.setLocation(jsonObject.getString("location"));
        chargingStation.setOperationalStatus(jsonObject.getString("operationalStatus"));
        chargingStation.setUser(jsonObject.getString("user"));
        chargingStation.setUserTransactionId(jsonObject.getString("userTransactionId"));
        chargingStation = new ChargingStation(chargingStation.getName(), chargingStation.getLocation(), chargingStation.getOperationalStatus() , chargingStation.getUser(), chargingStation.getUserTransactionId());

    }

    @OnClose
    private void onClose(Session session){

    }

    public Session getSession() {
        return session;
    }

    public ChargingStation getChargingStation() {
        return chargingStation;
    }
}

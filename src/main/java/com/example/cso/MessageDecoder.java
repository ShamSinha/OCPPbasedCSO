package com.example.cso;

import org.json.JSONException;
import org.json.JSONObject;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MessageDecoder implements Decoder.Text<JSONObject> {

    @Override
    public JSONObject decode(String message) throws DecodeException {
        try {
            return new JSONObject(message);
        } catch (JSONException e) {
            throw new DecodeException(message, "Invalid CSO JSON message", e);
        }
    }

    @Override
    public boolean willDecode(String message) {
        if (message == null || !message.trim().startsWith("{")) {
            return false;
        }
        try {
            new JSONObject(message);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }
}

package com.example.cso;

import org.json.JSONObject;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<JSONObject> {
    @Override
    public String encode(JSONObject object) throws EncodeException {
        return object == null ? "{}" : object.toString();
    }

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }
}

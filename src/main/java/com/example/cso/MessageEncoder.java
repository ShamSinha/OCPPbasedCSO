package com.example.cso;

import org.json.JSONObject;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.nio.ByteBuffer;

public class MessageEncoder implements Encoder.Binary<JSONObject> {
    @Override
    public ByteBuffer encode(JSONObject object) throws EncodeException {
        return null;
    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }
}

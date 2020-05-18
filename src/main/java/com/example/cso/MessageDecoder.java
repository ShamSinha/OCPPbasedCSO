package com.example.cso;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class MessageDecoder implements Decoder.Binary<JSONObject> {

    @Override
    public JSONObject decode(ByteBuffer bytes) throws DecodeException {
        String converted = null;
        JSONObject json = new JSONObject();
        try {
            converted = new String(bytes.array(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JSONParser parser = new JSONParser();
        try {
            json = (JSONObject) parser.parse(converted);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return json ;

    }

    @Override
    public boolean willDecode(ByteBuffer bytes) {
        return true;
    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }
}

package com.example.cso;

import org.json.JSONObject;

public interface CsoMessageListener {
    void onMessage(JSONObject message);
}

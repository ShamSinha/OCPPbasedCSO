package com.example.cso;

import org.json.JSONObject;

final class CsoOcppStatus {
    private CsoOcppStatus() {
    }

    static boolean isOcppMessage(JSONObject message) {
        String type = message.optString("type");
        return "OcppForwardResult".equals(type)
                || "OcppCallResult".equals(type)
                || "OcppCallError".equals(type)
                || "OcppNotifyDisplayMessages".equals(type);
    }

    static String text(JSONObject message) {
        String text = message.optString("message", message.optString("type"));
        JSONObject payload = message.optJSONObject("payload");
        if (payload != null && payload.length() > 0) {
            text = text + " " + payload.toString();
        }
        return text;
    }
}

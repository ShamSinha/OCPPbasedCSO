package com.example.cso;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.HandshakeResponse;
import java.util.List;
import java.util.Map;

public class ClientConfigurator extends ClientEndpointConfig.Configurator {

    public ClientConfigurator() {
        super();
    }

    @Override
    public void beforeRequest(Map<String, List<String>> headers) {
        super.beforeRequest(headers);
    }

    @Override
    public void afterResponse(HandshakeResponse hr) {
        super.afterResponse(hr);
    }
}

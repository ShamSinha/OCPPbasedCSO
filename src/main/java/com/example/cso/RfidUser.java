package com.example.cso;

import javafx.beans.property.SimpleStringProperty;

public class RfidUser {
    private final SimpleStringProperty idToken;
    private final SimpleStringProperty tokenType;
    private final SimpleStringProperty userName;
    private final SimpleStringProperty status;
    private final SimpleStringProperty updatedAt;

    public RfidUser(String idToken, String tokenType, String userName, String status, String updatedAt) {
        this.idToken = new SimpleStringProperty(idToken);
        this.tokenType = new SimpleStringProperty(tokenType);
        this.userName = new SimpleStringProperty(userName);
        this.status = new SimpleStringProperty(status);
        this.updatedAt = new SimpleStringProperty(updatedAt);
    }

    public String getIdToken() {
        return idToken.get();
    }

    public SimpleStringProperty idTokenProperty() {
        return idToken;
    }

    public String getTokenType() {
        return tokenType.get();
    }

    public SimpleStringProperty tokenTypeProperty() {
        return tokenType;
    }

    public String getUserName() {
        return userName.get();
    }

    public SimpleStringProperty userNameProperty() {
        return userName;
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public String getUpdatedAt() {
        return updatedAt.get();
    }

    public SimpleStringProperty updatedAtProperty() {
        return updatedAt;
    }
}

package com.example.cso;

import javafx.beans.property.SimpleStringProperty;

public class ChargingStation {

    private final SimpleStringProperty name;
    private final SimpleStringProperty location;
    private final SimpleStringProperty operationalStatus;
    private final SimpleStringProperty user;
    private final SimpleStringProperty userTransactionId;

    public ChargingStation(String name, String location, String operationalStatus, String user, String userTransactionId) {
        this.name = new SimpleStringProperty(name);
        this.location = new SimpleStringProperty(location);
        this.operationalStatus = new SimpleStringProperty(operationalStatus);
        this.user = new SimpleStringProperty(user);
        this.userTransactionId = new SimpleStringProperty(userTransactionId);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getLocation() {
        return location.get();
    }

    public SimpleStringProperty locationProperty() {
        return location;
    }

    public String getOperationalStatus() {
        return operationalStatus.get();
    }

    public SimpleStringProperty operationalStatusProperty() {
        return operationalStatus;
    }

    public String getUser() {
        return user.get();
    }

    public SimpleStringProperty userProperty() {
        return user;
    }

    public String getUserTransactionId() {
        return userTransactionId.get();
    }

    public SimpleStringProperty userTransactionIdProperty() {
        return userTransactionId;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public void setOperationalStatus(String operationalStatus) {
        this.operationalStatus.set(operationalStatus);
    }

    public void setUser(String user) {
        this.user.set(user);
    }

    public void setUserTransactionId(String userTransactionId) {
        this.userTransactionId.set(userTransactionId);
    }
}



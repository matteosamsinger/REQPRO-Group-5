package org.example.stepdefinitions.support;

import org.example.app.ElectricChargingStationNetwork;

public class TestContext {

    private ElectricChargingStationNetwork network;
    private Exception lastException;

    public ElectricChargingStationNetwork getNetwork() {
        return network;
    }

    public void setNetwork(ElectricChargingStationNetwork network) {
        this.network = network;
    }

    public Exception getLastException() {
        return lastException;
    }

    public void setLastException(Exception lastException) {
        this.lastException = lastException;
    }

    public void clearLastException() {
        this.lastException = null;
    }
}

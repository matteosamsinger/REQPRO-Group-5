package org.example.steps.support;

import org.example.*;

import java.util.List;

public class ScenarioContext {
    public ElectricChargingStationNetwork network;

    public Location lookedUpLocation;
    public Charger lookedUpCharger;
    public Client lookedUpClient;

    public ChargingSession currentSession;
    public Client currentClient;
    public Charger currentCharger;

    public List<Charger> lastNetworkStatus;
}


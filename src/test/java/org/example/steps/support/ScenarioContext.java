package org.example.steps.support;

import org.example.app.ElectricChargingStationNetwork;
import org.example.entities.*;

import java.util.List;

public class ScenarioContext {
    public ElectricChargingStationNetwork network;

    public Location lookedUpLocation;
    public Charger lookedUpCharger;
    public Account lookedUpAccount;

    public ChargingSession currentSession;
    public Account currentAccount;
    public Charger currentCharger;

    public List<Charger> lastNetworkStatus;
}



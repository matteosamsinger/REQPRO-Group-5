package org.example;

import java.util.ArrayList;
import java.util.List;

public class ChargingManager {

    private final LocationManager locationManager;
    private final ClientManager clientManager;

    public ChargingManager(LocationManager locationManager, ClientManager clientManager) {
        this.locationManager = locationManager;
        this.clientManager = clientManager;
    }

    /**
     * Beispiel: Netzwerk-Status liefern (alle Charger aller Locations).
     * Kann man später auch für Start/Stop von Sessions nutzen.
     */
    public List<Charger> getNetworkStatus() {
        List<Charger> result = new ArrayList<>();
        for (Location loc : locationManager.getAllLocations()) {
            result.addAll(loc.getChargers());
        }
        return result;
    }

    // Hier könnten später Methoden hin wie:
    // startSession(...), stopSession(...), calculatePrice(...), etc.
}

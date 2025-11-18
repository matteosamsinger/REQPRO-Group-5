package org.example;

import java.util.HashMap;
import java.util.Map;

public class ElectricChargingStationNetwork {

    private final Map<String, Location> locations = new HashMap<>();
    private final Map<String, Client> clients = new HashMap<>();

    public void addLocation(Location location) {
        locations.put(location.getId(), location);
    }

    public Location findLocation(String id) {
        return locations.get(id);
    }

    public void deleteLocation(String id) {
        locations.remove(id);
    }

    public void addChargerToLocation(String locationId, Charger charger) {
        Location location = findLocation(locationId);
        if (location == null) {
            throw new IllegalArgumentException("Location not found: " + locationId);
        }
        location.addCharger(charger);
    }


        // ... Locations & Chargers wie gehabt ...

        // ----- Clients -----

        public void registerClient(Client client) {
            clients.put(client.getClientId(), client);
        }

        public Client findClient(String clientId) {
            return clients.get(clientId);
        }

        public void deleteClient(String clientId) {
            clients.remove(clientId);
        }

    public void removeChargerFromLocation(String locationId, String number) {
        Location location = findLocation(locationId);
        if (location != null) {
            location.removeChargerByNumber(number);
        }
    }


}



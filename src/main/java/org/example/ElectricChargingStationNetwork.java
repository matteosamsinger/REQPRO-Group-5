package org.example;

import java.util.HashMap;
import java.util.Map;

public class ElectricChargingStationNetwork {

    private final Map<String, Location> locations = new HashMap<>();

    public void addLocation(Location location) {
        locations.put(location.getId(), location);
    }

    public Location findLocation(String id) {
        return locations.get(id);
    }
}

package org.example;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LocationManager {

    private final Map<String, Location> locations = new HashMap<>();

    public void addLocation(Location location) {
        locations.put(location.getId(), location);
    }

    public Location findLocation(String id) {
        return locations.get(id);
    }

    public void deleteLocation(String id) {
        locations.remove(id);
    }

    public Collection<Location> getAllLocations() {
        return locations.values();
    }

    public void addChargerToLocation(String locationId, Charger charger) {
        Location location = findLocation(locationId);
        if (location == null) {
            throw new IllegalArgumentException("Location not found: " + locationId);
        }
        location.addCharger(charger);
    }

    public void removeChargerFromLocation(String locationId, String number) {
        Location location = findLocation(locationId);
        if (location != null) {
            location.removeChargerByNumber(number);
        }
    }

    public void setEnergyTariffForLocation(String locationId, Tariff tariff) {
        Location location = findLocation(locationId);
        if (location == null) {
            throw new IllegalArgumentException("Location not found: " + locationId);
        }
        location.setEnergyTariff(tariff);
    }

    public Tariff getEnergyTariffForLocation(String locationId) {
        Location location = findLocation(locationId);
        if (location == null) {
            throw new IllegalArgumentException("Location not found: " + locationId);
        }
        return location.getEnergyTariff();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Location loc : locations.values()) {
            sb.append(loc.getId())
                    .append(" - ")
                    .append(loc.getName())
                    .append(" - ")
                    .append(loc.getAddress())
                    .append(System.lineSeparator());
        }
        return sb.toString();
    }
}

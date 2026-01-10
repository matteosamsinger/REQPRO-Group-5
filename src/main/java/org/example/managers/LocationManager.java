package org.example.managers;

import org.example.entities.Charger;
import org.example.entities.Location;
import org.example.entities.Tariff;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LocationManager {

    private final Map<String, Location> locations = new HashMap<>();


    //Locations
    //Create
    public void createLocation(Location location) {
        locations.put(location.readId(), location);
    }

    //Read
    public Location readLocation(String id) {
        return locations.get(id);
    }

    public Collection<Location> readAllLocations() {
        return locations.values();
    }


    //Update
    public void updateLocationName(String locationId, String newName) {
        Location location = readLocation(locationId);
        if (location == null) {
            throw new IllegalArgumentException("Location not found: " + locationId);
        }
        location.updateName(newName);
    }


    //Delete
    public void deleteLocation(String id) {
        locations.remove(id);
    }


    //Chargers für Location
    //Create
    public void addChargerToLocation(String locationId, Charger charger) {
        Location location = readLocation(locationId);
        if (location == null) {
            throw new IllegalArgumentException("Location not found: " + locationId);
        }
        location.createCharger(charger);
    }

    //Delete
    public void deleteChargerFromLocation(String locationId, String number) {
        Location location = readLocation(locationId);
        if (location == null) {
            throw new IllegalArgumentException("Location not found: " + locationId);
        }
        location.deleteChargerByNumber(number);
    }


    //Tariff

    //Create
    //tariff zu location hinzufügen
    public void createTariffForLocation(String locationId, Tariff tariff) {
        Location location = readLocation(locationId);
        if (location == null) {
            throw new IllegalArgumentException("Location not found: " + locationId);
        }
        location.createTariff(tariff);
    }

    //read
    public Tariff readTariffAt(String locationId, LocalDateTime time) {
        Location location = readLocation(locationId);
        if (location == null) {
            throw new IllegalArgumentException("Location not found: " + locationId);
        }
        return location.readTariffAt(time);
    }

    public Tariff readCurrentTariff(String locationId) {
        return readTariffAt(locationId, LocalDateTime.now());
    }




    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Location loc : locations.values()) {
            sb.append(loc.readId())
                    .append(" - ")
                    .append(loc.readName())
                    .append(" - ")
                    .append(loc.readAddress())
                    .append(System.lineSeparator());
        }
        return sb.toString();
    }
}

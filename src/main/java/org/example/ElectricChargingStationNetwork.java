package org.example;

import java.util.Collection;
import java.util.List;

public class ElectricChargingStationNetwork {

    private final LocationManager locationManager = new LocationManager();
    private final ClientManager clientManager = new ClientManager();
    private final ChargingManager chargingManager =
            new ChargingManager(locationManager, clientManager);

    // --- Location-bezogene Methoden ---

    public void addLocation(Location location) {
        locationManager.addLocation(location);
    }

    public Location findLocation(String id) {
        return locationManager.findLocation(id);
    }

    public void deleteLocation(String id) {
        locationManager.deleteLocation(id);
    }

    public Collection<Location> getAllLocations() {
        return locationManager.getAllLocations();
    }

    public void addChargerToLocation(String locationId, Charger charger) {
        locationManager.addChargerToLocation(locationId, charger);
    }

    public void removeChargerFromLocation(String locationId, String number) {
        locationManager.removeChargerFromLocation(locationId, number);
    }

    public void setEnergyTariffForLocation(String locationId, Tariff tariff) {
        locationManager.setEnergyTariffForLocation(locationId, tariff);
    }

    public Tariff getEnergyTariffForLocation(String locationId) {
        return locationManager.getEnergyTariffForLocation(locationId);
    }

    // Optional: für deine Liste der Locations
    public String locationsToString() {
        return locationManager.toString();
    }

    // --- Client-bezogene Methoden ---

    public void registerClient(Client client) {
        clientManager.registerClient(client);
    }

    public void addClient(Client client) {
        clientManager.registerClient(client);
    }

    public Client findClient(String clientId) {
        return clientManager.findClient(clientId);
    }

    public void deleteClient(String clientId) {
        clientManager.deleteClient(clientId);
    }

    // --- Charging-bezogene Methoden (falls du sie nutzen willst) ---

    public List<Charger> getNetworkStatus() {
        return chargingManager.getNetworkStatus();
    }

    // Getter auf Manager, falls du sie irgendwo direkt brauchst:
    public LocationManager getLocationManager() {
        return locationManager;
    }

    public ClientManager getClientManager() {
        return clientManager;
    }

    public ChargingManager getChargingManager() {
        return chargingManager;
    }
}
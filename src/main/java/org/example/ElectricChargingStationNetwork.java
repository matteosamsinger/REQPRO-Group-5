package org.example;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class ElectricChargingStationNetwork {

    private final LocationManager locationManager = new LocationManager();
    private final AccountManager accountManager = new AccountManager();
    private final ChargingManager chargingManager =
            new ChargingManager(locationManager, accountManager);

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

    /**
     * Fügt einen (neuen) Tarif für einen Standort hinzu.
     * Tarife können mehrmals pro Tag wechseln -> daher "add" statt "replace".
     */
    public void setEnergyTariffForLocation(String locationId, Tariff tariff) {
        locationManager.setEnergyTariffForLocation(locationId, tariff);
    }

    /**
     * Gibt den aktuell gültigen Tarif (jetzt) für den Standort zurück.
     * Optional, falls du das brauchst.
     */
    public Tariff getEnergyTariffForLocation(String locationId) {
        return locationManager.getEnergyTariffForLocation(locationId);
    }

    // Optional: für deine Liste der Locations
    public String locationsToString() {
        return locationManager.toString();
    }

    // --- Account-bezogene Methoden ---

    public void registerAccount(Account account) {
        accountManager.registerAccount(account);
    }

    public void addClient(Account account) {
        accountManager.registerAccount(account);
    }

    public Account findAccount(String accountId) {
        return accountManager.findAccount(accountId);
    }

    public void deleteAccount(String accountId) {
        accountManager.deleteAccount(accountId);
    }

    // --- Charging-bezogene Methoden ---

    public List<Charger> getNetworkStatus() {
        return chargingManager.getNetworkStatus();
    }

    /**
     * Startet eine Ladesession.
     * Wichtig: der Tarif wird zum Startzeitpunkt ermittelt und in der Session gespeichert (Snapshot).
     */
    public ChargingSession startChargingSession(String accountId,
                                                String locationId,
                                                String chargerNumber,
                                                LocalDateTime startTime) {
        return chargingManager.startSession(accountId, locationId, chargerNumber, startTime);
    }

    /**
     * Stoppt eine Ladesession, berechnet den Preis (kWh + Minuten) mit den Startpreisen
     * und bucht den Betrag vom Account ab.
     */
    public ChargingSession stopChargingSession(int sessionId,
                                               LocalDateTime endTime,
                                               double energyKWh) {
        return chargingManager.stopSession(sessionId, endTime, energyKWh);
    }

    // Getter auf Manager, falls du sie irgendwo direkt brauchst:
    public LocationManager getLocationManager() {
        return locationManager;
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public ChargingManager getChargingManager() {
        return chargingManager;
    }
}
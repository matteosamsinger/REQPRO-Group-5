package org.example.app;

import org.example.entities.*;
import org.example.enums.ChargerType;
import org.example.managers.AccountManager;
import org.example.managers.ChargingManager;
import org.example.managers.LocationManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class ElectricChargingStationNetwork {

    private final LocationManager locationManager = new LocationManager();
    private final AccountManager accountManager = new AccountManager();
    private final ChargingManager chargingManager =
            new ChargingManager(locationManager, accountManager);

    // --- Location-bezogene Methoden ---

    //Location hinzufügen - create
    public void createLocation(Location location) {
        locationManager.createLocation(location);
    }

    //Location ID lesen - read
    public Location readLocation(String id) {
        return locationManager.readLocation(id);
    }

    public Collection<Location> readAllLocations() {
        return locationManager.readAllLocations();
    }

    public String locationsToString() {
        return locationManager.toString();
    }

    //Location name ändern - update
    public void updateLocationName(String locationId, String newName) {
        locationManager.updateLocationName(locationId, newName);
    }


    //Location entfernen vom netz - delete
    public void deleteLocation(String id) {
        if (chargingManager.hasActiveSessionAtLocation(id)) {
            throw new IllegalStateException("Cannot delete location " + id + ": active charging session exists");
        }
        locationManager.deleteLocation(id);
    }



    //Charger
    //Create
    public void addChargerToLocation(String locationId, Charger charger) {
        locationManager.addChargerToLocation(locationId, charger);
    }

    //delete
    public void deleteChargerFromLocation(String locationId, String number) {
        locationManager.deleteChargerFromLocation(locationId, number);
    }

    //update
    public void updateChargerType(String locationId, String number, ChargerType newType) {
        locationManager.updateChargerType(locationId, number, newType);
    }

    //Tariff
    /**
     * Fügt einen Tarif für einen Standort hinzu.
     * Tarife können mehrmals pro Tag wechseln -> daher "add" statt "replace".
     */
    //Create
    public void createTariffForLocation(String locationId, Tariff tariff) {
        locationManager.createTariffForLocation(locationId, tariff);
    }

    /**
     * Gibt den aktuell gültigen Tarif (jetzt) für den Standort zurück.
     * Optional, falls du das brauchst.
     */
    //read
    public Tariff readCurrentTariffForLocation(String locationId) {
        return locationManager.readCurrentTariff(locationId);
    }

    public Tariff readTariffAt(String locationId, LocalDateTime time) {
        return locationManager.readTariffAt(locationId, time);
    }





    // --- Account-bezogene Methoden ---

    //create
    public void createAccount(Account account) {
        accountManager.createAccount(account);
    }



    //delete
    public void deleteAccount(String accountId) {
        if (chargingManager.hasActiveSessionForAccount(accountId)) {
            throw new IllegalStateException("Cannot delete account " + accountId + ": active charging session exists");
        }
        accountManager.deleteAccount(accountId);
    }

    //read
    public Account findAccount(String accountId) {
        return accountManager.readAccount(accountId);
    }

    @Override
    public String toString() {
        String nl = System.lineSeparator();
        StringBuilder sb = new StringBuilder();

        sb.append(nl).append("Accounts in charging network:").append(nl);

        List<Account> accounts = new ArrayList<>(accountManager.readAllAccounts());
        accounts.sort(Comparator.comparing(Account::getAccountId));

        if (accounts.isEmpty()) {
            sb.append("  (none)").append(nl);
        } else {
            for (Account acc : accounts) {
                sb.append(" - Account ").append(acc.getAccountId())
                        .append(" | name=").append(acc.getName())
                        .append(" | email=").append(acc.getEmail()).append(nl);
                sb.append("   Balance: ").append(acc.getBalance()).append(" EUR").append(nl);
                sb.append("   Top-ups: ").append(acc.getTopUps().size()).append(nl);
            }
        }

        return sb.toString();
    }

    public String toNetworkStatusString() {
        String nl = System.lineSeparator();
        StringBuilder sb = new StringBuilder();

        sb.append("NETWORK STATUS").append(nl);

        List<Location> locations = new ArrayList<>(locationManager.readAllLocations());
        locations.sort(Comparator.comparing(Location::readId));

        if (locations.isEmpty()) {
            sb.append("  (no locations)").append(nl);
            return sb.toString();
        }

        for (Location loc : locations) {
            sb.append(nl)
                    .append("Location ").append(loc.readId())
                    .append(" | ").append(loc.readName())
                    .append(" | ").append(loc.readAddress())
                    .append(nl);

            // Tarif (aktuell gültig)
            try {
                Tariff t = locationManager.readCurrentTariff(loc.readId());
                sb.append(String.format(java.util.Locale.US,
                        "  Prices: AC %.2f €/kWh + %.2f €/min | DC %.2f €/kWh + %.2f €/min%n",
                        t.getPricePerKWh(org.example.enums.ChargerType.AC),
                        t.getPricePerMinute(org.example.enums.ChargerType.AC),
                        t.getPricePerKWh(org.example.enums.ChargerType.DC),
                        t.getPricePerMinute(org.example.enums.ChargerType.DC)
                ));
            } catch (Exception e) {
                sb.append("  Prices: (no tariff set)").append(nl);
            }

            // Charger Status
            List<Charger> chargers = new ArrayList<>(loc.readChargers()); // falls bei dir getChargers()/readChargers()
            chargers.sort(Comparator.comparing(Charger::getNumber));

            if (chargers.isEmpty()) {
                sb.append("  Chargers: (none)").append(nl);
            } else {
                sb.append("  Chargers:").append(nl);
                for (Charger c : chargers) {
                    sb.append("   - #").append(c.getNumber())
                            .append(" | type=").append(c.getType())
                            .append(" | status=").append(c.getStatus())
                            .append(nl);
                }
            }
        }

        return sb.toString();
    }



    // --- Charging-bezogene Methoden ---

    public List<Charger> readNetworkStatus() {
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
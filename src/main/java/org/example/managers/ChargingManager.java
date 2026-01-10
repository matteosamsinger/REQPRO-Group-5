package org.example.managers;

import org.example.entities.*;
import org.example.enums.ChargerStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChargingManager {

    private final LocationManager locationManager;
    private final AccountManager accountManager;

    private final Map<Integer, ChargingSession> activeSessions = new HashMap<>();
    private int nextSessionId = 1;

    public ChargingManager(LocationManager locationManager, AccountManager accountManager) {
        this.locationManager = locationManager;
        this.accountManager = accountManager;
    }

    public ChargingSession startSession(String accountId, String locationId, String chargerNumber, LocalDateTime startTime) {
        Account account = accountManager.readAccount(accountId);
        if (account == null) throw new IllegalArgumentException("Account not found: " + accountId);

        Location location = locationManager.readLocation(locationId);
        if (location == null) throw new IllegalArgumentException("Location not found: " + locationId);

        Charger charger = location.readChargerByNumber(chargerNumber);
        if (charger == null) throw new IllegalArgumentException("Charger not found: " + chargerNumber);

        if (!charger.isAvailable()) {
            throw new IllegalStateException("Charger not available: " + chargerNumber);
        }

        Tariff tariff = location.readTariffAt(startTime);
        double pricePerKWh = tariff.getPricePerKWh(charger.getType());
        double pricePerMin = tariff.getPricePerMinute(charger.getType());

        ChargingSession session = new ChargingSession(
                nextSessionId++,
                account,
                charger,
                startTime,
                pricePerKWh,
                pricePerMin
        );

        charger.setStatus(ChargerStatus.IN_USE);
        activeSessions.put(session.getSessionId(), session);
        return session;
    }

    public ChargingSession stopSession(int sessionId, LocalDateTime endTime, double energyKWh) {
        ChargingSession session = activeSessions.get(sessionId);
        if (session == null) throw new IllegalArgumentException("Session not found: " + sessionId);

        session.stop(endTime, energyKWh);

        // Abrechnung
        Account account = session.getAccount();
        double amount = session.getTotalPrice();

        account.debit(amount);

        // Rechnungsposten anlegen
        int pos = account.getInvoiceLineItems().size() + 1;

        Charger charger = session.getCharger();
        String locationName = charger.getLocation().readName();

        InvoiceLineItem item = new InvoiceLineItem(
                pos,
                session.getStartTime(),
                locationName,
                charger.getNumber(),
                charger.getType(),
                session.getDurationMinutes(),
                session.getEnergyKWh(),
                session.getTotalPrice()
        );

        account.createInvoiceLineItem(item);

        charger.setStatus(org.example.enums.ChargerStatus.AVAILABLE);

        activeSessions.remove(sessionId);
        return session;
    }

    public List<Charger> getNetworkStatus() {
        List<Charger> result = new ArrayList<>();
        for (Location loc : locationManager.readAllLocations()) {
            result.addAll(loc.readChargers());
        }
        return result;
    }
}
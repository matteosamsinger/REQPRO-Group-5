package org.example;

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
        Account account = accountManager.findAccount(accountId);
        if (account == null) throw new IllegalArgumentException("Account not found: " + accountId);

        Location location = locationManager.findLocation(locationId);
        if (location == null) throw new IllegalArgumentException("Location not found: " + locationId);

        Charger charger = location.findChargerByNumber(chargerNumber);
        if (charger == null) throw new IllegalArgumentException("Charger not found: " + chargerNumber);

        if (!charger.isAvailable()) {
            throw new IllegalStateException("Charger not available: " + chargerNumber);
        }

        Tariff tariff = location.getTariffAt(startTime);
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

        // Transaction anlegen (positionNumber kann z.B. sessionId sein)
        int txId = account.getTransactions().size() + 1;
        Transaction tx = new Transaction(txId, sessionId, amount);
        account.addTransaction(tx);

        // Rechnungsposten hinzufügen
        int nextPos = account.getInvoiceItems().size() + 1;
        InvoiceLineItem item = new InvoiceLineItem(
                nextPos,
                session.getStartTime(),
                session.getCharger().getLocation().getName(),
                session.getCharger().getNumber(),
                session.getCharger().getType(),
                session.getDurationMinutes(),
                session.getEnergyKWh(),
                session.getTotalPrice()
        );
        account.addInvoiceItem(item);


        // Charger wieder freigeben
        session.getCharger().setStatus(ChargerStatus.AVAILABLE);

        activeSessions.remove(sessionId);
        return session;
    }

    public List<Charger> getNetworkStatus() {
        List<Charger> result = new ArrayList<>();
        for (Location loc : locationManager.getAllLocations()) {
            result.addAll(loc.getChargers());
        }
        return result;
    }
}
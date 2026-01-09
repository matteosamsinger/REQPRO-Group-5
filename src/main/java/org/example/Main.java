package org.example;

import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        System.out.println("Starting EV Charging Network Demo...");

        ElectricChargingStationNetwork network = new ElectricChargingStationNetwork();

        // --- Create Location ---
        Location hq = new Location("LOC-001", "Headquarters", "HQ Street 1");
        network.addLocation(hq);

        // --- Add Tariff for Location (validFrom includes time) ---
        Tariff tariff1 = new Tariff(
                1,
                LocalDateTime.now().minusHours(2),
                0.49, 0.05,   // AC: €/kWh, €/minute
                0.59, 0.10    // DC: €/kWh, €/minute
        );
        network.setEnergyTariffForLocation("LOC-001", tariff1);

        // --- Add Charger ---
        Charger charger1 = new Charger(
                1,
                "1",
                ChargerType.AC,
                22,
                hq
        );
        network.addChargerToLocation("LOC-001", charger1);

        // --- Create & Register Account (replaces Client) ---
        Account account = new Account("A-001", "Alice", "alice@example.com");
        network.registerAccount(account);

        // --- Top up ---
        account.topUpAccountWithMoney(50.0);

        // --- Output state ---
        System.out.println("\nAccounts in charging network:");
        Account found = network.findAccount("A-001");
        if (found != null) {
            System.out.println(" - Account " + found.getAccountId()
                    + " | name=" + found.getName()
                    + " | email=" + found.getEmail());
            System.out.println("   Balance: " + found.getAccountBalance() + " EUR");
            System.out.println("   Top-ups: " + found.getBalanceTopUps().size());
        }

        // --- Start session ---
        LocalDateTime start = LocalDateTime.now();
        ChargingSession session = network.startChargingSession("A-001", "LOC-001", "1", start);

        // simulate stop after 30 min + 12.5 kWh
        ChargingSession finished = network.stopChargingSession(
                session.getSessionId(),
                start.plusMinutes(30),
                12.5
        );

        System.out.println("\nSession finished:");
        System.out.println(" - durationMinutes=" + finished.getDurationMinutes());
        System.out.println(" - energyKWh=" + finished.getEnergyKWh());
        System.out.println(" - totalPrice=" + finished.getTotalPrice() + " EUR");

        System.out.println("\nBalance after charging:");
        System.out.println(" - " + account.getAccountBalance() + " EUR");

        System.out.println("\nDemo finished.");

        account.topUp(100);

        System.out.println("\nTop-ups:");
        for (TopUp t : account.getTopUps()) {
            System.out.println(" - #" + t.getTopUpId()
                    + " | time=" + t.getTime()
                    + " | amount=" + t.getAmount() + " EUR");
        }

        System.out.println("\nTransactions:");
        for (Transaction tx : account.getTransactions()) {
            System.out.println(" - txId=" + tx.getTransactionId()
                    + " | position=" + tx.getPositionNumber()
                    + " | amount=" + tx.getAmount() + " EUR"
                    + " | time=" + tx.getStartTime());
        }
    }
}
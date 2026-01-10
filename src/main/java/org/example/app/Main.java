package org.example.app;

import org.example.entities.*;
import org.example.enums.ChargerType;

import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        System.out.println("Starting EV Charging Network Demo...");

        ElectricChargingStationNetwork network = new ElectricChargingStationNetwork();

        // --- Create Location ---
        Location hq = new Location("LOC-001", "Headquarters", "HQ Street 1");
        Location hq2 = new Location("LOC-002", "Headquarters2", "HQ Street 12");
        network.createLocation(hq);
        network.createLocation(hq2);

        // --- Add Tariff for Location (validFrom includes time) ---
        Tariff tariff1 = new Tariff(
                1,
                LocalDateTime.now().minusHours(2),
                0.49, 0.05,   // AC: €/kWh, €/minute
                0.59, 0.10    // DC: €/kWh, €/minute
        );
        network.createTariffForLocation("LOC-001", tariff1);
        network.createTariffForLocation("LOC-002", tariff1);

        // --- Add Charger ---
        Charger charger1 = new Charger(
                1,
                "1",
                ChargerType.AC
        );
        network.addChargerToLocation("LOC-001", charger1);

        Charger charger2 = new Charger(
                2,
                "1",
                ChargerType.DC
        );
        network.addChargerToLocation("LOC-002", charger2);

        // --- Create & Register Account (replaces Client) ---
        Account account = new Account("A-001", "Alice", "alice@example.com");
        Account account2 = new Account("A-002", "Alice", "alice@example.com");
        network.createAccount(account);
        network.createAccount(account2);

        // --- Top up ---
        account.topUp(50);

        // --- Output state ---
        /*System.out.println("\nAccounts in charging network:");
        Account found = network.findAccount("A-001");
        if (found != null) {
            System.out.println(" - Account " + found.getAccountId()
                    + " | name=" + found.getName()
                    + " | email=" + found.getEmail());
            System.out.println("   Balance: " + found.getAccountBalance() + " EUR");
            System.out.println("   Top-ups: " + found.getBalanceTopUps().size());
        }*/
        System.out.println(network.toString());



        // --- Start session ---
        LocalDateTime start = LocalDateTime.now();
        ChargingSession session = network.startChargingSession("A-001", "LOC-001", "1", start);

        // simulate stop after 30 min + 12.5 kWh
        ChargingSession finished = network.stopChargingSession(
                session.getSessionId(),
                start.plusMinutes(30),
                12.5
        );

        ChargingSession session2 = network.startChargingSession("A-001", "LOC-002", "1", start);

        // simulate stop after 30 min + 12.5 kWh
        ChargingSession finished2 = network.stopChargingSession(
                session2.getSessionId(),
                start.plusMinutes(30),
                12.5
        );


        System.out.println("\nSession finished:");
        System.out.println(" - durationMinutes=" + finished.getDurationMinutes());
        System.out.println(" - energyKWh=" + finished.getEnergyKWh());
        System.out.println(" - totalPrice=" + finished.getTotalPrice() + " EUR");

        System.out.println("\nBalance after charging:");
        System.out.println(" - " + account.getBalance() + " EUR");

        System.out.println("\nDemo finished.");

        account.topUp(100);

        System.out.println("\nTop-ups:");
        for (TopUp t : account.getTopUps()) {
            System.out.println(" - #" + t.getTopUpId()
                    + " | time=" + t.getTime()
                    + " | amount=" + t.getAmount() + " EUR");
        }

        System.out.println("\nTransactions:");
        System.out.println(account.toInvoiceString());



        System.out.println(network.toNetworkStatusString());

        hq.updateName("headqurter3");

        network.updateLocationName("LOC-001", "headquarter5");

        System.out.println(network.toNetworkStatusString());

    }
}
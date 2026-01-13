package org.example.app;

import org.example.entities.*;
import org.example.enums.ChargerStatus;
import org.example.enums.ChargerType;

import java.time.LocalDateTime;

public class MainForPresentation {

    public static void main(String[] args) {
        ElectricChargingStationNetwork net = new ElectricChargingStationNetwork();

        System.out.println("=== DEMO: ElectricChargingStationNetwork ===");

        // -------------------------
        // 1) Create 10 locations
        // -------------------------
        System.out.println("\n--- Create 10 locations ---");
        for (int i = 1; i <= 10; i++) {
            String id = String.format("LOC-%03d", i);
            net.createLocation(new Location(id, "Location " + i, "Street " + i));
        }

        System.out.println(net.locationsToString());

        // -------------------------
        // 2) Update location name
        // -------------------------
        System.out.println("\n--- Update location name (LOC-001 -> City Center) ---");
        net.updateLocationName("LOC-001", "City Center");
        System.out.println(net.readLocation("LOC-001").readName());

        // -------------------------
        // 3) Add chargers to a few locations
        // -------------------------
        System.out.println("\n--- Add chargers to locations ---");
        net.addChargerToLocation("LOC-001", new Charger(1, "1", ChargerType.AC));
        net.addChargerToLocation("LOC-001", new Charger(2, "2", ChargerType.DC));

        net.addChargerToLocation("LOC-002", new Charger(1, "1", ChargerType.AC));
        net.addChargerToLocation("LOC-002", new Charger(2, "2", ChargerType.DC));

        net.addChargerToLocation("LOC-010", new Charger(1, "1", ChargerType.AC)); // für charging demo

        // OUT_OF_ORDER setzen
        net.readLocation("LOC-002").readChargerByNumber("2").setStatus(ChargerStatus.OUT_OF_ORDER);

        // -------------------------
        // 4) Create tariffs (normal + free + error negative)
        // -------------------------
        System.out.println("\n--- Create tariffs ---");
        net.createTariffForLocation("LOC-001", new Tariff(1,
                LocalDateTime.parse("2000-01-01T00:00"),
                0.35, 0.05,
                0.60, 0.10));

        net.createTariffForLocation("LOC-002", new Tariff(1,
                LocalDateTime.parse("2000-01-01T00:00"),
                0.30, 0.04,
                0.55, 0.09));

        // gratis Tarif als Edge Case (alles 0)
        net.createTariffForLocation("LOC-003", new Tariff(1,
                LocalDateTime.parse("2000-01-01T00:00"),
                0.00, 0.00,
                0.00, 0.00));

        // negativer Tarif als Error Case
        try {
            net.createTariffForLocation("LOC-004", new Tariff(1,
                    LocalDateTime.parse("2000-01-01T00:00"),
                    -0.10, 0.00,
                    0.00, 0.00));
            System.out.println("ERROR: negative tariff should have failed but did not.");
        } catch (Exception e) {
            System.out.println("Expected error (negative tariff): " + e.getMessage());
        }

        // Für LOC-010 auch Tarif setzen (sonst start charging scheitert)
        net.createTariffForLocation("LOC-010", new Tariff(1,
                LocalDateTime.parse("2000-01-01T00:00"),
                0.40, 0.10,
                0.70, 0.20));

        // -------------------------
        // 5) Create accounts + topups
        // -------------------------
        System.out.println("\n--- Create accounts + topups ---");
        Account a1 = new Account("A-001", "Leul", "leul@example.com");
        net.createAccount(a1);
        a1.topUp(30.00);
        a1.topUp(20.00);

        Account a2 = new Account("A-002", "Bob", "bob@example.com");
        net.createAccount(a2); // Edge Case invoice (no topups, no sessions)

        // -------------------------
        // 6) Start a charging session (Happy path)
        // -------------------------
        System.out.println("\n--- Start charging session (Happy Path) ---");
        ChargingSession s1 = net.startChargingSession(
                "A-001",
                "LOC-010",
                "1",
                LocalDateTime.parse("2026-01-10T10:00")
        );
        System.out.println("Session started with id: " + s1.getSessionId());
        System.out.println("Charger status after start: " +
                net.readLocation("LOC-010").readChargerByNumber("1").getStatus());

        // -------------------------
        // 7) Error: start second session on same charger
        // -------------------------
        System.out.println("\n--- Start second session on same charger (Error Case) ---");
        try {
            net.startChargingSession(
                    "A-001",
                    "LOC-010",
                    "1",
                    LocalDateTime.parse("2026-01-10T10:05")
            );
            System.out.println("ERROR: second session should have failed but did not.");
        } catch (Exception e) {
            System.out.println("Expected error (charger not available): " + e.getMessage());
        }

        // -------------------------
        // 8) Error: update charger type while IN_USE
        // -------------------------
        System.out.println("\n--- Update charger type while IN_USE (Error Case) ---");
        try {
            net.updateChargerType("LOC-010", "1", ChargerType.DC);
            System.out.println("ERROR: update while in use should have failed but did not.");
        } catch (Exception e) {
            System.out.println("Expected error (update charger in use): " + e.getMessage());
        }

        // -------------------------
        // 9) Stop session (Happy path) + verify invoice
        // -------------------------
        System.out.println("\n--- Stop session (Happy Path) ---");
        ChargingSession stopped = net.stopChargingSession(
                s1.getSessionId(),
                LocalDateTime.parse("2026-01-10T11:00"), // 60 min
                10.0 // kWh
        );

        System.out.println("Stopped session total price: " + stopped.getTotalPrice() + " EUR");
        System.out.println("Charger status after stop: " +
                net.readLocation("LOC-010").readChargerByNumber("1").getStatus());

        // -------------------------
        // 10) Error: stop non-existing session
        // -------------------------
        System.out.println("\n--- Stop non-existing session (Error Case) ---");
        try {
            net.stopChargingSession(9999, LocalDateTime.now(), 1.0);
            System.out.println("ERROR: stopping unknown session should have failed but did not.");
        } catch (Exception e) {
            System.out.println("Expected error (session not found): " + e.getMessage());
        }

        // -------------------------
        // 11) Invoice print (uses Account.toInvoiceString)
        // -------------------------
        System.out.println("\n--- Print invoice for A-001 (2 topups + 1 charge) ---");
        System.out.println(a1.toInvoiceString());

        System.out.println("\n--- Print invoice for A-002 (no topups, no charges) ---");
        System.out.println(a2.toInvoiceString());

        // -------------------------
        // 12) Network status print
        // -------------------------
        System.out.println("\n--- Network status ---");
        System.out.println(net.toNetworkStatusString());

        // -------------------------
        // 13) Delete location error: active session exists
        // (Start a new session and try delete)
        // -------------------------
        System.out.println("\n--- Delete location with active session (Error Case) ---");
        ChargingSession s2 = net.startChargingSession(
                "A-001",
                "LOC-010",
                "1",
                LocalDateTime.parse("2026-01-10T12:00")
        );

        try {
            net.deleteLocation("LOC-010");
            System.out.println("ERROR: delete location with active session should have failed but did not.");
        } catch (Exception e) {
            System.out.println("Expected error (cannot delete location): " + e.getMessage());
        }

        // cleanup: stop it, then delete should work
        net.stopChargingSession(s2.getSessionId(), LocalDateTime.parse("2026-01-10T12:10"), 1.0);
        net.deleteLocation("LOC-010");
        System.out.println("LOC-010 deleted after stopping session: " + (net.readLocation("LOC-010") == null));

    }
}


package org.example;


public class Main {

    public static void main(String[] args) {
        System.out.println("Starting EV Charging Network Demo...");

        // Owner = Betreiber des Netzes (hier nur implizit, über das Network-Objekt)
        ElectricChargingStationNetwork network = new ElectricChargingStationNetwork();

        // --- Create Location (Glossary: Create Location) ---
        Location hq = new Location("LOC-001", "Headquarters", "HQ Street 1");
        network.addLocation(hq);

        // --- Add Charger (Glossary: Add Charger) ---
        Charger charger1 = new Charger(
                1,      // internal chargerId
                "1",    // charger number
                "AC",   // mode / type: AC or DC (Glossary: Select Mode)
                22,     // max power in kW
                hq
        );
        network.addChargerToLocation("LOC-001", charger1);

        // --- Register Account (Glossary: Register AcYcount / Customer) ---
        Account account = new Account();
        Client customer = new Client("C-001", "Alice", "alice@example.com", account);
        network.registerClient(customer);

        // --- Top Up Account with Money (Glossary) ---
        account.topUpAccountWithMoney(50.0);

        // --- Output demo state ---

        System.out.println("\nLocations in charging network:");
        Location foundLoc = network.findLocation("LOC-001");
        if (foundLoc != null) {
            System.out.println(" - Location " + foundLoc.getId()
                    + " | name=" + foundLoc.getName()
                    + " | address=" + foundLoc.getAddress());
            System.out.println("   Chargers at this location: " + foundLoc.getChargers().size());
            if (!foundLoc.getChargers().isEmpty()) {
                Charger c = foundLoc.getChargers().get(0);
                System.out.println("   -> Charger number " + c.getNumber()
                        + " | mode=" + c.getType()
                        + " | maxPower=" + c.getMaxPowerKw() + " kW");
            }
        }

        System.out.println("\nCustomers in charging network:");
        Client foundCustomer = network.findClient("C-001");
        if (foundCustomer != null) {
            System.out.println(" - Customer " + foundCustomer.getClientId()
                    + " | name=" + foundCustomer.getName()
                    + " | email=" + foundCustomer.getEmail());
            System.out.println("   Account balance (prepaid credit): "
                    + foundCustomer.getAccount().getAccountBalance() + " EUR");
            System.out.println("   List of balance top-ups: "
                    + foundCustomer.getAccount().getBalanceTopUps().size() + " top-ups");
        }

        System.out.println("\nDemo finished.");
    }
}


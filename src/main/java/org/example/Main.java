package org.example;

public class Main {

    public static void main(String[] args) {
        System.out.println("Starting Electric Charging Station Demo...");

        // Management-Klasse
        ElectricChargingStationNetwork network = new ElectricChargingStationNetwork();

        // Beispiel-Location anlegen
        Location hq = new Location("LOC-001", "Headquarters", "HQ Street 1");
        network.addLocation(hq);

        // Charger an der Location anlegen
        Charger charger1 = new Charger(
                1,
                "1",
                "AC",
                22,
                hq
        );
        network.addChargerToLocation("LOC-001", charger1);


        Account account = new Account();
        Client client = new Client("C-001", "Alice", "alice@example.com", account);
        network.registerClient(client);


        account.topUp(50.0);

        // ausgeben

        System.out.println("Locations in network:");
        Location foundLoc = network.findLocation("LOC-001");
        if (foundLoc != null) {
            System.out.println(" - " + foundLoc.getId() + " | " + foundLoc.getName() + " | " + foundLoc.getAddress());
            System.out.println("   Chargers at this location: " + foundLoc.getChargers().size());
            if (!foundLoc.getChargers().isEmpty()) {
                Charger c = foundLoc.getChargers().get(0);
                System.out.println("   -> Charger #" + c.getNumber() + " | type=" + c.getType() + " | maxPower=" + c.getMaxPowerKw() + " kW");
            }
        }

        System.out.println();
        System.out.println("Clients in network:");
        Client foundClient = network.findClient("C-001");
        if (foundClient != null) {
            System.out.println(" - " + foundClient.getClientId() + " | " + foundClient.getName() + " | " + foundClient.getEmail());
            System.out.println("   Account balance: " + foundClient.getAccount().getBalance() + " EUR");
        }

        System.out.println();
        System.out.println("Demo finished.");
    }
}

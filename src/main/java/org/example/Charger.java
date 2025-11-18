package org.example;

public class Charger {

    private final int chargerId;    // interne ID, z.B. laufende Nummer
    private String number;          // sichtbare Nummer, z.B. "1", "2"
    private String type;            // "AC" oder "DC"
    private int maxPowerKw;         // z.B. 22

    private Location location;      // optional Rückreferenz

    public Charger(int chargerId, String number, String type, int maxPowerKw, Location location) {
        this.chargerId = chargerId;
        this.number = number;
        this.type = type;
        this.maxPowerKw = maxPowerKw;
        this.location = location;
    }

    public int getChargerId() {
        return chargerId;
    }

    public String getNumber() {
        return number;
    }

    public String getType() {
        return type;
    }

    public int getMaxPowerKw() {
        return maxPowerKw;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


}


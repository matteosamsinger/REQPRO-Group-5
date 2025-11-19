package org.example;

public class Charger {

    private final int chargerId;
    private String number;
    private String type;
    private int maxPowerKw;
    private Location location;

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


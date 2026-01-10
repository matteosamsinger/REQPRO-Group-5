package org.example.entities;

import org.example.enums.ChargerStatus;
import org.example.enums.ChargerType;

public class Charger {

    private final int chargerId;
    private String number;
    private ChargerType type;
    private int maxPowerKw;
    private Location location;
    private ChargerStatus status;

    public Charger(int chargerId, String number, ChargerType type, int maxPowerKw, Location location) {
        this.chargerId = chargerId;
        this.number = number;
        this.type = type;
        this.maxPowerKw = maxPowerKw;
        this.location = location;
        this.status = ChargerStatus.AVAILABLE; // Standard: verfügbar
    }

    public int getChargerId() {
        return chargerId;
    }

    public String getNumber() {
        return number;
    }

    public ChargerType getType() {
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

    public ChargerStatus getStatus() {
        return status;
    }

    public void setStatus(ChargerStatus status) {
        this.status = status;
    }

    public boolean isAvailable() {
        return status == ChargerStatus.AVAILABLE;
    }
}


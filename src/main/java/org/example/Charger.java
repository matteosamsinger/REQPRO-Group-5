package org.example;

public class Charger {


    private final int chargerId;
    private String number;
    private String type;   // z.B. "AC" oder "DC"
    private String status; // z.B. "AVAILABLE", "IN_USE", "OUT_OF_ORDER"
    private Location location;

    public Charger(int chargerId, String number, String type, String status, Location location) {
        this.chargerId = chargerId;
        this.number = number;
        this.type = type;
        this.status = status;
        this.location = location;
    }


    public int getChargerId() {
        return chargerId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    public boolean isAvailable() {
        return "AVAILABLE".equalsIgnoreCase(status);
    }


    public ChargingSession startSession(Client client) {
        throw new UnsupportedOperationException("startSession not implemented yet");
    }

    public void stopSession(ChargingSession session, double energyKWh) {
        throw new UnsupportedOperationException("stopSession not implemented yet");
    }
}


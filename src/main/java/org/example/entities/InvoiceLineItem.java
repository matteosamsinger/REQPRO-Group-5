package org.example.entities;

import org.example.enums.ChargerType;

import java.time.LocalDateTime;

public class InvoiceLineItem {

    private final int positionNumber;              // Rechnungspostennummer
    private final LocalDateTime startTime;         // zum Sortieren
    private final String locationName;
    private final String chargerNumber;
    private final ChargerType mode;                // AC/DC
    private final long durationMinutes;
    private final double energyKWh;
    private final double priceEur;

    public InvoiceLineItem(int positionNumber, LocalDateTime startTime, String locationName, String chargerNumber,
                           ChargerType mode, long durationMinutes, double energyKWh, double priceEur) {
        this.positionNumber = positionNumber;
        this.startTime = startTime;
        this.locationName = locationName;
        this.chargerNumber = chargerNumber;
        this.mode = mode;
        this.durationMinutes = durationMinutes;
        this.energyKWh = energyKWh;
        this.priceEur = priceEur;
    }

    public int getPositionNumber() {
        return positionNumber;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getChargerNumber() {
        return chargerNumber;
    }

    public ChargerType getMode() {
        return mode;
    }

    public long getDurationMinutes() {
        return durationMinutes;
    }

    public double getEnergyKWh() {
        return energyKWh;
    }

    public double getPriceEur() {
        return priceEur;
    }

}

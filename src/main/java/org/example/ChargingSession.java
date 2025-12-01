package org.example;

import java.time.Duration;
import java.time.LocalDateTime;

public class ChargingSession {

    private final int sessionId;
    private final Client client;
    private final Charger charger;

    private final LocalDateTime startTime;
    private LocalDateTime endTime;

    private double energyKWh;
    private long durationMinutes;
    private double totalPrice;

    public ChargingSession(int sessionId, Client client, Charger charger, LocalDateTime startTime) {
        this.sessionId = sessionId;
        this.client = client;
        this.charger = charger;
        this.startTime = startTime;
    }

    /**
     * Beendet die Session, berechnet Dauer und Gesamtpreis.
     */
    public void stop(LocalDateTime endTime, double energyKWh, double pricePerKWh) {
        this.endTime = endTime;
        this.energyKWh = energyKWh;
        this.durationMinutes = calculateDurationMinutesInternal();
        this.totalPrice = energyKWh * pricePerKWh;
    }

    private long calculateDurationMinutesInternal() {
        if (endTime == null) {
            return 0;
        }
        return Duration.between(startTime, endTime).toMinutes();
    }

    public long getDurationMinutes() {
        return durationMinutes;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Client getClient() {
        return client;
    }

    public Charger getCharger() {
        return charger;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public double getEnergyKWh() {
        return energyKWh;
    }

    public int getSessionId() {
        return sessionId;
    }
}



package org.example;

import java.time.Duration;
import java.time.LocalDateTime;

public class ChargingSession {

    private final int sessionId;
    private final Client client;
    private final Tariff appliedTariff;

    private final LocalDateTime startTime;
    private LocalDateTime endTime;

    private double energyKWh;
    private long durationMinutes;
    private double totalPrice;

    public ChargingSession(int sessionId, Client client, Tariff appliedTariff) {
        this.sessionId = sessionId;
        this.client = client;
        this.appliedTariff = appliedTariff;
        this.startTime = LocalDateTime.now();
    }


    public void endSession(LocalDateTime endTime, double energyKWh) {
        this.endTime = endTime;
        this.energyKWh = energyKWh;
        this.durationMinutes = calculateDuration();
        this.totalPrice = calculatePrice();
    }


    public long calculateDuration() {
        if (endTime == null) {
            return 0;
        }
        return Duration.between(startTime, endTime).toMinutes();
    }


    public double calculatePrice() {
        return energyKWh * appliedTariff.getPricePerKWh("AC");
    }

    //Getter

    public int getSessionId() {
        return sessionId;
    }

    public Client getClient() {
        return client;
    }

    public Tariff getAppliedTariff() {
        return appliedTariff;
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

    public long getDurationMinutes() {
        return durationMinutes;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}


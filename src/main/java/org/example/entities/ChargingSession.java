package org.example.entities;

import java.time.Duration;
import java.time.LocalDateTime;

public class ChargingSession {

    private final int sessionId;
    private final Account account;
    private final Charger charger;

    private final LocalDateTime startTime;
    private final double pricePerKWhAtStart;
    private final double pricePerMinuteAtStart;

    private LocalDateTime endTime;
    private double energyKWh;
    private long durationMinutes;
    private double totalPrice;

    public ChargingSession(int sessionId, Account account, Charger charger,
                           LocalDateTime startTime, double pricePerKWhAtStart, double pricePerMinuteAtStart) {
        this.sessionId = sessionId;
        this.account = account;
        this.charger = charger;
        this.startTime = startTime;
        this.pricePerKWhAtStart = pricePerKWhAtStart;
        this.pricePerMinuteAtStart = pricePerMinuteAtStart;
    }

    /**
     * Beendet die Session, berechnet Dauer und Gesamtpreis.
     */
    public void stop(LocalDateTime endTime, double energyKWh) {
        this.endTime = endTime;
        this.energyKWh = energyKWh;
        this.durationMinutes = calculateDurationMinutesInternal();

        this.totalPrice = (energyKWh * pricePerKWhAtStart) + (durationMinutes * pricePerMinuteAtStart);
    }

    private long calculateDurationMinutesInternal() {
        if (endTime == null) {
            return 0;
        }
        return Duration.between(startTime, endTime).toMinutes();
    }


    public int getSessionId() {
        return sessionId;
    }

    public Account getAccount() {
        return account;
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

    public long getDurationMinutes() {
        return durationMinutes;
    }

    public double getTotalPrice() {
        return totalPrice;
    }


    public double getPricePerKWhAtStart() {
        return pricePerKWhAtStart;
    }

    public double getPricePerMinuteAtStart() {
        return pricePerMinuteAtStart;
    }
}



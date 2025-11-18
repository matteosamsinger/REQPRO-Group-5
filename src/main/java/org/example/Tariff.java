package org.example;

import java.time.LocalDate;

public class Tariff {

    private final int tariffId;
    private final LocalDate validFrom;
    private final double pricePerKWhAC;
    private final double pricePerMinuteAC;
    private final double pricePerKWhDC;
    private final double pricePerMinuteDC;
    private final Location location;

    public Tariff(int tariffId,
                  LocalDate validFrom,
                  double pricePerKWhAC,
                  double pricePerMinuteAC,
                  double pricePerKWhDC,
                  double pricePerMinuteDC,
                  Location location) {
        this.tariffId = tariffId;
        this.validFrom = validFrom;
        this.pricePerKWhAC = pricePerKWhAC;
        this.pricePerMinuteAC = pricePerMinuteAC;
        this.pricePerKWhDC = pricePerKWhDC;
        this.pricePerMinuteDC = pricePerMinuteDC;
        this.location = location;
    }

    public int getTariffId() {
        return tariffId;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public double getPricePerKWhAC() {
        return pricePerKWhAC;
    }

    public double getPricePerMinuteAC() {
        return pricePerMinuteAC;
    }

    public double getPricePerKWhDC() {
        return pricePerKWhDC;
    }

    public double getPricePerMinuteDC() {
        return pricePerMinuteDC;
    }

    public Location getLocation() {
        return location;
    }

    // --- einfache Logik für MVP1 ---

    public boolean isValidAt(LocalDate date) {
        // super simple: gültig ab validFrom, kein Enddatum
        return !date.isBefore(validFrom);
    }

    public double getPricePerKWh(String type) {
        if ("AC".equalsIgnoreCase(type)) {
            return pricePerKWhAC;
        }
        if ("DC".equalsIgnoreCase(type)) {
            return pricePerKWhDC;
        }
        throw new IllegalArgumentException("Unknown charger type: " + type);
    }

    public double getPricePerMinute(String type) {
        if ("AC".equalsIgnoreCase(type)) {
            return pricePerMinuteAC;
        }
        if ("DC".equalsIgnoreCase(type)) {
            return pricePerMinuteDC;
        }
        throw new IllegalArgumentException("Unknown charger type: " + type);
    }
}

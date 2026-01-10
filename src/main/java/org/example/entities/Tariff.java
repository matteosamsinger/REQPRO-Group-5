package org.example.entities;

import org.example.enums.ChargerType;

import java.time.LocalDateTime;

public class Tariff {

    private final int tariffId;
    private final LocalDateTime validFrom;
    private final double pricePerKWhAC;
    private final double pricePerMinuteAC;
    private final double pricePerKWhDC;
    private final double pricePerMinuteDC;

    public Tariff(int tariffId,
                  LocalDateTime validFrom,
                  double pricePerKWhAC,
                  double pricePerMinuteAC,
                  double pricePerKWhDC,
                  double pricePerMinuteDC) {
        this.tariffId = tariffId;
        this.validFrom = validFrom;
        this.pricePerKWhAC = pricePerKWhAC;
        this.pricePerMinuteAC = pricePerMinuteAC;
        this.pricePerKWhDC = pricePerKWhDC;
        this.pricePerMinuteDC = pricePerMinuteDC;
    }

    public int getTariffId() {
        return tariffId;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public double getPricePerKWh(ChargerType type) {
        if (type == ChargerType.AC) {
            return pricePerKWhAC;
        } else {
            return pricePerKWhDC;
        }
    }

    public double getPricePerMinute(ChargerType type) {
        if (type == ChargerType.AC) {
            return pricePerMinuteAC;
        } else {
            return pricePerMinuteDC;
        }

    }
}

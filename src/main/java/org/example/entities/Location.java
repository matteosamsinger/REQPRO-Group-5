package org.example.entities;

import org.example.enums.ChargerStatus;
import org.example.enums.ChargerType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Location {

    private final String id;
    private String name;
    private final String address;
    //private Tariff energyTariff;


    private final List<Charger> chargers = new ArrayList<>();
    private final List<Tariff> tariffs = new ArrayList<>();

    public Location(String id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    //READ
    public String readId() {
        return id;
    }

    public String readName() {
        return name;
    }

    public String readAddress() {
        return address;
    }

    public List<Charger> readChargers() {
        return new ArrayList<>(chargers);
    }

    public Charger readChargerByNumber(String number) {
        for (Charger charger : chargers) {
            if (charger.getNumber().equals(number)) return charger;
        }
        return null;
    }

    public List<Tariff> readTariffs() {
        return new ArrayList<>(tariffs);
    }

    public Tariff readTariffAt(LocalDateTime time) {
        Tariff best = null;
        for (Tariff t : tariffs) {
            if (!t.getValidFrom().isAfter(time)) {
                best = t; // da sortiert: best wird immer "letzter gültiger"
            } else {
                break; // weil sortiert
            }
        }
        if (best == null) {
            throw new IllegalStateException("No tariff defined for location " + id + " at " + time);
        }
        return best;
    }



    //Update
    public void updateName(String name) {
        this.name = name;
    }

    //kein updatetariff weil tariff sich mehrmals pro tag ändern - sprich ein neuer tarif ist gültig und nicht der alte ändert sich

    //Create
    public void createCharger(Charger charger) {
        if (charger == null) {
            throw new IllegalArgumentException("Charger must not be null");
        }

        if (readChargerByNumber(charger.getNumber()) != null) {
            throw new IllegalArgumentException(
                    "Charger number already exists at location " + id + ": " + charger.getNumber()
            );
        }

        // Beziehung setzen (damit charger.getLocation() später stimmt)
        charger.setLocation(this);

        chargers.add(charger);
    }

    //neuer Tayrif wird hinzugefügen - Preise können mehrmals pro Tag wechseln
    public void createTariff(Tariff tariff) {
        if (tariff == null) {
            throw new IllegalArgumentException("Tariff must not be null");
        }

        // Negative Preise verhindern (Error-Case im Feature)
        if (tariff.getPricePerKWh(ChargerType.AC) < 0
                || tariff.getPricePerKWh(ChargerType.AC) < 0
                || tariff.getPricePerMinute(ChargerType.DC) < 0
                || tariff.getPricePerMinute(ChargerType.AC) < 0) {
            throw new IllegalArgumentException("Tariff prices must be >= 0");
        }


        tariffs.add(tariff);
        tariffs.sort(Comparator.comparing(Tariff::getValidFrom));
    }


    //Delete
    public void deleteChargerByNumber(String number) {
        Charger charger = readChargerByNumber(number);
        if (charger == null) {
            throw new IllegalArgumentException("Charger not found at location " + id + ": " + number);
        }
        if (charger.getStatus() == ChargerStatus.IN_USE) {
            throw new IllegalStateException("Cannot delete charger " + number + " at location " + id + ": charger is in use");
        }
        chargers.removeIf(c -> c.getNumber().equals(number));
    }

    //kein remove Tarif weil macht die Historie kaputt bei getTariffAt


}

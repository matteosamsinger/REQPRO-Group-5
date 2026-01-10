package org.example.entities;

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
        if (readChargerByNumber(charger.getNumber()) != null) {
            throw new IllegalArgumentException(
                    "Charger number already exists at location " + id + ": " + charger.getNumber()
            );
        }
        chargers.add(charger);
    }

    //neuer Tayrif wird hinzugefügen - Preise können mehrmals pro Tag wechseln
    public void createTariff(Tariff tariff) {
        tariffs.add(tariff);
        tariffs.sort(Comparator.comparing(Tariff::getValidFrom));
    }


    //Delete
    public void deleteChargerByNumber(String number) {
        chargers.removeIf(c -> c.getNumber().equals(number));
    }

    //kein remove Tarif weil macht die Historie kaputt bei getTariffAt


}

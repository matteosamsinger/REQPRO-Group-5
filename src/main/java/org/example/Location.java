package org.example;

import java.util.ArrayList;
import java.util.List;

public class Location {

    private final String id;
    private String name;
    private final String address;

    private final List<Charger> chargers = new ArrayList<>();
    private final List<Tariff> tariffs = new ArrayList<>();

    public Location(String id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addCharger(Charger charger) {
        chargers.add(charger);
    }

    public List<Charger> getChargers() {
        return new ArrayList<>(chargers);
    }

    public Charger findChargerByNumber(String number) {
        for (Charger charger : chargers) {
            if (charger.getNumber().equals(number)) {
                return charger;
            }
        }
        return null;
    }


    public void addTariff(Tariff tariff) {
        tariffs.add(tariff);
    }

    public List<Tariff> getTariffs() {
        return new ArrayList<>(tariffs);
    }

    public void removeChargerByNumber(String number) {
        chargers.removeIf(c -> c.getNumber().equals(number));
    }


}



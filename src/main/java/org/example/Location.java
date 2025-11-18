package org.example;

import java.util.ArrayList;
import java.util.List;

public class Location {

    private final String id;          // z.B. "LOC-001"
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

    public void addTariff(Tariff tariff) {
        tariffs.add(tariff);
    }

    public List<Tariff> getTariffs() {
        return new ArrayList<>(tariffs);
    }
}



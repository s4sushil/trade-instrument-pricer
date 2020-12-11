package com.mizuho.model;

import java.util.Objects;

public class Vendor implements Data {

    private final Long id;
    private final String name;

    public Vendor(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Vendor{ " +
                "id=" + id +
                ", name='" + name + '\'' + '}';
    }

    @Override
    public boolean equals(Data obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Vendor)) {
            return false;
        }
        Vendor that = (Vendor) obj;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}

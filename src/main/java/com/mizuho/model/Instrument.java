package com.mizuho.model;

import java.util.Objects;

public class Instrument implements Data {

    private final Long id;
    private final String instrumentName;

    public Instrument(Long id, String instrumentName) {
        this.id = id;
        this.instrumentName = instrumentName;
    }

    public Long getId() {
        return id;
    }

    public String getInstrumentName() {
        return instrumentName;
    }

    @Override
    public String toString() {
        return "Instrument[" +
                "id='" + id + '\'' +
                "symbol='" + instrumentName + '\'' + ']';
    }

    @Override
    public boolean equals(Data obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Instrument)) {
            return false;
        }
        Instrument that = (Instrument) obj;
        return Objects.equals(id, that.id) && Objects.equals(instrumentName, that.instrumentName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instrumentName, id);
    }

}

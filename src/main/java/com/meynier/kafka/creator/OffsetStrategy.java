package com.meynier.kafka.creator;

public enum OffsetStrategy {
    EARLIEST,LATEST;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}

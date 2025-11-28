package com.library.model;

import java.io.Serial;
import java.io.Serializable;

public abstract class Item implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected String id;
    protected String title;
    protected String publisher;
    protected int year;

    public Item(String id, String title, String publisher, int year) {
        this.id = id;
        this.title = title;
        this.publisher = publisher;
        this.year = year;
    }

    public String getId() {
        return id;
    }

    // Yeni: id setter eklendi (repository'lerin id ataması için)
    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getPublisher() {
        return publisher;
    }

    public int getYear() {
        return year;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%d) - %s", id, title, year, publisher);
    }
}

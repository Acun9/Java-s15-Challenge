package com.library.model;

import java.io.Serial;
import java.io.Serializable;

public abstract class Person implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected String id;
    protected String name;
    protected String email;

    public Person(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    // Yeni: id setter eklendi (repository'lerin id ataması için)
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        // ID'yi de göstererek console'da kullanıcıyı rahatça referans alalım
        return String.format("%s | %s (%s)", id == null ? "(no-id)" : id, name, email);
    }
}

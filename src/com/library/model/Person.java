package com.library.model;

import java.io.Serial;
import java.io.Serializable;

public abstract class Person implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected String id;      // sistemdeki benzersiz kimlik
    protected String name;    // ad soyad
    protected String email;   // iletişim e-postası

    public Person(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

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
        return String.format("Ad: %s | E-posta: %s", name, email);
    }
}

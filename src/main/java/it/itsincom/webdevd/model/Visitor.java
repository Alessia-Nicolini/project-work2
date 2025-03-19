package it.itsincom.webdevd.model;

import java.util.Objects;

public class Visitor {
    private int id;
    private String name;
    private String surname;
    private String phone;
    private String email;

    public Visitor(int id, String name, String surname, String phone, String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
    }
    public int getId() {return id;}
    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }
    public String getPhone() {
        return phone;
    }
    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Visitor visitor = (Visitor) o;
        return Objects.equals(name, visitor.name) && Objects.equals(surname, visitor.surname) && Objects.equals(phone, visitor.phone) && Objects.equals(email, visitor.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, phone, email);
    }

    public void setId(int newId) {
        id = newId;
    }

}


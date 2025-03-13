package it.itsincom.webdevd.models;

import it.itsincom.webdevd.models.enums.Department;

import java.util.Objects;

public class Employee {

    private final int id;
    private final String firstName;
    private final String lastName;
    private String email;
    private String password;
    private Department department;

    public Employee(int id, String firstName, String lastName, String email, String password, Department department) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.department = department;
    }

    public Employee(String id, String firstName, String lastName, String email, String password, String department) {
        this.id = Integer.parseInt(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.department = Department.valueOf(department);
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id == employee.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return id + "," + firstName + "," + lastName + "," + email + "," + password + "," + department;
    }
}

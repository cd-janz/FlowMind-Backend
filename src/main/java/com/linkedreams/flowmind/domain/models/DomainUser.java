package com.linkedreams.flowmind.domain.models;

import java.util.ArrayList;
import java.util.Collection;

public class DomainUser {
    private final String id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final String username;
    private final String phoneNumber;
    private final Collection<DomainModule> modules;
    private DomainUser(String id, String firstName, String lastName, String email, String password, String username, String phoneNumber) {
        if (firstName == null || firstName.isBlank()) throw new IllegalArgumentException("first name cannot be null or blank");
        if (lastName == null || lastName.isBlank()) throw new IllegalArgumentException("last name cannot be null or blank");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("email cannot be null or blank");
        if (password == null || password.isBlank()) throw new IllegalArgumentException("password cannot be null or blank");
        if (username == null || username.isBlank()) throw new IllegalArgumentException("username cannot be null or blank");
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.modules = new ArrayList<>();
    }
    public void addModule(DomainModule module) {
        if(module == null) throw new IllegalArgumentException("module cannot be null");
        modules.add(module);
    }
    public static DomainUser createMinimal(String firstName, String lastName, String email, String password, String username) {
        return new DomainUser(null, firstName, lastName, email, password, username, null);
    }
    public static DomainUser createFull(String id, String firstName, String lastName, String email, String password, String username, String phoneNumber) {
        return new DomainUser(id, firstName, lastName, email, password, username, phoneNumber);
    }
    public static DomainUser create(String firstName, String lastName, String email, String password, String username, String phoneNumber) {
        return new DomainUser(null, firstName, lastName, email, password, username, phoneNumber);
    }
    public static DomainUser createWithoutPhone(String id, String firstName, String lastName, String email, String password, String username) {
        return new DomainUser(id, firstName, lastName, email, password, username, null);
    }
    public String getId() {
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
    public String getPassword() {
        return password;
    }
    public String getUsername() {
        return username;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public Collection<DomainModule> getModules() {
        return modules;
    }
}

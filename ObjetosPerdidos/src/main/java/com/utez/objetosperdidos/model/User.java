package com.utez.objetosperdidos.model;

public class User {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String matricula;
    private String rol;

    public User(String name, String email, String password, String phoneNumber, String matricula, String rol) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.matricula = matricula;
        this.rol = rol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getRole() {
        return rol;
    }

    public void setRole(String role) {
        this.rol = rol;
    }
    
}
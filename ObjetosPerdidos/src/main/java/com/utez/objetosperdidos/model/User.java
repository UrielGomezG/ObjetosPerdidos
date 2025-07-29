package com.utez.objetosperdidos.model;

public class User {
    private int id;
    private String name;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String email;
    private String password;
    private String phoneNumber;
    private String matricula;
    private int rol;

   public User(int id, String name, String email, String password, String phoneNumber, String matricula, int rol, String apellidoPaterno, String apellidoMaterno) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.password = password;
    this.phoneNumber = phoneNumber;
    this.matricula = matricula;
    this.rol = rol;
    this.apellidoPaterno = apellidoPaterno;
    this.apellidoMaterno = apellidoMaterno;
}

public User(int id, String name, String email, String password, String phoneNumber, String matricula, int rol) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.password = password;
    this.phoneNumber = phoneNumber;
    this.matricula = matricula;
    this.rol = rol;
}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
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

    public int getRole() {
        return rol;
    }

    public void setRole(int rol) {
        this.rol = rol;
    }
}
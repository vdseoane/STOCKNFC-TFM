package com.example.victor.stocknfc.VOs;

import java.util.Date;

/**
 * Created by Victor on 24/03/2018.
 */

public class Usuario {

    private String nombre;
    private String pass;
    private String rol;



    public Usuario(){};

    public Usuario(String nombre, String pass, String rol) {
        this.nombre = nombre;
        this.pass = pass;
        this.rol = rol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}

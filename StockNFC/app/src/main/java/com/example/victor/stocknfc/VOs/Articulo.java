package com.example.victor.stocknfc.VOs;

import java.sql.Blob;
import java.util.Date;

/**
 * Created by Victor on 18/03/2018.
 */

public class Articulo {

    private String nombre;
    private int stock;
    private int id;
    private int alerta_stock;
    private Date fecha_creacion;
    private float precio;
    private byte[] imagen_articulo;


    public Articulo(){};

    public Articulo(String nombre, int stock, int id, int alerta_stock, Date fecha_creacion, float precio, byte[] imagen_articulo) {
        this.nombre = nombre;
        this.stock = stock;
        this.id = id;
        this.alerta_stock = alerta_stock;
        this.fecha_creacion = fecha_creacion;
        this.precio = precio;
        this.imagen_articulo = imagen_articulo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAlerta_stock(int alerta_stock) {
        this.alerta_stock = alerta_stock;
    }

    public void setFecha_creacion(Date fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public void setImagen_articulo(byte[] imagen_articulo) {
        this.imagen_articulo = imagen_articulo;
    }

    public String getNombre() {

        return nombre;
    }

    public int getStock() {
        return stock;
    }

    public int getId() {
        return id;
    }

    public int getAlerta_stock() {
        return alerta_stock;
    }

    public Date getFecha_creacion() {
        return fecha_creacion;
    }

    public float getPrecio() {
        return precio;
    }

    public byte[] getImagen_articulo() {
        return imagen_articulo;
    }
}

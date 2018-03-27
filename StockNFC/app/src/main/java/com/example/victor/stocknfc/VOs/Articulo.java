package com.example.victor.stocknfc.VOs;

import java.util.Date;

/**
 * Created by Victor on 18/03/2018.
 */

public class Articulo {

    private String nombre;
    private int stock;
    private int id;
    private int alerta_stock;
    private String fecha_creacion;
    private float precio;
    private byte[] imagen_articulo;
    private String proveedor;


    public Articulo(){};

//    public Articulo(String nombre, int stock, int alerta_stock, String fecha_creacion, float precio, byte[] imagen_articulo, String proveedor) {
//        this.nombre = nombre;
//        this.stock = stock;
//        this.alerta_stock = alerta_stock;
//        this.fecha_creacion = fecha_creacion;
//        this.precio = precio;
//        this.imagen_articulo = imagen_articulo;
//        this.proveedor = proveedor;
//    }


    public Articulo(int id, String nombre, int stock, int alerta_stock, String fecha_creacion, float precio, byte[] imagen_articulo, String proveedor) {
        this.nombre = nombre;
        this.stock = stock;
        this.id = id;
        this.alerta_stock = alerta_stock;
        this.fecha_creacion = fecha_creacion;
        this.precio = precio;
        this.imagen_articulo = imagen_articulo;
        this.proveedor = proveedor;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public Articulo(String nombre) {
        this.nombre = nombre;
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

    public void setFecha_creacion(String fecha_creacion) {
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

    public String getFecha_creacion() {
        return fecha_creacion;
    }

    public float getPrecio() {
        return precio;
    }

    public byte[] getImagen_articulo() {
        return imagen_articulo;
    }
}

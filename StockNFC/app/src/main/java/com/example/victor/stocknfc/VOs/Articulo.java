package com.example.victor.stocknfc.VOs;

import java.util.Date;

/**
 * Created by Victor on 18/03/2018.
 */

public class Articulo {

    private String nombre;
    private int stock;
    private int id;
    private int alertaStock;
    private String fechaCreacion;
    private float precio;
    private byte[] imagenArticulo;
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


    public Articulo(int id, String nombre, int stock, int alertaStock, String fechaCreacion, float precio, byte[] imagenArticulo, String proveedor) {
        this.nombre = nombre;
        this.stock = stock;
        this.id = id;
        this.alertaStock = alertaStock;
        this.fechaCreacion = fechaCreacion;
        this.precio = precio;
        this.imagenArticulo = imagenArticulo;
        this.proveedor = proveedor;
    }

    public Articulo(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAlertaStock() {
        return alertaStock;
    }

    public void setAlertaStock(int alertaStock) {
        this.alertaStock = alertaStock;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public byte[] getImagenArticulo() {
        return imagenArticulo;
    }

    public void setImagenArticulo(byte[] imagenArticulo) {
        this.imagenArticulo = imagenArticulo;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }
}

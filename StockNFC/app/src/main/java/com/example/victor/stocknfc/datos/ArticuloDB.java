package com.example.victor.stocknfc.datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.victor.stocknfc.VOs.Articulo;

import java.sql.Blob;
import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by Victor on 18/03/2018.
 */

public class ArticuloDB extends StockNFCDataBase{

    public ArticuloDB(Context context) {
        super(context);
    }

    public void insertarArticulo(SQLiteDatabase db, String nombre,
                                 int stock){
        ContentValues values = new ContentValues();
        values.put(ConstantesArticulo.NOMBRE_ARTICULO, nombre);
        values.put(ConstantesArticulo.STOCK_ARTICULO, stock);
        db.insert(ConstantesArticulo.ARTICULO_TABLE_NAME, null, values);
    }

    public int editarArticulo(SQLiteDatabase db, int stock, int id){
        ContentValues values = new ContentValues();
        values.put(ConstantesArticulo.STOCK_ARTICULO, stock);

        int cant = db.update(ConstantesArticulo.ARTICULO_TABLE_NAME, values, "[ID]= " + id, null);

        return cant;
    }

    public int getStock(SQLiteDatabase db, String nombre){
        /*db  = getReadableDatabase();
        String condicion = ConstantesArticulo.NOMBRE_ARTICULO + "= '" + nombre + "'";
        Articulo articulo = new Articulo();
        Cursor c = db.query(ConstantesArticulo.ARTICULO_TABLE_NAME,
                null, condicion, null, null, null, null);
        if (c.moveToFirst()){
            int nombreIndex = c.getColumnIndex(ConstantesArticulo.NOMBRE_ARTICULO);
            int stockIndex = c.getColumnIndex(ConstantesArticulo.STOCK_ARTICULO);
            int idIndex = c.getColumnIndex(ConstantesArticulo.ID_ARTICULO);
            int aletaStockIndex = c.getColumnIndex(ConstantesArticulo.ALERTA_STOCK);
            int fechaCreacionIndex = c.getColumnIndex(ConstantesArticulo.FECHA_CREACION);
            int precioIndex = c.getColumnIndex(ConstantesArticulo.PRECIO_ARTICULO);
            int imagenIndex = c.getColumnIndex(ConstantesArticulo.IMAGEN_ARTICULO);
            do {
                String nombreArticulo = c.getString(nombreIndex);
                int stockArticulo= c.getInt(stockIndex);
                int id = c.getInt(idIndex);
                int alertaStock = c.getInt(aletaStockIndex);
                //Date fechaCreacion = c.get(idIndex);
                float precioArticulo = c.getFloat(precioIndex);
                byte[] imagenArticulo = c.getBlob(imagenIndex);
                articulo = new Articulo(nombreArticulo, stockArticulo, id, alertaStock, null, precioArticulo, imagenArticulo);
            } while (c.moveToNext());
        }*/


        return obtenerArticulo(db, nombre).getStock();
    }

    public Articulo obtenerArticulo(SQLiteDatabase db, String nombre) {
        db  = getReadableDatabase();
        String condicion = ConstantesArticulo.NOMBRE_ARTICULO + "= '" + nombre + "'";
        Articulo articulo = new Articulo();
        Cursor c = db.query(ConstantesArticulo.ARTICULO_TABLE_NAME,
                null, condicion, null, null, null, null);
        if (c.moveToFirst()){
            int nombreIndex = c.getColumnIndex(ConstantesArticulo.NOMBRE_ARTICULO);
            int stockIndex = c.getColumnIndex(ConstantesArticulo.STOCK_ARTICULO);
            int idIndex = c.getColumnIndex(ConstantesArticulo.ID_ARTICULO);
            int aletaStockIndex = c.getColumnIndex(ConstantesArticulo.ALERTA_STOCK);
            int fechaCreacionIndex = c.getColumnIndex(ConstantesArticulo.FECHA_CREACION);
            int precioIndex = c.getColumnIndex(ConstantesArticulo.PRECIO_ARTICULO);
            int imagenIndex = c.getColumnIndex(ConstantesArticulo.IMAGEN_ARTICULO);
            do {
                String nombreArticulo = c.getString(nombreIndex);
                int stockArticulo= c.getInt(stockIndex);
                int id = c.getInt(idIndex);
                int alertaStock = c.getInt(aletaStockIndex);
                //Date fechaCreacion = c.get(idIndex);
                float precioArticulo = c.getFloat(precioIndex);
                byte[] imagenArticulo = c.getBlob(imagenIndex);
                //articulo = new Articulo(nombreArticulo, stockArticulo, id, alertaStock, null, precioArticulo, imagenArticulo);
            } while (c.moveToNext());
        }
        return articulo;
    }

    public long insertarArticulo(SQLiteDatabase db, Articulo articulo) {
        ContentValues values = new ContentValues();
        values.put(ConstantesArticulo.NOMBRE_ARTICULO, articulo.getNombre());
        values.put(ConstantesArticulo.STOCK_ARTICULO, articulo.getStock());
        values.put(ConstantesArticulo.ALERTA_STOCK, articulo.getAlertaStock());
        values.put(ConstantesArticulo.FECHA_CREACION, articulo.getFechaCreacion());
        values.put(ConstantesArticulo.IMAGEN_ARTICULO, articulo.getImagenArticulo());
        values.put(ConstantesArticulo.PROVEEDOR_ARTICULO, articulo.getStock());
        values.put(ConstantesArticulo.PRECIO_ARTICULO, articulo.getPrecio());
        long toret = db.insert(ConstantesArticulo.ARTICULO_TABLE_NAME, null, values);

        return toret;
    }

    public ArrayList<Articulo> obtenerArticulos(SQLiteDatabase db) {
        db  = getReadableDatabase();
        ArrayList<Articulo> listaArticulos = new ArrayList<Articulo>();
        Articulo articulo = new Articulo();
        Cursor c = db.query(ConstantesArticulo.ARTICULO_TABLE_NAME,
                null, null, null, null, null, null);
        if (c.moveToFirst()){
            int nombreIndex = c.getColumnIndex(ConstantesArticulo.NOMBRE_ARTICULO);
            int stockIndex = c.getColumnIndex(ConstantesArticulo.STOCK_ARTICULO);
            int idIndex = c.getColumnIndex(ConstantesArticulo.ID_ARTICULO);
            int aletaStockIndex = c.getColumnIndex(ConstantesArticulo.ALERTA_STOCK);
            int fechaCreacionIndex = c.getColumnIndex(ConstantesArticulo.FECHA_CREACION);
            int precioIndex = c.getColumnIndex(ConstantesArticulo.PRECIO_ARTICULO);
            int imagenIndex = c.getColumnIndex(ConstantesArticulo.IMAGEN_ARTICULO);
            int proveedorIndex = c.getColumnIndex(ConstantesArticulo.PROVEEDOR_ARTICULO);
            do {
                String nombreArticulo = c.getString(nombreIndex);
                int stockArticulo= c.getInt(stockIndex);
                int id = c.getInt(idIndex);
                int alertaStock = c.getInt(aletaStockIndex);
                String fechaCreacion = c.getString(idIndex);
                float precioArticulo = c.getFloat(precioIndex);
                String proveedorArticulo = c.getString(proveedorIndex);
                byte[] imagenArticulo = c.getBlob(imagenIndex);
                articulo = new Articulo(id, nombreArticulo, stockArticulo, alertaStock, fechaCreacion, precioArticulo, imagenArticulo, proveedorArticulo);
                listaArticulos.add(articulo);
            } while (c.moveToNext());
        }
        return listaArticulos;
    }
}

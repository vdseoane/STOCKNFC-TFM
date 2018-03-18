package com.example.victor.stocknfc.datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.victor.stocknfc.VOs.Articulo;

import java.sql.Blob;
import java.sql.Date;

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
                articulo = new Articulo(nombreArticulo, stockArticulo, id, alertaStock, null, precioArticulo, imagenArticulo);
            } while (c.moveToNext());
        }
        return articulo;
    }
}

package com.example.victor.stocknfc.datos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Victor on 18/03/2018.
 */

public class StockNFCDataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "stockNFCDataBase";

    /*
    public static final String TABLE_NAME_PROVEEDOR = "proveedor";
    public static final String TABLE_NAME_USUARIO = "usuario";
    */


    private static final int DATABASE_VERSION = 1;

    public StockNFCDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creamos las tablas

        //Articulo
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ConstantesArticulo.ARTICULO_TABLE_NAME + " (" +
                ConstantesArticulo.ID_ARTICULO + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ConstantesArticulo.NOMBRE_ARTICULO + " TEXT NOT NULL, " +
                ConstantesArticulo.ALERTA_STOCK + " INTEGER, " +
                ConstantesArticulo.FECHA_CREACION + " DATE NOT NULL, " +
                ConstantesArticulo.PRECIO_ARTICULO + " FLOAT, " +
                ConstantesArticulo.IMAGEN_ARTICULO + " BLOB, " +
                ConstantesArticulo.STOCK_ARTICULO + " INTEGER NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            //Eliminamos las tablas para volver a crearlas
            db.execSQL("DROP TABLE IF EXISTS " + ConstantesArticulo.ARTICULO_TABLE_NAME + ";");
            onCreate(db);
        }
    }
}

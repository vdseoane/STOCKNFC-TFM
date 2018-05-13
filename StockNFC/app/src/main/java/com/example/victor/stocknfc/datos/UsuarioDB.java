package com.example.victor.stocknfc.datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;

import com.example.victor.stocknfc.VOs.Articulo;
import com.example.victor.stocknfc.VOs.Usuario;

import java.util.ArrayList;

import javax.mail.internet.InternetAddress;

/**
 * Created by Victor on 24/03/2018.
 */

public class UsuarioDB extends StockNFCDataBase {
    public UsuarioDB(Context context) {
        super(context);
    }



    public void insertarUsuario(SQLiteDatabase db, String nombre, String rol, String pass){
        ContentValues values = new ContentValues();
        values.put(ConstantesUsuario.NOMBRE_USUARIO, nombre);
        values.put(ConstantesUsuario.ROL_USUARIO, rol);
        values.put(ConstantesUsuario.PASS_USUARIO, pass);
        db.insert(ConstantesUsuario.USUARIO_TABLE_NAME, null, values);
    }

    public Usuario obtenerUsuario(SQLiteDatabase db, String nombre, String pass){
        db  = getReadableDatabase();
        String condicion = ConstantesUsuario.NOMBRE_USUARIO+ "= '" + nombre+ "'";
        Usuario usuario = null;
        Cursor c = db.query(ConstantesUsuario.USUARIO_TABLE_NAME,
                null, condicion, null, null, null, null);
        if (c.moveToFirst()){
            int nombreIndex = c.getColumnIndex(ConstantesUsuario.NOMBRE_USUARIO);
            int rolIndex = c.getColumnIndex(ConstantesUsuario.ROL_USUARIO);
            int passIndex = c.getColumnIndex(ConstantesUsuario.PASS_USUARIO);
            do {
                String nombreUsuario = c.getString(nombreIndex);
                String passUsuario = c.getString(passIndex);
                String rolUsuario = c.getString(rolIndex);
                usuario = new Usuario(nombreUsuario, passUsuario, rolUsuario);
            } while (c.moveToNext());
        }
        return usuario;

    }

    public ArrayList<String> obtenerAdministradores(SQLiteDatabase db) {
        db  = getReadableDatabase();
        String condicion = ConstantesUsuario.ROL_USUARIO+ "= 'Administrador'" ;
        ArrayList<String> emails = new ArrayList<String>();
        Cursor c = db.query(ConstantesUsuario.USUARIO_TABLE_NAME,
                null, condicion, null, null, null, null);
        if (c.moveToFirst()){
            int emailIndex = c.getColumnIndex(ConstantesUsuario.NOMBRE_USUARIO);

            do {
                String emailUsuario = c.getString(emailIndex);

                emails.add(emailUsuario);
            } while (c.moveToNext());
        }

        return emails;
    }
}

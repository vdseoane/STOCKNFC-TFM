package com.example.victor.stocknfc.datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.victor.stocknfc.VOs.Articulo;
import com.example.victor.stocknfc.VOs.Usuario;

/**
 * Created by Victor on 24/03/2018.
 */

public class UsuarioDB extends StockNFCDataBase {
    public UsuarioDB(Context context) {
        super(context);
    }

    public void insertarUsuario(SQLiteDatabase db, String nombre, String email, String rol, String pass){
        ContentValues values = new ContentValues();
        values.put(ConstantesUsuario.NOMBRE_USUARIO, nombre);
        values.put(ConstantesUsuario.EMAIL_USUARIO, email);
        values.put(ConstantesUsuario.ROL_USUARIO, rol);
        values.put(ConstantesUsuario.PASS_USUARIO, pass);
        db.insert(ConstantesUsuario.USUARIO_TABLE_NAME, null, values);
    }

    public Usuario obtenerUsuario(SQLiteDatabase db, String email, String pass){
        db  = getReadableDatabase();
        String condicion = ConstantesUsuario.EMAIL_USUARIO+ "= '" + email+ "'";
        Usuario usuario = null;
        Cursor c = db.query(ConstantesUsuario.USUARIO_TABLE_NAME,
                null, condicion, null, null, null, null);
        if (c.moveToFirst()){
            int nombreIndex = c.getColumnIndex(ConstantesUsuario.NOMBRE_USUARIO);
            int emailIndex = c.getColumnIndex(ConstantesUsuario.EMAIL_USUARIO);
            int rolIndex = c.getColumnIndex(ConstantesUsuario.ROL_USUARIO);
            int passIndex = c.getColumnIndex(ConstantesUsuario.PASS_USUARIO);
            do {
                String nombreUsuario = c.getString(nombreIndex);
                String emailUsuario = c.getString(emailIndex);
                String passUsuario = c.getString(passIndex);
                String rolUsuario = c.getString(rolIndex);
                usuario = new Usuario(nombreUsuario, passUsuario, emailUsuario, rolUsuario);
            } while (c.moveToNext());
        }
        return usuario;

    }
}

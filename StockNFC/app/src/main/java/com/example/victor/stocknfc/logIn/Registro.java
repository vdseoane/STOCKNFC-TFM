package com.example.victor.stocknfc.logIn;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.victor.stocknfc.R;
import com.example.victor.stocknfc.datos.StockNFCDataBase;
import com.example.victor.stocknfc.datos.UsuarioDB;

public class Registro extends AppCompatActivity {
    public StockNFCDataBase bd;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        bd = new StockNFCDataBase(context);
    }

    public void onClick(View v) throws InterruptedException {
        //Toast.makeText(this, "Boton registrar pulsado", Toast.LENGTH_LONG).show();

        TextView txtNombreUsuario = findViewById(R.id.usuarioRegistrar);
        TextView txtEmailUsuario = findViewById(R.id.emailRegistrar);
        TextView txtPassUsuario = findViewById(R.id.passRegistrar);
        TextView txtRolUsuario = findViewById(R.id.rolRegistrar);

        UsuarioDB dbUsuario = new UsuarioDB(context);

        dbUsuario.insertarUsuario(bd.getWritableDatabase(), txtNombreUsuario.getText().toString(), txtEmailUsuario.getText().toString(), txtRolUsuario.getText().toString(), txtPassUsuario.getText().toString());

        Toast.makeText(this, "Usuario guardado", Toast.LENGTH_LONG).show();
    }
}

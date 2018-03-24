package com.example.victor.stocknfc.logIn;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.victor.stocknfc.R;
import com.example.victor.stocknfc.VOs.*;
import com.example.victor.stocknfc.VOs.Usuario;
import com.example.victor.stocknfc.datos.StockNFCDataBase;
import com.example.victor.stocknfc.datos.UsuarioDB;


public class LogIn extends AppCompatActivity {

    public StockNFCDataBase bd;
    Context context = this;

    TextView textoRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        bd = new StockNFCDataBase(context);
        //Crear el evento al pulsar sobre el texto registrar
        textoRegistrar = findViewById(R.id.textoRegistrar);

        textoRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRegistrar = new Intent(LogIn.this, Registro.class);
                LogIn.this.startActivity(intentRegistrar);
            }
        });

    }

    public void onClick(View v) {
        Toast.makeText(this, "Boton pulsado", Toast.LENGTH_LONG).show();

        TextView txtUsuario = findViewById(R.id.usuarioTxt);
        TextView txtPassUsuario = findViewById(R.id.passTxt);
 Usuario usuarioObtenido = null;
        UsuarioDB dbUsuario = new UsuarioDB(context);

        usuarioObtenido = dbUsuario.obtenerUsuario(bd.getWritableDatabase(), txtUsuario.getText().toString(), txtPassUsuario.getText().toString());
if(usuarioObtenido != null){
    if(usuarioObtenido.getEmail().equals(txtUsuario.getText().toString()) && usuarioObtenido.getPass().equals(txtPassUsuario.getText().toString())){
        Toast.makeText(this, "Usuario correcto, entrando", Toast.LENGTH_LONG).show();
    }

}

    }

}

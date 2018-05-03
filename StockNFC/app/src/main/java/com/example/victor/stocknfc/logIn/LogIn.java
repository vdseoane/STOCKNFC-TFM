package com.example.victor.stocknfc.logIn;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.victor.stocknfc.Dialogo;
import com.example.victor.stocknfc.MainActivity;
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
                //Vamos a la pantalla de registro de usuario
                Intent intentRegistrar = new Intent(LogIn.this, Registro.class);
                LogIn.this.startActivity(intentRegistrar);
            }
        });

    }

    public void onClick(View v) {
        TextView txtUsuario = findViewById(R.id.usuarioTxt);
        TextView txtPassUsuario = findViewById(R.id.passTxt);
        Usuario usuarioObtenido = null;
        UsuarioDB dbUsuario = new UsuarioDB(context);

//        usuarioObtenido = dbUsuario.obtenerUsuario(bd.getWritableDatabase(), txtUsuario.getText().toString(), txtPassUsuario.getText().toString());
//        if (usuarioObtenido != null) {
//            if (usuarioObtenido.getEmail().equals(txtUsuario.getText().toString()) && usuarioObtenido.getPass().equals(txtPassUsuario.getText().toString())) {
//                //Usuario correcto, mostramos la pantalla principal
//                Toast.makeText(this, "Usuario correcto", Toast.LENGTH_SHORT).show();
//                //Intent, enviamos los datos del usuario (nombre e email)
//                Intent intentPaginaPrincipal = new Intent(LogIn.this, MainActivity.class);
//                intentPaginaPrincipal.putExtra("nombreUsuario", usuarioObtenido.getNombre());
//                intentPaginaPrincipal.putExtra("emailUsuario", usuarioObtenido.getEmail());
//                LogIn.this.startActivity(intentPaginaPrincipal);
//            } else if (!usuarioObtenido.getPass().equals(txtPassUsuario.getText().toString())) {
//                //Toast.makeText(this, "La contraseña no es correcta", Toast.LENGTH_LONG).show();
//                Dialogo dialogo = new Dialogo(context, "La contraseña no es correcta");
//                dialogo.getDialogo().show();
//            }
//        } else {
//            //Toast.makeText(this, "No existe ningún usuario registrado con ese email", Toast.LENGTH_LONG).show();
//            Dialogo dialogo = new Dialogo(context, "No existe ningún usuario registrado con ese email");
//            dialogo.getDialogo().show();
//        }

        Intent intentPaginaPrincipal = new Intent(LogIn.this, MainActivity.class);
               intentPaginaPrincipal.putExtra("nombreUsuario", "prueba");
                intentPaginaPrincipal.putExtra("emailUsuario", "email@prueba");
                LogIn.this.startActivity(intentPaginaPrincipal);
        finish();
    }

}

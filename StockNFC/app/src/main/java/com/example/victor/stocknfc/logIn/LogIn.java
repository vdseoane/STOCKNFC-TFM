package com.example.victor.stocknfc.logIn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.victor.stocknfc.R;


public class LogIn extends AppCompatActivity {

    TextView textoRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

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


}

package com.example.victor.stocknfc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Victor on 26/03/2018.
 */

public class Dialogo {

    AlertDialog.Builder builder;
    AlertDialog dialogo;

    public Dialogo(Context context, String mensaje) {
        builder = new AlertDialog.Builder(context);
        builder.setMessage(mensaje);
        builder.setPositiveButton(com.example.victor.stocknfc.R.string.aceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
    }

    public AlertDialog.Builder getBuilder() {
        return builder;
    }

    public AlertDialog getDialogo() {
        return builder.create();
    }
}

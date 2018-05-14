package com.example.victor.stocknfc;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.NavigationView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

/**
 * Created by Victor on 23/04/2018.
 */

public class Utilidades {

    public void esconderTeclado(Activity activity, Context context){
        View view = activity.getCurrentFocus();
        view.clearFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static String obtenerUsuarioActual(Activity activity) {
        NavigationView navigationView = (NavigationView) activity.findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        TextView emailUsuario = (TextView) hView.findViewById(R.id.emailUsuarioMenu);
        return emailUsuario.getText().toString();
    }
}

package com.example.victor.stocknfc.fragmetos;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.victor.stocknfc.R;

import java.text.DateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragmento_Articulo extends android.support.v4.app.Fragment {

    EditText nombreArticulo;
    EditText stockArticulo;
    EditText alertaArticulo;
    EditText precioArticulo;
    EditText fechaArticulo;
    EditText proveedorArticulo;
    ImageView imgArticulo;


    public Fragmento_Articulo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_articulo, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Date fechaArticuloDate = new Date();
        DateFormat formatter = android.text.format.DateFormat.getDateFormat(getContext());

        nombreArticulo = getView().findViewById(R.id.nombreArticulo);
        stockArticulo = getView().findViewById(R.id.stockArticulo);
        alertaArticulo = getView().findViewById(R.id.alertaArticulo);
        precioArticulo = getView().findViewById(R.id.precioArticulo);
        proveedorArticulo = getView().findViewById(R.id.proveedorArticulo);
        imgArticulo = getView().findViewById(R.id.imgArticulo);

        fechaArticulo.setText(formatter.format(fechaArticuloDate).toString());

    }
}

package com.example.victor.stocknfc.fragmetos;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.victor.stocknfc.Dialogo;
import com.example.victor.stocknfc.R;
import com.example.victor.stocknfc.VOs.Articulo;
import com.example.victor.stocknfc.datos.ArticuloDB;
import com.example.victor.stocknfc.datos.StockNFCDataBase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragmento_Articulo extends android.support.v4.app.Fragment {

    private StockNFCDataBase bd;
    ArticuloDB bdArticulo;

    EditText nombreArticulo;
    EditText stockArticulo;
    EditText alertaArticulo;
    EditText precioArticulo;
    EditText fechaArticulo;
    EditText proveedorArticulo;
    ImageView imgArticulo;
    Uri imgUri;
    FloatingActionButton btnGuardarArticulo;
    SimpleDateFormat fechaFormat = new SimpleDateFormat("dd-MM-yyyy");
    Date fechaArticuloDate = new Date();

    private static final int OBTENER_IMAGEN = 100;


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

        String fechaArt = fechaFormat.format(fechaArticuloDate);

        obtenerTextViews();
        crearOnClicks();

        fechaArticulo.setText(fechaArt);
    }

    private void crearOnClicks() {
        imgArticulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirGaleria();
            }
        });


        btnGuardarArticulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Articulo articulo;
                try {
                    articulo = obtenerDatosGuardar();
                    guardarArticulo(articulo);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void guardarArticulo(Articulo articulo) {
        bdArticulo = new ArticuloDB(getContext());

        long inserccion = bdArticulo.insertarArticulo(bdArticulo.getWritableDatabase(), articulo);

        if(inserccion != -1){
            Toast.makeText(getContext(), "Articulo añadido correctamente", Toast.LENGTH_LONG).show();
        }else{
            Dialogo dialogo = new Dialogo(getContext(), "Ha habido un error al insertar el artículo");
            dialogo.getDialogo().show();
        }
    }

    private Articulo obtenerDatosGuardar() throws ParseException {
        return new Articulo(0, nombreArticulo.getText().toString(), Integer.parseInt(stockArticulo.getText().toString()), Integer.parseInt(alertaArticulo.getText().toString()), fechaArticulo.getText().toString(), Float.parseFloat(precioArticulo.getText().toString()), null, proveedorArticulo.getText().toString());
    }

    private void abrirGaleria() {
        Intent abrirGaleria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(abrirGaleria, OBTENER_IMAGEN);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == OBTENER_IMAGEN){
            imgUri = data.getData();
            imgArticulo.setImageURI(imgUri);
        }
    }

    private void obtenerTextViews() {
        nombreArticulo = getView().findViewById(R.id.nombreArticulo);
        stockArticulo = getView().findViewById(R.id.stockArticulo);
        alertaArticulo = getView().findViewById(R.id.alertaArticulo);
        precioArticulo = getView().findViewById(R.id.precioArticulo);
        proveedorArticulo = getView().findViewById(R.id.proveedorArticulo);
        imgArticulo = getView().findViewById(R.id.imgArticulo);
        fechaArticulo = getView().findViewById(R.id.fechaArticulo);
        btnGuardarArticulo = getView().findViewById(R.id.btnGuardarArticulo);
    }
}

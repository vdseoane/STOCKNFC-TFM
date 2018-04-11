package com.example.victor.stocknfc.fragmetos;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
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
import com.example.victor.stocknfc.Validaciones;
import com.example.victor.stocknfc.datos.ArticuloDB;
import com.example.victor.stocknfc.datos.StockNFCDataBase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragmento_Articulo extends android.support.v4.app.Fragment {

    Validaciones validaciones = new Validaciones();
    Dialogo dialogo;

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


    //Datos articulo
    Articulo articulo = new Articulo();
    String nombre;
    int stock;
    int alerta;
    String fecha;
    float precio;
    String proveedor;
    byte[] imageInByte;

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
                try {
                    if(comprobarArticuloCorrecto()){
                        obtenerDatosGuardar();
                        guardarArticulo();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void guardarArticulo() {
        bdArticulo = new ArticuloDB(getContext());

        long inserccion = bdArticulo.insertarArticulo(bdArticulo.getWritableDatabase(), articulo);

        if(inserccion != -1){
            Toast.makeText(getContext(), "Articulo añadido correctamente", Toast.LENGTH_LONG).show();
        }else{
            Dialogo dialogo = new Dialogo(getContext(), "Ha habido un error al insertar el artículo");
            dialogo.getDialogo().show();
        }
    }

    private void obtenerDatosGuardar() throws ParseException {
        articulo.setNombre(nombreArticulo.getText().toString());
        articulo.setStock(Integer.parseInt(stockArticulo.getText().toString()));
//        alerta = Integer.parseInt(alertaArticulo.getText().toString());
        articulo.setFechaCreacion(fechaArticulo.getText().toString());
//        precio = Float.parseFloat(precioArticulo.getText().toString());
        articulo.setProveedor(proveedorArticulo.getText().toString());

        //Obtenemos la imagen
//        BitmapDrawable bitmapDrawable = ((BitmapDrawable) imgArticulo.getDrawable());
//        Bitmap bitmap = bitmapDrawable .getBitmap();
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//        imageInByte = stream.toByteArray();
    }

    private void abrirGaleria() {
        Intent abrirGaleria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(abrirGaleria, OBTENER_IMAGEN);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == OBTENER_IMAGEN){
            imgUri = data.getData();
            imgArticulo.setBackground(null);
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

    private boolean comprobarArticuloCorrecto() throws ParseException {
        if(validaciones.textoNoNulo(nombreArticulo.getText().toString())){
            dialogo = new Dialogo(getContext(), getContext().getResources().getString(R.string.msnNombreNulo));
            dialogo.getBuilder().create().show();
            return false;
        }else if(validaciones.textoNoNulo(stockArticulo.getText().toString())){
            dialogo = new Dialogo(getContext(), getContext().getResources().getString(R.string.msnStockNulo));
            dialogo.getBuilder().create().show();
            return false;
        }else if(validaciones.numeroNegativo(Integer.parseInt(stockArticulo.getText().toString()))){
            dialogo = new Dialogo(getContext(), getContext().getResources().getString(R.string.msnStockNegativo));
            dialogo.getBuilder().create().show();
            return  false;
        }else {
            //Alerta
            if (validaciones.textoNoNulo(alertaArticulo.getText().toString())) {
//                dialogo = new Dialogo(getContext(), getContext().getResources().getString(R.string.msnAlertaNulo));
//                dialogo.getBuilder().create().show();
                articulo.setAlertaStock(-1);
            }else articulo.setAlertaStock(Integer.parseInt(alertaArticulo.getText().toString()));
            //Precio
            if (validaciones.textoNoNulo(precioArticulo.getText().toString())){
                articulo.setPrecio(0);
            } else articulo.setPrecio(Float.parseFloat(precioArticulo.getText().toString()));

            //Imagen
            //Obtenemos la imagen
            BitmapDrawable bitmapDrawable = ((BitmapDrawable) imgArticulo.getDrawable());

            if(validaciones.esNulo(bitmapDrawable)){
                //Metemos emoticono
                Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.user);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
                articulo.setImagenArticulo(stream.toByteArray());
            }else{
                Bitmap bitmap = bitmapDrawable .getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                articulo.setImagenArticulo(stream.toByteArray());
            }
            return true;
        }
    }
}

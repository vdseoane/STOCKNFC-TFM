package com.example.victor.stocknfc.fragmetos;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.victor.stocknfc.Dialogo;
import com.example.victor.stocknfc.R;
import com.example.victor.stocknfc.VOs.Articulo;
import com.example.victor.stocknfc.Validaciones;
import com.example.victor.stocknfc.datos.ArticuloDB;
import com.example.victor.stocknfc.datos.StockNFCDataBase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
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

    //Toolbar
    android.support.v7.widget.Toolbar toolbarAticulo;

    //Menu lateral
    DrawerLayout drawer;

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

    //Datos obtencion imagen
    private static final int ACTIVITY_SELECT_IMAGE = 1020, ACTIVITY_SELECT_FROM_CAMERA = 1040, MY_CAMERA_PERMISSION_CODE = 1050;

    public Fragmento_Articulo() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_articulo, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Toolbar
        toolbarAticulo = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbarArticulo);
        toolbarAticulo.setTitle("Articulo");
        toolbarAticulo.inflateMenu(R.menu.menu_articulo);

//Quitamos el menu lateral
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
//Fecha del día de hoy para el artículo
        String fechaArt = fechaFormat.format(fechaArticuloDate);
//Obtenemos los textviewa
        obtenerTextViews();
        //creamos los onClicks
        crearOnClicks();

        fechaArticulo.setText(fechaArt);
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

    private void crearOnClicks() {
        imgArticulo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                seleccionarImagenDialogo();
            }
        });

        btnGuardarArticulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (comprobarArticuloCorrecto()) {
                        obtenerDatosGuardar();
                        guardarArticulo();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        toolbarAticulo.setOnMenuItemClickListener(new android.support.v7.widget.Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.borrarArticuloMenu:
                        Toast.makeText(getContext(), "Boton borrar pulsado", Toast.LENGTH_SHORT).show();
                    case R.id.modificarArticuloMenu:
                        Toast.makeText(getContext(), "Boton modificar pulsado", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    private void seleccionarImagenDialogo() {
        try {
            PackageManager pm = getActivity().getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getActivity().getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, ACTIVITY_SELECT_FROM_CAMERA);
                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, ACTIVITY_SELECT_IMAGE);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else {
                if (getContext().checkSelfPermission(
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    getActivity().requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_PERMISSION_CODE);
                }
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, ACTIVITY_SELECT_IMAGE);
            } else {
                Toast.makeText(getContext(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void guardarArticulo() {
        bdArticulo = new ArticuloDB(getContext());

        long inserccion = bdArticulo.insertarArticulo(bdArticulo.getWritableDatabase(), articulo);

        if (inserccion != -1) {
            Toast.makeText(getContext(), "Articulo añadido correctamente", Toast.LENGTH_LONG).show();
        } else {
            Dialogo dialogo = new Dialogo(getContext(), "Ha habido un error al insertar el artículo");
            dialogo.getDialogo().show();
        }
    }

    private void obtenerDatosGuardar() throws ParseException {
        articulo.setNombre(nombreArticulo.getText().toString());
        articulo.setStock(Integer.parseInt(stockArticulo.getText().toString()));
        articulo.setFechaCreacion(fechaArticulo.getText().toString());
        articulo.setProveedor(proveedorArticulo.getText().toString());

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_SELECT_IMAGE && resultCode == getActivity().RESULT_OK) {
            imgUri = data.getData();
            imgArticulo.setBackground(null);
            imgArticulo.setImageURI(imgUri);
        } else if (requestCode == ACTIVITY_SELECT_FROM_CAMERA
                && resultCode == getActivity().RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imgArticulo.setBackground(null);
            imgArticulo.setImageBitmap(photo);
        }
    }

    private boolean comprobarArticuloCorrecto() throws ParseException {
        if (validaciones.textoNoNulo(nombreArticulo.getText().toString())) {
            dialogo = new Dialogo(getContext(), getContext().getResources().getString(R.string.msnNombreNulo));
            dialogo.getBuilder().create().show();
            return false;
        } else if (validaciones.textoNoNulo(stockArticulo.getText().toString())) {
            dialogo = new Dialogo(getContext(), getContext().getResources().getString(R.string.msnStockNulo));
            dialogo.getBuilder().create().show();
            return false;
        } else if (validaciones.numeroNegativo(Integer.parseInt(stockArticulo.getText().toString()))) {
            dialogo = new Dialogo(getContext(), getContext().getResources().getString(R.string.msnStockNegativo));
            dialogo.getBuilder().create().show();
            return false;
        } else {
            //Alerta
            if (validaciones.textoNoNulo(alertaArticulo.getText().toString())) {
                articulo.setAlertaStock(-1);
            } else articulo.setAlertaStock(Integer.parseInt(alertaArticulo.getText().toString()));
            //Precio
            if (validaciones.textoNoNulo(precioArticulo.getText().toString())) {
                articulo.setPrecio(0);
            } else articulo.setPrecio(Float.parseFloat(precioArticulo.getText().toString()));

            //Imagen
            //Obtenemos la imagen
            BitmapDrawable bitmapDrawable = ((BitmapDrawable) imgArticulo.getDrawable());
            if (!validaciones.esNulo(bitmapDrawable)) {
                Bitmap bitmap = bitmapDrawable.getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                articulo.setImagenArticulo(stream.toByteArray());
            }
            return true;
        }
    }
}

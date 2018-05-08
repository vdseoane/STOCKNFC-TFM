package com.example.victor.stocknfc.fragmetos;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.victor.stocknfc.Dialogo;
import com.example.victor.stocknfc.EscrituraActivity;
import com.example.victor.stocknfc.R;
import com.example.victor.stocknfc.Utilidades;
import com.example.victor.stocknfc.VOs.Articulo;
import com.example.victor.stocknfc.Validaciones;
import com.example.victor.stocknfc.datos.ArticuloDB;
import com.example.victor.stocknfc.datos.StockNFCDataBase;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Fragmento_Pedido extends android.support.v4.app.Fragment {
    Validaciones validaciones = new Validaciones();
    Dialogo dialogo;

    //Utilidades
    Utilidades utilidades = new Utilidades();

    //Toolbar
    android.support.v7.widget.Toolbar toolbarAticulo;
    //Menu lateral
    DrawerLayout drawer;

    //Menu toolbar
    Menu menuToolbar;

    private StockNFCDataBase bd;
    ArticuloDB bdArticulo;

    TextView nombreArticulo;
    EditText stockArticulo;
    EditText proveedorArticulo;

    //Datos articulo
    Articulo articuloObtenido;
    String nombre;
    int stock;
    String proveedor;
    int id;
    FloatingActionButton botonEnviar;

    PackageManager pm;

    private static final int ENVIAR_EMAIL = 10;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragmento_pedido, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Package manager
        pm = getActivity().getPackageManager();
        //Base de datos
        bd = new StockNFCDataBase(getContext());
        bdArticulo = new ArticuloDB(getContext());
        //Toolbar
        toolbarAticulo = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbarArticulo);
        toolbarAticulo.setTitle("Realizar Pedido");
        Drawable drawable = getContext().getDrawable(R.drawable.left_arrow);
        toolbarAticulo.setNavigationIcon(drawable);
        //Quitamos el menu lateral
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        //Obtenemos los textviews
        obtenerTextViews();
        //creamos los onClicks
        crearOnClicks();
        Bundle arguments = getArguments();
        if (arguments != null) {
            id = Integer.parseInt(arguments.getString("articulo"));
            //Obtenemos el articulo de BD
            articuloObtenido = bdArticulo.obtenerArticulo(bd.getReadableDatabase(), id);
            //Rellenamos los campos
            rellenarCampos(articuloObtenido);
        }
    }


    private void rellenarCampos(Articulo articuloObtenido) {
        nombreArticulo.setText(articuloObtenido.getNombre());
        stockArticulo.setText(String.valueOf(articuloObtenido.getStock()));
        proveedorArticulo.setText(articuloObtenido.getProveedor());
    }

    private void obtenerTextViews() {
        nombreArticulo = getView().findViewById(R.id.nombreArticuloPedido);
        stockArticulo = getView().findViewById(R.id.stockArticuloPedido);
        proveedorArticulo = getView().findViewById(R.id.proveedorArticuloPedido);
        //Boton enviar email
        botonEnviar = (FloatingActionButton) getView().findViewById(R.id.botonEnviar);
    }

    private void crearOnClicks() {

        toolbarAticulo.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.contenedorFragments, new Fragmento_ListadoPedidos()).commit();
            }
        });

        botonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Comprobar permiso NFC
                int tienePermisoEnviarEmail = pm.checkPermission(Manifest.permission.WRITE_VOICEMAIL, getActivity().getPackageName());
                if (tienePermisoEnviarEmail == PackageManager.PERMISSION_GRANTED) {
                    //Comprobar articulo correcto
                    try {
                        utilidades.esconderTeclado(getActivity(), getContext());
                        if (comprobarArticuloCorrecto()) {
                            obtenerDatosGuardar();
                            //Lanzamos dialogo

                            //Enviamos el email
                            //Contruimos clase para enviar email
//                            Intent intent = new Intent(getContext(), EscrituraActivity.class);
//                            intent = meterDatosIntent(intent, articulo);
//                            startActivityForResult(intent, ARTICULO_ANHADIDO);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (getContext().checkSelfPermission(
                            Manifest.permission.WRITE_VOICEMAIL)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_VOICEMAIL},
                                ENVIAR_EMAIL);
                    }
                }
            }
        });
    }

    private boolean comprobarArticuloCorrecto() throws ParseException {
        if (validaciones.textoNoNulo(stockArticulo.getText().toString())) {
            dialogo = new Dialogo(getContext(), getContext().getResources().getString(R.string.msnStockNulo));
            dialogo.getBuilder().create().show();
            return false;
        } else if (validaciones.numeroNegativo(Integer.parseInt(stockArticulo.getText().toString()))) {
            dialogo = new Dialogo(getContext(), getContext().getResources().getString(R.string.msnStockNegativo));
            dialogo.getBuilder().create().show();
            return false;
        } else if (validaciones.textoNoNulo(proveedorArticulo.getText().toString())) {
                dialogo = new Dialogo(getContext(), getContext().getResources().getString(R.string.msnProveedorNulo));
                dialogo.getBuilder().create().show();
                return false;
            } else
                return true;
    }

    private void obtenerDatosGuardar() throws ParseException {
        articuloObtenido.setStock(Integer.parseInt(stockArticulo.getText().toString()));
        articuloObtenido.setProveedor(proveedorArticulo.getText().toString());
    }
}

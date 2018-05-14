package com.example.victor.stocknfc.fragmetos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.victor.stocknfc.ControlStockActivity;
import com.example.victor.stocknfc.Dialogo;
import com.example.victor.stocknfc.R;
import com.example.victor.stocknfc.Utilidades;
import com.example.victor.stocknfc.VOs.Articulo;
import com.example.victor.stocknfc.datos.ArticuloDB;
import com.example.victor.stocknfc.datos.StockNFCDataBase;
import com.example.victor.stocknfc.datos.UsuarioDB;

import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Fragmento_ControlStock extends android.support.v4.app.Fragment {

    SeekBar barra;
    EditText numeroControl;
    FloatingActionButton botonLeer;
    private static final int ARTICULO_LEIDO = 100;
    Fragmento_Articulo fragmento_articulo;
    Articulo articuloObtenido;
    Articulo articuloStock;

    Session session = null;
    ProgressDialog pdialog = null;
    String mensaje;
    Address destinatarios[];

    //Base de datos
    private StockNFCDataBase bd;
    private ArticuloDB bdArticulo;
    private UsuarioDB bdUsuario;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            setHasOptionsMenu(true);
            return inflater.inflate(R.layout.fragmento_control_stock, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.controlStock);
//Base de datos
        bd = new StockNFCDataBase(getContext());
        bdArticulo = new ArticuloDB(getContext());
        bdUsuario = new UsuarioDB(getContext());
        barra = getActivity().findViewById(R.id.barra);
        numeroControl = getActivity().findViewById(R.id.numeroControlStock);

        barra.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
numeroControl.setText(String.valueOf(barra.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        botonLeer = getActivity().findViewById(R.id.btnLeerArticuloNFC);
        botonLeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(datosCorrectos()) {
                    Intent intentLeer = new Intent(getActivity(), ControlStockActivity.class);
                    intentLeer.putExtra("NUMERO", Integer.valueOf(numeroControl.getText().toString()));
                    startActivityForResult(intentLeer, ARTICULO_LEIDO);
                }
            }
        });

    }

    private Boolean datosCorrectos() {
        if(Integer.valueOf(String.valueOf(numeroControl.getText())) < 1){
            Dialogo dialogo = new Dialogo(getContext(), "El número de unidades vendidas debe ser mayor que 0");
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ARTICULO_LEIDO
                && resultCode == getActivity().RESULT_CANCELED) {
            Dialogo dialogoNFC = new Dialogo(getContext(),"NFC no operativo, por favor, habilite el NFC y reintente la operación");
            dialogoNFC.getBuilder().show();
        }else{
            //Reseteamos campos
            resetearCampos();
            //Comprobamos la alerta
            articuloStock = bdArticulo.obtenerArticulo(bd.getWritableDatabase(), data.getIntExtra("ID", 0));
            if(articuloStock.getAlertaStock() >= articuloStock.getStock()){
                //Enviamos email
                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.socketFactory.port", "465");
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.port", "465");

                session = Session.getDefaultInstance(props, new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("stocknfc@gmail.com", "stocknfcadmin");
                    }
                });

                Fragmento_ControlStock.RetreiveFeedTask task = new Fragmento_ControlStock.RetreiveFeedTask();
                task.execute();
            }
//            Bundle bundle = new Bundle();
//            fragmento_articulo = new Fragmento_Articulo();
//            bundle.putString("articulo", String.valueOf(data.getIntExtra("ID", 0)));
//            fragmento_articulo.setArguments(bundle);
//            FragmentManager manager = ((AppCompatActivity)getContext()).getSupportFragmentManager();
//            manager.beginTransaction().replace(R.id.contenedorFragments, fragmento_articulo).addToBackStack(null).commit();
        }
    }

    class RetreiveFeedTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("stocknfc@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, obtenerDestinatarios());
                message.setSubject(getContext().getResources().getString(R.string.asuntoEmail));
                message.setContent(construirMensaje(articuloStock.getNombre().toString()), "text/html; charset=utf-8");
                Transport.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            //Toast.makeText(getContext(), getContext().getResources().getString(R.string.emailEnviado), Toast.LENGTH_LONG).show();
            //Volvermos al listado
        }
    }

    private Address[] obtenerDestinatarios() {
        ArrayList<String> listaEmails = bdUsuario.obtenerAdministradores(bd.getWritableDatabase());
        destinatarios = new Address[listaEmails.size()];
        for (int i = 0; i < listaEmails.size(); i++) {
            try {
                destinatarios[i] = new InternetAddress(listaEmails.get(i));
            } catch (AddressException e) {
                e.printStackTrace();
            }
        }
        return destinatarios;
    }

    private String construirMensaje(String nombreArticulo) {
        mensaje = "Atención, quedan pocas unidades del artículo: " + nombreArticulo;
        return mensaje;
    }

    private void resetearCampos() {
        numeroControl.setText("");
        barra.setProgress(0);
    }
}

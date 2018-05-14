package com.example.victor.stocknfc.fragmetos;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.victor.stocknfc.Dialogo;
import com.example.victor.stocknfc.R;
import com.example.victor.stocknfc.Utilidades;
import com.example.victor.stocknfc.VOs.Articulo;
import com.example.victor.stocknfc.Validaciones;
import com.example.victor.stocknfc.datos.ArticuloDB;
import com.example.victor.stocknfc.datos.StockNFCDataBase;
import com.example.victor.stocknfc.datos.UsuarioDB;

import java.text.ParseException;
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

public class Fragmento_Pedido_Alerta extends android.support.v4.app.Fragment {
    Validaciones validaciones = new Validaciones();
    Dialogo dialogo;
    Session session = null;
    ProgressDialog pdialog = null;
    String mensaje;
    Address destinatarios[];

    //Utilidades
    Utilidades utilidades = new Utilidades();

    //Toolbar
    Toolbar toolbarAticulo;
    //Menu lateral
    DrawerLayout drawer;

    //Menu toolbar
    Menu menuToolbar;

    private StockNFCDataBase bd;
    ArticuloDB bdArticulo;
    UsuarioDB bdUsuario;

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

    private static final int INTERNET = 10;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragmento_pedido_alerta, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Package manager
        pm = getActivity().getPackageManager();
        //Base de datos
        bd = new StockNFCDataBase(getContext());
        bdArticulo = new ArticuloDB(getContext());
        bdUsuario = new UsuarioDB(getContext());
        //Toolbar
        toolbarAticulo = (Toolbar) getActivity().findViewById(R.id.toolbarArticulo);
        toolbarAticulo.setTitle("Realizar Pedido con Alerta");
        Drawable drawable = getContext().getDrawable(R.drawable.leftarrowwhite);
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
                getFragmentManager().beginTransaction().replace(R.id.contenedorFragments, new Fragmento_ListaAlertas()).commit();
            }
        });

        botonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Comprobar permiso NFC
                int tienePermisoEnviarEmail = pm.checkPermission(Manifest.permission.INTERNET, getActivity().getPackageName());
                if (tienePermisoEnviarEmail == PackageManager.PERMISSION_GRANTED) {
                    //Comprobar articulo correcto
                    try {
                        utilidades.esconderTeclado(getActivity(), getContext());
                        if (comprobarArticuloCorrecto()) {
                            obtenerDatosGuardar();
                            //Enviamos el email
                            //Contruimos clase para enviar email
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
                            //Lanzamos dialogo
                            pdialog = ProgressDialog.show(getContext(), "", getContext().getResources().getString(R.string.enviandoEmail), true);

                            RetreiveFeedTask task = new RetreiveFeedTask();
                            task.execute();
//                            Intent intent = new Intent(getContext(), EscrituraActivity.class);
//                            intent = meterDatosIntent(intent, articulo);
//                            startActivityForResult(intent, ARTICULO_ANHADIDO);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (getContext().checkSelfPermission(
                            Manifest.permission.INTERNET)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_VOICEMAIL},
                                INTERNET);
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == INTERNET) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                botonEnviar.callOnClick();
            } else {
                Toast.makeText(getContext(), "No se ha permitido el envío de emails", Toast.LENGTH_LONG).show();
            }
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
                message.setContent(construirMensaje(nombreArticulo.getText().toString(), stockArticulo.getText().toString(), Utilidades.obtenerUsuarioActual(getActivity())), "text/html; charset=utf-8");
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
            pdialog.dismiss();

            Toast.makeText(getContext(), getContext().getResources().getString(R.string.emailEnviado), Toast.LENGTH_LONG).show();
            //Volvermos al listado
        }
    }

    private Address[] obtenerDestinatarios() {
        ArrayList<String> listaEmails = bdUsuario.obtenerAdministradores(bd.getWritableDatabase());
        destinatarios = new Address[listaEmails.size() + 1];
        for (int i = 0; i < listaEmails.size(); i++) {
            try {
                destinatarios[i] = new InternetAddress(listaEmails.get(i));
            } catch (AddressException e) {
                e.printStackTrace();
            }
        }
        try {
            destinatarios[listaEmails.size()] = new InternetAddress(proveedorArticulo.getText().toString());
        } catch (AddressException e) {
            e.printStackTrace();
        }
        return destinatarios;
    }

    private String construirMensaje(String nombreArticulo, String numeroUnidades, String usuario) {
        mensaje = "El usuario " + usuario + " solicita el envío de " + numeroUnidades + " unidades del producto: " + nombreArticulo;
        return mensaje;
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

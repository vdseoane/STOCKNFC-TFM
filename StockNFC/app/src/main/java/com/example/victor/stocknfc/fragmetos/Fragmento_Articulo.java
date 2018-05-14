package com.example.victor.stocknfc.fragmetos;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragmento_Articulo extends android.support.v4.app.Fragment {

    Validaciones validaciones = new Validaciones();
    Dialogo dialogo;

    //Utilidades
    Utilidades utilidades = new Utilidades();

    //Toolbar
    android.support.v7.widget.Toolbar toolbarAticulo;
    //Boton guardar NFC
    FloatingActionButton botonGuardarNFC;
    //Menu lateral
    DrawerLayout drawer;

    //Menu toolbar
    Menu menuToolbar;
    MenuItem btnAnhadir;
    MenuItem btnEliminar;
    MenuItem btnModificar;

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
    String path;
    SimpleDateFormat fechaFormat = new SimpleDateFormat("dd-MM-yyyy");
    Date fechaArticuloDate = new Date();


    //Datos articulo
    Articulo articulo = new Articulo();
    Articulo articuloObtenido;
    String nombre;
    int stock;
    int alerta;
    String fecha;
    float precio;
    String proveedor;
    int id;
    byte[] imageInByte;

    PackageManager pm;

    //Datos obtencion imagen
    private static final int ACTIVITY_SELECT_IMAGE = 1020, ACTIVITY_SELECT_FROM_CAMERA = 1040, USAR_CAMARA = 1050, LEER_MEMORIA_EXTERNA = 1060, ESCRIBIR_MEMORIA_EXTERNA = 1070, USAR_NFC = 1000, ARTICULO_ANHADIDO = 2000;
    private static final String ARTICULO_INSERTAR = "Insertar articulo";

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
        //Package manager
        pm = getActivity().getPackageManager();
        //Base de datos
        bd = new StockNFCDataBase(getContext());
        bdArticulo = new ArticuloDB(getContext());
        //Toolbar
        toolbarAticulo = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbarArticulo);
        toolbarAticulo.setTitle("Articulo");
        Drawable drawable = getContext().getDrawable(R.drawable.leftarrowwhite);
        toolbarAticulo.setNavigationIcon(drawable);
        //Quitamos el menu lateral
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        toolbarAticulo.inflateMenu(R.menu.menu_articulo_editar);
        toolbarAticulo.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        //Obtenemos los textviews
        obtenerTextViews();
        //creamos los onClicks
        crearOnClicks();
        Bundle arguments = getArguments();
        if (arguments != null) {
            id = Integer.parseInt(arguments.getString("articulo"));
            //Botones(eliminar/modificar) visibles
            habilitarBotonesMenu(true);
            habilitarBotonNFC(false);
            //Ponemos los campos a readonly
            habilitarCampos(false);
            //Obtenemos el articulo de BD
            articuloObtenido = bdArticulo.obtenerArticulo(bd.getReadableDatabase(), id);
            //Rellenamos los campos
            rellenarCampos(articuloObtenido);
        } else {
            //Insercion visible
            habilitarBotonesMenu(false);
            deshabilitarInsercion();
            habilitarBotonNFC(true);
            //Habilitamos los campos
            habilitarCampos(true);
            //Fecha del día de hoy para el artículo
            String fechaArt = fechaFormat.format(fechaArticuloDate);
            fechaArticulo.setText(fechaArt);
        }
    }

    private void habilitarBotonNFC(Boolean insercion) {
        if(!insercion)
        botonGuardarNFC.setVisibility(View.INVISIBLE);
        else
            botonGuardarNFC.setVisibility(View.VISIBLE);
    }

    private void deshabilitarInsercion() {
        btnAnhadir.setVisible(false);
        btnAnhadir.setVisible(false);
    }

    private void habilitarBotonesMenu(boolean edicion) {
        btnModificar.setVisible(edicion);
        btnModificar.setEnabled(edicion);
        btnEliminar.setVisible(edicion);
        btnEliminar.setVisible(edicion);
        btnAnhadir.setVisible(!edicion);
        btnAnhadir.setVisible(!edicion);
    }

    private void rellenarCampos(Articulo articuloObtenido) {
        nombreArticulo.setText(articuloObtenido.getNombre());
        stockArticulo.setText(String.valueOf(articuloObtenido.getStock()));
        alertaArticulo.setText(String.valueOf(articuloObtenido.getAlertaStock()));
        precioArticulo.setText(String.valueOf(articuloObtenido.getPrecio()));
        proveedorArticulo.setText(articuloObtenido.getProveedor());
        imgArticulo.setBackground(null);
        if (articuloObtenido.getImagenArticulo() != null) {
            Bitmap imagenArtBitmap = BitmapFactory.decodeByteArray(articuloObtenido.getImagenArticulo(), 0, articuloObtenido.getImagenArticulo().length);
            imgArticulo.setImageBitmap(imagenArtBitmap);
        } else {
            imgArticulo.setImageResource(R.drawable.trolley);
        }
        fechaArticulo.setText(articuloObtenido.getFechaCreacion());
    }

    private void habilitarCampos(boolean habilitar) {
        if (habilitar == false) {
            nombreArticulo.setFocusable(habilitar);
            stockArticulo.setFocusable(habilitar);
            alertaArticulo.setFocusable(habilitar);
            precioArticulo.setFocusable(habilitar);
            proveedorArticulo.setFocusable(habilitar);
            imgArticulo.setFocusable(habilitar);
            imgArticulo.setOnClickListener(null);
        } else {
            nombreArticulo.setFocusableInTouchMode(habilitar);
            stockArticulo.setFocusableInTouchMode(habilitar);
            alertaArticulo.setFocusableInTouchMode(habilitar);
            precioArticulo.setFocusableInTouchMode(habilitar);
            proveedorArticulo.setFocusableInTouchMode(habilitar);
            imgArticulo.setFocusableInTouchMode(habilitar);
            crearOnClickImagen();
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
        //Menu toolbar
        menuToolbar = toolbarAticulo.getMenu();
        btnAnhadir = menuToolbar.findItem(R.id.anhadirArticuloMenu);
        btnEliminar = menuToolbar.findItem(R.id.borrarArticuloMenu);
        btnModificar = menuToolbar.findItem(R.id.modificarArticuloMenu);
        //Boton guardar NFC
        botonGuardarNFC = (FloatingActionButton)getView().findViewById(R.id.btnGuardarArticuloNFC);
    }

    private void crearOnClicks() {
        crearOnClickImagen();

        toolbarAticulo.setOnMenuItemClickListener(new android.support.v7.widget.Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.borrarArticuloMenu:
                        int cant = bdArticulo.eliminarArticulo(bd.getWritableDatabase(), articuloObtenido.getId());
                        if (cant > 0) {
                            Toast.makeText(getContext(), "Articulo borrado", Toast.LENGTH_SHORT).show();
                            getFragmentManager().beginTransaction().replace(R.id.contenedorFragments, new Fragmento_Lista_Articulos()).commit();
                        } else {
                            Toast.makeText(getContext(), "Error al borrar el artículo", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.modificarArticuloMenu:
                        habilitarBotonesMenu(false);
                        habilitarCampos(true);
                        break;
                    case R.id.anhadirArticuloMenu:
                        try {
                            utilidades.esconderTeclado(getActivity(), getContext());
                            if (comprobarArticuloCorrecto()) {
                                obtenerDatosGuardar();
                                if (articuloObtenido != null) {
                                    editarArticulo();
                                } else {
                                    guardarArticulo();
                                }

                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                return true;
            }
        });

        toolbarAticulo.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.contenedorFragments, new Fragmento_Lista_Articulos()).commit();
            }
        });

        botonGuardarNFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Comprobar permiso NFC
                int tienePermisoEscribirNFC = pm.checkPermission(Manifest.permission.NFC, getActivity().getPackageName());
                if (tienePermisoEscribirNFC == PackageManager.PERMISSION_GRANTED) {
                    //Comprobar articulo correcto
                    try {
                        utilidades.esconderTeclado(getActivity(), getContext());
                        if (comprobarArticuloCorrecto()) {
                            obtenerDatosGuardar();
                            Intent intent = new Intent(getContext(), EscrituraActivity.class);
                            intent = meterDatosIntent(intent, articulo);
                            startActivityForResult(intent, ARTICULO_ANHADIDO);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else{
                if (getContext().checkSelfPermission(
                        Manifest.permission.NFC)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.NFC},
                            USAR_NFC);
                }
            }
            }
        });
    }

    private Intent meterDatosIntent(Intent intent, Articulo articulo) {
        intent.putExtra("ID", articulo.getId());
        intent.putExtra("NOMBRE", articulo.getNombre());
        intent.putExtra("STOCK", articulo.getStock());
        intent.putExtra("ALERTA", articulo.getAlertaStock());
        intent.putExtra("FECHA", articulo.getFechaCreacion());
        intent.putExtra("PRECIO", articulo.getPrecio());
        intent.putExtra("PROVEEDOR", articulo.getProveedor());
        intent.putExtra("IMAGEN", articulo.getImagenArticulo());

        return intent;
    }

    private void crearOnClickImagen() {
        imgArticulo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                seleccionarImagenDialogo();
            }
        });
    }

    private void seleccionarImagenDialogo() {
        try {
            int tienePermisoCamara = pm.checkPermission(Manifest.permission.CAMERA, getActivity().getPackageName());
            int tienePermisoLeerDatos = pm.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, getActivity().getPackageName());
            int tienePermisoEscribirDatos = pm.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getActivity().getPackageName());
            if (tienePermisoCamara == PackageManager.PERMISSION_GRANTED && PackageManager.PERMISSION_GRANTED == tienePermisoLeerDatos && PackageManager.PERMISSION_GRANTED == tienePermisoEscribirDatos) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            //imgUri = Uri.fromFile(getFile());
                            //intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imgUri);
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
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            USAR_CAMARA);
                }
                if (getContext().checkSelfPermission(
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            LEER_MEMORIA_EXTERNA);
                }
                if (getContext().checkSelfPermission(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            ESCRIBIR_MEMORIA_EXTERNA);
                }
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private File getFile() {
        File folder = Environment.getExternalStoragePublicDirectory("/From_camera/imagens");// the file path

        //if it doesn't exist the folder will be created
        if (!folder.exists()) {
            folder.mkdir();
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File image_file = null;

        try {
            image_file = File.createTempFile(imageFileName, ".jpg", folder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        path = image_file.getAbsolutePath();
        return image_file;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == USAR_CAMARA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                seleccionarImagenDialogo();
            } else {
                Toast.makeText(getContext(), "No se ha permitido el uso de la cámara", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == LEER_MEMORIA_EXTERNA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                seleccionarImagenDialogo();
            } else {
                Toast.makeText(getContext(), "No se ha permitido el acceso a imágenes", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == ESCRIBIR_MEMORIA_EXTERNA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                seleccionarImagenDialogo();
            } else {
                Toast.makeText(getContext(), "No se ha permitido el guardado de imágenes", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == USAR_NFC) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                botonGuardarNFC.callOnClick();
            } else {
                Toast.makeText(getContext(), "No se ha permitido usar el NFC", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void guardarArticulo() {
        //bdArticulo = new ArticuloDB(getContext());
        long inserccion = bdArticulo.insertarArticulo(bdArticulo.getWritableDatabase(), articulo);

        if (inserccion != -1) {
            Toast.makeText(getContext(), "Articulo añadido correctamente", Toast.LENGTH_LONG).show();
            getFragmentManager().beginTransaction().replace(R.id.contenedorFragments, new Fragmento_Lista_Articulos()).commit();
        } else {
            Dialogo dialogo = new Dialogo(getContext(), "Ha habido un error al insertar el artículo");
            dialogo.getDialogo().show();
        }
    }

    private void editarArticulo() {
        long edicion = bdArticulo.editarArticulo(bd.getWritableDatabase(), articuloObtenido.getId(), articulo);

        if (edicion != -1) {
            Toast.makeText(getContext(), "Articulo editado correctamente", Toast.LENGTH_LONG).show();
            getFragmentManager().beginTransaction().replace(R.id.contenedorFragments, new Fragmento_Lista_Articulos()).commit();
        } else {
            Dialogo dialogo = new Dialogo(getContext(), "Ha habido un error al editar el artículo");
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
            // Bitmap photo2 = rotarImagen(photo);
            imgArticulo.setBackground(null);
            imgArticulo.setImageBitmap(photo);
        }
        else if (requestCode == ARTICULO_ANHADIDO
                && resultCode == getActivity().RESULT_OK) {
            Toast.makeText(getContext(), "Articulo añadido correctamente", Toast.LENGTH_LONG).show();
            getFragmentManager().beginTransaction().replace(R.id.contenedorFragments, new Fragmento_Lista_Articulos()).commit();
        }else{
            Dialogo dialogoNFC = new Dialogo(getContext(),"NFC no operativo, por favor, habilite el NFC y reintente la operación");
            dialogoNFC.getBuilder().show();
        }
    }

    private Bitmap rotarImagen(Bitmap bitmap) {
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(String.valueOf(imgUri));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientacion = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();
        switch (orientacion) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(270);
                break;
            default:
        }
        Bitmap rotadoBitMap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return rotadoBitMap;
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
            //BitmapDrawable bitmapDrawable = ((BitmapDrawable) imgArticulo.getDrawable());
            Drawable drawable = getResources().getDrawable(R.drawable.trolley, null);
            Drawable drawable2 = imgArticulo.getDrawable();
            //if(bitmapDrawable == drawable && bitmapDrawable != drawable)
            if (!validaciones.esNulo(drawable2) && !getBitmap(drawable).sameAs(getBitmap(drawable2))) {
                Bitmap bitmap = drawableToBitmap(drawable2);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                articulo.setImagenArticulo(stream.toByteArray());
            }
            return true;
        }
    }

    public static Bitmap getBitmap(Drawable drawable) {
        Bitmap result;
        if (drawable instanceof BitmapDrawable) {
            result = ((BitmapDrawable) drawable).getBitmap();
        } else {
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();
            // Some drawables have no intrinsic width - e.g. solid colours.
            if (width <= 0) {
                width = 1;
            }
            if (height <= 0) {
                height = 1;
            }

            result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }
        return result;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}

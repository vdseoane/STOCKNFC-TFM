package com.example.victor.stocknfc;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.victor.stocknfc.VOs.Articulo;
import com.example.victor.stocknfc.datos.ArticuloDB;
import com.example.victor.stocknfc.datos.StockNFCDataBase;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class EscrituraActivity extends AppCompatActivity {
    //Datos articulo
    Articulo articulo;
    String nombre;
    int stock;
    int alerta;
    String fecha;
    float precio;
    String proveedor;
    int id;
    byte[] imageInByte;
    public final int LEER_NFC = 1;

    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    boolean writeMode;
    Tag myTag;
    NfcAdapter nfcAdapter;
    int idArticulo;

    //Base de datos
    private StockNFCDataBase bd;
    private ArticuloDB bdArticulo;

    private static final int ARTICULO_ANHADIDO = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        if (getIntent() != null) {
            articulo = new Articulo(getIntent().getIntExtra("ID", 0), getIntent().getStringExtra("NOMBRE"), getIntent().getIntExtra("STOCK", 0), getIntent().getIntExtra("ALERTA", -1), getIntent().getStringExtra("FECHA"), getIntent().getFloatExtra("PRECIO", 0), getIntent().getByteArrayExtra("IMAGEN"), getIntent().getStringExtra("PROVEEDOR"));

            //Base de datos
            bd = new StockNFCDataBase(this);
            bdArticulo = new ArticuloDB(this);
            nfcAdapter = NfcAdapter.getDefaultAdapter(this);
            if (nfcAdapter != null && nfcAdapter.isEnabled()) {
                //Toast.makeText(this, this.getString(R.string.nfc_operativo), Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(this, this.getString(R.string.nfcNoOperativo), Toast.LENGTH_SHORT).show();
            }

            pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
            tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
            writeTagFilters = new IntentFilter[]{tagDetected};

            Toast.makeText(this, "Escribiendo pegatina NFC...", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, this.getString(R.string.errorArticulo), Toast.LENGTH_SHORT).show();
        }

    }

    protected void onNewIntent(Intent intent) {
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            //Toast.makeText(this, this.getString(R.string.NFCDetectado), Toast.LENGTH_SHORT).show();

            try {
                //Si no existe tag al que escribir, mostramos un mensaje de que no existe.
                if (myTag == null) {
                    Toast.makeText(this, this.getString(R.string.noHayPegatina), Toast.LENGTH_LONG).show();
                } else {
                    //Llamamos al método write que definimos más adelante donde le pasamos por
                    //parámetro el tag que hemos detectado y el mensaje a escribir.
                    write(obtenerSiguienteId(), myTag);
                    //Toast.makeText(this, this.getString(R.string.escrituraCorrecta), Toast.LENGTH_LONG).show();
                    long inserccion = bdArticulo.insertarArticulo(bd.getWritableDatabase(), articulo);
                    if (inserccion != -1) {
                        //Toast.makeText(this, "Articulo añadido correctamente", Toast.LENGTH_LONG).show();
                        Intent returnIntent = new Intent();
                        setResult(this.RESULT_OK, returnIntent);
                        finish();
                    } else {
                        Dialogo dialogo = new Dialogo(this, "Ha habido un error al insertar el artículo");
                        dialogo.getDialogo().show();
                    }
                }
            } catch (IOException e) {
                Toast.makeText(this, this.getString(R.string.errorEscritura), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            } catch (FormatException e) {
                Toast.makeText(this, this.getString(R.string.errorEscritura), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    private int obtenerSiguienteId() {
        int id = bdArticulo.obtenerSiguienteId(bd.getReadableDatabase());
        articulo.setId(id);
       return id;
    }

    private void write(int text, Tag tag) throws IOException, FormatException {
        //Creamos un array de elementos NdefRecord. Este Objeto representa un registro del mensaje NDEF
        //Para crear el objeto NdefRecord usamos el método createRecord(String s)
        NdefRecord[] records = {createRecord(String.valueOf(text))};
        //NdefMessage encapsula un mensaje Ndef(NFC Data Exchange Format). Estos mensajes están
        //compuestos por varios registros encapsulados por la clase NdefRecord
        NdefMessage message = new NdefMessage(records);
        //Obtenemos una instancia de Ndef del Tag
        Ndef ndef = Ndef.get(tag);
        ndef.connect();
        ndef.writeNdefMessage(message);
        ndef.close();
    }

    private NdefRecord createRecord(String text) throws UnsupportedEncodingException {
        String lang = "us";
        byte[] textBytes = text.getBytes();
        byte[] langBytes = lang.getBytes("US-ASCII");
        int langLength = langBytes.length;
        int textLength = textBytes.length;
        byte[] payLoad = new byte[1 + langLength + textLength];

        payLoad[0] = (byte) langLength;

        System.arraycopy(langBytes, 0, payLoad, 1, langLength);
        System.arraycopy(textBytes, 0, payLoad, 1 + langLength, textLength);

        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payLoad);

        return recordNFC;

    }

    public void onPause() {
        super.onPause();
        WriteModeOff();
    }

    public void onResume() {
        super.onResume();
        WriteModeOn();
    }

    private void WriteModeOn() {
        writeMode = true;
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null);
    }

    private void WriteModeOff() {
        writeMode = false;
        nfcAdapter.disableForegroundDispatch(this);
    }


}

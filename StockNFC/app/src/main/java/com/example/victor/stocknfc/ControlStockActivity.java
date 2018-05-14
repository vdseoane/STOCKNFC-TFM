package com.example.victor.stocknfc;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.victor.stocknfc.VOs.Articulo;
import com.example.victor.stocknfc.datos.ArticuloDB;
import com.example.victor.stocknfc.datos.StockNFCDataBase;

import java.io.UnsupportedEncodingException;

public class ControlStockActivity extends AppCompatActivity {

    private int numeroControl;
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

    private static final int ARTICULO_LEIDO = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_stock);
//Base de datos
        bd = new StockNFCDataBase(this);
        bdArticulo = new ArticuloDB(this);
        if(getIntent() != null){
            numeroControl =getIntent().getIntExtra("NUMERO", 1);
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setMessage("Leyendo NFC...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null || !nfcAdapter.isEnabled()) {
            Toast.makeText(this, this.getString(R.string.nfcNoOperativo), Toast.LENGTH_SHORT).show();
            Intent returnIntent = new Intent();
            setResult(this.RESULT_CANCELED, returnIntent);
            finish();
        } else {
            pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
            tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
            writeTagFilters = new IntentFilter[]{tagDetected};

            Toast.makeText(this, "Leyendo pegatina NFC...", Toast.LENGTH_SHORT).show();
        }

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writeTagFilters = new IntentFilter[]{tagDetected};
    }

    protected void onNewIntent(Intent intent){
        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            getIdArticulo(intent);
        }
    }

    private void getIdArticulo(Intent intent){
        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if(parcelables != null && parcelables.length > 0) {
                int idArticuloNFC = Integer.valueOf(readTextFromMessage((NdefMessage) parcelables[0]));
                articulo = bdArticulo.obtenerArticulo(bd.getReadableDatabase(), idArticuloNFC);
                int cant = decrementarStock(articulo.getStock()-numeroControl, articulo.getId());
                if (cant >0) {
                    Toast.makeText(this, "Unidades restantes: " + bdArticulo.obtenerArticulo(bd.getReadableDatabase(), idArticuloNFC).getStock(), Toast.LENGTH_SHORT).show();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("ID", idArticuloNFC);
                    setResult(this.RESULT_OK, returnIntent);
                    finish();
                }else{
                    Toast.makeText(this, "Error al modificar el artículo", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Etiqueta vacía", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private int decrementarStock(int stock, int id){
        int cant = bdArticulo.editarStockArticulo(bd.getWritableDatabase(), stock, id);
        return cant;
    }

    private String readTextFromMessage(NdefMessage ndefMessage) {
        NdefRecord[] ndefRecords = ndefMessage.getRecords();
        if(ndefRecords != null && ndefRecords.length > 0){
            NdefRecord ndefRecord = ndefRecords[0];
            String tagContent = getTextFromNdefRecord(ndefRecord);
            return tagContent;
        }
        return null;
    }

    private String getTextFromNdefRecord(NdefRecord ndefRecord)
    {
        String tagContent = null;
        try {
            byte[] payload = ndefRecord.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageSize = payload[0] & 0063;
            tagContent = new String(payload, languageSize + 1, payload.length - languageSize - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e("getTextFromNdefRecord", e.getMessage(), e);
        }
        return tagContent;
    }

    public void onPause(){
        super.onPause();
        WriteModeOff();
    }
    public void onResume(){
        super.onResume();
        WriteModeOn();
    }

    private void WriteModeOn(){
        writeMode = true;
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null);
    }

    private void WriteModeOff(){
        writeMode = false;
        nfcAdapter.disableForegroundDispatch(this);
    }
}

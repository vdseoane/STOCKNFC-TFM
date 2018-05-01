package com.example.victor.stocknfc.fragmetos;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.example.victor.stocknfc.VOs.Articulo;
import com.example.victor.stocknfc.datos.ArticuloDB;
import com.example.victor.stocknfc.datos.StockNFCDataBase;

public class Fragmento_ControlStock extends android.support.v4.app.Fragment {

    SeekBar barra;
    EditText numeroControl;
    FloatingActionButton botonLeer;
    private static final int ARTICULO_LEIDO = 100;
    Fragmento_Articulo fragmento_articulo;
    Articulo articuloObtenido;
    //Base de datos
    private StockNFCDataBase bd;
    private ArticuloDB bdArticulo;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            setHasOptionsMenu(true);
            return inflater.inflate(R.layout.fragmento_control_stock, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//Base de datos
        bd = new StockNFCDataBase(getContext());
        bdArticulo = new ArticuloDB(getContext());
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
            Bundle bundle = new Bundle();
            fragmento_articulo = new Fragmento_Articulo();
            bundle.putString("articulo", String.valueOf(data.getIntExtra("ID", 0)));
            fragmento_articulo.setArguments(bundle);
            FragmentManager manager = ((AppCompatActivity)getContext()).getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.contenedorFragments, fragmento_articulo).commit();
        }
    }
}

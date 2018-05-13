package com.example.victor.stocknfc.fragmetos;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.victor.stocknfc.R;
import com.example.victor.stocknfc.VOs.Articulo;
import com.example.victor.stocknfc.datos.ArticuloDB;
import com.example.victor.stocknfc.datos.StockNFCDataBase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragmento_ListadoPedidos extends android.support.v4.app.Fragment {
    public StockNFCDataBase bd;
    ArticuloDB bdArticulo;

    ArrayList<Articulo> listaArticulo;
    RecyclerView recycler;
    Context context;

    public Fragmento_ListadoPedidos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragmento_lista_pedidos, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
context = getContext();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.pedidos);
        bd = new StockNFCDataBase(context);
        recycler= (RecyclerView) getView().findViewById(R.id.recyclerPedidos);
        recycler.setLayoutManager(new LinearLayoutManager(getView().getContext(), LinearLayoutManager.VERTICAL, false));

        listaArticulo = obtenerArticulos();
        for(int i=0; i<listaArticulo.size(); i++){
            AdaptadorDatosPedidos adapter = new AdaptadorDatosPedidos(getActivity(), listaArticulo);
            recycler.setAdapter(adapter);
        }
    }

    private ArrayList<Articulo> obtenerArticulos() {
        bdArticulo = new ArticuloDB(context);

        return bdArticulo.obtenerArticulos(bd.getReadableDatabase());
    }
}

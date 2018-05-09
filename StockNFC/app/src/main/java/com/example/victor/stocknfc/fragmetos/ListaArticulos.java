package com.example.victor.stocknfc.fragmetos;


import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.victor.stocknfc.MainActivity;
import com.example.victor.stocknfc.R;
import com.example.victor.stocknfc.VOs.Articulo;
import com.example.victor.stocknfc.datos.ArticuloDB;
import com.example.victor.stocknfc.datos.StockNFCDataBase;
import com.example.victor.stocknfc.logIn.LogIn;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListaArticulos extends android.support.v4.app.Fragment {
    public StockNFCDataBase bd;
    ArticuloDB bdArticulo;

    ArrayList<Articulo> listaArticulo;
    RecyclerView recycler;
    Context context;

    public ListaArticulos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_lista_articulos, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
context = getContext();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.listaArticulos);
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.consultarProductoMenu);
        bd = new StockNFCDataBase(context);
        recycler= (RecyclerView) getView().findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getView().getContext(), LinearLayoutManager.VERTICAL, false));
FloatingActionButton botonAnadir = getView().findViewById(R.id.btnAnhadirArticulo);
botonAnadir.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().replace(R.id.contenedorFragments, new Fragmento_Articulo()).commit();
    }
});
        listaArticulo = obtenerArticulos();
        for(int i=0; i<listaArticulo.size(); i++){
            AdaptadorDatos adapter = new AdaptadorDatos(getActivity(), listaArticulo);
            recycler.setAdapter(adapter);
        }
    }

    private ArrayList<Articulo> obtenerArticulos() {
        bdArticulo = new ArticuloDB(context);

        return bdArticulo.obtenerArticulos(bd.getReadableDatabase());
    }
}

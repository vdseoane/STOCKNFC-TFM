package com.example.victor.stocknfc.fragmetos;


import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.victor.stocknfc.MainActivity;
import com.example.victor.stocknfc.R;
import com.example.victor.stocknfc.VOs.Articulo;
import com.example.victor.stocknfc.logIn.LogIn;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListaArticulos extends android.support.v4.app.Fragment {

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
        listaArticulo = new ArrayList<Articulo>();

        for(int i=0; i<300; i++){
            listaArticulo.add(new Articulo("Hola" + i));

            AdaptadorDatos adapter = new AdaptadorDatos(getActivity(), listaArticulo);
            recycler.setAdapter(adapter);
        }
    }
}

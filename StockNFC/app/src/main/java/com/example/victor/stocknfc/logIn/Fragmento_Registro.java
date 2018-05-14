package com.example.victor.stocknfc.logIn;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.victor.stocknfc.Dialogo;
import com.example.victor.stocknfc.R;
import com.example.victor.stocknfc.VOs.Usuario;
import com.example.victor.stocknfc.datos.StockNFCDataBase;
import com.example.victor.stocknfc.datos.UsuarioDB;
import com.example.victor.stocknfc.fragmetos.Fragmento_Lista_Articulos;


public class Fragmento_Registro extends android.support.v4.app.Fragment {
    Spinner desplegableRol;
    public StockNFCDataBase bd;

    TextView txtNombreUsuario;
    TextView txtPassUsuario;
    String rol;
    Button registro;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmento_registro, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.registroUsuario);
        bd = new StockNFCDataBase(getContext());


        txtNombreUsuario = getActivity().findViewById(R.id.usuarioRegistrar);
        txtPassUsuario = getActivity().findViewById(R.id.passRegistrar);
        desplegableRol = getActivity().findViewById(R.id.desplegableRol);
registro = getActivity().findViewById(R.id.botonRegistrar);

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Usuario usuario;
                UsuarioDB dbUsuario = new UsuarioDB(getContext());

                //Comprobamos que el usuario no existe
                usuario = dbUsuario.obtenerUsuario(dbUsuario.getReadableDatabase(), txtNombreUsuario.getText().toString(), txtPassUsuario.getText().toString());
                if (usuario == null) {
                    dbUsuario.insertarUsuario(bd.getWritableDatabase(), txtNombreUsuario.getText().toString(), rol, txtPassUsuario.getText().toString());

                    Toast.makeText(getContext(), "Usuario registrado", Toast.LENGTH_LONG).show();

                    //Enviamos a pantalla principal
                    getFragmentManager().beginTransaction().replace(R.id.contenedorFragments, new Fragmento_Lista_Articulos()).commit();

                } else {
                    Dialogo dialogo = new Dialogo(getContext(), "Ya existe un usuario registrado con ese email");
                    dialogo.getDialogo().show();
                }
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, getResources()
                .getStringArray(R.array.array_roles));//setting the country_array to spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        desplegableRol.setAdapter(adapter);
//if you want to set any action you can do in this listener
        desplegableRol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                rol = desplegableRol.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }
}

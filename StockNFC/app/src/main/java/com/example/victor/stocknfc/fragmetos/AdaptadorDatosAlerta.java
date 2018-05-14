package com.example.victor.stocknfc.fragmetos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.victor.stocknfc.R;
import com.example.victor.stocknfc.VOs.Articulo;

import java.util.ArrayList;

/**
 * Created by Victor on 26/03/2018.
 */

public class AdaptadorDatosAlerta extends RecyclerView.Adapter<AdaptadorDatosAlerta.ViewHolderDatos> {

    Fragmento_Pedido_Alerta fragmento_pedido= new Fragmento_Pedido_Alerta();

    private Context context;
    private ArrayList<Articulo> listaArticulos;


    public AdaptadorDatosAlerta(Context context, ArrayList<Articulo> listaArticulos) {
        this.context = context;
        this.listaArticulos = listaArticulos;
    }

    @Override

    public AdaptadorDatosAlerta.ViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listaproductos, null, false);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(AdaptadorDatosAlerta.ViewHolderDatos holder, int position) {
        holder.asignarDatos(listaArticulos.get(position));
    }

    @Override
    public int getItemCount() {
        return listaArticulos.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView nombreArticulo;
        TextView stockArticulo;
        TextView alertaArticulo;
        ImageView imagenArticulo;
       TextView idArticulo;
        CardView cv;

        public ViewHolderDatos(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cardView);
            nombreArticulo = (TextView) itemView.findViewById(R.id.nombreArticuloLista);
            stockArticulo = (TextView) itemView.findViewById(R.id.stockArticuloLista);
            alertaArticulo = itemView.findViewById(R.id.precioArticuloLista);
            imagenArticulo = itemView.findViewById(R.id.imagenArticuloLista);
            idArticulo = (TextView) itemView.findViewById(R.id.idArticuloLista);

            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Articulo articuloObtenido = listaArticulos.get(getAdapterPosition());
                    Bundle bundle = new Bundle();
                    bundle.putString("articulo", String.valueOf(articuloObtenido.getId()));
                    fragmento_pedido.setArguments(bundle);
                    FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.contenedorFragments, fragmento_pedido).addToBackStack(null).commit();
                }
            });
        }

        public void asignarDatos(Articulo articulo) {
            nombreArticulo.setText(articulo.getNombre());
            stockArticulo.setText(context.getResources().getString(R.string.stockArticulo) + ": " + String.valueOf(articulo.getStock()));
            alertaArticulo.setText(context.getResources().getString(R.string.alerta) + ": " + String.valueOf(articulo.getAlertaStock()));

            if (articulo.getImagenArticulo() != null) {
                Bitmap imagenArtBitmap = BitmapFactory.decodeByteArray(articulo.getImagenArticulo(), 0, articulo.getImagenArticulo().length);
                imagenArticulo.setImageBitmap(imagenArtBitmap);
            }else{
                imagenArticulo.setImageResource(R.drawable.trolley);
            }
            idArticulo.setText(String.valueOf(articulo.getId()));
        }
    }
}

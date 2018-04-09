package com.example.victor.stocknfc.fragmetos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
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

public class AdaptadorDatos extends RecyclerView.Adapter<AdaptadorDatos.ViewHolderDatos> {

    private Context context;
    private ArrayList<Articulo> listaArticulos;


    public AdaptadorDatos(Context context, ArrayList<Articulo> listaArticulos) {
        this.context = context;
        this.listaArticulos = listaArticulos;
    }

    @Override

    public AdaptadorDatos.ViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listaproductos, null, false);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(AdaptadorDatos.ViewHolderDatos holder, int position) {
        holder.asignarDatos(listaArticulos.get(position));
    }

    @Override
    public int getItemCount() {
        return listaArticulos.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView nombreArticulo;
        TextView stockArticulo;
        TextView precioArticulo;
        ImageView imagenArticulo;
        CardView cv;

        public ViewHolderDatos(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cardView);
            nombreArticulo = (TextView) itemView.findViewById(R.id.nombreArticuloLista);
            stockArticulo = (TextView) itemView.findViewById(R.id.stockArticuloLista);
            precioArticulo = itemView.findViewById(R.id.precioArticuloLista);
            imagenArticulo = itemView.findViewById(R.id.imagenArticuloLista);
        }

        public void asignarDatos(Articulo articulo) {
            nombreArticulo.setText(articulo.getNombre());
            stockArticulo.setText(context.getResources().getString(R.string.stockArticulo) + ": " + String.valueOf(articulo.getStock()));
            precioArticulo.setText(context.getResources().getString(R.string.precioArticulo) + ": " + String.valueOf(articulo.getPrecio()));
            if (articulo.getImagen_articulo() != null) {
                Bitmap imagenArtBitmap = BitmapFactory.decodeByteArray(articulo.getImagen_articulo(), 0, articulo.getImagen_articulo().length);
                imagenArticulo.setImageBitmap(imagenArtBitmap);
            }
        }
    }
}

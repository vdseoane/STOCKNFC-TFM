package com.example.victor.stocknfc;

import com.example.victor.stocknfc.VOs.Articulo;

/**
 * Created by Victor on 10/04/2018.
 */

public class Validaciones {

    public boolean textoNoNulo(String texto) {
        if (texto != null)
            if (!texto.isEmpty())
                return true;
            else return false;
        else return false;
    }

    public boolean longitudAdecuada(String texto, int longitud) {
        if (textoNoNulo(texto)) {
            if (texto.length() < longitud)
                return true;
            else return false;
        }else return false;
    }

    public boolean esNulo (Object objeto){
        if(objeto == null)
            return true;
        else return false;
    }

    public boolean numeroNegativo(int i) {
        if(i<0) return true;
        else return false;
    }
}

package org.example.chainOfResponsability;

import org.example.builderPattern.Libro;

import java.util.ArrayList;
import java.util.List;

public class FiltraPerISBN extends FiltroHandler{

    @Override
    protected List<Libro> applicaFiltro(List<Libro> listaLibri, String isbn) {
        List<Libro> filtrati = new ArrayList<>();

        for(Libro l: listaLibri){
            if(l.getCodiceISBN().toLowerCase().contains(isbn.toLowerCase()) ){
                filtrati.add(l);
            }
        }
        return filtrati;
    }

    public String toString(){
        return "ISBN";
    }

}

package org.example.chainOfResponsability;

import org.example.builderPattern.Libro;

import java.util.ArrayList;
import java.util.List;

public class FiltraPerAutore extends FiltroHandler {



    @Override
    public List<Libro> applicaFiltro(List<Libro> listaLibro,String autore) {
        List<Libro> filtrati = new ArrayList<>();


        for(Libro l: listaLibro){
            if(l.getAutore().toLowerCase().trim().contains(autore.toLowerCase().trim()) && l.getAutore() != null){
                filtrati.add(l);
            }
        }
        return filtrati;
    }

    @Override
    public String toString() {
        return "Autore";
    }
}

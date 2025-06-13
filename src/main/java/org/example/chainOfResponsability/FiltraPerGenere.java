package org.example.chainOfResponsability;

import org.example.builderPattern.Libro;

import java.util.ArrayList;
import java.util.List;

public class FiltraPerGenere extends FiltroHandler {

    public FiltraPerGenere() {

    }

    @Override
    public List<Libro> applicaFiltro(List<Libro> listaLibro,String genere) {
        List<Libro> filtrati = new ArrayList<>();
        for(Libro l: listaLibro){
            if(l.getGenere().toLowerCase().trim().contains(genere.toLowerCase().trim()) && l.getGenere() != null){
                filtrati.add(l);
            }
        }
        return filtrati;
    }

    @Override
    public String toString() {

        return "Genere";
    }
}

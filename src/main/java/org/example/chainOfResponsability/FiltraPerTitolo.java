package org.example.chainOfResponsability;

import org.example.builderPattern.Libro;

import java.util.ArrayList;
import java.util.List;

public class FiltraPerTitolo extends FiltroHandler {
    @Override
    public List<Libro> applicaFiltro(List<Libro> listaLibro,String titolo) {
        List<Libro> filtrati = new ArrayList<>();
        for(Libro l: listaLibro){
            if(l.getTitolo().toLowerCase().trim().contains(titolo.toLowerCase().trim()) && l.getTitolo() != null){
                filtrati.add(l);
            }
        }
        return filtrati;
    }
    @Override
    public String toString() {
        return "Titolo";
    }

}

package org.example.chainOfResponsability;

import org.example.builderPattern.Libro;

import java.util.ArrayList;
import java.util.List;

public class FiltraPerStelle extends FiltroHandler{
    @Override
    public List<Libro> applicaFiltro(List<Libro> listaLibro, String stella) {
        List<Libro> filtrati = new ArrayList<>();
        for(Libro l: listaLibro){

            if(l.getStelle().getValore()==Integer.parseInt(stella) && l.getStelle() != null){
                filtrati.add(l);
            }
        }
        return filtrati;
    }

    @Override
    public String toString() {
        return "Stelle";
    }
}


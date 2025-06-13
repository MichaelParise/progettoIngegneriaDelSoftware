package org.example.chainOfResponsability;

import org.example.builderPattern.Libro;

import java.util.ArrayList;
import java.util.List;

public class FiltraPerLingua extends FiltroHandler{
    @Override
    public List<Libro> applicaFiltro(List<Libro> listaLibro, String lingua) {
        List<Libro> filtrati = new ArrayList<>();
        for(Libro l: listaLibro){

            if (l.getLingua() != null && l.getLingua().toLowerCase().trim().contains(lingua.toLowerCase().trim())) {
                filtrati.add(l);
            }

        }
        return filtrati;
    }

    @Override
    public String toString() {
        return "Lingua";
    }
}

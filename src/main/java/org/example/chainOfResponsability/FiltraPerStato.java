package org.example.chainOfResponsability;

import org.example.builderPattern.Libro;

import java.util.ArrayList;
import java.util.List;

public class FiltraPerStato extends FiltroHandler {
    @Override
    public List<Libro> applicaFiltro(List<Libro> listaLibro,String statoLettura) {
        List<Libro> filtrati = new ArrayList<>();

        for(Libro l: listaLibro){

            if(l.getStatoLettura().getValore().trim().toLowerCase().contains(statoLettura.toLowerCase().trim()) && l.getStatoLettura() != null){
                filtrati.add(l);
            }
        }
        return filtrati;
    }

    @Override
    public String toString() {

        return "Stato Lettura";
    }
}

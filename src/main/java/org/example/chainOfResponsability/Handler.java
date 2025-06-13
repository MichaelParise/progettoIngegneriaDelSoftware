package org.example.chainOfResponsability;

import org.example.builderPattern.Libro;

import java.util.List;

public interface Handler {

    void setNext(Handler next);
    List<Libro> filtra(List<Libro> listaLibri);
    void setParametro(String parametro);

}

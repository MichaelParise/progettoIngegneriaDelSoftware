package org.example.strategyPattern;

import org.example.singleton.Libreria;


import java.util.Comparator;

public class OrdinaPerStelleCrescente implements OrdinaStrategy {

    @Override
    public void ordina() {
        Libreria.getInstance().getListaLibri().sort(Comparator.comparingInt(libro -> libro.getStelle().getValore()));

    }
}

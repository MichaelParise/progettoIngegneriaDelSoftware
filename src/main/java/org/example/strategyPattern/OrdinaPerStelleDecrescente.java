package org.example.strategyPattern;

import org.example.singleton.Libreria;

import java.util.Collections;
import java.util.Comparator;

public class OrdinaPerStelleDecrescente implements OrdinaStrategy {

    @Override
    public void ordina() {
        Libreria.getInstance().getListaLibri().sort(Comparator.comparingInt(libro -> libro.getStelle().getValore()));
        Collections.reverse(Libreria.getInstance().getListaLibri());//dalla valutazione più alta a quella più bassa
    }
}

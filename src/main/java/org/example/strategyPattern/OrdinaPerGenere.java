package org.example.strategyPattern;

import org.example.singleton.Libreria;

public class OrdinaPerGenere implements OrdinaStrategy {

    @Override
    public void ordina() {
        Libreria.getInstance().getListaLibri().sort((l1, l2) -> l1.getGenere().compareToIgnoreCase(l2.getGenere()));
    }
}

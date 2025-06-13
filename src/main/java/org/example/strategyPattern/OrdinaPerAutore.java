package org.example.strategyPattern;

import org.example.singleton.Libreria;

public class OrdinaPerAutore implements OrdinaStrategy {
    @Override
    public void ordina() {
        Libreria.getInstance().getListaLibri().sort((l1, l2) -> l1.getAutore().compareToIgnoreCase(l2.getAutore()));
    }
}

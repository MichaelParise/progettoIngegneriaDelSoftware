package org.example.strategyPattern;

import org.example.singleton.Libreria;

public class OrdinaPerTitolo implements OrdinaStrategy {

    @Override
    public void ordina() {
        Libreria.getInstance().getListaLibri().sort((l1, l2) -> l1.getTitolo().compareToIgnoreCase(l2.getTitolo()));
    }
}

package org.example.strategyPattern;

import org.example.singleton.Libreria;

public class OrdinaPerStatoLettura implements OrdinaStrategy {
    @Override
    public void ordina() {
        Libreria.getInstance().getListaLibri().sort((l1, l2) -> l1.getStatoLettura().getValore().compareToIgnoreCase(l2.getStatoLettura().getValore()));
    }
}

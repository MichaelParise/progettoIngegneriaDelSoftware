package org.example.strategyPattern;

import org.example.singleton.Libreria;

public class OrdinaPerLingua implements OrdinaStrategy {


    @Override
    public void ordina() {
        Libreria.getInstance().getListaLibri().sort((l1, l2) -> {
            String lingua1 = l1.getLingua();
            String lingua2 = l2.getLingua();

            if (lingua1 == null && lingua2 == null) return 0;
            if (lingua1 == null) return 1;  // null va dopo
            if (lingua2 == null) return -1;

            return lingua1.compareToIgnoreCase(lingua2);
        });
    }


}

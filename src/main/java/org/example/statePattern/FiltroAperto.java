package org.example.statePattern;

import org.example.libreria.LibreriaGUI;

public class FiltroAperto implements StatoFiltro {
    @Override
    public void gestisci(LibreriaGUI context) {
        context.getPannelloFiltri().setVisible(false);
        context.setStato(new FiltroChiuso());
    }
}

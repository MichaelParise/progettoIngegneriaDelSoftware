package org.example.statePattern;

import org.example.libreria.LibreriaGUI;

import java.awt.*;

public class FiltroChiuso implements StatoFiltro {
    @Override
    public void gestisci(LibreriaGUI context) {
        context.creaPannelloFiltri();
        context.add(context.getPannelloFiltri(), BorderLayout.WEST);
        context.getPannelloFiltri().setVisible(true);
        context.getPannelloFiltri().revalidate();
        context.getPannelloFiltri().repaint();
        context.setStato(new FiltroAperto());
    }
}


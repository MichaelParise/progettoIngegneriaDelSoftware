package org.example.mementoPattern;


import org.example.salvataggioDati.PersistenzaJson;
import org.example.singleton.Libreria;

import java.util.LinkedList;


public class LibreriaCaretaker {
    private final LinkedList<Libreria.LibreriaMemento> undoList = new LinkedList<>();
    private final LinkedList<Libreria.LibreriaMemento> redoList = new LinkedList<>();

    public LibreriaCaretaker() {
    }

    public void salvaStato() {
        undoList.addLast(Libreria.getInstance().salva());
        redoList.clear(); // ogni nuova azione annulla la possibilità di redo
    }

    public void undo() {
        Libreria.LibreriaMemento statoCorrente = Libreria.getInstance().salva();
        Libreria.LibreriaMemento daRipristinare = undoList.removeLast();

        redoList.addLast(statoCorrente);
        Libreria.getInstance().ripristina(daRipristinare);
        PersistenzaJson.salva(Libreria.getInstance().getListaLibri());

    }


    public void redo() {
        Libreria.LibreriaMemento statoCorrente = Libreria.getInstance().salva();
        Libreria.LibreriaMemento daRipristinare = redoList.removeLast();
        undoList.addLast(statoCorrente);
        Libreria.getInstance().ripristina(daRipristinare);
        PersistenzaJson.salva(Libreria.getInstance().getListaLibri());
    }

    public boolean canUndo() {
        return !undoList.isEmpty();
    }

    public boolean canRedo() {

        return !redoList.isEmpty();
    }




}


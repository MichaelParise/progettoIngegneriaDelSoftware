package org.example.libreria;

// package org.example.model;

public enum Valutazione {
    UNO(1),
    DUE(2),
    TRE(3),
    QUATTRO(4),
    CINQUE(5);

    private final int valore;

    Valutazione(int valore) {
        this.valore = valore;
    }

    public int getValore() {
        return valore;
    }

    @Override
    public String toString() {
        return String.valueOf(valore);
    }
}


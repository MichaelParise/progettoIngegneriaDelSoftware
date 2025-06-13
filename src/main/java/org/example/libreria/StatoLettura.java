package org.example.libreria;

public enum StatoLettura {

    LETTO("letto"),
    DA_LEGGERE("da leggere"),
    IN_LETTURA("in lettura");

    private final String valore;

    StatoLettura(String valore) {
        this.valore = valore;
    }

    public String getValore() {
        return valore;
    }

    @Override
    public String toString() {
        return valore;
    }
}

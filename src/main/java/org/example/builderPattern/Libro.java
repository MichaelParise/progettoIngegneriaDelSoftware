package org.example.builderPattern;

import org.example.libreria.StatoLettura;
import org.example.libreria.Valutazione;


//originator del pattern memento
public class Libro {
    private final String titolo;
    private final String autore;
    private final String codiceISBN;
    private final String genere;
    private final Valutazione stelle;
    private final StatoLettura statoLettura;
    private final String lingua;

    private Libro(Builder builder) {
        this.titolo = builder.titolo;
        this.autore = builder.autore;
        this.genere = builder.genere;
        this.codiceISBN = builder.isbn;
        this.stelle = builder.stelle;
        this.statoLettura = builder.stato;
        this.lingua = builder.lingua;
    }

    public String getTitolo() {
        return titolo;
    }

    public String getAutore() {
        return autore;
    }

    public String getCodiceISBN() {
        return codiceISBN;
    }

    public String getGenere() {
        return genere;
    }

    public Valutazione getStelle() {
        return stelle;
    }

    public StatoLettura getStatoLettura() {
        return statoLettura;
    }

    public String getLingua() {
        return lingua;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Libro libro))
            return false;
        return codiceISBN.equals(libro.codiceISBN);
    }








    public static class Builder {
        private final String titolo;
        private final String autore;
        private final String isbn;
        private final String genere;
        private final Valutazione stelle;
        private final StatoLettura stato;
        private String lingua;

        public Builder(String titolo, String autore, String isbn, String genere, Valutazione stelle, StatoLettura stato) {
            if (titolo == null || autore == null || isbn == null ||genere == null || stelle == null || stato == null) {
                throw new NullPointerException("Tutti i campi obbligatori devono essere non nulli");
            }

            if (!isbn.matches("\\d{4}")) {
                throw new IllegalArgumentException("L'ISBN deve essere composto da esattamente 4 cifre numeriche.");
            }

            this.titolo = titolo;
            this.autore = autore;
            this.isbn = isbn;
            this.genere = genere;
            this.stelle = stelle;
            this.stato = stato;
        }


        public Builder lingua(String lin) {
            this.lingua = lin;
            return this;
        }


        public Libro build() {
            return new Libro(this);
        }
    }
}




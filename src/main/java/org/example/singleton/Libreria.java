package org.example.singleton;

import org.example.builderPattern.Libro;

import org.example.salvataggioDati.PersistenzaJson;
import org.example.strategyPattern.OrdinaStrategy;

import java.util.*;

//receiver del pattern command
public final class Libreria {

    private static Libreria instance;
    private List<Libro> listaLibri;

    private Libreria() {
        this.listaLibri = PersistenzaJson.carica();
    }

    public List<Libro> getListaLibri() {
        return listaLibri;
    }

    public static Libreria getInstance() {
        if (instance == null) {
            instance = new Libreria();
        }

        return instance;
    }

    public void aggiungiLibro(Libro libro) {
        for (Libro l : listaLibri) {
            if (l.equals(libro) || l.getCodiceISBN() == libro.getCodiceISBN()) {
                return;
            }
        }
        listaLibri.add(libro);
        PersistenzaJson.salva(listaLibri);
    }


    public List<Libro> cerca(String parametro) {
        List<Libro> filtrati = new ArrayList<>();

        for (Libro libro : Libreria.getInstance().getListaLibri()) {
            if (libro.getTitolo().toLowerCase().contains(parametro) ||
                    libro.getAutore().toLowerCase().contains(parametro) ||
                    libro.getGenere().toLowerCase().contains(parametro) ||
                    String.valueOf(libro.getCodiceISBN()).toLowerCase().contains(parametro) ||
                    libro.getStatoLettura().toString().toLowerCase().contains(parametro) ||
                    String.valueOf(libro.getStelle()).equals(parametro)) {
                filtrati.add(libro);
            }
        }

        return filtrati;
    }



    //RIMOZIONE
    public StringBuilder rimuoviLibro(List<Libro> lista) {
        listaLibri.removeAll(lista);
        PersistenzaJson.salva(listaLibri);
        StringBuilder sb = new StringBuilder("Libri rimossi:\n");

        if (lista.isEmpty())
            return sb;

        for (Libro libro : lista) {
            sb.append("- ").append(libro.getTitolo()).append("\n");
        }
        return sb;
    }


    //MODIFICA
    public void modificaLibro(Libro esistente, Libro modificato) {
        int index = listaLibri.indexOf(esistente);
        if (index != -1) {
            listaLibri.set(index, modificato);
            PersistenzaJson.salva(listaLibri);
        }
    }

    public boolean esisteGiaISBN(int isbn, Libro escluso) {
        for (Libro l : listaLibri) {
            if (l.getCodiceISBN() == isbn && (!l.equals(escluso))) {
                return true;
            }
        }
        return false;
    }


    //Context del pattern Strategy
    public void eseguiStrategy(OrdinaStrategy strategy) {
        strategy.ordina();
    }




    //originator pattern memento
    public LibreriaMemento salva() {
        return new LibreriaMemento(listaLibri);
    }

    public void ripristina(LibreriaMemento memento) {
        listaLibri = memento.getStato(); // sovrascrivo lo stato corrente
    }


    //pattern memento
    public static class LibreriaMemento {
        private final List<Libro> stato;


        private LibreriaMemento(List<Libro> stato) {
            this.stato = new LinkedList<>();
            for (Libro libro : stato) {
                this.stato.add(new Libro.Builder(libro.getTitolo(),libro.getAutore(),libro.getCodiceISBN(),libro.getGenere(),libro.getStelle(),libro.getStatoLettura()).lingua(libro.getLingua()).build()); // supponendo tu abbia un copy constructor
            }
        }

        private List<Libro> getStato() {
            List<Libro> copia = new LinkedList<>();
            for (Libro libro : stato) {
                copia.add(new Libro.Builder(libro.getTitolo(),libro.getAutore(), libro.getCodiceISBN(),libro.getGenere(),libro.getStelle(),libro.getStatoLettura()).lingua(libro.getLingua()).build());
            }
            return copia;
        }
    }

}

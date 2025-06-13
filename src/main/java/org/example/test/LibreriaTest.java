package org.example.test;



import org.example.builderPattern.Libro;
import org.example.libreria.StatoLettura;
import org.example.libreria.Valutazione;
import org.example.mementoPattern.LibreriaCaretaker;
import org.example.salvataggioDati.PersistenzaJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


import org.example.singleton.Libreria;
import org.junit.jupiter.api.*;



public class LibreriaTest {

    private final LibreriaCaretaker caretaker = new LibreriaCaretaker();

   @BeforeEach
    void pulisci() {
        caretaker.salvaStato();
        Libreria.getInstance().getListaLibri().clear();

    }
    @AfterEach
    void ripristina(){
       caretaker.undo();
       PersistenzaJson.salva(Libreria.getInstance().getListaLibri());
    }

    @Test
    void testAggiuntaLibroUnico() {
        Libro libro = creaLibro(1234);
        Libreria.getInstance().aggiungiLibro(libro);
        assertEquals(1, Libreria.getInstance().getListaLibri().size());
        assertTrue(Libreria.getInstance().getListaLibri().contains(libro));
    }

    @Test

    void testAggiuntaDuplicatoISBN() {
        Libro libro1 = creaLibro(1234);
        Libro libro2 = creaLibro(1234); // stesso ISBN
        Libreria.getInstance().aggiungiLibro(libro1);
        Libreria.getInstance().aggiungiLibro(libro2);
        assertEquals(1, Libreria.getInstance().getListaLibri().size());
    }

    @Test
    void testRimozioneLibro() {
        Libro libro = creaLibro(5678);
        Libreria.getInstance().aggiungiLibro(libro);
        List<Libro> daRimuovere = List.of(libro);

        StringBuilder result = Libreria.getInstance().rimuoviLibro(daRimuovere);
        assertEquals(0, Libreria.getInstance().getListaLibri().size());
        assertTrue(result.toString().contains("Libri rimossi"));
    }

    @Test

    void testModificaLibro() {
        Libro libro = creaLibro(5678);
        Libreria.getInstance().aggiungiLibro(libro);

        Libro modificato = new Libro.Builder("Titolo Modificato", "Autore", Integer.parseInt("5678"), "Genere", Valutazione.QUATTRO, StatoLettura.IN_LETTURA).lingua("Italiano").build();
        Libreria.getInstance().modificaLibro(libro, modificato);

        assertEquals("Titolo Modificato", Libreria.getInstance().getListaLibri().get(0).getTitolo());
    }

    @Test
    void testCercaLibro() {
        Libro libro = creaLibro(4321);
        Libreria.getInstance().aggiungiLibro(libro);

        List<Libro> risultati = Libreria.getInstance().cerca("titolo");
        assertFalse(risultati.isEmpty());
    }


   //un metodo di supporto utilizzato solo per il testing, infatti è privato
    private Libro creaLibro(int isbn) {
        return new Libro.Builder(
                "Titolo",
                "Autore",
                isbn,
                "Genere",
                Valutazione.CINQUE,
                StatoLettura.LETTO
        ).lingua("Italiano").build();
    }

    @Test
    void testSingleton() {
        Libreria l1 = Libreria.getInstance();
        Libreria l2 = Libreria.getInstance();
        assertSame(l1, l2);
    }

}


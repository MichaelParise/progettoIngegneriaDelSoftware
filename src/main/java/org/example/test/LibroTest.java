package org.example.test;

import org.example.builderPattern.Libro;
import org.example.libreria.StatoLettura;
import org.example.libreria.Valutazione;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LibroTest {

    @Test
    void lanciaEccezioneSeISBNNonNumerico() {
        String inputIsbn = "abc1";
        assertThrows(NumberFormatException.class, () -> Integer.parseInt(inputIsbn));
    }


    @Test
    void lanciaEccezioneSeISBNNonHa4Cifre() {
        // Test ISBN troppo lungo (int)
        assertThrows(IllegalArgumentException.class, () -> new Libro.Builder(
                "Titolo",
                "Autore",
                12345,  // troppo lungo
                "Genere",
                Valutazione.CINQUE,
                StatoLettura.LETTO
        ).build());


    }


    @Test
    void lanciaEccezioneSeTitoloENullo() {
        assertThrows(NullPointerException.class, () -> new Libro.Builder(
                null,
                "Autore",
                1234,
                "Genere",
                Valutazione.CINQUE,
                StatoLettura.LETTO
        ).build());
    }

    @Test
    void lanciaEccezioneSeAutoreENullo() {
        assertThrows(NullPointerException.class, () -> new Libro.Builder(
                "Titolo",
                null,
                1234,
                "Genere",
                Valutazione.CINQUE,
                StatoLettura.LETTO
        ).build());
    }



    @Test
    void lanciaEccezioneSeGenereENullo() {
        assertThrows(NullPointerException.class, () -> new Libro.Builder(
                "Titolo",
                "Autore",
                1234,
                null,
                Valutazione.CINQUE,
                StatoLettura.LETTO
        ).build());
    }

    @Test
    void lanciaEccezioneSeValutazioneENulla() {
        assertThrows(NullPointerException.class, () -> new Libro.Builder(
                "Titolo",
                "Autore",
                1234,
                "Genere",
                null,
                StatoLettura.LETTO
        ).build());
    }

    @Test
    void lanciaEccezioneSeStatoLetturaENullo() {
        assertThrows(NullPointerException.class, () -> new Libro.Builder(
                "Titolo",
                "Autore",
                1234,
                "Genere",
                Valutazione.CINQUE,
                null
        ).build());
    }

    @Test
    void nonLanciaEccezioneSeLinguaENulla() {
        assertDoesNotThrow(() -> {
            new Libro.Builder(
                    "Titolo",
                    "Autore",
                    1234,
                    "Genere",
                    Valutazione.CINQUE,
                    StatoLettura.LETTO
            ).lingua(null).build();
        });
    }
}

package org.example.test;



import org.example.builderPattern.Libro;
import org.example.libreria.StatoLettura;
import org.example.libreria.Valutazione;
import org.example.salvataggioDati.PersistenzaJson;
import org.junit.jupiter.api.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PersistenzaJsonTest {

    private static final Path TEMP_FILE = Path.of("test_libreria.json");



    @AfterEach
    void eliminaFile() throws Exception {
        Files.delete(TEMP_FILE);
    }

    @Test
    void testSalvataggioECaricamento() {
        List<Libro> libriOriginali = List.of(
                new Libro.Builder("Titolo 1", "Autore 1", 1111, "Romanzo", Valutazione.CINQUE, StatoLettura.LETTO).lingua("IT").build(),
                new Libro.Builder("Titolo 2", "Autore 2", 2222, "Giallo", Valutazione.QUATTRO, StatoLettura.IN_LETTURA).lingua("EN").build()
        );

        PersistenzaJson.salvaNelPercorsoPersonalizzato(libriOriginali, TEMP_FILE.toString());
        List<Libro> libriCaricati = PersistenzaJson.caricaDaPercorsoPersonalizzato(TEMP_FILE.toString());

        assertEquals(libriOriginali, libriCaricati);
    }


}


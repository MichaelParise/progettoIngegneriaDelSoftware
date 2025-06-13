package org.example.salvataggioDati;



import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.example.builderPattern.Libro;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PersistenzaJson {
    private static final String FILE_PATH = "libreria.json";
    private static final Gson gson = new Gson();

    public static void salva(List<Libro> libri) {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(libri, writer);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Libro> carica() {
        try (Reader reader = new FileReader(FILE_PATH)) {
            Type tipoLista = new TypeToken<List<Libro>>() {}.getType();
            return gson.fromJson(reader, tipoLista);
        }
        catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public static void salvaNelPercorsoPersonalizzato(List<Libro> libri, String filePath) {
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(libri, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Libro> caricaDaPercorsoPersonalizzato(String filePath) {
        try (Reader reader = new FileReader(filePath)) {
            Type tipoLista = new TypeToken<List<Libro>>() {}.getType();
            return gson.fromJson(reader, tipoLista);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}


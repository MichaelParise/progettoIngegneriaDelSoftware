package org.example.libreria;

import org.example.builderPattern.Libro;
import org.example.singleton.Libreria;


import javax.swing.*;
import java.awt.*;

public class LibroDialog extends JDialog {

    private boolean confermato = false;
    private final JTextField titoloField;
    private final JTextField autoreField;
    private final JTextField genereField;
    private final JTextField isbnField;
    private final JTextField linguaField;
    private final JComboBox<Valutazione> stelleBox;
    private final JComboBox<StatoLettura> statoBox;

    private final Libro libroOriginale;

    public LibroDialog(JFrame parent, Libro libro) {
        super(parent, libro == null ? "Aggiungi Libro" : "Modifica Libro", true);
        this.libroOriginale = libro;

        setLayout(new BorderLayout());
        setSize(450, 400);
        setLocationRelativeTo(parent);

        // Campi input
        titoloField = new JTextField(20);
        autoreField = new JTextField(20);
        genereField = new JTextField(20);
        isbnField = new JTextField(20);
        linguaField = new JTextField(20);
        stelleBox = new JComboBox<>(Valutazione.values());
        statoBox = new JComboBox<>(StatoLettura.values());

        //campi precompilati
        if (libro != null) {
            titoloField.setText(libro.getTitolo());
            autoreField.setText(libro.getAutore());
            genereField.setText(libro.getGenere());
            isbnField.setText(libro.getCodiceISBN());
            linguaField.setText(libro.getLingua());
            stelleBox.setSelectedItem(libro.getStelle());
            statoBox.setSelectedItem(libro.getStatoLettura());
        }

        JPanel pannelloInput = new JPanel(new GridLayout(0, 2, 8, 8));
        pannelloInput.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        pannelloInput.add(new JLabel("Titolo*"));
        pannelloInput.add(titoloField);
        pannelloInput.add(new JLabel("Autore*"));
        pannelloInput.add(autoreField);
        pannelloInput.add(new JLabel("Genere*"));
        pannelloInput.add(genereField);
        pannelloInput.add(new JLabel("ISBN*"));
        pannelloInput.add(isbnField);
        pannelloInput.add(new JLabel("Stelle*"));
        pannelloInput.add(stelleBox);
        pannelloInput.add(new JLabel("Stato Lettura*"));
        pannelloInput.add(statoBox);
        pannelloInput.add(new JLabel("Lingua"));
        pannelloInput.add(linguaField);

        add(pannelloInput, BorderLayout.CENTER);


        JButton btnOk = new JButton("Conferma");
        JButton btnCancella = new JButton("Annulla");

        btnOk.addActionListener(e -> {
            if (campiValidi()) {
                confermato = true;
                dispose();
            }
        });

        btnCancella.addActionListener(e -> dispose());

        JPanel pannelloBottoni = new JPanel();
        pannelloBottoni.add(btnOk);
        pannelloBottoni.add(btnCancella);
        add(pannelloBottoni, BorderLayout.SOUTH);
    }

    private boolean campiValidi() {
        if (titoloField.getText().isBlank() || autoreField.getText().isBlank()
                || genereField.getText().isBlank()
                || stelleBox.getSelectedItem() == null || statoBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "I primi 6 campi sono obbligatori", "Errore", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if(!isbnField.getText().trim().matches("\\d{4}")){
            JOptionPane.showMessageDialog(this, "L'ISBN è composta da esattamente 4 cifre e non può contenere lettere.", "Errore", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (Libreria.getInstance().esisteGiaISBN(isbnField.getText().trim(), libroOriginale)) {
            JOptionPane.showMessageDialog(this, "ISBN già esistente.", "Errore", JOptionPane.WARNING_MESSAGE);
            return false;
        }


        return true;
    }





    public Libro mostraFinestra() {
        setVisible(true);
        Libro.Builder builder=new Libro.Builder(
                titoloField.getText().trim().toLowerCase(),
                autoreField.getText().trim().toLowerCase(),
                isbnField.getText().trim(),
                genereField.getText().trim().toLowerCase(),
                (Valutazione) stelleBox.getSelectedItem(),
                (StatoLettura) statoBox.getSelectedItem());

        if (confermato) {
            if (!linguaField.getText().trim().isEmpty()) {
                builder.lingua(linguaField.getText().trim().toLowerCase());
            }

            return builder.build();

        }
        return null;
    }


}



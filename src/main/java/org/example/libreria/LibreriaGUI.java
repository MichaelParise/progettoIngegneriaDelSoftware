package org.example.libreria;

import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;
import org.example.builderPattern.Libro;
import org.example.chainOfResponsability.*;


import org.example.mementoPattern.LibreriaCaretaker;
import org.example.salvataggioDati.PersistenzaJson;
import org.example.singleton.Libreria;

import org.example.statePattern.*;
import org.example.strategyPattern.*;
import org.example.strategyPattern.OrdinaPerGenere;
import org.example.strategyPattern.OrdinaPerTitolo;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LibreriaGUI extends JFrame {

    private final JTable table;
    private final DefaultTableModel tableModel;
    private final LibreriaCaretaker caretaker = new LibreriaCaretaker();
    private JPanel pannelloFiltri;
    private Map<JCheckBox, JComponent> pannelloCheckBox = new HashMap<>();
    private StatoFiltro statoFiltro;
    private final JTextField campoCerca;
    private final JButton btnUndo= new JButton("Undo");
    private final JButton btnRedo = new JButton("Redo");
    private int idContatore;


public LibreriaGUI() {
            Color tableSelection = new Color(220, 237, 255); // selezione tabella azzurrina
            idContatore = 1;
            this.statoFiltro = new FiltroChiuso();

            //inizialmente undo e redo sono disabilitati perchè non è stata fatta nessuna operazione sulla tabella
            btnUndo.setEnabled(caretaker.canUndo());
            btnRedo.setEnabled(caretaker.canRedo());
            try {
                UIManager.setLookAndFeel(new FlatLightLaf());
            } catch (UnsupportedLookAndFeelException ex) {
                ex.printStackTrace();
            }




//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            //creazione tabella
            tableModel = new DefaultTableModel(new Object[]{"ID", "Titolo", "Autore", "Genere", "codice ISBN", "Valutazione", "Stato Lettura", "Lingua"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Nessuna cella modificabile direttamente
                }
            };


            table = new JTable(tableModel);
            table.getTableHeader().setReorderingAllowed(false);
            table.setShowGrid(false);
            table.setIntercellSpacing(new Dimension(0, 0));
            table.setFillsViewportHeight(true);
            table.setShowHorizontalLines(true);
            table.setShowVerticalLines(true);
            table.setRowHeight(28);
            table.setSelectionBackground(tableSelection);
            table.setSelectionForeground(Color.BLACK);




            JTableHeader header = table.getTableHeader();
            header.setFont(new Font("Segoe UI", Font.BOLD, 15));
            header.setBackground(new Color(30, 60, 90));
            header.setForeground(Color.WHITE);

            DefaultTableCellRenderer modernRenderer = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                                                               boolean isSelected, boolean hasFocus,
                                                               int row, int column) {
                    JLabel label = (JLabel) super.getTableCellRendererComponent(
                            table, value, isSelected, hasFocus, row, column);

                    label.setBorder(null);
                    label.setOpaque(true);
                    label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    label.setForeground(isSelected ? Color.BLACK : new Color(50, 50, 50));
                    label.setBackground(isSelected ? new Color(210, 230, 255) : Color.WHITE);
                    return label;
                }
            };
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(modernRenderer);
            }
            //titolo dell'app
            setTitle("Libreria personale");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(900, 500);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());
            setVisible(true);
            aggiornaTabella(Libreria.getInstance().getListaLibri());

            //se la tabella è molto grande si aggiunge automaticamente la barra di scorrimento
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.getViewport().setBackground(Color.WHITE);
            add(scrollPane,BorderLayout.CENTER);



            //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


            //BOTTONE AGGIUNGI
            JButton btnAggiungi = new JButton("Aggiungi");
            btnAggiungi.setPreferredSize(new Dimension(100, 30));
            btnAggiungi.setFont(new Font("Segoe UI", Font.BOLD, 18));

            //AGGIUNGI
            btnAggiungi.addActionListener(e -> {
                try {
                    Libro libro = new LibroDialog(this, null).mostraFinestra();


                    if (libro != null) {
                        caretaker.salvaStato();
                        idContatore++;
                        Libreria.getInstance().aggiungiLibro(libro);
                        JOptionPane.showMessageDialog(this, "Libro aggiunto: " + libro.getTitolo());
                        aggiornaTabella(Libreria.getInstance().getListaLibri());
                        btnUndo.setEnabled(caretaker.canUndo());
                        btnRedo.setEnabled(caretaker.canRedo());


                    }

                }
                catch (IllegalArgumentException ex) {

                }
            });

            //BOTTONE MODIFICA
            JButton btnModifica = new JButton("Modifica");
            btnModifica.setPreferredSize(new Dimension(100, 30));
            btnModifica.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            //MODIFICA
            btnModifica.addActionListener(e -> {
                int rigaSelezionata = table.getSelectedRow();
                int[] righeSelezionate = table.getSelectedRows();

                if (rigaSelezionata == -1) {
                    JOptionPane.showMessageDialog(this, "Seleziona un libro da modificare");
                    return;
                }

                if(righeSelezionate.length > 1){
                    JOptionPane.showMessageDialog(this, "Puoi modificare un solo libro per volta");
                    return;
                }

                Libro esistente = Libreria.getInstance().getListaLibri().get(rigaSelezionata);
                Libro modificato = new LibroDialog(this, esistente).mostraFinestra();

                if (modificato != null) {
                    caretaker.salvaStato();
                    Libreria.getInstance().modificaLibro(esistente, modificato);
                    aggiornaTabella(Libreria.getInstance().getListaLibri());
                    btnUndo.setEnabled(caretaker.canUndo());
                    btnRedo.setEnabled(caretaker.canRedo());
                }
            });



            //BOTTONE RIMUOVI
            JButton btnRimuovi = new JButton("Rimuovi");
            btnRimuovi.setPreferredSize(new Dimension(100, 30));
            btnRimuovi.setFont(new Font("Segoe UI", Font.PLAIN, 18));

            btnRimuovi.addActionListener(e -> {
                int[] righeSelezionate = table.getSelectedRows();
                if (righeSelezionate.length == 0) {
                    JOptionPane.showMessageDialog(this, "Seleziona uno o più libri da rimuovere.");
                    return;
                }

                caretaker.salvaStato();
                List<Libro> daRimuovere = new ArrayList<>();
                for (int riga : righeSelezionate) {
                    daRimuovere.add(Libreria.getInstance().getListaLibri().get(riga));
                }

                StringBuilder sb = Libreria.getInstance().rimuoviLibro(daRimuovere);
                JOptionPane.showMessageDialog(this, sb.toString(), "Rimozione completata", JOptionPane.INFORMATION_MESSAGE);
                aggiornaTabella(Libreria.getInstance().getListaLibri());
                btnUndo.setEnabled(caretaker.canUndo());
                btnRedo.setEnabled(caretaker.canRedo());

            });

            JButton btnMostraTabella = new JButton("Carica Libri");

            btnMostraTabella.addActionListener(e -> aggiornaTabella(PersistenzaJson.carica()));

            //pannello dei bottoni in basso
            JPanel pannelloInferiore = new JPanel(new MigLayout("insets 10, gap 20",
                    "[grow, fill] [grow, fill] [grow, fill]",  // 3 colonne che crescono e i bottoni si espandono
                    "[]"));
            pannelloInferiore.setBackground(Color.WHITE);
            pannelloInferiore.add(btnAggiungi);
            pannelloInferiore.add(btnModifica);
            pannelloInferiore.add(btnRimuovi);
            add(pannelloInferiore,BorderLayout.SOUTH);

//---------------------------------------------------------------------------------------------------------------------------------------------------------------
            // BOTTONE ORDINA:Mostra il menu quando il bottone è cliccato
            JButton btnOrdina = new JButton("Ordina");
            JPopupMenu menuOrdina = new JPopupMenu();
            JMenuItem ordinaTitolo = new JMenuItem("Per Titolo");
            JMenuItem ordinaGenere = new JMenuItem("Per Genere");
            JMenuItem ordinaAutore = new JMenuItem("Per Autore");
            JMenuItem ordinaStelleCrescente = new JMenuItem("Per Valutazione(crescente)");
            JMenuItem ordinaStelleDecrescente = new JMenuItem("Per Valutazione(decrescente)");
            JMenuItem ordinaStato = new JMenuItem("Per Stato Lettura");
            JMenuItem ordinaLingua = new JMenuItem("Per Lingua");

            menuOrdina.add(ordinaTitolo);
            menuOrdina.add(ordinaGenere);
            menuOrdina.add(ordinaAutore);
            menuOrdina.add(ordinaStelleCrescente);
            menuOrdina.add(ordinaStelleDecrescente);
            menuOrdina.add(ordinaStato);
            menuOrdina.add(ordinaLingua);

            btnOrdina.addActionListener(e -> menuOrdina.show(btnOrdina, 0, btnOrdina.getHeight()));
            ordinaTitolo.addActionListener(e -> {
                caretaker.salvaStato();
                Libreria.getInstance().eseguiStrategy(new OrdinaPerTitolo());
                aggiornaTabella(Libreria.getInstance().getListaLibri());
                btnUndo.setEnabled(caretaker.canUndo());
                btnRedo.setEnabled(caretaker.canRedo());

            });
            ordinaGenere.addActionListener(e -> {
                caretaker.salvaStato();
                Libreria.getInstance().eseguiStrategy(new OrdinaPerGenere());
                aggiornaTabella(Libreria.getInstance().getListaLibri());
                btnUndo.setEnabled(caretaker.canUndo());
                btnRedo.setEnabled(caretaker.canRedo());
            });
            ordinaAutore.addActionListener(e -> {
                caretaker.salvaStato();
                Libreria.getInstance().eseguiStrategy(new OrdinaPerAutore());
                aggiornaTabella(Libreria.getInstance().getListaLibri());
                btnUndo.setEnabled(caretaker.canUndo());
                btnRedo.setEnabled(caretaker.canRedo());
            });
            ordinaStelleDecrescente.addActionListener(e -> {
                caretaker.salvaStato();
                Libreria.getInstance().eseguiStrategy(new OrdinaPerStelleDecrescente());
                aggiornaTabella(Libreria.getInstance().getListaLibri());
                btnUndo.setEnabled(caretaker.canUndo());
                btnRedo.setEnabled(caretaker.canRedo());
            });

            ordinaStelleCrescente.addActionListener(e -> {
                caretaker.salvaStato();
                Libreria.getInstance().eseguiStrategy(new OrdinaPerStelleCrescente());
                aggiornaTabella(Libreria.getInstance().getListaLibri());
                btnUndo.setEnabled(caretaker.canUndo());
                btnRedo.setEnabled(caretaker.canRedo());
            });
            ordinaStato.addActionListener(e -> {
                caretaker.salvaStato();
                Libreria.getInstance().eseguiStrategy(new OrdinaPerStatoLettura());
                aggiornaTabella(Libreria.getInstance().getListaLibri());
                btnUndo.setEnabled(caretaker.canUndo());
                btnRedo.setEnabled(caretaker.canRedo());
            });

            ordinaLingua.addActionListener(e -> {
                caretaker.salvaStato();
                Libreria.getInstance().eseguiStrategy(new OrdinaPerLingua());
                aggiornaTabella(Libreria.getInstance().getListaLibri());
                btnUndo.setEnabled(caretaker.canUndo());
                btnRedo.setEnabled(caretaker.canRedo());
            });

            //BOTTONE FILTRA
            JButton btnFiltra  = new JButton("Filtra");

            btnFiltra.addActionListener(e -> premiBottone());

            //BOTTONE CERCA
            campoCerca = new JTextField(15);
            JButton btnCerca = new JButton("Cerca");

            btnCerca.addActionListener(e -> {
                String valore = campoCerca.getText();
                if (valore.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Inserisci qualcosa nella barra di ricerca.");
                    return;
                }
                List<Libro> risultati = Libreria.getInstance().cerca(campoCerca.getText().toLowerCase());
                aggiornaTabella(risultati);
            });



            //GESTIONE DEL CAMPO DI RICERCA: fa in modo che quando il campo di ricerca è vuoto la tabella torna allo stato di partenza
            campoCerca.getDocument().addDocumentListener(
                    new DocumentListener() {
                        private void aggiorna() {
                            String valore = campoCerca.getText().trim();
                            if (valore.isEmpty()) {
                                btnCerca.setEnabled(false);
                                aggiornaTabella(Libreria.getInstance().getListaLibri());
                            }

                            else{
                                btnCerca.setEnabled(true);
                            }
                        }

                        @Override
                        public void insertUpdate(DocumentEvent e) {
                            aggiorna();
                        }

                        @Override
                        public void removeUpdate(DocumentEvent e) {
                            aggiorna();
                        }

                        @Override
                        public void changedUpdate(DocumentEvent e) {
                            aggiorna();
                        }
                    });
        //UNDO
        btnUndo.addActionListener(e -> {
            if(!caretaker.canUndo())
                JOptionPane.showMessageDialog(this, "Prima di fare undo devi eseguire qualche operazione sulla libreria");

            else {
                caretaker.undo();
                aggiornaTabella(Libreria.getInstance().getListaLibri());

                btnUndo.setEnabled(caretaker.canUndo());
                btnRedo.setEnabled(caretaker.canRedo());
            }

        });


        //REDO

        btnRedo.addActionListener(e -> {
            if(!caretaker.canRedo())
                JOptionPane.showMessageDialog(this, "Prima di fare redo devi eseguire qualche operazione sulla libreria");
            else {
                caretaker.redo();
                aggiornaTabella(Libreria.getInstance().getListaLibri());
                btnUndo.setEnabled(caretaker.canUndo());
                btnRedo.setEnabled(caretaker.canRedo());

            }

        });






        //pannello in alto a destra con undo e redo
        JPanel pannelloSuperioreDestro = new JPanel(new MigLayout("insets 0, gap 10", "[grow][]", "[]"));
        pannelloSuperioreDestro.add(btnUndo);
        pannelloSuperioreDestro.add(btnRedo);
        pannelloSuperioreDestro.add((Box.createRigidArea(new Dimension(1, 0))));
        btnUndo.setEnabled(caretaker.canUndo());
        btnRedo.setEnabled(caretaker.canRedo());

        //pannello in alto a sinistra con ordina, filtra e cerca
        JPanel pannelloSuperioreSinistro = new JPanel(new MigLayout("insets 0, gap 10",
                "[][][grow][]",
                "[]"));
        pannelloSuperioreSinistro.setBackground(new Color(245, 245, 245));
        pannelloSuperioreSinistro.setBorder(null);
        pannelloSuperioreSinistro.add((Box.createRigidArea(new Dimension(1, 0))));
        pannelloSuperioreSinistro.add(btnFiltra);
        pannelloSuperioreSinistro.add(btnOrdina);



        //PANNELLO SUPERIORE CENTRALE
        JPanel pannelloSuperioreCentrale = new JPanel(new MigLayout("insets 0, align center", "[]", "[]"));

        pannelloSuperioreCentrale.add(btnCerca);
        pannelloSuperioreCentrale.add(campoCerca);
        pannelloSuperioreCentrale.add(btnMostraTabella);


        //PANNELLO SUPERIORE PRINCIPALE
        JPanel pannelloSuperiore = new JPanel(new MigLayout("insets 0, fill",
                "[left][center][right]", "[]"));
        pannelloSuperiore.add(pannelloSuperioreSinistro);
        pannelloSuperiore.add(pannelloSuperioreCentrale);
        pannelloSuperiore.add(pannelloSuperioreDestro);

        pannelloSuperiore.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        // Aggiunta del pannello che comprende pannelloSuperioreSinistro e UndoRedoPanel in alto
        add(pannelloSuperiore,BorderLayout.NORTH);

        //BOTTONI ARROTONDATI
        personalizzaBottone(btnAggiungi);
        personalizzaBottone(btnModifica);
        personalizzaBottone(btnRimuovi);
        personalizzaBottone(btnOrdina);
        personalizzaBottone(btnUndo);
        personalizzaBottone(btnRedo);
        personalizzaBottone(btnCerca);
        personalizzaBottone(btnFiltra);
        personalizzaBottone(btnMostraTabella);
        aggiungiHover(btnAggiungi);
        aggiungiHover(btnModifica);
        aggiungiHover(btnRimuovi);
        aggiungiHover(btnOrdina);
        aggiungiHover(btnUndo);
        aggiungiHover(btnRedo);
        aggiungiHover(btnCerca);
        aggiungiHover(btnFiltra);
        aggiungiHover(btnMostraTabella);

    }


    //aggiorna la tabella dopo ogni cambiamento
    private void aggiornaTabella(List<Libro> listaLibri) {
        tableModel.setRowCount(0);
        idContatore = 0;

        for (Libro libro : listaLibri) {
            idContatore++;
            tableModel.addRow(new Object[]{
                    idContatore,
                    libro.getTitolo(),
                    libro.getAutore(),
                    libro.getGenere(),
                    libro.getCodiceISBN(),
                    libro.getStelle(),
                    libro.getStatoLettura(),
                    libro.getLingua(),
            });
        }
    }
//----------------------------------------------------------------------------------------------------------------------------------------

   //ESTETICA BOTTONI
    private void personalizzaBottone(JButton button) {
        button.setBackground(new Color(30, 60, 90));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorderPainted(false);

        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                AbstractButton b = (AbstractButton) c;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (b.isEnabled()) {
                    if (b.getModel().isPressed()) {
                        g2.setColor(new Color(60, 110, 160));
                    } else {
                        g2.setColor(b.getBackground());
                    }

                    g2.fillRoundRect(0, 0, b.getWidth(), b.getHeight(), 30, 30);
                }

                // Testo centrato (anche se disabilitato)
                FontMetrics fm = b.getFontMetrics(b.getFont());
                Rectangle r = new Rectangle(b.getWidth(), b.getHeight());
                int x = (r.width - fm.stringWidth(b.getText())) / 2;
                int y = (r.height - fm.getHeight()) / 2 + fm.getAscent();

                if (b.isEnabled()) {
                    g2.setColor(b.getForeground());
                } else {
                    g2.setColor(Color.GRAY);
                }

                g2.drawString(b.getText(), x, y);
                g2.dispose();
            }

        });
    }

    private void aggiungiHover(JButton button) {
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(50, 80, 110)
                );
            }

            public void mouseExited(MouseEvent evt) {
                button.setBackground(
                        new Color(30, 60, 90)
                );
            }
        });
    }

//--------------------------------------------------------------------------------------------------------------------------------------------

    //GESTIONE PANNELLO FILTRI
    List<FiltroHandler> nomiFiltri = List.of
            (new FiltraPerTitolo(),
                    new FiltraPerAutore(),
                    new FiltraPerGenere(),
                    new FiltraPerISBN(),
                    new FiltraPerStelle(),
                    new FiltraPerStato(),
                    new FiltraPerLingua()
            );


    //PANNELLO LATERALE CHE SI APRE QUANDO PREMO IL BOTTONE FILTRA
    public void creaPannelloFiltri() {
        pannelloCheckBox = new HashMap<>();
        JButton btnApplicaFiltro = new JButton("Applica filtro");
        JButton btnResettaFiltri = new JButton("Resetta filtri");
        JButton btnChiudiFiltro = new JButton("Chiudi filtri");

        pannelloFiltri = new JPanel(new MigLayout("wrap 2", "[][100px!]", "[]5[]5[]5[]"));
        pannelloFiltri.setBackground(UIManager.getColor("Panel.background"));
        pannelloFiltri.setPreferredSize(new Dimension(320, 220));
        pannelloFiltri.setMaximumSize(new Dimension(320, 220));

        //mi creo una checkBox associata a ogni filtro, scegliendo opportunamnete se inserire una ComboBox o un campo di testo
        for (Handler fh : nomiFiltri) {
            JCheckBox checkBox = new JCheckBox(fh.toString());
            JComponent input = getInput(fh);
            input.setEnabled(false);//le textBox inizialmente sono disattivate

            //si attivano quando la checkBox corrispondente è selezionata
            checkBox.addActionListener(e -> {
                input.setEnabled(checkBox.isSelected());
                aggiornaStatoBtnResetta(btnResettaFiltri);
            });
            pannelloCheckBox.put(checkBox, input);
            pannelloFiltri.add(checkBox, "gapright 10");
            pannelloFiltri.add(input, "growx, wrap");
        }


        //CHIUDI FILTRO
        btnChiudiFiltro.addActionListener(e -> premiBottone());


        //RESETTA FILTRI
        btnResettaFiltri.setEnabled(false);
        btnResettaFiltri.addActionListener(e -> {
            btnResettaFiltri.setEnabled(false);
            aggiornaTabella(Libreria.getInstance().getListaLibri());
            PersistenzaJson.salva(Libreria.getInstance().getListaLibri());

            for (Map.Entry<JCheckBox, JComponent> entry : pannelloCheckBox.entrySet()) {
                JCheckBox cb = entry.getKey();
                JComponent input = entry.getValue();
                cb.setSelected(false);
                input.setEnabled(false);

                if (input instanceof JTextField tf) {
                    tf.setText("");
                }

                else if (input instanceof JComboBox<?> combo) {
                    if (combo.getItemCount() > 0) {
                        combo.setSelectedIndex(0);//di default imposta il primo valore presente nella tendina
                    }
                }
            }
        });



        //APPLICA FILTRO
        btnApplicaFiltro.addActionListener(e -> {
            List<Handler> catena = new ArrayList<>();
            for (Map.Entry<JCheckBox, JComponent> entry : pannelloCheckBox.entrySet()) {
                JCheckBox checkBox = entry.getKey();
                if (checkBox.isSelected()) {
                    JComponent input = entry.getValue();
                    String nomeFiltro = checkBox.getText();
                    String parametroRicerca = "";

                    if (input instanceof JTextField tf) {
                        parametroRicerca = tf.getText().trim();
                    }
                    else if (input instanceof JComboBox<?> combo) {
                        Object selezionato = combo.getSelectedItem();
                        if (selezionato != null) {
                            parametroRicerca = selezionato.toString().trim();
                        }


                    }

                    if (parametroRicerca.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Inserisci un valore per " + nomeFiltro);
                        return;
                    }
                    Handler filtro = getFiltroHandler(nomeFiltro, parametroRicerca);
                    catena.add(filtro);
                }

            }

            if (catena.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleziona almeno un filtro.");
                return;
            }

            for (int i = 0; i < catena.size() - 1; i++) {
                catena.get(i).setNext(catena.get(i + 1));
            }

            //UTILIZZO DI CHAIN OF RESPONSABILITY
            List<Libro> risultati = catena.get(0).filtra(Libreria.getInstance().getListaLibri());
            aggiornaTabella(risultati);

            pannelloFiltri.setVisible(true);
            pannelloFiltri.setBorder(BorderFactory.createCompoundBorder(
            ));

        });
        personalizzaBottone(btnApplicaFiltro);
        personalizzaBottone(btnChiudiFiltro);
        personalizzaBottone(btnResettaFiltri);

        aggiungiHover(btnApplicaFiltro);
        aggiungiHover(btnChiudiFiltro);
        aggiungiHover(btnResettaFiltri);

        pannelloFiltri.add(btnApplicaFiltro);
        pannelloFiltri.add(btnChiudiFiltro);
        pannelloFiltri.add(btnResettaFiltri);


    }

    private JComponent getInput(Handler fh) {
        JComponent input;
        if (fh instanceof FiltraPerStato) {
            input = new JComboBox<>(new String[]{
                    StatoLettura.LETTO.getValore(),
                    StatoLettura.DA_LEGGERE.getValore(),
                    StatoLettura.IN_LETTURA.getValore()
            });
        }

        else if (fh instanceof FiltraPerStelle) {
            input = new JComboBox<>(new Valutazione[]{
                    Valutazione.UNO, Valutazione.DUE,
                    Valutazione.TRE, Valutazione.QUATTRO,
                    Valutazione.CINQUE
            });
        }
        else {
            input = new JTextField(15);
        }
        return input;
    }

    private void aggiornaStatoBtnResetta(JButton btnResettaFiltri) {
        boolean almenoUnaSelezionata = false;
        for (AbstractButton bottone : pannelloCheckBox.keySet()) {
            if (bottone.isSelected()) {
                almenoUnaSelezionata = true;
                break;
            }
        }

        btnResettaFiltri.setEnabled(almenoUnaSelezionata);
    }




    private Handler getFiltroHandler(String nomeFiltro, String parametroRicerca) {
        Handler filtro = null;

        for (FiltroHandler fh : nomiFiltri) {
            if (fh.toString().equals(nomeFiltro)) {
                if (fh instanceof FiltraPerTitolo)
                    filtro = new FiltraPerTitolo();
                else if (fh instanceof FiltraPerAutore)
                    filtro = new FiltraPerAutore();
                else if (fh instanceof FiltraPerGenere)
                    filtro = new FiltraPerGenere();
                else if (fh instanceof FiltraPerISBN)
                    filtro = new FiltraPerISBN();
                else if (fh instanceof FiltraPerStelle)
                    filtro = new FiltraPerStelle();
                else if (fh instanceof FiltraPerStato)
                    filtro = new FiltraPerStato();
                else if (fh instanceof FiltraPerLingua)
                    filtro = new FiltraPerLingua();
                break;
            }
        }

        if (filtro != null) {
            filtro.setParametro(parametroRicerca);
        }

        return filtro;
    }




    //STATE PATTERN PER IL BOTTONE FILTRA
    public void setStato(StatoFiltro stato) {
        this.statoFiltro = stato;
    }

    public void premiBottone() {
        statoFiltro.gestisci(this);
    }

    public JPanel getPannelloFiltri() {
        return pannelloFiltri;
    }





}
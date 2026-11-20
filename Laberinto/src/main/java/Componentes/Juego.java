package Componentes;

import Entidades.Casilla;
import java.awt.Dimension;
import java.util.List;
import javax.swing.*;

/**
 *
 * @author AleX JGT
 */
public class Juego extends javax.swing.JFrame {

    private Laberinto laberinto;
    private RecorridoBFS bfs;
    private RecorridoTopologico topologico;
    private RecorridoEstrella estrella;
    private boolean generado = false;
    private JTextField campoFilaEntrada;
    private JComboBox<String> cmbAlgoritmo;

    public Juego() {
        laberinto = new Laberinto();
        bfs = new RecorridoBFS(laberinto);

        initComponents();
        configurarMenu();

        setLocationRelativeTo(null);
        setResizable(false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("LABERINTO DE GRAFO");
    }

    private void crearLaberinto() {
        try {
            int filas = Integer.parseInt(jTFilas.getText().trim());
            int columnas = Integer.parseInt(jTColumnas.getText().trim());
            if (filas < 1 || columnas < 2 || filas > 40 || columnas > 70) {
                throw new IllegalArgumentException(
                        "Ingresa entre 1 y 40 filas y entre 2 y 70 columnas.");
            }

            int filaEntrada = -1; // -1 = entradas aleatorias
            String textoEntrada = campoFilaEntrada.getText().trim();
            if (!textoEntrada.isEmpty()) {
                filaEntrada = Integer.parseInt(textoEntrada) - 1;
                if (filaEntrada < 0 || filaEntrada >= filas) {
                    throw new IllegalArgumentException(
                            "La fila de entrada debe estar entre 1 y " + filas + ".");
                }
            }

            estrella = null;
            generado = false;
            btnResolver.setEnabled(false);
            boolean completable;
            int intentos = 0;
            do {
                laberinto.setDimensiones(filas, columnas, filaEntrada);
                completable = bfs.existeCaminoBFS();
                intentos++;
            } while (completableCheckBox.isSelected() && !completable && intentos < 200);

            // Evita recursividad y asegura el camino si 200 intentos no bastaron.
            if (completableCheckBox.isSelected() && !completable) {
                laberinto.garantizarCamino();
                completable = bfs.existeCaminoBFS();
            }

            generado = true;
            btnResolver.setEnabled(generado);
            jLaberinto.repaint();
            if (completable) {
                JOptionPane.showMessageDialog(this,
                        "Laberinto creado con " + laberinto.getEntradas().size()
                        + " entradas y " + laberinto.getSalidas().size() + " salidas.");
            } else {
                JOptionPane.showMessageDialog(this,
                        "No existe camino entre ninguna entrada y salida.",
                        "Laberinto sin solución", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Ingresa números enteros válidos para las dimensiones.",
                    "Datos inválidos", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Datos inválidos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void configurarMenu() {

        JMenuBar barra = new JMenuBar();
        JMenu menu = new JMenu("Configuración");

        JCheckBoxMenuItem opcionPesosNegativos
                = new JCheckBoxMenuItem(
                        "Ponderaciones de -100 a 100", false
                );

        opcionPesosNegativos.addActionListener(e -> {
            laberinto.setPermitirPesosNegativos(
                    opcionPesosNegativos.isSelected()
            );

            JOptionPane.showMessageDialog(this,
                    "El rango de ponderaciones se aplicará "
                    + "al generar otro laberinto.");
        });

        menu.add(opcionPesosNegativos);
        barra.add(menu);

        barra.add(javax.swing.Box.createHorizontalStrut(20));

        barra.add(new JLabel("Algoritmo: "));

        cmbAlgoritmo = new JComboBox<>(
                new String[]{"BFS", "Topológico", "A*"}
        );

        barra.add(cmbAlgoritmo);

        barra.add(javax.swing.Box.createHorizontalStrut(20));

        barra.add(new JLabel("Fila de entrada (opcional): "));

        campoFilaEntrada = new JTextField(3);
        campoFilaEntrada.setMaximumSize(new Dimension(50, 24));

        barra.add(campoFilaEntrada);

        setJMenuBar(barra);
    }


    private void mostrarRecorridoBFS() {
        if (!generado) {
            JOptionPane.showMessageDialog(this, "Primero genera un laberinto.");
            return;
        }

        boolean hayCamino = bfs.existeCaminoBFS();
        String texto = "BFS\n¿Existe un camino? " + (hayCamino ? "Sí" : "No") + "\n\n";

        texto += "Orden de visita:\n";
        for (Casilla c : bfs.getOrdenVisitados()) {
            texto += posicion(c) + " ";
        }
        texto += "\n\nPasos:\n";
        for (String paso : bfs.getPasos()) {
            texto += paso + "\n";
        }

        mostrarTexto("Recorrido BFS", texto);
    }

    private void mostrarOrdenTopologico() {
        if (!generado) {
            JOptionPane.showMessageDialog(this, "Primero genera un laberinto.");
            return;
        }

        topologico = new RecorridoTopologico(laberinto);
        boolean valido = topologico.ordenar();
        String texto = "Orden topológico (grafo dirigido: derecha y abajo)\n";
        texto += "¿Es válido? " + (valido ? "Sí" : "No") + "\n\n";

        texto += "Orden de visita:\n";
        for (Casilla c : topologico.getOrdenVisitados()) {
            texto += posicion(c) + " ";
        }
        texto += "\n\nPasos:\n";
        for (String paso : topologico.getPasos()) {
            texto += paso + "\n";
        }

        mostrarTexto("Recorrido topológico", texto);
    }

    private void mostrarPasosEstrella() {
        if (estrella == null) {
            JOptionPane.showMessageDialog(this,
                    "Primero presiona Resolver para ejecutar A*.");
            return;
        }

        String texto = "A*\nCosto total: " + estrella.getCostoTotal() + "\n\n";
        texto += "Orden de visita:\n";
        for (Casilla c : estrella.getOrdenVisitados()) {
            texto += posicion(c) + " ";
        }
        texto += "\n\nPasos:\n";
        for (String paso : estrella.getPasos()) {
            texto += paso + "\n";
        }

        mostrarTexto("Recorrido A*", texto);
    }

    private void mostrarTexto(String titulo, String texto) {
        JTextArea area = new JTextArea(texto);
        area.setEditable(false);
        area.setCaretPosition(0);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(650, 400));
        JOptionPane.showMessageDialog(this, scroll, titulo,
                JOptionPane.INFORMATION_MESSAGE);
    }

    private String posicion(Casilla c) {
        return "(" + (c.getY() + 1) + "," + (c.getX() + 1) + ")";
    }

    private void resolverBFS() {
        if (!generado) {
            JOptionPane.showMessageDialog(this,
                    "Primero genera un laberinto.");
            return;
        }

        estrella = null;
        laberinto.setInicioSeleccionado(null);
        laberinto.setCaminoSolucion(null);
        jLaberinto.repaint();

        mostrarRecorridoBFS();
    }

    private void resolverTopologico() {
        if (!generado) {
            JOptionPane.showMessageDialog(this,
                    "Primero genera un laberinto.");
            return;
        }

        estrella = null;
        laberinto.setInicioSeleccionado(null);
        laberinto.setCaminoSolucion(null);
        jLaberinto.repaint();

        mostrarOrdenTopologico();
    }

    private void resolverEstrella() {

        if (!generado) {
            JOptionPane.showMessageDialog(this,
                    "Primero genera un laberinto.");
            return;
        }

        List<Casilla> entradas = laberinto.getEntradas();
        String[] opciones = new String[entradas.size()];

        for (int i = 0; i < entradas.size(); i++) {
            opciones[i] = "Entrada " + (i + 1)
                    + " " + posicion(entradas.get(i));
        }

        String seleccion = (String) JOptionPane.showInputDialog(
                this,
                "Selecciona la entrada:",
                "A*",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (seleccion == null) {
            return;
        }

        Casilla inicio = null;

        for (int i = 0; i < opciones.length; i++) {
            if (opciones[i].equals(seleccion)) {
                inicio = entradas.get(i);
                break;
            }
        }

        laberinto.setInicioSeleccionado(inicio);
        estrella = new RecorridoEstrella(laberinto);

        try {
            boolean encontrado = estrella.resolver(inicio);

            laberinto.setCaminoSolucion(estrella.getCamino());
            jLaberinto.repaint();

            if (encontrado) {
                JOptionPane.showMessageDialog(this,
                        "Camino encontrado con A*.\n"
                        + "Costo total: " + estrella.getCostoTotal()
                        + "\nCasillas del camino: "
                        + estrella.getCamino().size());
            } else {
                JOptionPane.showMessageDialog(this,
                        "No existe un camino desde la entrada seleccionada.");
            }

            mostrarPasosEstrella();

        } catch (IllegalArgumentException ex) {
            estrella = null;
            laberinto.setCaminoSolucion(null);
            jLaberinto.repaint();

            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "A* no disponible",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        JFondo = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLaberinto = new PanelLaberinto(laberinto);
        jPanel1 = new javax.swing.JPanel();
        btnConfirmar = new javax.swing.JButton();
        jTFilas = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTColumnas = new javax.swing.JTextField();
        completableCheckBox = new javax.swing.JCheckBox();
        btnResolver = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        JFondo.setBackground(new java.awt.Color(192, 92, 237));

        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 48)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Laberinto");

        jLaberinto.setBackground(new java.awt.Color(255, 255, 255));
        jLaberinto.setPreferredSize(new java.awt.Dimension(920, 540));

        javax.swing.GroupLayout jLaberintoLayout = new javax.swing.GroupLayout(jLaberinto);
        jLaberinto.setLayout(jLaberintoLayout);
        jLaberintoLayout.setHorizontalGroup(
            jLaberintoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 920, Short.MAX_VALUE)
        );
        jLaberintoLayout.setVerticalGroup(
            jLaberintoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 540, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout JFondoLayout = new javax.swing.GroupLayout(JFondo);
        JFondo.setLayout(JFondoLayout);
        JFondoLayout.setHorizontalGroup(
            JFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JFondoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLaberinto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(228, 228, 228))
            .addGroup(JFondoLayout.createSequentialGroup()
                .addGap(385, 385, 385)
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        JFondoLayout.setVerticalGroup(
            JFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JFondoLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jLaberinto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnConfirmar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnConfirmar.setText("Ingresar");
        btnConfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmarActionPerformed(evt);
            }
        });

        jTFilas.setText("5");

        jLabel1.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel1.setText("Filas");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel2.setText("Columnas");

        jTColumnas.setText("5");

        completableCheckBox.setSelected(true);
        completableCheckBox.setText("solo completable");

        btnResolver.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnResolver.setText("resolver");
        btnResolver.setEnabled(false);
        btnResolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResolverActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnConfirmar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTFilas)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTColumnas)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(completableCheckBox)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btnResolver, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTFilas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jTColumnas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(completableCheckBox)
                .addGap(13, 13, 13)
                .addComponent(btnConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnResolver, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(JFondo, javax.swing.GroupLayout.PREFERRED_SIZE, 942, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(JFondo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmarActionPerformed
        crearLaberinto();
    }//GEN-LAST:event_btnConfirmarActionPerformed

    private void btnResolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResolverActionPerformed
        String algoritmo = (String) cmbAlgoritmo.getSelectedItem();

        switch (algoritmo) {
            case "BFS" -> resolverBFS();
            case "Topológico" -> resolverTopologico();
            case "A*" -> resolverEstrella();
        }

    }//GEN-LAST:event_btnResolverActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel JFondo;
    private javax.swing.JButton btnConfirmar;
    private javax.swing.JButton btnResolver;
    private javax.swing.JCheckBox completableCheckBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jLaberinto;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTColumnas;
    private javax.swing.JTextField jTFilas;
    // End of variables declaration//GEN-END:variables
}

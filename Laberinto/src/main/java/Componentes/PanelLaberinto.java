
package Componentes;

import Entidades.Casilla;
import Entidades.Personaje;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

/**
 * Panel del laberinto. Permite elegir la entrada haciendo clic sobre ella.
 *
 * @author Alex JGT
 */
public class PanelLaberinto extends JPanel {

    private Laberinto laberinto;
    private Personaje personaje;

    public PanelLaberinto(Laberinto laberinto) {
        this.laberinto = laberinto;

        // Clic sobre una casilla de entrada = entrada seleccionada.
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarEntrada(e.getX(), e.getY());
            }
        });
    }

    private void seleccionarEntrada(int px, int py) {
        int anchoBloque = laberinto.getAnchoBloque(getWidth());
        int altoBloque = laberinto.getAltoBloque(getHeight());
        if (anchoBloque <= 0 || altoBloque <= 0) {
            return;
        }

        int columna = px / anchoBloque;
        int fila = py / altoBloque;
        Casilla[][] matriz = laberinto.getLaberinto();
        if (fila < 0 || fila >= matriz.length || columna < 0 || columna >= matriz[0].length) {
            return;
        }

        Casilla casilla = matriz[fila][columna];
        if (laberinto.getEntradas().contains(casilla)) {
            laberinto.setInicioSeleccionado(casilla);
            laberinto.setCaminoSolucion(null); // limpia el camino anterior
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics grafico) {
        super.paintComponent(grafico);

        laberinto.paint(grafico, getWidth(), getHeight());

        // El personaje se dibuja en la entrada seleccionada (o en la primera por defecto).
        Casilla inicio = laberinto.getInicioSeleccionado() != null
                ? laberinto.getInicioSeleccionado() : laberinto.getCInicio();
        this.personaje = new Personaje(
                inicio.getX() * laberinto.getAnchoBloque(getWidth()),
                inicio.getY() * laberinto.getAltoBloque(getHeight()),
                laberinto.getAnchoBloque(getWidth()),
                laberinto.getAltoBloque(getHeight()));

        personaje.paint(grafico);
    }

}

package Componentes;

import Entidades.Personaje;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author Alex JGT 
 */
public class PanelLaberinto extends JPanel {
    
    private Laberinto laberinto;
    private Personaje personaje;
    
    public PanelLaberinto(Laberinto laberinto) {
        this.laberinto = laberinto;
    }
    
    protected void paintComponent(Graphics grafico) {
        super.paintComponent(grafico);
        
        laberinto.paint(grafico, getWidth(), getHeight());
        this.personaje = new Personaje(laberinto.getCInicio().getX() * laberinto.getAnchoBloque(getWidth()), laberinto.getCInicio().getY() *laberinto.getAltoBloque(getHeight()), laberinto.getAnchoBloque(getWidth()), laberinto.getAltoBloque(getHeight()));

        personaje.paint(grafico);
    }
    
}

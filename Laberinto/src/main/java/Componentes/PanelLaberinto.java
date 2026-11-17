
package Componentes;

import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author Ale JGT 
 */
public class PanelLaberinto extends JPanel {
    
    private Laberinto laberinto;
    private Personaje personaje;
    
    public PanelLaberinto(Laberinto laberinto, Personaje personaje) {
        this.laberinto = laberinto;
        this.personaje = personaje;
    }
    
    protected void paintComponent(Graphics grafico) {
        super.paintComponent(grafico);
        
        laberinto.paint(grafico, getWidth(), getHeight());
        personaje.paint(grafico);
    }
    
}

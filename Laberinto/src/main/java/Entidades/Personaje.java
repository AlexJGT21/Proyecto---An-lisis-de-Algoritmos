
package Entidades;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author AleX JGT
 */
public class Personaje {
    
    private int x;
    private int y;
    private int ancho;
    private int alto;

    public Personaje() {
        this.x = 40;
        this.y = 40;
        this.ancho = 40;
        this.alto = 40;
    }

    public Personaje(int x, int y, int ancho, int alto) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getAncho() {
        return ancho;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }

    public int getAlto() {
        return alto;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }
    
    
//    private final int movimiento = 40;
    
    public void paint(Graphics grafico) {
        grafico.setColor(Color.red);
        grafico.fillOval(x, y, ancho, alto);
        
        grafico.setColor(Color.BLACK);
        grafico.drawOval(x, y, ancho, alto);
    }
    
}


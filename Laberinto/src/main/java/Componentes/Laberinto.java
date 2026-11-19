
package Componentes;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

/**
 *
 * @author Ale JGT
 */
public class Laberinto {
    
    private int filas;
    private int columnas;
    private Casilla CInicio;
    private Casilla CFinal;
    
    private Casilla[][] matriz;
    
    private Random random = new Random();
    
    /**
     * Constrictor que crea un laberinto por defecto
     */
    public Laberinto(){
        this.filas = 13;
        this.columnas = 23;
        
        matriz = setearLaberinto();
    }
    
    /**
     * Constructor que crea un laberinto de forma dinamica
     * @param filas Número de filas por el usuario
     * @param columnas Número de columnas por el usuario
     */
    public Laberinto(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        
        matriz = setearLaberinto();
    }
    
    public void setDimensiones(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        
        matriz = setearLaberinto();
    }
    
    public int getAnchoBloque(int anchoPanel) {
        return (columnas > 0)? anchoPanel / columnas: 40;
    }
    
    public int getAltoBloque(int altoPanel) {
        return (columnas > 0)? altoPanel / filas: 40;
    }
    
    public Casilla[][] getLaberinto(){
        return matriz;
    }

    public Casilla getCInicio() {
        return CInicio;
    }

    public void setCInicio(Casilla CInicio) {
        this.CInicio = CInicio;
    }

    public Casilla getCFinal() {
        return CFinal;
    }

    public void setCFinal(Casilla CFinal) {
        this.CFinal = CFinal;
    }
    
    
    
    
    
    public void paint(Graphics grafico, int anchoPalen, int altoPanel) {
        Casilla[][] laberinto = matriz;
        
        if (filas <= 0 || columnas <= 0) {
            return;
        }
        
        int anchoBloque = anchoPalen / columnas;
        int altoBloque = altoPanel / filas;
        
        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                //pasillo
                if (laberinto[fila][columna].getRol() == 0) {
                    grafico.setColor(Color.WHITE);
                    grafico.fillRect(columna * anchoBloque, fila * altoBloque, anchoBloque, altoBloque);
                    
                    grafico.setColor(Color.WHITE);
                    grafico.drawRect(columna * anchoBloque, fila * altoBloque, anchoBloque, altoBloque);  
                    laberinto[fila][columna].setX(columna);
                    laberinto[fila][columna].setY(fila);
                }
                
                //color paredes
                if (laberinto[fila][columna].getRol() == 1) {
                    grafico.setColor(Color.BLACK);
                    grafico.fillRect(columna * anchoBloque, fila * altoBloque, anchoBloque, altoBloque);
                    
                    grafico.setColor(Color.black);
                    grafico.drawRect(columna * anchoBloque, fila * altoBloque, anchoBloque, altoBloque); 
//                    laberinto[fila][columna] = null;
                }
                
                //color entrada
                if (laberinto[fila][columna].getRol() == 2) {
                    grafico.setColor(Color.CYAN);
                    grafico.fillRect(columna * anchoBloque, fila * altoBloque, anchoBloque, altoBloque);
                    
                    grafico.setColor(Color.CYAN);
                    grafico.drawRect(columna * anchoBloque, fila * altoBloque, anchoBloque, altoBloque); 
                    
                    laberinto[fila][columna].setX(columna);
                    laberinto[fila][columna].setY(fila);
                    
                    CInicio = laberinto[fila][columna];
                }
                
                //color salida
                if (laberinto[fila][columna].getRol() == 3) {
                    grafico.setColor(Color.CYAN);
                    grafico.fillRect(columna * anchoBloque, fila * altoBloque, anchoBloque, altoBloque);
                    
                    grafico.setColor(Color.CYAN);
                    grafico.drawRect(columna * anchoBloque, fila * altoBloque, anchoBloque, altoBloque);    
                    
                    laberinto[fila][columna].setX(columna);
                    laberinto[fila][columna].setY(fila);
                    
                    CFinal = laberinto[fila][columna];
                }
            }            
        }
    }
    
    
    private Casilla[][] setearLaberinto() {
        
        //Obtenemos las filas y columnas y creamos un laberinto
        Casilla[][] laberinto = new Casilla[filas][columnas];
        
        //Pasamos a rellenar de manera aleatoria con 0 y 1
        //1 es un muro
        //0 es un camino
        
        for (int i = 0; i < laberinto.length; i++) {
            for (int j = 0; j < laberinto[i].length; j++) {
                laberinto[i][j] = new Casilla();
                laberinto[i][j].setRol(random.nextInt(2));
            }
        }
        
        laberinto = entradaSalida(laberinto);
        /*
        int[][] laberinto = {
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
                {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
                {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
                {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
                {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
                {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
                {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
                {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
                {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
                {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
                {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        };
        
        //Laberinto de prueba
        /*
        int[][] laberinto = {
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1},
                {1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1},
                {1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1},
                {1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1},
                {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1},
                {1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1},
                {1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1},
                {1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        };
        */
        return laberinto;
    }
    
    private Casilla[][] entradaSalida(Casilla[][] laberinto){
        int entrada = random.nextInt(filas);
        int salida= random.nextInt(filas);
        laberinto[entrada][0] = new Casilla();
        laberinto[salida][columnas-1] = new Casilla();
        laberinto[entrada][0].setRol(2);
        laberinto[salida][columnas-1].setRol(3);
        
        return laberinto;
    }
    
}

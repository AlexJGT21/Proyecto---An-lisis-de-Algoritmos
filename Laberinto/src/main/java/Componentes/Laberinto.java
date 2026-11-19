
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
        this.filas = 5;
        this.columnas = 5;
        
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
                //camino
                if (laberinto[fila][columna].getRol() == 0) {
                    grafico.setColor(Color.WHITE);
                    grafico.fillRect(columna * anchoBloque, fila * altoBloque, anchoBloque, altoBloque);
                    
                    grafico.setColor(Color.WHITE);
                    grafico.drawRect(columna * anchoBloque, fila * altoBloque, anchoBloque, altoBloque);  
                }
                
                //muro
                if (laberinto[fila][columna].getRol() == 1) {
                    grafico.setColor(Color.BLACK);
                    grafico.fillRect(columna * anchoBloque, fila * altoBloque, anchoBloque, altoBloque);
                    
                    grafico.setColor(Color.black);
                    grafico.drawRect(columna * anchoBloque, fila * altoBloque, anchoBloque, altoBloque); 
                }
                
                //entrada
                if (laberinto[fila][columna].getRol() == 2) {
                    grafico.setColor(Color.CYAN);
                    grafico.fillRect(columna * anchoBloque, fila * altoBloque, anchoBloque, altoBloque);
                    
                    grafico.setColor(Color.CYAN);
                    grafico.drawRect(columna * anchoBloque, fila * altoBloque, anchoBloque, altoBloque); 
                }
                
                //salida
                if (laberinto[fila][columna].getRol() == 3) {
                    grafico.setColor(Color.CYAN);
                    grafico.fillRect(columna * anchoBloque, fila * altoBloque, anchoBloque, altoBloque);
                    
                    grafico.setColor(Color.CYAN);
                    grafico.drawRect(columna * anchoBloque, fila * altoBloque, anchoBloque, altoBloque);    
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
                laberinto[i][j].setX(j);
                laberinto[i][j].setY(i);
            }
        }
        
        laberinto = SeteoEntradaSalida(laberinto);
        laberinto = seteoVecinos(laberinto);
        
        //solo para confirmar correcto seteo de aristas/vecinos
        imprimirLaberinto(laberinto);
        return laberinto;
    }
    
    private Casilla[][] SeteoEntradaSalida(Casilla[][] laberinto){
        int entrada = random.nextInt(filas);
        int salida= random.nextInt(filas);
        laberinto[entrada][0] = new Casilla();
        laberinto[salida][columnas - 1] = new Casilla();
        
        laberinto[entrada][0].setRol(2);
        laberinto[salida][columnas-1].setRol(3);
        
        laberinto[entrada][0].setX(0);
        laberinto[entrada][0].setY(entrada);
        CInicio = laberinto[entrada][0];
        
        laberinto[salida][columnas-1].setX(columnas-1);
        laberinto[salida][columnas-1].setY(salida);
        CFinal = laberinto[salida][columnas-1];        
        return laberinto;
    }
    
    private Casilla[][] seteoVecinos(Casilla[][] laberinto){
        //de arriba a abajo
        for (int i = 1; i < laberinto.length; i++) {
            for (int j = 0; j < laberinto[i].length; j++) {
                if(laberinto[i][j].getRol() != 1){
                    if( laberinto[i-1][j].getRol() != 1){
                        int costoArista = (random.nextInt(100))+1;
                        laberinto[i][j].setVecinoArriba(laberinto[i-1][j]);
                        laberinto[i][j].setCostoVecinoArriba(costoArista);
                        laberinto[i-1][j].setVecinoAbajo(laberinto[i][j]);
                        laberinto[i-1][j].setCostoVecinoAbajo(costoArista);
                    }
                }
            }
        }
        
        //de izquierda a derecha
        for (int i = 0; i < laberinto.length; i++) {
            for (int j = 1; j < laberinto[i].length; j++) {
                if(laberinto[i][j].getRol() != 1){
                    if( laberinto[i][j-1].getRol() != 1){
                        int costoArista = (random.nextInt(100))+1;
                        laberinto[i][j].setVecinoIzquierda(laberinto[i][j-1]);
                        laberinto[i][j].setCostoVecinoIzquierda(costoArista);
                        laberinto[i][j-1].setVecinoDerecha(laberinto[i][j]);
                        laberinto[i][j-1].setCostoVecinoDerecha(costoArista);
                    }
                }
            }
        }
        
        return laberinto;
    }
    
    //solo para confirmar correcto seteo de aristas/vecinos
    private void imprimirLaberinto(Casilla[][] laberinto){
        System.out.println("-----------------------------------------------------");
        for (int i = 0; i < laberinto.length; i++) {
            for (int j = 0; j < laberinto[i].length; j++) {
                System.out.println(laberinto[i][j].toString());
            }
        }
        System.out.println("-----------------------------------------------------");
    }
    
}

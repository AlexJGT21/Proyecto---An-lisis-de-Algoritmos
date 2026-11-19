/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Componentes;

/**
 *
 * @author le0jx
 */
public class Casilla {
    private int x, y;
    private int valor;
    private boolean visitado;
    private int rol;
    private Casilla vecinoArriba;
    private Casilla vecinoDerecha;
    private Casilla vecinoIzquierda;
    private Casilla vecinoAbajo;

    public Casilla() {
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
    
    

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public boolean isVisitado() {
        return visitado;
    }

    public void setVisitado(boolean visitado) {
        this.visitado = visitado;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }

    public Casilla getVecinoArriba() {
        return vecinoArriba;
    }

    public void setVecinoArriba(Casilla vecinoArriba) {
        this.vecinoArriba = vecinoArriba;
    }

    public Casilla getVecinoDerecha() {
        return vecinoDerecha;
    }

    public void setVecinoDerecha(Casilla vecinoDerecha) {
        this.vecinoDerecha = vecinoDerecha;
    }

    public Casilla getVecinoIzquierda() {
        return vecinoIzquierda;
    }

    public void setVecinoIzquierda(Casilla vecinoIzquierda) {
        this.vecinoIzquierda = vecinoIzquierda;
    }

    public Casilla getVecinoAbajo() {
        return vecinoAbajo;
    }

    public void setVecinoAbajo(Casilla vecinoAbajo) {
        this.vecinoAbajo = vecinoAbajo;
    }
    
}

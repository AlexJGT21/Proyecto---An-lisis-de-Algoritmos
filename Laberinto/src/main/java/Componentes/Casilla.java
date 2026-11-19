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
    private int x, y, valor, rol;
    private boolean visitado;
    private Casilla vecinoArriba, vecinoDerecha, vecinoIzquierda, vecinoAbajo;
    private int costoVecinoArriba, costoVecinoDerecha, costoVecinoIzquierda, costoVecinoAbajo;

    public Casilla() {
        this.valor = 0;
        this.visitado = false;
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

    public int getCostoVecinoArriba() {
        return costoVecinoArriba;
    }

    public void setCostoVecinoArriba(int costoVecinoArriba) {
        this.costoVecinoArriba = costoVecinoArriba;
    }

    public int getCostoVecinoDerecha() {
        return costoVecinoDerecha;
    }

    public void setCostoVecinoDerecha(int costoVecinoDerecha) {
        this.costoVecinoDerecha = costoVecinoDerecha;
    }

    public int getCostoVecinoIzquierda() {
        return costoVecinoIzquierda;
    }

    public void setCostoVecinoIzquierda(int costoVecinoIzquierda) {
        this.costoVecinoIzquierda = costoVecinoIzquierda;
    }

    public int getCostoVecinoAbajo() {
        return costoVecinoAbajo;
    }

    public void setCostoVecinoAbajo(int costoVecinoAbajo) {
        this.costoVecinoAbajo = costoVecinoAbajo;
    }

    @Override
    public String toString() {
        return "Casilla{" + "x=" + x + ", y=" + y + ", valor=" + valor + ", rol=" + rol + ", visitado=" + visitado + 
                ", costoVecinoArriba=" + costoVecinoArriba + ", costoVecinoDerecha=" + costoVecinoDerecha + ", costoVecinoIzquierda=" + costoVecinoIzquierda + ", costoVecinoAbajo=" + costoVecinoAbajo + '}';
    }
    
}

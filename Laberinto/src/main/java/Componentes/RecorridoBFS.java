
package Componentes;

import Entidades.Casilla;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 *
 * @author Alex JGT
 */
public class RecorridoBFS {
    
    private Laberinto laberinto;
    
    public RecorridoBFS(Laberinto laberinto) {
        this.laberinto = laberinto;
    }
    
    public boolean existeCaminoBFS() {
        //Cola para BFS
        Queue<Casilla> cola = new LinkedList<>();
        
        //Conjunto que registra los nodos visitados
        Set<Casilla> visitadas = new HashSet();
        
        
        //Obtenemos entradas y salidas
        Casilla inicio = laberinto.getCInicio();
        Casilla fin = laberinto.getCFinal();
        
        if (inicio == null || fin == null) {
            return false;
        }
        
        cola.add(inicio);
        visitadas.add(inicio);
        
        //Iniciamos recorrido
        while(!cola.isEmpty()) {
            
            Casilla actual = cola.poll();
            
            //Comprobamos si llegamos al fin
            if (actual == fin) {
                return true;
            }
            
            //Obtenemos todos los vecinos
            Casilla[] vecinos = {
                actual.getVecinoArriba(),
                actual.getVecinoDerecha(),
                actual.getVecinoAbajo(),
                actual.getVecinoIzquierda()
            };
            
            //Exploramos todos los vecinos
            for (Casilla vecino: vecinos) {                
                if (vecino != null && !visitadas.contains(vecino)) {
                    visitadas.add(vecino);
                    cola.add(vecino);                    
                }
            }
        }
        
        //No existe un camino posible.
        return false;        
    }
    
}


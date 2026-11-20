
package Componentes;

import Entidades.Casilla;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 *
 * @author Alex JGT
 */
public class RecorridoBFS {

    private final Laberinto laberinto;
    private List<Casilla> ordenVisitados = new ArrayList<>();
    private List<String> pasos = new ArrayList<>();

    public RecorridoBFS(Laberinto laberinto) {
        this.laberinto = laberinto;
    }

    public boolean existeCaminoBFS() {
        ordenVisitados = new ArrayList<>();
        pasos = new ArrayList<>();

        Queue<Casilla> cola = new LinkedList<>();
        Set<Casilla> visitadas = new HashSet<>();

        // Todas las entradas inician en la cola (BFS multi-origen).
        for (Casilla entrada : laberinto.getEntradas()) {
            cola.add(entrada);
            visitadas.add(entrada);
        }

        while (!cola.isEmpty()) {
            pasos.add("Cola: " + posiciones(cola));

            Casilla actual = cola.poll();
            ordenVisitados.add(actual);
            pasos.add("Visita: " + posicion(actual));

            if (laberinto.getSalidas().contains(actual)) {
                pasos.add("Salida alcanzada en " + posicion(actual));
                return true;
            }

            Casilla[] vecinos = {
                actual.getVecinoArriba(),
                actual.getVecinoDerecha(),
                actual.getVecinoAbajo(),
                actual.getVecinoIzquierda()
            };

            for (Casilla vecino : vecinos) {
                if (vecino != null && !visitadas.contains(vecino)) {
                    visitadas.add(vecino);
                    cola.add(vecino);
                }
            }
        }

        pasos.add("No existe camino entre ninguna entrada y salida.");
        return false;
    }

    public List<Casilla> getOrdenVisitados() {
        return ordenVisitados;
    }

    public List<String> getPasos() {
        return pasos;
    }

    private static String posiciones(Queue<Casilla> cola) {
        StringBuilder texto = new StringBuilder("[");
        int i = 0;
        for (Casilla c : cola) {
            if (i == 20) {
                texto.append(", ...");
                break;
            }
            if (i++ > 0) {
                texto.append(", ");
            }
            texto.append(posicion(c));
        }
        return texto.append("]").toString();
    }

    private static String posicion(Casilla c) {
        return "(" + (c.getY() + 1) + "," + (c.getX() + 1) + ")";
    }
}

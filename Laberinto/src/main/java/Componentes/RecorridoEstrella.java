/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Componentes;

import Entidades.Casilla;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Recorrido A* utilizando g(n) + h(n), con h = distancia Manhattan. */
public class RecorridoEstrella {

    private final Laberinto laberinto;
    private List<Casilla> camino = new ArrayList<>();
    private List<Casilla> ordenVisitados = new ArrayList<>();
    private List<String> pasos = new ArrayList<>();
    private int costoTotal = -1;

    public RecorridoEstrella(Laberinto laberinto) {
        this.laberinto = laberinto;
    }

    public boolean resolver(Casilla inicio) {
        camino = new ArrayList<>();
        ordenVisitados = new ArrayList<>();
        pasos = new ArrayList<>();
        costoTotal = -1;

        if (inicio == null || !laberinto.getEntradas().contains(inicio)) {
            return false;
        }

        List<Casilla> abiertos = new ArrayList<>();
        Set<Casilla> cerrados = new HashSet<>();
        Map<Casilla, Integer> distancia = new HashMap<>();
        Map<Casilla, Casilla> padre = new HashMap<>();

        distancia.put(inicio, 0);
        abiertos.add(inicio);

        while (!abiertos.isEmpty()) {
            pasos.add("Abiertos: " + posiciones(abiertos, distancia));

            // Extraer el nodo con el menor f = g + h.
            Casilla actual = abiertos.get(0);
            for (Casilla c : abiertos) {
                if (distancia.get(c) + heuristica(c)
                        < distancia.get(actual) + heuristica(actual)) {
                    actual = c;
                }
            }

            abiertos.remove(actual);
            cerrados.add(actual);
            ordenVisitados.add(actual);
            pasos.add("Visita " + posicion(actual)
                    + " g=" + distancia.get(actual)
                    + " h=" + heuristica(actual)
                    + " f=" + (distancia.get(actual) + heuristica(actual)));

            if (laberinto.getSalidas().contains(actual)) {
                costoTotal = distancia.get(actual);
                for (Casilla c = actual; c != null; c = padre.get(c)) {
                    camino.add(c);
                }
                Collections.reverse(camino);
                pasos.add("Salida " + posicion(actual) + ", costo=" + costoTotal);
                return true;
            }

            Casilla[] vecinos = {
                actual.getVecinoArriba(), actual.getVecinoDerecha(),
                actual.getVecinoAbajo(), actual.getVecinoIzquierda()
            };
            int[] pesos = {
                actual.getCostoVecinoArriba(), actual.getCostoVecinoDerecha(),
                actual.getCostoVecinoAbajo(), actual.getCostoVecinoIzquierda()
            };

            for (int i = 0; i < vecinos.length; i++) {
                Casilla vecino = vecinos[i];
                if (vecino == null || cerrados.contains(vecino)) {
                    continue;
                }
                if (pesos[i] < 0) {
                    throw new IllegalArgumentException("A* requiere pesos no negativos.");
                }
                int nuevoCosto = distancia.get(actual) + pesos[i];
                if (nuevoCosto < distancia.getOrDefault(vecino, Integer.MAX_VALUE)) {
                    distancia.put(vecino, nuevoCosto);
                    padre.put(vecino, actual);
                    if (!abiertos.contains(vecino)) {
                        abiertos.add(vecino);
                    }
                    pasos.add("Actualiza " + posicion(vecino)
                            + " g=" + nuevoCosto
                            + " h=" + heuristica(vecino)
                            + " f=" + (nuevoCosto + heuristica(vecino)));
                }
            }
        }

        pasos.add("No hay camino a ninguna salida.");
        return false;
    }

    private int heuristica(Casilla casilla) {
        int menor = Integer.MAX_VALUE;
        for (Casilla salida : laberinto.getSalidas()) {
            int distancia = Math.abs(casilla.getX() - salida.getX())
                    + Math.abs(casilla.getY() - salida.getY());
            menor = Math.min(menor, distancia);
        }
        return menor == Integer.MAX_VALUE ? 0 : menor;
    }

    private String posiciones(List<Casilla> abiertos, Map<Casilla, Integer> distancia) {
        String texto = "[";
        for (int i = 0; i < abiertos.size() && i < 20; i++) {
            Casilla c = abiertos.get(i);
            if (i > 0) {
                texto += ", ";
            }
            texto += posicion(c) + ":f=" + (distancia.get(c) + heuristica(c));
        }
        if (abiertos.size() > 20) {
            texto += ", ...";
        }
        return texto + "]";
    }

    private String posicion(Casilla c) {
        return "(" + (c.getY() + 1) + "," + (c.getX() + 1) + ")";
    }

    public List<Casilla> getCamino() {
        return camino;
    }

    public int getCostoTotal() {
        return costoTotal;
    }

    public List<Casilla> getOrdenVisitados() {
        return ordenVisitados;
    }

    public List<String> getPasos() {
        return pasos;
    }
}

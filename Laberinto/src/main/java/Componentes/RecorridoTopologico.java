/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Componentes;

import Entidades.Casilla;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class RecorridoTopologico {

    private final Laberinto laberinto;
    private List<Casilla> ordenVisitados = List.of();
    private List<String> pasos = List.of();

    public RecorridoTopologico(Laberinto laberinto) {
        this.laberinto = laberinto;
    }

    public boolean ordenar() {
        Map<Casilla, Integer> gradoEntrada = new HashMap<>();
        List<Casilla> vertices = new ArrayList<>();
        List<Casilla> orden = new ArrayList<>();
        List<String> historial = new ArrayList<>();

        // Obtener las casillas libres
        for (Casilla[] fila : laberinto.getLaberinto()) {
            for (Casilla casilla : fila) {
                if (casilla.getRol() != 1) {
                    vertices.add(casilla);
                    gradoEntrada.put(casilla, 0);
                }
            }
        }

        // Calcular los grados de entrada
        for (Casilla actual : vertices) {
            Casilla[] vecinos = {
                actual.getVecinoDerecha(),
                actual.getVecinoAbajo()
            };

            for (Casilla vecino : vecinos) {
                if (vecino != null) {
                    gradoEntrada.put(vecino,
                            gradoEntrada.get(vecino) + 1);
                }
            }
        }

        // Agregar los nodos con grado de entrada 0
        Queue<Casilla> cola = new ArrayDeque<>();

        for (Casilla casilla : vertices) {
            if (gradoEntrada.get(casilla) == 0) {
                cola.offer(casilla);
            }
        }

        // Realizar el recorrido topológico
        while (!cola.isEmpty()) {
            historial.add("Cola: " + posiciones(cola));

            Casilla actual = cola.poll();
            orden.add(actual);

            historial.add("Visita: " + posicion(actual));

            Casilla[] vecinos = {
                actual.getVecinoDerecha(),
                actual.getVecinoAbajo()
            };

            for (Casilla vecino : vecinos) {
                if (vecino != null) {
                    int grado = gradoEntrada.get(vecino) - 1;
                    gradoEntrada.put(vecino, grado);

                    if (grado == 0) {
                        cola.offer(vecino);
                    }
                }
            }
        }

        ordenVisitados = List.copyOf(orden);
        pasos = List.copyOf(historial);

        return orden.size() == vertices.size();
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

        for (Casilla casilla : cola) {
            if (i == 20) {
                texto.append(", ...");
                break;
            }
            if (i++ > 0) {
                texto.append(", ");
            }
            texto.append(posicion(casilla));
        }

        return texto.append("]").toString();
    }

    private static String posicion(Casilla casilla) {
        return "(" + (casilla.getY() + 1)
                + "," + (casilla.getX() + 1) + ")";
    }
}


package Componentes;

import Entidades.Casilla;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author AleX JGT
 */
public class Laberinto {

    private int filas;
    private int columnas;
    private Casilla CInicio;
    private Casilla CFinal;

    private final List<Casilla> entradas = new ArrayList<>();
    private final List<Casilla> salidas = new ArrayList<>();
    private Casilla inicioSeleccionado;
    private List<Casilla> caminoSolucion = new ArrayList<>();

    private Casilla[][] matriz;

    private Random random = new Random();

    // Activado: costos entre -100 y 100.
    // Desactivado: costos entre 1 y 100.
    private boolean permitirPesosNegativos = true;

    public void setPermitirPesosNegativos(boolean permitirPesosNegativos) {
        this.permitirPesosNegativos = permitirPesosNegativos;
    }

    private int generarCostoArista() {
        return permitirPesosNegativos
                ? random.nextInt(201) - 100
                : random.nextInt(100) + 1;
    }

    private int filaEntrada = -1; // -1 = entradas aleatorias

    /**
     * Constructor que crea un laberinto por defecto
     */
    public Laberinto() {
        this.filas = 5;
        this.columnas = 5;

        matriz = setearLaberinto();
    }

    /**
     * Constructor que crea un laberinto de forma dinamica
     *
     * @param filas Número de filas por el usuario
     * @param columnas Número de columnas por el usuario
     */
    public Laberinto(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;

        matriz = setearLaberinto();
    }

    public void setDimensiones(int filas, int columnas, int filaEntrada) {
        this.filaEntrada = filaEntrada;
        setDimensiones(filas, columnas);
    }

    public void setDimensiones(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;

        matriz = setearLaberinto();
    }

    public int getAnchoBloque(int anchoPanel) {
        return (columnas > 0) ? anchoPanel / columnas : 40;
    }

    public int getAltoBloque(int altoPanel) {
        return (filas > 0) ? altoPanel / filas : 40;
    }

    public Casilla[][] getLaberinto() {
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

    public List<Casilla> getEntradas() {
        return entradas;
    }

    public List<Casilla> getSalidas() {
        return salidas;
    }

    public Casilla getInicioSeleccionado() {
        return inicioSeleccionado;
    }

    public void setInicioSeleccionado(Casilla inicioSeleccionado) {
        this.inicioSeleccionado = inicioSeleccionado;
    }

    public List<Casilla> getCaminoSolucion() {
        return caminoSolucion;
    }

    public void setCaminoSolucion(List<Casilla> caminoSolucion) {
        this.caminoSolucion = (caminoSolucion == null)
                ? new ArrayList<>()
                : new ArrayList<>(caminoSolucion);
    }

    public void paint(Graphics grafico, int anchoPanel, int altoPanel) {
        Casilla[][] laberinto = matriz;

        if (filas <= 0 || columnas <= 0) {
            return;
        }

        int anchoBloque = anchoPanel / columnas;
        int altoBloque = altoPanel / filas;

        Set<Casilla> enCamino = new HashSet<>(caminoSolucion);

        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                Casilla casilla = laberinto[fila][columna];

                int x = columna * anchoBloque;
                int y = fila * altoBloque;

                switch (casilla.getRol()) {
                    case 1 -> grafico.setColor(Color.BLACK);
                    case 2 -> grafico.setColor(Color.CYAN);
                    case 3 -> grafico.setColor(Color.ORANGE);
                    default -> grafico.setColor(Color.WHITE);
                }

                if (enCamino.contains(casilla) && casilla.getRol() == 0) {
                    grafico.setColor(Color.GREEN);
                }

                grafico.fillRect(x, y, anchoBloque, altoBloque);

                grafico.setColor(Color.LIGHT_GRAY);
                grafico.drawRect(x, y, anchoBloque, altoBloque);

                if (casilla == inicioSeleccionado) {
                    grafico.setColor(Color.BLUE);
                    grafico.drawRect(
                            x + 1, y + 1,
                            anchoBloque - 2, altoBloque - 2
                    );
                }
            }
        }

        if (caminoSolucion.size() > 1) {
            java.awt.Graphics2D g2 = (java.awt.Graphics2D) grafico;
            java.awt.Stroke anterior = g2.getStroke();

            g2.setColor(new Color(0, 150, 0));
            g2.setStroke(new java.awt.BasicStroke(
                    Math.max(2, Math.min(anchoBloque, altoBloque) / 4)
            ));

            for (int i = 0; i < caminoSolucion.size() - 1; i++) {
                Casilla a = caminoSolucion.get(i);
                Casilla b = caminoSolucion.get(i + 1);

                g2.drawLine(
                        a.getX() * anchoBloque + anchoBloque / 2,
                        a.getY() * altoBloque + altoBloque / 2,
                        b.getX() * anchoBloque + anchoBloque / 2,
                        b.getY() * altoBloque + altoBloque / 2
                );
            }

            g2.setStroke(anterior);
        }

        // Dibuja el costo de cada conexión una sola vez.
        dibujarPonderaciones(grafico, anchoBloque, altoBloque);
    }

    /**
     * Muestra las ponderaciones sobre las aristas entre casillas.
     * Los pesos negativos se dibujan en rojo y los demás en azul.
     */
    private void dibujarPonderaciones(
            Graphics grafico, int anchoBloque, int altoBloque) {

        // No colocar números cuando las celdas son muy pequeñas.
        if (anchoBloque < 35 || altoBloque < 35) {
            return;
        }

        java.awt.Graphics2D g2 =
                (java.awt.Graphics2D) grafico.create();
        g2.setFont(new Font("Arial", Font.BOLD, 13));

        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                Casilla casilla = matriz[fila][columna];

                if (casilla.getRol() == 1) {
                    continue;
                }

                // Solo derecha y abajo: no repite aristas bidireccionales.
                if (casilla.getVecinoDerecha() != null) {
                    int x = (columna + 1) * anchoBloque;
                    int y = fila * altoBloque + altoBloque / 2;
                    dibujarCosto(g2, x, y,
                            casilla.getCostoVecinoDerecha());
                }

                if (casilla.getVecinoAbajo() != null) {
                    int x = columna * anchoBloque + anchoBloque / 2;
                    int y = (fila + 1) * altoBloque;
                    dibujarCosto(g2, x, y,
                            casilla.getCostoVecinoAbajo());
                }
            }
        }
        g2.dispose();
    }

    /** Dibuja una etiqueta legible sobre una arista. */
    private void dibujarCosto(java.awt.Graphics2D g2,
            int x, int y, int costo) {

        String texto = String.valueOf(costo);
        java.awt.FontMetrics fm = g2.getFontMetrics();
        int ancho = fm.stringWidth(texto);
        int alto = fm.getHeight();

        g2.setColor(new Color(255, 255, 255, 230));
        g2.fillRoundRect(x - ancho / 2 - 4, y - alto / 2,
                ancho + 8, alto, 8, 8);

        g2.setColor(costo < 0
                ? Color.RED : new Color(30, 70, 130));
        g2.drawString(texto, x - ancho / 2,
                y + (fm.getAscent() - fm.getDescent()) / 2);
    }

    private Casilla[][] setearLaberinto() {

        Casilla[][] laberinto = new Casilla[filas][columnas];

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

        return laberinto;
    }

    /**
     * Crea al menos una entrada en la primera columna
     * y al menos una salida en la última columna.
     */
    private Casilla[][] SeteoEntradaSalida(Casilla[][] laberinto) {
        entradas.clear();
        salidas.clear();

        inicioSeleccionado = null;
        caminoSolucion = new ArrayList<>();

        if (filaEntrada >= 0 && filaEntrada < filas) {
            Casilla elegida = laberinto[filaEntrada][0];
            elegida.setRol(2);
            entradas.add(elegida);
        }

        marcarExtremo(laberinto, 0, 2, entradas);
        marcarExtremo(laberinto, columnas - 1, 3, salidas);

        CInicio = entradas.get(0);
        CFinal = salidas.get(0);

        return laberinto;
    }

    private void marcarExtremo(
            Casilla[][] laberinto,
            int columna,
            int rol,
            List<Casilla> destino) {

        int cantidad = 1 + random.nextInt(
                Math.max(1, Math.min(3, filas))
        );

        while (destino.size() < cantidad) {
            Casilla c = laberinto[random.nextInt(filas)][columna];

            if (c.getRol() != rol) {
                c.setRol(rol);
                destino.add(c);
            }
        }

        destino.sort(
                (a, b) -> Integer.compare(a.getY(), b.getY())
        );
    }

    /**
     * Abre un pasillo desde la primera entrada hasta la
     * primera salida y regenera las aristas.
     */
    public void garantizarCamino() {
        Casilla entrada = entradas.get(0);
        Casilla salida = salidas.get(0);

        int fila = entrada.getY();
        int columna = entrada.getX();

        while (columna < columnas - 1) {
            columna++;
            abrir(matriz[fila][columna]);
        }

        while (fila != salida.getY()) {
            fila += (salida.getY() > fila) ? 1 : -1;
            abrir(matriz[fila][columna]);
        }

        for (Casilla[] f : matriz) {
            for (Casilla c : f) {
                c.setVecinoArriba(null);
                c.setVecinoAbajo(null);
                c.setVecinoDerecha(null);
                c.setVecinoIzquierda(null);
            }
        }

        seteoVecinos(matriz);
    }

    private void abrir(Casilla c) {
        if (c.getRol() == 1) {
            c.setRol(0);
        }
    }

    private Casilla[][] seteoVecinos(Casilla[][] laberinto) {

        // Conexiones verticales
        for (int i = 1; i < laberinto.length; i++) {
            for (int j = 0; j < laberinto[i].length; j++) {

                if (laberinto[i][j].getRol() != 1) {
                    if (laberinto[i - 1][j].getRol() != 1) {

                        int costoArista = generarCostoArista();

                        laberinto[i][j].setVecinoArriba(
                                laberinto[i - 1][j]
                        );
                        laberinto[i][j].setCostoVecinoArriba(costoArista);

                        laberinto[i - 1][j].setVecinoAbajo(
                                laberinto[i][j]
                        );
                        laberinto[i - 1][j].setCostoVecinoAbajo(costoArista);
                    }
                }
            }
        }

        // Conexiones horizontales
        for (int i = 0; i < laberinto.length; i++) {
            for (int j = 1; j < laberinto[i].length; j++) {

                if (laberinto[i][j].getRol() != 1) {
                    if (laberinto[i][j - 1].getRol() != 1) {

                        int costoArista = generarCostoArista();

                        laberinto[i][j].setVecinoIzquierda(
                                laberinto[i][j - 1]
                        );
                        laberinto[i][j].setCostoVecinoIzquierda(costoArista);

                        laberinto[i][j - 1].setVecinoDerecha(
                                laberinto[i][j]
                        );
                        laberinto[i][j - 1].setCostoVecinoDerecha(costoArista);
                    }
                }
            }
        }

        return laberinto;
    }

    private void imprimirLaberinto(Casilla[][] laberinto) {
        System.out.println("-----------------------------------------------------");

        for (int i = 0; i < laberinto.length; i++) {
            for (int j = 0; j < laberinto[i].length; j++) {
                System.out.println(laberinto[i][j].toString());
            }
        }

        System.out.println("-----------------------------------------------------");
    }
}

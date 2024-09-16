
package agentes;

import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author macario
 */
public class Agente extends Thread
{
    private final String nombre;
    private int i;
    private int j;
    private final ImageIcon icon;
    private final int[][] matrix;
    private final JLabel tablero[][];
    
    
    private JLabel casillaAnterior;
    Random aleatorio = new Random(System.currentTimeMillis());
    
    public Agente(String nombre, ImageIcon icon, int[][] matrix, JLabel tablero[][])
    {
        this.nombre = nombre;
        this.icon = icon;
        this.matrix = matrix;
        this.tablero = tablero;

        
        this.i = aleatorio.nextInt(matrix.length);
        this.j = aleatorio.nextInt(matrix.length);
        tablero[i][j].setIcon(icon);        
    }
    
    @Override
    public void run()
    {
        while(true)
        {
            movRegalo();
            caminarRand();   
            try
            {
                sleep(100+aleatorio.nextInt(100));
            }
            catch (InterruptedException ex)
            {
                ex.printStackTrace(System.out);
            }
        }

                      
    }
    
    public synchronized void actualizarPosicion()
    {
        casillaAnterior.setIcon(null); // Elimina su figura de la casilla anterior
        tablero[i][j].setIcon(icon); // Pone su figura en la nueva casilla
        System.out.println(nombre + " in -> Row: " + i + " Col:"+ j);              
    }
    
    public int numrand(){
        int num = aleatorio.nextInt(4) + 1;
        return num;
    }
    
    public void caminarRand() {
        int dirRow = 0; // Mueve de arriba abajo
        int dirCol = 0; // Mueve de izquierda a derecha

        casillaAnterior = tablero[i][j];

        // Verifica si hay obstáculos alrededor
        boolean obstaculo = false;
        if ((i > 0 && matrix[i - 1][j] == 5) || 
            (i < matrix.length - 1 && matrix[i + 1][j] == 5) || 
            (j > 0 && matrix[i][j - 1] == 5) || 
            (j < matrix[0].length - 1 && matrix[i][j + 1] == 5)) {
            obstaculo = true;
        }

        // Si hay obstáculo, intenta esquivarlo
        if (obstaculo) {
            movostaculo();
        } else {
            // Si no hay obstáculos, se mueve aleatoriamente
            int numdir = numrand();
            switch (numdir) {
                case 1: // Arriba
                    dirRow = -1;
                    break;
                case 2: // Abajo
                    dirRow = 1;
                    break;
                case 3: // Derecha
                    dirCol = 1;
                    break;
                case 4: // Izquierda
                    dirCol = -1;
                    break;
            }

            // Calcula la nueva posición
            int nuevaI = i + dirRow;
            int nuevaJ = j + dirCol;

            // Verifica si la nueva posición está dentro de los límites y no es un obstáculo
            if (nuevaI >= 0 && nuevaI < matrix.length && 
                nuevaJ >= 0 && nuevaJ < matrix[0].length && 
                matrix[nuevaI][nuevaJ] != 5) {
                i = nuevaI;
                j = nuevaJ;
                actualizarPosicion();
            }
        }
    }

    public void movostaculo() {
        int[] posiblesMovimientos = new int[4];
        int movimientosValidos = 0;

        // Verifica si las posiciones adyacentes son válidas (dentro de los límites y sin obstáculo)
        if (i > 0 && matrix[i - 1][j] != 5) { // Arriba
            posiblesMovimientos[movimientosValidos++] = 1;
        }
        if (i < matrix.length - 1 && matrix[i + 1][j] != 5) { // Abajo
            posiblesMovimientos[movimientosValidos++] = 2;
        }
        if (j > 0 && matrix[i][j - 1] != 5) { // Izquierda
            posiblesMovimientos[movimientosValidos++] = 3;
        }
        if (j < matrix[0].length - 1 && matrix[i][j + 1] != 5) { // Derecha
            posiblesMovimientos[movimientosValidos++] = 4;
        }

        // Si hay movimientos válidos, elige uno aleatoriamente
        if (movimientosValidos > 0) {
            int eleccion = posiblesMovimientos[aleatorio.nextInt(movimientosValidos)];
            int dirRow = 0;
            int dirCol = 0;

            switch (eleccion) {
                case 1: // Arriba
                    dirRow = -1;
                    break;
                case 2: // Abajo
                    dirRow = 1;
                    break;
                case 3: // Izquierda
                    dirCol = -1;
                    break;
                case 4: // Derecha
                    dirCol = 1;
                    break;
            }

            // Calcula la nueva posición
            int nuevaI = i + dirRow;
            int nuevaJ = j + dirCol;

            // Actualiza la posición del agente
            i = nuevaI;
            j = nuevaJ;
            actualizarPosicion();
        }

    }
    public void movRegalo() {
        int dirRow = 0;  // Dirección en filas
        int dirCol = 0;  // Dirección en columnas

        // Verifica si hay una muestra en las posiciones adyacentes
        if (i > 0 && matrix[i - 1][j] == 6) { // Muestra arriba
            dirRow = -1;
        } else if (i < matrix.length - 1 && matrix[i + 1][j] == 6) { // Muestra abajo
            dirRow = 1;
        } else if (j > 0 && matrix[i][j - 1] == 6) { // Muestra a la izquierda
            dirCol = -1;
        } else if (j < matrix[0].length - 1 && matrix[i][j + 1] == 6) { // Muestra a la derecha
            dirCol = 1;
        }

        // Si encontró una muestra, se mueve hacia ella
        if (dirRow != 0 || dirCol != 0) {
            int nuevaI = i + dirRow;
            int nuevaJ = j + dirCol;

            // Verifica si la nueva posición no es un obstáculo (valor 5 en la matriz)
            if (matrix[nuevaI][nuevaJ] != 5) {
                // Limpia la casilla anterior (elimina la imagen del agente)
                tablero[i][j].setIcon(null);

                // Actualiza la posición del agente
                i = nuevaI;
                j = nuevaJ;

                // Actualiza la matriz para indicar que la muestra fue recogida
                matrix[nuevaI][nuevaJ] = 0;

                // Actualiza la posición del agente en la interfaz gráfica
                actualizarPosicion();
            }
        }
    }
}

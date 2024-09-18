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
    private final ImageIcon cargado;
    private final int[][] matrix;
    private final JLabel tablero[][];
    private boolean muestraObtenida = false;
    private boolean muestrasSI = false;
    private int direccion;
    private int bandera = 0;
    private int cordX;
    private int cordY;
    private int min;
    private double[] distancia = new double[4];
    
    private JLabel casillaAnterior;
    Random aleatorio = new Random(System.currentTimeMillis());
    Random rand = new Random();
    
    public Agente(String nombre, ImageIcon icon, ImageIcon cargado, int[][] matrix, JLabel tablero[][])
    {
        this.nombre = nombre;
        this.icon = icon;
        this.cargado = cargado;
        this.matrix = matrix;
        this.tablero = tablero;

        this.i = aleatorio.nextInt(matrix.length);
        this.j = aleatorio.nextInt(matrix.length);
        tablero[i][j].setIcon(icon); 
        
        
    }
    
    @Override
    public void run()
    {
        findMother();
        muestrasSI = sinMuestras();

        while(muestrasSI == true)
        {

            casillaAnterior = tablero[i][j];
            
            if(muestraObtenida != true){ //Si se ha obtenido una muestra, el metodo para mover al agente sera agenteCargado
                mov();
            }else{
                agenteCargado();
            }
                    
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
        if(matrix[i][j] == 1){ //Si en la casilla que se actualizara el agente hay una muestra (indice 1 para muestras) se cambiara el valor del booleano muestraObtenida a true
            muestraObtenida = true;
            tablero[i][j].setIcon(null);
            matrix[i][j] = 0;
        }
        if(muestraObtenida != true){
            casillaAnterior.setIcon(null); // Elimina su figura de la casilla anterior
            tablero[i][j].setIcon(icon); // Pone su figura en la nueva casilla
            muestrasSI = sinMuestras();
        }else{
            casillaAnterior.setIcon(null); // Elimina su figura de la casilla anterior
            tablero[i][j].setIcon(cargado); // Pone su figura en la nueva casilla
        }

        System.out.println(nombre + " in -> Row: " + i + " Col:"+ j);   
        bandera = 0;
        
    }
    
    public void agenteCargado(){
        distancia [0]= Math.sqrt((Math.pow(cordX-j,2)+Math.pow(cordY-(i+1),2)));
        distancia [1]= Math.sqrt((Math.pow(cordX-j,2)+Math.pow(cordY-(i-1),2)));
        distancia [2]= Math.sqrt((Math.pow(cordX-(j+1),2)+Math.pow(cordY-i,2)));
        distancia [3]= Math.sqrt((Math.pow(cordX-(j-1),2)+Math.pow(cordY-i,2)));
        
        min = 0;
        for (int i = 0; i < 4; i++) {
            if (distancia[i] < distancia[min]) {
                min = i; // Si encontramos un elemento menor, actualizamos la posición del mínimo
            }
        }
        movCargado();
        actualizarPosicion();
    }
    
    public void movCargado(){
        switch(min){
            case 0:
                if (i + 1 < matrix.length) {
                    if (matrix[i+1][j] == 2 || matrix[i+1][j] == 1){
                        while(bandera != 1){
                            moverAleatorioCargado();
                        }
                        break;
                    }
                    if(matrix[i+1][j] == 3){
                        dejarMuestra();
                        break;
                    }
                    i++;
                }
                break;
            case 1:
                if (i - 1 >= 0) {
                    if (matrix[i-1][j] == 2 || matrix[i-1][j] == 1){
                        while(bandera != 1){
                            moverAleatorioCargado();
                        }
                        break;
                    }
                    if(matrix[i-1][j] == 3){
                        dejarMuestra();
                        break;
                    }
                    i--;
                }
                break;
            case 2:
                if (j + 1 < matrix[0].length) {
                    if (matrix[i][j+1] == 2 || matrix[i][j+1] == 1){
                        while(bandera != 1){
                            moverAleatorioCargado();
                        }
                        break;
                    }
                    if(matrix[i][j+1] == 3){
                        dejarMuestra();
                        break;
                    }
                    j++;
                }
                break;
            case 3:
                if (j - 1 >= 0) {
                    if (matrix[i][j-1] == 2 || matrix[i][j-1] == 1){
                        while(bandera != 1){
                            moverAleatorioCargado();
                        }
                        break;
                    }
                    if(matrix[i][j-1] == 3){
                        dejarMuestra();
                        break;
                    }
                    j--;
                }
                break;
        }
    }
    
    public void moverAleatorioCargado(){
        direccion = rand.nextInt(4);
        
        switch (direccion) {
            case 0:
                if (i + 1 < matrix.length) {
                    if (matrix[i+1][j] == 2 || matrix[i+1][j] == 1){
                        break;
                    }
                    i++;
                    bandera =1;
                }
                break;
            case 1:
                if (i - 1 >= 0) {
                    if (matrix[i-1][j] == 2 || matrix[i-1][j] == 1){
                        break;
                    }
                    i--;
                    bandera =1;
                }
                break;
            case 2:
                if (j + 1 < matrix[0].length) {
                    if (matrix[i][j+1] == 2 || matrix[i][j+1] == 1){
                        break;
                    }
                    j++;
                    bandera =1;
                }
                break;
            case 3:
                if (j - 1 >= 0) {
                    if (matrix[i][j-1] == 2 || matrix[i][j-1] == 1){
                        break;
                    }
                    j--;
                    bandera =1;
                }
                break;
        }
    }
    
    public void movAleatorio(){ //Se usa un numero aleatorio y evalua si no esta fuera de rango, luego evalua si en la casilla seleccionada no hay un obstaculo o la nave nodriza
        direccion = rand.nextInt(4);
        
        switch(direccion){
            case 0:
                if(i-1 >= 0){
                    if(obstaculoCasilla(i-1, j) == true || matrix[i-1][j] == 3){
                        break;
                    }else{
                        i--;
                        bandera =1;
                    }
                }
                break;
            case 1:
                if(i+1 < matrix.length){
                    if(obstaculoCasilla(i+1, j) == true || matrix[i+1][j] == 3){
                        break;
                    }else{
                        i++;
                        bandera =1;
                    }
                }
                break;
            case 2:
                if(j+1 < matrix.length){
                    if(obstaculoCasilla(i, j+1) == true || matrix[i][j+1] == 3){
                        break;
                    }else{
                        j++;
                        bandera =1;
                    }
                }
                break;
            case 3:
                if(j-1 >= 0){
                    if(obstaculoCasilla(i, j-1) == true || matrix[i][j-1] == 3){
                        break;
                    }else{
                        j--;
                        bandera =1;
                    }
                }
                break;
        }
    }
    
    public void muestraCasilla(int fil, int col){ //Metodo para evaluar si en la casilla [fil][col] hay una muestra y actualizar indices i, j
         if(matrix[fil][col] == 1){ //Si en la casilla hay una muestra
             bandera = 1;
             i = fil;
             j = col;
        }
    }
    
    public boolean obstaculoCasilla(int fil, int col){ //metodo para evaluar si en la casilla [fil][col] hay un obstaculo
        if(matrix[fil][col] == 2){ //Si en la casilla hay un obstaculo
             return true;
        }else{
            return false;
        }
    }

    public void findMother(){//busca en que coordenadas esta la base
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (matrix[i][j] == 3) {
                    cordY = i;
                    cordX = j;
                    break;
                }
            }
        }
    }
    
    public void mov(){ //Metodo principal de movimiento, primero evalua si en las casillas alrededor hay una muestra, si la hay, el agente se movera directamente a esa casilla 
        if(i-1 >= 0){
            muestraCasilla(i-1, j);
        }
        if(i+1 < matrix.length && bandera != 1){
            muestraCasilla(i+1, j);
        }
        if(j+1 < matrix.length && bandera != 1){
            muestraCasilla(i, j+1);
        }
        if(j-1 >= 0 && bandera != 1){
            muestraCasilla(i, j-1);
        }
        
        while(bandera != 1){ //Si no hay muestras se movera aleatoriamente llamando al metodo movAleatorio
            movAleatorio();
        }
        actualizarPosicion(); //Se actualiza la posicion del agente
    } 
    
    public void dejarMuestra(){
        muestraObtenida = false;
        tablero[i][j].setIcon(icon); // Pone su figura en la nueva casilla

    }
    
    public boolean sinMuestras(){
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (matrix[i][j] == 1) {
                    return true;
                    
                }
            }
        }
        return false;
    }
    
}


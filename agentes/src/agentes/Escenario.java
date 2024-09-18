package agentes;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;

/**
 * Clase Escenario que extiende de JFrame. Esta clase representa el escenario en el que los agentes 
 * se moverán. El escenario es una cuadrícula visual de 15x15 con íconos, y permite agregar obstáculos, 
 * muestras y la nave nodriza en diferentes celdas de la cuadrícula.
 */
public class Escenario extends JFrame
{
    // Matriz visual de etiquetas para representar el tablero
    private JLabel[][] tablero;     
    // Matriz lógica que representa los diferentes estados del tablero
    private int[][] matrix;
    // Dimensión del tablero (15x15)
    private final int dim = 15;

    // Íconos para representar los diferentes objetos y agentes
    private ImageIcon gisantes;
    private ImageIcon papa;
    private ImageIcon Cargado; 
    private ImageIcon obstacleIcon;
    private ImageIcon sampleIcon;
    private ImageIcon actualIcon;
    private ImageIcon motherIcon;
    
    // Agentes que se moverán en el tablero
    private Agente agente1;
    private Agente agente2;
   
    // Panel de fondo con una imagen personalizada
    private final BackGroundPanel fondo = new BackGroundPanel(new ImageIcon("imagenes/scene.png"));
    
    // Menú para configurar el escenario
    private final JMenu settings = new JMenu("Settings");
    private final JRadioButtonMenuItem obstacle = new JRadioButtonMenuItem("Obstacle");
    private final JRadioButtonMenuItem sample = new JRadioButtonMenuItem("Sample");
    private final JRadioButtonMenuItem motherShip = new JRadioButtonMenuItem("MotherShip");
    
    /**
     * Constructor de la clase Escenario.
     * Configura el marco principal y llama a la inicialización de componentes.
     */
    public Escenario()
    {
        // Configura el fondo y las propiedades de la ventana
        this.setContentPane(fondo);
        this.setTitle("Agentes");
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setBounds(50,50,dim*50+35,dim*50+85);
        initComponents();
    }
        
    /**
     * Método que inicializa los componentes del escenario, como el menú y el tablero.
     */
    private void initComponents()
    {
        // Agrupa las opciones del menú de configuración
        ButtonGroup settingsOptions = new ButtonGroup();
        settingsOptions.add(sample);
        settingsOptions.add(obstacle);       
        settingsOptions.add(motherShip);
        
        // Barra de menús con opciones de archivo y configuración
        JMenuBar barraMenus = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem run  = new JMenuItem("Run");
        JMenuItem exit   = new JMenuItem("Exit");
              
        this.setJMenuBar(barraMenus);
        barraMenus.add(file);
        barraMenus.add(settings);
        file.add(run);
        file.add(exit);
        settings.add(motherShip);
        settings.add(obstacle);
        settings.add(sample);
            
        // Carga los íconos de las imágenes y los redimensiona
        gisantes = new ImageIcon("imagenes/gisantes.png");
        gisantes = new ImageIcon(gisantes.getImage().getScaledInstance(50,50,  java.awt.Image.SCALE_SMOOTH));
        
        papa = new ImageIcon("imagenes/papa.png");
        papa = new ImageIcon(papa.getImage().getScaledInstance(50,50,  java.awt.Image.SCALE_SMOOTH));
        
        obstacleIcon = new ImageIcon("imagenes/zombie.png");
        obstacleIcon = new ImageIcon(obstacleIcon.getImage().getScaledInstance(50,50,  java.awt.Image.SCALE_SMOOTH));
        
        sampleIcon = new ImageIcon("imagenes/sol.png");
        sampleIcon = new ImageIcon(sampleIcon.getImage().getScaledInstance(30,30,  java.awt.Image.SCALE_SMOOTH));
        
        motherIcon = new ImageIcon("imagenes/girasol.png");
        motherIcon = new ImageIcon(motherIcon.getImage().getScaledInstance(50,50,  java.awt.Image.SCALE_SMOOTH));
        
        Cargado = new ImageIcon("imagenes/sol.png");
        Cargado = new ImageIcon(Cargado.getImage().getScaledInstance(50,50,  java.awt.Image.SCALE_SMOOTH));
        
        // Establece un diseño nulo y forma el plano del tablero
        this.setLayout(null);
        formaPlano();  
        
        // Asigna eventos a las acciones de salida, ejecución y selección de objetos
        exit.addActionListener(evt -> gestionaSalir(evt));
        run.addActionListener(evt -> gestionaRun(evt));
        obstacle.addItemListener(evt -> gestionaObstacle(evt));
        sample.addItemListener(evt -> gestionaSample(evt));
        motherShip.addItemListener(evt -> gestionaMotherShip(evt));

        // Crea un listener personalizado para manejar el cierre de la ventana
        class MyWindowAdapter extends WindowAdapter
        {
            @Override
            public void windowClosing(WindowEvent eventObject)
            {
                goodBye();
            }
        }
        addWindowListener(new MyWindowAdapter());
        
        // Crea dos agentes para el escenario
        agente1 = new Agente("agente1",gisantes, Cargado, matrix, tablero); 
        agente2 = new Agente("agente2",papa, Cargado, matrix, tablero);         
    }
        
    /**
     * Gestiona el evento de salida del programa.
     */
    private void gestionaSalir(ActionEvent eventObject)
    {
        goodBye();
    }
        
    /**
     * Muestra un cuadro de diálogo para confirmar la salida del programa.
     */
    private void goodBye()
    {
        int respuesta = JOptionPane.showConfirmDialog(rootPane, "Desea salir?","Aviso",JOptionPane.YES_NO_OPTION);
        if(respuesta==JOptionPane.YES_OPTION) System.exit(0);
    }
  
    /**
     * Forma el tablero del escenario creando las celdas visuales y la matriz lógica.
     */
    private void formaPlano()
    {
        tablero = new JLabel[dim][dim];
        matrix = new int[dim][dim];
        
        int i, j;
        
        // Inicializa la matriz y las celdas gráficas
        for(i=0;i<dim;i++)
            for(j=0;j<dim;j++)
            {
                matrix[i][j]=0;
                tablero[i][j]=new JLabel();
                tablero[i][j].setBounds(j*50+10,i*50+10,50,50);
                tablero[i][j].setBorder(BorderFactory.createDashedBorder(Color.white));
                tablero[i][j].setOpaque(false);
                this.add(tablero[i][j]);
                
                // Añade un MouseListener para insertar objetos en las celdas
                tablero[i][j].addMouseListener(new MouseAdapter() 
                {
                    @Override
                    public void mousePressed(MouseEvent e) 
                    {
                        insertaObjeto(e);
                    }   
                
                    @Override
                    public void mouseReleased(MouseEvent e) 
                    {
                        insertaObjeto(e);
                    }   
                });
            }
    }
        
    /**
     * Gestiona la selección de la opción 'Obstacle' en el menú.
     */
    private void gestionaObstacle(ItemEvent eventObject)
    {
        JRadioButtonMenuItem opt = (JRadioButtonMenuItem) eventObject.getSource();
        if(opt.isSelected())
           actualIcon = obstacleIcon;
        else actualIcon = null;        
    }
    
    /**
     * Gestiona la selección de la opción 'Sample' en el menú.
     */
    private void gestionaSample(ItemEvent eventObject)
    {
        JRadioButtonMenuItem opt = (JRadioButtonMenuItem) eventObject.getSource();
        if(opt.isSelected())
           actualIcon = sampleIcon;
        else actualIcon = null;   
    }
    
    /**
     * Gestiona la selección de la opción 'MotherShip' en el menú.
     */
    private void gestionaMotherShip(ItemEvent eventObject)
    {
        JRadioButtonMenuItem opt = (JRadioButtonMenuItem) eventObject.getSource();
        if(opt.isSelected())
           actualIcon = motherIcon;
        else actualIcon = null;   
    }

    /**
     * Inicia el movimiento de los agentes cuando se selecciona 'Run' en el menú.
     */
    private void gestionaRun(ActionEvent eventObject)
    {
        if(!agente1.isAlive()) agente1.start();
        if(!agente2.isAlive()) agente2.start();
        settings.setEnabled(false);
    }
       
    /**
     * Inserta un objeto (obstáculo, muestra o nave nodriza) en la celda seleccionada del tablero.
     */
    public void insertaObjeto(MouseEvent e)
    {
        JLabel casilla = (JLabel) e.getSource(); 
        
        int fila = -1, columna = -1;

        // Encuentra la fila y columna de la celda seleccionada
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (tablero[i][j] == casilla) {
                    fila = i;
                    columna = j;
                    break;
                }
            }
        }   

        // Coloca el icono seleccionado en la celda del tablero
        if(actualIcon!=null) casilla.setIcon(actualIcon); 

        // Actualiza la matriz lógica según el tipo de objeto insertado
        if (actualIcon == sampleIcon) {
            matrix[fila][columna] = 1;
        } else if (actualIcon == obstacleIcon) {
            matrix[fila][columna] = 2;
        } else if (actualIcon == motherIcon) {
            matrix[fila][columna] = 3;
        }
    }
}

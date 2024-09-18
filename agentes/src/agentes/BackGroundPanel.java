package agentes;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Clase BackGroundPanel que extiende de JPanel.
 * Esta clase permite establecer una imagen de fondo personalizada para el panel.
 * Se redimensiona automáticamente el fondo para ajustarse al tamaño del panel.
 */
public class BackGroundPanel extends JPanel
{
    // Imagen de fondo que se dibujará en el panel
    ImageIcon fondo;
    
    /**
     * Constructor de la clase BackGroundPanel.
     * Recibe un ImageIcon que será utilizado como fondo del panel.
     *
     * @param fondo La imagen que se usará como fondo del panel.
     */
    public BackGroundPanel(ImageIcon fondo)
    {
        super();  // Llama al constructor de la clase padre JPanel
        this.fondo = fondo;  // Asigna la imagen de fondo
        repaint();  // Solicita una actualización visual del panel
    }
    
    /**
     * Sobrescribe el método paintComponent de JPanel.
     * Este método se encarga de pintar la imagen de fondo redimensionada al tamaño actual del panel.
     *
     * @param g El objeto Graphics que se utiliza para dibujar la imagen.
     */
    @Override
    public void paintComponent(Graphics g)
    {
        // Obtiene el tamaño actual del panel
        Dimension dim = this.getSize();
        int ancho = (int) dim.getWidth();  // Ancho del panel
        int alto  = (int) dim.getHeight(); // Alto del panel
        
        // Dibuja la imagen de fondo redimensionada al tamaño del panel
        g.drawImage(fondo.getImage(), 0, 0, ancho, alto, null);
        
        // Hace que el fondo del panel sea transparente
        setOpaque(false);
        
        // Llama al método paintComponent de la clase padre para seguir dibujando el resto de los componentes
        super.paintComponent(g);
    }
}

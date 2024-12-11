package gui;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;

public class GraphicPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private int decibelLevel = 0;  // (0-100)
    private boolean showDB = true; // Controls if deibels is displayed

	/**
	 * Methods.
	 */
    
    /**
     * <h1>setDecibelLevel</h1>
     * <p>Sets the decibel level within a valid range and updates the visual representation.</p>
     *
     * @param level The desired decibel level. The value will be clamped between 0 and 100.
     */
    public void setDecibelLevel(int level) {
        this.decibelLevel = Math.max(0, Math.min(level, 100)); // Limits the range between 0 and 100
        repaint(); 
    }

    /**
     * <h1>setShowDB</h1>
     * <p>Sets the visibility of the decibel level display and updates the graphical representation.</p>
     *
     * @param showDB A boolean indicating whether the decibel level should be displayed (true) or hidden (false).
     */
    public void setShowDB(boolean showDB) {
        this.showDB = showDB;
        repaint();
    }
    
    /**
     * <h1>getColorByDecibels</h1>
     * <p>Determines the color based on the given decibel level.</p>
     *
     * @param nivelDecibelios The decibel level. The method returns a color based on the range of the input level.
     * @return A Color object corresponding to the decibel level, with colors ranging from light green to dark red.
     */
    private Color getColorByDecibels(int nivelDecibelios) {
        if (nivelDecibelios < 10) {
            return new Color(0, 255, 118); // Light green
        } else if (nivelDecibelios < 20) {
            return new Color(12, 255, 0); // Neon green
        } else if (nivelDecibelios < 30) {
            return new Color(50, 216, 0); // Dark green
        } else if (nivelDecibelios < 40) {
            return new Color(175, 219, 0); // Yellowish green
        } else if (nivelDecibelios < 50) {
            return new Color(233, 227, 0); // Yellow
        } else if (nivelDecibelios < 60) {
            return new Color(245, 204, 0); // Yellow-orange
        } else if (nivelDecibelios < 70) {
            return new Color(245, 174, 0); // Orange
        } else if (nivelDecibelios < 80) {
            return new Color(245, 119, 0); // Dark orange
        } else if (nivelDecibelios < 90) {
            return new Color(245, 55, 0); // Red
        } else {
            return new Color(194, 0, 0); // Dark red
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Background
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Decibel bar
        g.setColor(getColorByDecibels(decibelLevel));
        int barHeight = (int) (getHeight() * (decibelLevel / 100.0)); // Height proportional to level
        g.fillRect(0, getHeight() - barHeight, getWidth(), barHeight);

        // Current level text
        g.setColor(Color.WHITE);

        // Numeric value
        String texto = decibelLevel + (showDB ? " dB" : "");
        g.drawString(texto, 10, 20);
    }
}



package gui;

import logic.CaptureAudio;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.sound.sampled.AudioFormat;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.awt.event.ActionListener;

public class MainGui extends JFrame {

    private static final long serialVersionUID = 1L;
    private int xMouse, yMouse; 
    private JPanel contentPane;
    private JToggleButton toggle_view;
    private JButton btn_stop, btn_start, btn_configuration;

    private GraphicPanel panel_totalGraphic, panel_miniGraphic;
    private CaptureAudio captureAudio;
    private Thread captureThread;
    private boolean capturingAudio = false;
    
	/**
	 * Launch the application.
	 */

    public static void main(String[] args) {
    	
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarculaLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        EventQueue.invokeLater(() -> {
            try {
                MainGui frame = new MainGui();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Constructor
    public MainGui() {
        captureAudio = new CaptureAudio();
        initializeUI();
    }
    
	/**
	 * Create the frame.
	 */

    private void initializeUI() {
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 250, 300);

        contentPane = new JPanel();
        contentPane.setBorder(new LineBorder(new Color(107, 107, 107), 1, true));
        contentPane.setBackground(Color.DARK_GRAY);
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
		// Add functionality to drag the window
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                xMouse = e.getX();
                yMouse = e.getY();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getXOnScreen();
                int y = e.getYOnScreen();
                setLocation(x - xMouse, y - yMouse);
            }
        });
        

        // Main window
        JLabel lbl_titulo = new JLabel("DBLEDUS");
        lbl_titulo.setVerticalAlignment(SwingConstants.BOTTOM);
        lbl_titulo.setBounds(55, 14, 140, 35);
        lbl_titulo.setFont(new Font("Corbel", Font.BOLD, 24));
        lbl_titulo.setHorizontalAlignment(SwingConstants.CENTER);
        lbl_titulo.setForeground(Color.WHITE);
        contentPane.add(lbl_titulo);

        // Main window -- > Upper section
        JButton btn_close = new JButton("X");
		btn_close.setBackground(new Color(108, 0, 0));
		btn_close.setBorder(new LineBorder(new Color(128, 0, 0), 2, true));
        btn_close.setBounds(10, 11, 35, 35);
        btn_close.addActionListener(this::confirmClosure);
        contentPane.add(btn_close);
        
        toggle_view = new JToggleButton(addImage("/resources/view.png", 20, 20));
        toggle_view.setBounds(205, 11, 35, 35);
        toggle_view.addActionListener(e -> {
            boolean activateTransparency = toggle_view.isSelected();
            setBackground(activateTransparency ? new Color(0, 0, 0, 0) : Color.DARK_GRAY);
            setAlwaysOnTop(activateTransparency);
            if(activateTransparency) {
                contentPane.setBorder(new EmptyBorder(5,5,5,5));
            } else {
                contentPane.setBorder(new LineBorder(new Color(107, 107, 107), 1, true));
            }
            panel_miniGraphic.setVisible(activateTransparency);
            panel_miniGraphic.setShowDB(!activateTransparency); 
            panel_totalGraphic.setVisible(!activateTransparency);
            btn_start.setVisible(!activateTransparency);
            btn_stop.setVisible(!activateTransparency);
            btn_close.setVisible(!activateTransparency);
            lbl_titulo.setVisible(!activateTransparency);
            btn_configuration.setVisible(!activateTransparency); 
        });
        contentPane.add(toggle_view);
        
        // Main window -- > Middle section. Graph
        panel_totalGraphic = new GraphicPanel();
        panel_totalGraphic.setBounds(35, 57, 179, 187);
        contentPane.add(panel_totalGraphic);
        
        panel_miniGraphic = new GraphicPanel();
        panel_miniGraphic.setVisible(false);
        panel_miniGraphic.setBounds(205, 57, 35, 187);
        contentPane.add(panel_miniGraphic);

        // Main window -- > Lower section buttons
        btn_start = new JButton("Start");
        btn_start.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		startAudioCapture(e);
        		if(capturingAudio) {
            		btn_start.setEnabled(false);
            		btn_stop.setEnabled(true);
            		btn_configuration.setEnabled(false);
        		}
        	}
        });
        btn_start.setBounds(22, 266, 80, 23);
        contentPane.add(btn_start);

        btn_stop = new JButton("Stop");
        btn_stop.setEnabled(false);
        btn_stop.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		stopAudioCapture(e);
        		btn_start.setEnabled(true);
        		btn_stop.setEnabled(false);
        		btn_configuration.setEnabled(true);
        	}
        });
        btn_stop.setBounds(149, 266, 80, 23);
        contentPane.add(btn_stop);

        btn_configuration = new JButton(addImage("/resources/config.png", 10, 10));
        btn_configuration.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		ConfigurationGui ajustarBarra = new ConfigurationGui(panel_miniGraphic, captureAudio);
        		ajustarBarra.setVisible(true);
            }
        });
        btn_configuration.setBounds(111, 266, 30, 23);
        contentPane.add(btn_configuration);
    }
    
	/**
	 * Methods.
	 */
 
    /**
     * <h1>startAudioCapture()</h1>
     * <p>Initiates the audio capture process and updates the graphical panels with the decibel levels in real time.</p>
     *
     * @param e The ActionEvent triggered by the user when starting the audio capture.
     */
    private void startAudioCapture(ActionEvent e) {
        AudioFormat format = new AudioFormat(48000, 16, 2, true, false); 
        capturingAudio = true;
        // Create a thread for audio capture
        captureThread = new Thread(() -> {
            captureAudio.captureAudio(format, dbLevel -> {
                if (!capturingAudio) return;
                SwingUtilities.invokeLater(() -> {
                    panel_totalGraphic.setDecibelLevel((int) dbLevel);
                    panel_miniGraphic.setDecibelLevel((int) dbLevel);
                });
            });
        });
        captureThread.start();
    }

    /**
     * <h1>stopAudioCapture()</h1>
     * <p>Stops the ongoing audio capture process, terminates the capture thread, and resets the graphical panels.</p>
     *
     * @param e The ActionEvent triggered by the user when stopping the audio capture.
     */
    private void stopAudioCapture(ActionEvent e) {
        if (captureThread != null && captureThread.isAlive()) {
            capturingAudio = false; 
            captureAudio.stopCapturing(); 
            captureThread.interrupt();
            try {
                captureThread.join(); 
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        // Clean graph
        SwingUtilities.invokeLater(() -> {
            panel_totalGraphic.setDecibelLevel(0);
            panel_miniGraphic.setDecibelLevel(0);
        });
    }
    
    /**
     * <h1>addImage()</h1>
     * <p>This method loads an image from the specified path and resizes it to the desired dimensions. 
     * If the image cannot be loaded, a default placeholder icon is returned.</p>
     *
     * @param path The path to the image resource.
     * @param width The desired width for the scaled image.
     * @param height The desired height for the scaled image.
     * 
     * @return A resized image as an Icon, or a placeholder Icon if the image is not found.
     */
    private Icon addImage(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(getClass().getResource(path));

        if (icon.getImage() == null) {
            return new ImageIcon(createTextIcon("⛶", 25, 25));
        }

        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    /**
     * <h1>createTextIcon()</h1>
     * <p>This helper method generates an image containing a given text. 
     * The resulting image can be used as a placeholder icon.</p>
     *
     * @param text The text to be drawn on the image.
     * @param width The width of the generated image.
     * @param height The height of the generated image.
     * 
     * @return An image containing the specified text.
     */
    private Image createTextIcon(String text, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.drawString(text, width / 4, height / 2);

        g2.dispose();
        return image;
    }
    
    /**
     * <h1>confirmClosure()</h1>
     * <p>Displays a confirmation dialog to the user before closing the application. 
     * If confirmed, it stops the audio capture process and exits the program.</p>
     *
     * @param e The ActionEvent triggered by the user when attempting to close the application.
     */
    private void confirmClosure(ActionEvent e) {
    	int answer = JOptionPane.showConfirmDialog(
    	    null, 
    	    "Are you sure you want to exit?", 
    	    "Exit confirmation", 
    	    JOptionPane.YES_NO_OPTION, 
    	    JOptionPane.WARNING_MESSAGE
    	);
    
        if (answer == JOptionPane.YES_OPTION) {
            stopAudioCapture(null);
            System.exit(0);
        }
    }
}

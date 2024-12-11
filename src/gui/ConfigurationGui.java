package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSeparator;

import logic.CaptureAudio;

import javax.swing.border.EtchedBorder;

public class ConfigurationGui extends JFrame {

	private static final long serialVersionUID = 1L;
	private int xMouse, yMouse;
	private int sensitivityValue = 70;
	private JPanel contentPane, panel_graficaMini, panelPreview;
	private JLabel lbl_sensitivityValue;

	/**
	 * Launch the application.
	 */

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarculaLaf());
		} catch (Exception e) {
			e.printStackTrace();
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JPanel testPanel = new JPanel();
					testPanel.setBounds(0, 0, 100, 100);
					CaptureAudio capturarAudio = new CaptureAudio();
					ConfigurationGui frame = new ConfigurationGui(testPanel, capturarAudio);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */

	public ConfigurationGui(JPanel panel, CaptureAudio capturarAudio) {
		this.panel_graficaMini = panel; // Save the JPanel reference to update the changes
		this.sensitivityValue = (int) capturarAudio.getMaxObserved();
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 380);
		
		contentPane = new JPanel();
		contentPane.setBorder(new LineBorder(new Color(107, 107, 107), 1, true));
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

		
		// General window buttons
		JButton btn_close = new JButton("X");
		btn_close.setBackground(new Color(108, 0, 0));
		btn_close.setBorder(new LineBorder(new Color(128, 0, 0), 2, true));
		btn_close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btn_close.setBounds(10, 11, 46, 24);
		contentPane.add(btn_close);
		
		JButton btn_save = new JButton("Save");
		btn_save.setBorder(new LineBorder(new Color(68, 110, 158), 2, true));
		btn_save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel_graficaMini.setBounds(panel_graficaMini.getX(),
											panel_graficaMini.getY(),
											panel_graficaMini.getWidth(),
											panel_graficaMini.getHeight());
				// Refresh and update the parent container's layout and appearance
				panel_graficaMini.getParent().revalidate();
				panel_graficaMini.getParent().repaint();
				dispose();
			}
		});
		btn_save.setBounds(177, 336, 107, 23);
		contentPane.add(btn_save);
		

		// Dimensions menu -- > ...
		JLabel lbl_personaliza = new JLabel("DIMENSIONS");
		lbl_personaliza.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_personaliza.setForeground(new Color(255, 255, 255));
		lbl_personaliza.setVerticalAlignment(SwingConstants.BOTTOM);
		lbl_personaliza.setFont(new Font("Bahnschrift", Font.BOLD, 16));
		lbl_personaliza.setBounds(20, 57, 137, 24);
		contentPane.add(lbl_personaliza);
		
		JLabel lbl_infoDimension = new JLabel(
			  "<html>"
				+ "<div style='text-align: center;'>"
					+ "Adjust the bar in minimized mode."
				+ "</div>"
			+ "</html>\r\n");
		lbl_infoDimension.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lbl_infoDimension.setBounds(34, 80, 107, 45);
		contentPane.add(lbl_infoDimension);
		
		// Dimensions menu -- > Width
		JLabel lbl_width = new JLabel("Width");
		lbl_width.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_width.setForeground(Color.LIGHT_GRAY);
		lbl_width.setBounds(65, 148, 46, 14);
		contentPane.add(lbl_width);
		
		JButton btn_moreWidth1 = new JButton("+1");
		btn_moreWidth1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setMoreWidth(1);
				drawPreview();
			}
		});
		btn_moreWidth1.setBounds(34, 168, 50, 25);
		contentPane.add(btn_moreWidth1);

		JButton btn_lessWidth1 = new JButton("-1");
		btn_lessWidth1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setLessWidth(1);
				drawPreview();
			}
		});
		btn_lessWidth1.setBounds(91, 168, 50, 25);
		contentPane.add(btn_lessWidth1);

		JButton btn_lessWidth5 = new JButton("-5");
		btn_lessWidth5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setLessWidth(5);
				drawPreview();
			}
		});
		btn_lessWidth5.setBounds(91, 194, 50, 25);
		contentPane.add(btn_lessWidth5);

		JButton btn_moreWidth5 = new JButton("+5");
		btn_moreWidth5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setMoreWidth(5);
				drawPreview();
			}
		});
		btn_moreWidth5.setBounds(34, 194, 50, 25);
		contentPane.add(btn_moreWidth5);
		
		// Dimensions menu -- > Height
		JLabel lbl_height = new JLabel("Height");
		lbl_height.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_height.setForeground(Color.LIGHT_GRAY);
		lbl_height.setBounds(65, 230, 46, 24);
		contentPane.add(lbl_height);

		JButton btn_moreHeight1 = new JButton("+1");
		btn_moreHeight1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setMoreHeight(1);
				drawPreview();
			}
		});
		btn_moreHeight1.setBounds(34, 254, 50, 25);
		contentPane.add(btn_moreHeight1);

		JButton btn_lessHeight1 = new JButton("-1");
		btn_lessHeight1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setLessHeight(1);
				drawPreview();
			}
		});
		btn_lessHeight1.setBounds(91, 254, 50, 25);
		contentPane.add(btn_lessHeight1);

		JButton btn_moreHeight5 = new JButton("+5");
		btn_moreHeight5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setMoreHeight(5);
				drawPreview();
			}
		});
		btn_moreHeight5.setBounds(34, 280, 50, 25);
		contentPane.add(btn_moreHeight5);

		JButton btn_lessHeight5 = new JButton("-5");
		btn_lessHeight5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setLessHeight(5);
				drawPreview();
			}
		});
		btn_lessHeight5.setBounds(91, 280, 50, 25);
		contentPane.add(btn_lessHeight5);
		
		// Dimensions menu -- > Preview panel
		panelPreview = new JPanel();
		panelPreview.setBackground(new Color(87, 187, 247));
		panelPreview.setBounds(171, 95, panel_graficaMini.getWidth(), panel_graficaMini.getHeight());
		contentPane.add(panelPreview);
		
		
		// Separator line
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(247, 57, 10, 248);
		contentPane.add(separator);
		
		
		// Sensitivity menu
		JLabel lbl_sensitivity = new JLabel("SENSITIVITY");
		lbl_sensitivity.setVerticalAlignment(SwingConstants.BOTTOM);
		lbl_sensitivity.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_sensitivity.setForeground(Color.WHITE);
		lbl_sensitivity.setFont(new Font("Bahnschrift", Font.BOLD, 16));
		lbl_sensitivity.setBounds(281, 57, 137, 24);
		contentPane.add(lbl_sensitivity);

		JLabel lbl_infoSensitivity = new JLabel(
			 "<html>"
				+ "<div style='width: 100%; text-align: justify;'>"
					+ "Adjust the sensitivity to the maximum noise level you want. Increase the"
					+ "value to broaden the range and allow louder voices, or decrease it to "
					+ "increase sensitivity and capture quieter sounds.<br><br>"
					+ "Default value: 70"
				+ "</div>"
			+ "</html>\r\n");
		lbl_infoSensitivity.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lbl_infoSensitivity.setBounds(267, 68, 154, 162);
		contentPane.add(lbl_infoSensitivity);
		
		lbl_sensitivityValue = new JLabel(String.valueOf((int) capturarAudio.getMaxObserved()));
		lbl_sensitivityValue.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		lbl_sensitivityValue.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lbl_sensitivityValue.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_sensitivityValue.setBounds(348, 255, 70, 50);
		contentPane.add(lbl_sensitivityValue);

		JButton btn_moreSensitivity = new JButton("+");
		btn_moreSensitivity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sensitivityValue++;
				// Set a maximum sensitivity value
				if (sensitivityValue > 100) {
					sensitivityValue = 100;
				}
				lbl_sensitivityValue.setText(String.valueOf(sensitivityValue));
			}
		});
		btn_moreSensitivity.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btn_moreSensitivity.setBounds(267, 255, 55, 24);
		contentPane.add(btn_moreSensitivity);

		JButton btn_lessSensitivity = new JButton("-");
		btn_lessSensitivity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sensitivityValue--;
				// Set a minimum sensitivity value
				if (sensitivityValue < 40) {
					sensitivityValue = 40;
				}
				lbl_sensitivityValue.setText(String.valueOf(sensitivityValue));
			}
		});
		btn_lessSensitivity.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btn_lessSensitivity.setBounds(267, 281, 55, 24);
		contentPane.add(btn_lessSensitivity);
	}
	
	
	/**
	 * Methods.
	 */
	
	/**
	 * <h1>setMoreWidth()</h1>
	 * <p>Increases the width of the panel dynamically by the specified value.</p>
	 *
	 * @param width The amount of width to add to the current width of the panel.
	 */
	private void setMoreWidth(int height) {
		panel_graficaMini.setBounds(panel_graficaMini.getX(), 
									panel_graficaMini.getY(),
									panel_graficaMini.getWidth() + height,	
									panel_graficaMini.getHeight());
		// Set a maximum width value
		if (panel_graficaMini.getWidth() > 50) {
			panel_graficaMini.setBounds(panel_graficaMini.getX(),
										panel_graficaMini.getY(),
										50,
										panel_graficaMini.getHeight());
		}
	}
	
	/**
	 * <h1>setLessWidth()</h1>
	 * <p>Decreases the width of the panel dynamically by the specified value.</p>
	 *
	 * @param width The amount of width to add to the current width of the panel.
	 */
	private void setLessWidth(int height) {
		panel_graficaMini.setBounds(panel_graficaMini.getX(), 
									panel_graficaMini.getY(),
									panel_graficaMini.getWidth() - height,	
									panel_graficaMini.getHeight());
		// Set a minimum width value
		if (panel_graficaMini.getWidth() < 10) {
			panel_graficaMini.setBounds(panel_graficaMini.getX(),
										panel_graficaMini.getY(),
										10,
										panel_graficaMini.getHeight());
		}
	}
	
	/**
	 * <h1>setMoreHeight()</h1>
	 * <p>Increases the height of the panel dynamically by the specified value.</p>
	 *
	 * @param height The amount of height to add to the current height of the panel.
	 */
	private void setMoreHeight(int height) {
		panel_graficaMini.setBounds(panel_graficaMini.getX(), 
									panel_graficaMini.getY(),
									panel_graficaMini.getWidth(),	
									panel_graficaMini.getHeight() + height);
		// Set a maximum height value
		if (panel_graficaMini.getHeight() > 210) {
		    panel_graficaMini.setBounds(panel_graficaMini.getX(), 
		    							panel_graficaMini.getY(),
								        panel_graficaMini.getWidth(), 
								        210);
		}
	}
	
	/**
	 * <h1>setLessHeight()</h1>
	 * <p>Decreases the height of the panel dynamically by the specified value.</p>
	 *
	 * @param height The amount of height to add to the current height of the panel.
	 */
	private void setLessHeight(int height) {
		panel_graficaMini.setBounds(panel_graficaMini.getX(), 
									panel_graficaMini.getY(),
									panel_graficaMini.getWidth(),	
									panel_graficaMini.getHeight() - height);
		// Set a minimum height value
		if (panel_graficaMini.getHeight() < 70) {
			panel_graficaMini.setBounds(panel_graficaMini.getX(),
										panel_graficaMini.getY(),
										panel_graficaMini.getWidth(),
										70);
		}
	}

    /**
     * <h1>drawPreview()</h1>
     * <p>Adjusts the bounds of the panelPreview based on the width and height of another panel. 
     * This ensures the preview panel updates in real time to match changes in size.</p>
     */
	private void drawPreview() {
		panelPreview.setBounds(panelPreview.getX(), 
							   panelPreview.getY(), 
							   panel_graficaMini.getWidth(),
							   panel_graficaMini.getHeight());
		
		panelPreview.revalidate();
		panelPreview.repaint();
	}
}

/**
 * @author Tyler Viarengo
 */

package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import shapes.Dot;
import shapes.Line;
import shapes.Shape;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import java.awt.Panel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainWindow implements MouseListener, MouseMotionListener, ActionListener{

	private JFrame frame;
	private FieldCanvas canvas;
	private JLabel coordLabel;
	private JButton modeButton;
	private JButton clearButton;
	
	private boolean drawing = false;
	private int x1 = -1;
	private int x2 = -1;
	private int y1 = -1;
	private int y2 = -1;
	private int pressX = -1;
	private int pressY = -1;
	private int releaseX = -1;
	private int releaseY = -1;
	private int mode = 0; //0 Freeform, 1 Continuous Line

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 865, 606);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		canvas = new FieldCanvas();
		canvas.setBounds(0, 0, 577, 577);
		canvas.setForeground(Color.WHITE);
		canvas.setVisible(true);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(canvas);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBackground(Color.LIGHT_GRAY);
		separator.setBounds(578, 0, 2, 577);
		frame.getContentPane().add(separator);
		
		JPanel panel = new JPanel();
		panel.setBounds(580, 0, 279, 577);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		coordLabel = new JLabel("Coordinates: x, y");
		coordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		coordLabel.setBounds(10, 11, 259, 35);
		panel.add(coordLabel);
		
		modeButton = new JButton("Set Mode to (Continuous)");
		modeButton.addActionListener(this);
		modeButton.setBounds(10, 57, 259, 35);
		panel.add(modeButton);
		
		clearButton = new JButton("Clear Canvas");
		clearButton.addActionListener(this);
		clearButton.setBounds(10, 103, 259, 35);
		panel.add(clearButton);
	}
	
	public void addDot(int x, int y) {
		int radius = 10;
		Shape newShape = new Dot(x - radius/2, y -radius/2, radius, Color.BLACK, new BasicStroke(1), true);
		canvas.addShape(newShape);
		canvas.drawShape(canvas.getGraphics(), newShape);
		//canvas.repaint();
	}
	
	public void addLine(int x1, int y1, int x2, int y2) {
		Shape newShape = new Line(x1, y1, x2, y2);
		canvas.addShape(newShape);
		canvas.drawShape(canvas.getGraphics(), newShape);
	}
	
	public void afterDrawing() {
		double distance = Math.sqrt(Math.pow((pressX - releaseX), 2) + Math.pow((pressY - releaseY), 2));
		//System.out.println(distance);
		
		if(distance < 5) {
			if(mode == 0) {
				addDot(pressX, pressY);
			}else if(mode == 1) {
				if(x1 == -1 && y1 == -1) {
					x1 = pressX;
					y1 = pressY;
				}else{
					addLine(x1, y1, releaseX, releaseY);
					
					double deg = Math.toDegrees(Math.atan2(releaseX - x1, releaseY - y1));
					double deg2 = -1;
					
					if(releaseX - x1 > 0){
						if(releaseY - y1 > 0) {
							//Q4 +,-
							deg2 = deg + 270;
						}else{
							//Q1 +,+
							deg2 = deg - 90;
						}
					}else {
						if(releaseY - y1 > 0) {
							//Q3 -,-
							deg2 = Math.abs(deg + 90) + 180;
						}else{
							//Q2 -,+
							deg2 = Math.abs(deg + 180) + 90;
						}
					}
					
					System.out.println(deg2);
					
					x1 = releaseX;
					y1 = releaseY;
				}
				addDot(x1, y1);
			}
			
		}else{
			if(mode == 0) {
				addLine(pressX, pressY, releaseX, releaseY);
			}
		}
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {}
	
	@Override
	public void mousePressed(MouseEvent e) {
		//System.out.println("Mouse is Down");
		
		drawing = true;
		pressX = e.getX();
		pressY = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		//System.out.println("Mouse is Up");
		
		drawing = false;
		releaseX = e.getX();
		releaseY = e.getY();
		
		afterDrawing();
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {
		//System.out.println("Mouse Has Been Dragged");
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		coordLabel.setText("Coordinates: "+e.getX()+", "+e.getY());
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == modeButton) {
			if(mode == 1) {
				mode = 0;
				modeButton.setText("Set Mode to (Continuous)");
				x1 = -1;
				y1 = -1;
				x2 = -1;
				y2 = -1;
			}else{
				mode = 1;
				modeButton.setText("Set Mode to (Freeform)");
				x1 = -1;
				y1 = -1;
				x2 = -1;
				y2 = -1;
			}			
		}
		
		if(e.getSource() == clearButton) {
			int choice = JOptionPane.showConfirmDialog(frame, "Warning! Are you sure you wish to clear the Canvas and all Commands?", "Warning: Clear Canvas?", JOptionPane.YES_NO_OPTION);
			if(choice == 0) {
				x1 = -1;
				y1 = -1;
				x2 = -1;
				y2 = -1;
				canvas.clearShapes();
				canvas.repaint();
			}
		}
	}
}
 
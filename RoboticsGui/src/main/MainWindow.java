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
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import shapes.Dot;
import shapes.Line;
import shapes.ProgramPoint;
import shapes.Shape;
import shapes.StartPoint;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import commands.Command;

import java.awt.Dimension;
import java.awt.Panel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JCheckBox;

public class MainWindow implements MouseListener, MouseMotionListener, ActionListener{

	private final int RADIUS = 10;
	
	private JFrame frame;
	private FieldCanvas canvas;
	private JLabel coordLabel;
	private JButton modeButton;
	private JButton clearButton;
	private JLabel startPosLabel;
	private JLabel startXLabel;
	private JTextField startXField;
	private JLabel startALabel;
	private JTextField startAField;
	private JLabel startYLabel;
	private JTextField startYField;
	private JButton setStartPosButton;
	private JTextField strafeTextField;
	private JLabel lblStrafeAngle;
	private JCheckBox chckbxStrafe;
	private JButton programButton;
	private JLabel lblSave;
	private JTextField saveTextField;
	private JTextField ftPRevField;
	private JTextField pxPFtField;
	private JTextField degPRevField;
	
	private boolean startPointSet = false;
	private boolean drawing = false;
	private boolean constantsSet = false;
	private int startPosX;
	private int startPosY;
	private int startPosA;
	private int x1 = -1;
	private int x2 = -1;
	private int y1 = -1;
	private int y2 = -1;
	private int pressX = -1;
	private int pressY = -1;
	private int releaseX = -1;
	private int releaseY = -1;
	private int mode = 0; //0 Freeform, 1 Continuous Line, 2 Programming
	private double degreesPerRev;
	private double feetPerRev;
	private double pixelsPerFoot;
	
	private ArrayList<ProgramPoint> programPoints = new ArrayList<ProgramPoint>();
	
	
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
		
		modeButton = new JButton("Set Mode (Current: Freeform)");
		modeButton.addActionListener(this);
		modeButton.setBounds(10, 57, 259, 35);
		panel.add(modeButton);
		
		clearButton = new JButton("Clear Canvas");
		clearButton.addActionListener(this);
		clearButton.setBounds(10, 103, 259, 35);
		panel.add(clearButton);
		
		startPosLabel = new JLabel("Start Pos: x, y | Start Angle: a\u00B0");
		startPosLabel.setHorizontalAlignment(SwingConstants.CENTER);
		startPosLabel.setBounds(10, 149, 259, 35);
		panel.add(startPosLabel);
		
		startXField = new JTextField();
		startXField.setEditable(false);
		startXField.setBounds(40, 195, 50, 20);
		panel.add(startXField);
		startXField.setColumns(10);
		
		startXLabel = new JLabel("X: ");
		startXLabel.setBounds(20, 195, 20, 20);
		panel.add(startXLabel);
		
		startALabel = new JLabel("A: ");
		startALabel.setBounds(180, 195, 20, 20);
		panel.add(startALabel);
		
		startAField = new JTextField();
		startAField.setEditable(false);
		startAField.setColumns(10);
		startAField.setBounds(200, 195, 50, 20);
		panel.add(startAField);
		
		startYLabel = new JLabel("Y: ");
		startYLabel.setBounds(100, 195, 20, 20);
		panel.add(startYLabel);
		
		startYField = new JTextField();
		startYField.setEditable(false);
		startYField.setColumns(10);
		startYField.setBounds(120, 195, 50, 20);
		panel.add(startYField);
		
		setStartPosButton = new JButton("Set Start Position");
		setStartPosButton.setEnabled(false);
		setStartPosButton.setBounds(10, 226, 259, 35);
		setStartPosButton.addActionListener(this);
		panel.add(setStartPosButton);
		
		JLabel lblStrafeSettings = new JLabel("Strafe Settings");
		lblStrafeSettings.setHorizontalAlignment(SwingConstants.CENTER);
		lblStrafeSettings.setBounds(10, 271, 259, 14);
		panel.add(lblStrafeSettings);
		
		chckbxStrafe = new JCheckBox("Strafe");
		chckbxStrafe.setEnabled(false);
		chckbxStrafe.setBounds(10, 292, 60, 23);
		chckbxStrafe.addActionListener(this);
		panel.add(chckbxStrafe);
		
		lblStrafeAngle = new JLabel("Strafe Angle (90\u00B0 \u00B1 X):");
		lblStrafeAngle.setBounds(76, 296, 144, 14);
		panel.add(lblStrafeAngle);
		
		strafeTextField = new JTextField();
		strafeTextField.setEditable(false);
		strafeTextField.setColumns(10);
		strafeTextField.setBounds(219, 293, 50, 20);
		strafeTextField.addActionListener(this);
		panel.add(strafeTextField);
		
		programButton = new JButton("PROGRAM!");
		programButton.setEnabled(false);
		programButton.setBounds(10, 531, 259, 35);
		programButton.addActionListener(this);
		panel.add(programButton);
		
		saveTextField = new JTextField();
		saveTextField.setEditable(false);
		saveTextField.setBounds(10, 500, 259, 20);
		panel.add(saveTextField);
		saveTextField.setColumns(10);
		
		lblSave = new JLabel("Program Destination Location");
		lblSave.setHorizontalAlignment(SwingConstants.CENTER);
		lblSave.setBounds(10, 469, 259, 20);
		panel.add(lblSave);
		
		JLabel lblConstants = new JLabel("Constants");
		lblConstants.setHorizontalAlignment(SwingConstants.CENTER);
		lblConstants.setBounds(86, 331, 100, 14);
		panel.add(lblConstants);
		
		ftPRevField = new JTextField();
		ftPRevField.setHorizontalAlignment(SwingConstants.CENTER);
		ftPRevField.setEditable(false);
		ftPRevField.setBounds(40, 376, 50, 20);
		panel.add(ftPRevField);
		ftPRevField.setColumns(10);
		
		pxPFtField = new JTextField();
		pxPFtField.setText("48");
		pxPFtField.setHorizontalAlignment(SwingConstants.CENTER);
		pxPFtField.setEditable(false);
		pxPFtField.setColumns(10);
		pxPFtField.setBounds(120, 376, 50, 20);
		panel.add(pxPFtField);
		
		degPRevField = new JTextField();
		degPRevField.setHorizontalAlignment(SwingConstants.CENTER);
		degPRevField.setEditable(false);
		degPRevField.setColumns(10);
		degPRevField.setBounds(200, 376, 50, 20);
		panel.add(degPRevField);
		
		JLabel constantDescriptionLabel = new JLabel("       Feet/Rev         Pixel/Foot      Degrees/Rev");
		constantDescriptionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		constantDescriptionLabel.setBounds(10, 351, 259, 14);
		panel.add(constantDescriptionLabel);
	}
	
	public void addStartPoint(int x, int y, int a) {
		Shape newShape = new StartPoint(x - RADIUS/2, y -RADIUS/2, a);
		canvas.addShape(newShape);
		canvas.drawShape(canvas.getGraphics(), newShape);
	}
	
	public void addDot(int x, int y) {
		Shape newShape = new Dot(x - RADIUS/2, y -RADIUS/2, RADIUS, Color.BLACK, new BasicStroke(1), true);
		canvas.addShape(newShape);
		canvas.drawShape(canvas.getGraphics(), newShape);
	}
	
	public void addLine(int x1, int y1, int x2, int y2) {
		Shape newShape = new Line(x1, y1, x2, y2);
		canvas.addShape(newShape);
		canvas.drawShape(canvas.getGraphics(), newShape);
	}
	
	public void setStartPosition() {
		
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
					
					double deg = getAngle(releaseX, releaseY, x1, y1);
					
					System.out.println(deg);
					
					x1 = releaseX;
					y1 = releaseY;
				}
				addDot(x1, y1);
			}else if(mode == 2) {
				if(startPointSet) {
					if(x1 == -1 && y1 == -1) {
						x1 = startPosX;
						y1 = startPosY;
					}
					
					addLine(x1, y1, releaseX, releaseY);
					
					double deg = getAngle(releaseX, releaseY, x1, y1);
					
					addProgramPoint(releaseX, releaseY, deg);
					
					System.out.println(deg);
					
					x1 = releaseX;
					y1 = releaseY;
				}else{
					alert("You must set a start point");
				}
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
			if(mode == 0) {
				mode = 1;
				modeButton.setText("Set Mode (Current: Continuous)");
				toggleGUI();
			}else if (mode == 1){
				mode = 2;
				modeButton.setText("Set Mode (Current: Programming)");
				toggleGUI();
			}else if (mode == 2) {
				mode = 0;
				modeButton.setText("Set Mode (Current: Freeform)");
				toggleGUI();
			}			
		}
		
		if(e.getSource() == clearButton) {
			int choice = JOptionPane.showConfirmDialog(frame, "Warning! Are you sure you wish to clear the Canvas and all Commands?", "Warning: Clear Canvas?", JOptionPane.YES_NO_OPTION);
			if(choice == 0) {
				x1 = -1;
				y1 = -1;
				x2 = -1;
				y2 = -1;
				
				startPointSet = false;
				canvas.clearShapes();
				canvas.repaint();
				programPoints.clear();
			}
		}
		
		if(e.getSource() == setStartPosButton) {
			startPointSet = validateStartPos();
			x1 = -1;
			y1 = -1;
			x2 = -1;
			y2 = -1;
			canvas.clearShapes();
			canvas.repaint();
			programPoints.clear();
			addProgramPoint(startPosX,startPosY,startPosA);
			addStartPoint(startPosX, startPosY, startPosA);
			startPosLabel.setText("Start Pos: "+startPosX+", "+startPosY+" | Start Angle: "+startPosA+"\u00B0");
		}
		
		if(e.getSource() == chckbxStrafe) {
			if(chckbxStrafe.isSelected()) {
				strafeTextField.setEditable(true);
			}else{
				strafeTextField.setEditable(false);
			}
		}
		
		if(e.getSource() == strafeTextField) {
			int angle = validateAngle(strafeTextField.getText());
			if(angle != -1) {
				lblStrafeAngle.setText("Strafe Angle (90\u00B0 \u00B1 "+angle+"):");
			}
		}
		
		if(e.getSource() == programButton) {
			constantsSet = validateConstants();
			if(programPoints != null && constantsSet) {
				int strafeAngle;
				if(chckbxStrafe.isSelected()) {
					strafeAngle = validateAngle(strafeTextField.getText());
				}else{
					strafeAngle = -1;
				}
				CommandProcessor processor = new CommandProcessor(strafeAngle, degreesPerRev,feetPerRev,pixelsPerFoot);
				
				processor.setPoints(programPoints);
				processor.process();
				
				//trySave(processor.getCommands());
			}
		}
	}
	
	public void trySave(List<Command> commands) {
		try {
			File file = new File(saveTextField.getText());
			if(file.isDirectory()) {
				ArrayList<String> commandStr = new ArrayList<String>();
				for(Command elem : commands) {
					commandStr.add(elem.command);
				}
				
				CommandWriter writer = new CommandWriter(commandStr,file.getPath());
				writer.write();
			}else {
				alert("Invalid Save Location");
			}
		}catch (Exception e) {
			alert("Invalid Save Location");
		}
	}
	
	public void addProgramPoint(int x, int y, double a) {
		ProgramPoint point = new ProgramPoint(x,y,a);
		programPoints.add(point);
	}
	
	public int validateAngle(String num) {
		int angle;
		try {
			angle = Integer.parseInt(num);
		}catch (Exception e) {
			alert("Angle Must Be a Number.");
			return -1;
		}
		
		if(angle < 0 || angle > 359) {
			alert("Angle Must Be 0-359");
			return -1;
		}
		
		return angle;
	}
	
	public boolean validateStartPos() {
		int xStart,yStart,aStart;
		try {
			xStart = Integer.parseInt(startXField.getText());
			yStart = Integer.parseInt(startYField.getText());
			aStart = Integer.parseInt(startAField.getText());
		}catch (Exception e) {
			alert("Start Position Fields Accepts Whole Numbers Only");
			return false;
		}
		
		if(xStart < 0 || xStart > 576) {
			alert("Start X Field Must Be 0-576");
			return false;
		}
		if(yStart < 0 || yStart > 576) {
			alert("Start Y Field Must Be 0-576");
			return false;
		}
		if(aStart < 0 || aStart > 359) {
			alert("Start A Field Must Be 0-359");
			return false;
		}
		
		startPosX = xStart;
		startPosY = yStart;
		startPosA = aStart;
		return true;
	}
	
	public boolean validateConstants() {
		double DPR, FPR, PPF; //Degrees Per Rev, Feet Per Rev, Pixels Per Foot
		try {
			DPR = Double.parseDouble(degPRevField.getText());
			FPR = Double.parseDouble(ftPRevField.getText());
			PPF = Double.parseDouble(pxPFtField.getText());
		}catch (Exception e) {
			alert("Constant Fields Accepts Numbers Only");
			return false;
		}
		
		if(DPR < 0 || FPR < 0 || PPF < 0) {
			alert("Constant Fields Accepts Positive Numbers Only");
			return false;
		}
		
		degreesPerRev = DPR;
		feetPerRev = FPR;
		pixelsPerFoot = PPF;
		return true;
	}
	
	public void toggleGUI() {
		x1 = -1;
		y1 = -1;
		x2 = -1;
		y2 = -1;
		canvas.clearShapes();
		canvas.repaint();
		
		if(mode == 2) {
			startXField.setEditable(true);
			startYField.setEditable(true);
			startAField.setEditable(true);
			setStartPosButton.setEnabled(true);
			chckbxStrafe.setEnabled(true);
			strafeTextField.setEditable(true);
			programButton.setEnabled(true);
			saveTextField.setEditable(true);
			ftPRevField.setEditable(true);
			degPRevField.setEditable(true);
			pxPFtField.setEditable(true);
		}else{
			startPointSet = false;
			startXField.setEditable(false);
			startYField.setEditable(false);
			startAField.setEditable(false);
			setStartPosButton.setEnabled(false);
			chckbxStrafe.setEnabled(false);
			strafeTextField.setEditable(false);
			programButton.setEnabled(false);
			saveTextField.setEditable(false);
			ftPRevField.setEditable(false);
			degPRevField.setEditable(false);
			pxPFtField.setEditable(false);
		}
		
	}
	
	public double getAngle(int x1, int y1, int x2, int y2) {
		double deg = Math.toDegrees(Math.atan2(x1 - x2, y1 - y2));
		double deg2;
		
		if(x1 - x2 > 0){
			if(y1 - y2 > 0) {
				//Q4 +,-
				deg2 = deg + 270;
			}else{
				//Q1 +,+
				deg2 = deg - 90;
			}
		}else {
			if(y1 - y2 > 0) {
				//Q3 -,-
				deg2 = Math.abs(deg + 90) + 180;
			}else{
				//Q2 -,+
				deg2 = Math.abs(deg + 180) + 90;
			}
		}
		
		return deg2;
	}
	
	public void alert(String msg) {
		JOptionPane.showMessageDialog(frame, msg, "Warning", JOptionPane.WARNING_MESSAGE);
	}
}
 
/**
 * @author Tyler Viarengo
 */

package main;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import shapes.Dot;
import shapes.Line;
import shapes.Shape;

public class FieldCanvas extends Canvas{
	
	private ArrayList<Shape> shapes = new ArrayList<Shape>(); 
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		BufferedImage fieldBG;
		try {
			fieldBG = ImageIO.read(new File("src\\Field.png"));
			g.drawImage(fieldBG, 0, 0, this);

		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		for(Shape s : shapes) {
			drawShape(g2, s);
		}
	}
	
	public void drawShape(Graphics g, Shape s) {
		Graphics2D g2 = (Graphics2D) g;
		
		if(s instanceof Dot) {
			Dot dot = (Dot) s;
			
			g2.setStroke(dot.getStroke());
			g2.setColor(dot.getColor());
			
			if(s.filled()){
				g2.fillOval(dot.pointsX.get(0), dot.pointsY.get(0), dot.getRadius(), dot.getRadius());
			}else{
				g2.drawOval(dot.pointsX.get(0), dot.pointsY.get(0), dot.getRadius(), dot.getRadius());
			}	
		}else if(s instanceof Line){
			Line line = (Line) s;
			
			g2.setStroke(line.getStroke());
			g2.setColor(line.getColor());
			
			g2.drawLine(line.pointsX.get(0), line.pointsY.get(0), line.pointsX.get(1), line.pointsY.get(1));
		}
	}
	
	public void addShape(Shape shape) {
		shapes.add(shape);
	}
	
	public void addShape(Shape shape, int index) {
		shapes.add(index, shape);
	}
	
	public void removeShape(int index) {
		shapes.remove(index);
	}
	
	public void clearShapes() {
		shapes.clear();
	}
	
	public ArrayList<Shape> getShapes(){
		return shapes;
	}
}
 
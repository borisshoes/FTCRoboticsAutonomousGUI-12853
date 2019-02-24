/**
 * @author Tyler Viarengo
 */

package shapes;

import java.awt.Color;
import java.awt.Stroke;
import java.util.ArrayList;

public abstract class Shape {
	public ArrayList<Integer> pointsX = new ArrayList<Integer>();
	public ArrayList<Integer> pointsY = new ArrayList<Integer>();
	private Color color;
	private boolean filled = false;
	private Stroke stroke;
	
	public void addPoint(int x, int y) {
		pointsX.add(x);
		pointsY.add(y);
	}
	
	public void deletePoint(int index) {
		pointsX.remove(index);
		pointsY.remove(index);
	}
	
	public void addPoint(int x, int y, int index) {
		pointsX.add(index, x);
		pointsY.add(index, y);
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setFill(boolean fill) {
		this.filled = fill;
	}
	
	public boolean filled() {
		return filled;
	}
	
	public void setStroke(Stroke stroke) {
		this.stroke = stroke;
	}
	
	public Stroke getStroke() {
		return stroke;
	}

}
 
/**
 * @author Tyler Viarengo
 */

package shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

public class Dot extends Shape{
	private int radius;
	
	public Dot(int x, int y, int radius) {
		addPoint(x, y);
		setStroke(new BasicStroke(1));
		setColor(Color.BLACK);
		setFill(false);
		this.radius = radius;
	}
	
	public Dot(int x, int y, int radius, Color color) {
		addPoint(x, y);
		setColor(color);
		setStroke(new BasicStroke(1));
		setFill(false);
		this.radius = radius;
	}
	
	public Dot(int x, int y, int radius, Color color, Stroke stroke, boolean filled) {
		addPoint(x, y);
		setColor(color);
		setStroke(stroke);
		setFill(filled);
		this.radius = radius;
	}
	
	public int getRadius() {
		return radius;
	}
}
 
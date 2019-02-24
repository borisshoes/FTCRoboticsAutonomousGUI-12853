/**
 * @author Tyler Viarengo
 */

package shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

public class Line extends Shape{
	
	public Line(int x1, int y1, int x2, int y2) {
		addPoint(x1,y1);
		addPoint(x2,y2);
		setStroke(new BasicStroke(1));
		setColor(Color.BLACK);
	}
	
	public Line(int x1, int y1, int x2, int y2, Stroke stroke) {
		addPoint(x1,y1);
		addPoint(x2,y2);
		setStroke(stroke);
		setColor(Color.BLACK);
	}
	
	public Line(int x1, int y1, int x2, int y2, Color color) {
		addPoint(x1,y1);
		addPoint(x2,y2);
		setColor(color);
		setStroke(new BasicStroke(1));
	}
	
	public Line(int x1, int y1, int x2, int y2, Stroke stroke, Color color) {
		addPoint(x1,y1);
		addPoint(x2,y2);
		setStroke(stroke);
		setColor(color);
	}
}
 	

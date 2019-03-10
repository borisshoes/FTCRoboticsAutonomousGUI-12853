/**
 * @author Tyler Viarengo
 */

package shapes;

import java.awt.BasicStroke;
import java.awt.Color;

public class StartPoint extends Dot{
	
	private int angle;
	private int x;
	private int y;
	
	public StartPoint(int x, int y, int a) {
		super(x, y, 10, Color.CYAN, new BasicStroke(1), true);
		this.angle = a;
		this.x = x;
		this.y = y;
	}
	
	public int getAngle() {
		return this.angle;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
}
 
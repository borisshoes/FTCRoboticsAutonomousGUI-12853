/**
 * Virtual High School
 * @author Tyler Viarengo
 */

package shapes;
public class ProgramPoint {
	
	private int xPos;
	private int yPos;
	private double angle;
	
	public ProgramPoint(int x, int y, double a) {
		xPos = x;
		yPos = y;
		angle = a;
	}
	
	public int getX() {
		return xPos;
	}
	
	public int getY() {
		return yPos;
	}
	
	public double getAngle() {
		return angle;
	}
	
	public void setAngle(double a){
		angle = a;
	}
}
 
/**
 * @author Tyler Viarengo
 */

package commands;

import java.text.DecimalFormat;

public class Turn extends Command{
	public static final double DEGPERREV = 52;
	
	public double angle;
	public double angleRev;
	public double power = 1;
	
	public Turn(double angle) {
		this.angle = angle;
		calculate();
	}
	
	public void calculate() {
		angleRev = angle / DEGPERREV;
		
		DecimalFormat format = new DecimalFormat("#.###");
		String simpleAng = format.format(angleRev);
		
		command =  "turnRev("+power+", "+simpleAng+");";
	}
}
 
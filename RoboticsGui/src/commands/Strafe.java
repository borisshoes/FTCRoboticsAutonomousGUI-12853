/**
 * @author Tyler Viarengo
 */

package commands;

import java.text.DecimalFormat;

public class Strafe extends Command{
	public double PIXPERFOOT = 48;
	public double FTPERREV = 1.04716667;
	
	public double distancePix;
	public double distanceFt;
	public double distanceRev;
	public double power = 1;
	
	public Strafe(double distP, double PPF, double FPR) {
		PIXPERFOOT = PPF;
		FTPERREV = FPR;
		distancePix = distP;
		calculate();
	}
	
	public Strafe(int x1, int y1, int x2, int y2, double PPF, double FPR) {
		PIXPERFOOT = PPF;
		FTPERREV = FPR;
		distancePix = Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
		calculate();
	}
	
	public void calculate() {
		distanceFt = distancePix / PIXPERFOOT;
		distanceRev = distanceFt / FTPERREV;
		
		DecimalFormat format = new DecimalFormat("#.###");
		String simpleDist = format.format(distanceRev);
		
		command =  "strafeRev("+power+", "+simpleDist+");";
	}
}
 
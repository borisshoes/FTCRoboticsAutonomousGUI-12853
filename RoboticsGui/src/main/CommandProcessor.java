/**
 * Virtual High School
 * @author Tyler Viarengo
 */

package main;

import java.util.ArrayList;
import java.util.List;

import commands.Command;
import commands.Move;
import commands.Strafe;
import commands.Turn;

public class CommandProcessor {
	
	private List<ProgramPoint> points;
	private List<Command> commands = new ArrayList<Command>();
	
	private int strafeAngle;
	private int moveAngle = 10;
	private double currentAngle;
	
	public CommandProcessor(int strafeAngle) {
		this.strafeAngle = strafeAngle;
	}
	
	public void setPoints(List<ProgramPoint> list) {
		points = new ArrayList<ProgramPoint>(list);
	}
	
	public List<ProgramPoint> getPoints(){
		return points;
	}
	
	public List<Command> getCommands(){
		return commands;
	}
	
	public List<Command> process(){
		//System.out.println(strafeAngle);
		currentAngle = points.get(0).getAngle();
		
		for(int i = 0; i < points.size(); i++) {
			ProgramPoint point1 = points.get(i);
			if(i != points.size() - 1) {
				ProgramPoint point2 = points.get(i + 1);
				
				ArrayList<Command> subcommands = subprocess(point1, point2);
				
				for(Command e : subcommands) {
					commands.add(e);
				}
				
				
			}
		}
		
		outputCommands();
		return commands;
	}
	
	
	public ArrayList<Command> subprocess(ProgramPoint p1, ProgramPoint p2){
		ArrayList<Command> subcommands = new ArrayList<Command>();
		double deltaAngle = (currentAngle - p2.getAngle()) % 360;
		if(deltaAngle > 180) {
			deltaAngle = 360 - deltaAngle;
		}
		
		//System.out.println("dA: "+deltaAngle);
		double distance = Math.sqrt(Math.pow((p1.getX() - p2.getX()), 2) + Math.pow((p1.getY() - p2.getY()), 2));
		
		if(deltaAngle >= moveAngle || deltaAngle <= -moveAngle) {
			if((deltaAngle <= (-90 + strafeAngle) && deltaAngle >= (-90 - strafeAngle)) || (deltaAngle >= (90 - strafeAngle) && deltaAngle <= (90 + strafeAngle)) && strafeAngle != -1) { //Strafe
				//System.out.println("Strafe");
				if(deltaAngle < 0) {
					subcommands.add(new Strafe(distance));
				}else{
					subcommands.add(new Strafe(-distance));
				}
			}else if((deltaAngle <= (-180 + moveAngle) && deltaAngle >= (-180 - moveAngle)) || (deltaAngle >= (180 - moveAngle) && deltaAngle <= (180 + moveAngle))) { //Move backwards
				//System.out.println("Move Backwards");
				subcommands.add(new Move(-distance));
			}else{ //Turn and move
				currentAngle = p2.getAngle();
				//System.out.println("Turn and Move");
				subcommands.add(new Turn(deltaAngle));
				subcommands.add(new Move(distance));
			}
		}else{ //Move
			//System.out.println("Move");
			subcommands.add(new Move(distance));
		}
		
		
		
		return subcommands;
	}
	
	public void outputCommands() {
		for(Command e : commands) {
			System.out.println(e.command);
		}
	}
}
 
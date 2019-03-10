/**
 * Virtual High School
 * @author Tyler Viarengo
 */

package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class CommandWriter {
	
	List<String> data;
	String path;
	
	public CommandWriter(List<String> l, String path) {
		data = l;
		this.path = path;
	}
	
	public void write() {
		PrintWriter out = null;
		try {
			String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(Calendar.getInstance().getTime());
			String fullPath = path +"\\Code-Output_"+ timeStamp + ".txt";
			
			
			out = new PrintWriter(new BufferedWriter(new FileWriter(fullPath, true)));
			
			for(String elem : data) {
				out.println(elem);
			}
			System.out.println("Written to: "+fullPath);
		}catch(Exception e) {
			System.out.println("Error In Writing");
		}finally {
			out.close();
		}
	}
}
 
package edu.brown.cs.mldm.lsim2.frontend;

import java.util.UUID;

public class Poll {
	private static UUID id; 
	private static String author; 
	private static String title; 
	private static String location; 
	private static String date; 
	private static String msg;
	private static double[] coordinates; 
	
	public Poll(UUID id, String author, String title, String location, String date, String msg, double[] coordinates) {
		this.id = id; 
		this.author = author; 
		this.title = title; 
		this.location = location;
		this.date = date; 
		this.msg = msg; 
		this.coordinates = coordinates; 
	}
	
	public String getAuthor() {
		return this.author; 
	}
	public String getMeal() {
		return this.title; 
	}
	public String getLocation() {
		return this.location; 
	}
	public String getDate() {
		return this.date; 
	}
	public String getMsg() {
		return this.msg; 
	}
	
	public double[] getCoordinates() {
		return this.coordinates;
	}
}

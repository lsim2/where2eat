package edu.brown.cs.lsim2.frontend;

import java.util.UUID;

public class Poll {
	private static UUID id; 
	private static String author; 
	private static String title; 
	private static String location; 
	private static String date; 
	private static String msg; 
	
	public Poll(UUID id, String author, String title, String location, String date, String msg) {
		this.id = id; 
		this.author = author; 
		this.title = title; 
		this.location = location;
		this.date = date; 
		this.msg = msg; 
	}
}

package edu.brown.cs.mldm.lsim2.frontend;

import java.util.List;

public class Answer {
	private static String user;
	private static List<String> cuisine;
	private static int price;
	private static List<String> time;
	private static double distance;
	private static List<String> misc;
	private static List<String> restrictions;

	public Answer(String user, List<String> cuisine, List<String> restrictions, int price, List<String> time,
			double distance, List<String> misc) {
		this.user = user;
		this.cuisine = cuisine;
		this.price = price;
		this.time = time;
		this.distance = distance;
		this.misc = misc;
		this.restrictions = restrictions;
	}

}

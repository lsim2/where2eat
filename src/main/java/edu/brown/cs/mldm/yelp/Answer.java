package edu.brown.cs.mldm.yelp;

import java.util.List;

public class Answer {

	// private double rating;
	private String userId;
	private int price;
	private List<String> cuisine;
	private int radius;
	private double[] coordinates;
	private List<String> foodTerms;
	private List<String> restrictions;

	public Answer(String userId, List<String> cuisine, List<String> restrictions, List<String> foodTerms, int price,
			double[] coordinates, int radius/* , int rating */) {
		// this.rating = rating;
		this.price = price;
		this.cuisine = cuisine;
		this.radius = radius;
		this.coordinates = coordinates;
		this.foodTerms = foodTerms;
		this.restrictions = restrictions;
	}


	public List<String> getCuisine() {
		return cuisine;
	}

	public int getPrice() {
		return price;
	}

	public int getRadius() {
		return radius;
	}

	public double[] getCoordinates() {
		return coordinates;
	}

	public List<String> getFoodTerms() {
		return foodTerms;
	}

	public List<String> getRestrictions() {
		return restrictions;
	}

}

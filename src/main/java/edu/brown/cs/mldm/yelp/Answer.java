package edu.brown.cs.mldm.yelp;

import java.util.List;

public class Answer {

	//private double rating;
	private int price;
	private List<String> cuisine;
	private int radius;
	private double[] coordinates;
	//private double time;
	private List<String> foodTerms;
	
	public Answer(List<String> cuisine, List<String> foodTerms, int price,double[] coordinates, int radius/*, int rating*/){
		//this.rating = rating;
		this.price = price;
		this.cuisine = cuisine;
		this.radius = radius;
		//this.time = time;
		this.coordinates = coordinates;
		this.foodTerms= foodTerms;
	}
	
//	public double getRating(){
//		return rating;
//	}

	public List<String> getCuisine() {
		return cuisine;
	}

	public int getPrice() {
		return price;
	}

	public int getRadius() {
		return radius;
	}
//
//	public double getTime() {
//		return time;
//	}
	
	public double[] getCoordinates(){
		return coordinates;
	}
	
	public List<String> getFoodTerms(){
		return foodTerms;
	}

}
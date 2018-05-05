package edu.brown.cs.mldm.yelp;

import java.util.List;
import java.util.Map;

import edu.brown.cs.mldm.main.Server;

public class Answer {

	// private double rating;
	private String userId;
	private int price;
	private List<String> cuisine;
	private int radius;
	private double[] coordinates;
	private List<String> foodTerms;
	private List<String> restrictions;
	private String[] priceSigns = {"$", "$$", "$$$", "$$$$"};

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
	
	public String toString() {
	  StringBuilder sb = new StringBuilder();
	  sb.append("<h5>My Preferences:</h5>");
	  sb.append("<b>Pric</b>: " + priceSigns[price] + "<br>");
	  sb.append("<b>Distance</b>: " + (int) (radius/1609.34+1) + " miles<br>");
	  addToHTML(sb, cuisine, "Cuisines", Server.getCuisinesMap());
	  addToHTML(sb, restrictions, "Food Restrictions", Server.getRestrictionsMap());
	  addToHTML(sb, foodTerms, "Others", Server.getFoodTermsMap());
	  return sb.toString();
	}
	
	private void addToHTML(StringBuilder sb, List<String> list, String title, Map<String, String> map) {
      if (list.size() > 0) {
        sb.append("<b>" +title + "</b>" + ": ");
        for (int i = 0; i < list.size(); i++) {
          if (i == 0 ) {
            sb.append(map.get(list.get(i)));
          } else {
            sb.append(", " + map.get(list.get(i)));
          }
        }
        sb.append("<br>");
      }
    }

}

package edu.brown.cs.mldm.yelp;

import java.util.List;
import java.util.Map;

import edu.brown.cs.mldm.main.Server;

/**
 * This is the answer class, it's responsible for storing the users' responses
 * about their preferences. It has fields such as their id, the price, the
 * cuisine, etc.
 *
 */
public class Answer {

  // the different fields in a user's response.
  private String userId;
  private int price;
  private List<String> cuisine;
  private int radius;
  private double[] coordinates;
  private List<String> foodTerms;
  private List<String> restrictions;
  private String[] priceSigns = { "$", "$$", "$$$", "$$$$" };

  /**
   * the constructor takes in all the necessary fields.
   * 
   * @param userId
   *          a unique id per user.
   * @param cuisine
   *          the user's preferred cuisines(up to three)
   * @param restrictions
   *          the user's dietary restrictions.
   * @param foodTerms
   *          extra t
   * @param price
   * @param coordinates
   * @param radius
   */
  public Answer(String userId, List<String> cuisine, List<String> restrictions,
      List<String> foodTerms, int price,
      double[] coordinates, int radius) {
    this.userId = userId;
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

  public String toHTML() {
    StringBuilder sb = new StringBuilder();
    sb.append("<h5>" + userId + "'s Preferences:</h5>");
    sb.append("<b>Price</b>: " + priceSigns[price] + "<br>");
    sb.append(
        "<b>Distance</b>: " + Math.round(radius / 1609.34) + " miles<br>");
    addToHTML(sb, cuisine, "Cuisines", Server.getCuisinesMap());
    addToHTML(sb, restrictions, "Food Restrictions",
        Server.getRestrictionsMap());
    addToHTML(sb, foodTerms, "Others", Server.getFoodTermsMap());
    return sb.toString();
  }

  private void addToHTML(StringBuilder sb, List<String> list, String title,
      Map<String, String> map) {
    if (list.size() > 0) {
      sb.append("<b>" + title + "</b>" + ": ");
      for (int i = 0; i < list.size(); i++) {
        if (i == 0) {
          sb.append(map.get(list.get(i)));
        } else {
          sb.append(", " + map.get(list.get(i)));
        }
      }
      sb.append("<br>");
    }
  }

}

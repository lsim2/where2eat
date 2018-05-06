package edu.brown.cs.mldm.yelp;

import java.util.List;
import java.util.Map;

import edu.brown.cs.mldm.main.Server;

/**
 * Holds information about the user and their preferences.
 *
 */
public class Answer {

  // all the important fields about the user.
  private String userId;
  private int price;
  private List<String> cuisine;
  private int radius;
  private double[] coordinates;
  private List<String> foodTerms;
  private List<String> restrictions;
  private String[] priceSigns = { "$", "$$", "$$$", "$$$$" };

  /**
   * The constructor takes in all the information about the user.
   * 
   * @param userId
   *          the user's id
   * @param cuisine
   *          the user's prefered cuisines
   * @param restrictions
   *          the user's dietary restrictions
   * @param foodTerms
   *          the user's preferred foodterms that are not cuisines(i.e: chicken
   *          wings)
   * @param price
   *          the user's preferred price range
   * @param coordinates
   *          the user's location(the initial user's only)
   * @param radius
   *          the radius they're willing to travel
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

  /**
   * @return the list of preferred cuisines.
   */
  public List<String> getCuisine() {
    return cuisine;
  }

  /**
   * 
   * @return the user's price range.
   */
  public int getPrice() {
    return price;
  }

  /**
   * 
   * @return the radius they're willing to travel
   */
  public int getRadius() {
    return radius;
  }

  /**
   * @return the user's location(the initial user's only)
   */
  public double[] getCoordinates() {
    return coordinates;
  }

  /**
   * 
   * @return the user's food terms
   */
  public List<String> getFoodTerms() {
    return foodTerms;
  }

  /**
   * @return the user's dietary restrictions
   */
  public List<String> getRestrictions() {
    return restrictions;
  }

  /**
   * @return changes the user's preferences as a string to be displayed on the
   *         webpage.
   */
  public String toHTML() {
    StringBuilder sb = new StringBuilder();
    sb.append("<h5>" + userId + "'s Preferences:</h5>");
    sb.append("<b>Price</b>: " + priceSigns[price - 1] + "<br>");
    sb.append(
        "<b>Distance</b>: " + Math.round(radius / 1609.34) + " miles<br>");
    addToHTML(sb, cuisine, "Cuisines", Server.getCuisinesMap());
    addToHTML(sb, restrictions, "Food Restrictions",
        Server.getRestrictionsMap());
    addToHTML(sb, foodTerms, "Others", Server.getFoodTermsMap());
    return sb.toString();
  }

  /**
   * Helper method to change the user's preferences to an HTMl string.
   * 
   * @param sb
   *          the string builder used.
   * @param list
   *          the list to change the to a string
   * @param title
   *          describes the contents of the list, ie: cuisines, etc.
   * @param map
   *          the map to get the terms from.
   */
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
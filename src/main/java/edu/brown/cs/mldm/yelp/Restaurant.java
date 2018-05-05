package edu.brown.cs.mldm.yelp;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Restaurant {
  
  private static enum VOTE_TYPE {
    NONE, UP, DOWN
  }

  
  private String id;
  private String name;
  private Map<String, Object> location; // need to get rid of Object?
  private String price;
  private List<Map<String, String>> categories;
  private double rating;
  private Map<String, String> coordinates;
  private String is_closed;
  private String open_at;
  private double score = 0;
  private int intPrice = 0;
  private String image_url;
  private double dist = 0.0;
  private int upVotes = 0; 
  private int downVotes = 0;
  private int voteType = VOTE_TYPE.NONE.ordinal();
  
  public Restaurant() {
    score = this.getRating();
  }

  /**
   * Returns the unique business ID of the restaurant.
   * 
   * @return
   */
  public String getId() {
    return id;
  }

  /**
   * Returns the name of the restaurant.
   * 
   * @return
   */
  public String getName() {
    return name;
  }
  /**
   * This method will return the location of the restaurant: City, State (might
   * get rid of this)
   * 
   * @return
   */
  // public String getLocation(){
  // return location;
  // }

  /**
   * Returns an array of doubles of size 2 representing the lat/lon coordinates
   * of the restaurant. [lat, lon]
   * 
   * @return
   */
  public double[] getCoordinates() {

    double[] coords = new double[2];
    coords[0] = Double.parseDouble(coordinates.get("latitude"));
    coords[1] = Double.parseDouble(coordinates.get("longitude"));
    return coords;
  }

  /**
   * Returns a number between 1 and 4 depending on how expensive the restaurant
   * is. 1 - $ 2 - $$ 3 - $$$ 4 - $$$$
   * 
   * @return
   */
  public int getPrice() {

    if (price.length() > 0) {
      intPrice = price.length();
      return price.length();
    }
    return 0;
  }

  /**
   * Returns a set of the categories that the restaurant satisfies. eg: italian,
   * mexican, indpak, etc...
   * 
   * @return
   */
  public Set<String> getCategories() {
    Set<String> cat = new HashSet<>();
    for (Map<String, String> rest : categories) {
      cat.add(rest.get("alias"));

    }
    return cat;
  }

  /**
   * Returns a double between 0-5 depending on how well the restaurant is rated,
   * 0 is worst, 5 is best.
   */
  public double getRating() {
    return rating;
  }

  public String getAddress() {
    String addr = "";
    for (String str : (List<String>) location.get("display_address")) {
      addr = addr + " " + str;
    }
    return addr;
  }

  public boolean isOpen() {
    if (is_closed.equals("false")) {
      return true;
    } else {
      return false;
    }
  }

  public String getOpenAt() {
    return open_at;
  }

  @Override
  public String toString() {
    return "ID" + id + "Name: " + name;
  }

  /**
   * Compares true if restaurants have the same name
   *
   * @param obj
   * @return
   */
  @Override
  public boolean equals(Object rest) {

    // return this.getCoordinates()[0] == rest.getCoordinates()[0]
    // && this.getCoordinates()[1] == rest.getCoordinates()[1];
    return this.getId().equals(((Restaurant) rest).getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getId());
  }

  /**
   *
   * @return the score variable which increases depending on how highly ranked
   *         as a suggestion it is. Every restaurant's score starts off as its
   *         ranking and increases if it is a better match.
   */
  public double getScore() {
    return score;
  }

  /**
   * @param d
   *          the new score to be set.
   */
  public void setScore(double d) {
    this.score = d;
  }

  /**
   * Increments the score of this restaurant.
   */
  public void incrementScore() {
    score++;
  }

  public Set<String> getRestrictions() {
    Set<String> result = new HashSet<>();
    Set<String> restrictions = new HashSet<>(
        Arrays.asList(new String[] { "vegan", "vegetarian", "gluten_free",
            "halal", "kosher" }));
    Set<String> all = this.getCategories();
    for (String cat : all) {
      for (String res : restrictions) {
        if (cat.equals(res)) {
          result.add(res);
        }
      }
    }
    return result;
  }

  public String getImageUrl() {
    return image_url;
  }

  /**
   * @return the distance
   */
  public double getDistance() {
    return dist;
  }

  /**
   * @param distance
   *          the distance to set
   */
  public void setDistance(double distance) {
    this.dist = distance;
  }
  
  public int getUpVotes() {
    return this.upVotes;
  }
  
  public int getDownVotes() {
    return this.downVotes;
  }
  
  public void incrementUpVotes() {
    this.upVotes++;
  }

  public void incrementDownVotes() {
    this.downVotes++;
  }
  
  public int getNetVotes() {
    return this.upVotes - this.downVotes;
  }
  
  public int getVoteType() {
    return this.voteType;
  }
  
  public void resetVote() {
    this.voteType = VOTE_TYPE.NONE.ordinal();
  }

}

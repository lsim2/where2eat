package edu.brown.cs.mldm.yelp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * Takes in a list of restaurant which match every users preference. Processes
 * and rates them on a score keeping method which is then based on to decide the
 * top 10.
 */
public class Ranker {
  private static final int INC = 3;
  private static final int DEC = -3;

  /**
   * The constructor for the ranker
   */
  public Ranker() {

  }

  /**
   * This method is called when a user/users fills out a form and calls the
   * helper method which returns a list of the 10 best restaurants.
   *
   * @param suggestions
   *          the list of the top 30 suggestions which match every user's
   *          preferences.
   * @return the list of top 10 suggestions
   *
   */
  public List<Restaurant> rank(Map<Answer, List<Restaurant>> suggestions) {
    List<Restaurant> allRests = new ArrayList<Restaurant>();
    List<Restaurant> dupls = new ArrayList<Restaurant>();
    Set<Answer> keys = suggestions.keySet();
    for (Answer key : keys) {
      Iterator<Restaurant> currRests = suggestions.get(key).iterator();
      while (currRests.hasNext()) {
        Restaurant currRest = currRests.next();
        Restaurant nRest = isInList(allRests, currRest);
        if (nRest != null) {
          suggestions.get(key).remove(currRest);
          nRest.incrementScore();
          suggestions.get(key).add(nRest);
        }
      }
    }
    return rankBestMatches(suggestions, dupls);

  }

  /**
   * Sorts through the top 30 for every user and returns with a list of 3
   * restaurants which are the best match based on restrictions, cuisines,
   * foodterms, location and price.
   *
   *
   * @return the set of restaurants to use
   */
  public List<Restaurant> findTop10(List<Restaurant> list, Answer answer,
      boolean three) {

    // methods which increment a restaurant's score based on these criteria
    checkRestrictions(list, answer);
    checkCuisines(list, answer);
    checkFoodTerms(list, answer);
    checkPrice(list, answer);
    checkLocation(list, answer);
    list.sort(new PriceComparator());
    list.sort(new DistComparator(answer));
    list.sort(new ScoreComparator());
    List<Restaurant> ret = new ArrayList<Restaurant>();
    for (int a = 0; a < list.size() && a < 10; a++) {
      Restaurant rest = list.get(a);
      if (isInList(ret, rest) == null) {
        ret.add(rest);
      }
    }
    // increments the top restaurant so that it shows up in the list
    if (!ret.isEmpty()) {
      ret.get(0).setScore(ret.get(0).getScore() + INC);
    }

    return ret;
  }

  /**
   * This method is called by the rank method to find every top10
   * restaurant/user, then rank them to return the best options.
   *
   * @param bestRests
   *          the list of everyone's restaurant
   */
  private List<Restaurant> rankBestMatches(
      Map<Answer, List<Restaurant>> suggestions, List<Restaurant> dupls) {

    Set<Restaurant> bestMatches = new HashSet<Restaurant>();
    Set<Entry<Answer, List<Restaurant>>> suggs = suggestions.entrySet();

    // loops through to find everyone's top10 restaurants
    for (Entry<Answer, List<Restaurant>> sugg : suggs) {
      List<Restaurant> rests = new ArrayList<Restaurant>();
      if (suggs.size() > 1) {
        rests = findTop10(sugg.getValue(), sugg.getKey(), false);
      } else {
        rests = findTop10(sugg.getValue(), sugg.getKey(), true);
      }

      // increment the score of all restaurants which are in more than one
      // person's top 10.
      for (Restaurant curr : rests) {
        if (isInList(bestMatches, curr) != null) {
          curr.incrementScore();
        } else {
          bestMatches.add(curr);
        }
      }
    }

    // the list of everyone's preferences.
    List<Restaurant> finalMatches = new ArrayList<Restaurant>(bestMatches);
    finalMatches.sort(new ScoreComparator());

    List<Restaurant> ret = new ArrayList<Restaurant>();
    for (int a = 0; a < finalMatches.size() && a < 10; a++) {
      ret.add(finalMatches.get(a));
    }

    return ret;

  }

  /**
   * Method decrements the score of all restaurants in a person's top
   * restaurants which do not cater to their dietary restrictions, thus allowing
   * a person's top10 to mostly include those which cater to their dietary
   * restrictions.
   * 
   * @param allRests
   *          all the person's restaurants
   * @param ans
   *          the answer containing a person's preference
   */
  private void checkRestrictions(List<Restaurant> allRests,
      Answer ans) {
    List<Restaurant> toRemv = new ArrayList<Restaurant>();
    for (Restaurant rest : allRests) {
      if (!rest.getRestrictions().containsAll(ans.getRestrictions())) {
        rest.setScore(rest.getScore() + DEC);
      }
    }

  }

  /**
   * The comparator for restaurant's scores.
   * 
   * @author dmutako
   *
   */
  private class ScoreComparator implements Comparator<Restaurant> {
    @Override
    public int compare(Restaurant r1, Restaurant r2) {
      return Double.compare(r2.getScore(), r1.getScore());
    }
  }

  /**
   * The comparator for restaurant's prices.
   * 
   * @author dmutako
   *
   */
  private class PriceComparator implements Comparator<Restaurant> {

    @Override
    public int compare(Restaurant o1, Restaurant o2) {
      return Double.compare(o1.getPrice(), o2.getPrice());
    }

  }

  // potentially change this..
  public Restaurant isInList(Collection<Restaurant> restaurants,
      Restaurant rest) {
    for (Restaurant rst : restaurants) {
      if (rst.equals(rest)) {
        return rst;
      }
    }
    return null;
  }

  /**
   * The comparator for restaurant's distances from the initial user.
   * 
   * @author dmutako
   *
   */
  private class DistComparator implements Comparator<Restaurant> {
    private Answer ans;

    public DistComparator(Answer answer) {
      ans = answer;
    }

    @Override
    public int compare(Restaurant r1, Restaurant r2) {
      double r1Dist = sqDistance(r1, ans);
      double r2Dist = sqDistance(r2, ans);
      r1.setDistance(r1Dist);
      r2.setDistance(r2Dist);
      return Double.compare(r1Dist, r2Dist);
    }
  }

  /**
   * Calculates the square distance from the restaurant to the answer's
   * location.
   * 
   * @param rest
   *          the restaurant used
   * @param ans
   *          the answer containing the answerer's location
   * @return the square distance between the answerer's location and restaurant.
   */
  private double sqDistance(Restaurant rest, Answer ans) {

    double distance = ((rest.getCoordinates()[0] - ans.getCoordinates()[0])
        * (rest.getCoordinates()[0] - ans.getCoordinates()[0]))
        + ((rest.getCoordinates()[1] - ans.getCoordinates()[1])
            * (rest.getCoordinates()[1] - ans.getCoordinates()[1]));
    return distance;

  }

  /**
   * For every cuisine, increment the score of a restaurant which has that
   * cuisine.
   * 
   * @param rests
   *          all the restaurants matching a user's preference
   * @param answer
   *          the answer indicating a person's preferences.
   */
  private void checkCuisines(List<Restaurant> rests, Answer answer) {
    for (String cuisine : answer.getCuisine()) {
      for (Restaurant rest : rests) {
        for (String category : rest.getCategories()) {
          if (cuisine.equals(category)) {
            rest.incrementScore();
          }
        }
      }
    }
  }

  /**
   * This method increments the top 10 closest restaurants.
   * 
   * @param rests
   *          all the user's restaurants.
   * @param answer
   *          the answer indicating the user's location.
   */
  private void checkLocation(List<Restaurant> rests, Answer answer) {
    rests.sort(new DistComparator(answer));
    int a = 10;
    for (int i = 0; i < rests.size() && i < 10; i++) {
      rests.get(i).setScore(rests.get(i).getScore() + a);
      a--;
    }

  }

  /**
   * This method increments the top 10 cheapest restaurants.
   * 
   * @param rests
   *          all the user's restaurants.
   * @param answer
   *          the answer indicating the user's location.
   */
  private void checkPrice(List<Restaurant> rests, Answer answer) {
    rests.sort(new PriceComparator());
    for (int i = 0; i < rests.size() && i < 10; i++) {
      rests.get(i).incrementScore();
    }

  }

  /**
   * For every answerer's foodterm, increment the score of a restaurant which
   * has it.
   * 
   * @param rests
   *          All the answerer's top 30 restaurants.
   * @param answer
   *          the answer indicating the user's foodterms.
   */
  private void checkFoodTerms(List<Restaurant> rests, Answer answer) {
    for (String foodTerm : answer.getFoodTerms()) {
      for (Restaurant rest : rests) {
        for (String category : rest.getCategories()) {
          if (foodTerm.equals(category)) {
            rest.incrementScore();
          }
        }
      }
    }
  }

  /**
   * Called in the server so that it can sort restaurants by price, or distance.
   * 
   * @param sort_type
   *          string indicating whether to sort by price or distance
   * @param rests
   *          the list to sort
   * @param ans
   *          the answer whose distance to use
   * @return the sorted list of restaurants
   */
  public List<Restaurant> sortRests(String sort_type, List<Restaurant> rests,
      Answer ans) {
    if (sort_type.equals("price")) {
      List<Restaurant> ret = new ArrayList<Restaurant>(rests);
      ret.sort(new PriceComparator());
      return ret;
    } else if (sort_type.equals("distance")) {
      List<Restaurant> ret = new ArrayList<Restaurant>(rests);
      ret.sort(new DistComparator(ans));
      return ret;
    }
    return rests;
  }

}

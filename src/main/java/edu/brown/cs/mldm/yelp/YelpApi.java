package edu.brown.cs.mldm.yelp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;

/**
 * This class handles getting and filtering out results of API requests to the
 * YelpApi.
 * 
 * @author mounikadandu
 *
 */

public class YelpApi {
  private String apiKey; // yelp api key
  private static final String NORMAL_LIMIT = "30"; // num suggestions per person
  private static final String SORT = "best_match"; // how to sort yelp re
  private static final Gson GSON = new Gson();

  public YelpApi(String key) {
    apiKey = key;
  }

  /**
   * This method takes in a list of answers and returns a map that from each
   * answer to a list of relevant restaurants for that answer. This map is then
   * given to the ranking algorithm to get the best possible restaurants for the
   * entire group.
   * 
   * @param answers
   *          list of answers
   * @return map of answers to restaurants for that answer.
   */
  public Map<Answer, List<Restaurant>> getPossibleRestaurants(
      List<Answer> answers) {
    Map<Answer, List<Restaurant>> results = new HashMap<>();
    if (answers == null || answers.size() == 0) {
      return results;
    }
    for (Answer answer : answers) {
      List<Restaurant> smallSet = this.getRestaurantSet(answer);
      // results.add(smallSet);

      results.put(answer, smallSet);
    }

    return results;
  }

  /**
   * This method takes in an instance of the Answer class and uses the
   * preferences specified in the Answer to search for relevant restaurant
   * results. It calls the make request method on the url that is built up based
   * on the specified preferences. If no radius is specified or itis invalid,
   * the default radius is set. The same is done for price. It looks through all
   * cuisine, food and restriction preferences and adds these to the url as
   * categories that should be searched for by the Yelp Api. It also includes
   * the users location. It then makes a request and returns the list of
   * restaurants that are possible matches for that particular answer. It tries
   * to satisy as many preferences as possible and returns the list in order of
   * best match.
   * 
   * @param answer
   *          Answer for a single user.
   * @return list of possible restaurants for that given user.
   */
  public List<Restaurant> getRestaurantSet(Answer answer) {
    // String location = "Providence, RI";
    String terms = "restaurants"; // makes sure that search results only include
                                  // restaurants, no other types of businesses
    String price = "1"; // price 1 = $, 2 = $$, 3 = $$$, 4 = $$$$
    String radius = "39999"; // ~25 miles - max radius
    String categories = "";

    List<Restaurant> restaurants = new ArrayList<>();
    if (answer.getCoordinates() == null || answer.getFoodTerms() == null
        || answer.getCuisine() == null) {
      System.out.println("ERROR: Answer attributes cannot be null!");
      return restaurants;
    }

    double[] coordinates = answer.getCoordinates();

    StringBuilder url = new StringBuilder();
    StringBuilder backupurl = new StringBuilder();

    url.append("term=" + terms);
    url.append("&latitude=" + coordinates[0] + "&longitude=" + coordinates[1]);
    backupurl.append("term=" + terms);
    backupurl
        .append("&latitude=" + coordinates[0] + "&longitude=" + coordinates[1]);

    // if price is specified, search for results at that price level and below.
    if (answer.getPrice() > 0) {
      int p = answer.getPrice();
      if (p == 1) {
        url.append("&price=" + "1");
        backupurl.append("&price=" + "1");
        price = "1";
      } else if (p == 2) {
        url.append("&price=" + "1,2");
        backupurl.append("&price=" + "1,2");
        price = "2";
      } else if (p == 3) {
        url.append("&price=" + "1,2,3");
        backupurl.append("&price=" + "1,2,3");
        price = "3";
      }
    }
    // if answer contains a valid radius preference, add radius to url to be
    // searched by
    if (answer.getRadius() > 0) {
      radius = Integer.toString(answer.getRadius());
    }

    // if the user entered any food preferencs,
    if (!answer.getCuisine().isEmpty() || !answer.getRestrictions().isEmpty()
        || !answer.getFoodTerms().isEmpty()) {

      // add all cuisine preferences to list of categories to be searched
      if (!answer.getCuisine().isEmpty()) {
        for (String category : answer.getCuisine()) {
          categories = categories + category + ",";
        }
      }
      // add all dietary restrictions to the url so that they can be searched
      // for
      if (!answer.getRestrictions().isEmpty()) {
        for (String category : answer.getRestrictions()) {
          categories = categories + category + ",";
        }
      }

      // add all food preferences to list of categories to be searched
      if (!answer.getFoodTerms().isEmpty()) {
        for (String term : answer.getFoodTerms()) {
          categories = categories + term + ",";
        }

      }
      if (categories.charAt(categories.length() - 1) == ',') {
        categories = categories.substring(0, categories.length() - 1);
      }
      url.append("&categories=" + categories);

    }

    url.append("&radius=" + radius);
    backupurl.append("&radius=" + radius);

    List<Restaurant> originalReq = this.makeRequest(url.toString());
    if (originalReq.size() > 0) {
      return originalReq;
    } else {
      return this.makeRequest(backupurl.toString());
    }

  }

  /**
   * This method makes a request to the YelpApi given a URL that encodes a set
   * of restaurant preferences and specifications such as location, radius in
   * which to search, cuisine/food preferences, price range, etc. It then takes
   * each result, converts it to a Restaurant Object and adds it to the results
   * list. A list of Restaurants is then returned sorted by best match.
   * 
   * @param requestURL
   *          url for querying yelp api
   * @return List of restaurants that match the criteria.
   */
  public List<Restaurant> makeRequest(String requestURL) {
    OkHttpClient client2 = new OkHttpClient();
    List<Restaurant> results = new ArrayList<>();
    Request request2 = new Builder()
        .url("https://api.yelp.com/v3/businesses/search?" + requestURL
            + "&limit=" + NORMAL_LIMIT + "&sort_by="
            + SORT)
        .get().addHeader("authorization", "Bearer" + " " + apiKey)
        .addHeader("cache-control", "no-cache")
        .addHeader("postman-token", "b5fc33ce-3dad-86d7-6e2e-d67e14e8071b")
        .build();

    try {
      Response response2 = client2.newCall(request2).execute();

      JsonObject jsonObject = (JsonObject) new JsonParser()
          .parse(response2.body().string().trim());
      // gets information about the business (restaurant) from Api result.
      JsonArray myResponse = jsonObject.get("businesses").getAsJsonArray();

      // for each suggestion in the result from the Api, converts to a
      // Restaurant object and adds to results list
      for (JsonElement str : myResponse) {
        Restaurant rest = GSON.fromJson(str.getAsJsonObject(),
            Restaurant.class);
        results.add(rest);
      }

    } catch (IOException e) {
      System.out.println("ERROR: Error making database query!");
    } catch (NullPointerException e) {
      System.out.println("ERROR: Error making database query!");
    }
    return results;

  }

}

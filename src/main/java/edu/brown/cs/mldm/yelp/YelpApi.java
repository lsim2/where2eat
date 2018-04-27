package edu.brown.cs.mldm.yelp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;

// using AOuth 2.0 to access Yelp Fusion api

public class YelpApi {
	private String apiKey;
	private static final String NORMAL_LIMIT = "30"; // suggestions per person
	private static final String SORT = "distance"; // could also be rating, number of reviews, distance or
	private static final Gson GSON = new Gson();

	public YelpApi(String key) {
		apiKey = key;
	}
	public Map<Answer,List<Restaurant>> getPossibleRestaurants(List<Answer> answers){
		Map<Answer, List<Restaurant>> results = new HashMap<>();
		if (answers == null || answers.size() == 0) {
			return results;
		}
		for(Answer answer: answers){
				List<Restaurant> smallSet = this.getRestaurantSet(answer);
				//results.add(smallSet);
				results.put(answer, smallSet);
		}

		return results;
	}

	public List<Restaurant> getRestaurantSet(Answer answer) {
		// String location = "Providence, RI";
		String terms = "restaurants";
		String price = "1"; // price 1 = $, 2 = $$, 3 = $$$, 4 = $$$$
		String radius = "40000";
		String categories = "";

		List<Restaurant> restaurants = new ArrayList<>();
		if (answer.getCoordinates() == null || answer.getFoodTerms() == null || answer.getCuisine() == null) {
			System.out.println("ERROR: Answer attributes cannot be null!");
			return restaurants;
		}

		double[] coordinates = answer.getCoordinates();

		StringBuilder url = new StringBuilder();
		
		if(!answer.getFoodTerms().isEmpty()){
			for(String term: answer.getFoodTerms()){
				terms = terms + "," + term  ;
			}

			if (terms.charAt(terms.length() - 1) == ',') {
				terms = terms.substring(0, terms.length() - 1);
			}
		}

		url.append("term=" + terms);
		url.append("&latitude=" + coordinates[0] + "&longitude=" + coordinates[1]);

		if (answer.getPrice() > 0) { // if price is specified
			int p = answer.getPrice();
			if (p == 1) {
				url.append("&price=" + "1");
				price = "1";
			} else if (p == 2) {
				url.append("&price=" + "1,2");
				price = "2";
			} else if (p == 3) {
				url.append("&price=" + "1,2,3");
				price = "3";
			}
		}
		if (answer.getRadius() >= 0) {
			radius = Integer.toString(answer.getRadius());
		}

		if (!answer.getCuisine().isEmpty() || !answer.getRestrictions().isEmpty()) {
			if (!answer.getCuisine().isEmpty()) {
				for (String category : answer.getCuisine()) {
					categories = categories + category + ",";
				}
			}
			if (!answer.getRestrictions().isEmpty()) {
				for (String category : answer.getRestrictions()) {
					categories = categories + category + ",";
				}
			}

			if (categories.charAt(categories.length() - 1) == ',') {
				categories = categories.substring(0, categories.length() - 1);
			}
			url.append("&categories=" + categories);

		}

		url.append("&radius=" + radius);
		System.out.println(url);
		return this.makeRequest(url.toString());
	}

	public List<Restaurant> makeRequest(String requestURL) {
		OkHttpClient client2 = new OkHttpClient();
		List<Restaurant> results = new ArrayList<>();
		Request request2 = new Builder()
				.url("https://api.yelp.com/v3/businesses/search?" + requestURL + "&limit=" + NORMAL_LIMIT + "&sort_by="
						+ SORT)
				.get().addHeader("authorization", "Bearer" + " " + apiKey).addHeader("cache-control", "no-cache")
				.addHeader("postman-token", "b5fc33ce-3dad-86d7-6e2e-d67e14e8071b").build();

		try {
			Response response2 = client2.newCall(request2).execute();

			JsonObject jsonObject = (JsonObject) new JsonParser().parse(response2.body().string().trim()); // parser
			JsonArray myResponse = jsonObject.get("businesses").getAsJsonArray();

			for (JsonElement str : myResponse) {

				Restaurant rest = GSON.fromJson(str.getAsJsonObject(), Restaurant.class);
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

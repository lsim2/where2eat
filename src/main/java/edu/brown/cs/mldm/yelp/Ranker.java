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
 * Rank the top 3 restaurants for every user
 *
 * Takes in a list of restaurant and preferences For every restaurant increase
 * the ranking if it shows up in the list of restaurants more than once increase
 * it's ranking Rank the top 3 and if a restaurant shows up more than once in
 * the top three: increase its ranking for every time it's encountered
 *
 *
 * Rank the 3n restaurants + those which appear more than once(if they're not
 * already included) to find the top 5
 *
 *
 * 
 * 
 * restaurant which appears first in its cuisine gets an increment,
 * 
 * If using google api, use it to rank a restaurant per cuisine Location sorter,
 *
 */
public class Ranker {
	private static final int INC = 10;

	public Ranker() {

	}

	/**
	 * This method ranks a group's rest
	 *
	 * @param suggestions
	 *            the list of the top 30 suggestions which match every user's
	 *            preferences.
	 * @return the list of top 5 suggestions
	 *
	 *         Test the lack of duplicates in
	 */
	public List<Restaurant> rank(Map<Answer, List<Restaurant>> suggestions) {
		// keep track of duplicate rests.
		List<Restaurant> allRests = new ArrayList<Restaurant>();
		List<Restaurant> dupls = new ArrayList<Restaurant>();
		Set<Answer> keys = suggestions.keySet();

		for (Answer key : keys) {
			Iterator<Restaurant> currRests = suggestions.get(key).iterator();
			if (currRests.hasNext()) {

				while (currRests.hasNext()) {
					Restaurant currRest = currRests.next();
					Restaurant nRest = isInList(allRests, currRest);
					if (nRest != null) {
						suggestions.get(key).remove(currRest);
						nRest.incrementScore();
						//suggestions.get(key).add(nRest);
						//dupls.add(nRest);
					}
				}
				// ensures it was not emptied by iteration
				assert !suggestions.get(key).isEmpty();
			}
		}

		return rankBestMatches(suggestions, dupls);

	}

	/**
	 * sorts through the top 30 for every user and returns with a list of 3
	 * restaurants which are the best match?
	 *
	 * maybe increase everyone's top rest by 2,
	 *
	 * How do I do this ranking?[FILL THIS IN]
	 *
	 * @return the set of restaurants to use
	 */
	public Set<Restaurant> findTop5(List<Restaurant> list, Answer answer, boolean three) {
		List<Restaurant> sortedRests = new ArrayList<Restaurant>(list);
		checkRestrictions(sortedRests, answer);
		sortedRests.sort(new PriceComparator());
		sortedRests.sort(new DistComparator(answer));
		checkCuisines(sortedRests, answer);
		// check whether in price range
		Set<Restaurant> ret = new HashSet<Restaurant>();
		for (int a = 0; a < 5 && a < sortedRests.size(); a++) {
			if (a == 0) {
				if (!answer.getRestrictions().isEmpty()) {
					sortedRests.get(a).setScore(sortedRests.get(a).getScore() + INC);

				} else {
					sortedRests.get(a).incrementScore();
				}
			}
			assert Math.sqrt(sqDistance(sortedRests.get(a), answer)) < answer.getRadius();
			sortedRests.get(a).incrementScore();

			ret.add(sortedRests.get(a));
		}
		return ret;
	}

	/**
	 * This method takes in a list of top 3 best restaurants and increases the score
	 * of the ones with a best match [HOW DO I DO THIS?? ]: Sort if the first top 5
	 * tie, make sure that everyone's top is included,
	 *
	 * @param bestRests
	 *            the list of everyone's restaurant
	 */
	private List<Restaurant> rankBestMatches(Map<Answer, List<Restaurant>> suggestions, List<Restaurant> dupls) {

		Set<Restaurant> bestMatches = new HashSet<Restaurant>();
		Set<Entry<Answer, List<Restaurant>>> suggs = suggestions.entrySet();
		// Call findTop3 this removes similar restaurants change this..
		for (Entry<Answer, List<Restaurant>> sugg : suggs) {
			Set<Restaurant> rests = new HashSet<Restaurant>();
			if (suggs.size() > 1) {
				rests = findTop5(sugg.getValue(), sugg.getKey(), false);
			} else {
				rests = findTop5(sugg.getValue(), sugg.getKey(), true);
			}
			for (Restaurant curr : rests) {
				if (isInList(bestMatches, curr) != null) {
					curr.incrementScore();
				}
				bestMatches.add(curr);
			}
		}
//		for (Restaurant curr : dupls) {
//			if (isInList(bestMatches, curr) == null) {
//				// just for testing
//				bestMatches.add(curr);
//			}
//		}
		List<Restaurant> finalMatches = new ArrayList<Restaurant>(bestMatches);

		// make sure that the scores are actually being incremented
		finalMatches.sort(new ScoreComparator());
		for(Restaurant rest: finalMatches){
			System.out.println(rest.getName() + " " + rest.getScore());
		}
		List<Restaurant> ret = new ArrayList<Restaurant>();
		for (int a = 0; a < finalMatches.size() && a < 5; a++) {
			ret.add(finalMatches.get(a));
		}
		
		
		return ret;

	}

	/**
	 * Method removes all restaurants in a person's top restaurants which do not
	 * cater to their dietary restrictions, thus allowing a person's top 3 to only
	 * include those in the top 5.
	 * 
	 * @param allRests
	 *            all the person's restaurants
	 * @param ans
	 *            the answer containing a person's preference
	 * @return the edited list of restaurants.
	 */
	private List<Restaurant> checkRestrictions(List<Restaurant> allRests, Answer ans) {
		List<Restaurant> toRemv = new ArrayList<Restaurant>();
		for (Restaurant rest : allRests) {
			if (!rest.getRestrictions().containsAll(ans.getRestrictions())) {
				toRemv.add(rest);
			}
		}
		allRests.removeAll(toRemv);

		return allRests;

	}

	private class ScoreComparator implements Comparator<Restaurant> {
		@Override
		public int compare(Restaurant r1, Restaurant r2) {
			return Double.compare(r1.getScore(), r2.getScore());
		}
	}

	private class PriceComparator implements Comparator<Restaurant> {

		@Override
		public int compare(Restaurant o1, Restaurant o2) {
			return Double.compare(o1.getPrice(), o2.getPrice());
		}

	}

	// potentially change this..
	public Restaurant isInList(Collection<Restaurant> restaurants, Restaurant rest) {
		for (Restaurant rst : restaurants) {
			if (rst.equals(rest)) {
				return rst;
			}
		}
		return null;
	}

	private class DistComparator implements Comparator<Restaurant> {
		private Answer ans;

		public DistComparator(Answer answer) {
			ans = answer;
		}

		@Override
		public int compare(Restaurant r1, Restaurant r2) {
			return Double.compare(sqDistance(r1, ans), sqDistance(r2, ans));
		}
	}

	private double sqDistance(Restaurant rest, Answer ans) {

		double distance = ((rest.getCoordinates()[0] - ans.getCoordinates()[0])
				* (rest.getCoordinates()[0] - ans.getCoordinates()[0]))
				+ ((rest.getCoordinates()[1] - ans.getCoordinates()[1])
						* (rest.getCoordinates()[1] - ans.getCoordinates()[1]));
		return distance;

	}

	/**
	 * For every cuisine, increment the score of a restaurant
	 * 
	 * @param rests
	 * @param answer
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

	// called in server
	public List<Restaurant> sortRests(String sort_type, List<Restaurant> rests, Answer ans) {
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
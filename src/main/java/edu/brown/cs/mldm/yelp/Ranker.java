package edu.brown.cs.mldm.yelp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
 * Restaurants in the order
 *
 * Ranking the top 3, based
 *
 *
 */
public class Ranker {
	private static final int INC = 30;

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
	public List<Restaurant> rank(HashMap<Answer, Set<Restaurant>> suggestions) {
		// Go through the set and increase the score of any restaurant which appears
		// more than once[NOT DONE] if a restaurant shows up more than once,
		// increment its score, replace it with that particular restaurant, ensure
		// they're the same, keep track of these restaurants, add it to the ranking
		List<Restaurant> allRests = new ArrayList<Restaurant>();
		List<Restaurant> dupls = new ArrayList<Restaurant>();
		Set<Answer> keys = suggestions.keySet();
		for (Answer key : keys) {
			Iterator<Restaurant> currRests = suggestions.get(key).iterator();
			Restaurant currRest = currRests.next();
			while (currRest != null) {
				Restaurant nRest = isInList(allRests, currRest);
				if (nRest != null) {
					suggestions.get(key).remove(currRest);
					nRest.incrementScore();
					suggestions.get(key).add(nRest);
					dupls.add(nRest);
				}
				currRest = currRests.next();
			}
			// ensures it was not emptied by iteration
			assert !suggestions.get(key).isEmpty();
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
	public Set<Restaurant> findTop3(Set<Restaurant> prefRestaurants, Answer answer) {
		List<Restaurant> sortedRests = new ArrayList<Restaurant>(prefRestaurants);
		checkRestrictions(sortedRests);
		sortedRests.sort(new PriceComparator());
		// check whether in price range
		Set<Restaurant> ret = new HashSet<Restaurant>();
		for (int a = 0; a < 3; a++) {
			if (a == 0) {
				// double check with Lina
				if (!answer.getRestrictions().isEmpty()) {
					sortedRests.get(a).setScore(sortedRests.get(a).getScore() + INC);

				} else {
					sortedRests.get(a).incrementScore();
				}
			}
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
	private List<Restaurant> rankBestMatches(HashMap<Answer, Set<Restaurant>> suggestions, List<Restaurant> dupls) {
		// check dietary restrictions[KEEP A LIST OF DIETARY RESTRICTIONS]
		// then sort and return
		// list of the 3 best matches per user
		Set<Restaurant> bestMatches = new HashSet<Restaurant>();
		Set<Entry<Answer, Set<Restaurant>>> suggs = suggestions.entrySet();
		// Call findTop3 this removes similar restaurants change this..
		for (Entry<Answer, Set<Restaurant>> sugg : suggs) {
			Set<Restaurant> rests = findTop3(sugg.getValue(), sugg.getKey());
			for (Restaurant curr : rests) {
				if (isInList(bestMatches, curr) != null) {
					curr.incrementScore();
				}
				bestMatches.add(curr);
			}
		}

		for (Restaurant curr : dupls) {
			if (isInList(bestMatches, curr) == null) {
				// just for testing
				bestMatches.add(curr);
			}
		}
		List<Restaurant> finalMatches = new ArrayList<Restaurant>(bestMatches);

		// make sure that the scores are actually being incremented
		finalMatches.sort(new ScoreComparator());
		List<Restaurant> ret = new ArrayList<Restaurant>();
		for (int a = 0; a < finalMatches.size(); a++) {
			ret.add(finalMatches.get(a));
		}
		return ret;

	}

	private void checkRestrictions(List<Restaurant> allRests) {
		// if it has dietary restrictions increase its scores by like 30 cannot make all
		// the restaurants, if a dietary restriction matches a preference, only on
		// increse

	}

	private class ScoreComparator implements Comparator<Restaurant> {
		@Override
		public int compare(Restaurant r1, Restaurant r2) {
			return Integer.compare(r1.getScore(), r2.getScore());
		}
	}

	private class PriceComparator implements Comparator<Restaurant> {

		@Override
		public int compare(Restaurant o1, Restaurant o2) {
			return Integer.compare(o2.getPrice(), o1.getPrice());
		}

	}

	private Restaurant isInList(Collection<Restaurant> restaurants, Restaurant rest) {
		for (Restaurant rst : restaurants) {
			if (rst.equals(rest)) {
				return rst;
			}
		}
		return null;
	}

}

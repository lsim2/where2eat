// package edu.brown.cs.mldm.rankingTest;
//
// import static org.junit.Assert.assertTrue;
//
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.Set;
//
// import org.junit.Test;
//
// import edu.brown.cs.mldm.yelp.Answer;
// import edu.brown.cs.mldm.yelp.Ranker;
// import edu.brown.cs.mldm.yelp.Restaurant;
// import edu.brown.cs.mldm.yelp.YelpApi;
//
//// include location per person
//// cards adding and removing restaurant or like ranking considerably reduced
//// use info on what was removed to rank
// public class RankerTest {
// private YelpApi testApi = new YelpApi(
// "F291TzGm16HMb6ZoMS4j1azreUQJq9PHCLjoeNPcS33pOntIqSRpC-aO-cXuQ_8O2O8TM-RohICpxPAzXuTekf-T7i2KtymLLCUTwTyEqJKsLm3XzmuWTSKPbzLdWnYx");
// private Ranker testRk = new Ranker();
//
// @Test
// public void testOnceAnswer() {
// // create ranker, create fake answer, call both of those classes
// // make the top 3 method public
// List<String> cuisine = new ArrayList<String>();
// cuisine.add("mexican");
// List<String> restrictions = new ArrayList<String>();
// double[] rockLoc = { 41.8251002, -71.4028338 };
// List<String> fT = new ArrayList<String>();
// // Answer testAns = new Answer(cuisine, fT, 2, rockLoc, 1000, restrictions);
//
// Map<String, Answer> answers = new HashMap<String, Answer>();
// answers.put("1", testAns);
// Map<Answer, Set<Restaurant>> possRest =
// testApi.getPossibleRestaurants(answers);
// List<Restaurant> res = testRk.rank(possRest);
// assertTrue(res.size() == 5);
// // resPrinter(res);
// assertTrue(containsRest(res, "bajas taqueria"));
// }
//
// @Test
// public void testMultiple() {
// Answer testAns = createAnswer("sandwiches", 3, new ArrayList<String>());
// Answer test2 = createAnswer("chinese", 2, new ArrayList<String>());
// Answer test3 = createAnswer("Fast Food", 3, Arrays.asList("vegan"));
// Map<String, Answer> answers = new HashMap<String, Answer>();
// answers.put("1", testAns);
// answers.put("2", test2);
// answers.put("3", test3);
// Map<Answer, Set<Restaurant>> possRest =
// testApi.getPossibleRestaurants(answers);
// List<Restaurant> res = testRk.rank(possRest);
// resPrinter(res);
// assertTrue(res.size() == 5);
// }
//
// private void printer(Map<Answer, Set<Restaurant>> possRests) {
// Set<Answer> keySet = possRests.keySet();
// for (Answer currKe : keySet) {
// System.out.println(possRests.get(currKe).size() + " Size: ");
// for (Restaurant currRes : possRests.get(currKe)) {
// System.out.println(currRes.getName() + " :fjjjjjjjjjjjjjjjjjjjjjjjj");
// }
// }
// }
//
// // private Answer createAnswer(String cuisine, int p, List<String> restr) {
// // double[] rockLoc = { 41.8251002, -71.4028338 };
// // List<String> cui = Arrays.asList(cuisine);
// // //return new Answer(cui, new ArrayList<String>(), p, rockLoc, 1000,
// restr);
// // }
//
// private void resPrinter(List<Restaurant> res) {
// for (Restaurant curr : res) {
// System.out.println(curr.getName() + " :
// fhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
// }
// }
//
// private boolean containsRest(List<Restaurant> rests, String resName) {
// for (Restaurant rest : rests) {
// if (rest.getName().equals(resName)) {
// return true;
// }
// }
// return false;
// }
// }

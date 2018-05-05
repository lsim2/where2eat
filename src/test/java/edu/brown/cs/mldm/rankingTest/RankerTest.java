//
// package edu.brown.cs.mldm.rankingTest;
//
// import static org.junit.Assert.assertTrue;
//
// import java.util.ArrayList;
// import java.util.Arrays;
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
////// include location per person[DONE]
////// include everyone's top cuisine restaurant[WORKING ON IT]
////// figure out how to use Mounika's google api[CAN'T DO IT]
////// cards adding and removing restaurant or like ranking considerably reduced
////// use info on what was removed to rank
// public class RankerTest {
// private YelpApi testApi = new YelpApi(
// "F291TzGm16HMb6ZoMS4j1azreUQJq9PHCLjoeNPcS33pOntIq"
// + "SRpC-aO-cXuQ_8O2O8TM-RohICpxPAzXuTekf-T7i2Ktym"
// + "LLCUTwTyEqJKsLm3XzmuWTSKPbzLdWnYx");
// private Ranker testRk = new Ranker();
//
// //
// //@Test
// public void testOnceAnswer() {
//
// List<String> cuisine = new ArrayList<String>();
// // cuisine.add("mexican");
// cuisine.add("chinese");
// // cuisine.add("sandwiches");
// List<String> restrictions = new ArrayList<String>();
// double[] rockLoc = { 41.8251002, -71.4028338 };
// List<String> fT = new ArrayList<String>();
// Answer testAns = new Answer("1", cuisine, fT, restrictions, 3, rockLoc,
// 6000);
//
// List<Answer> answers = new ArrayList<Answer>();
// answers.add(testAns);
// Map<Answer, List<Restaurant>> possRest = testApi
// .getPossibleRestaurants(answers);
// List<Restaurant> res = testRk.rank(possRest);
// resPrinter(res);
// // assertTrue(res.size() == 10);
// }
//
// // @Test
// public void testMultiple() {
// Answer testAns = createAnswer("sandwiches", 3, new ArrayList<String>());
// Answer test2 = createAnswer("chinese", 2, new ArrayList<String>());
// Answer test3 = createAnswer("Fast Food", 3, Arrays.asList("vegan"));
// List<Answer> answers = new ArrayList<Answer>();
// answers.add(testAns);
// answers.add(test2);
// answers.add(test3);
// Map<Answer, List<Restaurant>> possRest = testApi
// .getPossibleRestaurants(answers);
// List<Restaurant> res = testRk.rank(possRest);
// // RankerTest.resPrinter(res);
// assertTrue(res.size() == 5);
// }
//
// //
// // //@Test
// public void testMultiple1() {
// Answer testAns = createAnswer("sandwiches", 3, new ArrayList<String>());
// Answer test2 = createAnswer("chinese", 2, new ArrayList<String>());
// Answer test3 = createAnswer("korean", 3, Arrays.asList("vegan"));
// List<Answer> answers = new ArrayList<Answer>();
// answers.add(testAns);
// answers.add(test2);
// answers.add(test3);
// Map<Answer, List<Restaurant>> possRest = testApi
// .getPossibleRestaurants(answers);
// List<Restaurant> res = testRk.rank(possRest);
// // RankerTest.resPrinter(res);
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
// private Answer createAnswer(String cuisine, int p, List<String> restr) {
// double[] rockLoc = { 41.8251002, -71.4028338 };
//
// List<String> cui = new ArrayList<String>();
// cui.add("indpak");
// cui.add(cuisine);
// return new Answer("1", cui, restr, new ArrayList<String>(), p, rockLoc,
// 1000);
// }
//
// //
// public static void resPrinter(List<Restaurant> res) {
// for (Restaurant curr : res) {
// System.out.println(
// curr.getName() + " :fjjjjjjjjjjjjjjjjjjjjjjjj" + curr.getPrice());
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

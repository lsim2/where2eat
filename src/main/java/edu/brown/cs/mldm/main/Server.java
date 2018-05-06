package edu.brown.cs.mldm.main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.mldm.chatroom.ChatWebSocket;
import edu.brown.cs.mldm.lsim2.frontend.Poll;
import edu.brown.cs.mldm.yelp.Answer;
import edu.brown.cs.mldm.yelp.Ranker;
import edu.brown.cs.mldm.yelp.Restaurant;
import edu.brown.cs.mldm.yelp.YelpApi;
import freemarker.template.Configuration;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

public class Server {

  static ChatWebSocket chatSocket;

  private static Map<UUID, Poll> pollDb = new HashMap<>();
  private static Map<UUID, List<Answer>> answersDb = new HashMap<>();
  public static Map<String, String> cuisinesDb = new HashMap<>();
  public static Map<String, String> restrictionsDb = new HashMap<>();
  public static Map<String, String> foodDb = new HashMap<>();

  private static final Gson GSON = new Gson();
   private static final String YELPKEY =
   "gKGjR4vy8kXQAyKrBjuPXepYBqladSEtwSTm_NNshaMPebXqQkZsGLIOe6FSUESQIh_l-cSN5lIhxiQ3-mkCnr_orbJARb_cCSr3OlQs0Jxi21D-m8uiqoHJr1jVWnYx";

  void runSparkServer(int port) {
    readFiles();
    chatSocket = new ChatWebSocket();
    Spark.webSocket("/chatting", chatSocket);

    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    // note: there should NOT be a chatroom get handler
    Spark.get("/name", new NameGetHandler(), freeMarker);

    Spark.get("/home", new homeFrontHandler(), freeMarker);
    Spark.post("/home", new homeSubmitHandler());
    Spark.get("/poll/:id", new pollUniqueHandler(), freeMarker);
    Spark.post("/validate", new resignInHandler());
    Spark.post("/chat/:id", new pollResHandler(), freeMarker);
    Spark.get("/chat/:id", new pollUniqueHandler(), freeMarker);
  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  private void readFiles() {
    Reader reader = new Reader();
    reader.readFiles("data/cuisines.txt", cuisinesDb);
    reader.readFiles("data/food.txt", foodDb);
    reader.readFiles("data/restrictions.txt", restrictionsDb);
  }

  /**
   * Handle requests to the front page of our Stars website.
   *
   * @author lsim2
   */
  private static class homeFrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title", "Where2Eat");
      return new ModelAndView(variables, "home.ftl");
    }
  }

  /**
   * Handle requests to the front page of our Autocorrect website.
   *
   * @author lsim2
   */
  private static class pollUniqueHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      String id = req.raw().getQueryString();
      Poll poll = pollDb.get(UUID.fromString((id)));
      Map<String, Object> variables = ImmutableMap.<String, Object>builder()
          .put("title", "Where2Eat")
          .put("name", poll.getAuthor()).put("meal", poll.getMeal())
          .put("location", poll.getLocation())
          .put("date", poll.getDate()).put("message", poll.getMsg())
          .put("pollId", id)
          .put("cuisines", cuisinesDb).put("restrictions", restrictionsDb)
          .put("food", foodDb).build();
      return new ModelAndView(variables, "poll.ftl");
    }
  }

  /**
   * Handle requests to the front page of our Autocorrect website.
   *
   * @author lsim2
   */
  private static class homeSubmitHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String name = qm.value("name");
      String title = qm.value("title");
      String location = qm.value("location");
      String date = qm.value("date");
      String msg = qm.value("message");
      UUID pollId = UUID.randomUUID();
      String lat = qm.value("lat");
      String lng = qm.value("lng");

      double[] coordinates = { Double.parseDouble(lat),
          Double.parseDouble(lng) };

      Poll poll = new Poll(pollId, name, title, location, date, msg,
          coordinates);
      pollDb.put(pollId, poll);
      Map<String, Object> variables = ImmutableMap.of("pollId", pollId,
          "pollTitle", title, "location", location,
          "date", date);
      return GSON.toJson(variables);
    }
  }


  /**
   * Handle requests to the front page of our Autocorrect website.
   *
   * @author lsim2
   */
  private static class pollResHandler implements TemplateViewRoute {

    @SuppressWarnings("unchecked")
    @Override
    public ModelAndView handle(Request req, Response res) {
      // Getting information from frontend
      QueryParamsMap qm = req.queryMap();
      int price = Integer.parseInt(qm.value("price"));
      int distance = Math.min(Integer.parseInt(qm.value("distance")) * 1609,
          39999);
      String user = qm.value("user");
      String cuisineString = qm.value("cuisine");
      String restrictionsString = qm.value("restrictions");
      String miscString = qm.value("misc");
      List<String> cuisine = GSON.fromJson(cuisineString, List.class);
      List<String> restrictions = GSON.fromJson(restrictionsString, List.class);
      List<String> misc = GSON.fromJson(miscString, List.class);
      String url = qm.value("url");
      int index = url.lastIndexOf('?') + 1;
      String uuidString = url.substring(index, url.length());
      UUID id = UUID.fromString(uuidString);
      Answer ans = new Answer(user, cuisine, restrictions, misc, price,
          pollDb.get(id).getCoordinates(),
          distance);
      if (!answersDb.containsKey(id)) {
        answersDb.put(id, new ArrayList<Answer>());
      }
      answersDb.get(id).add(ans);

      // processing with the algorithm:
      Map<Answer, List<Restaurant>> results = YelpApi
          .getPossibleRestaurants(answersDb.get(id));

      Ranker ranker = new Ranker();

      List<Restaurant> restList = new ArrayList<Restaurant>(
          ranker.rank(results));
      List<String> restaurants = new ArrayList<>();
      for (int a = 0; a < restList.size(); a++) {
        restaurants.add(restList.get(a).getName());
      }

      // changed to include sortedLists
      chatSocket.addRestaurantList(id, restList);

      String name = qm.value("user");
      Answer previousAns = chatSocket.getPreviousAns(id, name);
      chatSocket.addName(id, name, ans);

      String prevAns = GSON.toJson(ans);
      if (previousAns != null) {
        prevAns = GSON.toJson(prevAns);
      }
      
      Poll poll = pollDb.get(id);
     
      Map<String, Object> variables = ImmutableMap.<String, Object>builder()
          .put("title", "Where2Eat")
          .put("user", user).put("restaurants", restaurants).put("pollId", id)
          .put("cuisines", cuisinesDb).put("restrictions", restrictionsDb)
          .put("food", foodDb).put("prevAns", prevAns)
          .put("pollTitle", poll.getMeal())
          .put("pollDate", poll.getDate())
          .put("pollLoc", poll.getLocation())
          .put("author", poll.getAuthor())
          .build();
      return new ModelAndView(variables, "chat.ftl");
    }
  }

  /**
   * Handle requests to the front page of our Stars website.
   *
   * @author lsim2
   */
  private static class resignInHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String username = qm.value("user");
      String url = qm.value("url");
      UUID id = chatSocket.getUuid(url);
      Map<UUID, Map<String, Answer>> usersDb = chatSocket.getMaps()
          .getUsersDb();
      Map<String, Object> variables = ImmutableMap.of("oldUser", false);
      if (usersDb.containsKey(id) && usersDb.get(id).containsKey(username)) {
        Answer ans = usersDb.get(id).get(username);
        Map<String, Object> variables2 = ImmutableMap.of("oldUser", true, "answer", ans, "id", id);
        return GSON.toJson(variables2);
      }
      return GSON.toJson(variables);
    }
  }

  private static class NameGetHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request request, Response response) {

      Map<String, Object> variables = ImmutableMap.of("title", "Name");
      return new ModelAndView(variables, "chatroom/name.ftl");
    }
  }

  /**
   * Display an error page when an exception occurs in the server.
   *
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }

  public static Map<String, String> getCuisinesMap() {
    return cuisinesDb;
  }

  public static Map<String, String> getRestrictionsMap() {
    return restrictionsDb;
  }

  public static Map<String, String> getFoodTermsMap() {
    return foodDb;
  }

}
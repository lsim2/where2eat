package edu.brown.cs.lsim2.frontend;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import freemarker.template.Configuration;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

/**
 * The Main class of stars. This is where execution begins.
 *
 * @author lsim2
 */
public final class Main {

  private static final int DEFAULT_PORT = 4567;
  //private static Repl myRepl;
  private static Map<UUID, Poll> pollDb = new HashMap<>(); 
  private static Map<UUID, List<Answer>> answersDb = new HashMap<>(); 
  private static final Gson GSON = new Gson();

  /**
   * The initial method called when execution begins.
   *
   * @param args
   *          An array of command line arguments
   * @throws IOException
   *           if there was a problem with the REPL
   */
  public static void main(String[] args) throws IOException {
    new Main(args).run();
  }

  private String[] args;

  private Main(String[] args) {
    this.args = args;
  }

  /*
   * Parses command line arguments and starts a repl
   *
   */
  private void run() throws IOException {
    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);
    //myRepl = new Repl();
    if (options.has("gui")) {
      runSparkServer((int) options.valueOf("port"));
    }
    //myRepl.execute();
  }

  /*
   * Creates a FreeMarkerEngine for the frontend.
   */
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

  /*
   * Runs the Sparkserver
   */
  private void runSparkServer(int port) {
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();
    // Setup Spark Routes
    Spark.get("/", new FrontHandler(), freeMarker);
    Spark.get("/home", new homeFrontHandler(), freeMarker);
    Spark.post("/home", new homeSubmitHandler());
    Spark.get("/date", new dateFrontHandler(), freeMarker);
//    Spark.post("/poll", new pollFrontHandler(), freeMarker);
    Spark.get("/poll/:id", new pollUniqueHandler(), freeMarker);
    Spark.get("/chat", new chatFrontHandler(), freeMarker);
//    Spark.post("/chat", new chatFrontHandler(), freeMarker);
    Spark.post("/chat", new pollResHandler(), freeMarker);
  }

  /**
   * Handle requests to the front page of our Stars website.
   *
   * @author lsim2
   */
  private static class FrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title",
          "Yelp 2.0");
      return new ModelAndView(variables, "index.ftl");
    }
  }
  
  /**
   * Handle requests to the front page of our Stars website.
   *
   * @author lsim2
   */
  private static class homeFrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title",
          "Yelp 2.0");
      return new ModelAndView(variables, "home.ftl");
    }
  }


  /**
   * Handle requests to the front page of our Autocorrect website.
   *
   * @author lsim2
   */
  private static class dateFrontHandler implements TemplateViewRoute {
	    @Override
	    public ModelAndView handle(Request req, Response res) {
	      Map<String, Object> variables = ImmutableMap.of("title",
	          "Yelp 2.0");
	      return new ModelAndView(variables, "date.ftl");
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
	    		    .put("title", "Yelp 2.0") 
	    		    .put("name", poll.getAuthor()) 
	    		    .put("meal", poll.getMeal()) 
	    		    .put("location", poll.getLocation()) 
	    		    .put("date", poll.getDate()) 
	    		    .put("message", poll.getMsg()) 
	    		    .put("key9", "value9")
	    		    .build();
	      return new ModelAndView(variables, "poll.ftl");
	    }
	  }
  
  /**
   * Handle requests to the front page of our Autocorrect website.
   *
   * @author lsim2
   */
  private static class pollFrontHandler implements TemplateViewRoute {
	    @Override
	    public ModelAndView handle(Request req, Response res) {
	      QueryParamsMap qm = req.queryMap();
	      String name = qm.value("name");
	      String title = qm.value("title");
	      String location = qm.value("location");
	      String date = qm.value("date");
	      String msg = qm.value("message");
	      UUID pollId = UUID.randomUUID();
	      Poll poll = new Poll(pollId, name, title,location,date,msg);
	      pollDb.put(pollId, poll);
	      Map<String, Object> variables = ImmutableMap.of("title",
		          "Yelp 2.0", "pollId", pollId);
	      return new ModelAndView(variables, "poll.ftl");
	    }
	  }
  
  /**
   * Handle requests to the front page of our Autocorrect website.
   *
   * @author lsim2
   */
  private static class homeSubmitHandler implements Route {
	    public String handle(Request req, Response res) {
	      QueryParamsMap qm = req.queryMap();
	      String name = qm.value("name");
	      String title = qm.value("title");
	      String location = qm.value("location");
	      String date = qm.value("date");
	      String msg = qm.value("message");
	      UUID pollId = UUID.randomUUID();
	      Poll poll = new Poll(pollId, name, title,location,date,msg);
	      pollDb.put(pollId, poll);
	      Map<String, Object> variables = ImmutableMap.of("pollId", pollId, "pollTitle", title, "location", location, "date", date);
	      return GSON.toJson(variables);
	    }
	  }

  /**
   * Handle requests to the front page of our Autocorrect website.
   *
   * @author lsim2
   */
  private static class chatFrontHandler implements TemplateViewRoute {

	  @Override
	    public ModelAndView handle(Request req, Response res) {
	      Map<String, Object> variables = ImmutableMap.of("title",
	          "Yelp 2.0");
	      return new ModelAndView(variables, "chat.ftl");
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
	      QueryParamsMap qm = req.queryMap();
	      String price = qm.value("price");
	      String distance = qm.value("distance");
	      String user = qm.value("user");
	      String startTime = qm.value("startTime");
	      String endTime = qm.value("endTime");
	      String cuisineString = qm.value("cuisine");
	      String restrictionsString = qm.value("restrictions");
	      String miscString = qm.value("misc");
	      List<String> cuisine  = GSON.fromJson(cuisineString, List.class);
	      List<String> restrictions  = GSON.fromJson(restrictionsString, List.class);
	      List<String> misc  = GSON.fromJson(miscString, List.class);
	      String url = qm.value("url");
	      int index = url.lastIndexOf('?') + 1;
	      String uuidString = url.substring(index, url.length());
	      UUID id = UUID.fromString(uuidString);
	      Answer ans = new Answer(user, cuisine, restrictions, Integer.parseInt(price), Arrays.asList(startTime, endTime), Double.parseDouble(distance), misc);
	      if (!answersDb.containsKey(id)) {
	    	  	answersDb.put(id, new ArrayList<Answer>());
	      }
	      answersDb.get(UUID.fromString(uuidString)).add(ans);
	      Map<String, Object> variables = ImmutableMap.of("title",
		          "Yelp 2.0", "user", user);
	      return new ModelAndView(variables, "chat.ftl");
	    }
  }

  /**
   * Display an error page when an exception occurs in the server.
   *
   * @author lsim2
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

}

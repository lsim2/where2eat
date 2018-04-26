package edu.brown.cs.mldm.main;

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

import edu.brown.cs.mldm.chatroom.ChatWebSocket;
import edu.brown.cs.mldm.yelp.Answer;
import edu.brown.cs.mldm.lsim2.frontend.Poll;
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
	private static Map<UUID, Poll> pollDb = new HashMap<>(); 
	private static Map<UUID, List<Answer>> answersDb = new HashMap<>(); 
	private static final Gson GSON = new Gson();
	
	void runSparkServer(int port) {
		Spark.webSocket("/score", ChatWebSocket.class);

		Spark.port(port);
		Spark.externalStaticFileLocation("src/main/resources/static");
		Spark.exception(Exception.class, new ExceptionPrinter());

		FreeMarkerEngine freeMarker = createEngine();

		Spark.get("/chatroom", new ChatroomGetHandler(), freeMarker);
	    Spark.get("/home", new homeFrontHandler(), freeMarker);
	    Spark.post("/home", new homeSubmitHandler());
	    Spark.get("/date", new dateFrontHandler(), freeMarker);
	    Spark.get("/poll/:id", new pollUniqueHandler(), freeMarker);
	    Spark.get("/chat", new chatFrontHandler(), freeMarker);
	    Spark.post("/chat", new pollResHandler(), freeMarker);
	}

	private static FreeMarkerEngine createEngine() {
		Configuration config = new Configuration();
		File templates = new File("src/main/resources/spark/template/freemarker");
		try {
			config.setDirectoryForTemplateLoading(templates);
		} catch (IOException ioe) {
			System.out.printf("ERROR: Unable use %s for template loading.%n", templates);
			System.exit(1);
		}
		return new FreeMarkerEngine(config);
	}

	/**
	 * Handles requests to the starting query page.
	 */
	private static class ChatroomGetHandler implements TemplateViewRoute {
		@Override
		public ModelAndView handle(Request request, Response response) {

			Map<String, Object> variables = ImmutableMap.of("title", "Chatroom");
			return new ModelAndView(variables, "chatroom/chatroom.ftl");
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
		      String lat = qm.value("lat");
		      String lng = qm.value("lng");
		      double[] coordinates ={Double.parseDouble(lat), Double.parseDouble(lng)};
		      UUID pollId = UUID.randomUUID();
		      Poll poll = new Poll(pollId, name, title,location,date,msg, coordinates);
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
		      String lat = qm.value("lat");
		      String lng = qm.value("lng");
		      double[] coordinates ={Double.parseDouble(lat), Double.parseDouble(lng)};
		      Poll poll = new Poll(pollId, name, title,location,date,msg,coordinates);
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
		      int price = Integer.parseInt(qm.value("price"));
		      int distance = Integer.parseInt(qm.value("distance"));
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
		      Answer ans = new Answer(user, cuisine, restrictions, misc, price, pollDb.get(id).getCoordinates(), distance);
		      if (!answersDb.containsKey(id)) {
		    	  	answersDb.put(id, new ArrayList<Answer>());
		      }
		      answersDb.get(id).add(ans);
		      Map<String, Object> variables = ImmutableMap.of("title",
			          "Yelp 2.0", "user", user);
		      return new ModelAndView(variables, "chat.ftl");
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

}

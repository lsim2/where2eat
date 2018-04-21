package edu.brown.cs.lsim2.frontend;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import freemarker.template.Configuration;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
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
    Spark.get("/date", new dateFrontHandler(), freeMarker);
    Spark.get("/chat", new chatFrontHandler(), freeMarker);
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
          "Yelp 2.0", "content", "");
      return new ModelAndView(variables, "index.ftl");
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
	          "Yelp 2.0", "content", "");
	      return new ModelAndView(variables, "date.ftl");
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
	          "Yelp 2.0", "content", "");
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

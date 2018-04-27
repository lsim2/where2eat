package edu.brown.cs.mldm.main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.mldm.chatroom.ChatWebSocket;
import freemarker.template.Configuration;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

public class Server {
	static ChatWebSocket chatSocket;

	void runSparkServer(int port) {
		chatSocket = new ChatWebSocket();
		Spark.webSocket("/chat", chatSocket);

		Spark.port(port);
		Spark.externalStaticFileLocation("src/main/resources/static");
		Spark.exception(Exception.class, new ExceptionPrinter());

		FreeMarkerEngine freeMarker = createEngine();

		Spark.post("/chatroom", new ChatroomPostHandler(), freeMarker);
		// Spark.get("/chatroom", new ChatroomGetHandler(), freeMarker);
		Spark.get("/name", new NameGetHandler(), freeMarker);
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
	private static class ChatroomPostHandler implements TemplateViewRoute {
		@Override
		public ModelAndView handle(Request req, Response response) {
			QueryParamsMap qm = req.queryMap();
			String name = qm.value("usrName");
			System.out.println("the name is: " + name);
			chatSocket.addName(name);
			Map<String, Object> variables = ImmutableMap.of("title", "Chatroom");
			return new ModelAndView(variables, "chatroom/chatroom.ftl");
		}
	}

	private static class ChatroomGetHandler implements TemplateViewRoute {
		@Override
		public ModelAndView handle(Request req, Response response) {
			Map<String, Object> variables = ImmutableMap.of("title", "Chatroom");
			return new ModelAndView(variables, "chatroom/chatroom.ftl");
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

}

package edu.brown.cs.mldm.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;

import edu.brown.cs.mldm.lsim2.frontend.Answer;
import edu.brown.cs.mldm.lsim2.frontend.Poll;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

public class Main {

	private static final int DEFAULT_PORT = 4567;
	private String[] args;

	private Main(String[] args) {
		this.args = args;
	}

	public static void main(String[] args) {

		OptionParser parser = new OptionParser();
		parser.accepts("gui");
		parser.accepts("port").withRequiredArg().ofType(Integer.class).defaultsTo(DEFAULT_PORT);
		OptionSet options = parser.parse(args);

		if (options.has("gui")) {
			Server server = new Server();
			server.runSparkServer((int) options.valueOf("port"));
		}

		Reader reader = new InputStreamReader(System.in, Charset.forName("UTF-8"));

		try (BufferedReader in = new BufferedReader(reader)) {
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println("echoding: " + line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}

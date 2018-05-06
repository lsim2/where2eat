package edu.brown.cs.mldm.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

/**
 * This is the main class which starts our program.
 *
 */
public class Main {

  private static final int DEFAULT_PORT = 4567;

  /**
   * The constructor with arguments.
   *
   * @param args
   */
  private Main(String[] args) {
  }

  /**
   * Reads information from the command line and runs the program with the
   * specified arguments.
   *
   * @param args
   *          the arguments from the main class.
   */
  public static void main(String[] args) {

    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_PORT);
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
      System.out.println("ERROR: Reading the command line");
    } finally {
      try {
        reader.close();
      } catch (IOException e) {
        System.out.println("ERROR: Closing the buffer reader");
      }
    }

  }

}

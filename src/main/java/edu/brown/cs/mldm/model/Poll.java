package edu.brown.cs.mldm.model;

import java.util.UUID;

/**
 *
 * Poll object bundles all information about a particular poll, so it can be
 * easily stored for later retrival.
 */
public class Poll {
  private UUID id;
  private String author;
  private String title;
  private String location;
  private String date;
  private String msg;
  private double[] coordinates;

  /**
   * Constructor of the poll.
   *
   * @param id
   *          unique id of the poll
   * @param author
   *          username of the initiator of the poll
   * @param title
   *          title of the poll
   * @param location
   *          location of the poll
   * @param date
   *          date of the poll
   * @param msg
   *          the written message of the poll
   * @param coordinates
   *          the array of lat lng coordinates of the location
   */
  public Poll(UUID id, String author, String title, String location,
      String date, String msg, double[] coordinates) {
    this.id = id;
    this.author = author;
    this.title = title;
    this.location = location;
    this.date = date;
    this.msg = msg;
    this.coordinates = coordinates;
  }

  /**
   *
   * @return the id of the poll
   */
  public UUID getId() {
    return this.id;
  }

  /**
   *
   * @return the author of the poll
   */
  public String getAuthor() {
    return this.author;
  }

  /**
   *
   * @return the meal of the poll
   */
  public String getMeal() {
    return this.title;
  }

  /**
   *
   * @return the location of the poll
   */
  public String getLocation() {
    return this.location;
  }

  /**
   *
   * @return the date of the poll
   */
  public String getDate() {
    return this.date;
  }

  /**
   *
   * @return the message of the poll
   */
  public String getMsg() {
    return this.msg;
  }

  /**
   *
   * @return the coodinates of the location of the poll
   */
  public double[] getCoordinates() {
    return this.coordinates;
  }
}

package edu.brown.cs.mldm.lsim2.frontend;

import java.util.UUID;

import edu.brown.cs.mldm.yelp.Answer;

/** Class representing a registered user. */
public class User {
  private final UUID id;
  private final String name;
  private static Answer answer;

  /**
   * Constructs a new User.
   *
   * @param id the ID of this User
   * @param name the username of this User
   * @param password
   * @param creation the creation time of this User
   */
  public User(UUID id, String name, Answer answer) {
    this.id = id;
    this.name = name;
    this.answer = answer;
  }

  /** Returns the ID of this User. */
  public UUID getId() {
    return id;
  }

  /** Returns the username of this User. */
  public String getName() {
    return name;
  }
}

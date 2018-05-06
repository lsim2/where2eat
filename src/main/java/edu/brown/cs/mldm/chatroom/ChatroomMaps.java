package edu.brown.cs.mldm.chatroom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

import org.eclipse.jetty.websocket.api.Session;

import edu.brown.cs.mldm.model.Message;
import edu.brown.cs.mldm.yelp.Answer;
import edu.brown.cs.mldm.yelp.Restaurant;

/*
 * Holds all the different hashmaps for the ChatWebSocket
 */
public class ChatroomMaps {
  private final Map<Integer, String> idsToName = new HashMap<>();
  private final Map<UUID, Map<String, Answer>> usersDb = new HashMap<>();
  private final Map<Session, Integer> sessionToId = new HashMap<Session, Integer>();
  private final Map<Session, String> sessionToURL = new HashMap<Session, String>();
  private final Map<UUID, List<Restaurant>> uuidToRestaurants = new HashMap<>();
  private final Map<UUID, List<Restaurant>> uuidToPriceRestaurants = new HashMap<>();
  private final Map<UUID, List<Restaurant>> uuidToDistRestaurants = new HashMap<>();
  private final Map<String, List<Message>> urlToMsgs = new HashMap<String, List<Message>>();
  private final Map<String, Queue<Session>> urlToQueueOfSessions = new HashMap<String, Queue<Session>>();
  private final Map<UUID, Map<String, Restaurant>> uuidToUpVoteMap = new HashMap<>();
  private final Map<UUID, Map<String, Restaurant>> uuidToDownVoteMap = new HashMap<>();

  public Map<Integer, String> getidsToName() {
    return idsToName;
  }

  public Map<Session, Integer> getSessionToId() {
    return sessionToId;
  }

  public Map<Session, String> getSessionToURL() {
    return sessionToURL;
  }

  public Map<UUID, List<Restaurant>> getUuidToRestaurants() {
    return uuidToRestaurants;
  }

  public Map<UUID, List<Restaurant>> getUuidToPriceRestaurants() {
    return uuidToPriceRestaurants;
  }

  public Map<UUID, List<Restaurant>> getUuidToDistRestaurants() {
    return uuidToDistRestaurants;
  }

  public Map<String, List<Message>> getUrlToMsgs() {
    return urlToMsgs;
  }

  public Map<String, Queue<Session>> getUrlToQueueOfSessions() {
    return urlToQueueOfSessions;
  }

  public Map<UUID, Map<String, Answer>> getUsersDb() {
    return usersDb;
  }

  /**
   * @return the upvotes
   */
  public Map<String, Restaurant> getUpvotes(UUID uuid) {
    if (!uuidToUpVoteMap.containsKey(uuid)) {
      Map<String, Restaurant> upvotes = new HashMap<>();
      uuidToUpVoteMap.put(uuid, upvotes);
    }
    // return upvotes;
    return uuidToUpVoteMap.get(uuid);

  }

  /**
   * @return the downvotes
   */
  public Map<String, Restaurant> getDownvotes(UUID uuid) {
    if (!uuidToDownVoteMap.containsKey(uuid)) {
      Map<String, Restaurant> dvotes = new HashMap<>();
      uuidToDownVoteMap.put(uuid, dvotes);
    }

    return uuidToDownVoteMap.get(uuid);
    // return downvotes;
  }

}

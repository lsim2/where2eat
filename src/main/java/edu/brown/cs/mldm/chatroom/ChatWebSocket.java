package edu.brown.cs.mldm.chatroom;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import edu.brown.cs.mldm.model.Message;
import edu.brown.cs.mldm.yelp.Answer;
import edu.brown.cs.mldm.yelp.Restaurant;

@WebSocket
public class ChatWebSocket {
  private static final Gson GSON = new Gson();
  private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();
  private static int nextId = 0;
  private ChatroomMaps myChatroomMaps = new ChatroomMaps();

  /**
   *
   * Enum representing message types from the client.
   *
   */
  private enum MESSAGE_TYPE {
    CONNECT, SEND, UPDATE, DELETE, UPDATEALLNAMES, ADDTOROOM, UPDATERESTS
  }

  /**
   *
   * Enum representing vote type on a given restaurant.
   *
   */
  private enum VOTE_TYPE {
    NONE, UP, DOWN
  }

  /**
   * Adds name and answer to the corresponding poll id.
   *
   * @param id
   *          the id of the poll
   * @param name
   *          name of the user
   * @param ans
   *          the preferences of the user
   */
  public void addName(UUID id, String name, Answer ans) {
    myChatroomMaps.getidsToName().put(nextId, name);
    Map<UUID, Map<String, Answer>> usersDb = myChatroomMaps.getUsersDb();
    if (!usersDb.containsKey(id)) {
      usersDb.put(id, new HashMap<String, Answer>());
    }
    usersDb.get(id).put(name, ans);

  }

  /**
   * Gets the previous preferences of a given user in a given unique poll, this
   * is so the user can update their preferences later.
   *
   * @param id
   *          id of the poll
   * @param name
   *          user name of the user
   * @return the previous answer from a user if there is one, none otherwise
   */
  public Answer getPreviousAns(UUID id, String name) {
    myChatroomMaps.getidsToName().put(nextId, name);
    Map<UUID, Map<String, Answer>> usersDb = myChatroomMaps.getUsersDb();
    if (!usersDb.containsKey(id) || !usersDb.get(id).containsKey(name)) {
      return null;
    }
    return usersDb.get(id).get(name);
  }

  /**
   *
   * @return an instance of the ChatRoomMaps class
   */
  public ChatroomMaps getMaps() {
    return this.myChatroomMaps;
  }

  /**
   * Adds updates restaurants to the list of restaurants mapped to from the
   * unique poll id.
   *
   * @param id
   *          the id of the poll
   * @param restaurants
   *          the list of restaurants to be added to the map
   */
  public void addRestaurantList(UUID id, List<Restaurant> restaurants) {
    myChatroomMaps.getUuidToRestaurants().put(id, restaurants);

  }

  /**
   * Called when a new client is connected.
   *
   * @param session
   *          the session that just connected
   * @throws IOException
   *           if an io exception occurs
   */
  @OnWebSocketConnect
  public void connected(Session session) throws IOException {
    sessions.add(session);

    JsonObject jObject = new JsonObject();
    jObject.addProperty("type", MESSAGE_TYPE.CONNECT.ordinal());

    JsonObject payLoadObject = new JsonObject();
    payLoadObject.addProperty("id", nextId);
    payLoadObject.addProperty("myName",
        myChatroomMaps.getidsToName().get(nextId));
    jObject.add("payload", payLoadObject);
    myChatroomMaps.getSessionToId().put(session, nextId);
    session.getRemote().sendString(GSON.toJson(jObject));
    nextId++;
  }

  /**
   * Adds all the names of the users to the chatroom of the given url.
   *
   * @param session
   *          the new session that just joined the chatroom
   * @param receivedRoomURL
   *          the url of the chatroom
   * @throws IOException
   *           if an io exception occured
   */
  public void addNamesInRoom(Session session, String receivedRoomURL)
      throws IOException {
    JsonObject jObject = new JsonObject();
    jObject.addProperty("type", MESSAGE_TYPE.UPDATEALLNAMES.ordinal());

    JsonArray allNamesInRoom = new JsonArray();

    Queue<Session> myQueue = myChatroomMaps.getUrlToQueueOfSessions()
        .get(receivedRoomURL);

    // need to add unique names
    for (Session sesh : myQueue) {
      Integer personId = myChatroomMaps.getSessionToId().get(sesh);
      String myName = myChatroomMaps.getidsToName().get(personId);
      allNamesInRoom.add(myName);
    }

    JsonArray suggestions = new JsonArray();
    JsonArray rests = new JsonArray();
    UUID uuid = this.getUuid(receivedRoomURL);
    List<Restaurant> restaurantList = getRestaurantList(receivedRoomURL);
    for (Restaurant r : restaurantList) {
      suggestions.add(r.getName());
      updateRestVotes(r, uuid);

      rests.add(GSON.toJson(r));
    }

    jObject.add("namesInRoom", allNamesInRoom);
    jObject.add("suggestions", suggestions);
    jObject.add("rests", rests);

    // broadcasting to every1 (in the room) that there's a new user - and we
    // should
    // handle it
    updateAllSessionsInPoll(receivedRoomURL, jObject);

  }

  /**
   * Called when the server gets a msg that one of the clients has closed/left.
   *
   * @param session
   *          the session that closed
   * @param statusCode
   *          the given status code of the client
   * @param reason
   *          reason for closing client
   * @throws IOException
   *           if an IO exception occured
   */
  @OnWebSocketClose
  public void closed(Session session, int statusCode, String reason)
      throws IOException {
    int idSession = myChatroomMaps.getSessionToId().get(session);
    myChatroomMaps.getSessionToId().remove(session);
    String nameOfClosedUser = myChatroomMaps.getidsToName().get(idSession);
    myChatroomMaps.getidsToName().remove(idSession);

    String sessionURL = myChatroomMaps.getSessionToURL().get(session);
    Queue<Session> myQueue = myChatroomMaps.getUrlToQueueOfSessions()
        .get(sessionURL);
    myQueue.remove(session);

    sessions.remove(session);

    // broadcast to every1 in the room to delete the - already - closed user
    // from
    // active users list
    for (Session sesh : myQueue) {
      JsonObject updatedObject = new JsonObject();
      updatedObject.addProperty("type", MESSAGE_TYPE.DELETE.ordinal());
      updatedObject.addProperty("name", nameOfClosedUser);
      sesh.getRemote().sendString(GSON.toJson(updatedObject));
    }

  }

  private JsonObject addPreviousMessages(String receivedName, Session session,
      String receivedRoomURL)
      throws IOException {
    JsonObject jObject = new JsonObject();
    jObject.addProperty("type", MESSAGE_TYPE.ADDTOROOM.ordinal());

    JsonObject payLoadObject = new JsonObject();
    payLoadObject.addProperty("id", nextId);
    payLoadObject.addProperty("myName", receivedName);
    JsonArray dates = new JsonArray();
    JsonArray names = new JsonArray();
    JsonArray ids = new JsonArray();
    JsonArray content = new JsonArray();
    JsonArray suggestions = new JsonArray();
    JsonArray rests = new JsonArray();

    List<Restaurant> restaurantList = getRestaurantList(receivedRoomURL);

    for (Restaurant r : restaurantList) {
      suggestions.add(r.getName());
      rests.add(GSON.toJson(r));
    }

    // we're adding all previous msgs (in the room) to the payload
    List<Message> msgsInRoom = myChatroomMaps.getUrlToMsgs()
        .get(receivedRoomURL);
    for (Message msg : msgsInRoom) {
      Date unParsedDate = msg.getDate();
      SimpleDateFormat dataFormat = new SimpleDateFormat("h:mm a");
      Integer senderId = msg.getSenderId();

      String StringId = Integer.toString(senderId);
      String stringDate = dataFormat.format(unParsedDate);
      String stringName = msg.getSenderName();
      String stringContent = msg.getContent();

      ids.add(StringId);
      dates.add(stringDate);
      names.add(stringName);
      content.add(stringContent);
    }
    payLoadObject.add("ids", ids);
    payLoadObject.add("dates", dates);
    payLoadObject.add("names", names);
    payLoadObject.add("content", content);
    payLoadObject.add("suggestions", suggestions);
    payLoadObject.add("rests", rests);
    addNamesInRoom(session, receivedRoomURL); // helper func: THIS ADDS ALL THE
                                              // names of the users within a
                                              // room

    jObject.add("payload", payLoadObject);
    return jObject;
  }

  /**
   * Called when we receive a message from the client.
   *
   * @param session
   *          the given session that receives the message
   * @param message
   *          the contents of the message
   * @throws IOException
   *           when an IO error occurs
   */
  @OnWebSocketMessage
  public void message(Session session, String message) throws IOException {

    JsonObject received = GSON.fromJson(message, JsonObject.class);
    JsonObject receivedPayload = received.get("payload").getAsJsonObject();
    String receivedName = receivedPayload.get("name").getAsString();
    int receivedId = receivedPayload.get("id").getAsInt();
    int msgType = received.get("type").getAsInt();
    String receivedRoomURL = receivedPayload.get("roomURL").getAsString();
    // if is a new user
    switch (msgType) {
    case 1:
      assert msgType == MESSAGE_TYPE.SEND.ordinal();
      sendMessgeToAll(receivedPayload, receivedRoomURL, receivedName,
          receivedId);
      break;
    case 5:
      assert msgType == MESSAGE_TYPE.ADDTOROOM.ordinal();
      addToRoom(session, receivedRoomURL, receivedName, receivedId);
      break;
    case 6:
      assert msgType == MESSAGE_TYPE.UPDATERESTS.ordinal();
      updateRestaurants(receivedPayload, receivedRoomURL);
      break;
    default:
      break;
    }
  }

  private void updateRestaurants(JsonObject receivedPayload,
      String receivedRoomURL) throws IOException {
    JsonElement voteRank = receivedPayload.get("voteRank");
    JsonArray newResList = voteRank.getAsJsonArray();
    JsonElement remaining = receivedPayload.get("remaining");
    List<Restaurant> updatedRestaurantList = new ArrayList<>();

    List<Restaurant> currRestaurantList = getRestaurantList(receivedRoomURL);
    UUID uuid = this.getUuid(receivedRoomURL);
    for (JsonElement jsonObj : newResList) {
      try {
        Restaurant rest = GSON.fromJson(jsonObj.getAsJsonObject(),
            Restaurant.class);
        int index = currRestaurantList.indexOf(rest);
        Restaurant restFromList = currRestaurantList.get(index);
        if (rest.getVoteType() == VOTE_TYPE.UP.ordinal()) {
          restFromList.incrementUpVotes();
          this.updateVotes(restFromList, "up", uuid);
        } else if (rest.getVoteType() == VOTE_TYPE.DOWN.ordinal()) {
          restFromList.incrementDownVotes();
          this.updateVotes(restFromList, "down", uuid);
        }

        rest = restFromList;
        rest.resetVote();
        updatedRestaurantList.add(rest);
      } catch (Exception e) {
        System.out.println("ERROR: Error updating!");
      }

    }

    updatedRestaurantList
        .sort((r1, r2) -> Integer.compare(r2.getNetVotes(), r1.getNetVotes()));

    JsonArray updatedJsonResList = new JsonArray();

    for (Restaurant r : updatedRestaurantList) {
      updatedJsonResList.add(GSON.toJson(r));
    }

    JsonObject updatedObject = new JsonObject();
    JsonObject payLoadObject = new JsonObject();
    updatedObject.addProperty("type", MESSAGE_TYPE.UPDATERESTS.ordinal());
    payLoadObject.add("ranking", updatedJsonResList);
    payLoadObject.add("remaining", remaining);
    updatedObject.add("payload", payLoadObject);
    updateAllSessionsInPoll(receivedRoomURL, updatedObject);

  }

  private void updateAllSessionsInPoll(String receivedRoomURL,
      JsonObject updatedObject) throws IOException {
    Queue<Session> myQueue = myChatroomMaps.getUrlToQueueOfSessions()
        .get(receivedRoomURL);

    // tell all sessions in unique poll about the new msg we received
    for (Session sesh : myQueue) {
      sesh.getRemote().sendString(GSON.toJson(updatedObject));
    }
  }

  private List<Restaurant> getRestaurantList(String receivedRoomURL) {
    int index = receivedRoomURL.lastIndexOf('?') + 1;
    String uuidString = receivedRoomURL.substring(index,
        receivedRoomURL.length());
    UUID id = UUID.fromString(uuidString);

    return myChatroomMaps.getUuidToRestaurants().get(id);
  }

  /**
   * Converts a string URL into a UUID.
   *
   * @param receivedRoomURL
   *          the given URL
   * @return the UUID of the url
   */
  public UUID getUuid(String receivedRoomURL) {
    int index = receivedRoomURL.lastIndexOf('?') + 1;
    String uuidString = receivedRoomURL.substring(index,
        receivedRoomURL.length());
    UUID id = UUID.fromString(uuidString);
    return id;
  }

  /**
   * Method updates the map which keeps track of a restaurant's votes.
   *
   * @param rest
   * @param direction
   */
  private void updateVotes(Restaurant rest, String direction, UUID uuid) {

    Map<String, Restaurant> dVotes = myChatroomMaps.getDownvotes(uuid);
    Map<String, Restaurant> upVotes = myChatroomMaps.getUpvotes(uuid);

    if (direction.equals("up")) {
      if (upVotes.containsKey(rest.getId())) {
        if (upVotes.get(rest.getId()).getUpVotes() != rest.getUpVotes()) {
          upVotes.get(rest.getId()).incrementUpVotes();
        }
      } else {
        upVotes.put(rest.getId(), rest);
      }
    } else if (direction.equals("down")) {
      if (dVotes.containsKey(rest.getId())) {
        if (dVotes.get(rest.getId()).getDownVotes() != rest.getDownVotes()) {
          dVotes.get(rest.getId()).incrementDownVotes();
        }
      } else {
        dVotes.put(rest.getId(), rest);
      }
    }
  }

  private void updateRestVotes(Restaurant rest, UUID uuid) {
    Map<String, Restaurant> dvotes = myChatroomMaps.getDownvotes(uuid);
    Map<String, Restaurant> uvotes = myChatroomMaps.getUpvotes(uuid);
    if (dvotes.containsKey(rest.getId())) {
      rest.setDownVotes(dvotes.get(rest.getId()).getDownVotes());
    }
    if (uvotes.containsKey(rest.getId())) {
      rest.setUpVotes(uvotes.get(rest.getId()).getUpVotes());
    }
  }

  private void addToRoom(Session session, String receivedRoomURL,
      String receivedName, int receivedId) throws IOException {
    myChatroomMaps.getSessionToURL().put(session, receivedRoomURL);
    if (!myChatroomMaps.getUrlToQueueOfSessions()
        .containsKey(receivedRoomURL)) {
      myChatroomMaps.getUrlToQueueOfSessions().put(receivedRoomURL,
          new ConcurrentLinkedQueue<Session>());
    }

    if (!myChatroomMaps.getUrlToMsgs().containsKey(receivedRoomURL)) {
      myChatroomMaps.getUrlToMsgs().put(receivedRoomURL,
          new ArrayList<Message>());
    }

    Map<String, Answer> userPreferences = myChatroomMaps.getUsersDb()
        .get(getUuid(receivedRoomURL));
    Answer preferences = userPreferences.get(receivedName);
    Date now = new Date();
    Message msg = new Message();
    msg.setContent(preferences.toHTML());
    msg.setSenderName(receivedName);
    msg.setDate(now);
    msg.setSenderId(receivedId);

    Queue<Session> myQueue = myChatroomMaps.getUrlToQueueOfSessions()
        .get(receivedRoomURL);
    myQueue.add(session);
    List<Message> msgsInRoom = myChatroomMaps.getUrlToMsgs()
        .get(receivedRoomURL);
    if (!msgsInRoom.contains(msg)) {
      msgsInRoom.add(msg);
    }
    JsonObject toSend = addPreviousMessages(receivedName, session,
        receivedRoomURL);
    updateAllSessionsInPoll(receivedRoomURL, toSend);
  }

  private void sendMessgeToAll(JsonObject receivedPayload,
      String receivedRoomURL,
      String receivedName, int receivedId) throws IOException {
    String userText = receivedPayload.get("text").getAsString();

    Date now = new Date();

    // construct a message
    Message msg = new Message();
    msg.setContent(userText);
    msg.setSenderName(receivedName);
    msg.setDate(now);
    msg.setSenderId(receivedId);
    // add the msg to our list of msgs
    List<Message> msgsInRoom = myChatroomMaps.getUrlToMsgs()
        .get(receivedRoomURL);
    msgsInRoom.add(msg);

    // wanna iterate through all messages and
    SimpleDateFormat dataFormat = new SimpleDateFormat("h:mm a");
    String date = dataFormat.format(now);

    JsonObject updatedObject = new JsonObject();
    updatedObject.addProperty("type", MESSAGE_TYPE.UPDATE.ordinal());

    JsonObject payLoadObject = new JsonObject();

    JsonArray suggestions = new JsonArray();
    JsonArray rests = new JsonArray();

    List<Restaurant> restaurantList = getRestaurantList(receivedRoomURL);
    for (Restaurant r : restaurantList) {
      suggestions.add(r.getName());
      rests.add(GSON.toJson(r));
    }

    payLoadObject.addProperty("id", receivedId);
    payLoadObject.addProperty("text", userText);
    payLoadObject.addProperty("date", date);
    payLoadObject.addProperty("name", receivedName);
    payLoadObject.add("suggestions", suggestions);
    payLoadObject.add("rests", rests);

    updatedObject.add("payload", payLoadObject);
    updateAllSessionsInPoll(receivedRoomURL, updatedObject);
  }
}

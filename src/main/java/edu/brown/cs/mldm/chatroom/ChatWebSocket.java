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
import com.google.gson.JsonObject;

import edu.brown.cs.mldm.model.Message;
import edu.brown.cs.mldm.yelp.Restaurant;

//TODO: ask why this annotation is necessary <-- what does it even do?
@WebSocket
public class ChatWebSocket {
	private static final Gson GSON = new Gson();
	private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();
	private static int nextId = 0;
	private ChatroomMaps myChatroomMaps = new ChatroomMaps();

	private static enum MESSAGE_TYPE {
		CONNECT, SEND, UPDATE, DELETE, UPDATEALLNAMES, ADDTOROOM
	}

	// called in server
	public void addName(String name) {
		myChatroomMaps.getidsToName().put(nextId, name);
	}

	// called in server
	public void addRestaurantList(UUID id, List<Restaurant> restaurants, List<Restaurant> priceList,
			List<Restaurant> distList) {
		myChatroomMaps.getUuidToRestaurants().put(id, restaurants);
		myChatroomMaps.getUuidToPriceRestaurants().put(id, priceList);
		myChatroomMaps.getUuidToDistRestaurants().put(id, distList);
	}

	@OnWebSocketConnect
	public void connected(Session session) throws IOException {
		System.out.println("Web socket connected");
		sessions.add(session);

		JsonObject jObject = new JsonObject();
		jObject.addProperty("type", MESSAGE_TYPE.CONNECT.ordinal());

		JsonObject payLoadObject = new JsonObject();
		payLoadObject.addProperty("id", nextId);
		payLoadObject.addProperty("myName", myChatroomMaps.getidsToName().get(nextId));

		jObject.add("payload", payLoadObject);
		myChatroomMaps.getSessionToId().put(session, nextId);
		// TODO: update connect on client
		session.getRemote().sendString(GSON.toJson(jObject));
		nextId++;
	}

	public void addNamesInRoom(Session session, String receivedRoomURL) throws IOException {
		System.out.println("add unique names called");
		JsonObject jObject = new JsonObject();
		jObject.addProperty("type", MESSAGE_TYPE.UPDATEALLNAMES.ordinal());

		JsonArray allNamesInRoom = new JsonArray();

		Queue<Session> myQueue = myChatroomMaps.getUrlToQueueOfSessions().get(receivedRoomURL);

		// need to add unique names
		for (Session sesh : myQueue) {
			Integer personId = myChatroomMaps.getSessionToId().get(sesh);
			String myName = myChatroomMaps.getidsToName().get(personId);
			allNamesInRoom.add(myName);
		}

		JsonArray suggestions = new JsonArray();
		JsonArray rests = new JsonArray();

		List<Restaurant> restaurantList = getRestaurantList(receivedRoomURL);
		for (Restaurant r : restaurantList) {
			suggestions.add(r.getName());
			rests.add(GSON.toJson(r));
		}
		JsonArray priceSuggestions = new JsonArray();
		for (Restaurant rest : myChatroomMaps.getUuidToPriceRestaurants().get(getUuid(receivedRoomURL))) {
			priceSuggestions.add(rest.getName());
		}
		JsonArray distSuggestions = new JsonArray();
		for (Restaurant rest : myChatroomMaps.getUuidToDistRestaurants().get(getUuid(receivedRoomURL))) {
			distSuggestions.add(rest.getName());
		}
		System.out.println("Suggestions" + distSuggestions);
		jObject.add("priceSuggestions", priceSuggestions);
		jObject.add("distSuggestions", distSuggestions);
		jObject.add("namesInRoom", allNamesInRoom);
		jObject.add("suggestions", suggestions);
		jObject.add("rests", rests);

		// broadcasting to every1 (in the room) that there's a new user - and we should
		// handle it
		for (Session sesh : myQueue) {
			sesh.getRemote().sendString(GSON.toJson(jObject));
		}

	}

	// when the server gets a msg that one of the clients has closed/left
	@OnWebSocketClose
	public void closed(Session session, int statusCode, String reason) throws IOException {
		System.out.println("closing starting");
		int idSession = myChatroomMaps.getSessionToId().get(session);
		System.out.println("closing and idsession is: " + idSession);
		myChatroomMaps.getSessionToId().remove(session);
		String nameOfClosedUser = myChatroomMaps.getidsToName().get(idSession);
		myChatroomMaps.getidsToName().remove(idSession);

		String sessionURL = myChatroomMaps.getSessionToURL().get(session);
		Queue<Session> myQueue = myChatroomMaps.getUrlToQueueOfSessions().get(sessionURL);
		myQueue.remove(session);

		sessions.remove(session);

		// broadcast to every1 in the room to delete the - already - closed user from
		// active users list
		for (Session sesh : myQueue) {
			System.out.println("broadcasting to remaining sessions");
			JsonObject updatedObject = new JsonObject();
			updatedObject.addProperty("type", MESSAGE_TYPE.DELETE.ordinal());
			updatedObject.addProperty("name", nameOfClosedUser);
			System.out.println("name of closed user is:" + nameOfClosedUser);
			sesh.getRemote().sendString(GSON.toJson(updatedObject));
		}

	}

	public void addPreviousMessages(Session session, String receivedRoomURL) throws IOException {
		JsonObject jObject = new JsonObject();
		jObject.addProperty("type", MESSAGE_TYPE.ADDTOROOM.ordinal());

		JsonObject payLoadObject = new JsonObject();
		payLoadObject.addProperty("id", nextId);
		payLoadObject.addProperty("myName", myChatroomMaps.getidsToName().get(nextId));

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
		List<Message> msgsInRoom = myChatroomMaps.getUrlToMsgs().get(receivedRoomURL);
		for (Message msg : msgsInRoom) {
			Date unParsedDate = msg.getDate();
			SimpleDateFormat dataFormat = new SimpleDateFormat("h:m a");
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
		System.out.println("addPreviousMessages Called" + suggestions);
		payLoadObject.add("ids", ids);
		payLoadObject.add("dates", dates);
		payLoadObject.add("names", names);
		payLoadObject.add("content", content);
		payLoadObject.add("suggestions", suggestions);
		payLoadObject.add("rests", rests);

		// TODO: repeated code!

		addNamesInRoom(session, receivedRoomURL); // helper func: THIS ADDS ALL THE names of the users within a room

		jObject.add("payload", payLoadObject);
		session.getRemote().sendString(GSON.toJson(jObject));

	}

	// when we receive a message from the client
	@OnWebSocketMessage
	public void message(Session session, String message) throws IOException {

		JsonObject received = GSON.fromJson(message, JsonObject.class);
		JsonObject receivedPayload = received.get("payload").getAsJsonObject();
		String receivedName = receivedPayload.get("name").getAsString();
		int receivedId = receivedPayload.get("id").getAsInt();
		int msgType = received.get("type").getAsInt();
		String receivedRoomURL = receivedPayload.get("roomURL").getAsString();

		// if is a new user
		if (msgType == MESSAGE_TYPE.ADDTOROOM.ordinal()) {

			myChatroomMaps.getSessionToURL().put(session, receivedRoomURL);
			if (!myChatroomMaps.getUrlToQueueOfSessions().containsKey(receivedRoomURL)) {
				myChatroomMaps.getUrlToQueueOfSessions().put(receivedRoomURL, new ConcurrentLinkedQueue<Session>());
			}

			if (!myChatroomMaps.getUrlToMsgs().containsKey(receivedRoomURL)) {
				myChatroomMaps.getUrlToMsgs().put(receivedRoomURL, new ArrayList<Message>());
			}

			Queue<Session> myQueue = myChatroomMaps.getUrlToQueueOfSessions().get(receivedRoomURL);
			myQueue.add(session);

			addPreviousMessages(session, receivedRoomURL);
			return;
		}

		// could have just directly asked for the name to

		assert msgType == MESSAGE_TYPE.SEND.ordinal();

		String userText = receivedPayload.get("text").getAsString();

		Date now = new Date();

		// construct a message
		Message msg = new Message();
		msg.setContent(userText);
		msg.setSenderName(receivedName);
		msg.setDate(now);
		msg.setSenderId(receivedId);
		// add the msg to our list of msgs
		List<Message> msgsInRoom = myChatroomMaps.getUrlToMsgs().get(receivedRoomURL);
		msgsInRoom.add(msg);

		// wanna iterate through all messages and
		SimpleDateFormat dataFormat = new SimpleDateFormat("h:m a");
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
		// might have to do something here?
		System.out.println(suggestions + "wesdfasdfsd");
		payLoadObject.add("suggestions", suggestions);
		payLoadObject.add("rests", rests);

		updatedObject.add("payload", payLoadObject);
		Queue<Session> myQueue = myChatroomMaps.getUrlToQueueOfSessions().get(receivedRoomURL);
		// tell all sessions (which share the same url) about the new msg we received
		for (Session sesh : myQueue) {
			sesh.getRemote().sendString(GSON.toJson(updatedObject));
		}
	}

	private List<Restaurant> getRestaurantList(String receivedRoomURL) {
		int index = receivedRoomURL.lastIndexOf('?') + 1;
		String uuidString = receivedRoomURL.substring(index, receivedRoomURL.length());
		UUID id = UUID.fromString(uuidString);

		return myChatroomMaps.getUuidToRestaurants().get(id);
	}

	// added to ease adding the restaurants
	private UUID getUuid(String receivedRoomURL) {
		int index = receivedRoomURL.lastIndexOf('?') + 1;
		String uuidString = receivedRoomURL.substring(index, receivedRoomURL.length());
		UUID id = UUID.fromString(uuidString);
		return id;
	}

}

package edu.brown.cs.mldm.chatroom;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
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

//TODO: ask why this annotation is necessary <-- what does it even do?
@WebSocket
public class ChatWebSocket {
	private static final Gson GSON = new Gson();
	private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();
	private static int nextId = 0;
	private List<Message> allMessages = new ArrayList<Message>();
	private Map<Integer, String> idsToName = new HashMap<Integer, String>();
	private Map<Session, Integer> sessionToId = new HashMap<Session, Integer>();

	private static enum MESSAGE_TYPE {
		CONNECT, SEND, UPDATE, DELETE, UPDATEALLNAMES
	}

	// called in server
	public void addName(String name) {
		idsToName.put(nextId, name);
	}

	@OnWebSocketConnect
	public void connected(Session session) throws IOException {
		System.out.println("Web socket connected");
		sessions.add(session);

		JsonObject jObject = new JsonObject();
		jObject.addProperty("type", MESSAGE_TYPE.CONNECT.ordinal());

		JsonObject payLoadObject = new JsonObject();
		payLoadObject.addProperty("id", nextId);
		payLoadObject.addProperty("myName", idsToName.get(nextId));

		sessionToId.put(session, nextId);

		JsonArray dates = new JsonArray();
		JsonArray names = new JsonArray();
		JsonArray ids = new JsonArray();
		JsonArray content = new JsonArray();

		// we're adding all previous msgs to the payload
		// this is because a new user should know all previous msgs in chatroom
		for (Message msg : allMessages) {
			Date unParsedDate = msg.getDate();
			SimpleDateFormat dataFormat = new SimpleDateFormat("h:m a");
			String strId = msg.getSender();
			Integer id = Integer.parseInt(strId);

			String StringId = strId;
			String stringDate = dataFormat.format(unParsedDate);
			String stringName = idsToName.get(id);
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

		addUniqueNames(session); // helper func: THIS ADDS ALL THE UNIQUE USERS

		jObject.add("payload", payLoadObject);

		session.getRemote().sendString(GSON.toJson(jObject));
		nextId++;
	}

	public void addUniqueNames(Session session) throws IOException {
		System.out.println("add unique names called");
		JsonObject jObject = new JsonObject();
		jObject.addProperty("type", MESSAGE_TYPE.UPDATEALLNAMES.ordinal());

		JsonArray uniqueNames = new JsonArray();

		// need to add unique names
		for (String uniqueName : idsToName.values()) {
			uniqueNames.add(uniqueName);
		}

		jObject.add("uniqueNames", uniqueNames);

		// broadcasting to every1 that there's a new user - and we should handle it
		for (Session sesh : sessions) {
			sesh.getRemote().sendString(GSON.toJson(jObject));
		}

	}

	// when the server gets a msg that one of the clients has closed/left
	@OnWebSocketClose
	public void closed(Session session, int statusCode, String reason) throws IOException {
		System.out.println("closing starting");
		int idSession = sessionToId.get(session);
		sessionToId.remove(session);
		String nameOfClosedUser = idsToName.get(idSession);
		idsToName.remove(idSession);

		sessions.remove(session);

		// broadcast to every1 to delete the - already - closed user from
		// active users list
		for (Session sesh : sessions) {
			System.out.println("broadcasting to remaining sessions");
			JsonObject updatedObject = new JsonObject();
			updatedObject.addProperty("type", MESSAGE_TYPE.DELETE.ordinal());
			updatedObject.addProperty("name", nameOfClosedUser);
			sesh.getRemote().sendString(GSON.toJson(updatedObject));
		}

	}

	// when we receive a message from the client
	@OnWebSocketMessage
	public void message(Session session, String message) throws IOException {

		JsonObject received = GSON.fromJson(message, JsonObject.class);
		JsonObject receivedPayload = received.get("payload").getAsJsonObject();
		int receivedId = receivedPayload.get("id").getAsInt();

		int msgType = received.get("type").getAsInt();
		System.out.println("received msg");

		assert msgType == MESSAGE_TYPE.SEND.ordinal();

		String userText = receivedPayload.get("text").getAsString();
		System.out.println("my userText is: " + userText);

		String receivedStringId = Integer.toString(receivedId);
		String receivedName = idsToName.get(receivedId);

		Date now = new Date();

		// construct a message
		Message msg = new Message();
		msg.setContent(userText);
		msg.setSender(receivedStringId);
		msg.setDate(now);
		// add the msg to our list of msgs
		allMessages.add(msg);

		// wanna iterate through all messages and
		SimpleDateFormat dataFormat = new SimpleDateFormat("h:m a");
		String date = dataFormat.format(now);

		JsonObject updatedObject = new JsonObject();
		updatedObject.addProperty("type", MESSAGE_TYPE.UPDATE.ordinal());

		JsonObject payLoadObject = new JsonObject();

		payLoadObject.addProperty("id", receivedId);
		payLoadObject.addProperty("text", userText);
		payLoadObject.addProperty("date", date);
		payLoadObject.addProperty("name", receivedName);

		updatedObject.add("payload", payLoadObject);

		// tell all sessions about the new msg we received
		for (Session sesh : sessions) {
			sesh.getRemote().sendString(GSON.toJson(updatedObject));
		}
	}
}

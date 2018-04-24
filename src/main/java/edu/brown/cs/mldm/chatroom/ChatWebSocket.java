package edu.brown.cs.mldm.chatroom;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import edu.brown.cs.mldm.model.Message;

//TODO: ask why this annotation is necessary <-- what does it even do?
@WebSocket
public class ChatWebSocket {
	private static final Gson GSON = new Gson();
	private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();
	private static int nextId = 0;
	private List<Message> allMessages = new ArrayList<Message>();

	private static enum MESSAGE_TYPE {
		CONNECT, SEND, UPDATE
	}

	@OnWebSocketConnect
	public void connected(Session session) throws IOException {
		System.out.println("Web socket connected");
		sessions.add(session);

		JsonObject jObject = new JsonObject();
		jObject.addProperty("type", MESSAGE_TYPE.CONNECT.ordinal());

		JsonObject payLoadObject = new JsonObject();
		payLoadObject.addProperty("id", nextId);

		jObject.add("payload", payLoadObject);

		session.getRemote().sendString(GSON.toJson(jObject));
		nextId++;
	}

	@OnWebSocketClose
	public void closed(Session session, int statusCode, String reason) {
		sessions.remove(session);
		// TODO Remove the session from the queue
	}

	@OnWebSocketMessage
	public void message(Session session, String message) throws IOException {

		JsonObject received = GSON.fromJson(message, JsonObject.class);
		assert received.get("type").getAsInt() == MESSAGE_TYPE.SEND.ordinal();

		JsonObject receivedPayload = received.get("payload").getAsJsonObject();
		String userText = receivedPayload.get("text").getAsString();
		System.out.println("my userText is: " + userText);

		int receivedId = receivedPayload.get("id").getAsInt();

		String receivedStringId = Integer.toString(receivedId);

		Date now = new Date();
		// construct a message
		Message msg = new Message();

		msg.setContent(userText);
		msg.setSender(receivedStringId);
		msg.setDate(now);
		allMessages.add(msg);

		// wanan iteraete through all messages and
		SimpleDateFormat dataFormat = new SimpleDateFormat("h:m a");
		String date = dataFormat.format(now);
		System.out.println("date is: " + date);

		JsonObject updatedObject = new JsonObject();
		updatedObject.addProperty("type", MESSAGE_TYPE.UPDATE.ordinal());

		JsonObject payLoadObject = new JsonObject();

		payLoadObject.addProperty("id", receivedId);
		payLoadObject.addProperty("text", userText);
		payLoadObject.addProperty("date", date);

		updatedObject.add("payload", payLoadObject);

		for (Session sesh : sessions) {
			System.out.println("brodcasting");
			sesh.getRemote().sendString(GSON.toJson(updatedObject));
		}
	}
}

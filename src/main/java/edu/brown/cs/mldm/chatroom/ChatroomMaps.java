package edu.brown.cs.mldm.chatroom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

import org.eclipse.jetty.websocket.api.Session;

import edu.brown.cs.mldm.model.Message;
import edu.brown.cs.mldm.yelp.Restaurant;

/*
 * Holds all the different hashmaps for the ChatWebSocket
 */
public class ChatroomMaps {
	private final Map<Integer, String> idsToName = new HashMap<Integer, String>();
	private final Map<Session, Integer> sessionToId = new HashMap<Session, Integer>();
	private final Map<Session, String> sessionToURL = new HashMap<Session, String>();
	private final Map<UUID, List<Restaurant>> uuidToRestaurants = new HashMap<>();
	private final Map<UUID, List<Restaurant>> uuidToPriceRestaurants = new HashMap<>();
	private final Map<UUID, List<Restaurant>> uuidToDistRestaurants = new HashMap<>();
	private final Map<String, List<Message>> urlToMsgs = new HashMap<String, List<Message>>();
	private final Map<String, Queue<Session>> urlToQueueOfSessions = new HashMap<String, Queue<Session>>();
	
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
	
	public  Map<UUID, List<Restaurant>> uuidToDistRestaurants() {
		return uuidToDistRestaurants;
	}
	
	public Map<String, List<Message>> getUrlToMsgs() {
		return urlToMsgs;
	}
	
	public Map<String, Queue<Session>> getUrlToQueueOfSessions() {
		return urlToQueueOfSessions;
	}
	
	
}

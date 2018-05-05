package edu.brown.cs.mldm.model;

import java.util.Date;

import edu.brown.cs.mldm.yelp.Restaurant;

public class Message {

	private String content;
	private String senderName;
	private int senderId;
	private Date date;
	private int tracker;

	public final String getContent() {
		return content;
	}

	public final String getSenderName() {
		return senderName;
	}

	public final int getSenderId() {
		return senderId;
	}
	

	public final Date getDate() {
		return date;
	}
	
	public final void setContent(final String content) {
		this.content = content;
	}
	
	
	public final void setSenderId(final int senderId) {
		this.senderId = senderId;
	}

	public final void setSenderName(final String sender) {
		this.senderName = sender;
	}

	public final void setDate(final Date date) {
		this.date = date;
	}
	
	 @Override
	 public boolean equals(Object msg) {

	    return this.getContent().equals(((Message)  msg).getContent());
	  }

}

package edu.brown.cs.mldm.model;

import java.util.Date;

public class Message {

	private String content;
	private String sender;
	private Date date;
	private int tracker;

	public final String getContent() {
		return content;
	}

	public final void setContent(final String content) {
		this.content = content;
	}

	public final String getSender() {
		return sender;
	}

	public final void setSender(final String sender) {
		this.sender = sender;
	}

	public final Date getDate() {
		return date;
	}

	public final void setDate(final Date date) {
		this.date = date;
	}

}

package com.asmack.model;

import org.jivesoftware.smack.packet.RosterPacket;

/**
 * @author zhanghaitao
 * @date 2011-7-10
 * @version 1.0
 */
public class User {
	private String name;
	private String user;
	private RosterPacket.ItemType type;
	private int size;
	private String status;
	private String from;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public RosterPacket.ItemType getType() {
		return type;
	}

	public void setType(RosterPacket.ItemType type) {
		this.type = type;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

}

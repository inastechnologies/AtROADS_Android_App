package com.inas.atroads.views.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Req_GetEmergencyContacts {
	
	@SerializedName("user_id")
	@Expose
	private Integer userId;
	
	/**
	 * No args constructor for use in serialization
	 *
	 */
	public Req_GetEmergencyContacts() {
	}
	
	/**
	 *
	 * @param userId
	 */
	public Req_GetEmergencyContacts(Integer userId) {
		super();
		this.userId = userId;
	}
	
	public Integer getUserId() {
		return userId;
	}
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
}
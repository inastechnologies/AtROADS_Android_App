package com.inas.atroads.views.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Req_EditEmergencyContact {
	
	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("mobile_number")
	@Expose
	private String mobileNumber;
	@SerializedName("email_id")
	@Expose
	private String emailId;
	@SerializedName("user_id")
	@Expose
	private Integer userId;
	@SerializedName("id")
	@Expose
	private Integer id;
	
	/**
	 * No args constructor for use in serialization
	 *
	 */
	public Req_EditEmergencyContact() {
	}
	
	/**
	 *
	 * @param mobileNumber
	 * @param name
	 * @param emailId
	 * @param id
	 * @param userId
	 */
	public Req_EditEmergencyContact(String name, String mobileNumber, String emailId, Integer userId, Integer id) {
		super();
		this.name = name;
		this.mobileNumber = mobileNumber;
		this.emailId = emailId;
		this.userId = userId;
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getMobileNumber() {
		return mobileNumber;
	}
	
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	
	public String getEmailId() {
		return emailId;
	}
	
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	public Integer getUserId() {
		return userId;
	}
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
}
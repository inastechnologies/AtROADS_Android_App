package com.inas.atroads.views.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Res_EditEmergencyContact {
	
	@SerializedName("message")
	@Expose
	private String message;
	@SerializedName("result")
	@Expose
	private List<Result> result = null;
	@SerializedName("status")
	@Expose
	private Integer status;
	
	/**
	 * No args constructor for use in serialization
	 *
	 */
	public Res_EditEmergencyContact() {
	}
	
	/**
	 *
	 * @param result
	 * @param message
	 * @param status
	 */
	public Res_EditEmergencyContact(String message, List<Result> result, Integer status) {
		super();
		this.message = message;
		this.result = result;
		this.status = status;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public List<Result> getResult() {
		return result;
	}
	
	public void setResult(List<Result> result) {
		this.result = result;
	}
	
	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	
public class Result
{
	
	@SerializedName("email_id")
	@Expose
	private String emailId;
	@SerializedName("mobile_number")
	@Expose
	private String mobileNumber;
	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("user_id")
	@Expose
	private Integer userId;
	
	/**
	 No args constructor for use in serialization
	 */
	public Result()
	{
	}
	
	/**
	 *
	 */
	public Result(String emailId,String mobileNumber,String name,Integer userId)
	{
		super();
		this.emailId = emailId;
		this.mobileNumber = mobileNumber;
		this.name = name;
		this.userId = userId;
	}
	
	public String getEmailId()
	{
		return emailId;
	}
	
	public void setEmailId(String emailId)
	{
		this.emailId = emailId;
	}
	
	public String getMobileNumber()
	{
		return mobileNumber;
	}
	
	public void setMobileNumber(String mobileNumber)
	{
		this.mobileNumber = mobileNumber;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public Integer getUserId()
	{
		return userId;
	}
	
	public void setUserId(Integer userId)
	{
		this.userId = userId;
	}
}
}
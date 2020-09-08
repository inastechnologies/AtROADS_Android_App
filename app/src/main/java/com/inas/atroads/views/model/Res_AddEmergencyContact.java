package com.inas.atroads.views.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Res_AddEmergencyContact {
	
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
	public Res_AddEmergencyContact() {
	}
	
	/**
	 *
	 * @param result
	 * @param message
	 * @param status
	 */
	public Res_AddEmergencyContact(String message, List<Result> result, Integer status) {
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
	


public class Result {
	
	@SerializedName("sos")
	@Expose
	private Sos sos;
	
	/**
	 * No args constructor for use in serialization
	 *
	 */
	public Result() {
	}
	
	/**
	 *
	 * @param sos
	 */
	public Result(Sos sos) {
		super();
		this.sos = sos;
	}
	
	public Sos getSos() {
		return sos;
	}
	
	public void setSos(Sos sos) {
		this.sos = sos;
	}
	
}

public class Sos
{
	
	@SerializedName("email_id")
	@Expose
	private String emailId;
	@SerializedName("id")
	@Expose
	private Integer id;
	@SerializedName("mobile_number")
	@Expose
	private String mobileNumber;
	@SerializedName("name")
	@Expose
	private String name;
	
	/**
	 No args constructor for use in serialization
	 */
	public Sos()
	{
	}
	
	/**
	 *
	 */
	public Sos(String emailId,Integer id,String mobileNumber,String name)
	{
		super();
		this.emailId = emailId;
		this.id = id;
		this.mobileNumber = mobileNumber;
		this.name = name;
	}
	
	public String getEmailId()
	{
		return emailId;
	}
	
	public void setEmailId(String emailId)
	{
		this.emailId = emailId;
	}
	
	public Integer getId()
	{
		return id;
	}
	
	public void setId(Integer id)
	{
		this.id = id;
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
}
}
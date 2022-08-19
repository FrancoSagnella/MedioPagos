package com.example.demo.clasesMercadoPago;

public class NotificacionMP {
	private Long id;
	private String type;
  	private String date_created;
  	private Long application_id;
  	private Long user_id;
  	private String action;
  	private DataNotificacionMP data;
  	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDate_created() {
		return date_created;
	}
	public void setDate_created(String date_created) {
		this.date_created = date_created;
	}
	public Long getApplication_id() {
		return application_id;
	}
	public void setApplication_id(Long application_id) {
		this.application_id = application_id;
	}
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public DataNotificacionMP getData() {
		return data;
	}
	public void setData(DataNotificacionMP data) {
		this.data = data;
	}
}

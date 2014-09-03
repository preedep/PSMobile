package com.epro.psmobile.sync;

import android.content.Context;

public class UserAuthen {

	private String userName;
	private String password;
	private String deviceId;
	private String locale = "041e";
	
	private Context context;
	public UserAuthen(Context context) {
		// TODO Auto-generated constructor stub
		this.setContext(context);
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

}

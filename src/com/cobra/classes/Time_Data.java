package com.cobra.classes;

public class Time_Data {

	private String time=null;
	private Boolean IsRemoved=false;
	
	public Boolean IsRemoved() {
		return IsRemoved;
	}

	public void IsRemoved(Boolean isRemoved) {
		IsRemoved = isRemoved;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
}

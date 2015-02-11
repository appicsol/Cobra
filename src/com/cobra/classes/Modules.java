package com.cobra.classes;

import java.util.ArrayList;

public class Modules {
	private String Address;
	private int Channel=0;
	private Boolean Active;
	ModuleCues ModuleCues;
	
	public ModuleCues getModuleCues() {
		return ModuleCues;
	}
	public void setModuleCues(ModuleCues moduleCues) {
		ModuleCues = moduleCues;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		this.Address = address;
	}
	public int getChannel() {
		return Channel;
	}
	public void setChannel(int channel) {
		this.Channel = channel;
	}
	public Boolean getActive() {
		return Active;
	}
	public void setAcive(Boolean active) {
		this.Active = active;
	}
}
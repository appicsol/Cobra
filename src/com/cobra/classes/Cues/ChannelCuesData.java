package com.cobra.classes.Cues;

import java.util.ArrayList;

public class ChannelCuesData {

	public ChannelCuesData(){
		
	}
	private int channelID;
	private ArrayList<ScriptData> scriptDataList;

	public int getChannelID() {
		return channelID;
	}

	public void setChannelID(int channelID) {
		this.channelID = channelID;
	}

	public ArrayList<ScriptData> getScriptDataList() {
		return scriptDataList;
	}

	public void setScriptDataList(ArrayList<ScriptData> scriptDataList) {
		this.scriptDataList = scriptDataList;
	}

}

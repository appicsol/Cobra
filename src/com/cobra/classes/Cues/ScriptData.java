package com.cobra.classes.Cues;

import java.util.ArrayList;


public class ScriptData {
	private int scriptID;
	private ArrayList<CuesData> cuesDataList;

	public int getScriptID() {
		return scriptID;
	}

	public void setScriptID(int scriptID) {
		this.scriptID = scriptID;
	}

	public ArrayList<CuesData> getCuesDataList() {
		return cuesDataList;
	}

	public void setCuesDataList(ArrayList<CuesData> cuesDataList) {
		this.cuesDataList = cuesDataList;
	}

}

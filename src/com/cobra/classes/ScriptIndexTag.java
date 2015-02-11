package com.cobra.classes;

import java.util.Arrays;

public class ScriptIndexTag {
	/** The trigger button1. */
	private int triggerButton1;

	/** The trigger button2. */
	private int triggerButton2;

	/** The trigger channel. */
	private int triggerChannel;

	/** The end channel. */
	private int endChannel;
	/**
	 * Is the value of time-discrete events stored in the script. This value
	 * includes step events, and only counts one event for overlapping fires
	 * (fires on multiple cues or channels) occurring at the same time.
	 * 
	 **/
	private int numEvents;

	/** The audiobox enable. */
	private Boolean audioboxEnable;
	/** NOT IMPLEMENTED. The audio filename. */
	private String audioFilename;

	/** The script name. */
	private String scriptName;

	/** The script id. */
	private int scriptID;

	public ScriptIndexTag(ScriptIndexTag tag) {
		setScriptID(tag.getScriptID());
		setTriggerButton1(tag.getTriggerButton1());
		setTriggerButton2(tag.getTriggerButton2());
		setTriggerChannel(tag.getTriggerChannel());
		setEndChannel(tag.getEndChannel());
		setNumEvents(tag.getNumEvents());
		setAudioboxEnable(tag.getAudioboxEnable());
		setAudioFilename(tag.getAudioFilename());
		setScriptName(tag.getScriptName());
	}

	public ScriptIndexTag() {
		// TODO Auto-generated constructor stub
	}

	public int getTriggerButton1() {
		return triggerButton1;
	}

	public void setTriggerButton1(int triggerButton1) {
		this.triggerButton1 = triggerButton1;
	}

	public int getTriggerButton2() {
		return triggerButton2;
	}

	public void setTriggerButton2(int triggerButton2) {
		this.triggerButton2 = triggerButton2;
	}

	public int getTriggerChannel() {
		return triggerChannel;
	}

	public void setTriggerChannel(int triggerChannel) {
		this.triggerChannel = triggerChannel;
	}

	public int getEndChannel() {
		return endChannel;
	}

	public void setEndChannel(int endChannel) {
		this.endChannel = endChannel;
	}

	public int getNumEvents() {
		return numEvents;
	}

	public void setNumEvents(int numEvents) {
		this.numEvents = numEvents;
	}

	public Boolean getAudioboxEnable() {
		return audioboxEnable;
	}

	public void setAudioboxEnable(Boolean audioboxEnable) {
		this.audioboxEnable = audioboxEnable;
	}

	public String getAudioFilename() {
		return audioFilename;
	}

	public void setAudioFilename(String audioFilename) {
		this.audioFilename = audioFilename;
	}

	public String getScriptName() {
		return scriptName;
	}

	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}

	public int getScriptID() {
		return scriptID;
	}

	public void setScriptID(int scriptID) {
		this.scriptID = scriptID;
	}

}

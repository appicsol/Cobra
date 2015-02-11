package com.cobra.views.modulelist;

import java.util.ArrayList;

import com.cobra.appClass;
import com.cobra.api.CobraDataTags.ChannelDataTag;
import com.cobra.api.CobraDataTags.ModuleDataTag;

public class ModuleListItem {
	private ModuleDataTag moduleTag;
	private ChannelDataTag channelTag;
	private boolean active;
	private ModuleRow currentUiRow;
	private ArrayList<String> lightStatus;

	public ModuleListItem(ModuleDataTag moduleTag, ChannelDataTag channelTag,
			ArrayList<String> lightStatus, boolean active) {

		this.moduleTag = moduleTag;
		this.channelTag = channelTag;
		this.active = active;
		this.lightStatus = lightStatus;
	}

	public ArrayList<String> getLightStatus() {
		return lightStatus;
	}

	public void setLightStatus(ArrayList<String> lightStatus) {
		this.lightStatus = lightStatus;
	}

	public ModuleDataTag getModuleTag() {
		return this.moduleTag;
	}

	public ChannelDataTag getChannelTag() {
		return this.channelTag;
	}

	// public void setActive(boolean active) {
	// this.active = active;
	// }

	// public boolean isActive() {
	// return this.active;
	// }

	public void setModuleTag(ModuleDataTag moduleTag) {
		this.moduleTag = moduleTag;
	}

	public void setChannelTag(ChannelDataTag channelTag) {
		this.channelTag = channelTag;
	}

	public void setUIROw(ModuleRow row) {
		this.currentUiRow = row;

	}

	public ModuleRow getUIROw() {
		return this.currentUiRow;

	}

	// public boolean setNewUIData() {
	// if (currentUiRow == null)
	// return false;
	// try {
	// currentUiRow.updateViewData(getModuleTag(), isActive(),
	// getChannelTag());
	// } catch (Exception ex) {
	// return false;
	// }
	// return true;
	// }
}

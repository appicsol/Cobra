package com.cobra.module.interfaces;

import java.util.ArrayList;

import com.cobra.api.CobraDataTags.ModuleDataTag;

public interface OnModuleInformationUpdate {
	public void onRemoveAllModule();

	public void onModuleStatusUpdate(ModuleDataTag moduleDataTag,
			ArrayList<String> lightStatus);
}

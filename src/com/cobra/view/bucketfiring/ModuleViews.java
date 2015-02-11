package com.cobra.view.bucketfiring;

import android.widget.TextView;

public class ModuleViews {

	private TextView ModuleAreaChannel = null;
	private TextView BucketAreaChannel = null;
	private int ModuleChannel = -1;
	private Boolean IsActive = false;

	public Boolean getIsActive() {
		return IsActive;
	}

	public void setIsActive(Boolean isActive) {
		IsActive = isActive;
	}

	public int getModuleChannel() {
		return ModuleChannel;
	}

	public void setModuleChannel(int moduleChannel) {
		ModuleChannel = moduleChannel;
	}

	public TextView getModuleAreaChannel() {
		return ModuleAreaChannel;
	}

	public void setModuleAreaChannel(TextView moduleAreaChannel) {
		ModuleAreaChannel = moduleAreaChannel;
	}

	public TextView getBucketAreaChannel() {
		return BucketAreaChannel;
	}

	public void setBucketAreaChannel(TextView bucketAreaChannel) {
		BucketAreaChannel = bucketAreaChannel;
	}

}

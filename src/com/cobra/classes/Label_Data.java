package com.cobra.classes;

public class Label_Data {

	private String labelName=null;
	private Boolean IsRemoved=false;
	
	public Boolean IsRemoved() {
		return IsRemoved;
	}

	public void IsRemoved(Boolean isRemoved) {
		IsRemoved = isRemoved;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabel_Name(String label_Name) {
		this.labelName = label_Name;
	}
	
}

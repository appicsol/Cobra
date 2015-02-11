package com.cobra.showcontrol.interfaces;


public interface OnWrongScriptListener {

	public void onWrongScriptStop();

	public void onWrongScriptClose();

	public void onWrongScriptContinue(int scriptIndex);
}

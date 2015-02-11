package com.cobra.views.modulelist;

import java.util.List;

import com.cobra.api.Cobra.ModuleType;
import com.cobra.api.CobraDataTags.ModuleDataTag;

import android.widget.LinearLayout;
import android.widget.TextView;

public class ModuleUIRow {

	private TextView tvDevice, tvAddress, tvChannel, tvKeyPosition, tvMode,
			tvSignal, tvBattery1, tvBattery2;
	private LinearLayout cuesParent;
	private List<TextView> tvCues;
	private LinearLayout content;
	private LinearLayout parentLayout;
	private int channel;

	private ModuleListItem moduleListItem;
	private int position;
	private int moduleid;
	private ModuleType Device;// - AB, 18M, 36M, 90M
	private int Address;// - A00, A01, A02, etc.
	private boolean Key_Pos;// - ARM, TEST
	private boolean Mode;// ARM, TEST
	private int Signal;// - -70, -60, -50, etc.
	private int Power_1;// 1P & 2P - 0, 1, 2, 3 etc.
	private int Power_2;
	private boolean HaveExtraCues;

	public ModuleType getDevice() {
		return Device;
	}

	public Boolean getHaveExtraCues() {
		return HaveExtraCues;
	}

	public void setHaveExtraCues(Boolean haveExtraCues) {
		HaveExtraCues = haveExtraCues;
	}

	public void setDevice(ModuleType eighteenM) {
		Device = eighteenM;
	}

	public int getAddress() {
		return Address;
	}

	public void setAddress(int address) {
		Address = address;
	}

	public Boolean getKey_Pos() {
		return Key_Pos;
	}

	public void setKey_Pos(Boolean keyPos) {
		Key_Pos = keyPos;
	}

	public Boolean getMode() {
		return Mode;
	}

	public void setMode(Boolean armed) {
		Mode = armed;
	}

	public int getSignal() {
		return Signal;
	}

	public void setSignal(int signal) {
		Signal = signal;
	}

	public int getPower_1() {
		return Power_1;
	}

	public void setPower_1(int power_1) {
		Power_1 = power_1;
	}

	public int getPower_2() {
		return Power_2;
	}

	public void setPower_2(int power_2) {
		Power_2 = power_2;
	}

	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public ModuleListItem getModuleListItem() {
		return moduleListItem;
	}

	public void setModuleListItem(ModuleListItem moduleListItem) {
		this.moduleListItem = moduleListItem;
	}

	public int getModuleid() {
		return moduleid;
	}

	public void setModuleid(int moduleid) {
		this.moduleid = moduleid;
	}

	public TextView getTvDevice() {
		return tvDevice;
	}

	public void setTvDevice(TextView tvDevice) {
		this.tvDevice = tvDevice;
	}

	public TextView getTvAddress() {
		return tvAddress;
	}

	public void setTvAddress(TextView tvAddress) {
		this.tvAddress = tvAddress;
	}

	public TextView getTvChannel() {
		return tvChannel;
	}

	public void setTvChannel(TextView tvChannel) {
		this.tvChannel = tvChannel;
	}

	public TextView getTvKeyPosition() {
		return tvKeyPosition;
	}

	public void setTvKeyPosition(TextView tvKeyPosition) {
		this.tvKeyPosition = tvKeyPosition;
	}

	public TextView getTvMode() {
		return tvMode;
	}

	public void setTvMode(TextView tvMode) {
		this.tvMode = tvMode;
	}

	public TextView getTvSignal() {
		return tvSignal;
	}

	public void setTvSignal(TextView tvSignal) {
		this.tvSignal = tvSignal;
	}

	public TextView getTvBattery1() {
		return tvBattery1;
	}

	public void setTvBattery1(TextView tvBattery1) {
		this.tvBattery1 = tvBattery1;
	}

	public TextView getTvBattery2() {
		return tvBattery2;
	}

	public void setTvBattery2(TextView tvBattery2) {
		this.tvBattery2 = tvBattery2;
	}

	public LinearLayout getCuesParent() {
		return cuesParent;
	}

	public void setCuesParent(LinearLayout cuesParent) {
		this.cuesParent = cuesParent;
	}

	public List<TextView> getTvCues() {
		return tvCues;
	}

	public void setTvCues(List<TextView> tvCues) {
		this.tvCues = tvCues;
	}

	public LinearLayout getContent() {
		return content;
	}

	public void setContent(LinearLayout content) {
		this.content = content;
	}

	public LinearLayout getParentLayout() {
		return parentLayout;
	}

	public void setParentLayout(LinearLayout parentLayout) {
		this.parentLayout = parentLayout;
	}

}

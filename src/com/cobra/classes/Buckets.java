package com.cobra.classes;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Buckets {

	private String bucketName = null;
	private List<Modules> bucketModules = null;
	private List<String> bucketCues = null;
	private List<Label_Data> bucketLabels = null;
	private TextView fired_cues=null;
	private TextView remain_cues=null;
	private Boolean IsPaused=true;
	private Button btn_fired=null;
	private Button btn_pause=null;
	private int time=0;
	private String status="Sequential";
	private View view;

	public View getView() {
		return view;
	}
	public void setView(View view) {
		this.view = view;
	}
	public int getBucketTime() {
		return time;
	}
	public void setBucketTime(int time) {
		this.time = time;
	}
	public String getBucketStatus() {
		return status;
	}
	public void setBucketStatus(String status) {
		this.status = status;
	}
	public Boolean IsPaused(){
		return IsPaused;
	}
	public Button get_BtnFired() {
		return btn_fired;
	}
	public void set_BtnFired(Button btn_fired) {
		this.btn_fired = btn_fired;
	}
	public Button get_BtnPause() {
		return btn_pause;
	}
	public void set_BtnPause(Button btn_pause) {
		this.btn_pause = btn_pause;
	}
	public void setPaused(Boolean pause){
		IsPaused=pause;
	}
	public TextView getFiredCues() {
		return fired_cues;
	}

	public void setFiredCues(TextView fired_cues) {
		this.fired_cues = fired_cues;
	}

	public TextView getRemainCues() {
		return remain_cues;
	}

	public void setRemainCues(TextView remain_cues) {
		this.remain_cues = remain_cues;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public List<Modules> getBucketModules() {
		return bucketModules;
	}

	public void setBucketModules(List<Modules> bucketMudules) {
		this.bucketModules = bucketMudules;
	}

	public List<String> getBucketCues() {
		return bucketCues;
	}

	public void setBucketCues(List<String> bucketCues) {
		this.bucketCues = bucketCues;
	}

	public List<Label_Data> getBucketLabels() {
		return bucketLabels;
	}

	public void setBucketLabels(List<Label_Data> bucketShells) {
		this.bucketLabels = bucketShells;
	}

	public Boolean IsLabelExists(String label) {
		if (bucketLabels != null && bucketLabels.size() > 0) {
			for (int i = 0; i < bucketLabels.size(); i++) {
				if (bucketLabels.get(i).getLabelName().equals(label) && !bucketLabels.get(i).IsRemoved()) {
					return true;
				}
			}
		}
		if (bucketLabels == null)
			bucketLabels = new ArrayList<Label_Data>();
			Label_Data labelData=new Label_Data();
			labelData.IsRemoved(false);
			labelData.setLabel_Name(label);
		bucketLabels.add(labelData);
		return false;

	}
	
}

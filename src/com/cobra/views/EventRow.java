package com.cobra.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cobra.R;

public class EventRow extends FrameLayout {

	private TextView tvDescription, tvTime, tvCh, tvCues;
	private long eventTime = 0;
	private int eventIndex = 0;

	private LinearLayout content;

	public EventRow(Context context) {
		super(context);
		content = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.showcontrols_eventrow, null);
		this.addView(content);

		tvDescription = (TextView) content
				.findViewById(R.id.scriptDescTextView);
		tvTime = (TextView) content.findViewById(R.id.tvTime);
		tvCh = (TextView) content.findViewById(R.id.tvCh);
		tvCues = (TextView) content.findViewById(R.id.tvCues);

		updateViewData();
	}

	public void updateViewData() {

	}

	public long getEventTime() {
		return this.eventTime;
	}

	public void setEventTime(long eventTime) {
		this.eventTime = eventTime;
	}

	public int getEventIndex() {
		return eventIndex;
	}

	public void setEventIndex(int eventIndex) {
		this.eventIndex = eventIndex;
	}

}

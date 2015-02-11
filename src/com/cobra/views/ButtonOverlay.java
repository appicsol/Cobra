package com.cobra.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Spinner;

import com.cobra.MainActivity;
import com.cobra.R;
import com.cobra.api.Cobra;

public class ButtonOverlay extends GridLayout {

//	private Button menuButton, 
	private Button refreshButton, helpButton;
	private Spinner editTries;
	private GridLayout content;

	private Cobra cobra;
	private MainActivity activity;
	String[] items = new String[] { "2", "3", "4", "5", "6", "7", "8", "9",
			"10" };

	public ButtonOverlay(Context context, MainActivity activity) {
		super(context);
		content = (GridLayout) LayoutInflater.from(context).inflate(
				R.layout.button_overlay, null);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
				android.R.layout.simple_spinner_item, items);
		this.addView(content);
		editTries = (Spinner) content.findViewById(R.id.editTries);
//		menuButton = (Button) content.findViewById(R.id.menuButton);
		refreshButton = (Button) content.findViewById(R.id.refreshButton);
		helpButton = (Button) content.findViewById(R.id.helpButton);
		this.activity = activity;
		editTries.setAdapter(adapter);
		editTries.setOnItemSelectedListener(d);
		editTries.setSelection(2);
		refreshButton.setOnClickListener(refreshButtonListener);
	}

//	public Button getMenuButton() {
//		return menuButton;
//	}

	public Button getRefreshButton() {
		return refreshButton;
	}

	OnClickListener refreshButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			activity.refreshPressed();
		}
	};

	OnClickListener helpButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			activity.helpPressed();
		}
	};

	OnItemSelectedListener d = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
//			activity.setNoOfTries(items[arg2]);

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	};
}

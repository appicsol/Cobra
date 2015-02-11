package com.cobra.views;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cobra.R;
import com.cobra.appClass;
import com.cobra.api.Cobra;
import com.cobra.api.CobraCommands;
import com.cobra.api.Cobra.CobraEvent;
import com.cobra.api.Cobra.ICobraEventListener;

public class TempView extends FrameLayout {

	private TextView tv_battarylevel;
	private Button btn_GetBattayLevel;

	private LinearLayout content;
	ProgressDialog pd;
	Cobra cobra;

	public TempView(Context context, Activity activity, final Cobra cobra) {
		super(context);

		this.cobra = cobra;
		content = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.temp_battary_level, null);
		this.addView(content);

		tv_battarylevel = (TextView) content.findViewById(R.id.textView1);
		btn_GetBattayLevel = (Button) content.findViewById(R.id.button1);
		pd = new ProgressDialog(getContext());
		pd.setMessage("Getting Battary Level. \n Please wait...");
		pd.setCancelable(false);

		this.cobra.addEventListener(cobraEventListener);
		btn_GetBattayLevel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cobra.refreshModuleData();
				pd.show();
			}
		});
	}

	public TempView(Context context) {
		super(context);

	}

	ICobraEventListener cobraEventListener = new ICobraEventListener() {

		@Override
		public void onDeviceDataChange(CobraEvent event) {
			switch (event.getEventType()) {
			case Cobra.EVENT_TYPE_MODULE_DATA_CHANGE:
//				appClass.createS19Log("RECIEVED : event type : "
//						+ event.getEventType() + "-----API : "
//						+ Cobra.EVENT_TYPE_MODULE_DATA_CHANGE);
				int battary = cobra.getDeviceBatteryLevel();
				pd.dismiss();
				tv_battarylevel.setText("" + battary);
				break;
			default:
				pd.dismiss();
//				appClass.createS19Log("RECIEVED : event type : "
//						+ event.getEventType());
			}
			// TODO Auto-generated method stub

		}

	};
}

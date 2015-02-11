package com.cobra.dialogs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cobra.R;
import com.cobra.appClass;

public class DialogAbout extends DialogFragment {

	public static DialogAbout newInstance(int num,String deviceName,String firmware,String releaseDate) {
		DialogAbout f = new DialogAbout();

		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putInt("num", num);
		args.putString("deviceName", deviceName);
		args.putString("firmware", firmware);
		args.putString("releaseDate", releaseDate);
		f.setArguments(args);

		return f;
	}

	private int mNum;
	private Button btn_close;
	private TextView txt_Firmware;
	private TextView txt_DeviceType;
	String deviceName;
	String firmware;
	String releaseDate;
	private LinearLayout layout_Top;
	private TextView value_control_panel_firmware;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = View.inflate(getActivity(), R.layout.dialog_about,
				null);
		layout_Top = (LinearLayout) v.findViewById(R.id.layout_top);
		btn_close = (Button) v.findViewById(R.id.btn_close);
		txt_DeviceType=(TextView)v.findViewById(R.id.value_device_type);
		txt_Firmware=(TextView)v.findViewById(R.id.value_firmware);
		value_control_panel_firmware=(TextView)v.findViewById(R.id.value_control_panel_firmware);
		txt_DeviceType.setText(deviceName);
		txt_Firmware.setText(firmware);
		value_control_panel_firmware.setText("BETA v1.0");
		
		btn_close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogAbout.this.dismiss();
			}
		});

		layout_Top.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogAbout.this.dismiss();
			}
		});
		return v;
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
		super.onDismiss(dialog);
	}

	private int widthOfScreen;
	private int heightOfScreen;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mNum = getArguments().getInt("num");
		deviceName = getArguments().getString("deviceName");
		firmware = getArguments().getString("firmware");
		releaseDate = getArguments().getString("releaseDate");
		// Pick a style based on the num.
		int style = DialogFragment.STYLE_NORMAL, theme = 0;
		switch (2) {
		case 1:
			style = DialogFragment.STYLE_NO_TITLE;
			break;
		case 2:
			style = DialogFragment.STYLE_NO_FRAME;
			break;
		case 3:
			style = DialogFragment.STYLE_NO_INPUT;
			break;
		case 4:
			style = DialogFragment.STYLE_NORMAL;
			break;
		case 5:
			style = DialogFragment.STYLE_NORMAL;
			break;
		case 6:
			style = DialogFragment.STYLE_NO_TITLE;
			break;
		case 7:
			style = DialogFragment.STYLE_NO_FRAME;
			break;
		case 8:
			style = DialogFragment.STYLE_NORMAL;
			break;
		}
		switch (2) {
		case 4:
			theme = android.R.style.Theme_Holo;
			break;
		case 5:
			theme = android.R.style.Theme_Holo_Light_Dialog;
			break;
		case 6:
			theme = android.R.style.Theme_Holo_Light;
			break;
		case 7:
			theme = android.R.style.Theme_Holo_Light_Panel;
			break;
		case 8:
			theme = android.R.style.Theme_Holo_Light;
			break;
		}
		setStyle(style, theme);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		widthOfScreen = getResources().getDisplayMetrics().widthPixels;
		heightOfScreen = getResources().getDisplayMetrics().heightPixels;
		getDialog().getWindow().setLayout(widthOfScreen, heightOfScreen);
	}
}

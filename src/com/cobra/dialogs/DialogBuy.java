package com.cobra.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cobra.MainActivity;
import com.cobra.R;
import com.cobra.appClass;
import com.cobra.api.SerialDriver;
import com.cobra.interfaces.DemoModeListener;
import com.cobra.views.Connectivity;

public class DialogBuy extends DialogFragment {

	private LinearLayout layout_Top;

	TextView btn_proceed;
	appClass globV;

	DemoModeListener demo_mode_listener;

	public static DialogBuy newInstance(int num, String scriptName,
			int scriptIndex) {
		DialogBuy f = new DialogBuy();

		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putInt("num", num);
		args.putString("scriptName", scriptName);
		args.putInt("scriptIndex", scriptIndex);
		f.setArguments(args);

		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = View.inflate(getActivity(), R.layout.dialog_buy, null);

		// layout_Top = (LinearLayout) v.findViewById(R.id.layout_top);

		btn_proceed = (TextView) v.findViewById(R.id.btn_close);

		demo_mode_listener = Connectivity.getDemoListener();

		btn_proceed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferences sharedpreferences = getActivity()
						.getSharedPreferences("KEYPREFERENCES",
								Context.MODE_PRIVATE);
				Editor editor = sharedpreferences.edit();
				editor.putBoolean("NotFirstTime", true);
				editor.commit();
				
//				try {
//					if (globV != null && globV.serialPort != null) {
//						globV.serialPort.close();
//					}
//				} catch (Exception e) {
//
//				}

				int pid = android.os.Process.myPid();
				android.os.Process.killProcess(pid);
				System.exit(0);
				// demo_mode_listener.OnDemoModeListener();
				DialogBuy.this.dismiss();
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
	private int mNum;
	private String scriptName;
	private int scriptIndex;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mNum = getArguments().getInt("num");
		scriptName = getArguments().getString("scriptName");
		scriptIndex = getArguments().getInt("scriptIndex");

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

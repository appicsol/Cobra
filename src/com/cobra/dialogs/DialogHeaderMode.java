package com.cobra.dialogs;

import android.app.Activity;
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
import android.widget.ScrollView;
import android.widget.TextView;

import com.cobra.R;
import com.cobra.appClass;
import com.cobra.classes.Constants;
import com.cobra.classes.GenerateBackground;
import com.cobra.interfaces.onFilterMode;

public class DialogHeaderMode extends DialogFragment {

	public static DialogHeaderMode newInstance() {
		DialogHeaderMode f = new DialogHeaderMode();

		// Supply num input as an argument.
		Bundle args = new Bundle();
		f.setArguments(args);

		return f;
	}

//	private Button btn_FilterByMode;
	private Button btn_Close;
	private Button btn_ModeAll;
	private Button btn_ModeARM;
	private Button btn_ModeTEST;
	private appClass globV;
	private onFilterMode filterModeListener;
	LinearLayout layout_Top;

	public static final int MODE_ALL = 1;
	public static final int MODE_TEST = 2;
	public static final int MODE_ARM = 3;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = View.inflate(getActivity(), R.layout.dialog_header_module,
				null);
//		btn_FilterByMode = (Button) v.findViewById(R.id.btn_filter_by_mode);
		btn_Close = (Button) v.findViewById(R.id.btn_close);
		btn_ModeAll = (Button) v.findViewById(R.id.btn_filter_by_mode);
		layout_Top = (LinearLayout) v.findViewById(R.id.layout_top);
		btn_ModeARM = (Button) v.findViewById(R.id.btn_arm);
		btn_ModeTEST = (Button) v.findViewById(R.id.btn_test);
		globV = (appClass) getActivity().getApplicationContext();

		btn_ModeARM.setBackground(GenerateBackground.Background(getActivity(),
				Constants.RED_DARK, true, 1f, 1f, 0.8f, 0.8f));
		btn_ModeTEST.setBackground(GenerateBackground.Background(getActivity(),
				Constants.GREEN, true, 1f, 1f, 0.5f, 0.5f));
		filterModeListener = globV.getModeFilter();
 
		layout_Top.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogHeaderMode.this.dismiss();
			}
		});
		
		btn_Close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogHeaderMode.this.dismiss();
			}
		});
		btn_ModeAll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				filterModeListener.onModeChange(MODE_ALL);
				DialogHeaderMode.this.dismiss();
			}
		});
		btn_ModeARM.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				filterModeListener.onModeChange(MODE_ARM);
				DialogHeaderMode.this.dismiss();
			}
		});
		btn_ModeTEST.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				filterModeListener.onModeChange(MODE_TEST);
				DialogHeaderMode.this.dismiss();
			}
		});

//		layout_Top.measure(View.MeasureSpec.UNSPECIFIED,
//				View.MeasureSpec.UNSPECIFIED);
//		widthOfScreen = layout_Top.getMeasuredWidth();
//		heightOfScreen = layout_Top.getMeasuredHeight();
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
		getDialog().getWindow().setLayout(widthOfScreen,heightOfScreen);
	}
}

package com.cobra.dialogs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cobra.R;
import com.cobra.appClass;
import com.cobra.showcontrol.interfaces.OnWrongScriptListener;

public class DialogWrongScript extends DialogFragment {

	private Button btn_stop_script, btn_continue_script;
	private TextView value_wrong_script_msg1, value_wrong_script_msg2,
			value_wrong_script_msg3;

	private OnWrongScriptListener onWrongScriptListener;

	appClass globV;

	private LinearLayout layout_Top;

	public static DialogWrongScript newInstance(int num, String scriptName,
			int scriptIndex, String preScriptName) {
		DialogWrongScript f = new DialogWrongScript();

		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putInt("num", num);
		args.putString("scriptName", scriptName);
		args.putInt("scriptIndex", scriptIndex);
		args.putString("preScriptName", preScriptName);
		f.setArguments(args);

		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = View
				.inflate(getActivity(), R.layout.dialog_wrong_script, null);
		layout_Top = (LinearLayout) v.findViewById(R.id.layout_top);

		btn_stop_script = (Button) v.findViewById(R.id.btn_stop_script);
		btn_continue_script = (Button) v.findViewById(R.id.btn_continue_script);
		value_wrong_script_msg1 = (TextView) v
				.findViewById(R.id.value_wrong_script_msg1);
		value_wrong_script_msg2 = (TextView) v
				.findViewById(R.id.value_wrong_script_msg2);
		value_wrong_script_msg3 = (TextView) v
				.findViewById(R.id.value_wrong_script_msg3);

		btn_continue_script.setText("Continue Script and Goto Script: "
				+ scriptName);

		String text1, text2, text3;
		text1 = "The 18R2 is running:";
		text2 = "Script: " + scriptName;
		text3 = "which is different from the selected Script: " + preScriptName;
		value_wrong_script_msg1.setText(text1);
		value_wrong_script_msg2.setText(text2);
		value_wrong_script_msg3.setText(text3);

		onWrongScriptListener = globV.getOnWrongScriptListener();

		btn_continue_script.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onWrongScriptListener.onWrongScriptContinue(scriptIndex);
				DialogWrongScript.this.dismiss();
			}
		});

		btn_stop_script.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onWrongScriptListener.onWrongScriptStop();
				DialogWrongScript.this.dismiss();

			}
		});

		layout_Top.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogWrongScript.this.dismiss();
				onWrongScriptListener.onWrongScriptClose();
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
	private String scriptName, preScriptName;
	private int scriptIndex;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mNum = getArguments().getInt("num");
		scriptName = getArguments().getString("scriptName");
		scriptIndex = getArguments().getInt("scriptIndex");
		preScriptName = getArguments().getString("preScriptName");

		globV = (appClass) getActivity().getApplicationContext();
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

	public Button getBtn_stop_script() {
		return btn_stop_script;
	}

	public void setBtn_stop_script(Button btn_stop_script) {
		this.btn_stop_script = btn_stop_script;
	}

	public Button getBtn_continue_script() {
		return btn_continue_script;
	}

	public void setBtn_continue_script(Button btn_continue_script) {
		this.btn_continue_script = btn_continue_script;
	}

	// public TextView getValue_wrong_script_msg() {
	// return value_wrong_script_msg;
	// }
	//
	// public void setValue_wrong_script_msg(TextView value_wrong_script_msg) {
	// this.value_wrong_script_msg = value_wrong_script_msg;
	// }
}

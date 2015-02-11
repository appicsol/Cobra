package com.cobra.dialogs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.cobra.R;

public class DialogExceptionHelp extends DialogFragment {

	public static DialogExceptionHelp newInstance(int num) {
		DialogExceptionHelp f = new DialogExceptionHelp();

		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putInt("num", num);
		f.setArguments(args);

		return f;
	}

	private int mNum;
	private Button btn_close;
	private LinearLayout layout_Top;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = View.inflate(getActivity(), R.layout.dialog_exception_help,
				null);
		layout_Top = (LinearLayout) v.findViewById(R.id.layout_top);
		btn_close = (Button) v.findViewById(R.id.btn_close);
		btn_close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogExceptionHelp.this.dismiss();
			}
		});

		layout_Top.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogExceptionHelp.this.dismiss();
			}
		});

		// layout_Top.setLayoutParams(new
		// LayoutParams(LayoutParams.WRAP_CONTENT,
		// LayoutParams.WRAP_CONTENT));
		//
		// layout_Top.measure(0, 0);
		// widthOfScreen = layout_Top.getMeasuredWidth() + 40;
		// heightOfScreen = layout_Top.getMeasuredHeight() + 40;

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

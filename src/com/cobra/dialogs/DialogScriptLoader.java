package com.cobra.dialogs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cobra.MainActivity;
import com.cobra.R;
import com.cobra.appClass;
import com.cobra.views.ShowControls;

public class DialogScriptLoader extends DialogFragment {

	private static MainActivity activity;

	public static DialogScriptLoader newInstance(int num, MainActivity mactivity) {
		DialogScriptLoader f = new DialogScriptLoader();

		activity = mactivity;

		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putInt("num", num);
		f.setArguments(args);

		return f;
	}

	private int mNum;
	private TextView txt_Msg;
	private Button btn_Restart;
	private RelativeLayout layout_Top;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = View.inflate(getActivity(), R.layout.dialog_script_listener,
				null);
		layout_Top = (RelativeLayout) v.findViewById(R.id.layout_top);
		txt_Msg = (TextView) v.findViewById(R.id.txt_msg);
		btn_Restart = (Button) v.findViewById(R.id.btn_restart);
		btn_Restart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});
		IntentFilter filter = new IntentFilter(
				appClass.DIALOG_SCRIPT_LISTENER_RECEIVER);
		getActivity().registerReceiver(receiver, filter);

		layout_Top.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		layout_Top.measure(0, 0);
		widthOfScreen = layout_Top.getMeasuredWidth();
		heightOfScreen = layout_Top.getMeasuredHeight();

		layout_Top.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (MainActivity.closeAppFromPopup) {
					MainActivity.closeAppFromPopup = false;
					getActivity().finish();
				}

			}
		});

		// btn_Restart.setVisibility(View.INVISIBLE);
		btn_Restart.setEnabled(false);
		txt_Msg.setText("Waiting for the scripts to load...");

		DialogScriptLoader.this.setCancelable(false);

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

	BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			int status = -1;

			if (intent.hasExtra("stauts")) {
				status = intent.getExtras().getInt("stauts");
			}
			// 1 - Scripts are comming
			if (status == 1) {
				if (intent.hasExtra("msg")) {
					String flag = intent.getExtras().getString("msg");
					txt_Msg.setText(flag);
				}
				// 2 - Scripts are finished
			} else if (status == 2) {
				if (intent.hasExtra("msg")) {
					final String msg = intent.getExtras().getString("msg");

					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							txt_Msg.setText(msg);
						}
					}, 1000);

					if (getActivity() != null) {
						ShowControls showControls = new ShowControls();
						FragmentManager fm = getActivity()
								.getSupportFragmentManager();
						FragmentTransaction tr = fm.beginTransaction();
						tr.replace(R.id.showcontrols_frame, showControls)
								.commit();
					}
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (DialogScriptLoader.this != null) {

								DialogScriptLoader.this.dismiss();
							}

						}
					}, 2000);
				}
				// 3 - Scripts got error
			} else if (status == 3) {
				if (intent.hasExtra("msg")) {
					String msg = intent.getExtras().getString("msg");
					txt_Msg.setText(msg);
					// btn_Restart.setVisibility(View.VISIBLE);
					btn_Restart.setEnabled(true);

					if (getActivity() != null) {
						ShowControls showControls = new ShowControls();
						FragmentManager fm = getActivity()
								.getSupportFragmentManager();
						FragmentTransaction tr = fm.beginTransaction();
						tr.replace(R.id.showcontrols_frame, showControls)
								.commit();
					}
				}
				// 4 - No Scripts
			} else if (status == 4) {
				if (intent.hasExtra("msg")) {

					if (getActivity() != null) {
						ShowControls showControls = new ShowControls();
						FragmentManager fm = getActivity()
								.getSupportFragmentManager();
						FragmentTransaction tr = fm.beginTransaction();
						tr.replace(R.id.showcontrols_frame, showControls)
								.commit();
					}

					String msg = intent.getExtras().getString("msg");
					txt_Msg.setText(msg);
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							DialogScriptLoader.this.dismiss();
						}
					}, 2000);
				}
			} else {

				// Toast.makeText(getActivity(), "Come in else", 500).show();
				// appClass.setAN106NewLog("Come in else");

			}
		}
	};

	private int widthOfScreen;
	private int heightOfScreen;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mNum = getArguments().getInt("num");
		// Pick a style based on the num.
		int style = DialogFragment.STYLE_NORMAL, theme = 0;
		switch (1) {
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
		// widthOfScreen = getResources().getDisplayMetrics().widthPixels;
		// heightOfScreen = getResources().getDisplayMetrics().heightPixels;
		// widthOfScreen = widthOfScreen / 3;
		// heightOfScreen = heightOfScreen / 3;
		getDialog().getWindow().setLayout(widthOfScreen + 20,
				heightOfScreen + 20);
	}

}

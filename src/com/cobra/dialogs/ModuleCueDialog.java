package com.cobra.dialogs;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnDismissListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.cobra.R;
import com.cobra.classes.Constants;
import com.cobra.classes.ModuleCues;
import com.cobra.interfaces.OnModuleCuesDialogListener;

public class ModuleCueDialog extends Dialog implements OnClickListener {

	private TextView[] CUES = new TextView[18];
	private ModuleCues moduleCues;
	private Context context;
	private int moduleIndex;
	OnModuleCuesDialogListener listener;

	public ModuleCueDialog(Context context, ModuleCues mouleCues,
			int moduleIndex, OnModuleCuesDialogListener listener) {
		super(context);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_module_cues);

		this.moduleCues = mouleCues;
		this.context = context;
		this.moduleIndex = moduleIndex;
		this.listener = listener;
		CUES[0] = (TextView) findViewById(R.id.cue_1);
		CUES[0].setOnClickListener(this);
		CUES[1] = (TextView) findViewById(R.id.cue_2);
		CUES[1].setOnClickListener(this);
		CUES[2] = (TextView) findViewById(R.id.cue_3);
		CUES[2].setOnClickListener(this);
		CUES[3] = (TextView) findViewById(R.id.cue_4);
		CUES[3].setOnClickListener(this);
		CUES[4] = (TextView) findViewById(R.id.cue_5);
		CUES[4].setOnClickListener(this);
		CUES[5] = (TextView) findViewById(R.id.cue_6);
		CUES[5].setOnClickListener(this);
		CUES[6] = (TextView) findViewById(R.id.cue_7);
		CUES[6].setOnClickListener(this);
		CUES[7] = (TextView) findViewById(R.id.cue_8);
		CUES[7].setOnClickListener(this);
		CUES[8] = (TextView) findViewById(R.id.cue_9);
		CUES[8].setOnClickListener(this);
		CUES[9] = (TextView) findViewById(R.id.cue_10);
		CUES[9].setOnClickListener(this);
		CUES[10] = (TextView) findViewById(R.id.cue_11);
		CUES[10].setOnClickListener(this);
		CUES[11] = (TextView) findViewById(R.id.cue_12);
		CUES[11].setOnClickListener(this);
		CUES[12] = (TextView) findViewById(R.id.cue_13);
		CUES[12].setOnClickListener(this);
		CUES[13] = (TextView) findViewById(R.id.cue_14);
		CUES[13].setOnClickListener(this);
		CUES[14] = (TextView) findViewById(R.id.cue_15);
		CUES[14].setOnClickListener(this);
		CUES[15] = (TextView) findViewById(R.id.cue_16);
		CUES[15].setOnClickListener(this);
		CUES[16] = (TextView) findViewById(R.id.cue_17);
		CUES[16].setOnClickListener(this);
		CUES[17] = (TextView) findViewById(R.id.cue_18);
		CUES[17].setOnClickListener(this);
		
		if (mouleCues != null) {
			for (int i = 0; i < CUES.length; i++) {
				setCueStatus(i);
			}
		}

	}

	@Override
	public void setOnDismissListener(OnDismissListener listener) {
		// TODO Auto-generated method stub
		super.setOnDismissListener(listener);
		this.listener.OnDialogDismiss(moduleCues);
	}

	private void setCueStatus(int i) {
		switch (getCueStatus(i)) {
//		case Constants.CUE_STATE_NOT_AVAILABLE:
//			CUES[i].setBackgroundResource(R.drawable.cue_inactive);
//			break;
		case Constants.CUE_STATE_AVAILABLE:
			CUES[i].setBackgroundResource(R.drawable.cue_green_m);
			break;
		case Constants.CUE_STATE_FIRED:
			CUES[i].setBackgroundResource(R.drawable.cue_red_m);
			break;
		default:
			Toast.makeText(context, "Invalid cue state: " + getCueStatus(i),
					Toast.LENGTH_SHORT).show();
		}
	}

	private int getCueStatus(int cue_id) {
		switch (cue_id) {
		case 0:
			return moduleCues.getCue_1();
		case 1:
			return moduleCues.getCue_2();
		case 2:
			return moduleCues.getCue_3();
		case 3:
			return moduleCues.getCue_4();
		case 4:
			return moduleCues.getCue_5();
		case 5:
			return moduleCues.getCue_6();
		case 6:
			return moduleCues.getCue_7();
		case 7:
			return moduleCues.getCue_8();
		case 8:
			return moduleCues.getCue_9();
		case 9:
			return moduleCues.getCue_10();
		case 10:
			return moduleCues.getCue_11();
		case 11:
			return moduleCues.getCue_12();
		case 12:
			return moduleCues.getCue_13();
		case 13:
			return moduleCues.getCue_14();
		case 14:
			return moduleCues.getCue_15();
		case 15:
			return moduleCues.getCue_16();
		case 16:
			return moduleCues.getCue_17();
		case 17:
			return moduleCues.getCue_18();
		default:
			return -1;
		}
	}

	@Override
	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		switch (v.getId()) {
//		case R.id.cue_1:
//			if (getCueStatus(0) == Constants.CUE_STATE_AVAILABLE) {
//				CUES[0].setBackgroundResource(R.drawable.cue_green);
//				this.moduleCues.setCue_1(Constants.CUE_STATE_NOT_AVAILABLE);
//			}
//			break;
//		case R.id.cue_2:
//			if (getCueStatus(1) == Constants.CUE_STATE_AVAILABLE) {
//				CUES[1].setBackgroundResource(R.drawable.cue_green);
//				this.moduleCues.setCue_2(Constants.CUE_STATE_NOT_AVAILABLE);
//			}
//			break;
//		case R.id.cue_3:
//			if (getCueStatus(2) == Constants.CUE_STATE_AVAILABLE) {
//				CUES[2].setBackgroundResource(R.drawable.cue_green);
//				this.moduleCues.setCue_3(Constants.CUE_STATE_NOT_AVAILABLE);
//			}
//			break;
//		case R.id.cue_4:
//			if (getCueStatus(3) == Constants.CUE_STATE_AVAILABLE) {
//				CUES[3].setBackgroundResource(R.drawable.cue_green);
//				this.moduleCues.setCue_4(Constants.CUE_STATE_NOT_AVAILABLE);
//			}
//			break;
//		case R.id.cue_5:
//			if (getCueStatus(4) == Constants.CUE_STATE_AVAILABLE) {
//				CUES[4].setBackgroundResource(R.drawable.cue_green);
//				this.moduleCues.setCue_5(Constants.CUE_STATE_NOT_AVAILABLE);
//			}
//			break;
//		case R.id.cue_6:
//			if (getCueStatus(5) == Constants.CUE_STATE_AVAILABLE) {
//				CUES[5].setBackgroundResource(R.drawable.cue_green);
//				this.moduleCues.setCue_6(Constants.CUE_STATE_NOT_AVAILABLE);
//			}
//			break;
//		case R.id.cue_7:
//			if (getCueStatus(6) == Constants.CUE_STATE_AVAILABLE) {
//				CUES[6].setBackgroundResource(R.drawable.cue_green);
//				this.moduleCues.setCue_7(Constants.CUE_STATE_NOT_AVAILABLE);
//			}
//			break;
//		case R.id.cue_8:
//			if (getCueStatus(7) == Constants.CUE_STATE_AVAILABLE) {
//				CUES[7].setBackgroundResource(R.drawable.cue_green);
//				this.moduleCues.setCue_8(Constants.CUE_STATE_NOT_AVAILABLE);
//			}
//			break;
//		case R.id.cue_9:
//			if (getCueStatus(8) == Constants.CUE_STATE_AVAILABLE) {
//				CUES[8].setBackgroundResource(R.drawable.cue_green);
//				this.moduleCues.setCue_9(Constants.CUE_STATE_NOT_AVAILABLE);
//			}
//			break;
//		case R.id.cue_10:
//			if (getCueStatus(9) == Constants.CUE_STATE_AVAILABLE) {
//				CUES[9].setBackgroundResource(R.drawable.cue_green);
//				this.moduleCues.setCue_10(Constants.CUE_STATE_NOT_AVAILABLE);
//			}
//			break;
//		case R.id.cue_11:
//			if (getCueStatus(10) == Constants.CUE_STATE_AVAILABLE) {
//				CUES[10].setBackgroundResource(R.drawable.cue_green);
//				this.moduleCues.setCue_11(Constants.CUE_STATE_NOT_AVAILABLE);
//			}
//			break;
//		case R.id.cue_12:
//			if (getCueStatus(11) == Constants.CUE_STATE_AVAILABLE) {
//				CUES[11].setBackgroundResource(R.drawable.cue_green);
//				this.moduleCues.setCue_12(Constants.CUE_STATE_NOT_AVAILABLE);
//			}
//			break;
//		case R.id.cue_13:
//			if (getCueStatus(12) == Constants.CUE_STATE_AVAILABLE) {
//				CUES[12].setBackgroundResource(R.drawable.cue_green);
//				this.moduleCues.setCue_13(Constants.CUE_STATE_NOT_AVAILABLE);
//			}
//			break;
//		case R.id.cue_14:
//			if (getCueStatus(13) == Constants.CUE_STATE_AVAILABLE) {
//				CUES[13].setBackgroundResource(R.drawable.cue_green);
//				this.moduleCues.setCue_14(Constants.CUE_STATE_NOT_AVAILABLE);
//			}
//			break;
//		case R.id.cue_15:
//			if (getCueStatus(14) == Constants.CUE_STATE_AVAILABLE) {
//				CUES[14].setBackgroundResource(R.drawable.cue_green);
//				this.moduleCues.setCue_15(Constants.CUE_STATE_NOT_AVAILABLE);
//			}
//			break;
//		case R.id.cue_16:
//			if (getCueStatus(15) == Constants.CUE_STATE_AVAILABLE) {
//				CUES[15].setBackgroundResource(R.drawable.cue_green);
//				this.moduleCues.setCue_16(Constants.CUE_STATE_NOT_AVAILABLE);
//			}
//			break;
//		case R.id.cue_17:
//			if (getCueStatus(16) == Constants.CUE_STATE_AVAILABLE) {
//				CUES[16].setBackgroundResource(R.drawable.cue_green);
//				this.moduleCues.setCue_17(Constants.CUE_STATE_NOT_AVAILABLE);
//			}
//			break;
//		case R.id.cue_18:
//			if (getCueStatus(17) == Constants.CUE_STATE_AVAILABLE) {
//				CUES[17].setBackgroundResource(R.drawable.cue_green);
//				this.moduleCues.setCue_18(Constants.CUE_STATE_NOT_AVAILABLE);
//			}
//			break;
//
//		default:
//			break;
//		}
	}
}

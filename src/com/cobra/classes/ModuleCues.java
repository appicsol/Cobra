package com.cobra.classes;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.cobra.R;
import com.cobra.view.bucketfiring.Module;

public class ModuleCues extends Module {
	int Cue_1 = 2;
	TextView[] CUES = new TextView[18];
	int Cue_2 = 2;
	TextView CUE_2 = null;
	int Cue_3 = 2;
	TextView CUE_3 = null;
	int Cue_4 = 2;
	TextView CUE_4 = null;
	int Cue_5 = 2;
	TextView CUE_5 = null;
	int Cue_6 = 2;
	TextView CUE_6 = null;
	int Cue_7 = 2;
	TextView CUE_7 = null;
	int Cue_8 = 2;
	TextView CUE_8 = null;
	int Cue_9 = 2;
	TextView CUE_9 = null;
	int Cue_10 = 2;
	TextView CUE_10 = null;
	int Cue_11 = 2;
	TextView CUE_11 = null;
	int Cue_12 = 2;
	TextView CUE_12 = null;
	int Cue_13 = 2;
	TextView CUE_13 = null;
	int Cue_14 = 2;
	TextView CUE_14 = null;
	int Cue_15 = 2;
	TextView CUE_15 = null;
	int Cue_16 = 2;
	TextView CUE_16 = null;
	int Cue_17 = 2;
	TextView CUE_17 = null;
	int Cue_18 = 2;
	TextView CUE_18 = null;

	public ModuleCues(Context context) {
		super(context);
	}

	// ModuleCues(Context context, int v) {
	// InitializeCuesUI(context, v);
	// }

	public void InitializeCuesUI(Context context, View view) {
		CUES[0] = (TextView) view.findViewById(R.id.cue_1);
		CUES[1] = (TextView) view.findViewById(R.id.cue_2);
		CUES[2] = (TextView) view.findViewById(R.id.cue_3);
		CUES[3] = (TextView) view.findViewById(R.id.cue_4);
		CUES[4] = (TextView) view.findViewById(R.id.cue_5);
		CUES[5] = (TextView) view.findViewById(R.id.cue_6);
		CUES[6] = (TextView) view.findViewById(R.id.cue_7);
		CUES[7] = (TextView) view.findViewById(R.id.cue_8);
		CUES[8] = (TextView) view.findViewById(R.id.cue_9);
		CUES[9] = (TextView) view.findViewById(R.id.cue_10);
		CUES[10] = (TextView) view.findViewById(R.id.cue_11);
		CUES[11] = (TextView) view.findViewById(R.id.cue_12);
		CUES[12] = (TextView) view.findViewById(R.id.cue_13);
		CUES[13] = (TextView) view.findViewById(R.id.cue_14);
		CUES[14] = (TextView) view.findViewById(R.id.cue_15);
		CUES[15] = (TextView) view.findViewById(R.id.cue_16);
		CUES[16] = (TextView) view.findViewById(R.id.cue_17);
		CUES[17] = (TextView) view.findViewById(R.id.cue_18);
		UpdateCuesStatus();
	}

	public void UpdateCuesStatus() {
		setCueStatus(Cue_1, 0);
		setCueStatus(Cue_2, 1);
		setCueStatus(Cue_3, 2);
		setCueStatus(Cue_4, 3);
		setCueStatus(Cue_5, 4);
		setCueStatus(Cue_6, 5);
		setCueStatus(Cue_7, 6);
		setCueStatus(Cue_8, 7);
		setCueStatus(Cue_9, 8);
		setCueStatus(Cue_10, 9);
		setCueStatus(Cue_11, 10);
		setCueStatus(Cue_12, 11);
		setCueStatus(Cue_13, 12);
		setCueStatus(Cue_14, 13);
		setCueStatus(Cue_15, 14);
		setCueStatus(Cue_16, 15);
		setCueStatus(Cue_17, 16);
		setCueStatus(Cue_18, 17);

	}

	private void setCueStatus(int status, int cue_id) {
		switch (status) {
		// case Constants.CUE_STATE_NOT_AVAILABLE:
		// CUES[cue_id].setBackgroundResource(R.drawable.cue_green);
		// break;
		case Constants.CUE_STATE_AVAILABLE:
			CUES[cue_id].setBackgroundColor(Color.parseColor("#00C906"));// Resource(R.drawable.cue_green);
			break;
		case Constants.CUE_STATE_FIRED:
			CUES[cue_id].setBackgroundColor(Color.parseColor("#E50000"));
			break;
		default:
			break;
		}
	}

	public int getCue_1() {
		return Cue_1;
	}

	public void setCue_1(int cue_1) {
		Cue_1 = cue_1;
		if (CUES[0] != null) {
			setCueStatus(cue_1, 0);
		}
	}

	public int getCue_2() {
		return Cue_2;
	}

	public void setCue_2(int cue_2) {
		Cue_2 = cue_2;
		if (CUES[1] != null) {
			setCueStatus(cue_2, 1);
		}
	}

	public int getCue_3() {
		return Cue_3;
	}

	public void setCue_3(int cue_3) {
		Cue_3 = cue_3;
		if (CUES[2] != null) {
			setCueStatus(cue_3, 2);
		}
	}

	public int getCue_4() {
		return Cue_4;
	}

	public void setCue_4(int cue_4) {
		Cue_4 = cue_4;
		if (CUES[3] != null) {
			setCueStatus(cue_4, 3);
		}
	}

	public int getCue_5() {
		return Cue_5;
	}

	public void setCue_5(int cue_5) {
		Cue_5 = cue_5;
		if (CUES[4] != null) {
			setCueStatus(cue_5, 4);
		}
	}

	public int getCue_6() {
		return Cue_6;
	}

	public void setCue_6(int cue_6) {
		Cue_6 = cue_6;
		if (CUES[5] != null) {
			setCueStatus(cue_6, 5);
		}
	}

	public int getCue_7() {
		return Cue_7;
	}

	public void setCue_7(int cue_7) {
		Cue_7 = cue_7;
		if (CUES[6] != null) {
			setCueStatus(cue_7, 6);
		}
	}

	public int getCue_8() {
		return Cue_8;
	}

	public void setCue_8(int cue_8) {
		Cue_8 = cue_8;
		if (CUES[7] != null) {
			setCueStatus(cue_8, 7);
		}
	}

	public int getCue_9() {
		return Cue_9;
	}

	public void setCue_9(int cue_9) {
		Cue_9 = cue_9;
		if (CUES[8] != null) {
			setCueStatus(cue_9, 8);
		}
	}

	public int getCue_10() {
		return Cue_10;
	}

	public void setCue_10(int cue_10) {
		Cue_10 = cue_10;
		if (CUES[9] != null) {
			setCueStatus(cue_10, 9);
		}
	}

	public int getCue_11() {
		return Cue_11;
	}

	public void setCue_11(int cue_11) {
		Cue_11 = cue_11;
		if (CUES[10] != null) {
			setCueStatus(cue_11, 10);
		}
	}

	public int getCue_12() {
		return Cue_12;
	}

	public void setCue_12(int cue_12) {
		Cue_12 = cue_12;
		if (CUES[11] != null) {
			setCueStatus(cue_12, 11);
		}
	}

	public int getCue_13() {
		return Cue_13;
	}

	public void setCue_13(int cue_13) {
		Cue_13 = cue_13;
		if (CUES[12] != null) {
			setCueStatus(cue_13, 12);
		}
	}

	public int getCue_14() {
		return Cue_14;
	}

	public void setCue_14(int cue_14) {
		Cue_14 = cue_14;
		if (CUES[13] != null) {
			setCueStatus(cue_14, 13);
		}
	}

	public int getCue_15() {
		return Cue_15;
	}

	public void setCue_15(int cue_15) {
		Cue_15 = cue_15;
		if (CUES[14] != null) {
			setCueStatus(cue_15, 14);
		}
	}

	public int getCue_16() {
		return Cue_16;
	}

	public void setCue_16(int cue_16) {
		Cue_16 = cue_16;
		if (CUES[15] != null) {
			setCueStatus(cue_16, 15);
		}
	}

	public int getCue_17() {
		return Cue_17;
	}

	public void setCue_17(int cue_17) {
		Cue_17 = cue_17;
		if (CUES[16] != null) {
			setCueStatus(cue_17, 16);
		}
	}

	public int getCue_18() {
		return Cue_18;
	}

	public void setCue_18(int cue_18) {
		Cue_18 = cue_18;
		if (CUES[17] != null) {
			setCueStatus(cue_18, 17);
		}
	}

	public int getFiredCues() {
		int fired_cues = 0;
		if (Cue_1 == Constants.CUE_STATE_FIRED)
			fired_cues += 1;
		if (Cue_2 == Constants.CUE_STATE_FIRED)
			fired_cues += 1;
		if (Cue_3 == Constants.CUE_STATE_FIRED)
			fired_cues += 1;
		if (Cue_4 == Constants.CUE_STATE_FIRED)
			fired_cues += 1;
		if (Cue_5 == Constants.CUE_STATE_FIRED)
			fired_cues += 1;
		if (Cue_6 == Constants.CUE_STATE_FIRED)
			fired_cues += 1;
		if (Cue_7 == Constants.CUE_STATE_FIRED)
			fired_cues += 1;
		if (Cue_8 == Constants.CUE_STATE_FIRED)
			fired_cues += 1;
		if (Cue_9 == Constants.CUE_STATE_FIRED)
			fired_cues += 1;
		if (Cue_10 == Constants.CUE_STATE_FIRED)
			fired_cues += 1;
		if (Cue_11 == Constants.CUE_STATE_FIRED)
			fired_cues += 1;
		if (Cue_12 == Constants.CUE_STATE_FIRED)
			fired_cues += 1;
		if (Cue_13 == Constants.CUE_STATE_FIRED)
			fired_cues += 1;
		if (Cue_14 == Constants.CUE_STATE_FIRED)
			fired_cues += 1;
		if (Cue_15 == Constants.CUE_STATE_FIRED)
			fired_cues += 1;
		if (Cue_16 == Constants.CUE_STATE_FIRED)
			fired_cues += 1;
		if (Cue_17 == Constants.CUE_STATE_FIRED)
			fired_cues += 1;
		if (Cue_18 == Constants.CUE_STATE_FIRED)
			fired_cues += 1;
		return fired_cues;
	}

	public Boolean IsLastCue() {
		int fired_cues = getFiredCues();
		if (fired_cues == 17)
			return true;
		return false;
	}

	public Boolean IsAnyCueAvailable() {
		if (Cue_1 == Constants.CUE_STATE_FIRED
				&& Cue_2 == Constants.CUE_STATE_FIRED
				&& Cue_3 == Constants.CUE_STATE_FIRED
				&& Cue_4 == Constants.CUE_STATE_FIRED
				&& Cue_5 == Constants.CUE_STATE_FIRED
				&& Cue_6 == Constants.CUE_STATE_FIRED
				&& Cue_7 == Constants.CUE_STATE_FIRED
				&& Cue_8 == Constants.CUE_STATE_FIRED
				&& Cue_9 == Constants.CUE_STATE_FIRED
				&& Cue_10 == Constants.CUE_STATE_FIRED
				&& Cue_11 == Constants.CUE_STATE_FIRED
				&& Cue_12 == Constants.CUE_STATE_FIRED
				&& Cue_13 == Constants.CUE_STATE_FIRED
				&& Cue_14 == Constants.CUE_STATE_FIRED
				&& Cue_15 == Constants.CUE_STATE_FIRED
				&& Cue_16 == Constants.CUE_STATE_FIRED
				&& Cue_17 == Constants.CUE_STATE_FIRED
				&& Cue_18 == Constants.CUE_STATE_FIRED) {
			return false;
		} else {
			return true;
		}
	}

	public int getNextAvailableCue() {

		if (Cue_1 == Constants.CUE_STATE_AVAILABLE)
			return 1;
		if (Cue_2 == Constants.CUE_STATE_AVAILABLE)
			return 2;
		if (Cue_3 == Constants.CUE_STATE_AVAILABLE)
			return 3;
		if (Cue_4 == Constants.CUE_STATE_AVAILABLE)
			return 4;
		if (Cue_5 == Constants.CUE_STATE_AVAILABLE)
			return 5;
		if (Cue_6 == Constants.CUE_STATE_AVAILABLE)
			return 6;
		if (Cue_7 == Constants.CUE_STATE_AVAILABLE)
			return 7;
		if (Cue_8 == Constants.CUE_STATE_AVAILABLE)
			return 8;
		if (Cue_9 == Constants.CUE_STATE_AVAILABLE)
			return 9;
		if (Cue_10 == Constants.CUE_STATE_AVAILABLE)
			return 10;
		if (Cue_11 == Constants.CUE_STATE_AVAILABLE)
			return 11;
		if (Cue_12 == Constants.CUE_STATE_AVAILABLE)
			return 12;
		if (Cue_13 == Constants.CUE_STATE_AVAILABLE)
			return 13;
		if (Cue_14 == Constants.CUE_STATE_AVAILABLE)
			return 14;
		if (Cue_15 == Constants.CUE_STATE_AVAILABLE)
			return 15;
		if (Cue_16 == Constants.CUE_STATE_AVAILABLE)
			return 16;
		if (Cue_17 == Constants.CUE_STATE_AVAILABLE)
			return 17;
		if (Cue_18 == Constants.CUE_STATE_AVAILABLE)
			return 18;
		return 0;

	}

	public int getRandomCue() {
		ArrayList<Integer> available_cues = new ArrayList<Integer>();
		if (Cue_1 == Constants.CUE_STATE_AVAILABLE)
			available_cues.add(1);
		if (Cue_2 == Constants.CUE_STATE_AVAILABLE)
			available_cues.add(2);
		if (Cue_3 == Constants.CUE_STATE_AVAILABLE)
			available_cues.add(3);
		if (Cue_4 == Constants.CUE_STATE_AVAILABLE)
			available_cues.add(4);
		if (Cue_5 == Constants.CUE_STATE_AVAILABLE)
			available_cues.add(5);
		if (Cue_6 == Constants.CUE_STATE_AVAILABLE)
			available_cues.add(6);
		if (Cue_7 == Constants.CUE_STATE_AVAILABLE)
			available_cues.add(7);
		if (Cue_8 == Constants.CUE_STATE_AVAILABLE)
			available_cues.add(8);
		if (Cue_9 == Constants.CUE_STATE_AVAILABLE)
			available_cues.add(9);
		if (Cue_10 == Constants.CUE_STATE_AVAILABLE)
			available_cues.add(10);
		if (Cue_11 == Constants.CUE_STATE_AVAILABLE)
			available_cues.add(11);
		if (Cue_12 == Constants.CUE_STATE_AVAILABLE)
			available_cues.add(12);
		if (Cue_13 == Constants.CUE_STATE_AVAILABLE)
			available_cues.add(13);
		if (Cue_14 == Constants.CUE_STATE_AVAILABLE)
			available_cues.add(14);
		if (Cue_15 == Constants.CUE_STATE_AVAILABLE)
			available_cues.add(15);
		if (Cue_16 == Constants.CUE_STATE_AVAILABLE)
			available_cues.add(16);
		if (Cue_17 == Constants.CUE_STATE_AVAILABLE)
			available_cues.add(17);
		if (Cue_18 == Constants.CUE_STATE_AVAILABLE)
			available_cues.add(18);

		Random r = new Random();
		int i = r.nextInt(available_cues.size());
		return available_cues.get(i);
	}

}

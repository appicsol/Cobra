package com.cobra.classes;

import java.util.ArrayList;

import com.cobra.appClass;

public class ChannelCues {
	/*
	 * Module consider cues as 24, skip 2 cues after every 6 cues
	 */
	public static final long[] cues = new long[24];

	private Boolean[] cuesStatus = new Boolean[24];

	public ChannelCues() {
		for (int i = 0; i < 24; i++) {
			cues[i] = (long) Math.pow(2, i);
		}
		for (int i = 0; i < 24; i++) {
			cuesStatus[i] = false;
		}
	}

	public void setCueStatus(long cueList) {
		for (int i = 0; i < 24; i++) {
			long temp = cues[i] & cueList;
			if (temp > 0) {
				cuesStatus[i] = true;
			}
		}
	}

	public static Boolean getCue18R2(int position, long cueList) {
		long temp = cues[position] & cueList;
		if (temp > 0) {
			return true;
		}
		return false;
	}

	/*
	 * These are 18M cues based on 8 bits per row after each 6 bits skip next
	 * two bits
	 */
	public static Boolean getCue18M(int position, long cueList) {
		// Lets say this position is 5 - Row 1
		int pos = position;
		if (position > 5) {
			pos = pos + 2;
		}
		if (position > 11) {
			pos = pos + 2;
		}

		long temp = cues[pos] & cueList;

		// if (pos < 3)
		// appClass.setAN188Log("pos=" + position + "  cues[pos]: "
		// + cues[pos] + "  cueList: " + cueList + "  temp= " + temp);

		if (temp > 0) {
			return true;
		}
		return false;
	}


	public static Boolean getCue18MString(int position,
			ArrayList<String> cueList) {
		
		int pos = position;
		if (position > 5) {
			pos = pos + 2;
		}

		if (position > 11) {
			pos = pos + 2;
		}

		if (cueList != null) {
			for (int i = 0; i < cueList.size(); i++) {
				if (cueList.get(i) != null
						&& cueList.get(i).equals((position + 1) + "")) {
					return true;
				}
			}
		} else
			return false;
		return false;
	}

	public Boolean getCueStatus(int position) {
		return cuesStatus[position];
	}
}

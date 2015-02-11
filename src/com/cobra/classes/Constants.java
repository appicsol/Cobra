package com.cobra.classes;

import android.graphics.Color;

public class Constants {
	public static final int[] RED_DARK=new int[]{197,43,43};
	public static final int[] RED_LIGHT=new int[]{255,80,80};
	public static final int[] GREEN=new int[]{54,195,0};
	public static final int[] GERY_LIGHT=new int[]{216,216,216};
	public static final int[] GERY=new int[]{167, 161, 161};
	public static final int[] GERY_DARK=new int[] {107,107,107};
	public static final int[] BLACK=new int[]{0,0,0};
	public static final int[] WHITE=new int[]{255,255,255};
	public static final int[] TRANSPARENT=new int[]{255,0,0,0};
	public static final int GERY_DARK_rgb=Color.rgb(107, 107, 107);
	/** if cue is fired */
	public static final int CUE_STATE_FIRED=1;
	/** if cue is not assigned to any bucket yet */
	public static final int CUE_STATE_AVAILABLE=2;
	
	public static final int BUCKET_DEFAULT_TIME_IN_MS=1000;
	public static final String BUCKET_STATUS_RANDOM="Random";
	public static final String BUCKET_STATUS_SEQ="Sequential";
}

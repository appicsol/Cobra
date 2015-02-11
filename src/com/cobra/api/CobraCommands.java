package com.cobra.api;

public class CobraCommands {
	public static final int MAX_MESSAGE_BYTES = 100;

	// ACKNOWLEDGEMENTS
	static final int ACK_REQ_DEVICE_STATUS = 0x30;// 48
	// Both ways
	public static final int VCOM_REQ_ALL_DATA_REFRESH = 0x62;// 98
	public static final int VCOM_REQ_ALL_EVENT_DATA = 0x65;// 101

	// Android to remote
	public static final int VCOM_REQ_QUEUED_MODULE_DATA = 0x6C;// 108
	public static final int VCOM_REQ_QUEUED_FIREDCUES_DATA = 0x6D;// 109
	public static final int VCOM_REQ_DEVICE_STATUS = 0x66;// 102
	public static final int VCOM_REQ_MODULE_DATA = 0x6A;// 106
	public static final int VCOM_REQ_FIREDCUES_DATA = 0x6B;// 107
	public static final int VCOM_OWN_ACK_FLAG_STATUS = 0x6E;// 110
	public static final int VCOM_ACKNOWLEDGE_STATUS_CHANGE = 0x31;// 49
	public static final int VCOM_SET_TEST_MODE = 0x21;// 33
	public static final int VCOM_SET_FIRE_MODE = 0x22;// 34
	public static final int VCOM_SET_CHANNEL = 0x23;// 35
	public static final int VCOM_SET_DEVICE_REBOOT = 0x24;// 36
	public static final int VCOM_SET_DUMMY_MODE = 0x6F;// 99
	public static final int VCOM_REQ_MODULE_DATA_REFRESH = 0x63;// 99
	public static final int VCOM_REQ_STEP_NEXT = 0x64;// 100
	public static final int VCOM_REQ_FIRE_CUES = 0x67;// 103
	public static final int VCOM_REQ_PLAY_SCRIPT = 0x68;// 104
	public static final int VCOM_REQ_PAUSE_SCRIPT = 0x69;// 105
	public static final int VCOM_REQ_RESUME_SCRIPT = 0x70;// 112
	public static final int VCOM_REQ_STOP_SCRIPT = 0x71;// 113
	public static final int VCOM_REQ_JUMPTO_TIME = 0x72;// 114
	public static final int VCOM_REQ_JUMPTO_EVENT = 0x73;// 115
	public static final int VCOM_STATUS_PINGBACK = 0x60;// 96

	// Remote to Android
	// private static final int VCOM_NOTIFY_STATUS_CHANGE = 0x30;//48
	public static final int VCOM_DEVICE_STATUS = 0x41;// 65
	public static final int VCOM_FIREDCUES_DATA = 0x42;// 66
	public static final int VCOM_SCRIPTCUES_DATA = 0x43;// 67
	public static final int VCOM_SCRIPT_DATA = 0x44;// 68
	public static final int VCOM_MODULE_DATA = 0x45;// 69
	public static final int VCOM_SCRIPT_PING = 0x46;// 70
	public static final int VCOM_CLEAR_MODULES = 0x47;// 71
	public static final int VCOM_REQ_NEXT_SCRIPT_DATA = 0x48;// 72
	public static final int VCOM_REQ_NEXT_EVENT_DATA = 0x49;// 73
	public static final int VCOM_REQ_DEVICE_INFO = 0x61;// 97

	public static final int VCOM_NOTIFY_STATUS_CHANGE_BYTES = 0x30;// 48
	public static final int VCOM_DEVICE_STATUS_BYTES = 4;
	public static final int VCOM_FIREDCUES_DATA_BYTES = 5;
	public static final int VCOM_SCRIPTCUES_DATA_BYTES = 5;
	public static final int VCOM_SCRIPT_DATA_BYTES = 8;
	public static final int VCOM_MODULE_DATA_BYTES = 9;
	public static final int VCOM_SCRIPT_PING_BYTES = 8;
	public static final int VCOM_CLEAR_MODULES_BYTES = 1;
	public static final int VCOM_REQ_DEVICE_INFO_BYTES = 6;

	public static final int ACK_REQ_DEVICE_STATUS_BYTES = 5;
	public static final int VCOM_REQ_ALL_DATA_REFRESH_BYTES = 1;
	public static final int VCOM_REQ_ALL_EVENT_DATA_BYTES = 13;

	// Bit masks
	/** Mask for the continuity bits **/
	public static final int CONTINUITY_MASK = 0x3F;// 63

	/** Mask for the AudioBox module bit in continuity bytes **/
	public static final int AUDIOBOX_MODULE_FLAG = 0x40;// 64 //stay clear of
														// six LSBs otherwise
														// they will show on the
														// continuity display

	/** Mask for the 90M module bit in continuity bytes **/
	public static final int NINETYM_MODULE_FLAG = 0x80;// 128 //used to identify
														// 90M type so the
														// sub-addresses are
														// handled accordingly

}
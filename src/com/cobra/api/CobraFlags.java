package com.cobra.api;

public class CobraFlags {

	// scripts data flags
	public static final int SCRIPTS_DATA_FORCE_STOPPING_ACKNOWLEDGE = -1;
	public static final int SCRIPTS_DATA_READY_TO_ACKNOWLEDGE = 0;
	public static final int READY_TO_CALL_SCRIPTS_DATA = 1;

	public static final int SCRIPT_DATA_IS_CALLING = 2;
	public static final int ALL_SCRIPT_DATA_IS_FINISHED = 3;
	public static final int SCRIPT_DATA_STOP_BROADCASTING = 4;

	// device data flags
	public static final int DEVICE_STATUS_READY_TO_ACKNOWLEDGE = 0;
	public static final int READY_TO_CALL_DEVICE_STATUS = 1;
	public static final int RECIEVED_DEVICE_STATUS = 2;

	// module data flags
	public static final int MODULE_DATA_NOT_READY_TO_ACKNOWLEDGE = -1;
	public static final int MODULE_DATA_READY_TO_ACKNOWLEDGE = 0;
	public static final int READY_TO_CALL_MODULE_DATA = 1;
	public static final int READY_TO_CALL_NEXT_MODULE_DATA = 2;
	public static final int WAIT_FOR_REQUESTED_MODULE_DATA = 3;
//	public static final int MODULE_LIST_SCROLLING = 4;

	// device firmware flags
	public static final int FIRMWARE_DATA_READY_TO_ACKNOWLEDGE = 0;
	public static final int READY_TO_CALL_FIRMWARE_DATA = 1;
	public static final int RECIEVED_FIRMWARE_DATA = 2;

	// firecue data flags
	public static final int FIRECUE_DATA_READY_TO_ACKNOWLEDGE = 0;
	public static final int READY_TO_CALL_FIRECUE_DATA = 1;
	public static final int READY_TO_CALL_NEXT_FIRECUE_DATA = 2;
}

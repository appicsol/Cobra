/*
 * 
 */
package com.cobra.api;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.cobra.MainActivity;
import com.cobra.appClass;
import com.cobra.api.CobraDataTags.ChannelDataTag;
import com.cobra.api.CobraDataTags.EventDataTag;
import com.cobra.api.CobraDataTags.MessageDataTag;
import com.cobra.api.CobraDataTags.ModuleDataTag;
import com.cobra.api.CobraDataTags.ModuleLightsHandler;
import com.cobra.api.CobraDataTags.ScriptIndexTag;
import com.cobra.api.CobraDataTags.ScriptPingTag;
import com.cobra.api.CobraDataTags.Version;
import com.cobra.api.SerialDriver.ISerialPortWListener;
import com.cobra.api.SerialDriver.SerialPortWEvent;
import com.cobra.classes.ACK_Event_Creator;
import com.cobra.classes.ChannelCues;
import com.cobra.classes.ScriptEvents;
import com.cobra.dialogs.DialogDisconnectDevice;
import com.cobra.interfaces.ScriptListener;
import com.cobra.views.PersistentHeader;
import com.cobra.views.ShowControls;
//import android.widget.Toast;
//import android.widget.Toast;

/**
 * The class used to interface with a Cobra device.
 */
public final class Cobra {

	/** The tag. */
	private final String TAG;

	/** The Constant COBRA_CODE_NODEVICE. */
	public final static int COBRA_CODE_NODEVICE = 0x1;// 1

	/** The Constant COBRA_CODE_NOPERM. */
	public final static int COBRA_CODE_NOPERM = 0x2;// 2

	/** The Constant MODE_TEST. */
	public static final int MODE_TEST = 1;
	/** The Constant MODE_ARMED. */
	public static final int MODE_ARMED = 2;

	/** The Constant DEVICE_TYPE_18R2. */
	public static final int DEVICE_TYPE_18R2 = 1;
	/** The Constant DEVICE_TYPE_UNKNOWN. */
	public static final int DEVICE_TYPE_UNKNOWN = 2;

	/** The maximum number of modules allowed. */
	public static final int MAX_NUMBER_OF_MODULES = 1000;
	/** The maximum number of channels allowed. */
	public static final int MAX_NUMBER_OF_CHANNELS = 100;
	/** The maximum number of scripts allowed. */
	public static final int MAX_NUMBER_OF_SCRIPTS = 100;

	/** The Constant STATUS_ARM_MODE. */
	public static final byte STATUS_ARM_MODE = 0x10;// 16
	/** The Constant STATUS_ARMED. */
	public static final byte STATUS_ARMED = 0x20;// 32
	/** The Constant STATUS_BATTERY. */
	public static final byte STATUS_BATTERY = 0x40;// 64
	/** The Constant STATUS_SCRIPT_MODE. */
	public static final byte STATUS_SCRIPT_MODE = (byte) 0x80;// -128

	/** The Constant EVENT_TYPE_STATUS_CHANGE. */
	public static final byte EVENT_TYPE_DISCONNECTED = 0x0F;// 15
	/** The Constant EVENT_TYPE_STATUS_CHANGE. */
	public static final byte EVENT_TYPE_STATUS_CHANGE = 0x01;// 1
	public static final byte EVENT_TYPE_REQ_DEVICE_STATUS = (byte) 0x66;// 102
	/** The Constant EVENT_TYPE_MODULE_DATA_CHANGE. */
	public static final byte EVENT_TYPE_MODULE_DATA_CHANGE = 0x02;// 2
	/** The Constant EVENT_TYPE_CHANNEL_DATA_CHANGE. */
	public static final byte EVENT_TYPE_CHANNEL_DATA_CHANGE = 0x04;// 4
	/** The Constant EVENT_TYPE_SCRIPT_DATA_CHANGE. */
	public static final byte EVENT_TYPE_SCRIPT_DATA_CHANGE = 0x08;// 8
	/** The Constant EVENT_TYPE_SCRIPT_PING. */
	public static final byte EVENT_TYPE_SCRIPT_PING = 0x09;// 9
	/** The Constant EVENT_CLEAR_MODULES. */
	public static final byte EVENT_CLEAR_MODULES = 0x0A;// 10

	public static final byte EVENT_ACK_DEVICE_DATA_CHANGE = 0x11;// 17
	public static final byte EVENT_ACK_MODULE_DATA_CHANGE = 0x12;// 18
	public static final byte EVENT_ACK_FIRECUE_DATA_CHANGE = 0x13;// 19
	public static final byte EVENT_ACK_DEVICE_MODULE_FIRECUE_DATA_CHANGE = 0x14;// 20
	public static final byte EVENT_ACK_DEVICE_MODULE_DATA_CHANGE = 0x15;// 21
	public static final byte EVENT_ACK_DEVICE_FIRECUE_DATA_CHANGE = 0x16;// 22
	public static final byte EVENT_ACK_MODULE_FIRECUE_DATA_CHANGE = 0x17;// 23

	/** The Constant MODULE_LOW_BATT_THRESHOLD. */
	private static final byte MODULE_LOW_BATT_THRESHOLD = 3;

	public static final byte EVENT_TYPE_DEVICE_INFO_CHANGE = 0x61;// 97

	/** The type of module **/
	public enum ModuleType {
		EIGHTEEN_M, NINETY_M_MAIN, NINETY_M_SUB, AUDIOBOX
	}

	/** The version api. */
	private final Version versionAPI = new Version(0, 1, 0, '\0');
	public static final String ERROR_POWER_CYCLE = "ERROR_POWER_CYCLE";

	/** The channel. */
	private volatile Integer Channel = 0;

	/** The battery. */
	private volatile Integer battery = -1;

	/** The mode. */
	private volatile Integer mode = 0;
	private int Mode = -1;
	/** The device type. */
	private volatile Integer deviceType = 0;

	/** The version device. */
	private volatile Version versionDevice = new Version(0, 0, 0, ' ');

	/** The serial port. */
	// private final SerialDriver serialPort;

	/** The event listener. */
	// private final ISerialPortWListener eventListener;

	/** The listener list. */
	private final List<ICobraEventListener> listenerList;

	/** The message thread. */
	private final Thread messageThread;

	/** The sem message available. */
	private static Semaphore semMessageAvailable;

	/** The data buffer. */
	private static ConcurrentLinkedQueue<MessageDataTag> dataBuffer;

	/** The module data. */
	private ConcurrentSkipListMap<Integer, ModuleDataTag> moduleData;

	/** The channel data. */
	private ConcurrentSkipListMap<Integer, ChannelDataTag> channelData;

	/** The script index data. */
	// private final ConcurrentSkipListMap<Integer, ScriptIndexTag>
	// scriptIndexData;
	public static ArrayList<ScriptIndexTag> scriptIndexDataList;

	/** The event data. */
	private static ConcurrentSkipListMap<Integer, SparseArray<EventDataTag>> eventData;

	/** The message out data. */
	private final HashMap<String, Byte> messageOutData;

	/** The context. */
	private final Context context;
	private final Activity activity;
	/**
	 * A hash map used to notify the main thread when a certain Cobra message
	 * with a specific command value has been received. This is the mechanism
	 * used to allow block until received functionality in the communication.
	 * I.e. if API makes a request for data from remote, this mechanism will
	 * allow it to block until it receives the corresponding response message
	 * from the remote. <br>
	 * <br>
	 * The <b>Integer key</b> should use the CobraCommands opCodes. <br>
	 * <br>
	 * The <b>{@link CyclicBarrier}</b> should then be used to wait on the
	 * opCode after a request is made and in parseMessages when a corresponding
	 * opCode response is received. Essentially, it will block, until both
	 * awaits are reached, thereby synchronizing a request with its response.
	 * Look at {@link #updateDeviceInfo()} met hod in this class to see an
	 * example.
	 * 
	 */
	private final ConcurrentHashMap<Integer, CyclicBarrier> asyncNotify;

	private String sError = null;

	private ArrayList<ModuleLightsHandler> moduleLightsHandlerList;

	private appClass globV;

	/**
	 * Instantiates a new Cobra device object.
	 * 
	 * @param context
	 *            the application context.
	 */
	public Cobra(Activity activity, Context context) {
		TAG = Cobra.class.getName();
		this.context = context;
		this.activity = activity;
		// eventListener = new SerialEventListener();
		globV = (appClass) context.getApplicationContext();

		// appClass.IsDeviceConnected = true;

		Log.i("com.cobra.api.Cobra", "Initiallizing SerialDriver");
		globV.serialPort = new SerialDriver(context, new SerialEventListener());
		listenerList = new ArrayList<ICobraEventListener>();
		semMessageAvailable = new Semaphore(0);

		dataBuffer = new ConcurrentLinkedQueue<MessageDataTag>();
		moduleData = new ConcurrentSkipListMap<Integer, ModuleDataTag>();
		channelData = new ConcurrentSkipListMap<Integer, ChannelDataTag>();
		moduleLightsHandlerList = new ArrayList<CobraDataTags.ModuleLightsHandler>();

		// scriptIndexData = new ConcurrentSkipListMap<Integer,
		// ScriptIndexTag>();
		eventData = new ConcurrentSkipListMap<Integer, SparseArray<EventDataTag>>();

		asyncNotify = new ConcurrentHashMap<Integer, CyclicBarrier>();
		messageOutData = new HashMap<String, Byte>();
		messageThread = new Thread(new ParseMessages());
		try {
			IntentFilter filter2 = new IntentFilter();
			filter2.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
			context.registerReceiver(mUsbReceiver2, filter2);

			updateCommandsNotification();
			messageThread.start();
		} catch (Exception e) {
			appClass.setLogOnDevice("Exception when initiallizing Cobra: "
					+ e.getMessage());
		}
	}

	// static UnRegisterListeners unregisterListener = null;

	public static interface UnRegisterListeners extends EventListener {
		public void Unregister();
	}

	/**
	 * Used to update asynNotify object with all the valid commands to allow for
	 * asynchronous notification. <br>
	 * <br>
	 * Use this function to add any blocking request/response queries.
	 */
	private void updateCommandsNotification() {
		asyncNotify.put(CobraCommands.VCOM_REQ_ALL_DATA_REFRESH,
				new CyclicBarrier(2));
	}

	/**
	 * Checks if Cobra device is connected.
	 * 
	 * @return true, if is connected.
	 */
	public boolean isConnected() {
		if (globV.serialPort == null)
			return false;
		return SerialDriver.isOpen();
	}

	public void VCOM_REQ_QUEUED_FIREDCUES_DATA(int channel) {
		this.messageOutData.put("data", (byte) (channel));
		this.sendMsg(CobraCommands.VCOM_REQ_QUEUED_FIREDCUES_DATA);
	}

	public void getNEXT_SCRIPT_DATA(int scriptid) {

		this.messageOutData.put("data", (byte) (scriptid));
		this.sendMsg(CobraCommands.VCOM_REQ_NEXT_SCRIPT_DATA);
	}

	public void getNEXT_EVENT_DATA(int scriptid, int eventid) {

		globV.setLastScriptCalled(scriptid);
		globV.setLastEventCalled(eventid);
		if (scriptid <= 0)
			scriptid = 0;
		this.messageOutData.put("data", (byte) (scriptid));
		this.messageOutData.put("firsteventbyte", (byte) (eventid >> 8));
		this.messageOutData.put("secondeventbyte", (byte) (eventid));
		this.sendMsg(CobraCommands.VCOM_REQ_NEXT_EVENT_DATA);
	}

	public void VCOM_REQ_QUEUED_MODULE_DATA(int moduleid) {
		this.messageOutData.put("firstbyte", (byte) (moduleid >> 8));
		this.messageOutData.put("secondbyte", (byte) (moduleid));
		AckOwnFlagStatusChange(777);

		this.sendMsg(CobraCommands.VCOM_REQ_QUEUED_MODULE_DATA);
		AckOwnFlagStatusChange(888);
	}

	public void getNext_QUEUED_MODULE_DATA() {

		// if (appClass.getNextModuleId() != -1)
		// {
		// appClass.setLogOnDevice("READY_TO_CALL_MODULE_DATA : "
		// + appClass.getNextModuleId());
		// appClass.setModuleLogOnDevice("READY_TO_CALL_MODULE_DATA : "+appClass.getNextModuleId());
		// appClass.setAN168Log("Cobra  Next Module Calling ID "+
		// appClass.getNextModuleId());
		VCOM_REQ_QUEUED_MODULE_DATA(appClass.getNextModuleId());
		// }
	}

	public void getNext_FIRECUE_DATA() {

		if (appClass.getNextFirecueChannel() != -1)
			VCOM_REQ_QUEUED_FIREDCUES_DATA(appClass.getNextFirecueChannel());

	}

	public void VCOM_REQ_DEVICE_INFO() {
		this.sendMsg(CobraCommands.VCOM_REQ_DEVICE_INFO);
	}

	public static Boolean IsSingleData = false;

	public void AckStatusChange(Boolean IsSingleData, int first, int second,
			int third) {
		if (second == 1) {
			appClass.setNextModuleId(-1);
		}
		Cobra.IsSingleData = IsSingleData;
		this.messageOutData.put("first", (byte) first);
		if (!IsSingleData) {
			this.messageOutData.put("second", (byte) second);
			this.messageOutData.put("third", (byte) third);
		}
		this.sendMsg(CobraCommands.VCOM_ACKNOWLEDGE_STATUS_CHANGE);
	}

	public void AckOwnFlagStatusChange() {
		// int flag_device = appClass.getFlagDeviceUpate();
		// int flag_module = appClass.getFlagModuleUpate();
		// int flag_script = appClass.getFlagScriptsUpate();
		// this.messageOutData.put("device", (byte) flag_device);
		// this.messageOutData.put("module", (byte) flag_module);
		// this.messageOutData.put("scripts", (byte) flag_script);
		// this.sendMsg(CobraCommands.VCOM_OWN_ACK_FLAG_STATUS);
	}

	public void AckOwnFlagStatusChange(int newVal) {
		// int flag_device = appClass.getFlagDeviceUpate();
		// int flag_module = appClass.getFlagModuleUpate();
		// this.messageOutData.put("device", (byte) flag_device);
		// this.messageOutData.put("module", (byte) flag_module);
		// this.messageOutData.put("scripts", (byte) newVal);
		// this.sendMsg(CobraCommands.VCOM_OWN_ACK_FLAG_STATUS);
	}

	public void ReqFireCuesData() {
		this.sendMsg(CobraCommands.VCOM_REQ_FIREDCUES_DATA);
	}

	public void ReqModuleData() {
		this.sendMsg(CobraCommands.VCOM_REQ_MODULE_DATA);
	}

	public void ReqDeviceStatus() {
		this.sendMsg(CobraCommands.VCOM_REQ_DEVICE_STATUS);
	}

	/**
	 * Sets the device to arm mode. The device may not have switched to arm mode
	 * by the time this call returns. Confirm test mode status using
	 * {@link #getDeviceStatus()}.
	 * 
	 */
	public void setArmMode() {
		this.sendMsg(CobraCommands.VCOM_SET_FIRE_MODE);
	}

	/**
	 * Sets the device to test mode. The device may not have switched to test
	 * mode by the time this call returns. Confirm test mode status using
	 * {@link #getDeviceStatus()}.
	 * 
	 */
	public void setTestMode() {
		this.sendMsg(CobraCommands.VCOM_SET_TEST_MODE);
	}

	/**
	 * Send an update to the remote
	 * 
	 */
	public void sendStatusPing() {
		this.sendMsg(CobraCommands.VCOM_STATUS_PINGBACK);
	}

	/**
	 * Sets the device channel. Sends a channel change request to the connected
	 * Cobra device. The device may not change channel before this function
	 * returns, so {@link #getDeviceChannel()} can be called to confirm if
	 * necessary.
	 * 
	 * @param channel
	 *            the channel
	 * @return true, if command successfully sent <br>
	 *         false, if channel not valid
	 */
	public Boolean setDeviceChannel(int channel) {
		if (channel > 99)
			return false;

		this.Channel = channel;
		this.messageOutData.put("channel", (byte) channel);
		this.sendMsg(CobraCommands.VCOM_SET_CHANNEL);

		return true;
	}

	/**
	 * Gets the device channel.
	 * 
	 * @return the current channel setting of the connected device.
	 */
	public int getDeviceChannel() {
		return this.Channel;
	}

	/**
	 * Gets the last known battery level readings of the attached Cobra device.
	 * Each battery reading represents the approximate state of charge
	 * indication in percent. If more than one battery is available, the result
	 * is the bitwise OR of the readings according to the table below.
	 * 
	 * @return the battery level of attached Cobra device.
	 */
	public int getDeviceBatteryLevel() {
		return this.battery;
	}

	/**
	 * Gets the Cobra device mode.
	 * 
	 * @return the device mode. <br>
	 * <br>
	 *         <table>
	 *         <tr>
	 *         <th>Result Code</th>
	 *         <th>Description</th>
	 *         </tr>
	 *         <tr>
	 *         <td>{@link #MODE_TEST}</td>
	 *         <td>Test Mode</td>
	 *         </tr>
	 *         <tr>
	 *         <td>{@link #MODE_ARMED}</td>
	 *         <td>Armed Mode</td>
	 *         </tr>
	 *         </table>
	 */
	public int getMode() {
		return this.mode;
	}

	public int getMode_() {
		return this.Mode;
	}

	/**
	 * Check armed status of all connected modules.
	 * 
	 * @return true, if all modules are armed <br>
	 *         false, if one or more modules are unarmed
	 */
	public boolean checkModulesArmedStatus() {
		if (this.moduleData.size() == 0)
			return true;

		for (Map.Entry<Integer, ModuleDataTag> tag : this.moduleData.entrySet()) {
			if (tag.getValue().armed == false) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Checks battery status of all connected modules.
	 * 
	 * @return true, if all battery levels are OK <br>
	 *         false, if one or more modules is low on battery
	 */
	public boolean checkModulesBattery() {
		for (Map.Entry<Integer, ModuleDataTag> tag : this.moduleData.entrySet()) {
			if (this.isLowBattery(tag.getKey()))
				return false;
		}
		return true;
	}

	/**
	 * Checks if is module <b>modID</b> is low on battery.
	 * 
	 * @param modID
	 *            the module id
	 * @return true, if either of the 2 batteries is low (below 40%)
	 */
	public boolean isLowBattery(int modID) {
		ModuleDataTag tag = this.moduleData.get(modID);
		if (tag.batteryLevel1 <= MODULE_LOW_BATT_THRESHOLD
				|| tag.batteryLevel2 <= MODULE_LOW_BATT_THRESHOLD)
			return true;
		return false;
	}

	public void refreshDeviceStatus() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				// temp();
				refreshDeviceStatus();
			}
		}, 3000);
	}

	/**
	 * Refresh all data. Sends a refresh request to the Cobra device, which will
	 * then begin to resend all current data. Called during connection.
	 */
	public void refreshData() {
		this.sendMsg(CobraCommands.VCOM_REQ_ALL_DATA_REFRESH);
	}

	/**
	 * Refreshes the data for all modules.
	 */
	public void refreshModuleData() {
		if (appClass.isDemoMode)
			this.sendMsg(CobraCommands.VCOM_SET_DUMMY_MODE);
		else
			this.sendMsg(CobraCommands.VCOM_REQ_MODULE_DATA_REFRESH);

		appClass.setFlagModuleUpate(CobraFlags.MODULE_DATA_READY_TO_ACKNOWLEDGE);
		AckOwnFlagStatusChange();
	}

	/**
	 * Gets the API version.
	 * 
	 * @return the API version number.
	 */
	public Version getAPIVersion() {
		return new Version(this.versionAPI);
	}

	/**
	 * Gets the firmware version number of the Cobra device.
	 * 
	 * @return the firmware version
	 */
	public Version getDeviceVersion() {
		return new CobraDataTags.Version(this.versionDevice);
	}

	/**
	 * Gets the type of device connected to the VCOM port.
	 * 
	 * @return the device type. Refer to table below.
	 * 
	 * <br>
	 * <br>
	 *         <table>
	 *         <tr>
	 *         <th>Type Code</th>
	 *         <th>Description</th>
	 *         </tr>
	 *         <tr>
	 *         <td>{@link #DEVICE_TYPE_UNKNOWN}</td>
	 *         <td>Unknown/unrecognizable device type</td>
	 *         </tr>
	 *         <tr>
	 *         <td>{@link #DEVICE_TYPE_18R2}</td>
	 *         <td>18R2 device</td>
	 *         </tr>
	 *         </table>
	 */
	public int getDeviceType() {
		int type = this.deviceType;
		if (type != Cobra.DEVICE_TYPE_18R2)
			return Cobra.DEVICE_TYPE_UNKNOWN;
		return type;
	}

	/**
	 * Gets a {@link SparseArray} of objects containing information pertaining
	 * to each module on the network. It is stored with module ID as key and
	 * {@link ModuleDataTag} as the value. Only those modules that exist on the
	 * network are in this array.
	 * 
	 * @return the module data {@link SparseArray}
	 */
	public SparseArray<ModuleDataTag> getModuleData() {
		SparseArray<ModuleDataTag> tags = new SparseArray<ModuleDataTag>();

		for (Map.Entry<Integer, ModuleDataTag> tag : this.moduleData.entrySet()) {
			tags.put(tag.getKey(), new ModuleDataTag(tag.getValue()));
		}

		return tags;
	}

	// public long getModuleLightsStatus(int _channel) {
	// if (moduleLightsHandlerList != null) {
	// for (int i = 0; i < moduleLightsHandlerList.size(); i++) {
	// if (moduleLightsHandlerList.get(i).getChannel() == _channel) {
	// long temp = moduleLightsHandlerList.get(i).getCueList();
	// appClass.setScriptsLog("GETTING -- channel : " + _channel
	// + " cueList : " + temp);
	//
	// return temp;
	// }
	// }
	// }
	// return 0;
	// }

	public ArrayList<String> getModuleLightsStatus(int _channel) {
		if (appClass.moduleLightsHandlerArray != null) {
			for (int i = 0; i < appClass.moduleLightsHandlerArray.length; i++) {
				if (appClass.moduleLightsHandlerArray[i].getChannel() == _channel) {
					ArrayList<String> temp = appClass.moduleLightsHandlerArray[i]
							.getCueStringList();

					return temp;
				}
			}
		}
		return null;
	}

	public void displayAllScripts() {
		if (moduleLightsHandlerList != null) {
			appClass.moduleLightsHandlerArray = new ModuleLightsHandler[moduleLightsHandlerList// All
																								// data
																								// against
																								// events
																								// is
																								// in
																								// this
																								// array
					.size()];

			for (int i = 0; i < moduleLightsHandlerList.size(); i++) {
				int channel = moduleLightsHandlerList.get(i).getChannel();
				long cueList = moduleLightsHandlerList.get(i).getCueList();
				int eventIndex = moduleLightsHandlerList.get(i).eventIndex;
				ArrayList<String> cueListString = moduleLightsHandlerList
						.get(i).getCueStringList();
				ModuleLightsHandler handler = new ModuleLightsHandler(channel,
						eventIndex, cueList, cueListString);

				// appClass.writeIntoLog(cueListString, "From line 697 cobra");

				appClass.moduleLightsHandlerArray[i] = handler;

				long temp = moduleLightsHandlerList.get(i).getCueList();

				// appClass.setAN188Log("i=" + i + "-channel : "
				// + moduleLightsHandlerList.get(i).getChannel()
				// + " cueList : " + temp);

			}
		}
	}

	public void clearModuleLightStatus() {
		if (moduleLightsHandlerList != null)
			moduleLightsHandlerList.clear();
		moduleLightsHandlerList = null;
	}

	private ArrayList<String> getCuesArray(long cues) {
		ArrayList<String> cuesList = new ArrayList<String>();
		if (cues == 0l) {
			cuesList.add("" + 1);
			return cuesList;
		}
		int bit = 1;

		// checks bit by bit, from the LSB to the MSB, if any bit is set; if so,
		// it adds the corresponding cue to the string
		for (int j = 1; j < 19; j++) {
			if ((cues & bit) == bit) {
				cuesList.add("" + j);
			}
			bit = bit * 2;
		}
		return cuesList;
	}

	private void updateModuleLightStatus(int _channel, int eventIndex,
			long cueList) {
		Boolean isFound = false;
		if (moduleLightsHandlerList == null)
			moduleLightsHandlerList = new ArrayList<ModuleLightsHandler>();

		for (int i = 0; i < moduleLightsHandlerList.size(); i++) {
			if (moduleLightsHandlerList.get(i).getChannel() == _channel) {
				isFound = true;
				int pre_eventIndex = moduleLightsHandlerList.get(i).eventIndex;

				// appClass.writeIntoLog(getCuesArray(cueList), "channel: "
				// + _channel + " Event: " + eventIndex);

				if (pre_eventIndex != eventIndex) {

					long pre_cueList = moduleLightsHandlerList.get(i)
							.getCueList();

					ArrayList<String> preCuesListString = moduleLightsHandlerList
							.get(i).getCueStringList();
					ArrayList<String> currentCuesListString = getCuesArray(cueList);

					if (preCuesListString == null
							|| preCuesListString.size() == 0)
						moduleLightsHandlerList.get(i).setCueStringList(
								currentCuesListString);
					else {
						for (int e = 0; e < currentCuesListString.size(); e++) {

							moduleLightsHandlerList.get(i).setThisCue(
									currentCuesListString.get(e));
						}

					}

					// /TEST///

					moduleLightsHandlerList.get(i).setCueList(
							pre_cueList | getCuesValue(cueList));
					moduleLightsHandlerList.get(i).eventIndex = eventIndex;
				}

				// appClass.writeIntoLog(moduleLightsHandlerList.get(i)
				// .getCueStringList(), "CUE LIST STRING----channel: "
				// + _channel + " Event: " + eventIndex);

				break;
			}
		}

		if (!isFound) {

			long temp = cueList;
			if (temp == 255)
				temp = 1;

			ArrayList<String> currentCuesListString = getCuesArray(cueList);

			ModuleLightsHandler handler = new ModuleLightsHandler(_channel,
					eventIndex, temp, currentCuesListString);
			moduleLightsHandlerList.add(handler);

			// /TEST///

		}

	}

	// private void getCuesArray(long cues) {
	// ArrayList<String> cuesList = new ArrayList<String>();
	// if (cues == 0l) {
	// cuesList.add("" + 1);
	// // return cuesList;
	// }
	// int bit = 1;
	//
	// // checks bit by bit, from the LSB to the MSB, if any bit is set; if so,
	// // it adds the corresponding cue to the string
	// for (int j = 1; j < 19; j++) {
	// if ((cues & bit) == bit) {
	// // cuesList.add("" + j);
	// getActiveCues(j);
	// }
	// bit = bit * 2;
	// }
	// // return cuesList;
	// }

	private long getCuesValue(long cueList) {
		switch ((int) cueList) {
		case 1:

			return (long) Math.pow(2, 0);
		case 2:

			return (long) Math.pow(2, 1);
		case 4:

			return (long) Math.pow(2, 2);
		case 8:

			return (long) Math.pow(2, 3);
		case 16:

			return (long) Math.pow(2, 4);
		case 32:

			return (long) Math.pow(2, 5);
		case 64:

			return (long) Math.pow(2, 8);
		case 128:

			return (long) Math.pow(2, 9);
		case 256:

			return (long) Math.pow(2, 10);
		case 512:

			return (long) Math.pow(2, 11);
		case 1024:

			return (long) Math.pow(2, 12);
		case 2048:

			return (long) Math.pow(2, 13);
		case 4096:

			return (long) Math.pow(2, 16);
		case 8192:

			return (long) Math.pow(2, 17);
		case 16384:

			return (long) Math.pow(2, 18);
		case 32768:

			return (long) Math.pow(2, 19);
		case 65536:

			return (long) Math.pow(2, 20);
		case 131072:
			return (long) Math.pow(2, 21);
		}
		return (long) cueList;
		// return (long) 0;
	}

	/**
	 * Gets a {@link SparseArray} of objects containing information pertaining
	 * to the scriptCues and firedCues for each channel. It is stored with
	 * channel ID as key and {@link ChannelDataTag} as the value. Only those
	 * channels that are fired or scripted to fire are this array.
	 * 
	 * @return each channel data {@link SparseArray}
	 */
	public SparseArray<ChannelDataTag> getChannelData() {
		SparseArray<ChannelDataTag> tags = new SparseArray<ChannelDataTag>();

		for (Entry<Integer, ChannelDataTag> tag : this.channelData.entrySet()) {
			tags.put(tag.getKey(), new ChannelDataTag(tag.getValue()));
		}

		return tags;
	}

	/**
	 * Opens the USB connection with the connected Cobra device and begins to
	 * download device data. This function will <b>BLOCK</b> until all required
	 * data is downloaded, so it is recommended to run on separate thread until
	 * connection is established. The device must be connected and USB
	 * permissions must be acquired before connecting.
	 * 
	 * 
	 * @return {@link #COBRA_CODE_NODEVICE}, if not device connected. <br>
	 *         {@link #SERIAL_CODE_NOPERM}, if permission to access device was
	 *         not acquired/disgranted
	 */
	public int openDevice() {

		appClass.SerialPortStatus = -2;

		// ActivityManager manager = (ActivityManager) context
		// .getSystemService(Context.ACTIVITY_SERVICE);
		//
		// for (RunningServiceInfo service : manager
		// .getRunningServices(Integer.MAX_VALUE)) {
		// if (ReaderService.class.getName().equals(
		// service.service.getClassName())) {
		//
		// context.stopService(new Intent(context, ReaderService.class));
		// SerialDriver.close();
		// }
		// }

		// while (appClass.SerialPortStatus < -1) {
		// }
		// int ret = appClass.SerialPortStatus;
		int ret = globV.serialPort.open();

		if (ret == SerialDriver.SERIAL_CODE_NODEVICE) {
			return COBRA_CODE_NODEVICE;
		} else if (ret == SerialDriver.SERIAL_CODE_NOPERM) {
			return COBRA_CODE_NOPERM;
		} else if (ret < 0) {
			return ret;
		}

		Log.i(TAG, "Connecting...");
		updateDeviceInfo();
		return ret;
	}

	/**
	 * Requests device to send all data. Blocks until all the data is received.
	 */
	private void updateDeviceInfo() {
		Log.i(TAG, "Data refresh...");
		// this.refreshData();
		Log.i(TAG, "Done Data refresh...");

		try {
			Log.i(TAG, "AN...");
			appClass.TitleText = "Connecting";
			appClass.UpdateIcon = true;
			// this.asyncNotify
			// .get(CobraCommands.VCOM_REQ_ALL_DATA_REFRESH & 0xFF)
			// .await(20, TimeUnit.SECONDS);
			try {
				ConnectCobra connect = new ConnectCobra();
				connect.execute();
				connect.get(5, TimeUnit.SECONDS);

			} catch (Exception e) {
				// Toast.makeText(context,
				// "Cobra Connect Timeout: " + e.getMessage(),
				// Toast.LENGTH_LONG).show();
			}
			Log.i(TAG, "AN Done...");
		} catch (Exception e) {
			// Toast.makeText(context, "Timeout", Toast.LENGTH_LONG).show();
			Log.e(TAG, e.getMessage());
			appClass.TitleText = "disconnected";
			appClass.UpdateIcon = true;
			// appClass.createReadingLogOnDevice("Cobra",
			// "CyclicBarrier Exception : " + e.getMessage());
			// appClass.log_Parser("CyclicBarrier Exception : " +
			// e.getMessage());
		}

		appClass.TitleText = "Connected";
		// appClass.createReadingLogOnDevice("Cobra", "CyclicBarrier End ");
		// appClass.log_Parser("CyclicBarrier End : ");

		Log.v(TAG, "\nCobra API Version: " + this.getAPIVersion().major + "."
				+ this.getAPIVersion().minor + "."
				+ this.getAPIVersion().revision);
		String type = (this.getDeviceType() == 1) ? "18R2" : "NUL";
		String temp = "Connected to Device: " + type + " Firmware: "
				+ this.getDeviceVersion().major + "."
				+ this.getDeviceVersion().minor + "."
				+ this.getDeviceVersion().revision;
		// appClass.createReadingLogOnDevice("Cobra", TAG + temp);
		Log.v(TAG, temp);
	}

	/**
	 * Closes the USB device connection. No further API calls should be made
	 * before calling {@link #openDevice()} the next time.
	 * 
	 */
	public void closeDevice() {
		// if (!this.isConnected())
		// return;
		try {
			Log.i("Unregister", "USBReciever");
			this.listenerList.clear();
			MainActivity.AckThread.interrupt();
			if (appClass.onEventChange != null)
				appClass.onEventChange.onEventChange();

			activity.unregisterReceiver(mUsbReceiver2);
			clearScriptObjects();

		} catch (Exception e) {
		}
	}

	/**
	 * The armed status of the device.
	 * 
	 * @return true, if armed.
	 * 
	 */
	public boolean isArmed() {
		// return (this.Mode == MODE_ARMED);
		return (PersistentHeader.Mode == Cobra.MODE_ARMED);
	}

	/**
	 * Forces a soft reset of the device. This will require re-establishing
	 * communication with the device after a period of time.
	 * 
	 */
	public void rebootDevice() {
		this.sendMsg(CobraCommands.VCOM_SET_DEVICE_REBOOT);
		this.closeDevice();
	}

	/**
	 * Fire cues. CueList Is a 32-bit field that contains information for each
	 * cue. The bits are arranged with bit 0 representing cue 1, and bit 17
	 * representing cue 18. A set (1) bit will be fired. <br>
	 * If device is not armed, this function will not do anything.
	 * 
	 * @param cueList
	 *            the binary cue list
	 * @param channel
	 *            the channel
	 */
	public void fireCues(int cueList, byte channel) {
		if (!isArmed())
			return;
		this.setDeviceChannel(channel);
		this.messageOutData.put("cueList0", (byte) (cueList));
		this.messageOutData.put("cueList1", (byte) (cueList >> 8));
		this.messageOutData.put("cueList2", (byte) (cueList >> 16));

		this.sendMsg(CobraCommands.VCOM_REQ_FIRE_CUES);
	}

	/**
	 * Gets the array of script headers.
	 * 
	 * @return the scripts data array
	 */
	public ArrayList<ScriptIndexTag> getScriptsData() {
		ArrayList<ScriptIndexTag> scripts = null;
		if (getScriptIndexData_List() != null) {
			for (ScriptIndexTag tag : getScriptIndexData_List()) {
				if (scripts == null)
					scripts = new ArrayList<ScriptIndexTag>();
				scripts.add(new ScriptIndexTag(tag));
			}
		}
		return scripts;

	}

	/**
	 * Gets an {@link ArrayList} of {@link SparseArray} containing event data.
	 * The SparseArray contains each event with eventID as key and
	 * {@link #EventDataTag} as value.
	 * 
	 * @return the events data
	 */
	public SparseArray<SparseArray<EventDataTag>> getEventsData() {
		SparseArray<SparseArray<EventDataTag>> events = new SparseArray<SparseArray<EventDataTag>>();

		for (int scriptIdx : eventData.keySet()) {
			SparseArray<EventDataTag> tags = eventData.get(scriptIdx);
			events.put(scriptIdx, new SparseArray<EventDataTag>());
			SparseArray<EventDataTag> eventsToAdd = events.get(scriptIdx);

			for (int i = 0; i < tags.size(); i++) {

				int key = tags.keyAt(i);

				eventsToAdd.put(key, new EventDataTag(tags.get(key)));

			}
		}

		return addShiftedEventTimeIndex(events);
	}

	/**
	 * Adds the shifted event time index.
	 * 
	 * @param list
	 *            the list
	 * @return the sparse array
	 */
	public SparseArray<SparseArray<EventDataTag>> addShiftedEventTimeIndex(
			SparseArray<SparseArray<EventDataTag>> list) {

		if (list == null)
			return list;
		long lastStepTimeIndex = 0;
		for (int i = 0; i < list.size(); i++) {
			lastStepTimeIndex = 0;
			SparseArray<EventDataTag> currentArray = list.get(i);
			if (currentArray == null)
				continue;
			for (int j = 0; j < currentArray.size(); j++) {
				EventDataTag currentTag = list.get(i).get(list.get(i).keyAt(j));
				if (currentTag.timeIndex >= 1048576) {
					lastStepTimeIndex = currentTag.timeIndex - 1048576;
				} else {
					currentTag.shiftedTimeIndex = currentTag.timeIndex
							- lastStepTimeIndex;


				}
			}
		}

		return list;
	}

	/**
	 * Performs as step.
	 */
	public void stepNext() {
		this.sendMsg(CobraCommands.VCOM_REQ_STEP_NEXT);
	}

	/**
	 * Start script. If remote is not armed, it will do nothing.
	 * 
	 * @param scriptIndex
	 *            Index of the script to play.
	 * @param startPaused
	 *            pause the script after initialization
	 */
	public void startScript(int scriptIndex, boolean startPaused) {
		if (!isArmed()) {
			// appClass.setPlayScriptLog("isArmed() is false");
			return;
		}
		// appClass.setPlayScriptLog("scriptIndex : " + scriptIndex
		// + " & startPaused : " + startPaused);
		messageOutData.put("scriptIndex", (byte) scriptIndex);
		messageOutData.put("startPaused", (byte) (startPaused ? 1 : 0));
		this.sendMsg(CobraCommands.VCOM_REQ_PLAY_SCRIPT);
	}

	/**
	 * Pause currently active script.
	 */
	public void pauseScript() {
		this.sendMsg(CobraCommands.VCOM_REQ_PAUSE_SCRIPT);
	}

	/**
	 * Resume currently paused script.
	 */
	public void resumeScript() {
		this.sendMsg(CobraCommands.VCOM_REQ_RESUME_SCRIPT);
	}

	/**
	 * Stop currently active script.
	 */
	public void stopScript() {
		this.sendMsg(CobraCommands.VCOM_REQ_STOP_SCRIPT);
	}

	/**
	 * Jump to a time index for the currently playing script.
	 * 
	 * @param timeIndex
	 *            the time index to jump to
	 */
	public void jumpToTime(long timeIndex) {
		messageOutData.put("jumpTimeIndex0", (byte) (timeIndex >> 24));
		messageOutData.put("jumpTimeIndex1", (byte) (timeIndex >> 16));
		messageOutData.put("jumpTimeIndex2", (byte) (timeIndex >> 8));
		messageOutData.put("jumpTimeIndex3", (byte) (timeIndex));
		this.sendMsg(CobraCommands.VCOM_REQ_JUMPTO_TIME);
	}

	/**
	 * Jump to a specific event for the currently playing script.
	 * 
	 * @param eventIndex
	 *            the event index
	 */
	public void jumpToEvent(int eventIndex) {

		// Toast.makeText(activity, eventIndex, Toast.LENGTH_LONG).show();

		messageOutData.put("jumpEventIndex0", (byte) (eventIndex >> 8));
		messageOutData.put("jumpEventIndex1", (byte) (eventIndex));
		this.sendMsg(CobraCommands.VCOM_REQ_JUMPTO_EVENT);

	}

	/**
	 * The Class CobraEvent.
	 */
	public class CobraEvent extends EventObject {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		/** The event type. */
		private final int eventType;

		/** The cobra event data. */
		private final Object cobraEventData;

		/**
		 * Instantiates a new cobra event object.
		 * 
		 * @param eventType
		 *            the event type, see <b>EVENT_TYPE_*</b>
		 * @param eventData
		 *            the event data
		 */
		public CobraEvent(int eventType, Object eventData) {
			super(Cobra.this);
			this.eventType = eventType;
			this.cobraEventData = eventData;
		}

		/**
		 * Gets the Cobra event type. See <b>EVENT_TYPE_*</b>.
		 * 
		 * @return the event type
		 */
		public int getEventType() {
			return this.eventType;
		}

		/**
		 * Gets the Cobra event data. Make sure to cast it to relevant object
		 * based on the table below.
		 * 
		 * @return the event data object <br>
		 *         <table>
		 *         <tr>
		 *         <th>Event Code</th>
		 *         <th>Cast To</th>
		 *         </tr>
		 *         <tr>
		 *         <td>{@link #EVENT_TYPE_DISCONNECTED}</td>
		 *         <td>N/A</td>
		 *         </tr>
		 *         <tr>
		 *         <td>{@link #EVENT_TYPE_STATUS_CHANGE}</td>
		 *         <td>N/A</td>
		 *         </tr>
		 *         <tr>
		 *         <td>{@link #EVENT_TYPE_MODULE_DATA_CHANGE}</td>
		 *         <td>{@link CobraDataTags.ModuleDataTag}</td>
		 *         </tr>
		 *         <tr>
		 *         <td>{@link #EVENT_TYPE_CHANNEL_DATA_CHANGE}</td>
		 *         <td>{@link CobraDataTags.ChannelDataTag}</td>
		 *         </tr>
		 *         <tr>
		 *         <td>{@link #EVENT_TYPE_SCRIPT_PING}</td>
		 *         <td>{@link CobraDataTags.ScriptPingTag}</td>
		 *         </tr>
		 *         <tr>
		 *         <td>{@link #EVENT_CLEAR_MODULES}</td>
		 *         <td>N/A</td>
		 *         </tr>
		 *         </table>
		 */
		public Object getCobraEventData() {
			return this.cobraEventData;
		}

	}

	// public class CobraEvent2 extends EventObject implements Serializable{
	//
	// private static final long serialVersionUID = 1L;
	//
	// private final int eventType;
	//
	// private final Object cobraEventData;
	//
	// public CobraEvent2(int eventType, Object eventData) {
	// super(Cobra.this);
	// this.eventType = eventType;
	// this.cobraEventData = eventData;
	// }
	//
	// public int getEventType() {
	// return this.eventType;
	// }
	//
	// public Object getCobraEventData() {
	// return this.cobraEventData;
	// }
	//
	// }

	static ICommandEventListener commandListener;

	public void addCommandEventListener(ICommandEventListener listener) {
		commandListener = listener;
	}

	public void removeCommandEventListener() {
		commandListener = null;
	}

	public static void fireCommandEventLister(Spanned text) {
		if (commandListener != null)
			commandListener.OnCommandChanged(text);
	}

	public static interface ICommandEventListener extends EventListener {
		public void OnCommandChanged(Spanned text);
	}

	/**
	 * The listener interface for receiving cobraEvent events. The class that is
	 * interested in processing a cobraEvent event implements this interface,
	 * and the object created with that class is registered with a component
	 * using the component's <code>addCobraEventListener<code> method. When
	 * serial data is available, that object's appropriate
	 * method is invoked.
	 * 
	 * @see CobraEvent
	 */

	// ICobraEventListener listener for module related data
	public static interface ICobraEventListener extends EventListener {

		/**
		 * On event.
		 * 
		 * @param event
		 *            object.
		 */
		public void onDeviceDataChange(CobraEvent event);
	}

	/**
	 * Adds the event listener.
	 * 
	 * @param listener
	 *            the listener
	 */
	public void addEventListener(ICobraEventListener listener) {
		this.listenerList.add(listener);
	}

	/**
	 * Removes the event listener.
	 * 
	 * @param listener
	 *            the listener
	 */
	public void removeEventListener(ICobraEventListener listener) {
		this.listenerList.remove(listener);
	}

	/**
	 * Removes all event listeners.
	 * 
	 */
	public void removeAllEventListeners() {
		this.listenerList.clear();
	}

	// ICobraEventListener listener_Header;

	// public void AddHeaderListener(ICobraEventListener listener) {
	// this.listener_Header = listener;
	// }

	ICobraEventListener listener_Module;
	ArrayList<ICobraEventListener> listener_BucketFiring;

	public void AddModuleListener(ICobraEventListener listener) {
		this.listener_Module = listener;
	}

	// ICobraEventListener listener_ShowControl;

	// public void AddShowControlListener(ICobraEventListener listener) {
	// this.listener_ShowControl = listener;
	// }

	public void AddBucketFiringListener(ICobraEventListener listener) {
		if (listener_BucketFiring == null)
			listener_BucketFiring = new ArrayList<Cobra.ICobraEventListener>();
		this.listener_BucketFiring.add(listener);
	}

	public void RemoveBucketFiringListener() {
		if (listener_BucketFiring != null)
			listener_BucketFiring.clear();
		this.listener_BucketFiring = null;
	}

	/**
	 * Fire serial input event on the UI thread.
	 * 
	 * @param event
	 *            the event
	 */

	private void fireSerialInputEvent(final CobraEvent event) {
		Activity activity = (Activity) context;

		for (ICobraEventListener l : this.listenerList) {
			final ICobraEventListener temp = l;
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					temp.onDeviceDataChange(event);
				}
			});
		}
	}

	// private void fireHeaderListener(CobraEvent event) {
	// if (this.listener_Header != null)
	// this.listener_Header.onDeviceDataChange(event);
	// }

	private void fireModuleListener(final CobraEvent event) {
		if (this.listener_Module != null) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					listener_Module.onDeviceDataChange(event);
				}
			});
		}
	}

	// private void fireShowControlListener(final CobraEvent event) {
	// if (this.listener_ShowControl != null) {
	// activity.runOnUiThread(new Runnable() {
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// listener_ShowControl.onDeviceDataChange(event);
	// }
	// });
	// }
	// }

	private void fireBucketFiringListener(final CobraEvent event) {
		if (this.listener_BucketFiring != null) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					for (ICobraEventListener listener : Cobra.this.listener_BucketFiring)
						listener.onDeviceDataChange(event);
				}
			});
		}
	}

	/**
	 * Send the message with command code. Look at examples to send data.
	 * 
	 * @param command
	 *            the command
	 */

	private void sendMsg(int command) {
		byte[] msg;
		try {
			msg = CobraMessage.createMsg(command, this.messageOutData);
			SerialDriver.write(msg);

			String message = "";
			if (CobraCommands.VCOM_REQ_QUEUED_MODULE_DATA == command) {
				for (int i = 0; i < msg.length; i++) {
					message += "  " + (msg[i] & 0xFF);
				}
				appClass.setAN106Logs(System.currentTimeMillis()
						+ " Cobra: Writing Message , message=" + message);
			}

			if (CobraCommands.VCOM_REQ_PLAY_SCRIPT == command) {
				// appClass.setPlayScriptLog("Command sent : ");
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (CobraCommands.VCOM_REQ_PLAY_SCRIPT == command) {
				// appClass.setPlayScriptLog("EXCEPTION : " + e.getMessage());
			}
			if (CobraCommands.VCOM_REQ_QUEUED_MODULE_DATA == command)
				appClass.setAN106Logs(System.currentTimeMillis()
						+ " Cobra: Exception , message=" + e.getMessage());
		}
	}

	/**
	 * Checks if the current status data is out of sync.
	 * 
	 * @param chan
	 *            the chan
	 * @param batt
	 *            the batt
	 * @param mode
	 *            the mode
	 * @return true, if status is out of sync
	 */
	// private Boolean checkStatusDataChanged(int chan, int batt, int mode) {
	// Boolean i1 = (chan == this.channel.intValue());
	// Boolean i2 = (batt == this.battery.intValue());
	// Boolean i3 = (mode == this.mode.intValue());
	// if (!i1 || !i2 || !i3) {
	// return true;
	// }
	// return false;
	// }

	/**
	 * The Class ParseMessages, a thread the parses all incoming messages and
	 * updates Cobra API.
	 */
	private class ParseMessages implements Runnable {
		@Override
		public void run() {
			try {
				int modID, channelID, scriptIndex, testResults;
				ModuleType modType;
				long scriptCues, firedCues;

				Object eventDataObject = null;
				int[] data = null;
				while (true) {
					int totalMsgs, currentMsgNum;
					IntBuffer buffer;
					MessageDataTag tempData;
					int eventType = 0;
					String broadcaseEventType = null;
					/*
					 * if (!isConnected()) { try { Thread.sleep(25); } catch
					 * (InterruptedException e) { // TODO Auto-generated catch
					 * block e.printStackTrace(); } return; }
					 */

					// acquireReadToken();

					if (messageThread.isInterrupted())
						return;

					tempData = dataBuffer.poll();

					if (tempData == null) {
						// Log.e(TAG, "Null Data!");
						continue;
					}

					if (tempData.checkSumPass == false) {
						Log.e(TAG, "Checksum error!");
						continue;
					}

					currentMsgNum = tempData.msgNumber;
					totalMsgs = tempData.totalMsgs;

					buffer = IntBuffer.allocate((totalMsgs + 1)
							* CobraCommands.MAX_MESSAGE_BYTES);
					buffer.put(tempData.commandCode);
					buffer.put(tempData.data);

					while (currentMsgNum < totalMsgs) {
						// acquireReadToken();

						if (messageThread.isInterrupted())
							return;

						tempData = dataBuffer.poll();
						if (tempData == null) {
							continue;
						}
						currentMsgNum = tempData.msgNumber;
						totalMsgs = tempData.totalMsgs;

						buffer.put(tempData.data);
					}

					data = new int[buffer.position()];
					System.arraycopy(buffer.array(), 0, data, 0,
							buffer.position());

					buffer.clear();
					buffer.compact();
					Log.i(TAG, String.format("READ_MSG CLN -- R:%d Msg: %s",
							data.length, SerialDriver.getHex(data)));

					int opCode = data[0];
					if (opCode == 101) {
						int i = 0;
						i++;
					}
					// TODO: check if the size of the data received matches what
					// is
					// expected
					switch (opCode) {
					case CobraCommands.ACK_REQ_DEVICE_STATUS:
						if (data.length < CobraCommands.ACK_REQ_DEVICE_STATUS_BYTES
								// || appClass.getFlagScriptsUpate() ==
								// CobraFlags.READY_TO_CALL_SCRIPTS_DATA
								|| appClass.getFlagScriptsUpate() == CobraFlags.SCRIPT_DATA_IS_CALLING) {
							Log.e(TAG,
									"ACK_REQ_DEVICE_STATUS_BYTES: not enough data received");
							continue;
						}
						/*
						 * If Fire listener if flag is ZERO. Ignore 18R2
						 * Notification is flag is greater then ZERO. if flag is
						 * greater than ZERO, it means we have'nt received
						 * previous updates yet. So wait for them
						 */
						if (appClass.getFlagScriptsUpate() != CobraFlags.SCRIPT_DATA_IS_CALLING) {

							if (ACK_Event_Creator.HaveEvent(data[1], data[2],
									data[3])) {
								int temp = 0;
								if (data[2] == 1
										&& appClass.getFlagModuleUpate() == CobraFlags.MODULE_DATA_NOT_READY_TO_ACKNOWLEDGE) {
									// appClass.setAN106NewLog("MainActivity line: "
									// + 1581);
									appClass.setFlagModuleUpate(CobraFlags.MODULE_DATA_READY_TO_ACKNOWLEDGE);
								}
								if (data[1] == 1) {
									temp = ACK_Event_Creator.getEvent(data[1],
											0, data[3]);

								} else if (appClass.getFlagDeviceUpate() == CobraFlags.DEVICE_STATUS_READY_TO_ACKNOWLEDGE)
									temp = ACK_Event_Creator.getEvent(data[1],
											data[2], data[3]);
								if (temp != 0) {
									eventType = temp;
								}
							}
						}
						// if (appClass.FlagDeviceUpate == 0) {
						// eventType = EVENT_ACK_DEVICE_DATA_CHANGE;
						//
						// }
						break;
					case CobraCommands.VCOM_REQ_ALL_DATA_REFRESH:
						if (data.length < CobraCommands.VCOM_REQ_ALL_DATA_REFRESH_BYTES) {
							Log.e(TAG,
									"VCOM_REQ_ALL_DATA_REFRESH: not enough data received");
							continue;
						}
						/*
						 * 18R2 sends back VCOM_REQ_ALL_DATA_REFRESH when all
						 * scripts get completed. so set scripts flag to 3 to
						 * indicate that all scripts are loaded sucessfully
						 */

						if ((appClass.getFlagScriptsUpate() == CobraFlags.SCRIPT_DATA_IS_CALLING)) {
							{
								MainActivity.scriptStatus = 1;
								appClass.setFlagScriptsUpate(CobraFlags.ALL_SCRIPT_DATA_IS_FINISHED);
								AckOwnFlagStatusChange();

							}
						}

						Log.v(TAG, "Refresh Complete");
						try {
							asyncNotify.get(opCode).await();
						} catch (Exception e) {
							Log.e(TAG, e.toString());
						}
						break;

					case CobraCommands.VCOM_DEVICE_STATUS:
						if (data.length < CobraCommands.VCOM_DEVICE_STATUS_BYTES
								// || appClass.getFlagScriptsUpate() ==
								// CobraFlags.READY_TO_CALL_SCRIPTS_DATA
								|| appClass.getFlagScriptsUpate() == CobraFlags.SCRIPT_DATA_IS_CALLING) {
							Log.e(TAG,
									"VCOM_DEVICE_STATUS: not enough data received");
							continue;
						}

						Channel = data[1];
						battery = data[2];
						mode = data[3];
						Mode = data[3];
						if (appClass.getFlagDeviceUpate() == CobraFlags.READY_TO_CALL_DEVICE_STATUS) {
							appClass.writeLog("Flag Device Update State RECIEVED_DEVICE_STATUS : "
									+ CobraFlags.RECIEVED_DEVICE_STATUS);
							appClass.setFlagDeviceUpate(CobraFlags.RECIEVED_DEVICE_STATUS);
							AckOwnFlagStatusChange();
						}
						broadcaseEventType = appClass.RECIEVER_PERSISTENT_HEADER;
						eventType = Cobra.EVENT_TYPE_STATUS_CHANGE;
						// }
						sendStatusPing();
						break;

					case CobraCommands.VCOM_REQ_DEVICE_INFO:
						if (data.length < CobraCommands.VCOM_REQ_DEVICE_INFO_BYTES
								// || appClass.getFlagScriptsUpate() ==
								// CobraFlags.READY_TO_CALL_SCRIPTS_DATA
								|| appClass.getFlagScriptsUpate() == CobraFlags.SCRIPT_DATA_IS_CALLING) {
							Log.e(TAG,
									"VCOM_REQ_DEVICE_INFO: not enough data received");
							continue;
						}

						eventType = Cobra.EVENT_TYPE_DEVICE_INFO_CHANGE;
						broadcaseEventType = appClass.RECIEVER_PERSISTENT_HEADER;
						deviceType = data[1];
						versionDevice = new Version(data[2], data[3], data[4],
								(char) (data[5]));
						break;

					case CobraCommands.VCOM_MODULE_DATA:
						if (data.length < CobraCommands.VCOM_MODULE_DATA_BYTES
						// || appClass.getFlagScriptsUpate() ==
						// CobraFlags.READY_TO_CALL_SCRIPTS_DATA
								|| appClass.getFlagScriptsUpate() == CobraFlags.SCRIPT_DATA_IS_CALLING) {
							Log.e(TAG,
									"VCOM_MODULE_DATA: not enough data received");
							continue;
						}
						/*
						 * If module id is 255(-1) it means all modules are
						 * loaded Set Module Flag to 0, so we will be able to
						 * acknowledge next module change Set Scripts flag to 1.
						 * It will allow us to call scripts data in
						 * MainActivity->checker(timer) If scripts flag is not
						 * ZERO, it means all scripts are already being loaded
						 * or loading or requested. So do'nt change it to 1.
						 */

						modID = (data[1] << 8) | data[2];
						// appClass.setAN168Log("MODULE ID  IN COBRA " + modID);
						if (modID == -2 || modID == 65534) {// -2
							appClass.IsRefreshPressed = false;
						}

						if (modID == 255 || modID == 65535) {
							if (appClass.getFlagModuleUpate() == CobraFlags.READY_TO_CALL_NEXT_MODULE_DATA
									|| appClass.getFlagModuleUpate() == CobraFlags.WAIT_FOR_REQUESTED_MODULE_DATA) {
								// appClass.setAN106NewLog("MainActivity line: "
								// + 1701);
								appClass.setFlagModuleUpate(CobraFlags.MODULE_DATA_READY_TO_ACKNOWLEDGE);
								appClass.setNextModuleId(-1);
								AckOwnFlagStatusChange();
							} else
								appClass.setModuleLogOnDevice("------------------TRIED TO CHANGE FLAG ON WRONG STATE ALL RECIEVED. READY TO ACK "
										+ appClass.getFlagModuleUpate());

							break;
						}
						// if (appClass.getFlagModuleUpate() ==
						// CobraFlags.READY_TO_CALL_MODULE_DATA) {
						//
						// appClass.setFlagModuleUpate(CobraFlags.READY_TO_CALL_NEXT_MODULE_DATA);
						// AckOwnFlagStatusChange();
						//
						// }
						eventType = Cobra.EVENT_TYPE_MODULE_DATA_CHANGE;
						broadcaseEventType = appClass.RECIEVER_PERSISTENT_HEADER;
						testResults = (data[3] & 0xFF) | (data[4] & 0xFF) << 8
								| (data[5] & 0xFF) << 16;

						if ((testResults & CobraCommands.NINETYM_MODULE_FLAG) != 0) {
							modType = ModuleType.NINETY_M_SUB;

							if (moduleData.containsKey(modID - 5)) {
								if (moduleData.get(modID - 5).modType == ModuleType.NINETY_M_MAIN) {
									modType = ModuleType.NINETY_M_MAIN;
								}
							}

							if (moduleData.containsKey(modID - 1)) {
								if (moduleData.get(modID - 1).modType != ModuleType.NINETY_M_MAIN
										&& moduleData.get(modID - 1).modType != ModuleType.NINETY_M_SUB) {
									modType = ModuleType.NINETY_M_MAIN;
								}
							} else {
								modType = ModuleType.NINETY_M_MAIN;
							}

						} else if ((testResults & CobraCommands.AUDIOBOX_MODULE_FLAG) != 0) {
							modType = ModuleType.AUDIOBOX;
						} else {
							modType = ModuleType.EIGHTEEN_M;
						}

						ModuleDataTag tag = new ModuleDataTag(modID, data[7],
								data[8], data[6], testResults, modType);
						moduleData.put(tag.modID, tag);

						eventDataObject = tag;
						Log.i(TAG, "*****ModID: " + modID + " Channel: "
								+ data[7] + " *****");

						break;

					case CobraCommands.VCOM_FIREDCUES_DATA:
						if (data.length < CobraCommands.VCOM_FIREDCUES_DATA_BYTES
								// || appClass.getFlagScriptsUpate() ==
								// CobraFlags.READY_TO_CALL_SCRIPTS_DATA
								|| appClass.getFlagScriptsUpate() == CobraFlags.SCRIPT_DATA_IS_CALLING) {
							Log.e(TAG,
									"VCOM_FIREDCUES_DATA: not enough data received");
							continue;
						}

						// appClass.setScriptsOnDevice("VCOM_FIREDCUES_DATA",
						// data);
						channelID = data[1];
						scriptCues = 0;

						if (channelID == -1 || channelID == 255) {
							appClass.setFlagFireCuesUpate(CobraFlags.FIRECUE_DATA_READY_TO_ACKNOWLEDGE);
							AckOwnFlagStatusChange();
							appClass.writeLog("Flag Firecues Update State FIRECUE_DATA_READY_TO_ACKNOWLEDGE : "
									+ CobraFlags.FIRECUE_DATA_READY_TO_ACKNOWLEDGE);
						} else {
							appClass.setNextFirecueChannel(channelID);
							appClass.writeLog("Flag Firecues Update State READY_TO_CALL_NEXT_FIRECUE_DATA : "
									+ CobraFlags.READY_TO_CALL_NEXT_FIRECUE_DATA);
							appClass.setFlagFireCuesUpate(CobraFlags.READY_TO_CALL_NEXT_FIRECUE_DATA);
							AckOwnFlagStatusChange();
						}

						if (channelData.containsKey(channelID)) {
							scriptCues = channelData.get(channelID).scriptCues;
						}
						channelData
								.put(channelID,
										new ChannelDataTag(
												channelID,
												((data[2] & 0xFF)
														| (data[3] & 0xFF) << 8 | (data[4] & 0xFF) << 16),
												scriptCues));
						eventType = Cobra.EVENT_TYPE_CHANNEL_DATA_CHANGE;
						broadcaseEventType = appClass.RECIEVER_BUCKETFIRING_BUCKET;
						eventDataObject = channelData.get(channelID);
						Log.i(TAG,
								"*****ChannelID: "
										+ channelID
										+ " FIREDCUES: "
										+ Long.toBinaryString(channelData
												.get(channelID).firedCues)
										+ "*****");
						break;

					case CobraCommands.VCOM_SCRIPTCUES_DATA:
						if (data.length < CobraCommands.VCOM_SCRIPTCUES_DATA_BYTES) {
							Log.e(TAG,
									"VCOM_SCRIPTCUES_DATA: not enough data received");
							continue;
						}

						// appClass.setScriptsOnDevice("VCOM_SCRIPTCUES_DATA",data);
						channelID = data[1];
						firedCues = 0;
						if (channelData.containsKey(channelID)) {
							firedCues = channelData.get(channelID).firedCues;
						}
						channelData
								.put(channelID,
										new ChannelDataTag(
												channelID,
												firedCues,
												((data[2] & 0xFF)
														| (data[3] & 0xFF) << 8 | (data[4] & 0xFF) << 16)));
						eventType = Cobra.EVENT_TYPE_CHANNEL_DATA_CHANGE;
						eventDataObject = channelData.get(channelID);
						Log.i(TAG,
								"*****ChannelID: "
										+ channelID
										+ " SCRIPTCUES: "
										+ Long.toBinaryString(channelData
												.get(channelID).scriptCues)
										+ "*****");
						break;

					case CobraCommands.VCOM_SCRIPT_DATA:
						if (data.length < CobraCommands.VCOM_SCRIPT_DATA_BYTES) {
							Log.e(TAG,
									"VCOM_SCRIPT_DATA: not enough data received");
							continue;
						}

						globV.setIsScriptDataCalled(true);
						scriptIndex = data[1];

						long scriptLength = (data[8] & 0xFF) << 24
								| (data[9] & 0xFF) << 16
								| (data[10] & 0xFF) << 8 | (data[11] & 0xFF);

						ScriptIndexTag ScriptTag = new ScriptIndexTag(
								(byte) scriptIndex, data[2], data[3], data[4],
								data[5], (data[6] & 0xFF) << 8
										| (data[7] & 0xFF), scriptLength,
								// !(data[8] == 0),
								// parseByteString(Arrays.copyOfRange(data,
								// 8,data.length), 0),
								// parseByteString(Arrays.copyOfRange(data,
								// 8,data.length), 1));
								parseByteString(Arrays.copyOfRange(data, 11,
										data.length), 0), parseByteString(
										Arrays.copyOfRange(data, 11,
												data.length), 1));

						String str = "";
						for (int s = 0; s < data.length; s++) {
							str = str + data[s];
						}

						appClass.setScriptsLogg("scriptIndexData_List INITIALLIZED"
								+ str);

						Boolean ClearPreviousList = false;
						if (ScriptTag.scriptID == 0)
							ClearPreviousList = true;

						globV.addScriptName(ScriptTag.scriptName,
								ClearPreviousList);

						int scriptSize = globV.getListScriptName().size();
						if (ScriptTag.scriptName.equals("")) {
							ScriptTag.scriptName = "" + (scriptSize);
						}
						setScriptIndexData_List(ScriptTag);
						appClass.setSerialLogOnDevice("script Data added in scriptIndexData_List");
						break;

					// case CobraCommands.VCOM_REQ_ALL_EVENT_DATA:
					// if (data.length <
					// CobraCommands.VCOM_REQ_ALL_EVENT_DATA_BYTES) {
					// Log.e(TAG,
					// "VCOM_REQ_ALL_EVENT_DATA: not enough data received");
					// for (int i = 0; i < data.length; i++)
					// Log.e(TAG, Integer.toString(data[i]));
					//
					// continue;
					// }
					//
					// //
					// appClass.setScriptsOnDevice("VCOM_REQ_ALL_EVENT_DATA",data);
					// int eventid = data[3] & 0xFF << 16
					// | (data[2] & 0xFF) << 24;
					// EventDataTag eventTag = new EventDataTag(data[1],
					// (data[2] & 0xFF) << 8 | (data[3] & 0xFF),
					// (data[4] & 0xFF) << 24 | (data[5] & 0xFF) << 16
					// | (data[6] & 0xFF) << 8
					// | (data[7] & 0xFF), data[8],
					// (data[9] & 0xFF) << 24
					// | (data[10] & 0xFF) << 16
					// | (data[11] & 0xFF) << 8
					// | (data[12] & 0xFF), parseByteString(
					// Arrays.copyOfRange(data, 13,
					// data.length), 0));
					// appClass.setSerialLogOnDevice("saving event data");
					//
					// appClass.setScriptsLogOnDevice("Recieved: "
					// + eventTag.eventIndex);
					// saveEvent(eventTag);
					// Log.i(TAG, "*****Event: Script-" + eventTag.scriptIndex
					// + " Event-" + eventTag.eventIndex + " name-"
					// + eventTag.eventDescription + " *****");
					// break;
					//
					case CobraCommands.VCOM_SCRIPT_PING:
						if (data.length < CobraCommands.VCOM_SCRIPT_PING_BYTES
						// || appClass.getFlagScriptsUpate() ==
						// CobraFlags.READY_TO_CALL_SCRIPTS_DATA
								|| appClass.getFlagScriptsUpate() == CobraFlags.SCRIPT_DATA_IS_CALLING) {
							Log.e(TAG,
									"VCOM_SCRIPT_PING: not enough data received");
							continue;
						}

						ScriptPingTag pingTag = new ScriptPingTag(data[4],

						(data[1] & 0xFF) << 24 | (data[2] & 0xFF) << 16
								| (data[3] & 0xFF) << 8 | (data[4] & 0xFF),
								data[5] & 0xFF, (data[6] & 0xFF) << 8
										| (data[7] & 0xFF));
						eventType = Cobra.EVENT_TYPE_SCRIPT_PING;
						eventDataObject = pingTag;
						break;

					case CobraCommands.VCOM_CLEAR_MODULES:
						if (data.length < CobraCommands.VCOM_CLEAR_MODULES_BYTES
								// || appClass.getFlagScriptsUpate() ==
								// CobraFlags.READY_TO_CALL_SCRIPTS_DATA
								|| appClass.getFlagScriptsUpate() == CobraFlags.SCRIPT_DATA_IS_CALLING) {
							Log.e(TAG,
									"VCOM_CLEAR_MODULES: not enough data received");
							continue;
						}

						moduleData.clear();
						eventType = Cobra.EVENT_CLEAR_MODULES;
						break;

					default:
						Log.i(TAG, "Unknown Command: " + (opCode & 0xFF));
						break;
					}

					if (eventType > 0) {
						// fireSerialInputEvent(new Cobra.CobraEvent(eventType,
						// eventDataObject));
						fireModuleListener(new Cobra.CobraEvent(eventType,
								eventDataObject));

						// CobraEvent2 eventData=new
						// Cobra.CobraEvent2(eventType,
						// eventDataObject);

						// fireShowControlListener(new
						// Cobra.CobraEvent(eventType,
						// eventDataObject));

						if (eventType == Cobra.EVENT_TYPE_SCRIPT_PING) {

							// ArrayList<CobraEvent2> parcelableExtra = new
							// ArrayList<CobraEvent2>();
							// parcelableExtra.add(eventData);

							Intent intent = new Intent(
									appClass.RECIEVER_SHOW_CONTROLS);
							int index = 0;
							long elapsedTime = 0;
							int scriptIndex1 = 0;
							int eventIndex = 0;
							ScriptPingTag pingTag = (ScriptPingTag) eventDataObject;

							index = pingTag.index;
							elapsedTime = pingTag.elapsedTime;
							eventIndex = pingTag.eventIndex;
							scriptIndex1 = pingTag.scriptIndex;

							intent.putExtra("index", index);
							intent.putExtra("elapsedTime", elapsedTime);
							intent.putExtra("scriptIndex", scriptIndex1);
							intent.putExtra("eventIndex", eventIndex);
							activity.sendBroadcast(intent);

						}

						if (eventType == Cobra.EVENT_TYPE_CHANNEL_DATA_CHANGE) {
							Intent intent = new Intent(broadcaseEventType);
							intent.putExtra(appClass.TAG_EVENT, eventType);

							ChannelDataTag pingTag = (ChannelDataTag) eventDataObject;

							intent.putExtra("channel", "" + pingTag.channel);
							intent.putExtra("firedCues", "" + pingTag.firedCues);
							intent.putExtra("scriptCues", ""
									+ pingTag.scriptCues);
							// intent.putExtra("sampleObject", ii);
							activity.sendBroadcast(intent);

							fireBucketFiringListener(new Cobra.CobraEvent(
									eventType, eventDataObject));
						}

						if (broadcaseEventType != null) {
							Intent intent = new Intent(broadcaseEventType);
							intent.putExtra(appClass.TAG_EVENT, eventType);
							intent.putExtra("battery", battery);
							intent.putExtra("channel", Channel);
							intent.putExtra("mode", mode);
							// intent.putExtra("sampleObject", ii);
							activity.sendBroadcast(intent);
						}
					}
				}
			} catch (final Exception e) {
				try {
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							// if (e != null)
							// Toast.makeText(context, e.getMessage(),
							// Toast.LENGTH_LONG).show();
						}
					});

				} catch (final Exception e1) {

				}
			}
		}
		/**
		 * Acquire read token, which is released when the serialDriver reads
		 * data.
		 */
		// private void acquireReadToken() {
		// try {
		// if (isConnected()
		// && semMessageAvailable.tryAcquire(100000,
		// TimeUnit.MILLISECONDS) == false) {
		// Log.e(TAG,
		// "VCOM silent for >5 seconds. Closing communications.");
		// SerialDriver.close();
		// messageThread.interrupt();
		// fireSerialInputEvent(new CobraEvent(
		// EVENT_TYPE_DISCONNECTED, null));
		// return;
		// }
		// } catch (InterruptedException e) {
		// Log.e(TAG, e.getMessage());
		// }
		// }
	}

	private long getScriptLength(byte[] data) {
		return ByteBuffer.wrap(data).order(ByteOrder.BIG_ENDIAN).getLong();
	}

	private Boolean AppDisconnected = false;
	private BroadcastReceiver mUsbReceiver2 = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			try {
				if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
					// CloseApp();

					showDialogDisconnectDevice();
					closeDevice();
				}
			} catch (Exception e) {

			}
		}
	};

	public void CloseApp() {
		try {
			if (activity != null)
				appClass.SaveLastLoggedTime(activity);
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			// PersistentHeader.KeepExecute = false;
			builder.setTitle("Disconnected");
			builder.setCancelable(false);
			builder.setMessage("The 18R2 is disconnected. Please press OK, wait 10 seconds, and re-connect the 18R2.");
			builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							appClass.IsAppForcedClosed = true;
							activity.finish();
						}
					});
			if (MainActivity.IsAppActive)
				builder.show();
			else {
				appClass.IsAppForcedClosed = true;
				activity.finish();
			}
		} catch (Exception e) {

		}
	}

	/**
	 * Parses the bytes and returns the strNum'th string, i.e. if bytes array
	 * contains multiple strings, strNum is used to specify the i'th index
	 * string to return.
	 * 
	 * @param byteString
	 *            the byte string
	 * @param strNum
	 *            the str num
	 * @return the string
	 */
	private String parseByteString(int[] byteString, int strNum) {
		byte[] bytes = veriyingDesc(byteString);// new byte[byteString.length];
		for (int i = 0; i < bytes.length; i++)
			bytes[i] = (byte) byteString[i];

		String[] strings = (new String(bytes)).split("\0");

		if (strings != null && strNum < strings.length)
			return strings[strNum];

		return "";
	}

	private byte[] veriyingDesc(int[] descArray) {
		ArrayList<Integer> tempList = new ArrayList<Integer>();
		for (int i = 0; i < descArray.length; i++) {
			if (descArray[i] <= 127) {
				tempList.add(descArray[i]);
			}
		}

		byte[] temp = new byte[tempList.size()];
		for (int i = 0; i < temp.length; i++) {
			int value = tempList.get(i);
			temp[i] = (byte) value;
		}
		return temp;
	}

	// public static interface IScriptEventListener extends EventListener {
	//
	// public void onScriptLoading(int script_id, int event_id);
	// }
	//
	// IScriptEventListener scriptListener;
	//
	// public void addScriptListener(IScriptEventListener listener) {
	// this.scriptListener = listener;
	// }

	private ScriptListener listener;

	public void addScriptListener(ScriptListener listener) {
		this.listener = listener;
	}

	/**
	 * Save events to the global eventData.
	 * 
	 * @param tag
	 *            the tag
	 */

	public static int counter = 0;

	// private int totalScripts = -1;

	private void saveEvent(EventDataTag tag) {
		counter++;

		if (tag.eventIndex != -1 && tag.eventIndex < 6000) {

			try {
				Intent intent = new Intent(
						appClass.DIALOG_SCRIPT_LISTENER_RECEIVER);
				intent.putExtra("stauts", 1);
				intent.putExtra(
						"msg",
						"Loading Scripts : "
								+ (tag.scriptIndex + 1)
								+ " of "
								+ globV.getScriptList().size()
								+ " \nLoading Events: "
								+ (tag.eventIndex + 1)
								+ " of "
								+ +getScriptIndexData_List().get(
										tag.scriptIndex).numEvents);

				activity.sendBroadcast(intent);
			} catch (Exception e) {
			}
			if (eventData == null)
				eventData = new ConcurrentSkipListMap<Integer, SparseArray<EventDataTag>>();

			if (eventData.containsKey(tag.scriptIndex)) {
				eventData.get(tag.scriptIndex).append(tag.eventIndex, tag);
			} else {
				SparseArray<EventDataTag> tags = new SparseArray<EventDataTag>();
				tags.append(tag.eventIndex, tag);
				eventData.put(tag.scriptIndex, tags);
			}

			// appClass.setPlayScriptLog("Channel: " + tag.channel +
			// ", CueList: "
			// + tag.cueList + ", ScriptIndex: " + tag.scriptIndex
			// + ", EventIndex: " + tag.eventIndex + ", TimeIndex: "
			// + tag.timeIndex + ", ShiftedTimeIndex: "
			// + tag.shiftedTimeIndex + ", Desc : " + tag.eventDescription);

			getNEXT_EVENT_DATA(tag.scriptIndex, tag.eventIndex);

		} else if ((tag.scriptIndex + 1) < globV.getScriptList().size()) {
			getNEXT_EVENT_DATA(tag.scriptIndex + 1, -1);
		} else if (getScriptIndexData_List().size() > 0) {
			MainActivity.scriptStatus = 1;
			appClass.setFlagScriptsUpate(CobraFlags.ALL_SCRIPT_DATA_IS_FINISHED);
		} else {
			MainActivity.scriptStatus = 0;
			appClass.setFlagScriptsUpate(CobraFlags.ALL_SCRIPT_DATA_IS_FINISHED);
		}

	}

	public String VerifyScriptsData() {
		try {
			if (getScriptsData() != null && getEventsData() != null) {
				ArrayList<ScriptIndexTag> scripts = getScriptsData();
				SparseArray<SparseArray<EventDataTag>> events = getEventsData();

				for (int i = 0; i < scripts.size(); i++) {
					ScriptIndexTag script = scripts.get(i);
					int numEvents = script.numEvents;
					SparseArray<EventDataTag> event = events.get(i);
					EventDataTag eventTag = event.get(numEvents - 1);
					long timeIndex = eventTag.timeIndex;
				}
				return "true";
			}
			// if(getScriptsData() == null && getEventsData() == null){
			// return "no_scripts";
			// }
			return "false";
		} catch (Exception e) {
			return "" + e.getMessage();
		}
	}

	public void clearScriptObjects() {
		if (getScriptIndexData_List() != null)
			getScriptIndexData_List().clear();
		scriptIndexDataList = null;
		appClass.setSerialLogOnDevice("scriptIndexData_List is NULL");
		if (eventData != null)
			eventData.clear();
		eventData = null;
		counter = 0;
	}

	public void setScriptIndexData_List(ScriptIndexTag tag) {
		if (scriptIndexDataList == null) {
			scriptIndexDataList = new ArrayList<ScriptIndexTag>();
			globV.clearScripts();
		}
		if (tag.scriptID != -1) {
			scriptIndexDataList.add(tag);

			ScriptEvents script = new ScriptEvents();
			script.setScriptIndex(tag.scriptID);
			script.setNumEvents(tag.numEvents);
			globV.setScripts(script);
			ScriptsAvailable = true;

			appClass.setNextScriptId(tag.scriptID);
			getNEXT_SCRIPT_DATA(appClass.getNextScriptId());

		} else if (scriptIndexDataList.size() > 0) {
			getNEXT_EVENT_DATA(globV.getScriptList().get(0).getScriptIndex(),
					-1);
		} else {
			ScriptsAvailable = false;
			appClass.setFlagScriptsUpate(CobraFlags.ALL_SCRIPT_DATA_IS_FINISHED);
		}
	}

	public static Boolean ScriptsAvailable = false;

	public ArrayList<ScriptIndexTag> getScriptIndexData_List() {
		return scriptIndexDataList;
	}

	/**
	 * The listener interface for receiving serialEvent events. The class that
	 * is interested in processing a serialEvent event implements this
	 * interface, and the object created with that class is registered with a
	 * component using the component's
	 * <code>addSerialEventListener<code> method. When
	 * the serialEvent event occurs, that object's appropriate
	 * method is invoked.
	 * 
	 * @see SerialEventEvent
	 */
	byte[] LastRecievedArray = null;

	public class SerialEventListener implements ISerialPortWListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.cobra.api.SerialDriver.ISerialPortWListener#onSerialRead(com.
		 * cobra.api.SerialDriver.SerialPortWEvent)
		 */
		@Override
		public void onSerialRead(SerialPortWEvent event) {
			// write this to third file

			byte[] data = event.getSerialData();
			int[] msg = new int[data.length];
			// String message = "";
			for (int i = 0; i < data.length; i++) {
				msg[i] = data[i] & 0xFF;
				// message += "  " + msg[i];// + Integer.toHexString(msg[i]);
			}

			GenerateLogMessage(msg);
			// AckStatusChange(false, 1, 0, 0);
			// if(msg[0]==101){
			// int i=0;
			// i++;
			// }
			MessageDataTag msgTag = new MessageDataTag(msg);
			if (msg[0] == CobraCommands.VCOM_REQ_ALL_EVENT_DATA) {
				String message = "";
				for (int i = 0; i < msg.length; i++) {
					message += "  " + msg[i];
				}
				// appClass.setPlayScriptLog("data : " + message);
				StoreScritpsInfo(msgTag);
				semMessageAvailable.release();
			} else {
				dataBuffer.add(msgTag);
				semMessageAvailable.release();
			}
			// dataBuffer.add(new MessageDataTag(msg));

		}
	}

	IntBuffer buffer;

	private void StoreScritpsInfo(MessageDataTag Tagdata) {
		int totalMsgs, currentMsgNum;
		int[] data = null;

		MessageDataTag tempData = Tagdata;

		currentMsgNum = tempData.msgNumber;
		totalMsgs = tempData.totalMsgs;

		if (tempData.msgNumber == 1) {
			buffer = IntBuffer.allocate((totalMsgs + 1)
					* CobraCommands.MAX_MESSAGE_BYTES);
			buffer.put(tempData.commandCode);
		}

		if (currentMsgNum < totalMsgs) {

			buffer.put(tempData.data);

		} else if (currentMsgNum == totalMsgs) {
			buffer.put(tempData.data);

			data = new int[buffer.position()];
			System.arraycopy(buffer.array(), 0, data, 0, buffer.position());

			buffer.clear();
			buffer.compact();
			Log.i(TAG, String.format("READ_MSG CLN -- R:%d Msg: %s",
					data.length, SerialDriver.getHex(data)));

			int opCode = data[0];

			switch (opCode) {
			case CobraCommands.VCOM_REQ_ALL_EVENT_DATA:
				if (data.length < CobraCommands.VCOM_REQ_ALL_EVENT_DATA_BYTES) {
					Log.e(TAG,
							"VCOM_REQ_ALL_EVENT_DATA: not enough data received");
					for (int i = 0; i < data.length; i++)
						Log.e(TAG, Integer.toString(data[i]));

					// continue;
				}

				int eventid = (data[2] & 0xFF) << 8 | (data[3] & 0xFF);

				// Problem is here for AN-188

				long cueList = (data[9] & 0xFF) << 24 | (data[10] & 0xFF) << 16
						| (data[11] & 0xFF) << 8 | (data[12] & 0xFF);

				updateModuleLightStatus(data[8], eventid, cueList);

				int scriptIndex = data[1];
				int event = (data[2] & 0xFF) << 8 | (data[3] & 0xFF);
				long timeIndex = (data[4] & 0xFF) << 24
						| (data[5] & 0xFF) << 16 | (data[6] & 0xFF) << 8
						| (data[7] & 0xFF);
				int channel = data[8];
				String eventDesc = parseByteString(
						Arrays.copyOfRange(data, 13, data.length), 0);

				ChannelCues channelCues = new ChannelCues();
				channelCues.setCueStatus(cueList);
				globV.setChannelCues(channel, channelCues);

				EventDataTag eventTag = new EventDataTag(scriptIndex, event,
						timeIndex, channel, cueList, eventDesc);

				// if (channel == 2) {
				// appClass.setAN188Log("EventId=" + eventid + ",cueList: "
				// + cueList);
				//
				// appClass.setAN188Log("EventId=" + eventid + ",Byte1: "
				// + (data[9] & 0xFF));
				//
				// appClass.setAN188Log("EventId=" + eventid + ",Byte2: "
				// + (data[10] & 0xFF));
				//
				// appClass.setAN188Log("EventId=" + eventid + ",Byte3: "
				// + (data[11] & 0xFF));
				//
				// appClass.setAN188Log("EventId=" + eventid + ",Byte4: "
				// + (data[12] & 0xFF));
				// appClass.setAN188Log("EventId=" + eventid
				// + ",==========================, ");
				// }
				saveEvent(eventTag);

				// String msg = "cueList = " + cueList + " [ ";
				// for (int i = 0; i < data.length; i++) {
				// msg += data[i] + " , ";
				// }
				//
				// appClass.writeLog(msg);
				break;
			}

		}

	}

	private void GenerateLogMessage(int[] msg) {
		String message = "";
		for (int i = 0; i < msg.length; i++) {
			message += "  " + msg[i];// + Integer.toHexString(msg[i]);
		}
		Spanned spanned_message;
		if (appClass.IsCommandPrompt_DECIMAL)
			spanned_message = Html.fromHtml("<b>--Reading:</b><br>" + message
					+ "<br>");
		else
			spanned_message = Html.fromHtml("<b>--Reading:</b><br>"
					+ SerialDriver.getHex(msg) + "<br>");

		fireCommandEventLister(spanned_message);

	}

	// public void SerialEventListener(SerialPortWEvent event) {
	// byte[] data = event.getSerialData();
	// int[] msg = new int[data.length];
	//
	// for (int i = 0; i < data.length; i++)
	// msg[i] = data[i] & 0xFF;
	//
	// dataBuffer.add(new MessageDataTag(msg));
	// semMessageAvailable.release();
	// }

	public void setError(String eRROR_POWER_CYCLE2) {
		this.sError = eRROR_POWER_CYCLE2;
	}

	public String getError() {
		return this.sError;
	}

	public class ConnectCobra extends AsyncTask<Void, Void, Void> {

		public ConnectCobra() {
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				asyncNotify.get(CobraCommands.VCOM_REQ_ALL_DATA_REFRESH & 0xFF)
						.await(10, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}

	public void showDialogDisconnectDevice() {

		// if (MainActivity.connect_popup == true) {
		appClass.setAN106NewLog("Devices Disconnect");

		FragmentTransaction ft = ((FragmentActivity) activity)
				.getSupportFragmentManager().beginTransaction();

		Fragment prev = ((FragmentActivity) activity)
				.getSupportFragmentManager().findFragmentByTag("dialog");

		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		// Create and show the dialog.
		DialogFragment newFragment = DialogDisconnectDevice.newInstance(1, "",
				1);
		newFragment.show(ft, "dialog");

	}
	// }
}
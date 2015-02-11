package com.cobra;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.text.format.DateFormat;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cobra.api.Cobra;
import com.cobra.api.CobraDataTags.ModuleLightsHandler;
import com.cobra.api.SerialDriver;
import com.cobra.classes.ChannelCues;
import com.cobra.classes.ScriptEvents;
import com.cobra.classes.Cues.ChannelCuesData;
import com.cobra.classes.Cues.ScriptData;
import com.cobra.dialogs.DialogContinuity.onCueDailogDismiss;
import com.cobra.interfaces.DemoModeListener;
import com.cobra.interfaces.OnEventChange;
import com.cobra.interfaces.onFilterCueModule;
import com.cobra.interfaces.onFilterMode;
import com.cobra.module.interfaces.OnModuleInformationUpdate;
import com.cobra.showcontrol.interfaces.OnWrongScriptListener;
import com.cobra.view.bucketfiring.ModuleViews;
import com.cobra.views.modulelist.ModuleListItem;
import com.cobra.views.modulelist.ModuleRow;
import com.cobra.views.modulelist.ModuleUIRow;
import com.parse.ParseObject;

@ReportsCrashes(formKey = "", mailTo = "arslaanulhaq@gmail.com")
public class appClass extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		ACRA.init(this);

		singleton = this;

	}

	private UsbDeviceConnection connection;
	private UsbDevice device;

	
	private HashMap<Integer, ChannelCues> channelCues;
	public static ModuleLightsHandler[] moduleLightsHandlerArray;
	private LinearLayout linear_listview;
	private Boolean IsScriptDataCalled = false;
	private int LastEventCalled = -1;
	private int LastScriptCalled = -1;
	private ArrayList<String> ListScriptName;

	public static void writeIntoLog(ArrayList<String> cueStringList, String msg) {

		if (cueStringList != null) {
			appClass.setAN188Log("########START#### " + msg);
			for (int i = 0; i < cueStringList.size(); i++) {
				appClass.setAN188Log("|| " + cueStringList.get(i));
			}
			appClass.setAN188Log("########END########");
		} else
			appClass.setAN188Log("######## ARRAY_NULL ########");

	}

	public Boolean getChannelCueStatus(int channel, int cue) {
		if (channelCues != null) {
			ChannelCues channelCue = channelCues.get(channel);
			if (channelCue != null) {
				return channelCue.getCueStatus(cue);
			}
		}
		return false;
	}

	public void setChannelCues(int channel, ChannelCues cues) {
		if (this.channelCues == null)
			this.channelCues = new HashMap<Integer, ChannelCues>();
		this.channelCues.put(channel, cues);
	}

	public ArrayList<String> getListScriptName() {
		return ListScriptName;
	}

	public void addScriptName(String ScriptName, Boolean clearPreviousList) {
		if (ListScriptName == null || clearPreviousList)
			ListScriptName = new ArrayList<String>();
		if (!ScriptName.equals(""))
			ListScriptName.add("" + (ListScriptName.size() + 1));
		else
			ListScriptName.add("" + (ListScriptName.size() + 1));
	}

	public int getLastEventCalled() {
		return LastEventCalled;
	}

	public void setLastEventCalled(int lastEventCalled) {
		LastEventCalled = lastEventCalled;
	}

	public int getLastScriptCalled() {
		return LastScriptCalled;
	}

	public void setLastScriptCalled(int lastScriptCalled) {
		LastScriptCalled = lastScriptCalled;
	}

	public Boolean getIsScriptDataCalled() {
		return IsScriptDataCalled;
	}

	public void setIsScriptDataCalled(Boolean script_data_Called) {
		IsScriptDataCalled = script_data_Called;
	}

	public void setLinearListView(LinearLayout listview) {
		this.linear_listview = listview;

	}

	// public LinearLayout getLinearListView() {
	// return this.linear_listview;
	// }

	public Boolean ClearLinearListView() {
		if (this.linear_listview != null) {
			this.linear_listview.removeAllViews();
			return true;
		} else
			return false;
	}

	public ArrayList<ModuleUIRow> ModuleUI_List;
	/*
	 * Recievers
	 */

	private Cobra cobra;

	public Cobra getCobra() {
		return cobra;
	}

	public void setCobra(Cobra cobra) {
		this.cobra = cobra;
	}

	private int CurrentChannel = -1;
	public static long last_module_called = -1;

	public int getCurrentChannel() {
		return CurrentChannel;
	}

	public void setCurrentChannel(int currentChannel) {
		CurrentChannel = currentChannel;
	}

	public static final String TAG_EVENT = "event";
	// public static final String RECIEVER_ACKNOWLEDGEMENT =
	// "com.cobra.acknowledgement.reciever";
	public static final String RECIEVER_BUCKETFIRING_BUCKET = "com.cobra.view.bucketfiring.bucket";
	public static final String RECIEVER_PERSISTENT_HEADER = "com.cobra.view.persistent.header";
	public static final String RECIEVER_MODULE_LIST = "com.cobra.view.module.list";
	public static final String RECIEVER_ADAPTER_MODULE_LIST = "com.cobra.view.modulelist.modulelist";
	public static final String RECIEVER_SHOW_CONTROLS = "com.cobra.view.showcontrols";

	public static Boolean IsRefreshPressed = false;
	// public static final int EVENT_TYPE_SCRIPT_HANDLER = 123;
	// public static final int EVENT_TYPE_SCRIPT_TIMER = 456;
	/*
	 * "0" if we recieved acknowledgement "1" if we answered acknowledgement and
	 * requested for data "2" if we have recieved data and stopped sending any
	 * acknowledgement
	 */
	private static int FlagDeviceUpate = 0;
	private static int FlagModuleUpate = 0;
	private static int FlagModulePreviousState = 0;
	private static int FlagFireCuesUpate = 0;
	private static int FlagFirmwareUpate = 0;
	private static int FlagScriptsUpate = 0;

	// public static Boolean IsModuleNegative = false;
	// public static int FlagGetDeviceInfo=0;

	private static int NextModuleId = -1;
	private static int LastModuleId = -2;
	private static int NextFirecueChannel = -1;

	// public static Boolean RetrieveModule=true;
	public static final int EVENT_MODULE_FLAG_DATA_CHANGE = -5;

	public static Boolean IsDeviceDetected = true;

	public static Boolean IsConnectionEstablished = false;
	// public static Boolean ListenScriptTimer = false;

	public static Boolean IsAppForcedClosed = true;
	public static Boolean IsAppInConnectedState = false;

	public static Activity activity = null;
	public static OnEventChange onEventChange = null;
	public TextView CommandPrompt;
	public static String TitleText = "Cobra Firing Systems";
	public static Boolean UpdateIcon = false;

	private ArrayList<ScriptEvents> scriptList;

	public ArrayList<ScriptEvents> getScriptList() {
		return scriptList;
	}

	public void setScripts(ScriptEvents scripts) {
		if (this.scriptList == null)
			this.scriptList = new ArrayList<ScriptEvents>();
		this.scriptList.add(scripts);
	}

	public void clearScripts() {
		if (this.scriptList != null) {
			this.scriptList.clear();
			this.scriptList = null;
		}
	}

	// public static Boolean ForceStopModuleDataCalling = false;
	// public static ArrayList<Modules> moduleList = new ArrayList<Modules>();
	private HashMap<Integer, ModuleViews> moduleList = new HashMap<Integer, ModuleViews>();

	@SuppressLint("UseSparseArrays")
	public HashMap<Integer, ModuleViews> getModuleList() {
		return moduleList;
	}

	public void setModuleList(HashMap<Integer, ModuleViews> moduleList) {
		this.moduleList = moduleList;
	}

	public SerialDriver serialPort;
	private appClass singleton;
	private ArrayList<ScriptData> scriptDataList;
	private ArrayList<ChannelCuesData> channelCuesData;
	public static Boolean IsCommandPrompt_DECIMAL = true;

	public static int SerialPortStatus = -1;
	private static ParseObject testObject;
	public static final String SERIAL_READER_RECEIVER = "com.cobra.broadcast.receiver.action.Reader";
	public static String TesterName = "-";

	public static final String DIALOG_SCRIPT_LISTENER_RECEIVER = "com.cobra.dialog.DialogScriptListener";

	public ArrayList<ModuleUIRow> getModuleUI_List() {
		return ModuleUI_List;
	}

	public void removeModuleUI_List() {
		ModuleUI_List = null;
	}

	public static Boolean IsNewModuleId() {
		if (getNextModuleId() != getLastModuleId())
			return true;
		else
			return false;
	}

	public void setModuleUI_List(ModuleUIRow moduleUI) {
		if (ModuleUI_List == null)
			ModuleUI_List = new ArrayList<ModuleUIRow>();

		for (int i = 0; i < ModuleUI_List.size(); i++) {
			if (ModuleUI_List.get(i).getModuleid() == moduleUI.getModuleid()) {
				ModuleUI_List.get(i).setContent(moduleUI.getContent());
				ModuleUI_List.get(i).setCuesParent(moduleUI.getCuesParent());
				ModuleUI_List.get(i)
						.setParentLayout(moduleUI.getParentLayout());
				ModuleUI_List.get(i).setPosition(moduleUI.getPosition());
				ModuleUI_List.get(i).setTvAddress(moduleUI.getTvAddress());
				ModuleUI_List.get(i).setTvBattery1(moduleUI.getTvBattery1());
				ModuleUI_List.get(i).setTvBattery2(moduleUI.getTvBattery2());
				ModuleUI_List.get(i).setTvChannel(moduleUI.getTvChannel());
				ModuleUI_List.get(i).setTvCues(moduleUI.getTvCues());
				ModuleUI_List.get(i).setTvDevice(moduleUI.getTvDevice());
				ModuleUI_List.get(i).setTvKeyPosition(
						moduleUI.getTvKeyPosition());
				ModuleUI_List.get(i).setTvMode(moduleUI.getTvMode());
				ModuleUI_List.get(i).setTvSignal(moduleUI.getTvSignal());
				ModuleUI_List.get(i).setTvDevice(moduleUI.getTvDevice());
				return;
			}
		}
		ModuleUI_List.add(moduleUI);

	}

	public static String[] names_Tester = { "Select Tester", "Scott", "Arslan",
			"Angelo" };
	private static int NextScriptId = -1;

	public static String ModuleFlagStatus(int Flag) {
		if (Flag == 0) {
			return "All Modules Data Recieved";
		} else if (Flag == 1) {
			return "Requesting Modules From 18R2";
		} else
			return "Acknowledged Module Data Change";
	}

	public static int getNextScriptId() {
		return NextScriptId;
	}

	public static void setNextScriptId(int nextScriptId) {
		NextScriptId = nextScriptId;
	}

	public static int getNextFirecueChannel() {
		return NextFirecueChannel;
	}

	public static void setNextFirecueChannel(int nextFirecueChannel) {
		NextFirecueChannel = nextFirecueChannel;
	}

	public static int getNextModuleId() {
		synchronized (itemLock) {
			return NextModuleId;
		}
	}

	public static int getLastModuleId() {
		synchronized (itemLock) {
			return LastModuleId;
		}
	}

	public static void setLastModuleId() {
		synchronized (itemLock) {
			LastModuleId = getNextModuleId();
		}
	}

	private static final Object itemLock = new Object();

	public static void setNextModuleId(int nextModuleId) {
		synchronized (itemLock) {
			NextModuleId = nextModuleId;
		}
	}

	public static int getFlagScriptsUpate() {
		return FlagScriptsUpate;
	}

	public static void setFlagScriptsUpate(int flagScriptsUpate) {
		FlagScriptsUpate = flagScriptsUpate;
	}

	public static int getFlagFirmwareUpate() {
		return FlagFirmwareUpate;
	}

	public static void setFlagFirmwareUpate(int flagFirmwareUpate) {
		FlagFirmwareUpate = flagFirmwareUpate;
	}

	public static int getFlagDeviceUpate() {
		return FlagDeviceUpate;
	}

	public static void setFlagDeviceUpate(int flagDeviceUpate) {
		FlagDeviceUpate = flagDeviceUpate;
	}

	public static int getFlagModuleUpate() {
		return FlagModuleUpate;
	}

	public static void setFlagModuleUpate(int flagModuleUpate) {
		// appClass.setAN106NewLog("In setFlagModuleUpate: " + flagModuleUpate);
		FlagModuleUpate = flagModuleUpate;
	}

	public static int getFlagModulePreviousState() {
		return FlagModulePreviousState;
	}

	public static void setFlagModulePreviousState(int flagModulePreviousState) {
		FlagModulePreviousState = flagModulePreviousState;
	}

	public static int getFlagFireCuesUpate() {
		return FlagFireCuesUpate;
	}

	public static void setFlagFireCuesUpate(int flagFireCuesUpate) {
		FlagFireCuesUpate = flagFireCuesUpate;
	}

	public static boolean isTablet(Context context) {
		boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
		boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
		return (xlarge || large);
	}

	public appClass getInstance() {
		singleton = this;
		return singleton;
	}

	public static String getDeviceName() {
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		if (model.startsWith(manufacturer)) {
			return capitalize(model);
		} else {
			return capitalize(manufacturer) + " " + model;
		}
	}

	private static String capitalize(String s) {
		if (s == null || s.length() == 0) {
			return "";
		}
		char first = s.charAt(0);
		if (Character.isUpperCase(first)) {
			return s;
		} else {
			return Character.toUpperCase(first) + s.substring(1);
		}
	}

	// public static void log_Parser(String log) {
	// // if (testObject == null)
	// testObject = new ParseObject("analyticsData");
	// testObject.put("log", log);
	//
	// String devicename = appClass.getDeviceName();
	//
	// testObject.put("DeviceDetails", devicename);
	// testObject.put("TesterName", TesterName);
	// try {
	// testObject.save();
	// } catch (ParseException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	public boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public static BufferedWriter out;

	public static Boolean LogSerialportCommunication(String message) {
		File Root = Environment.getExternalStorageDirectory();
		// if (Root.canWrite())
		{
			Root = new File(Root.getPath() + "/com.cobra.Logs/");
			if (!Root.exists())
				Root.mkdir();

			FileWriter LogWriter;
			try {

				LogWriter = new FileWriter(Root.getPath() + "/Communication-"
						+ MainActivity.FileName + ".txt", true);

				out = new BufferedWriter(LogWriter);
				CharSequence s2 = DateFormat.format("hh:mm:ss.sss",
						System.currentTimeMillis());
				out.write(message);
				out.close();
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
	}

	private static Boolean WriteLog = false;
	private static Boolean WriteScriptsLog = true;

	public static void setScriptsLog(String message) {
		File Root = Environment.getExternalStorageDirectory();
		// if (Root.canWrite())
		{
			if (WriteScriptsLog) {
				Root = new File(Root.getPath() + "/com.cobra.Logs/");
				if (!Root.exists())
					Root.mkdir();

				FileWriter LogWriter;
				try {

					LogWriter = new FileWriter(Root.getPath() + "/Scripts-"
							+ MainActivity.FileName + ".csv", true);

					out = new BufferedWriter(LogWriter);

					out.write(message + "\n");
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static void setScriptsLogg(String message) {
		File Root = Environment.getExternalStorageDirectory();
		// if (Root.canWrite())
		{
			if (WriteScriptsLog) {
				Root = new File(Root.getPath() + "/com.cobra.Logs/");
				if (!Root.exists())
					Root.mkdir();

				FileWriter LogWriter;
				try {

					LogWriter = new FileWriter(Root.getPath() + "/Sgcripts-"
							+ MainActivity.FileName + ".csv", true);

					out = new BufferedWriter(LogWriter);

					out.write(message + "\n");
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static void writeLog(String message) {
		File Root = Environment.getExternalStorageDirectory();
		// if (Root.canWrite())
		{
			if (WriteLog) {
				Root = new File(Root.getPath() + "/com.cobra.Logs/");
				if (!Root.exists())
					Root.mkdir();

				FileWriter LogWriter;
				try {

					LogWriter = new FileWriter(Root.getPath() + "/CobraFlags-"
							+ MainActivity.FileName + ".csv", true);

					out = new BufferedWriter(LogWriter);

					out.write(message + "\n");
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private static Boolean WriteDeviceLogs = false;

	public static void setLogOnDevice(String message) {
		File Root = Environment.getExternalStorageDirectory();
		if (WriteDeviceLogs) {
			Root = new File(Root.getPath() + "/com.cobra.Logs/");
			if (!Root.exists())
				Root.mkdir();

			FileWriter LogWriter;
			try {

				LogWriter = new FileWriter(Root.getPath() + "/CheckSequence-"
						+ MainActivity.FileName + ".csv", true);

				out = new BufferedWriter(LogWriter);

				out.write(message + "\n");
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static Boolean WriteModuleLog = false;

	public static void setModuleLogOnDevice(String message) {
		File Root = Environment.getExternalStorageDirectory();
		// if (Root.canWrite())
		{
			if (WriteModuleLog) {
				Root = new File(Root.getPath() + "/com.cobra.Logs/");
				if (!Root.exists())
					Root.mkdir();

				FileWriter LogWriter;
				try {

					LogWriter = new FileWriter(Root.getPath() + "/Modules-"
							+ MainActivity.FileName + ".csv", true);

					out = new BufferedWriter(LogWriter);

					out.write(message + "\n");
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private static Boolean WriteModuleLog2 = false;

	public static void setModuleLogOnDevice2(String message) {
		File Root = Environment.getExternalStorageDirectory();
		// if (Root.canWrite())
		{
			if (WriteModuleLog2) {
				Root = new File(Root.getPath() + "/com.cobra.Logs/");
				if (!Root.exists())
					Root.mkdir();

				FileWriter LogWriter;
				try {

					LogWriter = new FileWriter(Root.getPath() + "/Modules-"
							+ MainActivity.FileName + ".csv", true);

					out = new BufferedWriter(LogWriter);

					out.write(message + "\n");
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private static Boolean WriteHeaderLog = false;

	public static void setHeaderLogOnDevice(String message) {
		File Root = Environment.getExternalStorageDirectory();
		// if (Root.canWrite())
		{
			if (WriteHeaderLog) {
				Root = new File(Root.getPath() + "/com.cobra.Logs/");
				if (!Root.exists())
					Root.mkdir();

				FileWriter LogWriter;
				try {

					LogWriter = new FileWriter(Root.getPath() + "/Header-"
							+ MainActivity.FileName + ".csv", true);

					out = new BufferedWriter(LogWriter);

					out.write(message + "\n");
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private static Boolean writeScriptsLog = false;

	public static void setScriptsLogOnDevice(String message) {
		File Root = Environment.getExternalStorageDirectory();
		// if (Root.canWrite())
		{
			if (writeScriptsLog) {
				Root = new File(Root.getPath() + "/com.cobra.Logs/");
				if (!Root.exists())
					Root.mkdir();

				FileWriter LogWriter;
				try {

					LogWriter = new FileWriter(Root.getPath() + "/Scripts-"
							+ MainActivity.FileName + ".csv", true);

					out = new BufferedWriter(LogWriter);

					out.write(message + "\n");
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private static Boolean writeSerialLog = false;
	private static Boolean writeSerialLog2 = false;
	public static boolean IsScriptPlaying = false;

	public static void setSerialLogOnDevice2(String message) {
		File Root = Environment.getExternalStorageDirectory();
		// if (Root.canWrite())
		{
			if (writeSerialLog2) {
				Root = new File(Root.getPath() + "/com.cobra.Logs/");
				if (!Root.exists())
					Root.mkdir();

				FileWriter LogWriter;
				try {

					LogWriter = new FileWriter(Root.getPath() + "/SERIAL-"
							+ MainActivity.FileName + ".csv", true);

					out = new BufferedWriter(LogWriter);

					out.write(message + "\n");
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static void setSerialLogOnDevice(String message) {

		File Root = Environment.getExternalStorageDirectory();
		// if (Root.canWrite())
		{
			if (writeSerialLog) {
				Root = new File(Root.getPath() + "/com.cobra.Logs/");
				if (!Root.exists())
					Root.mkdir();

				FileWriter LogWriter;
				try {

					LogWriter = new FileWriter(Root.getPath() + "/Reader_Log-"
							+ MainActivity.FileName + ".csv", true);

					out = new BufferedWriter(LogWriter);

					out.write(message + "\n");
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private static Boolean writeAN168 = true;

	public static void setAN168Log(String message) {
		File Root = Environment.getExternalStorageDirectory();
		// if (Root.canWrite())
		{
			if (writeAN168) {
				Root = new File(Root.getPath() + "/com.cobra.Logs/");
				if (!Root.exists())
					Root.mkdir();

				FileWriter LogWriter;
				try {

					LogWriter = new FileWriter(Root.getPath() + "/AN-168: "
							+ MainActivity.FileName + ".csv", true);

					out = new BufferedWriter(LogWriter);

					out.write(System.currentTimeMillis() + "   " + message
							+ "\n");
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private static Boolean writeAN106 = true;

	public static void setAN188Log(String message) {
		File Root = Environment.getExternalStorageDirectory();
		// if (Root.canWrite())
		{
			if (writeAN106) {
				Root = new File(Root.getPath() + "/com.cobra.Logs/");
				if (!Root.exists())
					Root.mkdir();

				FileWriter LogWriter;
				try {

					LogWriter = new FileWriter(Root.getPath() + "/AN_188-"
							+ MainActivity.FileName + ".csv", true);

					out = new BufferedWriter(LogWriter);

					out.write(System.currentTimeMillis() + "   " + message
							+ "\n");
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static void setAN192_DEMOLog(String message) {
		File Root = Environment.getExternalStorageDirectory();
		// if (Root.canWrite())
		{
			if (writeAN106) {
				Root = new File(Root.getPath() + "/com.cobra.Logs/");
				if (!Root.exists())
					Root.mkdir();

				FileWriter LogWriter;
				try {

					LogWriter = new FileWriter(Root.getPath()
							+ "/AN192_DEMOLog-" + MainActivity.FileName
							+ ".csv", true);

					out = new BufferedWriter(LogWriter);

					out.write(System.currentTimeMillis() + "   " + message
							+ "\n");
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static void setAN106NewLog(String message) {
		File Root = Environment.getExternalStorageDirectory();
		// if (Root.canWrite())
		{
			if (writeAN106) {
				Root = new File(Root.getPath() + "/com.cobra.Logs/");
				if (!Root.exists())
					Root.mkdir();

				FileWriter LogWriter;
				try {

					LogWriter = new FileWriter(Root.getPath() + "/AN_106_NEW-"
							+ MainActivity.FileName + ".csv", true);

					out = new BufferedWriter(LogWriter);

					out.write(System.currentTimeMillis() + "   " + message
							+ "\n");
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private static Boolean writeAN106Logs = true;

	public static void setAN106Logs(String message) {
		File Root = Environment.getExternalStorageDirectory();
		// if (Root.canWrite())
		{
			if (writeAN106Logs) {
				Root = new File(Root.getPath() + "/com.cobra.Logs/");
				if (!Root.exists())
					Root.mkdir();

				FileWriter LogWriter;
				try {

					LogWriter = new FileWriter(Root.getPath() + "/AN_106-"
							+ MainActivity.FileName + ".csv", true);

					out = new BufferedWriter(LogWriter);

					out.write(message + "\n");
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static boolean appActivated(Context cont) {
		{

			try {
				String text = "";
				SharedPreferences sharedpreferences = cont
						.getSharedPreferences("KEYPREFERENCES",
								Context.MODE_PRIVATE);

				if (sharedpreferences.contains("key")
						&& sharedpreferences.contains("activated")) {
					return sharedpreferences.getString("activated", "false")
							.equals("true");
				}
				// String text = SecureStorage.retrieveString(getDeviceID(),
				// "activated", this);

				if (text.equals("true"))
					return true;
				else
					return false;
			} catch (Exception e) {
				return false;
			}
		}

	}

	private static boolean writePlayScriptLog = true;
	public static boolean isDemoMode = false;
	public static boolean is_show_buy_dialog = false;
	public static int Remote_Mode = -1;

	// This log is  important for Show Control
	public static void setPlayScriptLog(String message) {
		File Root = Environment.getExternalStorageDirectory();
		// if (Root.canWrite())
		{
			if (writePlayScriptLog) {
				Root = new File(Root.getPath() + "/com.cobra.Logs/");
				if (!Root.exists())
					Root.mkdir();

				FileWriter LogWriter;
				try {

					LogWriter = new FileWriter(Root.getPath() + "/PlayScripts-"
							+ MainActivity.FileName + ".csv", true);

					out = new BufferedWriter(LogWriter);

					out.write(System.currentTimeMillis() + "   " + message
							+ "\n");
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private static Boolean IsTimeValid(Context context,
			long AppForceClosedTime, int delay) {
		if (AppForceClosedTime == 0 || AppForceClosedTime == 0.0) {
			return true;
		}
		long LastUpdatedTime = AppForceClosedTime;
		long CurrentTime = System.currentTimeMillis();

		long diff = CurrentTime - LastUpdatedTime;
		int hours = (int) diff / (60 * 60 * 1000);
		int minutes = (int) (diff / (60 * 1000));
		// minutes = minutes - 60 * hours;
		int seconds = (int) (diff / (1000));
		seconds = seconds - 60 * minutes;
		// long seconds = diff / (1000);

		if (seconds < delay) {
			Toast.makeText(
					context,
					"Please wait " + (delay - seconds)
							+ " seconds to re-start the Cobra app.",
					Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	public static Boolean CanCallModule(Context context,
			long AppForceClosedTime, int delay) {
		if (AppForceClosedTime == -1 || AppForceClosedTime == 0
				|| AppForceClosedTime == 0.0) {
			return false;
		}
		long LastUpdatedTime = AppForceClosedTime;
		long CurrentTime = System.currentTimeMillis();

		long diff = CurrentTime - LastUpdatedTime;
		int hours = (int) diff / (60 * 60 * 1000);
		int minutes = (int) (diff / (60 * 1000));
		// minutes = minutes - 60 * hours;
		int seconds = (int) (diff / (1000));
		seconds = seconds - 60 * minutes;
		// long seconds = diff / (1000);

		if (seconds < delay) {
			return false;
		}
		return true;
	}

	public static Boolean StartCobra(Context context) {
		try {
			SharedPreferences pref = context.getApplicationContext()
					.getSharedPreferences("Timer", 0);

			Boolean IsForcedClosed = pref.getBoolean("IsForcedClosed", false);
			Boolean IsAppInConnectedState = pref.getBoolean(
					"IsAppInConnectedState", false);

			long LastLoggedTime = pref.getLong("LastLoggedTime", -1);

			// by default it will be true. If we finish() application by ourself
			// then we will set it to false
			if (!IsForcedClosed || !IsAppInConnectedState) {
				return true;
			} else
				return IsTimeValid(context, LastLoggedTime, 10);
		} catch (Exception e) {
			return true;
		}
	}

	public static void SaveLastLoggedTime(Context context) {
		try {
			SharedPreferences pref = context.getApplicationContext()
					.getSharedPreferences("Timer", 0);
			SharedPreferences.Editor edit = pref.edit();

			edit.putBoolean("IsForcedClosed", IsAppForcedClosed);
			edit.putLong("LastLoggedTime", System.currentTimeMillis());
			edit.putBoolean("IsAppInConnectedState", IsAppInConnectedState);
			edit.apply();
		} catch (Exception e) {

		}
	}

	public static void SaveNullLastLoggedTime(Context context) {
		try {
			SharedPreferences pref = context.getApplicationContext()
					.getSharedPreferences("Timer", 0);
			SharedPreferences.Editor edit = pref.edit();

			edit.putBoolean("IsForcedClosed", false);
			edit.putLong("LastLoggedTime", System.currentTimeMillis());
			edit.putBoolean("IsAppInConnectedState", IsAppInConnectedState);
			edit.apply();
		} catch (Exception e) {

		}
	}

	public void setActive(Context context, int CHANNEL) {
		ArrayList<ModuleUIRow> ModuleUI_List = getModuleUI_List();
		if (ModuleUI_List != null) {
			for (int i = 0; i < ModuleUI_List.size(); i++) {
				int channel = ModuleUI_List.get(i).getChannel();
				if (CHANNEL == channel) {
					ModuleRow.setActive(ModuleUI_List.get(i), context, true);
				} else
					ModuleRow.setActive(ModuleUI_List.get(i), context, false);
			}

		}
	}

	public static HashMap<Integer, ModuleListItem> sortByComparator(
			HashMap<Integer, ModuleListItem> hashMap) {

		// Convert Map to List
		List<Map.Entry<Integer, ModuleListItem>> list = new LinkedList<Map.Entry<Integer, ModuleListItem>>(
				hashMap.entrySet());

		// Sort list with comparator, to compare the Map values
		Collections.sort(list,
				new Comparator<Map.Entry<Integer, ModuleListItem>>() {

					public int compare(Map.Entry<Integer, ModuleListItem> o1,
							Map.Entry<Integer, ModuleListItem> o2) {
						ModuleListItem moduleListItem_1 = o1.getValue();
						ModuleListItem moduleListItem_2 = o2.getValue();
						int channel_1 = moduleListItem_1.getModuleTag().currentChannel;
						int channel_2 = moduleListItem_2.getModuleTag().currentChannel;

						if (channel_1 < channel_2)
							return -1;
						else if (channel_1 == channel_2)
							return 0;
						else
							return 1;
						// return (o1.getValue()).compareTo(o2.getValue());
					}
				});

		// Convert sorted map back to a Map
		HashMap<Integer, ModuleListItem> sortedMap = new LinkedHashMap<Integer, ModuleListItem>();
		for (Iterator<Map.Entry<Integer, ModuleListItem>> it = list.iterator(); it
				.hasNext();) {
			Map.Entry<Integer, ModuleListItem> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	public ArrayList<ModuleUIRow> sortModuleList() {
		ArrayList<ModuleUIRow> moduleUIList = getModuleUI_List();
		ArrayList<ModuleUIRow> tempList = new ArrayList<ModuleUIRow>();
		for (int i = 0; i < moduleUIList.size(); i++) {
			tempList.add(moduleUIList.get(i));
		}
		tempList = sortOnBaseOfChannel(tempList);
		// Collections.sort(tempList, new ModuleComparator());
		setModuleUI_List(tempList);
		return getModuleUI_List();
	}

	private void setModuleUI_List(ArrayList<ModuleUIRow> tempList) {
		// TODO Auto-generated method stub
		ModuleUI_List = tempList;
	}

	private ArrayList<ModuleUIRow> sortOnBaseOfChannel(
			ArrayList<ModuleUIRow> inputList) {
		ModuleUIRow TMPrOW = null;
		int tmpPos;
		for (int i = 0; i < inputList.size(); i++) {
			for (int j = i; j < inputList.size(); j++) {
				if (inputList.get(i).getChannel() < inputList.get(j)
						.getChannel()) {
					// SWAP
					TMPrOW = inputList.get(i);

					inputList.set(i, inputList.get(j));
					inputList.set(j, TMPrOW);

					// inputList.get(j).setPosition(i);
					// inputList.get(i).setPosition(j);
				}
			}
		}
		return inputList;
	}

	public class ModuleComparator implements Comparator<ModuleUIRow> {
		public int compare(ModuleUIRow right, ModuleUIRow left) {

			// if(left.getChannel()<right.getChannel())
			// return -1;
			// else if(left.getChannel()==right.getChannel())
			// return 0;
			// else
			// return +1;
			String ch_left = "" + left.getChannel();
			String ch_right = "" + right.getChannel();
			return ch_left.compareToIgnoreCase(ch_right);
		}
	}

	public void setChannelCueData(ArrayList<ChannelCuesData> channelCuesData) {
		this.channelCuesData = channelCuesData;
	}

	public ArrayList<ChannelCuesData> getChannelCuesData() {
		return this.channelCuesData;
	}

	onCueDailogDismiss listner;
	private onFilterCueModule filterListener;
	private onFilterMode filterModeListener;
	// private ImageButton imageDrawerOpen;
	private TextView tvViewTitle;
	private OnModuleInformationUpdate onModuleInformationUpdate;

	private OnWrongScriptListener onWrongScriptListener;

	public OnWrongScriptListener getOnWrongScriptListener() {
		return onWrongScriptListener;
	}

	public void setOnWrongScriptListener(
			OnWrongScriptListener onWrongScriptListener) {
		this.onWrongScriptListener = onWrongScriptListener;
	}

	public void setDiloagListener(onCueDailogDismiss listner) {
		// TODO Auto-generated method stub
		this.listner = listner;
	}

	private DemoModeListener demo_mode_Listener;

	public onCueDailogDismiss getDiloagListener() {
		// TODO Auto-generated method stub
		return this.listner;
	}

	public void setChannelFilter(onFilterCueModule filterListener) {
		this.filterListener = filterListener;

	}

	public onFilterCueModule getChannelFilter() {
		return this.filterListener;

	}

	public void setModeFilter(onFilterMode filterModeListener) {
		this.filterModeListener = filterModeListener;

	}

	public onFilterMode getModeFilter() {
		return this.filterModeListener;

	}

	public void setHeaderTitle(TextView tvViewTitle) {
		// TODO Auto-generated method stub
		this.tvViewTitle = tvViewTitle;
	}

	public TextView getHeaderTitle() {
		// TODO Auto-generated method stub
		return this.tvViewTitle;
	}

	public void setonModuleInformationUpdate(
			OnModuleInformationUpdate onModuleInformationUpdate) {
		// TODO Auto-generated method stub

		this.onModuleInformationUpdate = onModuleInformationUpdate;
	}

	public OnModuleInformationUpdate getonModuleInformationUpdate() {
		// TODO Auto-generated method stub

		return this.onModuleInformationUpdate;
	}

	public void setDummyModeOn18R2() {
		// TODO Auto-generated method stub

	}

}

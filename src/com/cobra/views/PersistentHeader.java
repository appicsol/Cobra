package com.cobra.views;

import java.lang.reflect.Modifier;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Global;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toast;

import com.cobra.MainActivity;
import com.cobra.R;
import com.cobra.R.color;
import com.cobra.appClass;
import com.cobra.api.Cobra;
import com.cobra.api.Cobra.CobraEvent;
import com.cobra.api.Cobra.ICobraEventListener;
import com.cobra.api.CobraDataTags.ModuleDataTag;
import com.cobra.api.CobraFlags;
import com.parse.gdata.Escaper;

public class PersistentHeader extends Fragment {
	TextView tvViewTitle, tvArmedStatus, tvRemoteChannel, tvNumModulesArmed,
			tvTotalModules, tvNumModulesTest;// tvFlagStatus;
	TextView tvBattery;

	ProgressBar progressBattery;
	ImageButton imageDrawerOpen;
	Cobra cobra;
	// MainActivity activity;
	appClass globV;
	private RelativeLayout layout_device_info_container;
	public static final int EVENT_TYPE_HEADER_INFO_CHANGE = -2;

	public static final int EVENT_TYPE_HEADER_TEXT_INFO_CHANGE = -3;

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		UnregisterReciever();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = View.inflate(getActivity(), R.layout.persistent_header,
				null);
		globV = (appClass) getActivity().getApplicationContext();
		this.cobra = globV.getCobra();
		// this.activity = activity;

		tvViewTitle = (TextView) view.findViewById(R.id.tvViewTitle);
		globV.setHeaderTitle(tvViewTitle);

		tvArmedStatus = (TextView) view.findViewById(R.id.tvArmedStatus);
		tvRemoteChannel = (TextView) view.findViewById(R.id.tvRemoteChannel);
		tvBattery = (TextView) view.findViewById(R.id.tvProgressBattery);
		tvTotalModules = (TextView) view.findViewById(R.id.lbl_total_value);

		tvNumModulesArmed = (TextView) view
				.findViewById(R.id.tvNumModulesArmed);
		tvNumModulesTest = (TextView) view.findViewById(R.id.tvNumModulesTest);

		layout_device_info_container = (RelativeLayout) view
				.findViewById(R.id.layout_device_info_container);

		// progressBattery = (ProgressBar)
		// view.findViewById(R.id.progressBattery);
		imageDrawerOpen = (ImageButton) view.findViewById(R.id.imageDrawerOpen);

		imageDrawerOpen.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					imageDrawerOpen.setBackgroundColor(Color
							.parseColor("#33CCCCCC"));
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					imageDrawerOpen.setBackgroundColor(Color.TRANSPARENT);
				}
				return false;
			}
		});

		imageDrawerOpen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!MainActivity.mDrawerLayout
						.isDrawerOpen(MainActivity.mDrawerList))
					MainActivity.mDrawerLayout
							.openDrawer(MainActivity.mDrawerList);
				else
					MainActivity.mDrawerLayout
							.closeDrawer(MainActivity.mDrawerList);
			}
		});

		try {
			IntentFilter intentFilter = new IntentFilter(
					appClass.RECIEVER_PERSISTENT_HEADER);
			getActivity().registerReceiver(broadcastReciever, intentFilter);

		} catch (Exception e) {
			int i = 0;
			i++;
		}
		updateCobraDeviceInformation();
		updateCobraDeviceStatus();

		// set view content visibility if app on demo mode
		// if (appClass.isDemoMode) {// DEMO
		// layout_device_info_container.setVisibility(View.GONE);
		// }

		return view;
	}

	private ArrayList<ModuleData> moduleInfo;

	private void updateModulesArmStatus(final int modID, final Boolean IsArmed,
			final Boolean clearTop) {


		if (IsArmed) {
			Mode = Cobra.MODE_ARMED;
		} else {
			Mode = -1;
		}

		appClass.setModuleLogOnDevice2("Header Broadcast Recieved, Mod id = "
				+ modID);
		int armed = 0, test = 0;
		Boolean IS_Found = false;
		if (globV == null)
			globV = (appClass) getActivity().getApplicationContext();
		/*
		 * If module id is -2 we need to clear whole list of modules set
		 * moduleInfo = null to clear it and set "IS_Found=true" to clear
		 * modules in header
		 */

		if (clearTop) {
			appClass.setHeaderLogOnDevice("Header Cleared");
			moduleInfo = null;

			if (armed == 0) {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {

						tvNumModulesArmed.setTextColor(getResources().getColor(
								R.color.white_showcontrols));
					}
				}, 50);
			} else if (armed > 0) {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {

						tvNumModulesArmed.setTextColor(getResources().getColor(
								R.color.red));
					}
				}, 50);
			}

			if (test == 0) {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {

						tvNumModulesTest.setTextColor(getResources().getColor(
								R.color.white_showcontrols));
					}
				}, 50);
			} else if (test > 0) {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {

						tvNumModulesTest.setTextColor(getResources().getColor(
								R.color.green_modulelist));
					}
				}, 50);
			}

			int totalModules = armed + test;

			if (armed == 0)
				tvNumModulesArmed.setText("0");
			else
				tvNumModulesArmed.setText("" + (armed));

			if (totalModules == 0)
				tvTotalModules.setText("0");
			else
				tvTotalModules.setText("" + totalModules);

			if (test == 0)
				tvNumModulesTest.setText("0");
			else
				tvNumModulesTest.setText("" + test);
			globV.ClearLinearListView();
			return;
		}
		if (modID == -1) {
			appClass.setHeaderLogOnDevice("MODULE ID -1 RECIEVED");
			return;
		}
		if (moduleInfo == null)
			moduleInfo = new ArrayList<PersistentHeader.ModuleData>();

		for (int i = 0; i < moduleInfo.size(); i++) {
			if (modID == moduleInfo.get(i).getModule_id()) {
				appClass.setHeaderLogOnDevice("STEP 4: Module FOUND : " + modID);
				moduleInfo.get(i).setIs_Arm(IsArmed);
				IS_Found = true;
			}
			if (moduleInfo.get(i).getIs_Arm()) {
				armed++;
				appClass.setHeaderLogOnDevice("STEP 5: Module on ARM , Total ARM : "
						+ armed + "Total TEST : " + test);
			} else {
				test++;
				appClass.setHeaderLogOnDevice("STEP 5: Module on TEST , Total TEST : "
						+ test + "Total ARM : " + armed);
			}
		}

		if (!IS_Found) {
			appClass.setHeaderLogOnDevice("STEP 2: New Module Found : " + modID);
			ModuleData module = new ModuleData();
			module.setIs_Arm(IsArmed);
			module.setModule_id(modID);
			moduleInfo.add(module);

			if (IsArmed) {
				armed++;
				appClass.setHeaderLogOnDevice("STEP 3: New Module ARM MODE : TOTAL ARM "
						+ armed + " TOTAL TEST : " + test);
			} else {
				test++;
				appClass.setHeaderLogOnDevice("STEP 3: New Module TEST MODE : TOTAL TEST "
						+ test + " TOTAL ARM " + armed);
			}
		}

		if (armed == 0) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {

					tvNumModulesArmed.setTextColor(getResources().getColor(
							R.color.white_showcontrols));
				}
			}, 50);
		} else if (armed > 0) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {

					tvNumModulesArmed.setTextColor(getResources().getColor(
							R.color.red));
				}
			}, 50);
		}

		if (test == 0) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {

					tvNumModulesTest.setTextColor(getResources().getColor(
							R.color.white_showcontrols));
				}
			}, 50);
		} else if (test > 0) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {

					tvNumModulesTest.setTextColor(getResources().getColor(
							R.color.green_modulelist));
				}
			}, 50);
		}

		int totalModules = armed + test;

		if (armed == 0)
			tvNumModulesArmed.setText("0");
		else
			tvNumModulesArmed.setText("" + (armed));

		if (totalModules == 0)
			tvTotalModules.setText("0");
		else
			tvTotalModules.setText("" + totalModules);

		if (test == 0)
			tvNumModulesTest.setText("0");
		else
			tvNumModulesTest.setText("" + test);

		if (armed == 0 && test == 0)
			globV.ClearLinearListView();
	}

	public void updateCobraDeviceInformation() {

		String str = (cobra.getDeviceType() == 1) ? "18R2" : "-";

		if (str.contains("-")) {
			appClass.setFlagFirmwareUpate(CobraFlags.READY_TO_CALL_FIRMWARE_DATA);
			cobra.AckOwnFlagStatusChange();
			appClass.writeLog("Flag Firmware Update State READY_TO_CALL_FIRMWARE_DATA : "
					+ CobraFlags.READY_TO_CALL_FIRMWARE_DATA);
		} else {
			appClass.setFlagFirmwareUpate(CobraFlags.RECIEVED_FIRMWARE_DATA);
			cobra.AckOwnFlagStatusChange();
			appClass.writeLog("Flag Firmware Update State RECIEVED_FIRMWARE_DATA : "
					+ CobraFlags.RECIEVED_FIRMWARE_DATA);
			if (appClass.getFlagScriptsUpate() == CobraFlags.SCRIPTS_DATA_READY_TO_ACKNOWLEDGE)
				appClass.setFlagScriptsUpate(CobraFlags.READY_TO_CALL_SCRIPTS_DATA);
			// appClass.setFlagScriptsUpate(CobraFlags.ALL_SCRIPT_DATA_IS_FINISHED);
			// cobra.AckOwnFlagStatusChange();
			// appClass.writeLog("Flag Scripts Update State ALL_SCRIPT_DATA_IS_FINISHED : "
			// + CobraFlags.ALL_SCRIPT_DATA_IS_FINISHED);
		}

		// tvDeviceType.setText(new String(str));
		//
		// str = String.format("v%d.%d.%d", cobra.getDeviceVersion().major,
		// cobra.getDeviceVersion().minor,
		// cobra.getDeviceVersion().revision);
		// tvDeviceFirmware.setText(new String(str));

	}

	String TAG_CLASS = "PersistentHeader:";

	public void updateCobraDeviceStatus() {
		try {

			if (cobra.getMode() == Cobra.MODE_TEST) {
				if (appClass.IsScriptPlaying) {
					appClass.IsScriptPlaying = false;
					if (cobra != null)
						cobra.stopScript();
				}
				tvArmedStatus.setText("test");
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						tvArmedStatus.setTextColor(getResources().getColor(
								R.color.green_modulelist));
					}
				}, 100);

			} else if (cobra.getMode() == Cobra.MODE_ARMED) {

				tvArmedStatus.setText("armed");
				tvArmedStatus.setTextColor(getResources().getColor(
						R.color.red_modulelist));
			}
			int pre_channel = -1;
			try {
				pre_channel = Integer.parseInt(tvRemoteChannel.getText()
						.toString());
				tvRemoteChannel.setText("" + (cobra.getDeviceChannel() & 0xFF));
			} catch (Exception e) {

			}
			int new_channel = cobra.getDeviceChannel() & 0xFF;
			// if (pre_channel != new_channel) {
			// Intent intent = new Intent(
			// appClass.RECIEVER_ADAPTER_MODULE_LIST);
			// activity.sendBroadcast(intent);
			// }
			setBattery();
		} catch (Exception e) {
			// Toast.makeText(getContext(), "Exception : " + e.getMessage(),
			// Toast.LENGTH_LONG).show();
		}
	}

	private void setBattery() {
		String FunctionName = "setBattery()";
		int oldchannel;
		int oldmode;
		int oldbattery;
		int battery = (cobra.getDeviceBatteryLevel() & 0xFF);
		if (battery > 9) {
			tvBattery.setText("Loading");
			appClass.setFlagDeviceUpate(CobraFlags.READY_TO_CALL_DEVICE_STATUS);
			cobra.AckOwnFlagStatusChange();
			appClass.writeLog("Flag Device Update State READY_TO_CALL_DEVICE_STATUS : "
					+ CobraFlags.READY_TO_CALL_DEVICE_STATUS);
		} else {
			tvBattery.setText("P" + battery);

		}

		// try {
		// String bat = tvBattery.getText().toString();
		// if (bat.contains("P")) {
		//
		// } else {
		// appClass.FlagDeviceUpate = 1;
		// }
		// } catch (Exception e) {
		// int i = 0;
		// i++;
		// }

		progressBattery.setProgress(0);
		Rect bounds = progressBattery.getProgressDrawable().getBounds();
		progressBattery.getProgressDrawable().setBounds(bounds);

		if (battery < 0)
			return;
		if (battery > 4)
			progressBattery.setProgressDrawable(getResources().getDrawable(
					R.drawable.persistentheader_battery_normal));
		else if (battery > 2)
			progressBattery.setProgressDrawable(getResources().getDrawable(
					R.drawable.persistentheader_battery_medium));
		else
			progressBattery.setProgressDrawable(getResources().getDrawable(
					R.drawable.persistentheader_battery_low));

		progressBattery.setProgress(battery);
		progressBattery.invalidate();

	}

	public void setHeaderData(int modid, boolean armed) {

		if (armed) {
			appClass.Remote_Mode = Cobra.MODE_ARMED;
		} else {
			appClass.Remote_Mode = -1;
		}

		Boolean cleartop = false;

		if (modid == -2)
			cleartop = true;

		updateModulesArmStatus(modid, armed, cleartop);
	}

	String temp = "";
	BroadcastReceiver broadcastReciever = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			try {

				int event = -1;
				int battery = -1;
				int channel = -1;
				int mode = -1;
				int time = -1;
				if (intent.hasExtra(appClass.TAG_EVENT)) {
					event = intent.getExtras().getInt(appClass.TAG_EVENT);
				}
				if (intent.hasExtra("battery")) {
					battery = intent.getExtras().getInt("battery");
				}
				if (intent.hasExtra("channel")) {
					channel = intent.getExtras().getInt("channel");
				}
				if (intent.hasExtra("mode")) {
					mode = intent.getExtras().getInt("mode");
				}
				if (intent.hasExtra("time")) {
					time = intent.getExtras().getInt("time");
				}

				appClass.Remote_Mode = mode;

				switch (event) {
				case PersistentHeader.EVENT_TYPE_HEADER_INFO_CHANGE:

					int modid = -1;
					Boolean armed = false;
					Boolean cleartop = false;
					if (intent.hasExtra("modID") && intent.hasExtra("Armed")) {
						modid = intent.getExtras().getInt("modID");
						armed = intent.getExtras().getBoolean("Armed");
						if (modid == -2)
							cleartop = true;
					}
					updateModulesArmStatus(modid, armed, cleartop);
					break;

				case Cobra.EVENT_TYPE_DEVICE_INFO_CHANGE:
					updateCobraDeviceInformation();

					break;

				case Cobra.EVENT_TYPE_STATUS_CHANGE:

					updateCobraDeviceStatus(battery, channel, mode);
					updateCobraDeviceInformation();
					globV.setCurrentChannel(channel);
					globV.setActive(getActivity(), channel);

					// if (appClass.getFlagScriptsUpate() ==
					// CobraFlags.SCRIPTS_DATA_READY_TO_ACKNOWLEDGE)
					// appClass.setFlagScriptsUpate(CobraFlags.READY_TO_CALL_SCRIPTS_DATA);
					cobra.AckOwnFlagStatusChange();
					appClass.writeLog("Flag Scripts Update State ALL_SCRIPT_DATA_IS_FINISHED : "
							+ CobraFlags.ALL_SCRIPT_DATA_IS_FINISHED);

					if (appClass.getFlagDeviceUpate() == CobraFlags.RECIEVED_DEVICE_STATUS) {
						appClass.writeLog("Flag Device Update State DEVICE_STATUS_READY_TO_ACKNOWLEDGE : "
								+ CobraFlags.DEVICE_STATUS_READY_TO_ACKNOWLEDGE);
						appClass.setFlagDeviceUpate(CobraFlags.DEVICE_STATUS_READY_TO_ACKNOWLEDGE);
						cobra.AckOwnFlagStatusChange();
					}
					// if we recieve any module then we set flag to "2"
					// if (appClass.getFlagModuleUpate() ==
					// CobraFlags.READY_TO_CALL_NEXT_MODULE_DATA) {
					// // || appClass.getFlagModuleUpate() ==
					// // CobraFlags.WAIT_FOR_REQUESTED_MODULE_DATA) {
					// appClass.setFlagModuleUpate(CobraFlags.READY_TO_CALL_NEXT_MODULE_DATA);
					// cobra.AckOwnFlagStatusChange();
					// appClass.setAN168Log("PERSISTENT HEADER");
					// cobra.getNext_QUEUED_MODULE_DATA();
					// }
					break;

				// case Cobra.EVENT_TYPE_MODULE_DATA_CHANGE:
				//
				// updateModulesArmStatus(null, false);
				// break;
				case appClass.EVENT_MODULE_FLAG_DATA_CHANGE:
					String flag = "";
					// if (tvFlagStatus != null && intent.hasExtra("flag")) {
					// flag = intent.getExtras().getString("flag");
					// // tvFlagStatus.setText(flag);
					// }
					// if (intent.hasExtra("msg")) {
					// String msg = intent.getExtras().getString("msg");
					// tvFlagStatus.setText(msg);
					// }
					break;
				case PersistentHeader.EVENT_TYPE_HEADER_TEXT_INFO_CHANGE:

				}

			} catch (Exception e) {
				// appClass.setLogOnDevice("EXCEPTION: " + e.getMessage());
			}
		}
	};

	// BroadcastReceiver broadcastRecieverFlag = new BroadcastReceiver() {
	//
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// // TODO Auto-generated method stub
	// // Toast.makeText(context, "broadcast is working",
	// // Toast.LENGTH_SHORT).show();
	// try {
	// if (tvFlagStatus != null && intent.hasExtra("flag")) {
	// int flag = intent.getExtras().getInt("flag");
	// tvFlagStatus.setText(appClass.ModuleFlagStatus(flag));
	// }
	//
	// } catch (Exception e) {
	//
	// }
	// }
	// };

	public static int Mode = -1;

	public void updateCobraDeviceStatus(int battery, int channel, int mode) {
		try {
			Mode = mode;

			if (mode == Cobra.MODE_TEST) {

				tvArmedStatus.setText("test");
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						tvArmedStatus.setTextColor(getResources().getColor(
								R.color.green_modulelist));
					}
				}, 100);

				Intent intent = new Intent(appClass.RECIEVER_SHOW_CONTROLS);
				intent.putExtra("stopscripts", true);
				getActivity().sendBroadcast(intent);// IF scripts running stop
													// it
				// because scripts dont run in
				// test mode

			} else if (mode == Cobra.MODE_ARMED) {

				tvArmedStatus.setText("armed");
				tvArmedStatus.setTextColor(getResources().getColor(
						R.color.red_modulelist));
			}

			int chan = cobra.getDeviceChannel() & 0xFF;

			tvRemoteChannel.setText("" + (channel));
			// Intent intent = new
			// Intent(appClass.RECIEVER_ADAPTER_MODULE_LIST);
			// activity.sendBroadcast(intent);
			setBattery(battery);
		} catch (Exception e) {
			Toast.makeText(getActivity(), "Exception : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
		}
	}

	private void setBattery(int battery2) {
		String FunctionName = "setBattery()";

		final int battery = battery2;
		if (battery > 9) {
			tvBattery.setText(" ");
			appClass.setFlagDeviceUpate(CobraFlags.READY_TO_CALL_DEVICE_STATUS);
			cobra.AckOwnFlagStatusChange();
			appClass.writeLog("Flag Device Update State READY_TO_CALL_DEVICE_STATUS : "
					+ CobraFlags.READY_TO_CALL_DEVICE_STATUS);
		} else {

			if (battery >= 4) {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						tvBattery.setTextColor(getResources().getColor(
								R.color.green_modulelist));
						tvBattery.setText("P" + battery);
					}
				}, 100);
			} else if (battery >= 2) {

				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						tvBattery.setTextColor(getResources().getColor(
								R.color.orange));
						tvBattery.setText("P" + battery);
					}
				}, 100);
			} else if (battery >= 0) {

				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						tvBattery.setTextColor(getResources().getColor(
								R.color.red_modulelist));
						tvBattery.setText("P" + battery);
					}
				}, 100);
			}

		}

		// progressBattery.setProgress(0);
		// Rect bounds = progressBattery.getProgressDrawable().getBounds();
		// progressBattery.getProgressDrawable().setBounds(bounds);
		//
		// if (battery < 0)
		// return;
		// if (battery > 4)
		// progressBattery.setProgressDrawable(getResources().getDrawable(
		// R.drawable.persistentheader_battery_normal));
		// else if (battery > 2)
		// progressBattery.setProgressDrawable(getResources().getDrawable(
		// R.drawable.persistentheader_battery_medium));
		// else
		// progressBattery.setProgressDrawable(getResources().getDrawable(
		// R.drawable.persistentheader_battery_low));
		//
		// progressBattery.setProgress(battery);
		// progressBattery.invalidate();

	}

	public void UnregisterReciever() {
		getActivity().unregisterReceiver(broadcastReciever);
	}

	public class ModuleData {
		int module_id;
		Boolean Is_Arm;

		public ModuleData() {
			module_id = -1;
			Is_Arm = false;
		}

		public int getModule_id() {
			return module_id;
		}

		public void setModule_id(int module_id) {
			this.module_id = module_id;
		}

		public Boolean getIs_Arm() {
			return Is_Arm;
		}

		public void setIs_Arm(Boolean is_Arm) {
			Is_Arm = is_Arm;
		}

	}
}
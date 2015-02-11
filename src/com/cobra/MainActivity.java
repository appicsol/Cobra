package com.cobra;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cobra.api.Cobra;
import com.cobra.api.Cobra.CobraEvent;
import com.cobra.api.Cobra.ICobraEventListener;
import com.cobra.api.Cobra.ModuleType;
import com.cobra.api.CobraDataTags.EventDataTag;
import com.cobra.api.CobraDataTags.ModuleDataTag;
import com.cobra.api.CobraFlags;
import com.cobra.db.DBAdapter;
import com.cobra.dialogs.DialogAbout;
import com.cobra.dialogs.DialogBuy;
import com.cobra.dialogs.DialogDemo;
import com.cobra.dialogs.DialogDisconnectNoDevice;
import com.cobra.dialogs.DialogScriptLoader;
import com.cobra.interfaces.DemoModeListener;
import com.cobra.module.interfaces.OnModuleInformationUpdate;
import com.cobra.navdrawer.NavDrawerAdapter;
import com.cobra.navdrawer.NavDrawerItem;
import com.cobra.navdrawer.NavMenuItem;
import com.cobra.navdrawer.NavMenuSection;
import com.cobra.services.ReaderService;
import com.cobra.view.bucketfiring.ModuleViews;
import com.cobra.views.BucketFiringNew;
import com.cobra.views.Connectivity;
import com.cobra.views.Connectivity.CobraConnectedEvent;
import com.cobra.views.Connectivity.DemoButtonListener;
import com.cobra.views.Connectivity.OnCobraConnected;
import com.cobra.views.PersistentHeader;
import com.cobra.views.TestCommands;
import com.cobra.views.modulelist.ModuleList;
import com.parse.Parse;

//import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	private String TAG;
	private static final int VIEW_MODULELIST = 1;
	private static final int VIEW_SHOWCONTROLS = 2;
	private static final int VIEW_BUCKETFIRING = 3;
	private static final int VIEW_TESTCOMMANDS = 4;
	private static final int ABOUT = 5;

	public static final boolean debugMode = false;
	public static long timer_threshold = 400;
	public static boolean demoMode = true;

	public static boolean connect_popup = true;

	public static DrawerLayout mDrawerLayout;
	public static ListView mDrawerList;
	private NavDrawerItem[] drawerList;

	private RelativeLayout contentFrame;
	// private FrameLayout headerFrame;

	// private PersistentHeader persistentHeader;
	// private ButtonOverlay buttonOverlay;
	// private ModuleList moduleList;
	// private ShowControls showControls;
	private Connectivity connectivity;

	private MainActivity context = this;

	Cobra cobra = null;
	// private BucketFiring bucketFiring;
	// private BucketFiringNew bucketFiringNew;
	private TestCommands test_commands;
	public static String FileName = "";

	// 794cce6e305f3dac - emulator id

	// 9367a4c5651cda19 - Stephen's moto g
	appClass globV;
	private Intent service_intent;
	private Boolean CreateTimeLog = true;
	public static Thread AckThread;

	OnModuleInformationUpdate onModuleInformationUpdate;
	protected boolean isFirstTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		isFirstTime = Connectivity.appFirstTime(MainActivity.this);
		On_Create(isFirstTime);

		// show dialog on demo

	}

	private void UnSupportedDeviceDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setTitle("UnSupported Device Detected!");
		builder.setMessage("Small screens does not support DEBUGGER in Cobra. Please use larg screen devices");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				appClass.IsAppForcedClosed = false;
				finish();
			}
		});
		builder.show();
	}

	public void clearFlags() {
		appClass.setFlagDeviceUpate(CobraFlags.DEVICE_STATUS_READY_TO_ACKNOWLEDGE);
		appClass.setFlagFirmwareUpate(CobraFlags.FIRMWARE_DATA_READY_TO_ACKNOWLEDGE);
		appClass.setFlagScriptsUpate(CobraFlags.SCRIPTS_DATA_READY_TO_ACKNOWLEDGE);

		appClass.setFlagModuleUpate(CobraFlags.MODULE_DATA_NOT_READY_TO_ACKNOWLEDGE);
		appClass.setFlagFireCuesUpate(CobraFlags.FIRECUE_DATA_READY_TO_ACKNOWLEDGE);

		appClass.writeLog("Flag Device Update State : DEVICE_STATUS_READY_TO_ACKNOWLEDGE "
				+ CobraFlags.DEVICE_STATUS_READY_TO_ACKNOWLEDGE);
		appClass.writeLog("Flag Firmware Data State FIRMWARE_DATA_READY_TO_ACKNOWLEDGE : "
				+ CobraFlags.FIRMWARE_DATA_READY_TO_ACKNOWLEDGE);
		appClass.writeLog("Flag Script Data State ALL_SCRIPT_DATA_IS_FINISHED : "
				+ CobraFlags.ALL_SCRIPT_DATA_IS_FINISHED);
		appClass.writeLog("Flag Module Data State MODULE_DATA_NOT_READY_TO_ACKNOWLEDGE : "
				+ CobraFlags.MODULE_DATA_NOT_READY_TO_ACKNOWLEDGE);
		appClass.writeLog("Flag Firecue Data State FIRECUE_DATA_READY_TO_ACKNOWLEDGE : "
				+ CobraFlags.FIRECUE_DATA_READY_TO_ACKNOWLEDGE);
	}

	public void On_Create(boolean isFirstTime2) {

		Cobra.counter = 0;
		// if (!appClass.isTablet(this)) {
		// UnSupportedDeviceDialog();
		// return;
		// }

		// DemoModeListener demolistener = null;
		// cobra_demo.setDemo_mode(demolistener);

		appClass.activity = MainActivity.this;
		IsAppActive = true;
		appClass.IsConnectionEstablished = false;

		if (!appClass.StartCobra(MainActivity.this)) {
			CreateTimeLog = false;
			finish();
		} else {
			appClass.IsAppInConnectedState = false;
			appClass.IsAppForcedClosed = true;
		}

		Parse.initialize(this, "MWFrHczt4AzIyB8irh2wzDzjI7jJg76zLJqf5NJJ",
				"vliC7NOVJDtPN04h0AL4rGYRh0gfsuXrkSb6SvRg");

		ClearModuleObjects();
		// ParseObject testObject = new ParseObject("analyticsData");
		// testObject.put("log", "App just Started!");
		// testObject.saveInBackground();
		// appClass.createReadingLogOnDevice("MainActivity",
		// "App just Started!");
		// appClass.log_Parser("App just Started!");
		DBAdapter db = new DBAdapter(this.getApplicationContext());
		globV = (appClass) getApplicationContext();

		if (globV.getScriptList() != null) {
			globV.getScriptList().clear();

		}
		try {
			db.createDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
		TAG = MainActivity.class.getName();
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		connectivity = new Connectivity(this, new OnCobraConnected() {
			@Override
			public void onCobraConnected(CobraConnectedEvent event) {
				if (isFirstTime) {
					Log.i(TAG, "First Time App!");

					showFirstTimeDialog();
					return;
				}
				if (event.getCobraDevice() != null
						&& event.getCobraDevice().getError() != null
						&& event.getCobraDevice().getError()
								.equals(Cobra.ERROR_POWER_CYCLE)) {
					Log.i(TAG, "PowerCycle Error!");

					// if (!appClass.isDemoMode) {// DEMO
					showDialogDisconnectNoDevice();
					// }

					// AlertDialog.Builder builder = new AlertDialog.Builder(
					// MainActivity.this);
					// builder.setTitle("Disconnected");
					// builder.setMessage("You tried to plug in the cable connecting the 18R2 and Android too quickly. Please press OK and wait 5 seconds before connecting the 18R2 and Android.");
					// builder.setPositiveButton("OK",
					// new DialogInterface.OnClickListener() {
					// @Override
					// public void onClick(DialogInterface dialog,
					// int which) {
					// appClass.IsAppForcedClosed = false;
					// finish();
					// }
					// });
					// builder.show();
					// appClass.createReadingLogOnDevice();
					// appClass.log_Parser("App was closed forcefully, Please disconnect and then reconnect with Device.");
					return;
				}
				Log.i(TAG, "onCobraConnected");
				demoMode = false;
				cobra = event.getCobraDevice();
				globV.setCobra(cobra);
				// refreshPressed();

				if (!appClass.isDemoMode) {
				} else {
					showDialogDemo();
				}

				startCheckerThread();

				mainViewInit();

				appClass.IsAppInConnectedState = true;
				SparseArray<SparseArray<EventDataTag>> temp = cobra
						.getEventsData();
				for (int i = 0; i < temp.size(); i++) {
					int key = temp.keyAt(i);
					for (int j = 0; j < temp.get(key).size(); j++) {
						int key2 = temp.get(key).keyAt(j);
						EventDataTag tag = temp.get(key).get(key2);
						Log.i(TAG, String.format(
								"#: %d timeIdx: %d chan: %d cues: %d name: %s",
								tag.eventIndex, tag.timeIndex, tag.channel,
								tag.cueList, tag.eventDescription));
					}
				}
			}

		}, appActivated(), isFirstTime2);

		connectivity.setDemoButtonListener(new DemoButtonListener() {
			@Override
			public void demoModeButtonPress() {
				cobra = new Cobra(MainActivity.this, context);
				mainViewInit();
			}
		});
		setContentView(connectivity);

		service_intent = new Intent(context, ReaderService.class);
		startService(service_intent);

		// IntentFilter intentFilter = new IntentFilter(
		// appClass.RECIEVER_ACKNOWLEDGEMENT);
		// registerReceiver(AcknowledgementReceiver, intentFilter);

	}

	private void showFirstTimeDialog() {
		FragmentTransaction ft = (this).getSupportFragmentManager()
				.beginTransaction();

		Fragment prev = (this).getSupportFragmentManager().findFragmentByTag(
				"dialog");

		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		// Create and show the dialog.
		DialogFragment newFragment = DialogBuy.newInstance(1, "", 1);
		newFragment.show(ft, "dialog");

	}

	private int Tries_For_Modules = 0;
	private int lastscript = -1;
	private int lastevent = -1;
	private int event_loading_tries = 0;
	private int script_loading_tries = 0;
	public static int scriptStatus = 0;

	public static DialogScriptLoader static_dialog;
	public static FragmentTransaction static_ft;

	private boolean firstCall = true;

	public static boolean closeAppFromPopup = false;

	// This is checker thread that receive first time all data from 18H2
	private Runnable checker = new Runnable() {
		@Override
		public void run() {

			if (appClass.IsConnectionEstablished) {
				if (cobra != null) {

					// / TEXT///
					// if(event_loading_tries == 0){
					// cobra.getNEXT_SCRIPT_DATA(-1);
					// }
					// / TEXT///

					appClass.setPlayScriptLog("Calling first time refresh!");

					refreshPressed();
				}
			}
			while (true) {

				try {

					if (AckThread.isInterrupted()) {
						appClass.setLogOnDevice("----TIMER IMTERRUPTED----");
						return;
					}
					Log.i("MainActivity ACK Flag Checker", "running");
					Thread.sleep(MainActivity.timer_threshold);
					if (appClass.IsConnectionEstablished) {
						if (cobra != null) {

							if (appClass.getFlagScriptsUpate() == CobraFlags.SCRIPT_DATA_IS_CALLING) {

								int currentscript = globV.getLastScriptCalled();
								int currentevent = globV.getLastEventCalled();

								if (event_loading_tries == 5
										|| script_loading_tries == 5) {
									scriptStatus = 3;

									appClass.setFlagScriptsUpate(CobraFlags.ALL_SCRIPT_DATA_IS_FINISHED);

								}

//								appClass.setAN188Log(appClass.getNextScriptId()
//										+ " DD lastscript: " + lastscript
//										+ " |currentscript" + currentscript
//										+ " ||| lastevent: " + lastevent
//										+ " |currentevent" + currentevent);

								if (appClass.getNextScriptId() < 0) {

									cobra.getNEXT_SCRIPT_DATA(-1);

									script_loading_tries++;

//									appClass.setAN188Log("Script tries: "
//											+ script_loading_tries++);

								} else if (currentscript == lastscript
										&& currentevent == lastevent
										&& Cobra.scriptIndexDataList != null
										&& Cobra.scriptIndexDataList.size() > -1) {

									event_loading_tries++;

//									appClass.setAN188Log("Event tries: "
//											+ event_loading_tries++);

									cobra.getNEXT_EVENT_DATA(currentscript,
											currentevent);
									script_loading_tries = 0;
								} else {

									script_loading_tries = 0;
									event_loading_tries = 0;
									lastscript = currentscript;
									lastevent = currentevent;
								}

								continue;
							}

							// 1st Priority
							if (appClass.getFlagDeviceUpate() == CobraFlags.READY_TO_CALL_DEVICE_STATUS) {
								appClass.setLogOnDevice("READY_TO_CALL_DEVICE_STATUS (timer)");
								cobra.ReqDeviceStatus();
								continue;
							}

							// 2nd Priority
							if (appClass.getFlagFirmwareUpate() == CobraFlags.READY_TO_CALL_FIRMWARE_DATA) {
								appClass.setLogOnDevice("READY_TO_CALL_FIRMWARE_DATA");
								cobra.VCOM_REQ_DEVICE_INFO();

								appClass.setFlagModuleUpate(CobraFlags.MODULE_DATA_NOT_READY_TO_ACKNOWLEDGE);
								continue;
							}

							// 3rd Priority
							if (appClass.getFlagScriptsUpate() == CobraFlags.READY_TO_CALL_SCRIPTS_DATA) {

								appClass.setFlagScriptsUpate(CobraFlags.SCRIPT_DATA_IS_CALLING);
								cobra.clearModuleLightStatus();
								cobra.AckOwnFlagStatusChange();

								// cobra.getNEXT_SCRIPT_DATA(-1);

								runOnUiThread(new Runnable() {

									public void run() {
										FragmentTransaction ft = (MainActivity.this)
												.getSupportFragmentManager()
												.beginTransaction();
										Fragment prev = (MainActivity.this)
												.getSupportFragmentManager()
												.findFragmentByTag("dialog");
										if (prev != null) {
											ft.remove(prev);
										}
										ft.addToBackStack(null);

										// Create and show the dialog.
										globV.setCobra(cobra);
										DialogScriptLoader dialog = DialogScriptLoader
												.newInstance(2,
														MainActivity.this);

										// static_dialog = dialog;
										// static_ft = ft;

										dialog.show(ft, "dialog");
									}
								});
								continue;
							}

							if (appClass.getFlagScriptsUpate() == CobraFlags.ALL_SCRIPT_DATA_IS_FINISHED) {
								appClass.setFlagScriptsUpate(CobraFlags.SCRIPT_DATA_STOP_BROADCASTING);
								appClass.writeLog("Flag Scripts Update State SCRIPT_DATA_STOP_BROADCASTING : "
										+ CobraFlags.SCRIPT_DATA_STOP_BROADCASTING);

								cobra.displayAllScripts();

								final Intent intent = new Intent(
										appClass.DIALOG_SCRIPT_LISTENER_RECEIVER);
								intent.putExtra(appClass.TAG_EVENT,
										appClass.EVENT_MODULE_FLAG_DATA_CHANGE);
								String msg = "";
								int status = -1;
								if (scriptStatus == 0) {
									status = 4;
									msg = "No Script Found";

								} else if (scriptStatus == 1) {
									status = 2;
									msg = "Successfully loaded scripts.";

								} else if (scriptStatus == 3) {
									status = 3;
									closeAppFromPopup = true;
									msg = "Failed to load script.\nPlease re-start control panel.";
								}

								intent.putExtra("stauts", status);
								intent.putExtra("msg", msg);
								MainActivity.this.sendBroadcast(intent);

								refreshPressed();
								/*
								 * Call Refresh Module Data Refresh on receiving
								 * scripts data. This will allow us to update
								 * modules data on device in a case if we
								 * restart android.
								 */

							}

							if (appClass.getFlagModuleUpate() == CobraFlags.MODULE_DATA_NOT_READY_TO_ACKNOWLEDGE) {
								refreshPressed();
							}
							if (appClass.IsRefreshPressed
									&& appClass.getFlagModuleUpate() == CobraFlags.MODULE_DATA_READY_TO_ACKNOWLEDGE) {
								refreshPressed();
								MainActivity.timer_threshold = 400;

								firstCall = true;
							}
							// 4th Priority
							if (appClass.getFlagModuleUpate() == CobraFlags.READY_TO_CALL_MODULE_DATA) {
								Tries_For_Modules = 0;
								cobra.AckOwnFlagStatusChange(111);
								MainActivity.timer_threshold = 200;

								appClass.setFlagModuleUpate(CobraFlags.WAIT_FOR_REQUESTED_MODULE_DATA);
								appClass.setAN106Logs(System
										.currentTimeMillis()
										+ " Timer: called with -1");
								appClass.setNextModuleId(-1);
								cobra.VCOM_REQ_QUEUED_MODULE_DATA(-1);

							}

							if (appClass.getFlagModuleUpate() == CobraFlags.WAIT_FOR_REQUESTED_MODULE_DATA) {
								Tries_For_Modules++;
								if (Tries_For_Modules % noOfTries == 0) {

									cobra.AckOwnFlagStatusChange(222);

									appClass.setFlagModuleUpate(CobraFlags.READY_TO_CALL_NEXT_MODULE_DATA);

									cobra.getNext_QUEUED_MODULE_DATA();
									cobra.AckOwnFlagStatusChange(999);
								}
							}

							if (appClass.getFlagModuleUpate() == CobraFlags.READY_TO_CALL_NEXT_MODULE_DATA) {

								// if (firstCall) {
								// firstCall = false;

								Tries_For_Modules = 0;
								cobra.AckOwnFlagStatusChange(333);
								// appClass.setAN168Log("CHECKER : READY_TO_CALL_NEXT_MODULE_DATA");
								cobra.getNext_QUEUED_MODULE_DATA();

								appClass.setFlagModuleUpate(CobraFlags.WAIT_FOR_REQUESTED_MODULE_DATA);
								// }
							}

							// if (appClass.getFlagDeviceUpate() ==
							// CobraFlags.DEVICE_STATUS_READY_TO_ACKNOWLEDGE
							// && appClass.getFlagModuleUpate() ==
							// CobraFlags.READY_TO_CALL_NEXT_MODULE_DATA) {
							// {
							// cobra.AckOwnFlagStatusChange(111);
							// cobra.getNext_QUEUED_MODULE_DATA();
							// }
							// }

							// 5th Priority
							if (appClass.getFlagFireCuesUpate() == CobraFlags.READY_TO_CALL_FIRECUE_DATA) {
								appClass.setLogOnDevice("READY_TO_CALL_FIRECUE_DATA");
								cobra.VCOM_REQ_QUEUED_FIREDCUES_DATA(-1);
							}

							if (appClass.getFlagDeviceUpate() == CobraFlags.DEVICE_STATUS_READY_TO_ACKNOWLEDGE
									&& appClass.getFlagFireCuesUpate() == CobraFlags.READY_TO_CALL_NEXT_FIRECUE_DATA) {
								cobra.getNext_FIRECUE_DATA();
								appClass.setLogOnDevice("READY_TO_CALL_NEXT_FIRECUE_DATA");
							}
						} else {
							appClass.setLogOnDevice("Cobra is NULL");
						}
					} else {
						appClass.setLogOnDevice("Connection is not established YET");
					}
				} catch (InterruptedException e) {
					appClass.setLogOnDevice("INTERRUPTED EXCEPTION IN TIMER : "
							+ e.getMessage());
					Thread.currentThread().interrupt();
				} catch (Exception e) {
					appClass.setLogOnDevice("EXCEPTION IN TIMER : "
							+ e.getMessage());
				}
			}

		}
	};

	private Boolean ListenScriptTimer = false;
	FrameLayout moduleFram;
	FrameLayout showcontrolFram;
	FrameLayout buckectfiringFram;

	Boolean bucketFiringLoad_firsTime = true;

	public static Boolean showControlCheck = false;

	public static Handler UIhandler;

	public static Handler UIhandlerDemo;

	public PersistentHeader fragPersistentHeader = null;

	public void mainViewInit() {
		if (!demoMode) {
			cobra.AddModuleListener(cobraEventListener);
		}

		// persistentHeader = new PersistentHeader(this, cobra);
		// buttonOverlay = new ButtonOverlay(this, this);
		// moduleList = new ModuleList(this, cobra);
		// moduleList.removeAllModules();

		// button overlay layout pre
		// RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
		// RelativeLayout.LayoutParams.MATCH_PARENT,
		// RelativeLayout.LayoutParams.WRAP_CONTENT);
		// lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

		// buttonOverlay.setLayoutParams(lp);

		setContentView(R.layout.activity_main);
		contentFrame = (RelativeLayout) findViewById(R.id.content_frame);
		// headerFrame = (FrameLayout) findViewById(R.id.header_frame);

		moduleFram = (FrameLayout) findViewById(R.id.moduleList_frame);
		showcontrolFram = (FrameLayout) findViewById(R.id.showcontrols_frame);
		buckectfiringFram = (FrameLayout) findViewById(R.id.bucketfiring_frame);

		// headerFrame.addView(persistentHeader);
		fragPersistentHeader = new PersistentHeader();
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.replace(R.id.header_frame, fragPersistentHeader).commit();

		// module list fragment
		contentFrame.setVisibility(View.GONE);
		showcontrolFram.setVisibility(View.GONE);
		buckectfiringFram.setVisibility(View.GONE);
		moduleFram.setVisibility(View.VISIBLE);

		// origional module list
		ModuleList moduleList = new ModuleList();
		FragmentManager fm1 = getSupportFragmentManager();
		FragmentTransaction transaction1 = fm1.beginTransaction();
		transaction1.replace(R.id.moduleList_frame, moduleList).commit();
		UIhandler = moduleList.getHandler();

		// showcontrol we call show control fragment when scripts are loaded
		// completely
		// showcontrol is calling in DialogScriptLoader.java

		// And bucket firing fragment we load it at first time click

		drawerList = new NavDrawerItem[4];
		drawerList[0] = NavMenuSection.create(0, "GENERAL");
		drawerList[1] = NavMenuItem.create(1, "Device List", this);
		drawerList[2] = NavMenuItem.create(2, "Show Controls", this);
		//drawerList[3] = NavMenuItem.create(3, "Bucket Firing", this);
		// drawerList[4] = NavMenuItem.create(4, "Test Commands", this);
		drawerList[3] = NavMenuItem.create(5, "About", this);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.LEFT);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawerList.setAdapter(new NavDrawerAdapter(MainActivity.this,
				R.layout.navdrawer_item, drawerList));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		selectItem(VIEW_MODULELIST);

	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private int menual_pos = -1;

	private void selectItem(int position) {

		if (menual_pos != -1 && position == 3) {// this condition will change if
												// there more or less items in
												// drawer list
			mDrawerList.setItemChecked(menual_pos, true);
		} else {
			menual_pos = position;
		}

		// mAdapter.notifyDataSetChanged();

		// update selected item and title, then close the drawer
		// mDrawerList.setItemChecked(position, true);
		mDrawerLayout.closeDrawer(mDrawerList);
		updateContentFrame(position);

	}

	// this is function that is called on side drawer click
	// and this function take position of drawer list in when we click on
	private void updateContentFrame(int pos) {
		// this.contentFrame.removeAllViews();

		// for temp
		if (pos == 3)
			pos = 5;

		switch (pos) {

		// Device list page will open in this case
		case VIEW_MODULELIST:
			// cobra.ReqModuleData();
			this.cobra.RemoveBucketFiringListener();
			cobra.removeCommandEventListener();
			this.contentFrame.removeAllViews();
			if (globV.getHeaderTitle() != null)
				globV.getHeaderTitle().setText("Device List");
			contentFrame.setVisibility(View.GONE);

			showcontrolFram.setVisibility(View.GONE);
			buckectfiringFram.setVisibility(View.GONE);
			moduleFram.setVisibility(View.VISIBLE);

			showControlCheck = false;

			// contentFrame.addView(moduleList);
			// contentFrame.addView(buttonOverlay);
			break;

		// Show Controls page will open in this case
		case VIEW_SHOWCONTROLS:
			cobra.removeCommandEventListener();
			try {
				if (appClass.getFlagScriptsUpate() >= CobraFlags.ALL_SCRIPT_DATA_IS_FINISHED) {
					appClass.setLogOnDevice("ALL_SCRIPT_DATA_IS_FINISHED");
					// if (scriptStatus == 1) {
					this.cobra.RemoveBucketFiringListener();
					this.contentFrame.removeAllViews();
					if (globV.getHeaderTitle() != null)
						globV.getHeaderTitle().setText("Show Controls");

					contentFrame.setVisibility(View.GONE);

					moduleFram.setVisibility(View.GONE);
					buckectfiringFram.setVisibility(View.GONE);
					showcontrolFram.setVisibility(View.VISIBLE);

					showControlCheck = true;
					// } else {
					//
					// Toast.makeText(
					// MainActivity.this,
					// "There was an error in scripts while loading. Please reload scripts",
					// Toast.LENGTH_LONG).show();
					//
					// }
				} else
					Toast.makeText(MainActivity.this,
							"Please wait for the scripts to load",
							Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				Toast.makeText(MainActivity.this,
						"Exception in MainActivity: " + e.getMessage(),
						Toast.LENGTH_LONG).show();
			}
			break;

		// Bucket Firing page will open in this case
		case VIEW_BUCKETFIRING:
			cobra.removeCommandEventListener();
			this.contentFrame.removeAllViews();
			if (globV.getHeaderTitle() != null)
				globV.getHeaderTitle().setText("Bucket Firing New");
			// bucketFiring = new BucketFiring(this, this, cobra);

			// bucketFiringNew = new BucketFiringNew(this, this, cobra);
			// this.contentFrame.addView(bucketFiringNew);

			if (bucketFiringLoad_firsTime) {
				BucketFiringNew bucketFiringNew = new BucketFiringNew();
				FragmentManager fm2 = getSupportFragmentManager();
				FragmentTransaction transaction2 = fm2.beginTransaction();
				transaction2.replace(R.id.bucketfiring_frame, bucketFiringNew)
						.commit();
				bucketFiringLoad_firsTime = false;
			}

			contentFrame.setVisibility(View.GONE);

			moduleFram.setVisibility(View.GONE);
			showcontrolFram.setVisibility(View.GONE);
			buckectfiringFram.setVisibility(View.VISIBLE);

			// new DBUpdateTask().execute();

			showControlCheck = false;

			break;

		// Test Commands page will open in this case
		case VIEW_TESTCOMMANDS:
			try {
				this.cobra.RemoveBucketFiringListener();
				this.contentFrame.removeAllViews();
				if (globV.getHeaderTitle() != null)
					globV.getHeaderTitle().setText("Test Commands");
				test_commands = new TestCommands(this, this, cobra);
				this.contentFrame.addView(test_commands);
			} catch (Exception e) {
				// TODO: handle exception
			}
			showControlCheck = false;

			contentFrame.setVisibility(View.VISIBLE);

			moduleFram.setVisibility(View.GONE);
			showcontrolFram.setVisibility(View.GONE);
			buckectfiringFram.setVisibility(View.GONE);
			break;

		// About page will open in this case
		case ABOUT:

			FragmentTransaction ft = (MainActivity.this)
					.getSupportFragmentManager().beginTransaction();

			Fragment prev = (MainActivity.this).getSupportFragmentManager()
					.findFragmentByTag("dialog");

			if (prev != null) {
				ft.remove(prev);
			}

			ft.addToBackStack(null);

			String deviceName = (cobra.getDeviceType() == 1) ? "18R2" : "-";
			String firmware = String.format("v%d.%d.%d",
					cobra.getDeviceVersion().major,
					cobra.getDeviceVersion().minor,
					cobra.getDeviceVersion().revision);
			String releaseDate = "00-00-0000";
			DialogFragment newFragment = DialogAbout.newInstance(2, deviceName,
					firmware, releaseDate);
			newFragment.show(ft, "dialog");
			showControlCheck = false;

			contentFrame.setVisibility(View.VISIBLE);

			// moduleFram.setVisibility(View.GONE);
			// showcontrolFram.setVisibility(View.GONE);
			// buckectfiringFram.setVisibility(View.GONE);
			break;
		default:
			showControlCheck = false;

			contentFrame.setVisibility(View.VISIBLE);

			moduleFram.setVisibility(View.GONE);
			showcontrolFram.setVisibility(View.GONE);
			buckectfiringFram.setVisibility(View.GONE);
			break;
		}
	}

	public void UpdateDatabase() {
		HashMap<Integer, ModuleViews> list = globV.getModuleList();
		Iterator i = list.keySet().iterator();

		// while (i.hasNext()) {
		// int modID = Integer.parseInt(i.next().toString());
		// ModuleViews value = list.get(modID);
		//
		// try {
		// if (value != null) {
		// DBHelper.insertModule(MainActivity.this, "" + modID,
		// value.getModuleChannel(), "");
		// }
		// } catch (Exception e) {
		//
		// }
		// }
	}

	public static ArrayList<Integer> moduleIDs = null;
	// public static Boolean ClearModuleList = false;
	public static boolean IsModuleTimerRunning = false;
	public static boolean KeepCallingModules = true;

	private void ClearModuleObjects() {
		moduleIDs = null;
		IsModuleTimerRunning = false;
		KeepCallingModules = true;
	}

	// this listener receive data about modules.
	ICobraEventListener cobraEventListener = new ICobraEventListener() {
		@Override
		public void onDeviceDataChange(final CobraEvent event) {
			try {
				// when modID==-1 it means 18h2 have no ore modules in que

				if (onModuleInformationUpdate == null)
					onModuleInformationUpdate = globV
							.getonModuleInformationUpdate();

				switch (event.getEventType()) {

				// This case is called when modules data is change or update
				case Cobra.EVENT_TYPE_MODULE_DATA_CHANGE:

					appClass.setFlagModuleUpate(CobraFlags.READY_TO_CALL_NEXT_MODULE_DATA);

					cobra.AckOwnFlagStatusChange();
					Log.v(TAG, "NEW MODULE");

					final ModuleDataTag moduleTag = (ModuleDataTag) event
							.getCobraEventData();

					if (moduleTag.modID == 65534) {// -2 // it means we have to
													// reset module data
						moduleTag.modID = -2;
					}

					appClass.setNextModuleId(moduleTag.modID);

					appClass.last_module_called = System.currentTimeMillis();

					Boolean IsModuleIdFound = false;
					if (moduleIDs == null) {
						moduleIDs = new ArrayList<Integer>();

					}
					if (moduleTag.modID == -2) {
						moduleIDs.clear();
					}

					// First time we maintain modulesID list
					// And adding modules one by one as it they come from 18H2
					// Before adding each module we check if it is not already
					// added to list
					for (int i = 0; i < moduleIDs.size(); i++) {

						if (moduleIDs.get(i) == moduleTag.modID) {
							IsModuleIdFound = true;
							break;
						}
					}
					if (!IsModuleIdFound) {
						moduleIDs.add(moduleTag.modID);
					}

					if (moduleTag.modID == -2) {
						// moduleList.removeAllModules();
						onModuleInformationUpdate.onRemoveAllModule();
					}

					ArrayList<String> lightStatus = cobra
							.getModuleLightsStatus(moduleTag.currentChannel);

					// appClass.writeIntoLog(lightStatus,
					// "Channel: "+moduleTag.currentChannel);

					// //

					// appClass.setAN188Log("chanel: " +
					// moduleTag.currentChannel
					// + " |Liht status: " + lightStatus);

					// //

					// moduleList.updateModuleRow(moduleTag, lightStatus);
					if (moduleTag.modID != -2) {
						onModuleInformationUpdate.onModuleStatusUpdate(
								moduleTag, lightStatus);
					}
					SendHeaderBroadcast(moduleTag.modID, moduleTag.armed,
							moduleTag.currentChannel);
					//

					// temp copy of modules ids
					temp_moduleIds = new ArrayList<Integer>();
					temp_moduleIds = moduleIDs;

					// Here is calling StartModulesTimer(); to update modules
					// and send to ModuleList.java
					if (!IsModuleTimerRunning) {
						IsModuleTimerRunning = true;

						SendModulesData();

						// StartModulesTimer();
					}

					// if (KeepCallingModules) {
					// if (appClass.getFlagModuleUpate() ==
					// CobraFlags.READY_TO_CALL_NEXT_MODULE_DATA) {
					// cobra.AckOwnFlagStatusChange(444);
					// appClass.setFlagModuleUpate(CobraFlags.WAIT_FOR_REQUESTED_MODULE_DATA);
					// appClass.setAN106Logs(System.currentTimeMillis()
					// + " MainActivity: called next module data");
					// appClass.setAN106NewLog("Call again->");
					// //
					// appClass.setAN168Log("CALLING Next_QUEUED_MODULE_DATA() ");
					// cobra.getNext_QUEUED_MODULE_DATA();
					// }
					// }

					break;

				case Cobra.EVENT_TYPE_DISCONNECTED: {// DEMO
					showDialogDisconnectNoDevice();
				}

					// // appClass.createReadingLogOnDevice();
					// AlertDialog.Builder builder = new AlertDialog.Builder(
					// MainActivity.this);
					// // PersistentHeader.KeepExecute = false;
					// builder.setTitle("Disconnected");
					// builder.setCancelable(false);
					// builder.setMessage("The 18R2 is disconnected. Please press OK, wait 10 seconds, and re-connect the 18R2.");
					// builder.setPositiveButton("OK",
					// new DialogInterface.OnClickListener() {
					// @Override
					// public void onClick(DialogInterface dialog,
					// int which) {
					// appClass.IsAppForcedClosed = false;
					// finish();
					// }
					// });
					//
					// if (IsAppActive)
					// builder.show();
					// // appClass.createReadingLogOnDevice(
					// // "MainActivity",
					// //
					// "The 18R2 is disconnected. Please press OK, wait 10 seconds, and re-connect the 18R2.");

					break;
				//
				// case Cobra.EVENT_TYPE_SCRIPT_PING:
				// appClass.createReadingLogOnDevice("MainActivity",
				// "EVENT_TYPE_SCRIPT_PING: "
				// + Cobra.EVENT_TYPE_SCRIPT_PING);
				// ScriptPingTag pingTag = (ScriptPingTag) event
				// .getCobraEventData();
				// Log.i(TAG, String.format("PLAYING: S: %d E: %d Time: %d",
				// pingTag.scriptIndex, pingTag.eventIndex,
				// pingTag.elapsedTime));
				// break;
				//
				// case Cobra.EVENT_CLEAR_MODULES:
				// appClass.createReadingLogOnDevice("MainActivity",
				// "EVENT_CLEAR_MODULES: " + Cobra.EVENT_CLEAR_MODULES);
				// moduleList.removeAllModules();
				// // persistentHeader.updateCobraDeviceStatus();
				// persistentHeader.updateModulesArmStatus();
				// moduleList.updateActiveStatus();
				// break;
				case Cobra.EVENT_ACK_DEVICE_MODULE_FIRECUE_DATA_CHANGE:
					if (appClass.getFlagDeviceUpate() == CobraFlags.DEVICE_STATUS_READY_TO_ACKNOWLEDGE) {
						cobra.AckStatusChange(false, 1, 0, 0);
						appClass.setFlagDeviceUpate(CobraFlags.READY_TO_CALL_DEVICE_STATUS);
						cobra.AckOwnFlagStatusChange();
					}
					if (appClass.getFlagModuleUpate() > CobraFlags.MODULE_DATA_READY_TO_ACKNOWLEDGE) {
						// appClass.setAN106NewLog("Line: " + 953);
						appClass.setFlagModuleUpate(CobraFlags.MODULE_DATA_READY_TO_ACKNOWLEDGE);
						MainActivity.timer_threshold = 400;
					}
					if (appClass.getFlagModuleUpate() == CobraFlags.MODULE_DATA_READY_TO_ACKNOWLEDGE) {
						cobra.AckStatusChange(false, 0, 1, 0);

						appClass.setFlagModuleUpate(CobraFlags.READY_TO_CALL_MODULE_DATA);
						cobra.AckOwnFlagStatusChange();

						if (appClass.getFlagModuleUpate() == CobraFlags.READY_TO_CALL_MODULE_DATA) {
							Tries_For_Modules = 0;
							MainActivity.timer_threshold = 200;
							cobra.AckOwnFlagStatusChange(111);

							appClass.setFlagModuleUpate(CobraFlags.WAIT_FOR_REQUESTED_MODULE_DATA);
							appClass.setAN106Logs(System.currentTimeMillis()
									+ " Timer: called with -1");
							appClass.setNextModuleId(-1);
							cobra.VCOM_REQ_QUEUED_MODULE_DATA(-1);

						}

					}

					if (appClass.getFlagFireCuesUpate() == CobraFlags.FIRECUE_DATA_READY_TO_ACKNOWLEDGE) {
						cobra.AckStatusChange(false, 0, 0, 1);
						appClass.writeLog("Flag Firecues Update State READY_TO_CALL_FIRECUE_DATA : "
								+ CobraFlags.READY_TO_CALL_FIRECUE_DATA);
						appClass.setFlagFireCuesUpate(CobraFlags.READY_TO_CALL_FIRECUE_DATA);
						cobra.AckOwnFlagStatusChange();
					}
					break;

				case Cobra.EVENT_ACK_DEVICE_MODULE_DATA_CHANGE:
					if (appClass.getFlagDeviceUpate() == CobraFlags.DEVICE_STATUS_READY_TO_ACKNOWLEDGE) {
						cobra.AckStatusChange(false, 1, 0, 0);
						appClass.writeLog("Flag Device Update State READY_TO_CALL_DEVICE_STATUS : "
								+ CobraFlags.READY_TO_CALL_DEVICE_STATUS);
						appClass.setFlagDeviceUpate(CobraFlags.READY_TO_CALL_DEVICE_STATUS);
						cobra.AckOwnFlagStatusChange();
					}
					if (appClass.getFlagModuleUpate() > CobraFlags.MODULE_DATA_READY_TO_ACKNOWLEDGE) {

						appClass.setFlagModuleUpate(CobraFlags.MODULE_DATA_READY_TO_ACKNOWLEDGE);
						MainActivity.timer_threshold = 400;
					}
					if (appClass.getFlagModuleUpate() == CobraFlags.MODULE_DATA_READY_TO_ACKNOWLEDGE) {
						cobra.AckStatusChange(false, 0, 1, 0);

						appClass.writeLog("Flag Module Update State READY_TO_CALL_MODULE_DATA : "
								+ CobraFlags.READY_TO_CALL_MODULE_DATA);
						appClass.setFlagModuleUpate(CobraFlags.READY_TO_CALL_MODULE_DATA);
						cobra.AckOwnFlagStatusChange();
						// appClass.setModuleLogOnDevice("READY_TO_CALL_MODULE_DATA");
						if (appClass.getFlagModuleUpate() == CobraFlags.READY_TO_CALL_MODULE_DATA) {
							Tries_For_Modules = 0;
							cobra.AckOwnFlagStatusChange(111);
							MainActivity.timer_threshold = 200;

							appClass.setFlagModuleUpate(CobraFlags.WAIT_FOR_REQUESTED_MODULE_DATA);
							appClass.setAN106Logs(System.currentTimeMillis()
									+ " Timer: called with -1");
							appClass.setNextModuleId(-1);
							cobra.VCOM_REQ_QUEUED_MODULE_DATA(-1);

						}
					}
					break;

				case Cobra.EVENT_ACK_DEVICE_FIRECUE_DATA_CHANGE:
					if (appClass.getFlagDeviceUpate() == CobraFlags.DEVICE_STATUS_READY_TO_ACKNOWLEDGE) {
						cobra.AckStatusChange(false, 1, 0, 0);
						appClass.writeLog("Flag Device Update State : "
								+ CobraFlags.READY_TO_CALL_DEVICE_STATUS);
						appClass.writeLog("Flag Device Update State READY_TO_CALL_DEVICE_STATUS : "
								+ CobraFlags.READY_TO_CALL_DEVICE_STATUS);
						appClass.setFlagDeviceUpate(CobraFlags.READY_TO_CALL_DEVICE_STATUS);
						cobra.AckOwnFlagStatusChange();
					}
					if (appClass.getFlagFireCuesUpate() == CobraFlags.FIRECUE_DATA_READY_TO_ACKNOWLEDGE) {
						cobra.AckStatusChange(false, 0, 0, 1);
						appClass.setFlagFireCuesUpate(CobraFlags.READY_TO_CALL_FIRECUE_DATA);
						appClass.writeLog("Flag Firecues Update State READY_TO_CALL_FIRECUE_DATA : "
								+ CobraFlags.READY_TO_CALL_FIRECUE_DATA);
						cobra.AckOwnFlagStatusChange();
						break;
					}

				case Cobra.EVENT_ACK_MODULE_FIRECUE_DATA_CHANGE:
					if (appClass.getFlagModuleUpate() > CobraFlags.MODULE_DATA_READY_TO_ACKNOWLEDGE) {

						appClass.setFlagModuleUpate(CobraFlags.MODULE_DATA_READY_TO_ACKNOWLEDGE);
						MainActivity.timer_threshold = 400;
					}
					if (appClass.getFlagModuleUpate() == CobraFlags.MODULE_DATA_READY_TO_ACKNOWLEDGE) {
						cobra.AckStatusChange(false, 0, 1, 0);
						appClass.writeLog("Flag Module Update State READY_TO_CALL_MODULE_DATA : "
								+ CobraFlags.READY_TO_CALL_MODULE_DATA);

						appClass.setFlagModuleUpate(CobraFlags.READY_TO_CALL_MODULE_DATA);
						cobra.AckOwnFlagStatusChange();

						if (appClass.getFlagModuleUpate() == CobraFlags.READY_TO_CALL_MODULE_DATA) {
							Tries_For_Modules = 0;
							cobra.AckOwnFlagStatusChange(111);
							MainActivity.timer_threshold = 200;

							appClass.setFlagModuleUpate(CobraFlags.WAIT_FOR_REQUESTED_MODULE_DATA);
							appClass.setAN106Logs(System.currentTimeMillis()
									+ " Timer: called with -1");
							appClass.setNextModuleId(-1);
							cobra.VCOM_REQ_QUEUED_MODULE_DATA(-1);

						}
						// appClass.setModuleLogOnDevice("READY_TO_CALL_MODULE_DATA");
					}
					if (appClass.getFlagFireCuesUpate() == CobraFlags.FIRECUE_DATA_READY_TO_ACKNOWLEDGE) {
						cobra.AckStatusChange(false, 0, 0, 1);
						appClass.setFlagFireCuesUpate(CobraFlags.READY_TO_CALL_FIRECUE_DATA);
						appClass.writeLog("Flag Firecues Update State READY_TO_CALL_FIRECUE_DATA : "
								+ CobraFlags.READY_TO_CALL_FIRECUE_DATA);
						cobra.AckOwnFlagStatusChange();
					}
					break;

				case Cobra.EVENT_ACK_DEVICE_DATA_CHANGE:
					if (appClass.getFlagDeviceUpate() == CobraFlags.DEVICE_STATUS_READY_TO_ACKNOWLEDGE) {
						cobra.AckStatusChange(false, 1, 0, 0);
						appClass.writeLog("Flag Device Update State READY_TO_CALL_DEVICE_STATUS : "
								+ CobraFlags.READY_TO_CALL_DEVICE_STATUS);
						appClass.setFlagDeviceUpate(CobraFlags.READY_TO_CALL_DEVICE_STATUS);
						cobra.AckOwnFlagStatusChange();
					}
					break;

				case Cobra.EVENT_ACK_MODULE_DATA_CHANGE:
					if (appClass.getFlagModuleUpate() > CobraFlags.MODULE_DATA_READY_TO_ACKNOWLEDGE) {

						appClass.setFlagModuleUpate(CobraFlags.MODULE_DATA_READY_TO_ACKNOWLEDGE);
						MainActivity.timer_threshold = 400;
					}
					if (appClass.getFlagModuleUpate() == CobraFlags.MODULE_DATA_READY_TO_ACKNOWLEDGE) {
						cobra.AckStatusChange(false, 0, 1, 0);
						appClass.writeLog("Flag Module Update State READY_TO_CALL_MODULE_DATA : "
								+ CobraFlags.READY_TO_CALL_MODULE_DATA);

						appClass.setFlagModuleUpate(CobraFlags.READY_TO_CALL_MODULE_DATA);
						MainActivity.timer_threshold = 400;
						cobra.AckOwnFlagStatusChange();
						// appClass.setModuleLogOnDevice("READY_TO_CALL_MODULE_DATA");

						if (appClass.getFlagModuleUpate() == CobraFlags.READY_TO_CALL_MODULE_DATA) {
							Tries_For_Modules = 0;
							cobra.AckOwnFlagStatusChange(111);
							MainActivity.timer_threshold = 200;

							appClass.setFlagModuleUpate(CobraFlags.WAIT_FOR_REQUESTED_MODULE_DATA);
							appClass.setAN106Logs(System.currentTimeMillis()
									+ " Timer: called with -1");
							appClass.setNextModuleId(-1);
							cobra.VCOM_REQ_QUEUED_MODULE_DATA(-1);

						}
					}
					break;

				case Cobra.EVENT_ACK_FIRECUE_DATA_CHANGE:
					if (appClass.getFlagFireCuesUpate() == CobraFlags.FIRECUE_DATA_READY_TO_ACKNOWLEDGE) {
						cobra.AckStatusChange(false, 0, 0, 1);
						appClass.setFlagFireCuesUpate(CobraFlags.READY_TO_CALL_FIRECUE_DATA);
						appClass.writeLog("Flag Firecues Update State READY_TO_CALL_FIRECUE_DATA : "
								+ CobraFlags.READY_TO_CALL_FIRECUE_DATA);
						cobra.AckOwnFlagStatusChange();
					}
					break;
				default:
					break;
				}
			} catch (Exception e) {
				int i = 0;
				i++;
			}
		}
	};
	ArrayList<Integer> temp_moduleIds = null;

	private void SendModulesData() {

		// IsModuleTimerRunning = false;

		// we send message call with UIhandler to ModuleList.java for to show
		// module in devices list
		{
			Message msgObj = UIhandler.obtainMessage();
			Bundle b = new Bundle();
			b.putIntegerArrayList("modules_ids", moduleIDs);
			msgObj.setData(b);
			UIhandler.sendMessage(msgObj);
		}
	}

	private void StartModulesTimer() {

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				KeepCallingModules = false;
				appClass.setModuleLogOnDevice("6 : Stop Calling Next Module");
			}
		}, 1000);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// IsModuleTimerRunning = false;
				if (moduleIDs != null) {
					String temp = "";
					for (int i = 0; i < moduleIDs.size(); i++) {
						temp += moduleIDs.get(i) + " , ";
					}
					SendHeaderTextBroadcast();

					// here making copy of moduleIDs
					temp_moduleIds = new ArrayList<Integer>();
					temp_moduleIds = moduleIDs;

					Intent intent = new Intent(
							appClass.RECIEVER_ADAPTER_MODULE_LIST);
					intent.putExtra("modIDs", moduleIDs);
					sendBroadcast(intent);
				} else {
					// appClass.setAN106Log("MODULES ID LIST FOUND NULL");
				}
			}
		}, 1500);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				IsModuleTimerRunning = false;
				if (temp_moduleIds != null) {
					String temp = "";
					for (int i = 0; i < temp_moduleIds.size(); i++) {
						temp += temp_moduleIds.get(i) + " , ";
					}
					// appClass.setAN106Log("2nd Broadcast MODULE UI DATA SENT 2nd Time: IDs= "
					// + temp);
					Intent intent = new Intent(
							appClass.RECIEVER_ADAPTER_MODULE_LIST);
					intent.putExtra("modIDs", temp_moduleIds);
					// sendBroadcast(intent);
				} else {
					// appClass.setAN106Log("2nd Broadcast MODULES ID LIST FOUND NULL");
				}
			}
		}, 1600);

	}

	public static Boolean IsAppActive = false;

	@Override
	protected void onStop() {
		// appClass.createReadingLogOnDevice("MainActivity", TAG
		// + " : Application is stopping");
		Log.e(TAG, "Application is stopping");
		super.onStop();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		IsAppActive = false;
		if (AckThread != null)
			AckThread.interrupt();

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		IsAppActive = true;

		if (cobra == null || !cobra.isConnected()) {
			// appClass.SaveNullLastLoggedTime((Context) MainActivity.this);
			isFirstTime = Connectivity.appFirstTime(MainActivity.this);
			On_Create(isFirstTime);
			return;
		} else {
			if (AckThread != null)
				try {
					AckThread = new Thread(checker);
					AckThread.start();
				} catch (Exception e) {
				}
		}

	}

	@Override
	protected void onDestroy() {
		// Log.e(TAG, "Application is being destroyed!");
		super.onDestroy();
		try {
			AckThread.interrupt();
		} catch (Exception e) {

		}

		if (CreateTimeLog) {
			// Toast.makeText(this, "Time Logged", Toast.LENGTH_SHORT).show();
			appClass.SaveLastLoggedTime(MainActivity.this);
		}

		IsAppActive = false;
		closeDevice();
		if (service_intent != null)
			stopService(service_intent);
		else
			stopService(new Intent(MainActivity.this, ReaderService.class));
	}

	private void closeDevice() {
		if (cobra != null) {
			try {
				// unregisterReceiver(AcknowledgementReceiver);
				// // persistentHeader.UnregisterReciever();
				// moduleList.removeAllModules();
				onModuleInformationUpdate.onRemoveAllModule();
			} catch (Exception e) {

			}
			// Toast.makeText(this, "device closed", Toast.LENGTH_SHORT).show();
			Log.i("MainActivity", "Devce Closed");
			if (appClass.onEventChange != null) {
				Log.i("MainActivity", "OnEventChange Fired");
				appClass.onEventChange.onEventChange();
			} else
				Log.i("MainActivity", "OnEventChange is NULL");
		}
	}

	public static int counter = 0;

	private void SendHeaderBroadcast(int modID, Boolean IsArmed,
			int currentChannel) {
		Intent intent = new Intent(appClass.RECIEVER_PERSISTENT_HEADER);
		intent.putExtra(appClass.TAG_EVENT,
				PersistentHeader.EVENT_TYPE_HEADER_INFO_CHANGE);
		intent.putExtra("modID", modID);
		intent.putExtra("Armed", IsArmed);
		intent.putExtra("currentChannel", currentChannel);
		// WAQAR remove this comment below if you need
		// sendBroadcast(intent);
	}

	private void SendHeaderTextBroadcast() {
		Intent intent = new Intent(appClass.RECIEVER_PERSISTENT_HEADER);
		intent.putExtra(appClass.TAG_EVENT,
				PersistentHeader.EVENT_TYPE_HEADER_TEXT_INFO_CHANGE);
		sendBroadcast(intent);
	}

	private int temp = 0;

	// this is no of tries
	private int noOfTries = 2;

	public void refreshPressed() {

		cobra.refreshModuleData();

		appClass.IsRefreshPressed = true;

	}

	public void helpPressed() {
		// TODO Auto-generated method stub

	}

	public boolean deviceIsRegistered() {
		return true;
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting();
	}

	public boolean appActivated() {
		if (debugMode) {
			return true;
		} else {

			try {
				String text = "";
				SharedPreferences sharedpreferences = getSharedPreferences(
						"KEYPREFERENCES", Context.MODE_PRIVATE);

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

	public String getDeviceID() {
		return "794cce6e305f3dac";
	}

	// BroadcastReceiver AcknowledgementReceiver = new BroadcastReceiver() {
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// // TODO Auto-generated method stub
	// try {
	//
	// int event = -1;
	// if (intent.hasExtra(appClass.TAG_EVENT))
	// event = intent.getExtras().getInt(appClass.TAG_EVENT);
	// if (cobra != null
	// && appClass.getFlagScriptsUpate() !=
	// CobraFlags.READY_TO_CALL_SCRIPTS_DATA
	// && appClass.getFlagScriptsUpate() !=
	// CobraFlags.ALL_SCRIPT_DATA_IS_FINISHED) {
	// appClass.setLogOnDevice("Broadcast Passed to SWITCH");
	// }
	// if (cobra != null
	// && appClass.getFlagScriptsUpate() !=
	// CobraFlags.READY_TO_CALL_SCRIPTS_DATA
	// && appClass.getFlagScriptsUpate() !=
	// CobraFlags.ALL_SCRIPT_DATA_IS_FINISHED)
	// switch (event) {
	//
	// case Cobra.EVENT_ACK_DEVICE_MODULE_FIRECUE_DATA_CHANGE:
	// if (appClass.getFlagDeviceUpate() ==
	// CobraFlags.DEVICE_STATUS_READY_TO_ACKNOWLEDGE) {
	// cobra.AckStatusChange(false, 1, 0, 0);
	// appClass.writeLog("Flag Device Update State READY_TO_CALL_DEVICE_STATUS : "
	// + CobraFlags.READY_TO_CALL_DEVICE_STATUS);
	// appClass.setFlagDeviceUpate(CobraFlags.READY_TO_CALL_DEVICE_STATUS);
	// cobra.AckOwnFlagStatusChange();
	// }
	// if (appClass.getFlagModuleUpate() ==
	// CobraFlags.MODULE_DATA_READY_TO_ACKNOWLEDGE) {
	// cobra.AckStatusChange(false, 0, 1, 0);
	// appClass.setFlagModuleUpate(CobraFlags.READY_TO_CALL_MODULE_DATA);
	// appClass.writeLog("Flag Module Update State READY_TO_CALL_MODULE_DATA : "
	// + CobraFlags.READY_TO_CALL_MODULE_DATA);
	// cobra.AckOwnFlagStatusChange();
	// }
	// if (appClass.getFlagFireCuesUpate() ==
	// CobraFlags.FIRECUE_DATA_READY_TO_ACKNOWLEDGE) {
	// cobra.AckStatusChange(false, 0, 0, 1);
	// appClass.setFlagFireCuesUpate(CobraFlags.READY_TO_CALL_FIRECUE_DATA);
	// appClass.writeLog("Flag Firecues Update State READY_TO_CALL_FIRECUE_DATA : "
	// + CobraFlags.READY_TO_CALL_FIRECUE_DATA);
	// cobra.AckOwnFlagStatusChange();
	// }
	// break;
	//
	// case Cobra.EVENT_ACK_DEVICE_MODULE_DATA_CHANGE:
	// if (appClass.getFlagDeviceUpate() ==
	// CobraFlags.DEVICE_STATUS_READY_TO_ACKNOWLEDGE) {
	// cobra.AckStatusChange(false, 1, 0, 0);
	// appClass.writeLog("Flag Device Update State READY_TO_CALL_DEVICE_STATUS : "
	// + CobraFlags.READY_TO_CALL_DEVICE_STATUS);
	// appClass.setFlagDeviceUpate(CobraFlags.READY_TO_CALL_DEVICE_STATUS);
	// cobra.AckOwnFlagStatusChange();
	// }
	// if (appClass.getFlagModuleUpate() ==
	// CobraFlags.MODULE_DATA_READY_TO_ACKNOWLEDGE) {
	// cobra.AckStatusChange(false, 0, 1, 0);
	// appClass.setFlagModuleUpate(CobraFlags.READY_TO_CALL_MODULE_DATA);
	// appClass.writeLog("Flag Module Update State READY_TO_CALL_MODULE_DATA : "
	// + CobraFlags.READY_TO_CALL_MODULE_DATA);
	// cobra.AckOwnFlagStatusChange();
	// }
	// break;
	//
	// case Cobra.EVENT_ACK_DEVICE_FIRECUE_DATA_CHANGE:
	// if (appClass.getFlagDeviceUpate() ==
	// CobraFlags.DEVICE_STATUS_READY_TO_ACKNOWLEDGE) {
	// cobra.AckStatusChange(false, 1, 0, 0);
	// appClass.writeLog("Flag Device Update State READY_TO_CALL_DEVICE_STATUS : "
	// + CobraFlags.READY_TO_CALL_DEVICE_STATUS);
	// appClass.setFlagDeviceUpate(CobraFlags.READY_TO_CALL_DEVICE_STATUS);
	// cobra.AckOwnFlagStatusChange();
	// }
	// if (appClass.getFlagFireCuesUpate() ==
	// CobraFlags.FIRECUE_DATA_READY_TO_ACKNOWLEDGE) {
	// cobra.AckStatusChange(false, 0, 0, 1);
	// appClass.setFlagFireCuesUpate(CobraFlags.READY_TO_CALL_FIRECUE_DATA);
	// appClass.writeLog("Flag Firecues Update State READY_TO_CALL_FIRECUE_DATA : "
	// + CobraFlags.READY_TO_CALL_FIRECUE_DATA);
	// cobra.AckOwnFlagStatusChange();
	// break;
	// }
	// case Cobra.EVENT_ACK_MODULE_FIRECUE_DATA_CHANGE:
	// if (appClass.getFlagModuleUpate() ==
	// CobraFlags.MODULE_DATA_READY_TO_ACKNOWLEDGE) {
	// cobra.AckStatusChange(false, 0, 1, 0);
	// appClass.setFlagModuleUpate(CobraFlags.READY_TO_CALL_MODULE_DATA);
	// appClass.writeLog("Flag Module Update State READY_TO_CALL_MODULE_DATA : "
	// + CobraFlags.READY_TO_CALL_MODULE_DATA);
	// cobra.AckOwnFlagStatusChange();
	// }
	// if (appClass.getFlagFireCuesUpate() ==
	// CobraFlags.FIRECUE_DATA_READY_TO_ACKNOWLEDGE) {
	// cobra.AckStatusChange(false, 0, 0, 1);
	// appClass.setFlagFireCuesUpate(CobraFlags.READY_TO_CALL_FIRECUE_DATA);
	// appClass.writeLog("Flag Firecues Update State READY_TO_CALL_FIRECUE_DATA : "
	// + CobraFlags.READY_TO_CALL_FIRECUE_DATA);
	// cobra.AckOwnFlagStatusChange();
	// }
	// break;
	//
	// case Cobra.EVENT_ACK_DEVICE_DATA_CHANGE:
	// if (appClass.getFlagDeviceUpate() ==
	// CobraFlags.DEVICE_STATUS_READY_TO_ACKNOWLEDGE) {
	// cobra.AckStatusChange(false, 1, 0, 0);
	// appClass.writeLog("Flag Device Update State READY_TO_CALL_DEVICE_STATUS : "
	// + CobraFlags.READY_TO_CALL_DEVICE_STATUS);
	// appClass.setFlagDeviceUpate(CobraFlags.READY_TO_CALL_DEVICE_STATUS);
	// cobra.AckOwnFlagStatusChange();
	// }
	// break;
	//
	// case Cobra.EVENT_ACK_MODULE_DATA_CHANGE:
	// if (appClass.getFlagModuleUpate() ==
	// CobraFlags.MODULE_DATA_READY_TO_ACKNOWLEDGE) {
	// cobra.AckStatusChange(false, 0, 1, 0);
	// appClass.setFlagModuleUpate(CobraFlags.READY_TO_CALL_MODULE_DATA);
	// appClass.writeLog("Flag Module Update State READY_TO_CALL_MODULE_DATA : "
	// + CobraFlags.READY_TO_CALL_MODULE_DATA);
	// cobra.AckOwnFlagStatusChange();
	// }
	// break;
	//
	// case Cobra.EVENT_ACK_FIRECUE_DATA_CHANGE:
	// if (appClass.getFlagFireCuesUpate() ==
	// CobraFlags.FIRECUE_DATA_READY_TO_ACKNOWLEDGE) {
	// cobra.AckStatusChange(false, 0, 0, 1);
	// appClass.setFlagFireCuesUpate(CobraFlags.READY_TO_CALL_FIRECUE_DATA);
	// appClass.writeLog("Flag Firecues Update State READY_TO_CALL_FIRECUE_DATA : "
	// + CobraFlags.READY_TO_CALL_FIRECUE_DATA);
	// cobra.AckOwnFlagStatusChange();
	// }
	// break;
	// }
	// } catch (Exception e) {
	// int i = 0;
	// i++;
	// }
	// }
	// };

	@Override
	public void onBackPressed() {
		appClass.TitleText = "Disconnected";
		appClass.UpdateIcon = true;
		IsAppActive = false;
		moveTaskToBack(true);

	}

	private class DBUpdateTask extends AsyncTask<Void, Void, Void> {

		ProgressDialog pd = null;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(context);
			pd.setMessage("Updating Modules in Database");
			pd.show();
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (pd != null)
				pd.dismiss();
			// contentFrame.addView(bucketFiring);

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			UpdateDatabase();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

	}

	// public void setNoOfTries(String inte) {
	// this.noOfTries = Integer.parseInt(inte);
	// }

	// this function to show No Devices Dialoge to screen
	public void showDialogDisconnectNoDevice() {

		FragmentTransaction ft = (this).getSupportFragmentManager()
				.beginTransaction();

		Fragment prev = (this).getSupportFragmentManager().findFragmentByTag(
				"dialog");

		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		// Create and show the dialog.
		DialogFragment newFragment = DialogDisconnectNoDevice.newInstance(1,
				"", 1);
		newFragment.show(ft, "dialog");

	}

	public void showDialogDemo() {

		FragmentTransaction ft = ((FragmentActivity) this)
				.getSupportFragmentManager().beginTransaction();

		Fragment prev = ((FragmentActivity) this).getSupportFragmentManager()
				.findFragmentByTag("dialogdemo");

		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		// Create and show the dialog.
		DialogFragment newFragment = DialogDemo.newInstance(1, "", 1);
		newFragment.show(ft, "dialogdemo");

	}

	public void startCheckerThread() {

		try {
			clearFlags();
			AckThread = new Thread(checker);
			AckThread.start();
		} catch (Exception e) {
		}

	}

}
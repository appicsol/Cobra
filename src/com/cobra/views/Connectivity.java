package com.cobra.views;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.EventListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.hardware.usb.UsbManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.cobra.HTTPRequest.HTTPRequestListener;
import com.cobra.MainActivity;
import com.cobra.R;
import com.cobra.SecureStorage;
import com.cobra.ServerCOM;
import com.cobra.appClass;
import com.cobra.api.Cobra;
import com.cobra.api.SerialDriver.SerialProbe;
import com.cobra.api.SerialDriver.SerialProbe.SerialProbeEvent;
import com.cobra.api.SerialDriver.SerialProbe.SerialProbeListener;
import com.cobra.dialogs.DialogDemo;
import com.cobra.dialogs.DialogDisconnectNoDevice;
import com.cobra.interfaces.DemoModeListener;

public class Connectivity extends RelativeLayout {
	private String TAG;

	private TextView tvConn, tvTitle, tvTitle_sub, tvTitle_sub_sub;
	private ProgressBar pbConn;
	private Button btnConnect;
	private Button btnDemo;
	private Button btnRegister;

	private String androidID;

	private Cobra cobra;

	private Spinner tester_spinner;

	private RelativeLayout layout_tester_spinner;

	protected int selected_tester_index = 0;

	private Activity activity;

	Context cont;

	private boolean isFirstTime;

	static DemoModeListener demoListener;

	public static DemoModeListener getDemoListener() {
		return demoListener;
	}

	public void setDemoListener(DemoModeListener demoListener) {
		this.demoListener = demoListener;
	}

	public Connectivity(MainActivity context, final OnCobraConnected listener,
			boolean appActivated, boolean isFirstTime2) {
		super(context);
		this.isFirstTime = isFirstTime2;
		activity = context;
		cont = context;
		TAG = Connectivity.class.getPackage().getName() + "."
				+ Connectivity.class.getName();
		View view = View.inflate(context, R.layout.connectivity, this);

		tvConn = (TextView) view.findViewById(R.id.tvConnecting);
		tvTitle = (TextView) view.findViewById(R.id.tvTitle);
		tvTitle_sub = (TextView) view.findViewById(R.id.tvTitle_sub);
		tvTitle_sub_sub = (TextView) view.findViewById(R.id.tvTitle_sub_sub);
		tester_spinner = (Spinner) view.findViewById(R.id.spinner_tester);
		layout_tester_spinner = (RelativeLayout) view
				.findViewById(R.id.layout_spinner_tester);
		tester_spinner.setEnabled(true);
		pbConn = (ProgressBar) view.findViewById(R.id.pbConnection);
		btnConnect = (Button) view.findViewById(R.id.btnConnect);
		btnRegister = (Button) view.findViewById(R.id.btnRegister);
		btnDemo = (Button) view.findViewById(R.id.btnDemo);

		Typeface tf = Typeface.createFromAsset(context.getAssets(),
				"fonts/impact.ttf");

		tvTitle.setTypeface(tf);

		androidID = context.getDeviceID();

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
				R.layout.spinner_item, appClass.names_Tester);
		tester_spinner.setAdapter(adapter);
		tester_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				selected_tester_index = arg2;
				if (arg2 > 0) {
					appClass.TesterName = appClass.names_Tester[arg2];
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		btnConnect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Date d = new Date();
				String filename = (String) DateFormat.format(
						"MMMM-d-yyyy-hh:mm:ss", d.getTime());
				MainActivity.FileName = filename;
				btnConnect.setEnabled(false);

				// if (appClass.StartCobra(System.currentTimeMillis(), 20))
				connectToCobra(listener);
				// else{
				// btnConnect.setEnabled(true);
				// Toast.makeText(getContext(),
				// "Please wait for "+appClass.AppForceClosedTimeRemaining+" seconds.",
				// Toast.LENGTH_LONG).show();
				// }

			}
		});

		if (appActivated()) {
			// if (!MainActivity.debugMode && isOnline()) {
			// confirmReserve();
			// }
			btnRegister.setEnabled(false);
			btnRegister.setVisibility(INVISIBLE);
			btnDemo.setEnabled(false);
			btnDemo.setVisibility(INVISIBLE);
		}

		initializeRegistrationDialog();
		startUp();

		IntentFilter filter2 = new IntentFilter();
		filter2.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
		getContext().registerReceiver(mUsbReceiver, filter2);

		demoListener = new DemoModeListener() {

			@Override
			public void OnDemoModeListener() {
				// TODO Auto-generated method stub
				connectToCobra(listener);
			}
		};

		setDemoListener(demoListener);

		// if (isFirstTime) {
		// btnConnect.setVisibility(RelativeLayout.GONE);
		// btnRegister.setVisibility(RelativeLayout.GONE);
		// Date d = new Date();
		// String filename = (String) DateFormat.format(
		// "MMMM-d-yyyy-hh:mm:ss", d.getTime());
		// MainActivity.FileName = filename;
		// btnConnect.setEnabled(false);
		//
		// // if (appClass.StartCobra(System.currentTimeMillis(), 20))
		// connectToCobra(listener);
		// }

	}

	public static boolean appFirstTime(Context cont) {
		{

			try {
				String text = "";
				SharedPreferences sharedpreferences = cont
						.getSharedPreferences("KEYPREFERENCES",
								Context.MODE_PRIVATE);

				if (sharedpreferences.contains("NotFirstTime")) {
					return false;
				}

				return true;

			} catch (Exception e) {
				return false;
			}
		}

	}

	public boolean appActivated() {
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

	private void initializeRegistrationDialog() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View registrationDialog = inflater.inflate(
				R.layout.key_activation_dialog, null);
		final EditText etActivationKey = (EditText) registrationDialog
				.findViewById(R.id.etActivationKey);
		final AlertDialog.Builder keyDialogBuilder = new AlertDialog.Builder(
				getContext());

		keyDialogBuilder.setTitle("Register Device");
		keyDialogBuilder.setMessage("Enter Activation Key:");
		keyDialogBuilder.setView(registrationDialog);

		keyDialogBuilder.setPositiveButton("Activate",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						appClass globV = (appClass) getContext()
								.getApplicationContext();
						if (globV.isNetworkAvailable()) {
							startingAnimation();
							tvConn.setText("Connecting to server...");
							// TODO: input checking
							registerKey(etActivationKey.getText().toString());
							etActivationKey.setText("");
						} else {
							Toast.makeText(getContext(),
									"Network is not available",
									Toast.LENGTH_LONG).show();
						}
					}
				});
		keyDialogBuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						etActivationKey.setText("");
					}
				});

		final AlertDialog keyDialog = keyDialogBuilder.create();
		btnRegister.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				keyDialog.show();
			}
		});
	}

	public void startUp() {
		tvConn.setVisibility(View.INVISIBLE);
		pbConn.setVisibility(View.INVISIBLE);
		tvTitle.setVisibility(View.VISIBLE);
		tvTitle_sub.setVisibility(View.VISIBLE);
		tvTitle_sub_sub.setVisibility(View.VISIBLE);
		// layout_tester_spinner.setVisibility(View.VISIBLE);
		btnConnect.setEnabled(false);
		tester_spinner.setEnabled(true);
		// layout_tester_spinner.setEnabled(false);

		// tvTitle.startAnimation(AnimationUtils.loadAnimation(this.getContext(),
		// R.anim.fade_move_up));
		Animation an = AnimationUtils.loadAnimation(this.getContext(),
				android.R.anim.fade_in);
		an.setDuration(800);
		an.setStartOffset(1200);
		an.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				btnConnect.setEnabled(true);
				// layout_tester_spinner.setEnabled(true);
			}
		});
		btnConnect.setAnimation(an);
		// layout_tester_spinner.setAnimation(an);
	}

	public void connectToCobra(final OnCobraConnected listener) {
		int perm;

		// Toast.makeText(getContext(), "Connectivity:StatingAnimation",
		// Toast.LENGTH_SHORT).show();
		startingAnimation();

		// if (!this.appActivated()) {
		// // appClass.log_Parser("Device is not registered");
		// errorText("Device is not registered");
		// return;
		// }

		if (!this.appActivated()) {

			// Here will open demo mode

			appClass.isDemoMode = true;

		}

		perm = SerialProbe.checkPermission(this.getContext());
		// no device
		if (perm == SerialProbe.NO_DEVICE) {

			// "No Device Detected");
			// errorText("No Device Detected");

			showDialogDisconnectNoDevice();

			return;
		}

		if (selected_tester_index != 0) {
			// appClass.log_Parser("Tester: "
			// + appClass.names_Tester[selected_tester_index]
			// + "Logged in");
		} // no permission so request it

		// appClass.log_Parser("Time Before Connectivity : "+System.currentTimeMillis());
		if (perm == SerialProbe.NO_PERMISSION) {
			SerialProbe.clearSerialProbeListenerList();
			SerialProbe.requestPermission(this.getContext(),
					new SerialProbeListener() {
						@Override
						public void onPermissionRequestComplete(
								SerialProbeEvent event) {
							if (event.hasPermission()) {
								try {
									// Toast.makeText(
									// getContext(),
									// "Connectivity:onPermissionRequestComplete",
									// Toast.LENGTH_SHORT).show();
									connect(listener);
								} catch (Exception e) {
									// TODO: handle exception
									// Toast.makeText(getContext(),
									// "connectivity 258",
									// Toast.LENGTH_LONG).show();
								}

							} else {
								Activity a = (Activity) Connectivity.this
										.getContext();
								a.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										// appClass.createReadingLogOnDevice(
										// "Connectivity",
										// "Permission Denied");
										// appClass.log_Parser("Permission Denied");
										errorText("Permission Denied");
									}
								});
							}
						}
					});
		}
		// permission was already granted through intent filter
		else {
			connect(listener);
		}
	}

	// public void RemoveAnimations(){
	// tvConn.setVisibility(View.INVISIBLE);
	// pbConn.setVisibility(View.INVISIBLE);
	// }

	public void setDemoButtonListener(final DemoButtonListener listener) {

		btnDemo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.demoModeButtonPress();
			}
		});
	}

	public static interface DemoButtonListener extends EventListener {
		public void demoModeButtonPress();
	}

	public static interface OnCobraConnected extends EventListener {
		public void onCobraConnected(CobraConnectedEvent event);
	}

	public static class CobraConnectedEvent {
		private Cobra cobra;

		public CobraConnectedEvent(Cobra cobra) {
			this.cobra = cobra;
		}

		public Cobra getCobraDevice() {
			return this.cobra;
		}
	}

	private ConnectCobra tsk;

	private void connect(final OnCobraConnected listener) {
		cobra = new Cobra(activity, this.getContext());

		try {
			tsk = new ConnectCobra(listener);
			tsk.execute();

			// new Handler().postDelayed(new Runnable() {
			// @Override
			// public void run() {
			// // TODO Auto-generated method stub
			// if (!IsCobraConnected) {
			// try {
			// ShowTimeoutDialog();
			// } catch (BadTokenException e) {
			//
			// } catch (Exception e) {
			//
			// }
			// }
			// }
			// }, 10000);

			// tsk.get(5, TimeUnit.SECONDS);
		} catch (Exception e) {
		}
	}

	private void ShowTimeoutDialog() {
		// AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		// builder.setTitle("Connection Problem");
		// builder.setMessage("The COBRA control panel is taking longer than expected to connect. Please re-start your 18R2. Once re-started, the control panel should re-connect shortly after the channel is displayed following the start-up process.");
		// builder.setNegativeButton("Ok", new DialogInterface.OnClickListener()
		// {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// // TODO Auto-generated method stub
		// if (tsk != null)
		// tsk.cancel(true);
		// appClass.IsAppForcedClosed = false;
		// activity.finish();
		// }
		// });
		if (MainActivity.IsAppActive)
			showDialogDisconnectDevice();
		// builder.show();

	}

	private void startingAnimation() {
		Animation an1 = AnimationUtils.loadAnimation(this.getContext(),
				android.R.anim.fade_out);
		an1.setDuration(100);
		an1.setFillAfter(true);
		btnConnect.setAnimation(an1);
		tvTitle.setAnimation(an1);
		tvTitle_sub.setAnimation(an1);
		tvTitle_sub_sub.setAnimation(an1);
		// layout_tester_spinner.setAnimation(an1);
		// layout_tester_spinner.setVisibility(View.VISIBLE);

		an1.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				tester_spinner.setEnabled(false);
			}
		});

		tvConn.setVisibility(View.VISIBLE);
		pbConn.setVisibility(View.VISIBLE);

		// RemoveAnimations();

		Animation an2 = AnimationUtils.loadAnimation(this.getContext(),
				android.R.anim.fade_in);
		an2.setDuration(1500);
		tvConn.setAnimation(an2);

		Animation an3 = AnimationUtils.loadAnimation(this.getContext(),
				android.R.anim.fade_in);
		an3.setDuration(1500);
		an3.setStartOffset(400);
		pbConn.setAnimation(an3);
	}

	private void connectedAnimation(final OnCobraConnected listener) {
		// appClass.log_Parser("Time When Connected : "
		// + System.currentTimeMillis());
		tvConn.setText("Connected");
		Animation an = AnimationUtils.loadAnimation(this.getContext(),
				android.R.anim.fade_out);
		an.setDuration(600);
		an.setStartOffset(200);
		an.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
				try {
					listener.onCobraConnected(new CobraConnectedEvent(cobra));
				} catch (Exception e) {

				}
				cobra.refreshDeviceStatus();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}
		});
		tvConn.setAnimation(an);

		Animation an2 = AnimationUtils.loadAnimation(this.getContext(),
				android.R.anim.fade_out);
		an2.setDuration(300);
		pbConn.setAnimation(an2);

		tvConn.setVisibility(View.INVISIBLE);
		pbConn.setVisibility(View.INVISIBLE);
	}

	public void errorText(String str) {
		tvConn.setText(str);

		Animation an = AnimationUtils.loadAnimation(this.getContext(),
				android.R.anim.fade_out);
		an.setDuration(400);
		pbConn.setAnimation(an);

		pbConn.setVisibility(View.INVISIBLE);
	}

	private void restartApp() {
		Intent i = getContext().getPackageManager().getLaunchIntentForPackage(
				getContext().getPackageName());
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		getContext().startActivity(i);
	}

	private void registerKey(final String key) {
		String url = "http://www.cobrafiringsystems.com/api/android/check_key.php?key="
				+ key + "&deviceid=" + getDeviceID();
		url += "";
		ServerCOM.requestPage(url, new HTTPRequestListener() {
			public void onDataRecieved(String data) {
				if (data.equals("1")) {
					tvConn.setText("Registering device...");
					reserveKey(key);
				} else if (data.equals("2")) {
					// appClass.log_Parser("Key already activated on another device");
					errorText("Key already activated on another device");
				} else {
					// appClass.log_Parser("Invalid key");
					errorText("Invalid key");
				}
			}
		});
	}

	public String getDeviceID() {
		String android_id = Secure.getString(getContext().getContentResolver(),
				Secure.ANDROID_ID);

		String md5 = md5(android_id);

		return android_id;
	}

	public String md5(String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest
					.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++)
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	private void reserveKey(final String key) {
		String url = "http://www.cobrafiringsystems.com/api/android/reserve_key.php?key="
				+ key + "&android_id=" + androidID;
		url += "";
		ServerCOM.requestPage(url, new HTTPRequestListener() {
			public void onDataRecieved(String data) {
				if (data.equals("1")) {
					try {
						SharedPreferences sharedpreferences = cont
								.getSharedPreferences("KEYPREFERENCES",
										Context.MODE_PRIVATE);
						Editor editor = sharedpreferences.edit();
						editor.putString("key", key);
						editor.putString("activated", "true");

						// SecureStorage.storeString(androidID, "activated",
						// getContext(), "true");
						// SecureStorage.storeString(androidID, "key",
						// getContext(), key);
						editor.commit();
						restartApp();
					} catch (Exception e) {
						// appClass.log_Parser("Activation Failed");
						errorText("Activation Failed");
					}
				} else {
					// appClass.log_Parser("Activation unsuccessful");
					errorText("Activation unsuccessful");
				}
			}
		});
	}

	private void obselete_confirmReserve() {
		try {
			SharedPreferences sharedpreferences = cont.getSharedPreferences(
					"KEYPREFERENCES", Context.MODE_PRIVATE);
			String key = "";
			if (sharedpreferences.contains("key")
					&& sharedpreferences.contains("activated")) {
				key = sharedpreferences.getString("key", "");
			}
			ServerCOM.requestPage(
					"http://www.cobrafiringsystems.com/api/android/confirm_reserve.php?key="
							+ key + "&android_id=" + androidID,
					new HTTPRequestListener() {
						public void onDataRecieved(String data) {
							if (data != null)
								if (!data.equals("1")) {
									try {
										SecureStorage.storeString(androidID,
												"activated", getContext(),
												"false");
										btnRegister.setEnabled(true);
										btnRegister.setVisibility(VISIBLE);
										btnDemo.setEnabled(true);
										btnDemo.setVisibility(VISIBLE);
									} catch (Exception e) {
									}
								} else {
									Log.i(TAG, "Data equals 1...?");
								}
						}
					});
		} catch (Exception e) {
			Log.i(TAG, "Could not confirm key reservation (IO)");
		}
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) cont
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting();
	}

	public static Boolean IsCobraConnected = false;

	public class ConnectCobra extends AsyncTask<Void, Void, Void> {

		private OnCobraConnected listener;

		public ConnectCobra(OnCobraConnected listener) {
			this.listener = listener;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			IsCobraConnected = false;
			// Toast.makeText(getContext(), "Connectivity:ConnectCobra",
			// Toast.LENGTH_SHORT).show();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// Toast.makeText(getContext(), "Connectivity:ConnectCobraComplete",
			// Toast.LENGTH_SHORT).show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						int ret = cobra.openDevice();

						if (ret < 0) {
							cobra.setError(Cobra.ERROR_POWER_CYCLE);
							listener.onCobraConnected(new CobraConnectedEvent(
									cobra));
						}
					} catch (Exception e) {
						cobra.setError("Oops! We are not receiving communication from the 18R2. Please press OK to close the app, then disconnect the cable and re-connect the cable after 10 seconds. If this does not work, please re-start the 18R2.");
					}

					Activity activity = (Activity) Connectivity.this
							.getContext();

					Boolean Stop = false;
					int tries = 0;
					while (!Stop) {
						try {
							if (appClass.IsConnectionEstablished) {
								Stop = true;
								activity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										IsCobraConnected = true;
										connectedAnimation(listener);
									}
								});
							} else if (tries == 3) {
								Stop = true;
								activity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										ShowTimeoutDialog();
									}
								});
							}
							tries++;
							cobra.VCOM_REQ_DEVICE_INFO();
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}
			});
			thread.start();
			return null;
		}
	}

	private BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
				Toast.makeText(getContext(), "attached", Toast.LENGTH_LONG)
						.show();
				RefreshUI();
			}
		}
	};

	private void RefreshUI() {

		// layout_tester_spinner.setVisibility(View.VISIBLE); // spinner
		tvTitle.setVisibility(View.VISIBLE); // main heading
		tvTitle_sub.setVisibility(View.VISIBLE); // main heading
		tvTitle_sub_sub.setVisibility(View.VISIBLE); // main heading
		btnConnect.setVisibility(View.VISIBLE); // button
		//
		tvConn.setVisibility(View.INVISIBLE); // spinner

	}

	public void showDialogDisconnectNoDevice() {

		FragmentTransaction ft = ((FragmentActivity) activity)
				.getSupportFragmentManager().beginTransaction();

		Fragment prev = ((FragmentActivity) activity)
				.getSupportFragmentManager().findFragmentByTag("dialog");

		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		// Create and show the dialog.
		DialogFragment newFragment = DialogDisconnectNoDevice.newInstance(1,
				"", 1);
		newFragment.show(ft, "dialog");

	}

	public void showDialogDisconnectDevice() {

		FragmentTransaction ft = ((FragmentActivity) activity)
				.getSupportFragmentManager().beginTransaction();

		Fragment prev = ((FragmentActivity) activity)
				.getSupportFragmentManager().findFragmentByTag("dialog");

		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		// Create and show the dialog.
		DialogFragment newFragment = DialogDisconnectNoDevice.newInstance(1,
				"", 1);
		try {
			newFragment.show(ft, "dialog");
		} catch (Exception e) {

		}

	}

}
package com.cobra.views;

import android.app.Activity;
import android.content.Context;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.cobra.R;
import com.cobra.appClass;
import com.cobra.api.Cobra;
import com.cobra.api.Cobra.ICommandEventListener;

public class TestCommands extends LinearLayout {

	Button btn_save_logs;
	Button btn_ForceRefresh;
	Button btn_ModuleDataRefresh;
	Button btn_AllDataRefresh;
	Button btn_SetARM;
	Button btn_SetTest;
	Button btn_GetDeviceStatus;

	Button btn_AckStatusChange;
	Button btn_ReqFireCuesData;
	Button btn_ReqModuleData;
	Button btn_ReqDeviceStatus;

	Button VCOM_REQ_ALL_DATA_REFRESH;
	Button VCOM_ACKNOWLEDGE_STATUS_CHANGE_0_1_1;
	Button VCOM_ACKNOWLEDGE_STATUS_CHANGE_1_0_0;
	Button VCOM_ACKNOWLEDGE_STATUS_CHANGE_0_1_4;
	Button VCOM_ACKNOWLEDGE_STATUS_CHANGE_0_2_100;
	Button VCOM_ACKNOWLEDGE_STATUS_CHANGE_0_3_100;
	Button ACK_01;
	Button VCOM_REQ_QUEUED_FIREDCUES_DATA__1;
	Button VCOM_REQ_QUEUED_FIREDCUES_DATA_4;
	Button VCOM_REQ_QUEUED_MODULE_DATA__1;
	Button VCOM_REQ_QUEUED_MODULE_DATA_1;
	Button VCOM_REQ_QUEUED_MODULE_DATA_2;
	Button VCOM_REQ_QUEUED_MODULE_DATA_3;
	Button VCOM_REQ_DEVICE_STATUS;

	appClass globV;
	EditText WritingCommandPromot;
	View rootView;

	Cobra cobra;
	private ScrollView scroller;
	private ToggleButton toogle;

	public TestCommands(Context context, Activity activity, Cobra cobra) {
		super(context);
		rootView = View.inflate(context, R.layout.test_commands, this);
		// TODO Auto-generated constructor stub
		this.cobra = cobra;
		this.cobra.addCommandEventListener(listener);
		WritingCommandPromot = (EditText) rootView.findViewById(R.id.writing);
		toogle = (ToggleButton) rootView.findViewById(R.id.toggleButton1);
		toogle.setChecked(true);
		toogle.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					appClass.IsCommandPrompt_DECIMAL = true;
				} else {
					appClass.IsCommandPrompt_DECIMAL = false;
				}
			}
		});
		scroller = (ScrollView) rootView.findViewById(R.id.scrollView1);
		globV = (appClass) getContext().getApplicationContext();

		btn_save_logs = (Button) rootView.findViewById(R.id.btn_save_logs);
		btn_save_logs.setOnClickListener(SaveLogs);

		btn_ForceRefresh = (Button) rootView.findViewById(R.id.force_refresh);
		btn_ForceRefresh.setOnClickListener(ForceRefresh);

		btn_ModuleDataRefresh = (Button) rootView
				.findViewById(R.id.module_data_refresh);
		btn_ModuleDataRefresh.setOnClickListener(ModuleRefresh);

		btn_AllDataRefresh = (Button) rootView
				.findViewById(R.id.all_data_refresh);
		btn_AllDataRefresh.setOnClickListener(AllDataRefresh);

		btn_SetARM = (Button) rootView.findViewById(R.id.set_arm);
		btn_SetARM.setOnClickListener(ARM);

		btn_SetTest = (Button) rootView.findViewById(R.id.set_test);
		btn_SetTest.setOnClickListener(TEST);

		btn_GetDeviceStatus = (Button) rootView
				.findViewById(R.id.get_device_status);
		btn_GetDeviceStatus.setOnClickListener(DeviceStatus);

		btn_AckStatusChange = (Button) rootView
				.findViewById(R.id.ack_status_change);
		btn_AckStatusChange.setOnClickListener(AckStatusChange);

		btn_ReqFireCuesData = (Button) rootView
				.findViewById(R.id.req_fire_cues);
		btn_ReqFireCuesData.setOnClickListener(ReqFireCuesData);

		btn_ReqModuleData = (Button) rootView
				.findViewById(R.id.req_module_data);
		btn_ReqModuleData.setOnClickListener(ReqModuleData);

		btn_ReqDeviceStatus = (Button) rootView
				.findViewById(R.id.req_device_status);
		btn_ReqDeviceStatus.setOnClickListener(ReqDeviceStatus);

		VCOM_REQ_ALL_DATA_REFRESH = (Button) rootView
				.findViewById(R.id.VCOM_REQ_ALL_DATA_REFRESH);
		VCOM_REQ_ALL_DATA_REFRESH
				.setOnClickListener(VCOM_REQ_ALL_DATA_REFRESHListener);

		VCOM_ACKNOWLEDGE_STATUS_CHANGE_0_1_1 = (Button) rootView
				.findViewById(R.id.VCOM_ACKNOWLEDGE_STATUS_CHANGE_0_1_1);
		VCOM_ACKNOWLEDGE_STATUS_CHANGE_0_1_1
				.setOnClickListener(VCOM_ACKNOWLEDGE_STATUS_CHANGE_0_1_1Listener);

		VCOM_ACKNOWLEDGE_STATUS_CHANGE_1_0_0 = (Button) rootView
				.findViewById(R.id.VCOM_ACKNOWLEDGE_STATUS_CHANGE_1_0_0);
		VCOM_ACKNOWLEDGE_STATUS_CHANGE_1_0_0
				.setOnClickListener(VCOM_ACKNOWLEDGE_STATUS_CHANGE_1_0_0Listener);

		VCOM_ACKNOWLEDGE_STATUS_CHANGE_0_1_4 = (Button) rootView
				.findViewById(R.id.VCOM_ACKNOWLEDGE_STATUS_CHANGE_0_1_4);
		VCOM_ACKNOWLEDGE_STATUS_CHANGE_0_1_4
				.setOnClickListener(VCOM_ACKNOWLEDGE_STATUS_CHANGE_0_1_4Listener);

		VCOM_ACKNOWLEDGE_STATUS_CHANGE_0_2_100 = (Button) rootView
				.findViewById(R.id.VCOM_ACKNOWLEDGE_STATUS_CHANGE_0_2_100);
		VCOM_ACKNOWLEDGE_STATUS_CHANGE_0_2_100
				.setOnClickListener(VCOM_ACKNOWLEDGE_STATUS_CHANGE_0_2_100Listener);

		VCOM_ACKNOWLEDGE_STATUS_CHANGE_0_3_100 = (Button) rootView
				.findViewById(R.id.VCOM_ACKNOWLEDGE_STATUS_CHANGE_0_3_100);
		VCOM_ACKNOWLEDGE_STATUS_CHANGE_0_3_100
				.setOnClickListener(VCOM_ACKNOWLEDGE_STATUS_CHANGE_0_3_100Listener);

		ACK_01 = (Button) rootView.findViewById(R.id.ACK_01);
		ACK_01.setOnClickListener(ACK_01Listener);

		VCOM_REQ_QUEUED_FIREDCUES_DATA__1 = (Button) rootView
				.findViewById(R.id.VCOM_REQ_QUEUED_FIREDCUES_DATA__1);
		VCOM_REQ_QUEUED_FIREDCUES_DATA__1
				.setOnClickListener(VCOM_REQ_QUEUED_FIREDCUES_DATA__1Listener);

		VCOM_REQ_QUEUED_FIREDCUES_DATA_4 = (Button) rootView
				.findViewById(R.id.VCOM_REQ_QUEUED_FIREDCUES_DATA_4);
		VCOM_REQ_QUEUED_FIREDCUES_DATA_4
				.setOnClickListener(VCOM_REQ_QUEUED_FIREDCUES_DATA_4Listener);

		VCOM_REQ_QUEUED_MODULE_DATA__1 = (Button) rootView
				.findViewById(R.id.VCOM_REQ_QUEUED_MODULE_DATA__1);
		VCOM_REQ_QUEUED_MODULE_DATA__1
				.setOnClickListener(VCOM_REQ_QUEUED_MODULE_DATA__1Listener);

		VCOM_REQ_QUEUED_MODULE_DATA_1 = (Button) rootView
				.findViewById(R.id.VCOM_REQ_QUEUED_MODULE_DATA_1);
		VCOM_REQ_QUEUED_MODULE_DATA_1
				.setOnClickListener(VCOM_REQ_QUEUED_MODULE_DATA_1Listener);

		VCOM_REQ_QUEUED_MODULE_DATA_2 = (Button) rootView
				.findViewById(R.id.VCOM_REQ_QUEUED_MODULE_DATA_2);
		VCOM_REQ_QUEUED_MODULE_DATA_2
				.setOnClickListener(VCOM_REQ_QUEUED_MODULE_DATA_2Listener);

		VCOM_REQ_QUEUED_MODULE_DATA_3 = (Button) rootView
				.findViewById(R.id.VCOM_REQ_QUEUED_MODULE_DATA_3);
		VCOM_REQ_QUEUED_MODULE_DATA_3
				.setOnClickListener(VCOM_REQ_QUEUED_MODULE_DATA_3Listener);

		VCOM_REQ_DEVICE_STATUS = (Button) rootView
				.findViewById(R.id.VCOM_REQ_DEVICE_STATUS);
		VCOM_REQ_DEVICE_STATUS
				.setOnClickListener(VCOM_REQ_DEVICE_STATUSListener);
	}

	private void UpdateText(String msg) {

	}

	ICommandEventListener listener = new ICommandEventListener() {
		@Override
		public void OnCommandChanged(final Spanned msg) {

			((Activity) getContext()).runOnUiThread(new Runnable() {
				public void run() {
					try {
						if (WritingCommandPromot == null)
							WritingCommandPromot = (EditText) rootView
									.findViewById(R.id.writing);
						String temp = WritingCommandPromot.getText().toString();
						if (temp.length() >= 1000) {
							String temp2 = temp.substring(501,
									temp.length() - 1);
							WritingCommandPromot.setText(temp2 + "\n");
						} else
							WritingCommandPromot.append(msg);
						WritingCommandPromot.setSelection(WritingCommandPromot
								.getText().length());
						scroller.fullScroll(ScrollView.FOCUS_DOWN);
					} catch (Exception e) {
						int i = 0;
						i++;
					}
				}
			});
		}
	};

	OnClickListener SaveLogs = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (WritingCommandPromot != null) {
				String log = WritingCommandPromot.getText().toString();
				if(log.isEmpty()){
					Toast.makeText(getContext(), "No Logs to store in SD card",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (appClass.LogSerialportCommunication(log)) {
					Toast.makeText(getContext(), "Logs stored in SD card",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(
							getContext(),
							"Some problem occured while storing logs in SD card",
							Toast.LENGTH_LONG).show();
				}
			}
		}
	};

	OnClickListener ForceRefresh = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			cobra.refreshData();
		}
	};
	OnClickListener ModuleRefresh = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			cobra.refreshModuleData();
		}
	};
	OnClickListener AllDataRefresh = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			cobra.refreshData();
		}
	};
	OnClickListener ARM = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			cobra.setArmMode();
		}
	};
	OnClickListener TEST = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			cobra.setTestMode();
		}
	};
	OnClickListener DeviceStatus = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			cobra.ReqDeviceStatus();
		}
	};

	OnClickListener AckStatusChange = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// cobra.AckStatusChange();
		}
	};
	OnClickListener ReqFireCuesData = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			cobra.ReqFireCuesData();
		}
	};
	OnClickListener ReqModuleData = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			cobra.ReqModuleData();
		}
	};
	OnClickListener ReqDeviceStatus = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			cobra.ReqDeviceStatus();
		}
	};

	OnClickListener VCOM_REQ_ALL_DATA_REFRESHListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			cobra.refreshData();
		}
	};
	OnClickListener VCOM_ACKNOWLEDGE_STATUS_CHANGE_0_1_1Listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			cobra.AckStatusChange(false, 0, 1, 1);
		}
	};
	OnClickListener VCOM_ACKNOWLEDGE_STATUS_CHANGE_1_0_0Listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			cobra.AckStatusChange(false, 1, 0, 0);
		}
	};
	OnClickListener VCOM_ACKNOWLEDGE_STATUS_CHANGE_0_1_4Listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			cobra.AckStatusChange(false, 0, 1, 4);
		}
	};
	OnClickListener VCOM_ACKNOWLEDGE_STATUS_CHANGE_0_2_100Listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			cobra.AckStatusChange(false, 0, 2, 100);
		}
	};
	OnClickListener VCOM_ACKNOWLEDGE_STATUS_CHANGE_0_3_100Listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			cobra.AckStatusChange(false, 0, 3, 100);
		}
	};
	OnClickListener ACK_01Listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			cobra.AckStatusChange(true, 1, -1, -1);
		}
	};

	OnClickListener VCOM_REQ_QUEUED_FIREDCUES_DATA__1Listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			cobra.VCOM_REQ_QUEUED_FIREDCUES_DATA(-1);
		}
	};
	OnClickListener VCOM_REQ_QUEUED_FIREDCUES_DATA_4Listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			cobra.VCOM_REQ_QUEUED_FIREDCUES_DATA(4);
		}
	};
	OnClickListener VCOM_REQ_QUEUED_MODULE_DATA__1Listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			cobra.VCOM_REQ_QUEUED_MODULE_DATA(-1);
		}
	};
	OnClickListener VCOM_REQ_QUEUED_MODULE_DATA_1Listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			cobra.VCOM_REQ_QUEUED_MODULE_DATA(1);
		}
	};
	OnClickListener VCOM_REQ_QUEUED_MODULE_DATA_2Listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			cobra.VCOM_REQ_QUEUED_MODULE_DATA(2);
		}
	};
	OnClickListener VCOM_REQ_QUEUED_MODULE_DATA_3Listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			cobra.VCOM_REQ_QUEUED_MODULE_DATA(3);
		}
	};
	OnClickListener VCOM_REQ_DEVICE_STATUSListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			cobra.ReqDeviceStatus();
		}
	};

}
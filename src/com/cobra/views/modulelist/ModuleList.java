package com.cobra.views.modulelist;

import java.util.ArrayList;
import java.util.HashMap;

import android.R.color;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;
import com.cobra.MainActivity;
import com.cobra.R;
import com.cobra.appClass;
import com.cobra.api.Cobra;
import com.cobra.api.CobraDataTags.ChannelDataTag;
import com.cobra.api.CobraDataTags.ModuleDataTag;
import com.cobra.api.CobraFlags;
import com.cobra.db.DBHelper;
import com.cobra.dialogs.DialogBuy;
import com.cobra.dialogs.DialogChannelGrid;
import com.cobra.dialogs.DialogChannelGrid.ChannelGridListener;
import com.cobra.dialogs.DialogContinuity;
import com.cobra.dialogs.DialogContinuityLagend;
import com.cobra.dialogs.DialogDemo;
import com.cobra.dialogs.DialogExceptionHelp;
import com.cobra.dialogs.DialogHeaderMode;
import com.cobra.interfaces.onFilterCueModule;
import com.cobra.interfaces.onFilterMode;
import com.cobra.module.interfaces.OnModuleInformationUpdate;
import com.cobra.view.bucketfiring.ModuleViews;
import com.cobra.views.PersistentHeader;
//import android.widget.Toast;

//import android.widget.Toast;

public class ModuleList extends Fragment {
	private String TAG;

	private static final int SHOW_EXCEPTION = 1;
	private static final int CHANNEL = 2;
	private static final int MODE = 3;
	private static final int SHOW_EXCEPTION_CHANNEL_MODE = 4;
	private static final int SHOW_EXCEPTION_CHANNEL = 5;
	private static final int SHOW_EXCEPTION_MODE = 6;
	private static final int CHANNEL_MODE = 7;
	private static final int REMOVE_FILTERATION = 8;

	private static SparseArray<ChannelDataTag> channelData;

	private static Cobra cobra;
	// private static MainActivity activity;

	private static LinearLayout linear_ListView;
	private TextView TvRemoteChannel;
	private static ScrollView scroll_view;
	appClass globV;
	private ArrayList<Integer> moduleIds;
	private TextView btn_Showexceptions;
	private ImageView btn_ContiniuityLagend;
	private RelativeLayout layout_FilterChannel;
	// private Spinner spnr_filterChannel;
	private TextView value_Channel;
	private TextView value_Mode;
	public static ArrayList<String> List_Channel;
	// private TextView btn_ForceRefresh;
	private RelativeLayout layout_FilterMode;
	private RelativeLayout layout_RemoveFilters;
	// private Spinner spnr_filterMode;
	private Boolean IsShowException = true;

	public static HashMap<Integer, ModuleListItem> hashMap;
	private ImageView btn_ExceptionHelp;
	// ArrayAdapter<String> adapter_channel;
	private ChannelGridListener channelGridListener;
	private DialogFragment dialogChannelGrid;
	private DialogFragment dialogMode;

	// /reresh button overlay
	private Button refreshButton, helpButton;
	private Spinner editTries;
	private GridLayout content;
	private MainActivity activity;
	String[] items = new String[] { "2", "3", "4", "5", "6", "7", "8", "9",
			"10" };

	private TextView demo_text;

	// /reresh button overlay

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		TAG = ModuleList.class.getName();

		globV = (appClass) getActivity().getApplicationContext();

		globV.setonModuleInformationUpdate(onModuleInformationUpdate);

		// ModuleList.activity = (MainActivity) getActivity();
		View view = View.inflate(getActivity(), R.layout.modulelist, null);

		linear_ListView = (LinearLayout) view.findViewById(R.id.linear_list);
		TvRemoteChannel = (TextView) view.findViewById(R.id.tvRemoteChannel);
		btn_Showexceptions = (TextView) view
				.findViewById(R.id.btn_showexceptions);

		btn_ContiniuityLagend = (ImageView) view
				.findViewById(R.id.img_continutiy_lagend);

		layout_FilterChannel = (RelativeLayout) view
				.findViewById(R.id.layout_filter_channel);
		layout_FilterMode = (RelativeLayout) view
				.findViewById(R.id.layout_filter_mode);
		layout_RemoveFilters = (RelativeLayout) view
				.findViewById(R.id.layout_remove_filter);
		// btn_ForceRefresh = (TextView)
		// view.findViewById(R.id.btn_forceRefresh);
		// btn_ForceRefresh.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// cobra.refreshModuleData();
		// }
		// });

		// / refresh button overlay ui

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, items);
		// this.addView(content);

		content = (GridLayout) view.findViewById(R.id.gridView);

		editTries = (Spinner) view.findViewById(R.id.editTries);
		refreshButton = (Button) view.findViewById(R.id.refreshButton);
		helpButton = (Button) view.findViewById(R.id.helpButton);
		editTries.setAdapter(adapter);
		editTries.setOnItemSelectedListener(d);
		editTries.setSelection(2);
		refreshButton.setOnClickListener(refreshButtonListener);

		// / refresh button overlay ui

		btn_ExceptionHelp = (ImageView) view
				.findViewById(R.id.btn_exception_help);
		btn_ExceptionHelp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentTransaction ft = ((FragmentActivity) getActivity())
						.getSupportFragmentManager().beginTransaction();

				Fragment prev = ((FragmentActivity) getActivity())
						.getSupportFragmentManager()
						.findFragmentByTag("dialog");

				if (prev != null) {
					ft.remove(prev);
				}

				ft.addToBackStack(null);

				// Create and show the dialog.
				DialogFragment newFragment = DialogExceptionHelp.newInstance(2);
				newFragment.show(ft, "dialog");

			}
		});
		btn_ContiniuityLagend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentTransaction ft = ((FragmentActivity) getActivity())
						.getSupportFragmentManager().beginTransaction();

				Fragment prev = ((FragmentActivity) getActivity())
						.getSupportFragmentManager()
						.findFragmentByTag("dialog");

				if (prev != null) {
					ft.remove(prev);
				}

				ft.addToBackStack(null);

				// Create and show the dialog.
				DialogFragment newFragment = DialogContinuityLagend
						.newInstance(2);
				newFragment.show(ft, "dialog");

			}
		});
		// spnr_filterChannel = (Spinner) view.findViewById(R.id.spnr_channel);

		// lbl_Channel = (RelativeLayout)
		// view.findViewById(R.id.layout_filter_channel);
		value_Channel = (TextView) view.findViewById(R.id.value_channel);
		value_Mode = (TextView) view.findViewById(R.id.value_mode);
		layout_FilterChannel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// if(appClass.isDemoMode){//DEMO
				// showDialogBuy();
				// return;
				// }

				FragmentTransaction ft = ((FragmentActivity) getActivity())
						.getSupportFragmentManager().beginTransaction();

				Fragment prev = ((FragmentActivity) getActivity())
						.getSupportFragmentManager()
						.findFragmentByTag("dialog");
				if (prev != null) {
					ft.remove(prev);
				}
				ft.addToBackStack(null);

				dialogChannelGrid = DialogChannelGrid.newInstance(List_Channel);
				channelGridListener = ((DialogChannelGrid) dialogChannelGrid)
						.getListener();
				dialogChannelGrid.show(ft, "dialog");

			}
		});

		layout_FilterMode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// if(appClass.isDemoMode){//DEMO
				// showDialogBuy();
				// return;
				// }

				FragmentTransaction ft = ((FragmentActivity) getActivity())
						.getSupportFragmentManager().beginTransaction();

				Fragment prev = ((FragmentActivity) getActivity())
						.getSupportFragmentManager()
						.findFragmentByTag("dialog");
				if (prev != null) {
					ft.remove(prev);
				}
				ft.addToBackStack(null);

				dialogMode = DialogHeaderMode.newInstance();
				dialogMode.show(ft, "dialog");

			}
		});

		layout_RemoveFilters.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// if(appClass.isDemoMode){//DEMO
				// showDialogBuy();
				// return;
				// }

				DoFilterationChannel = false;
				DoFilterationMode = false;
				DoFilterationShowException = false;
				value_Mode.setText("All");
				value_Channel.setText("All");
				IsShowException = true;
				btn_Showexceptions
						.setText(ExcptionModuleConter + " EXCEPTIONS");
				btn_Showexceptions
						.setBackgroundResource(R.drawable.button_exceptions);

				FilterModules(DoFilterationChannel, DoFilterationMode,
						DoFilterationShowException, FilterChannel, FilterisArm);
			}
		});

		globV.setChannelFilter(filterListener);
		globV.setModeFilter(filterModeListener);
		layout_FilterMode = (RelativeLayout) view
				.findViewById(R.id.layout_filter_mode);

		if (List_Channel == null) {
			List_Channel = new ArrayList<String>();
		}

		btn_Showexceptions.setText(ExcptionModuleConter + " EXCEPTIONS");
		// exception color change when there zero exception
		if (ExcptionModuleConter < 1) {
			btn_Showexceptions
					.setBackgroundResource(R.drawable.exception_zero_button);
			btn_Showexceptions.setTextColor(getResources().getColor(
					R.color.gray));
		} else {
			btn_Showexceptions
					.setBackgroundResource(R.drawable.button_exceptions);
			btn_Showexceptions.setTextColor(getResources().getColor(
					R.color.cues_text_color));
		}
		btn_Showexceptions.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// if(appClass.isDemoMode){// DEMO
				// showDialogBuy();
				// return;
				// }

				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {

						if (IsShowException) {

							if (globV.getModuleUI_List() != null)
								btn_Showexceptions.setText("SHOW ALL "
										+ globV.getModuleUI_List().size());
							else
								btn_Showexceptions.setText("SHOW ALL #");

							btn_Showexceptions
									.setBackgroundResource(R.drawable.button_showall);

							CounterType = SHOW_ALL;
							IsShowException = false;
							DoFilterationShowException = true;
							FilterModules(DoFilterationChannel,
									DoFilterationMode,
									DoFilterationShowException, FilterChannel,
									FilterisArm);
						} else {
							IsShowException = true;
							btn_Showexceptions.setText(ExcptionModuleConter
									+ " EXCEPTIONS");
							btn_Showexceptions
									.setBackgroundResource(R.drawable.button_exceptions);

							CounterType = SHOW_EXCEPTIONS;
							DoFilterationShowException = false;
							FilterModules(DoFilterationChannel,
									DoFilterationMode,
									DoFilterationShowException, FilterChannel,
									FilterisArm);

							// exception color change when there zero exception
							if (ExcptionModuleConter < 1) {
								btn_Showexceptions
										.setBackgroundResource(R.drawable.exception_zero_button);
								btn_Showexceptions.setTextColor(getResources()
										.getColor(R.color.gray));
							} else {
								btn_Showexceptions
										.setBackgroundResource(R.drawable.button_exceptions);
								btn_Showexceptions.setTextColor(getResources()
										.getColor(R.color.cues_text_color));
							}
						}

					}
				}, 20);

			}

		});

		// spnr_filterMode.setOnItemSelectedListener(new
		// OnItemSelectedListener() {
		// @Override
		// public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
		// long arg3) {
		// // TODO Auto-generated method stub
		// if (pos == 0) {
		//
		// } else if (pos == 1) {
		//
		// } else if (pos == 2) {
		//
		// }
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		// });

		globV.setLinearListView(linear_ListView);
		scroll_view = (ScrollView) view.findViewById(R.id.scrollView1);
		ModuleList.cobra = globV.getCobra();

		channelData = cobra.getChannelData();

		hashMap = null;

		linear_ListView.removeAllViews();

		if (globV.getModuleUI_List() != null) {
			globV.getModuleUI_List().clear();
			globV.removeModuleUI_List();
		}

		IntentFilter filter = new IntentFilter(
				appClass.RECIEVER_ADAPTER_MODULE_LIST);
		getActivity().registerReceiver(NotifyDataSetChangeReciever, filter);

		removeAllModules();

		demo_text = (TextView) view.findViewById(R.id.demo_mode_txt);
		// here set view content visibility against if demo mode
		if (appClass.isDemoMode) {// DEMO

			demo_text.setVisibility(View.VISIBLE);
		} else {
			demo_text.setVisibility(View.GONE);

		}

		return view;
	}

	onFilterMode filterModeListener = new onFilterMode() {
		@Override
		public void onModeChange(int mode) {
			// TODO Auto-generated method stub
			switch (mode) {
			case DialogHeaderMode.MODE_ALL:
				value_Mode.setText("All");
				DoFilterationMode = false;
				FilterisArm = true;
				FilterModules(DoFilterationChannel, DoFilterationMode,
						DoFilterationShowException, FilterChannel, FilterisArm);
				break;
			case DialogHeaderMode.MODE_ARM:
				value_Mode.setText("ARM");
				DoFilterationMode = true;
				FilterisArm = true;
				FilterModules(DoFilterationChannel, DoFilterationMode,
						DoFilterationShowException, FilterChannel, FilterisArm);
				break;
			case DialogHeaderMode.MODE_TEST:
				value_Mode.setText("TEST");
				DoFilterationMode = true;
				FilterisArm = false;
				FilterModules(DoFilterationChannel, DoFilterationMode,
						DoFilterationShowException, FilterChannel, FilterisArm);
				break;

			default:
				break;
			}
		}
	};

	onFilterCueModule filterListener = new onFilterCueModule() {
		@Override
		public void FilterChannel(int channel) {

			if (value_Channel != null) {
				if (channel != -1) {
					DoFilterationChannel = true;
					FilterChannel = channel;
					if (channel < 10)
						value_Channel.setText("0" + channel);
					else
						value_Channel.setText("" + channel);

					FilterModules(DoFilterationChannel, DoFilterationMode,
							DoFilterationShowException, FilterChannel,
							FilterisArm);
				} else {
					DoFilterationChannel = false;
					FilterChannel = -1;
					value_Channel.setText("All");
					FilterModules(DoFilterationChannel, DoFilterationMode,
							DoFilterationShowException, FilterChannel,
							FilterisArm);
				}
			}
		}
	};

	/**
	 * Update module row. If row does not exist in list, adds it.
	 * 
	 * @param tag
	 *            the ModuleDataTag
	 * @param lightStatus
	 */
	private void updateModuleRow(ModuleDataTag tag,
			ArrayList<String> lightStatus) {

		if (tag != null && tag.modID == -2) {
			if (hashMap != null)
				hashMap.clear();

			return;
		}
		ModuleListItem item;
		if (channelData.get(tag.currentChannel) != null) {
			item = new ModuleListItem(tag, channelData.get(tag.currentChannel),
					lightStatus, false);
		} else {
			item = new ModuleListItem(tag, new ChannelDataTag(0, 0, 0),
					lightStatus, false);
		}

		Boolean IS_ModuleFound = false;

		Boolean Is_Found = false;

		if (hashMap == null)
			hashMap = new HashMap<Integer, ModuleListItem>();
		hashMap.put(tag.modID, item);

		if (moduleIds == null)
			moduleIds = new ArrayList<Integer>();
		for (int i = 0; i < moduleIds.size(); i++) {
			if (moduleIds.get(i) == tag.modID) {
				break;
			}
			moduleIds.add(tag.modID);
		}

		ModuleViews moduleView = new ModuleViews();
		moduleView.setModuleChannel(tag.currentChannel);
		moduleView.setBucketAreaChannel(null);
		moduleView.setModuleAreaChannel(null);
		globV.getModuleList().put(tag.modID, moduleView);
		DBHelper.InsertOrUpdateModule(getActivity(), tag.modID,
				tag.currentChannel);

		// HashMap<Integer, ModuleListItem> sortedMap =
		// appClass.sortByComparator(hashMap);
		// int i=0;
		// i++;
	}

	public void removeModuleRow(int modID) {
		hashMap.remove(modID);
	}

	public void removeAllModules() {
		if (hashMap != null) {
			hashMap.clear();
		}

		if (List_Channel != null) {
			List_Channel.clear();
			// List_Channel.add("All");
			// adapter_channel.notifyDataSetChanged();
		}
		if (globV.getModuleUI_List() != null)
			globV.getModuleUI_List().clear();

		if (getActivity() != null && linear_ListView != null) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					linear_ListView.removeAllViews();
				}
			});
		}

		if (channelGridListener != null)
			channelGridListener.onChannelUpdate(
					DialogChannelGrid.STATUS_CLEAR_ALL, null);
	}

	private Boolean DoFilterationShowException = false;
	private Boolean DoFilterationChannel = false;
	private Boolean DoFilterationMode = false;
	private int FilterChannel = -1;
	private Boolean FilterisArm = false;

	private int ExcptionModuleConter = 0;

	private void FilterModules(Boolean filterChannel, Boolean filterMode,
			Boolean showException, int channel, Boolean isArm) {

		int state = getFilteationState(filterChannel, filterMode, showException);

		ArrayList<ModuleUIRow> ListModulesUI = globV.getModuleUI_List();
		if (ListModulesUI != null) {
			if (List_Channel != null) {
				List_Channel.clear();
				// List_Channel.add("All");
			}

			for (int i = 0; i < ListModulesUI.size(); i++) {
				ModuleUIRow temp_module = ListModulesUI.get(i);
				if (temp_module == null)
					continue;
				Boolean isChannelFound = false;
				for (int j = 0; j < List_Channel.size(); j++) {
					if (List_Channel.get(j).equals(
							"" + temp_module.getChannel())) {
						isChannelFound = true;
						break;
					}
				}
				if (!isChannelFound) {
					List_Channel.add("" + temp_module.getChannel());
					// SortChannelList(List_Channel);
				}
				switch (state) {
				case SHOW_EXCEPTION:
					if (temp_module != null && isShowException(temp_module)) {
						if (temp_module.getContent() != null)
							temp_module.getContent()
									.setVisibility(View.VISIBLE);
					} else if (temp_module != null) {
						if (temp_module.getContent() != null)
							temp_module.getContent().setVisibility(View.GONE);
					}
					break;
				case CHANNEL:
					if (temp_module != null
							&& channel == temp_module.getChannel()) {
						if (temp_module.getContent() != null)
							temp_module.getContent()
									.setVisibility(View.VISIBLE);
					} else if (temp_module != null) {

						if (temp_module.getContent() != null)
							temp_module.getContent().setVisibility(View.GONE);
					}
					break;
				case MODE:
					if (temp_module != null && isArm == temp_module.getMode()) {
						if (temp_module.getContent() != null)
							temp_module.getContent()
									.setVisibility(View.VISIBLE);
					} else if (temp_module != null) {
						if (temp_module.getContent() != null)
							temp_module.getContent().setVisibility(View.GONE);
					}
					break;

				case SHOW_EXCEPTION_CHANNEL:
					if (temp_module != null && isShowException(temp_module)
							&& channel == temp_module.getChannel()) {
						if (temp_module.getContent() != null)
							temp_module.getContent()
									.setVisibility(View.VISIBLE);
					} else if (temp_module != null) {
						if (temp_module.getContent() != null)
							temp_module.getContent().setVisibility(View.GONE);
					}
					break;
				case SHOW_EXCEPTION_MODE:
					if (temp_module != null && isShowException(temp_module)
							&& isArm == temp_module.getMode()) {
						if (temp_module.getContent() != null)
							temp_module.getContent()
									.setVisibility(View.VISIBLE);
					} else if (temp_module != null) {
						if (temp_module.getContent() != null)
							temp_module.getContent().setVisibility(View.GONE);
					}
					break;
				case CHANNEL_MODE:
					if (temp_module != null
							&& channel == temp_module.getChannel()
							&& isArm == temp_module.getMode()) {
						if (temp_module.getContent() != null)
							temp_module.getContent()
									.setVisibility(View.VISIBLE);
					} else if (temp_module != null) {
						if (temp_module.getContent() != null)
							temp_module.getContent().setVisibility(View.GONE);
					}
					break;
				case SHOW_EXCEPTION_CHANNEL_MODE:
					if (temp_module != null && isShowException(temp_module)
							&& channel == temp_module.getChannel()
							&& isArm == temp_module.getMode()) {
						if (temp_module.getContent() != null)
							temp_module.getContent()
									.setVisibility(View.VISIBLE);
					} else if (temp_module != null) {
						if (temp_module.getContent() != null)
							temp_module.getContent().setVisibility(View.GONE);
					}
					break;
				case REMOVE_FILTERATION:
					if (temp_module != null)
						if (temp_module.getContent() != null)
							temp_module.getContent()
									.setVisibility(View.VISIBLE);
					break;
				default:
					break;
				}
			}

			if (channelGridListener != null)
				channelGridListener.onChannelUpdate(
						DialogChannelGrid.STATUS_ACTIVE_CHANNEL, List_Channel);

			// adapter_channel.notifyDataSetChanged();
		}

	}

	private static final int SHOW_EXCEPTIONS = 1;
	private static final int SHOW_ALL = 2;
	private int CounterType = SHOW_EXCEPTIONS;

	// this function check if there is an exception in module
	private Boolean isShowExceptionYes(ModuleUIRow temp_module) {
		if (temp_module != null
				&& ((temp_module.getSignal() >= 70 && temp_module.getSignal() <= 99)
						|| (temp_module.getPower_1() >= 0 && temp_module
								.getPower_1() <= 3)
						|| (temp_module.getPower_2() >= 0 && temp_module
								.getPower_2() <= 3)
						|| temp_module.getHaveExtraCues() || (temp_module
						.getKey_Pos() && PersistentHeader.Mode == Cobra.MODE_ARMED))) {
			return true;
		} else {
			return false;
		}
	}

	// Exception Module counter function
	// This function update each modules exception to exception button.
	private void FilterModulesCounter() {

		ExcptionModuleConter = 0;

		ArrayList<ModuleUIRow> ListModulesUI = globV.getModuleUI_List();

		if (ListModulesUI != null) {
			for (int i = 0; i < ListModulesUI.size(); i++) {
				ModuleUIRow temp_module = ListModulesUI.get(i);
				try {
					if (temp_module != null && isShowExceptionYes(temp_module)) {
						ExcptionModuleConter++;
					}
				} catch (Exception ex) {
				}
			}

		} else {
		}

		if (CounterType == SHOW_ALL) {

			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {

					if (globV.getModuleUI_List() != null)
						btn_Showexceptions.setText("SHOW ALL "
								+ globV.getModuleUI_List().size());
				}
			}, 20);

		} else if (CounterType == SHOW_EXCEPTIONS) {

			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {

					btn_Showexceptions.setText(ExcptionModuleConter
							+ " EXCEPTIONS");
					btn_Showexceptions
							.setBackgroundResource(R.drawable.button_exceptions);
					btn_Showexceptions.setTextColor(getResources().getColor(
							R.color.cues_text_color));

				}
			}, 20);
		}
		// else {//lll
		// btn_Showexceptions
		// .setBackgroundResource(R.drawable.button_exceptions);
		// btn_Showexceptions.setTextColor(getResources().getColor(
		// R.color.cues_text_color));
		// }
	}

	private int cnt = 0;

	private Boolean isShowException(ModuleUIRow temp_module) {
		if (temp_module != null
				&& ((temp_module.getSignal() >= 70 && temp_module.getSignal() <= 99)
						|| (temp_module.getPower_1() >= 0 && temp_module
								.getPower_1() <= 3)
						|| (temp_module.getPower_2() >= 0 && temp_module
								.getPower_2() <= 3)
						|| temp_module.getHaveExtraCues() || (temp_module
						.getKey_Pos() && PersistentHeader.Mode == Cobra.MODE_ARMED))) {
			return true;
		} else {
			return false;
		}
	}

	private int getFilteationState(Boolean filterChannel, Boolean filterMode,
			Boolean showException) {
		if (filterChannel && filterMode && showException) {
			return SHOW_EXCEPTION_CHANNEL_MODE;
		} else if (filterChannel && filterMode) {
			return CHANNEL_MODE;
		} else if (filterChannel && showException) {
			return SHOW_EXCEPTION_CHANNEL;
		} else if (filterMode && showException) {
			return SHOW_EXCEPTION_MODE;
		} else if (filterChannel) {
			return CHANNEL;
		} else if (filterMode) {
			return MODE;
		} else if (showException) {
			return SHOW_EXCEPTION;
		} else
			return REMOVE_FILTERATION;
	}

	private void SortChannelList(ArrayList<String> list) {
		String temp;
		if (list != null && list.size() > 1) {

			for (int x = 0; x < list.size(); x++) {
				for (int i = 1; i < list.size() - x - 1; i++) {
					int first = Integer.parseInt(list.get(i));
					int second = Integer.parseInt(list.get(i + 1));
					if (first > second) {
						temp = list.get(i);
						list.set(i, list.get(i + 1));
						list.set(i + 1, temp);
					}

				}
			}

		}

	}

	// /////////////////////UI handler message received for modules
	// Here we receive modules id list fist time and also for update in any
	// module

	private final Handler UIhandler = new Handler() {

		public void handleMessage(Message msg) {

			ArrayList<Integer> module_ids = null;
			module_ids = msg.getData().getIntegerArrayList("modules_ids");

			if (module_ids != null) {

				// this function in which we show all modules and their updates
				// to screen

				ReceivedModulesData(module_ids, getActivity());

				// exception counter
				FilterModulesCounter();

				MainActivity.IsModuleTimerRunning = false;

				MainActivity.KeepCallingModules = true;
				MainActivity.moduleIDs = null;

				// Here we call next module from 18H2 after updating UI
				if (appClass.getFlagModuleUpate() == CobraFlags.READY_TO_CALL_NEXT_MODULE_DATA
						&& appClass.IsNewModuleId()) {

					appClass.setLastModuleId();
					cobra.AckOwnFlagStatusChange(555);

					appClass.setFlagModuleUpate(CobraFlags.WAIT_FOR_REQUESTED_MODULE_DATA);

					cobra.getNext_QUEUED_MODULE_DATA();
				}

			} else {

			}

		}
	};

	public Handler getHandler() {
		return UIhandler;
	}

	private void ReceivedModulesData(final ArrayList<Integer> module_ids,
			final Context context) {

		DialogContinuity continuityDialog = DialogContinuity.getOldInstance();

		int module_UI_ListIndex = -1;
		int modID = -1;
		// ArrayList<Integer> module_ids = null;

		appClass.last_module_called = System.currentTimeMillis();

		ArrayList<ModuleUIRow> ModuleUI_List = globV.getModuleUI_List();
		cobra.AckOwnFlagStatusChange(666);

		if (module_ids != null && hashMap != null) {

			for (int k = 0; k < module_ids.size(); k++) {
				module_UI_ListIndex = -1;
				modID = module_ids.get(k);
				if (continuityDialog != null) {// comment this temprarily
												// refresh cues on continuity
												// dilaog
					continuityDialog.refresh(modID);
				}
				ModuleUI_List = globV.getModuleUI_List();

				// for update Persistent Header
				if (modID == -2) {
					if (((MainActivity) getActivity()).fragPersistentHeader != null)
						((MainActivity) getActivity()).fragPersistentHeader
								.setHeaderData(modID, false);
					continue;
				}

				ModuleListItem item = hashMap.get(modID);

				// waqar
				if (item != null && item.getModuleTag() != null) {
					boolean isArmed = item.getModuleTag().armed;
					if (((MainActivity) getActivity()).fragPersistentHeader != null)
						((MainActivity) getActivity()).fragPersistentHeader
								.setHeaderData(modID, isArmed);
				}
				// waqar

				if (modID >= 0 && item != null) {
					if (ModuleUI_List != null && ModuleUI_List.size() > 0) {

						String modInSTR = "|- ";
						for (int x = 0; x < module_ids.size(); x++) {
							modInSTR = modInSTR + module_ids.get(x) + ", ";
						}

						// This is loop for each module to show on screen
						// and add update
						for (int i = 0; i < ModuleUI_List.size(); i++) {
							if (ModuleUI_List.get(i).getModuleid() == modID) {
								module_UI_ListIndex = i;
								// ModuleUI_List
								// .get(i).getPosition();
								// globV.getModuleUI_List().get(i).setPosition(i);

								break;
							}
						}
						if (module_UI_ListIndex != -1) {

							ModuleUIRow module = ModuleUI_List
									.get(module_UI_ListIndex);

							// Here we update each module
							ModuleRow
									.updateViewData(context,
											module_UI_ListIndex, item, module,
											item.getModuleTag(),
											globV.getCurrentChannel(),
											item.getChannelTag(),
											item.getLightStatus());

							// appClass.setAN188Log("LIght status for cues: "
							// + item.getLightStatus());

							// exception module update
							FilterModulesCounter();

							FilterModules(DoFilterationChannel,
									DoFilterationMode,
									DoFilterationShowException, FilterChannel,
									FilterisArm);

							continue;
						}
					} else {
						String modInSTR = "|- ";
						for (int x = 0; x < module_ids.size(); x++) {
							modInSTR = modInSTR + module_ids.get(x) + ", ";
						}
						appClass.setAN192_DEMOLog("Else case at line: 997 : "
								+ modInSTR);

					}

					// Here we show each module to screen that comes from
					// 18H2
					if (ModuleUI_List == null) {

						View v = getView(item, 0, context);

						if (v != null) {
							// ModuleUI_List =
							// globV.getModuleUI_List();
							int index = globV.getModuleUI_List().size();
							index--;
							linear_ListView.addView(v, index);

						} else {
							String modInSTR = "|- ";
							for (int x = 0; x < module_ids.size(); x++) {
								modInSTR = modInSTR + module_ids.get(x) + ", ";
							}
							appClass.setAN192_DEMOLog("Else case at line: 1020 : "
									+ modInSTR);

						}
					} else {
						View v = getView(item, ModuleUI_List.size(), context);
						// ModuleUI_List =
						// globV.getModuleUI_List();
						int index = globV.getModuleUI_List().size();
						index--;
						if (v != null) {
							if (module_UI_ListIndex != -1) {

								linear_ListView.addView(v, module_UI_ListIndex);

							} else {

								linear_ListView.addView(v, index);
							}

						} else {
							String modInSTR = "|- ";
							for (int x = 0; x < module_ids.size(); x++) {
								modInSTR = modInSTR + module_ids.get(x) + ", ";
							}
							appClass.setAN192_DEMOLog("Else case at line: 1045 : "
									+ modInSTR);

						}
					}

				} else {

					String modInSTR = "|- ";
					for (int x = 0; x < module_ids.size(); x++) {
						modInSTR = modInSTR + module_ids.get(x) + ", ";
					}
					appClass.setAN192_DEMOLog("Else case at line: 1057 : "
							+ modInSTR);
				}
			}
		} else {
			String modInSTR = "|- ";
			for (int x = 0; x < module_ids.size(); x++) {
				modInSTR = modInSTR + module_ids.get(x) + ", ";
			}

			appClass.setAN192_DEMOLog("Else case at line: 1067 : " + modInSTR);

		}

	}

	BroadcastReceiver NotifyDataSetChangeReciever = new BroadcastReceiver() {
		@SuppressWarnings("null")
		@Override
		public void onReceive(final Context context, final Intent intent) {
			// TODO Auto-generated method stub
			// if (!intent.hasExtra("CHANNEL")) {
			// new Handler().postDelayed(new Runnable() {
			// @Override
			// public void run() {
			//
			// int module_UI_ListIndex = -1;
			// int modID = -1;
			// ArrayList<Integer> module_ids = null;
			// appClass.setModuleLogOnDevice("8 : Broadcast Recieved");
			//
			// appClass.last_module_called = System.currentTimeMillis();
			// // try
			// {
			// ArrayList<ModuleUIRow> ModuleUI_List = globV
			// .getModuleUI_List();
			// cobra.AckOwnFlagStatusChange(666);
			//
			// if (intent.hasExtra("modIDs") && hashMap != null) {
			// module_ids = intent.getExtras()
			// .getIntegerArrayList("modIDs");
			//
			// String temp = "";
			//
			// // Toast.makeText(context, ""+module_ids.size(),
			// // Toast.LENGTH_LONG).show();
			//
			// for (int i = 0; i < module_ids.size(); i++) {
			// temp += module_ids.get(i) + " , ";
			// }
			//
			// if (MainActivity.moduleIDs != null)
			// module_ids = MainActivity.moduleIDs;
			//
			// temp = "";
			// for (int i = 0; i < module_ids.size(); i++) {
			// temp += module_ids.get(i) + " , ";
			// }
			//
			//
			// appClass.setModuleLogOnDevice2("UI RECIEVED IDs FROM MAINACTIVITY= "
			// + temp);
			// appClass.setAN106Log("UI RECIEVED IDs FROM MAINACTIVITY= "
			// + temp);
			// for (int k = 0; k < module_ids.size(); k++) {
			// module_UI_ListIndex = -1;
			// modID = module_ids.get(k);
			// ModuleUI_List = globV.getModuleUI_List();
			//
			// // for update Persistent Header
			// if (modID == -2) {
			// if (((MainActivity) getActivity()).fragPersistentHeader != null)
			// ((MainActivity) getActivity()).fragPersistentHeader
			// .setHeaderData(modID, false);
			// continue;
			// }
			//
			// ModuleListItem item = hashMap.get(modID);
			//
			// // waqar
			// if (item != null && item.getModuleTag() != null) {
			// boolean isArmed = item.getModuleTag().armed;
			// if (((MainActivity) getActivity()).fragPersistentHeader != null)
			// ((MainActivity) getActivity()).fragPersistentHeader
			// .setHeaderData(modID, isArmed);
			// }
			// // waqar
			//
			// if (modID >= 0 && item != null) {
			// if (ModuleUI_List != null
			// && ModuleUI_List.size() > 0) {
			// for (int i = 0; i < ModuleUI_List
			// .size(); i++) {
			// if (ModuleUI_List.get(i)
			// .getModuleid() == modID) {
			// module_UI_ListIndex = i;
			// // ModuleUI_List
			// // .get(i).getPosition();
			// // globV.getModuleUI_List().get(i).setPosition(i);
			//
			// break;
			// }
			// }
			// if (module_UI_ListIndex != -1) {
			// appClass.setModuleLogOnDevice("10 : Module UI updated");
			//
			// ModuleUIRow module = ModuleUI_List
			// .get(module_UI_ListIndex);
			//
			// ModuleRow.updateViewData(context,
			// module_UI_ListIndex, item,
			// module,
			// item.getModuleTag(),
			// globV.getCurrentChannel(),
			// item.getChannelTag(),
			// item.getLightStatus());
			// FilterModules(DoFilterationChannel,
			// DoFilterationMode,
			// DoFilterationShowException,
			// FilterChannel, FilterisArm);
			//
			// appClass.setAN106Log("UPDATED MODULE ID= "
			// + modID);
			// continue;
			// }
			// }
			// if (ModuleUI_List == null) {
			// appClass.setModuleLogOnDevice2("MODULES UI CREATED");
			// View v = getView(item, 0, context);
			// if (v != null) {
			// // ModuleUI_List =
			// // globV.getModuleUI_List();
			// int index = globV
			// .getModuleUI_List().size();
			// index--;
			// appClass.setModuleLogOnDevice("11 : New Module UI created");
			// linear_ListView.addView(v, index);
			// appClass.setModuleLogOnDevice("Linear CHILD : "
			// + linear_ListView
			// .getChildCount());
			//
			// }
			// } else {
			// View v = getView(item,
			// ModuleUI_List.size(), context);
			// // ModuleUI_List =
			// // globV.getModuleUI_List();
			// int index = globV.getModuleUI_List()
			// .size();
			// index--;
			// if (v != null) {
			// if (module_UI_ListIndex != -1) {
			// // appClass.setModuleLogOnDevice("11 : New Module UI created");
			// linear_ListView.addView(v,
			// module_UI_ListIndex);
			//
			// } else {
			//
			// linear_ListView.addView(v,
			// index);
			// }
			//
			// }
			// }
			// }
			// }
			// }
			// }
			// // catch (Exception e)
			// // {
			// // appClass.setAN106Log("Exception: " + e.getMessage());
			// // }
			// MainActivity.KeepCallingModules = true;
			// MainActivity.moduleIDs = null;
			//
			// if (appClass.getFlagModuleUpate() ==
			// CobraFlags.READY_TO_CALL_NEXT_MODULE_DATA
			// && appClass.IsNewModuleId()) {
			// appClass.setLastModuleId();
			// cobra.AckOwnFlagStatusChange(555);
			//
			// appClass.setFlagModuleUpate(CobraFlags.WAIT_FOR_REQUESTED_MODULE_DATA);
			//
			// // appClass.setAN168Log("MODULE LIST CALLING");
			// cobra.getNext_QUEUED_MODULE_DATA();
			// }
			// }
			// }, 100);
		}
	};

	// Here we adding row to screen it is returns each row Type View
	public View getView(ModuleListItem item, final int position, Context context) {

		if (item == null)
			return null;
		final ModuleDataTag module = item.getModuleTag();

		// This is ModuleRow class that returns View for each module row
		ModuleRow row = new ModuleRow(position, context, item.getModuleTag(),
				globV.getCurrentChannel(), item.getChannelTag(),
				item.getLightStatus());

		final ModuleUIRow UIrow = new ModuleUIRow();
		UIrow.setModuleListItem(item);

		UIrow.setModuleid(module.modID);
		UIrow.setChannel(item.getModuleTag().currentChannel);
		UIrow.setAddress(item.getModuleTag().modID);
		UIrow.setDevice(item.getModuleTag().modType);
		UIrow.setKey_Pos(item.getModuleTag().keyPos);
		UIrow.setMode(item.getModuleTag().armed);
		UIrow.setPower_1(item.getModuleTag().batteryLevel1);
		UIrow.setPower_2(item.getModuleTag().batteryLevel2);
		UIrow.setSignal(item.getModuleTag().linkQuality);

		UIrow.setHaveExtraCues(row.getHaveExtraCues());
		UIrow.setContent(row.getContent());
		UIrow.setCuesParent(row.getCuesParent());
		UIrow.setParentLayout(row.getParentLayout());
		UIrow.setPosition(position);
		UIrow.setTvAddress(row.getTvAddress());
		UIrow.setTvBattery1(row.getTvBattery1());
		UIrow.setTvBattery2(row.getTvBattery2());
		UIrow.setTvChannel(row.getTvChannel());
		UIrow.setTvCues(row.getTvCues());
		UIrow.setTvDevice(row.getTvDevice());
		UIrow.setTvKeyPosition(row.getTvKeyPosition());
		UIrow.setTvMode(row.getTvMode());
		UIrow.setTvSignal(row.getTvSignal());

		UIrow.getCuesParent().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentTransaction ft = (getActivity())
						.getSupportFragmentManager().beginTransaction();

				Fragment prev = (getActivity()).getSupportFragmentManager()
						.findFragmentByTag("dialog");

				if (prev != null) {
					ft.remove(prev);
				}

				ft.addToBackStack(null);

				// Create and show the dialog.
				DialogFragment newFragment = DialogContinuity.newInstance(1,
						module.modID);
				newFragment.show(ft, "dialog");

				// try {
				// MyCueDialog dialog = new MyCueDialog(getContext(),
				// activity, module.modID);
				// dialog.show();
				// } catch (Exception e) {
				// Toast.makeText(getContext(), "crashed" + e.getMessage(),
				// Toast.LENGTH_LONG).show();
				// }
			}
		});
		globV.setModuleUI_List(UIrow);

		FilterModules(DoFilterationChannel, DoFilterationMode,
				DoFilterationShowException, FilterChannel, FilterisArm);

		// exception counter
		FilterModulesCounter();

		return row;

	}

	OnModuleInformationUpdate onModuleInformationUpdate = new OnModuleInformationUpdate() {

		@Override
		public void onRemoveAllModule() {
			// TODO Auto-generated method stub
			removeAllModules();
		}

		@Override
		public void onModuleStatusUpdate(ModuleDataTag moduleDataTag,
				ArrayList<String> lightStatus) {

			updateModuleRow(moduleDataTag, lightStatus);

		}
	};

	// / refresh button overlay
	public Button getRefreshButton() {
		return refreshButton;
	}

	OnClickListener refreshButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			((MainActivity) getActivity()).refreshPressed();

		}
	};

	OnClickListener helpButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			((MainActivity) getActivity()).helpPressed();
		}
	};

	OnItemSelectedListener d = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// ((MainActivity) getActivity()).setNoOfTries(items[arg2]);

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	};

	public void showDialogDemo() {

		FragmentTransaction ft = ((FragmentActivity) getActivity())
				.getSupportFragmentManager().beginTransaction();

		Fragment prev = ((FragmentActivity) getActivity())
				.getSupportFragmentManager().findFragmentByTag("dialog");

		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		DialogFragment newFragment = DialogDemo.newInstance(1, "", 1);
		newFragment.show(ft, "dialog");

	}

	public void showDialogBuy() {

		if (!appClass.is_show_buy_dialog) {
			appClass.is_show_buy_dialog = true;

			FragmentTransaction ft = ((FragmentActivity) getActivity())
					.getSupportFragmentManager().beginTransaction();

			Fragment prev = ((FragmentActivity) getActivity())
					.getSupportFragmentManager().findFragmentByTag("dialog");

			if (prev != null) {
				ft.remove(prev);
			}
			ft.addToBackStack(null);
			// Create and show the dialog.
			DialogFragment newFragment = DialogBuy.newInstance(1, "", 1);
			newFragment.show(ft, "dialog");
		}
	}

}

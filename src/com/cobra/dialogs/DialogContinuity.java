package com.cobra.dialogs;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cobra.R;
import com.cobra.appClass;
import com.cobra.api.Cobra;
import com.cobra.api.CobraDataTags.EventDataTag;
import com.cobra.classes.Cues.ChannelCuesData;
import com.cobra.classes.Cues.CuesData;
import com.cobra.classes.Cues.ScriptData;
import com.cobra.views.modulelist.ModuleList;
import com.cobra.views.modulelist.ModuleListItem;

public class DialogContinuity extends DialogFragment {
	int mNum;

	private int NUM_PAGES = -1;

	private int modID;

	private TextView txt_Channel;

	private appClass globV;
	private TextView txt_Counter;

	private Cobra cobra;

	private LinearLayout linear_ListView;
	private RelativeLayout layout_ActionBar;

	private SparseArray<SparseArray<EventDataTag>> events;
	ArrayList<ChannelCuesData> channelCuesDataList;

	private TextView txt_ScriptName;
	private ArrayList<String> ListScriptName;

	private TextView btn_Close;
	private static long STEP_CONST = 1048576;
	private ImageView img_Swipe;

	private int currentPage = 0;
	private int refreshCounter = 0;
	static DialogContinuity f = null;

	/**
	 * Create a new instance of MyDialogFragment, providing "num" as an
	 * argument.
	 */
	public static DialogContinuity getOldInstance() {

		return f;
	}

	// Boolean UserScrollView = false;

	/**
	 * Create a new instance of MyDialogFragment, providing "num" as an
	 * argument.
	 */
	public static DialogContinuity newInstance(int num, int modID) {
		f = new DialogContinuity();
		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putInt("num", num);
		args.putInt("modID", modID);
		f.setArguments(args);

		return f;
	}

	int heightOfScreen;
	int widthOfScreen;

	private RelativeLayout layout_Top;

	private ModuleListItem module;

	private ImageView btn_Left;

	private ImageView btn_Right;

	private TextView btn_refresh;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mNum = getArguments().getInt("num");
		modID = getArguments().getInt("modID");
		globV = (appClass) getActivity().getApplicationContext();
		listener = globV.getDiloagListener();
		// Pick a style based on the num.
		int style = DialogFragment.STYLE_NORMAL, theme = 0;
		switch (1) {
		case 1:
			style = DialogFragment.STYLE_NO_TITLE;
			break;
		case 2:
			style = DialogFragment.STYLE_NO_FRAME;
			break;
		case 3:
			style = DialogFragment.STYLE_NO_INPUT;
			break;
		case 4:
			style = DialogFragment.STYLE_NORMAL;
			break;
		case 5:
			style = DialogFragment.STYLE_NORMAL;
			break;
		case 6:
			style = DialogFragment.STYLE_NO_TITLE;
			break;
		case 7:
			style = DialogFragment.STYLE_NO_FRAME;
			break;
		case 8:
			style = DialogFragment.STYLE_NORMAL;
			break;
		}
		switch (2) {
		case 4:
			theme = android.R.style.Theme_Holo;
			break;
		case 5:
			theme = android.R.style.Theme_Holo_Light_Dialog;
			break;
		case 6:
			theme = android.R.style.Theme_Holo_Light;
			break;
		case 7:
			theme = android.R.style.Theme_Holo_Light_Panel;
			break;
		case 8:
			theme = android.R.style.Theme_Holo_Light;
			break;
		}
		setStyle(style, theme);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dialog_scripts_cues, container,
				false);

		cobra = globV.getCobra();

		layout_Top = (RelativeLayout) v.findViewById(R.id.layout_top);
		linear_ListView = (LinearLayout) v.findViewById(R.id.cues_number_View);
		btn_Left = (ImageView) v.findViewById(R.id.left_button);
		btn_Right = (ImageView) v.findViewById(R.id.right_button);

		btn_Left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				currentPage--;
				if ((currentPage > 0) && (currentPage <= NUM_PAGES)) {
					refreshCounter = refreshCounter - 1;
					btn_Left.setImageResource(R.drawable.button_left_arrow);
					btn_Left.setEnabled(true);
					updateSelectorTab(currentPage);
					UpdateScriptInfo(currentPage - 1);
				}
				if (currentPage < 2) {
					btn_Left.setImageResource(R.drawable.left_arrow_clicked);
					;
					btn_Left.setEnabled(false);
				}
				if (NUM_PAGES > 1 && currentPage < NUM_PAGES) {
					btn_Right.setImageResource(R.drawable.button_right_arrow);
					btn_Right.setEnabled(true);

				}
			}
		});

		btn_Right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentPage++;
				if ((currentPage > 0) && (currentPage <= NUM_PAGES)) {

					refreshCounter = refreshCounter + 1;
					btn_Right.setImageResource(R.drawable.button_right_arrow);
					btn_Right.setEnabled(true);
					UpdateScriptInfo(currentPage);
					updateSelectorTab(currentPage);
				}
				if (currentPage == NUM_PAGES) {
					btn_Right.setImageResource(R.drawable.right_arrow_clicked);
					btn_Right.setEnabled(false);
				}
				if (currentPage > 1 && NUM_PAGES > 0
						&& currentPage <= NUM_PAGES) {
					btn_Left.setImageResource(R.drawable.button_left_arrow);
					btn_Left.setEnabled(true);
				}

			}
		});

		if (globV.getListScriptName() != null
				&& globV.getListScriptName().size() > 0) {
			NUM_PAGES = globV.getListScriptName().size() - 1;

			// //////////////////////////
			ArrayList<ScriptData> scriptDataList = new ArrayList<ScriptData>();
			channelCuesDataList = new ArrayList<ChannelCuesData>();

			/*
			 * Converting general event data for continuity dialog. It belongs
			 * to the issue no AN-120 Here we need to separate all events
			 * channel wise and script wise
			 */
			if (cobra != null) {

				events = cobra.getEventsData();

				if (events != null) {

					/*
					 * Name of all the scripts
					 */
					for (int i = 0; i < globV.getListScriptName().size(); i++) {

						SparseArray<EventDataTag> eventDataTagList = events
								.get(i);
						if (eventDataTagList != null) {
							for (int j = 0; j < eventDataTagList.size(); j++) {
								if (eventDataTagList.get(j) != null) {

									EventDataTag eventDataTag = eventDataTagList
											.get(j);
									/*
									 * return no of cues to fire event
									 * tranlating this byte data in to readable
									 * formate to understand the actual cues
									 */
									ArrayList<String> cuesListArray = getCuesArray(eventDataTag.cueList);
									for (int k = 0; k < cuesListArray.size(); k++) {
										int scriptNo = i;
										int channel = eventDataTag.channel;
										int cue = Integer
												.parseInt(cuesListArray.get(k));

										String eventTime = "";
										/*
										 * checking event that its a STEP event
										 * or normal event. if time index is
										 * less than STEP_CONST then its normal
										 * event. othewise its step event
										 */
										if (eventDataTag.timeIndex < STEP_CONST) {
											eventTime = tenthsToTimeFormat(eventDataTag.shiftedTimeIndex);
										} else {
											eventTime = "STEP";
										}
										String desc = eventDataTag.eventDescription;

										setChannelData(channel, scriptNo, cue,
												eventTime, desc);

									}

								}
							}
						}
					}
				}
			}
			globV.setChannelCueData(channelCuesDataList);

			txt_Channel = (TextView) v.findViewById(R.id.txt_channel_value);
			txt_ScriptName = (TextView) v.findViewById(R.id.txt_script_name);
			btn_Close = (TextView) v.findViewById(R.id.btn_close);
			txt_Counter = (TextView) v.findViewById(R.id.txt_script_counter);
			img_Swipe = (ImageView) v.findViewById(R.id.img_swipe);

			btn_refresh = (TextView) v.findViewById(R.id.btn_refresh);

			btn_refresh.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					UpdateScriptInfo(refreshCounter);

				}
			});

			btn_Close.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					DialogContinuity.this.dismiss();
				}
			});

			if (NUM_PAGES == 1) {
				img_Swipe.setVisibility(View.GONE);
				txt_Counter.setVisibility(View.GONE);
			} else {
				txt_Counter.setVisibility(View.VISIBLE);
				txt_Counter.setText("Script " + 1 + " of " + NUM_PAGES);
			}
			if (ModuleList.hashMap != null) {
				module = ModuleList.hashMap.get(modID);
				int channel = module.getModuleTag().currentChannel;
				txt_Channel.setText("" + channel);
			}

			ListScriptName = globV.getListScriptName();
			if (ListScriptName != null) {
				String scriptName = ListScriptName.get(0);
				txt_ScriptName.setText(scriptName);
			}
			UpdateScriptInfo(currentPage++);
			if (NUM_PAGES == 1) {
				btn_Left.setImageResource(R.drawable.left_arrow_clicked);
				btn_Left.setEnabled(false);
				btn_Right.setImageResource(R.drawable.right_arrow_clicked);
				btn_Right.setEnabled(false);

			} else {
				btn_Right.setImageResource(R.drawable.button_right_arrow);
				btn_Right.setEnabled(true);
				btn_Left.setImageResource(R.drawable.left_arrow_clicked);
				btn_Left.setEnabled(false);
			}
		} else {
			Toast.makeText(getActivity(), "No script found", Toast.LENGTH_SHORT)
					.show();
			dismiss();
		}

		layout_Top.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		return v;

	}

	private void UpdateScriptInfo(int pageNo) {

		if (ModuleList.hashMap != null) {
			module = ModuleList.hashMap.get(modID);
			int channel = module.getModuleTag().currentChannel;
			txt_Channel.setText("" + channel);
		}
		if (module != null) {
			if (ListScriptName != null) {
				String scriptName = ListScriptName.get(pageNo);
				txt_ScriptName.setText(scriptName);
			}
			linear_ListView.removeAllViews();
			int channel = module.getModuleTag().currentChannel;
			ArrayList<String> cue_List = module.getLightStatus();
			long cueCont = module.getModuleTag().testResults;
			long scriptCues = module.getChannelTag().scriptCues;

			for (int i = 0; i < 18; i++) {

				CUE_COLOR = CUE_DEFAULT;

				// /////////////
				Boolean IsScriptFound = false;
				int scriptIndex = -1;
				ArrayList<ScriptData> scriptList = null;
				if (channelCuesDataList != null
						&& channelCuesDataList.size() > 0) {
					for (int a = 0; a < channelCuesDataList.size(); a++) {
						Boolean IsChannelFound = false;
						if (channelCuesDataList.get(a).getChannelID() == channel) {
							scriptList = channelCuesDataList.get(a)
									.getScriptDataList();

							if (scriptList != null) {
								for (int j = 0; j < scriptList.size(); j++) {
									if (scriptList.get(j).getScriptID() == pageNo) {
										IsScriptFound = true;
										scriptIndex = j;
									}

									// //
									// ArrayList<CuesData> cueList =
									// scriptList.get(j)
									// .getCuesDataList();
									// for (int k = 0; k < cueList.size(); k++)
									// {
									//
									// if (cueList.get(k).getCueNo() == (i + 1))
									// {
									// IsScriptFound = true;
									// } else {
									// IsScriptFound = false;
									// }
									// }
									// // //

									if (IsScriptFound)
										break;

								}

							}

						}
						if (IsChannelFound)
							break;
					}
				}

				// ////////////////////

				boolean isThisCueAvailable = false;
				if (IsScriptFound) {
					if (cue_List != null) {
						for (int j = 0; j < cue_List.size(); j++) {
							if (cue_List.get(j) != null
									&& cue_List.get(j).equals((i + 1) + "")) {
								isThisCueAvailable = true;
								break;
							}

						}
					}

				}

				if (i != 0 && (i % 6) == 0) {
					// cue_List = cue_List >> 2;
					cueCont = cueCont >> 2;
					scriptCues = scriptCues >> 2;
				}

				if (isThisCueAvailable) {
					CUE_COLOR = CUE_RED;
				}
				if ((cueCont & 0x1) == 0x1) {
					CUE_COLOR = CUE_GREEN;
				}

				cueCont = cueCont >> 1;
				scriptCues = scriptCues >> 1;
				// cue_List = cue_List >> 1;

				linear_ListView.addView(getView(i, pageNo, channel, CUE_COLOR));
			}
		}
	}

	private void setChannelData(int Channel, int ScriptNo, int Cue,
			String EventTime, String Desc) {
		Boolean IsFound = false;
		for (int i = 0; i < channelCuesDataList.size(); i++) {
			ChannelCuesData channelCuesData = channelCuesDataList.get(i);

			if (channelCuesData.getChannelID() == Channel) {
				IsFound = true;
				ArrayList<ScriptData> scriptList = channelCuesData
						.getScriptDataList();
				channelCuesData.setScriptDataList(UpdateScriptData(scriptList,
						ScriptNo, Cue, Desc, EventTime));
			}
		}
		if (!IsFound) {
			ChannelCuesData channelCuesData = new ChannelCuesData();
			channelCuesData.setChannelID(Channel);
			ArrayList<ScriptData> scriptList = channelCuesData
					.getScriptDataList();
			channelCuesData.setScriptDataList(UpdateScriptData(scriptList,
					ScriptNo, Cue, Desc, EventTime));
			channelCuesDataList.add(channelCuesData);
		}

	}

	private ArrayList<ScriptData> UpdateScriptData(
			ArrayList<ScriptData> scriptList, int ScriptNo, int Cue,
			String Desc, String EventTime) {

		if (scriptList == null) {
			scriptList = new ArrayList<ScriptData>();

			ScriptData scriptData = new ScriptData();
			scriptData.setScriptID(ScriptNo);
			ArrayList<CuesData> cueList = scriptData.getCuesDataList();
			scriptData.setCuesDataList(UpdateCuesData(cueList, Cue, Desc,
					EventTime));
			scriptList.add(scriptData);

			return scriptList;
		}

		Boolean IsFound = false;
		for (int j = 0; j < scriptList.size(); j++) {
			ScriptData scriptData = scriptList.get(j);
			if (scriptData.getScriptID() == ScriptNo) {
				IsFound = true;
				ArrayList<CuesData> cueList = scriptData.getCuesDataList();
				scriptData.setCuesDataList(UpdateCuesData(cueList, Cue, Desc,
						EventTime));
				return scriptList;
			}

		}

		if (!IsFound) {
			ScriptData scriptData = new ScriptData();
			scriptData.setScriptID(ScriptNo);
			ArrayList<CuesData> cueList = scriptData.getCuesDataList();
			scriptData.setCuesDataList(UpdateCuesData(cueList, Cue, Desc,
					EventTime));

			scriptList.add(scriptData);
		}

		return scriptList;
	}

	private ArrayList<CuesData> UpdateCuesData(ArrayList<CuesData> cueList,
			int Cue, String Desc, String EventTime) {
		if (cueList != null && cueList.size() > 0) {
			Boolean IsFound = false;

			for (int k = 0; k < cueList.size(); k++) {
				if (cueList.get(k).getCueNo() == Cue) {
					IsFound = true;
					break;
				}
			}
			if (!IsFound) {
				CuesData cueData = new CuesData();
				cueData.setCueNo(Cue);
				cueData.setEventTime(EventTime);
				cueData.setDesc(Desc);
				cueList.add(cueData);
			}
		} else {
			cueList = new ArrayList<CuesData>();
			CuesData cueData = new CuesData();
			cueData.setCueNo(Cue);
			cueData.setEventTime(EventTime);
			cueData.setDesc(Desc);
			cueList.add(cueData);
		}
		return cueList;
	}

	protected void updateSelectorTab(int position) {

		// Toast.makeText(getActivity(), position, Toast.LENGTH_SHORT).show();
		txt_Counter.setText("Script " + position + " of " + NUM_PAGES);

		if (ListScriptName != null) {
			String scriptName = ListScriptName.get(position - 1);
			txt_ScriptName.setText(scriptName);
		}
		// for (int i = 0; i < NUM_PAGES; i++) {
		// if (i == position) {
		// listSelectors.get(i).setImageResource(R.drawable.selected);
		// } else
		// listSelectors.get(i).setImageResource(R.drawable.unselected);
		// }
	}

	public static final int CUE_DEFAULT = R.drawable.modulerow_cue_default;
	public static final int CUE_RED = R.drawable.modulerow_cue_red;
	public static final int CUE_GREEN = R.drawable.modulerow_cue_green;
	public static final int CUE_RED_GREEN = R.drawable.modulerow_cue_yellow;
	private int CUE_COLOR = 0;

	public View getView(int position, int scriptNo, int channel,
			final int cueColor) {
		// TODO Auto-generated method stub
		View v = View.inflate(getActivity(),
				R.layout.list_item_dialog_scriptcue, null);

		if (position % 2 != 0) {
			v.setBackgroundColor(Color.LTGRAY);
		} else {
			v.setBackgroundColor(Color.WHITE);
		}

		final TextView txt_CueNo = (TextView) v.findViewById(R.id.txt_cue);
		TextView txt_EventTime = (TextView) v.findViewById(R.id.txt_eventtime);
		TextView txt_Desc = (TextView) v.findViewById(R.id.txt_desc);

		txt_CueNo.setBackgroundResource(CUE_DEFAULT);
		txt_CueNo.setText("" + (position + 1));

		boolean IsCueActive = false;

		if (channelCuesDataList != null && channelCuesDataList.size() > 0) {
			for (int i = 0; i < channelCuesDataList.size(); i++) {
				Boolean IsChannelFound = false;
				if (channelCuesDataList.get(i).getChannelID() == channel) {
					ArrayList<ScriptData> scriptList = channelCuesDataList.get(
							i).getScriptDataList();

					if (scriptList != null) {
						for (int j = 0; j < scriptList.size(); j++) {
							Boolean IsScriptFound = false;
							if (scriptList.get(j).getScriptID() == scriptNo) {
								IsScriptFound = true;
								ArrayList<CuesData> cueList = scriptList.get(j)
										.getCuesDataList();
								for (int k = 0; k < cueList.size(); k++) {

									if (cueList.get(k).getCueNo() == (position + 1)) {
										txt_EventTime.setText(cueList.get(k)
												.getEventTime());
										txt_Desc.setText(cueList.get(k)
												.getDesc());

										txt_CueNo
												.setBackgroundResource(CUE_COLOR);

										IsCueActive = true;
										break;
									}
								}

							}

							if (IsScriptFound)
								break;
						}

					}
				}
				if (IsChannelFound)
					break;
			}
		}

		if (cueColor == CUE_GREEN) {
			txt_CueNo.setBackgroundResource(CUE_GREEN);
			txt_CueNo.setTextColor(getResources().getColor(
					R.color.white_showcontrols));
		}
		if (cueColor == CUE_RED) {
			txt_CueNo.setBackgroundResource(CUE_RED);
			txt_CueNo.setTextColor(getResources().getColor(
					R.color.white_showcontrols));
		}

		if (!IsCueActive) {
			if (CUE_COLOR == CUE_GREEN) {

				txt_CueNo.setBackgroundResource(CUE_RED_GREEN);
				txt_CueNo.setTextColor(getResources().getColor(R.color.black));
			} else {

				txt_CueNo.setBackgroundResource(CUE_DEFAULT);
				txt_CueNo.setTextColor(getResources().getColor(R.color.black));

			}

		}

		return v;
	}

	public static String tenthsToTimeFormat(long t) {
		t = t * 100l;
		long hr = TimeUnit.MILLISECONDS.toHours(t);
		long min = TimeUnit.MILLISECONDS.toMinutes(t
				- TimeUnit.HOURS.toMillis(hr));
		long sec = TimeUnit.MILLISECONDS.toSeconds(t
				- TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min));
		long ms = TimeUnit.MILLISECONDS.toMillis(t
				- TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min)
				- TimeUnit.SECONDS.toMillis(sec));
		long tenths = ms / 100l;
		return String.format("%02d:%02d:%02d.%01d", hr, min, sec, tenths);
	}

	private static ArrayList<String> getCuesArray(long cues) {
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

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		widthOfScreen = (int) getResources().getDimension(
				R.dimen.script_dialog_width);
		heightOfScreen = (int) getResources().getDimension(
				R.dimen.script_dialog_height);
		// widthOfScreen = getResources().getDisplayMetrics().widthPixels;
		// heightOfScreen = getResources().getDisplayMetrics().heightPixels;

		getDialog().getWindow().setLayout(widthOfScreen, heightOfScreen);
	}

	private int getPercentage(int height, int percent) {
		float h = (percent * height);
		float a = h / 100;
		return (int) a;
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
		// listener.onDismiss();
		super.onDismiss(dialog);

	}

	onCueDailogDismiss listener;

	public static interface onCueDailogDismiss {
		public void onDismiss();
	}

	public void refresh(int modid) {
		try {
			if (getActivity() == null || isHidden() || isRemoving()
					|| !isVisible() || isDetached())
				return;
			int oldChannel = -1;
			if (module != null) {
				oldChannel = module.getModuleTag().currentChannel;
			}
			if (ModuleList.hashMap != null) {
				ModuleListItem thisModule = ModuleList.hashMap.get(modID);
				int channel = thisModule.getModuleTag().currentChannel;
				if (channel != oldChannel || modid == modID) {
					UpdateScriptInfo(refreshCounter);
				}
			}
		} catch (Exception ex) {

		}
	}
}
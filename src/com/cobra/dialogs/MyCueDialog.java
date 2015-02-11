package com.cobra.dialogs;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.app.Dialog;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cobra.MainActivity;
import com.cobra.R;
import com.cobra.appClass;
import com.cobra.api.Cobra;
import com.cobra.api.CobraDataTags.EventDataTag;
import com.cobra.classes.Cues.ChannelCuesData;
import com.cobra.classes.Cues.CuesData;
import com.cobra.classes.Cues.ScriptData;
import com.cobra.views.modulelist.ModuleList;
import com.cobra.views.modulelist.ModuleListItem;

public class MyCueDialog extends Dialog {

	public static final int CUE_DEFAULT = R.drawable.modulerow_cue_default;
	public static final int CUE_RED = R.drawable.modulerow_cue_red;
	public static final int CUE_GREEN = R.drawable.modulerow_cue_green;
	public static final int CUE_RED_GREEN = R.drawable.modulerow_cue_yellow;

	private ViewPager pager;
	public int modID;
	private List<View> data;
	private appClass globV;
	private Cobra cobra;
	private int NUM_PAGES;
	private static long STEP_CONST = 1048576;

	private ArrayList<ChannelCuesData> channelCuesDataList;
	private SparseArray<SparseArray<EventDataTag>> events;
	private Button btn_Close;
	private RelativeLayout layout_ActionBar;
	private TextView txt_Counter;
	private ImageView img_Swipe;
	private TextView txt_Channel;
	private ArrayList<String> ListScriptName;
	private TextView txt_ScriptName;
	private int CUE_COLOR = 0;
	private ModuleListItem module;

	public MyCueDialog(Context context, MainActivity activity, int modID) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_module_cues_pager);

		globV = (appClass) activity.getApplicationContext();
		cobra = globV.getCobra();

		pager = (ViewPager) findViewById(R.id.pager);
		this.modID = modID;
		// ////////////////////
		if (globV.getListScriptName() != null
				&& globV.getListScriptName().size() > 0) {
			NUM_PAGES = globV.getListScriptName().size() - 1;

			// //////////////////////////
			ArrayList<ScriptData> scriptDataList = new ArrayList<ScriptData>();
			channelCuesDataList = new ArrayList<ChannelCuesData>();
			if (cobra != null) {
				events = cobra.getEventsData();
				if (events != null) {
					for (int i = 0; i < globV.getListScriptName().size(); i++) {

						SparseArray<EventDataTag> eventDataTagList = events
								.get(i);
						if (eventDataTagList != null) {
							for (int j = 0; j < eventDataTagList.size(); j++) {
								if (eventDataTagList.get(j) != null) {

									EventDataTag eventDataTag = eventDataTagList
											.get(j);

									ArrayList<String> cuesListArray = getCuesArray(eventDataTag.cueList);
									for (int k = 0; k < cuesListArray.size(); k++) {
										int scriptNo = i;
										int channel = eventDataTag.channel;
										int cue = Integer
												.parseInt(cuesListArray.get(k));

										String eventTime = "";
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
			int test = scriptDataList.size();
			globV.setChannelCueData(channelCuesDataList);
			// /////////////////////////
			btn_Close = (Button) findViewById(R.id.btn_close);

			layout_ActionBar = (RelativeLayout) findViewById(R.id.layout_actionbar);

			txt_Counter = (TextView) findViewById(R.id.txt_script_counter);
			txt_Channel = (TextView) findViewById(R.id.txt_channel_value);
			txt_ScriptName = (TextView) findViewById(R.id.txt_script_name);

			img_Swipe = (ImageView) findViewById(R.id.img_swipe);
			//
			btn_Close.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					MyCueDialog.this.dismiss();
				}
			});

			if (NUM_PAGES == 1) {
//				img_Swipe.setVisibility(View.GONE);
//				txt_Counter.setVisibility(View.GONE);
			} else {
				txt_Counter.setVisibility(View.VISIBLE);
				txt_Counter.setText("Script " + 1 + " of " + NUM_PAGES);
			}
			if (ModuleList.hashMap != null) {
				ModuleListItem module = ModuleList.hashMap.get(modID);
				int channel = module.getModuleTag().currentChannel;
				txt_Channel.setText("" + channel);
			}

			ListScriptName = globV.getListScriptName();
			if (ListScriptName != null) {
				String scriptName = ListScriptName.get(0);
				txt_ScriptName.setText(scriptName);
			}

			data = new ArrayList<View>();
			getPagerView();
		} else {
			Toast.makeText(activity, "No script found", Toast.LENGTH_SHORT)
					.show();
			dismiss();
		}

		// ////////////////
		
		
		MyViewPagerAdapter adapter = new MyViewPagerAdapter(getContext(), data);
		pager.setAdapter(adapter);

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

	private class MyViewPagerAdapter extends PagerAdapter {

		private List<View> data;
		private Context ctx;

		public MyViewPagerAdapter(Context ctx, List<View> data) {
			this.ctx = ctx;
			this.data = data;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object instantiateItem(View collection, int position) {
			// TextView view = new TextView(ctx);
			// view.setText(data.get(position));
			View view = data.get(position);
			((ViewPager) collection).addView(view);
			return view;
		}

		@Override
		public void destroyItem(View collection, int position, Object view) {
			((ViewPager) collection).removeView((View) view);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public void startUpdate(View arg0) {
		}

		@Override
		public void finishUpdate(View arg0) {
		}
	}

	public void getPagerView() {
		if (ListScriptName != null) {
			for (int i = 0; i < ListScriptName.size(); i++) {

				data.add(getScriptView(i));
			}
		}

	}

	public View getScriptView(int mPageNumber) {

		View v = View.inflate(getContext(), R.layout.dialog_module_cues_test,
				null);
		LinearLayout layout = (LinearLayout) v
				.findViewById(R.id.layout_cue_container);

//		if (ModuleList.hashMap != null && modID >= 0) {
//			module = ModuleList.hashMap.get(modID);
//
//			int channel = module.getModuleTag().currentChannel;
//			long cue_List = module.getLightStatus();
//			long cueCont = module.getModuleTag().testResults;
//			long scriptCues = module.getChannelTag().scriptCues;
//
//			for (int i = 0; i < 18; i++) {
//
//				if (i != 0 && (i % 6) == 0) {
//					cue_List = cue_List >> 2;
//					cueCont = cueCont >> 2;
//					scriptCues = scriptCues >> 2;
//				}
//
//				CUE_COLOR = CUE_DEFAULT;
//
//				if ((cue_List & 0x1) == 0x1) {
//					CUE_COLOR = CUE_RED;
//				}
//				if ((cueCont & 0x1) == 0x1) {
//					CUE_COLOR = CUE_GREEN;
//				}
//
//				cueCont = cueCont >> 1;
//				scriptCues = scriptCues >> 1;
//				cue_List = cue_List >> 1;
//
//				layout.addView(getView(i, mPageNumber, channel, CUE_COLOR));
//			}
//		}
		return v;
	}

	public View getView(int position, int scriptNo, int channel, int cueColor) {
		// TODO Auto-generated method stub
		View v = View.inflate(getContext(), R.layout.list_module_cues,
				null);
		TextView txt_CueNo = (TextView) v.findViewById(R.id.txt_cue);
		TextView txt_EventTime = (TextView) v.findViewById(R.id.txt_eventtime);
		TextView txt_Desc = (TextView) v.findViewById(R.id.txt_desc);

		txt_CueNo.setBackgroundResource(CUE_DEFAULT);
		txt_CueNo.setText("" + (position + 1));

		Boolean IsCueActive = false;
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
		if (!IsCueActive) {
			if (CUE_COLOR == CUE_GREEN) {
				txt_CueNo.setBackgroundResource(CUE_RED_GREEN);
			}
		}
		return v;
	}
}

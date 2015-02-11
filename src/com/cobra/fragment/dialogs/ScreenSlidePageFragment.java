package com.cobra.fragment.dialogs;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cobra.R;
import com.cobra.appClass;
import com.cobra.api.CobraDataTags.EventDataTag;
import com.cobra.classes.Cues.ChannelCuesData;
import com.cobra.classes.Cues.CuesData;
import com.cobra.classes.Cues.ScriptData;
import com.cobra.views.modulelist.ModuleList;
import com.cobra.views.modulelist.ModuleListItem;

public class ScreenSlidePageFragment extends Fragment {

	public static final String ARG_PAGE = "page";
	public static final String ARG_MODID = "modID";

	public static final int CUE_DEFAULT = R.drawable.modulerow_cue_default;
	public static final int CUE_RED = R.drawable.modulerow_cue_red;
	public static final int CUE_GREEN = R.drawable.modulerow_cue_green;
	public static final int CUE_RED_GREEN = R.drawable.modulerow_cue_yellow;

	private int CUE_COLOR = 0;

	private int mPageNumber;
	private int modID;
	private LinearLayout linear_List;
	private LinearLayout layout_Column;

	private appClass globV;

	private ModuleListItem module;
	int widthOfScreen, heightOfScreen;
	Boolean UserScrollView = false;
	private ArrayList<ChannelCuesData> channelCuesDataList;

	public static ScreenSlidePageFragment create(int pageNumber, int modID,
			int widthOfScreen, int heightOfScreen, Boolean UserScrollView) {
		ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_PAGE, pageNumber);
		args.putInt(ARG_MODID, modID);
		args.putInt("widthOfScreen", widthOfScreen);
		args.putInt("heightOfScreen", heightOfScreen);
		args.putBoolean("UserScrollView", UserScrollView);
		fragment.setArguments(args);
		return fragment;
	}

	public ScreenSlidePageFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPageNumber = getArguments().getInt(ARG_PAGE);
		int pageNo = getArguments().getInt(ARG_PAGE);
		modID = getArguments().getInt(ARG_MODID);
		widthOfScreen = getArguments().getInt("widthOfScreen");
		heightOfScreen = getArguments().getInt("heightOfScreen");
		UserScrollView = getArguments().getBoolean("UserScrollView");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout containing a title and body text.
		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.fragment_screen_slide_page, container, false);
		linear_List = (LinearLayout) rootView.findViewById(R.id.linear_list);
		layout_Column = (LinearLayout) rootView
				.findViewById(R.id.layout_columns);
		// LinearLayout.LayoutParams parms=new
		// LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,getPercentage(heightOfScreen,
		// 3.68f));
		// layout_Column.setLayoutParams(parms);
		globV = (appClass) getActivity().getApplicationContext();
		channelCuesDataList = globV.getChannelCuesData();

		if (mPageNumber == 1) {
			int i = 0;
			i++;
		}

		if (ModuleList.hashMap != null && modID >= 0) {

			module = ModuleList.hashMap.get(modID);

			int channel = module.getModuleTag().currentChannel;
			ArrayList<String> cue_List = module.getLightStatus();
			long cueCont = module.getModuleTag().testResults;
			long scriptCues = module.getChannelTag().scriptCues;

			for (int i = 0; i < 18; i++) {

				boolean isThisCueAvailable = false;
				if (cue_List != null) {
					for (int j = 0; j < cue_List.size(); j++) {
						if (cue_List.get(j) != null
								&& cue_List.get(j).equals(i + "")) {
							isThisCueAvailable = true;
						}
					}
				}
				if (i != 0 && (i % 6) == 0) {
					// cue_List = cue_List >> 2;
					cueCont = cueCont >> 2;
					scriptCues = scriptCues >> 2;
				}

				CUE_COLOR = CUE_DEFAULT;

				if (isThisCueAvailable) {
					CUE_COLOR = CUE_RED;
				}
				if ((cueCont & 0x1) == 0x1) {
					CUE_COLOR = CUE_GREEN;
				}

				cueCont = cueCont >> 1;
				scriptCues = scriptCues >> 1;
				// cue_List = cue_List >> 1;

				linear_List
						.addView(getView(i, mPageNumber, channel, CUE_COLOR));
			}
		} else {
			getActivity().finish();
		}
		return rootView;
	}

	public View getView(int position, int scriptNo, int channel, int cueColor) {
		// TODO Auto-generated method stub
		View v = View.inflate(getActivity(), R.layout.list_module_cues, null);
		if (!UserScrollView) {
			LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, getPercentage(heightOfScreen,
							3.94f));
			v.setLayoutParams(parms);
		}
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

	private int getPercentage(int height, float percent) {
		float h = (percent * height);
		float a = h / 100;
		return (int) a;
	}

	public int getPageNumber() {
		return mPageNumber;
	}

}

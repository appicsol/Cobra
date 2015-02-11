package com.cobra.dialogs;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cobra.R;
import com.cobra.appClass;
import com.cobra.interfaces.onFilterCueModule;
import com.cobra.views.modulelist.ModuleList;
import com.cobra.views.modulelist.ModuleListItem;

public class DialogChannelGrid extends DialogFragment {

	public static DialogChannelGrid newInstance(ArrayList<String> list_Channel) {
		DialogChannelGrid f = new DialogChannelGrid();
		ListChannel = list_Channel;
		return f;
	}

	public static ArrayList<String> ListChannel;
	private GridLayout layout_Grid;
	private int widthOfScreen;
	private int heightOfScreen;
	public static final int STATUS_CLEAR_ALL = 1;
	public static final int STATUS_ACTIVE_CHANNEL = 2;
	public static final int STATUS_INACTIVE_CHANNEL = 3;
	private TextView[] button = new TextView[100];
	private ScrollView layout_Top;
	private Button btn_ShowAll;
	private appClass globV;
	private onFilterCueModule filterListener;
	
	private Button btn_Close;
	public DialogChannelGrid() {

	}  
 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = View
				.inflate(getActivity(), R.layout.dialog_channel_grid, null);
		layout_Grid = (GridLayout) v.findViewById(R.id.layout_grid);
		layout_Top = (ScrollView) v.findViewById(R.id.layout_top);
		btn_ShowAll = (Button) v.findViewById(R.id.btn_showall);
		btn_ShowAll.setOnClickListener(btnShowALLListener);
		btn_Close=(Button)v.findViewById(R.id.btn_close);
		btn_Close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DialogChannelGrid.this.dismiss();
			}
		});
		
		globV = (appClass) getActivity().getApplicationContext();

		filterListener = globV.getChannelFilter();
		 
		for (int i = 0; i < 100; i++) {
			View view=View.inflate(getActivity(), R.layout.button_channel_grid, null);
//			TextView tv=(TextView)v.findViewById(R.id.tv_channel);
			button[i] = (TextView)view.findViewById(R.id.tv_channel);
			button[i].setId(i);
			if (i < 10) 
 				button[i].setText("0" + (i));
			else
				button[i].setText("" + (i));
			button[i].setOnClickListener(btnListener);
			button[i].setTextColor(Color.parseColor("#d4d4d4"));
			button[i].setBackgroundResource(R.drawable.channel_grid_bg);
			int row = -1;
			int column = -1;

			row = i / 10;
			column = i % 10;
			GridLayout.LayoutParams parms = new GridLayout.LayoutParams(
					GridLayout.spec(row, GridLayout.CENTER), GridLayout.spec(
							column, GridLayout.CENTER));
			
			layout_Grid.addView(button[i], parms);
		}

		UpDateGrid(0, ModuleList.List_Channel);
//		HashMap<Integer, ModuleListItem> moduleListItem = ModuleList.hashMap;
//		if (moduleListItem != null) {
//			for (int i = 0; i < 100; i++) {
//				if (moduleListItem.containsKey(i)) {
//					ModuleListItem module = moduleListItem.get(i);
//					int channel = module.getModuleTag().currentChannel;
//					if (button[channel] != null) {
//						button[channel].setTextColor(Color.parseColor("#000000"));
//						button[channel].setTypeface(Typeface.DEFAULT_BOLD);
//						button[channel].setBackgroundResource(R.drawable.channel_grid_bg_selected);
//					}
//				}
//			}
//		}
		 
		layout_Top.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		layout_Top.measure(0, 0);
		widthOfScreen = layout_Top.getMeasuredWidth() + 10;
		heightOfScreen = layout_Top.getMeasuredHeight() + 10;

		return v;
	}

	View.OnClickListener btnListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			filterListener.FilterChannel(v.getId());
			DialogChannelGrid.this.dismiss();
		}
	};

	View.OnClickListener btnShowALLListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			filterListener.FilterChannel(-1);
			DialogChannelGrid.this.dismiss();
		}
	};
	
	ChannelGridListener listener = new ChannelGridListener() {
		@Override
		public void onChannelUpdate(int status1, ArrayList<String> channelList) {
			UpDateGrid(status1, channelList);
		}
	};
	
	private void UpDateGrid(int status1, ArrayList<String> channelList){

		for (int i = 0; i < button.length; i++) {
			if (button[i] != null)
			button[i].setTextColor(Color.parseColor("#d4d4d4"));
			button[i].setTypeface(Typeface.DEFAULT);
			button[i].setBackgroundResource(R.drawable.channel_grid_bg);
		}

		
		if (channelList != null) {
			for (int i = 0; i < channelList.size(); i++) {
				if (!channelList.get(i).equals("All")) {
					int channel = Integer.parseInt(channelList.get(i));
					if (button[channel] != null) {
						button[channel].setTextColor(Color.parseColor("#000000"));
						button[channel].setTypeface(Typeface.DEFAULT_BOLD);
						button[channel].setBackgroundResource(R.drawable.channel_grid_bg_selected);
					}
				}
			}
		}
	
	}
	private int mNum;

	public ChannelGridListener getListener() {
		return listener;
	}

	public void setListener(ChannelGridListener listener) {
		this.listener = listener;
	}

	public static interface ChannelGridListener {
		public void onChannelUpdate(int status, ArrayList<String> channelList);
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
		super.onDismiss(dialog);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mNum = 2;
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
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		getDialog().getWindow().setLayout(widthOfScreen + 40,
				heightOfScreen + 40);
	}
}

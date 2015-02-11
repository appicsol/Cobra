package com.cobra.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.cobra.MainActivity;
import com.cobra.R;
import com.cobra.appClass;
import com.cobra.api.Cobra;
import com.cobra.api.Cobra.ICommandEventListener;

public class BucketFiringNew extends Fragment {

	appClass globV;
	EditText WritingCommandPromot;
	View rootView;

	Cobra cobra;
	private ScrollView scroller;
	private ToggleButton toogle;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = View.inflate(getActivity(), R.layout.bucket_firing_new, null);
		// TODO Auto-generated constructor stub
		TableLayout tableCue = (TableLayout) rootView
				.findViewById(R.id.layout_table_cues);

		for (int i = 0; i < 100; i++) {
			for (int j = 0; j < 18; j++) {
				tableCue.addView(getView("" + i, "" + (j + 1), "" + i));
			}
		}
		
		return rootView;
	}

	private TableRow getView(String channel, String cue, String group) {
		View view = View.inflate(getActivity(), R.layout.list_item_cuetable_cue,
				null);
		final TableRow tblRow=(TableRow)view.findViewById(R.id.tableRow1);
		
		TextView txt_channel = (TextView) view.findViewById(R.id.txt_channel);
		TextView txt_cues = (TextView) view.findViewById(R.id.txt_cues);
		TextView txt_group = (TextView) view.findViewById(R.id.txt_group);

		txt_channel.setText(channel);
		txt_cues.setText(cue);
		txt_group.setText(channel);

		tblRow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tblRow.setBackgroundColor(Color.parseColor("#89ee29"));
			}
		});
		
		return (TableRow) view;
	}
}

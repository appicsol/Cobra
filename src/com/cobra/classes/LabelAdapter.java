package com.cobra.classes;

import java.util.List;

import com.cobra.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class LabelAdapter extends ArrayAdapter<Label_Data>{

	Context context;
	public LabelAdapter(Context context, int textViewResourceId,
			List<Label_Data> objects) {
		super(context, textViewResourceId, objects);
		this.context=context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v=View.inflate(this.context,android.R.layout.simple_list_item_1, null);
		return super.getView(position, convertView, parent);
	}

}

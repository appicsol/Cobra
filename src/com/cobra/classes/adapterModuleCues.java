package com.cobra.classes;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.cobra.R;
import com.cobra.views.modulelist.ModuleListItem;

public class adapterModuleCues extends ArrayAdapter<Integer> {

	ModuleListItem item;

	public adapterModuleCues(Context context, int resource,
			List<Integer> objects, ModuleListItem item) {
		super(context, resource, objects);
		this.item = item;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v=View.inflate(getContext(), R.layout.list_module_cues, null);
		return v;
	}

}

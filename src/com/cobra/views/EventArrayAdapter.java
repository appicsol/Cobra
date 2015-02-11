package com.cobra.views;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

public class EventArrayAdapter<T> extends ArrayAdapter<T> {

	public EventArrayAdapter(Context context, int textViewResourceId,
			List<T> objects) {
		super(context, textViewResourceId, objects);
	}

}

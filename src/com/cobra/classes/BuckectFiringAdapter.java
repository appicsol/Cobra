package com.cobra.classes;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnDragListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cobra.R;

public class BuckectFiringAdapter extends ArrayAdapter<Buckets> {

	private List<Buckets> data;
	private Context context;

	public BuckectFiringAdapter(Context context, int resource,
			int textViewResourceId, List<Buckets> objects) {
		super(context, resource, textViewResourceId, objects);
		this.context = context;
		data = objects;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View v = convertView;

		if (v == null) {
			LayoutInflater vi;
			vi = LayoutInflater.from(getContext());
			v = vi.inflate(R.layout.list_item, null);
		}

		TextView textview = (TextView) v.findViewById(R.id.textView1);
		textview.setTag("" + position);
		textview.setOnDragListener(dragListener);

		textview.setText(data.get(position).getBucketName());
		if (position % 3 == 0) {
			textview.setBackgroundColor(Color.parseColor("#6eabe5"));
		} else if (position % 3 == 1) {
			textview.setBackgroundColor(Color.parseColor("#80e3a1"));
		} else if (position % 3 == 2) {
			textview.setBackgroundColor(Color.parseColor("#f2af2d"));
		}
		return v;
	}

	View.OnDragListener dragListener = new OnDragListener() {

		@Override
		public boolean onDrag(View v, DragEvent event) {
			// TODO Auto-generated method stub
			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				// no action necessary
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				// no action necessary
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				// no action necessary

				break;
			case DragEvent.ACTION_DROP:
				break;
			case DragEvent.ACTION_DRAG_ENDED:

				View view = (View) event.getLocalState();
				view.setVisibility(View.GONE);

				TextView textView = (TextView) v;
				String tag = v.getTag().toString();
				 textView.setText("done");
				break;
			default:
				break;
			}
			return true;
		}
	};
}

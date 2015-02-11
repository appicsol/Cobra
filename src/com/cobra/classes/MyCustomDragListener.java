package com.cobra.classes;

import android.app.Activity;
import android.content.Context;
import android.view.DragEvent;
import android.view.View;

import com.cobra.MainActivity;
import com.cobra.TempClass;
import com.cobra.interfaces.CustomListViewListener;
import com.cobra.views.BucketFiring;

public class MyCustomDragListener extends BucketFiring {

	View ParentLayout = null;
	Activity activity = null;
	Context context = null;

	// public MyCustomDragListener(Context context, TempClass activity,
	public MyCustomDragListener(Context context, Activity activity,
			CustomListViewListener listviewlistener) {
		// TODO Auto-generated constructor stub
		super(context);
		this.activity = activity;
		this.context = context;
		this.ParentLayout = layout;
		this.listviewlistener = listviewlistener;
		this.ParentLayout.setOnDragListener(dragListener);

		// for (int i = 0; i < moduleList_Data.size(); i++) {
		// // CreateModules(i, gridLayout, true);
		// new Module(activity, context, i, gridLayout, true);
		// }
		// ArrangeBuckets();
	}

	CustomListViewListener listviewlistener;

	float x;
	float y;
	Boolean IsModuleInsideBucket = false;
	View.OnDragListener dragListener = new OnDragListener() {
		@Override
		public boolean onDrag(View v, DragEvent event) {
			// TODO Auto-generated method stub
			if (event.getX() == 0.0 && event.getY() == 0.0) {
			} else {
				x = event.getX();
				y = event.getY() + actionbarHeight;
			}
			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				// no action necessary
				final float xx = x;
				final float yy = y;
				if (isModuleInsideList(xx, yy, scrollerView)) {
					IsModuleInsideBucket = true;
				} else {
					IsModuleInsideBucket = false;
				}
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
				Boolean IsViewIsList = false;

				if (isPointInsideView(x, y, Bucket_AddMore_UI)) {
					if (listviewlistener != null) {
						IsViewIsList = true;
						listviewlistener.NewBucket(DraggedModule);
					}
				} else {
					for (int i = 0; i < bucketList_UI.size(); i++) {
						if (isPointInsideView(x, y, bucketList_UI.get(i))) {

							IsViewIsList = true;
							if (listviewlistener != null) {
								listviewlistener.ModuleDraggedInToTheList(
										DraggedModule, i);
							}
							break;
						}
					}
				}
				if (!IsViewIsList) {
					listviewlistener.ModuleDraggedOutFromList(DraggedModule);
				}

				break;
			default:
				break;
			}
			return true;
		}
	};

	private boolean isModuleInsideList(float x, float y, View view) {
		int location[] = new int[2];
		view.getLocationOnScreen(location);
		int viewX = location[0];
		int viewY = location[1];

		if (x >= viewX && y >= viewY) {
			return true;
		} else
			return false;
	}

	private boolean isPointInsideView(float x, float y, View view) {
		int location[] = new int[2];
		view.getLocationOnScreen(location);
		int viewX = location[0];
		int viewY = location[1];

		float x_width = view.getWidth() + viewX;
		float y_height = view.getHeight() + viewY;

		if (x >= viewX && y >= viewY) {
			if (x < x_width && y < y_height) {
				return true;
			} else
				return false;
		} else
			return false;
	}

}

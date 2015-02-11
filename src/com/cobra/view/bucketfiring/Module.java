package com.cobra.view.bucketfiring;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cobra.R;
import com.cobra.classes.Constants;
import com.cobra.classes.GenerateBackground;
import com.cobra.classes.ModuleCues;
import com.cobra.interfaces.OnModuleCuesDialogListener;
import com.cobra.views.BucketFiring;
import com.cobra.views.ModulesContainer;

/**
 * Child class of {@link BucketFiring} this class allow us to create new modules
 * linear.OnTouchListener allow us to drag and drop module
 */
public class Module extends BucketFiring {

	public Module(Context context) {
		super(context);
	}

	public Module(Activity activity, final Context context, final int position,
			View v, Boolean Is_Grid) {
		super(context);
		View view = View.inflate(activity, R.layout.module, null);
		final FrameLayout linear = (FrameLayout) view
				.findViewById(R.id.layout_linear);

		TextView txt_Address = (TextView) view.findViewById(R.id.txt_address);
		txt_Address.setText("Adr. : "
				+ moduleList_Data.get(position).getAddress());

		TextView txt_Channel = (TextView) view.findViewById(R.id.txt_channel);
		txt_Channel.setText("Ch. : "
				+ moduleList_Data.get(position).getChannel());

		TextView txt_Temp = (TextView) view.findViewById(R.id.txt_temp);
		txt_Temp.setVisibility(View.GONE);

		if (moduleList_Data.get(position).getActive()) {
			linear.setBackground(GenerateBackground.Background(activity,
					Constants.WHITE, false, 0.0f));
		} else
			linear.setBackground(GenerateBackground.Background(activity,
					Constants.GERY, false, 0.0f));
		int modID = -1;
		try {
			modID = Integer
					.parseInt(moduleList_Data.get(position).getAddress());
		} catch (Exception e) {

		}
		ModuleViews moduleView=null;
		if(modID!=-1 && modules_HashMap!=null && modules_HashMap.containsKey(modID)){
			moduleView=modules_HashMap.get(modID);
		}else
			moduleView=new ModuleViews();
		
		if (Is_Grid) {
			moduleView.setModuleAreaChannel(txt_Channel);
			linear.setId(position);
			((ModulesContainer) v).addView(linear);
			
		} else {
			moduleView.setBucketAreaChannel(txt_Channel);
			linear.setId(position + moduleList_Data.size());
			((ModulesContainer) v).addView(linear);
		}

		if(moduleView!=null)
			modules_HashMap.put(modID, moduleView);
		
		linear.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent e) {
				// TODO Auto-generated method stub
				ClipData data = ClipData.newPlainText("", "");
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
						linear);

				if (linear.getId() >= moduleList_Data.size()) {
					DraggedModule = linear.getId() - moduleList_Data.size();
					v.setVisibility(View.GONE);
				} else {
					DraggedModule = linear.getId();
					v.setVisibility(View.GONE);
				}
				linear.startDrag(data, shadowBuilder, linear, 0);
				return false;
			}
		});

		modules_UI.add(view);
		moduleList_Data.get(position).getModuleCues()
				.InitializeCuesUI(context, view);

	}

}

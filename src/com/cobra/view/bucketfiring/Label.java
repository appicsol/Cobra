package com.cobra.view.bucketfiring;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cobra.R;
import com.cobra.classes.Buckets;
import com.cobra.classes.GenerateBackground;
import com.cobra.db.DBHelper;
import com.cobra.views.BucketFiring;
import com.cobra.views.ModulesContainer;

public class Label extends BucketFiring{

	public Label(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public Label(Activity activity,final Context context,ModulesContainer LabelContainer,final int position, final String labelName) {
		super(context);
		final View v = View.inflate(context, R.layout.label, null);
		v.setId(position);
		int[] InSideColor = new int[] { 255, 255, 0 };
		int RadiusRightSide = 25;
		v.setBackground(GenerateBackground.Background(activity, InSideColor,
				RadiusRightSide));
		final TextView txt_LabelName = (TextView) v
				.findViewById(R.id.txt_labelname);
		TextView txt_LabelButton = (TextView) v
				.findViewById(R.id.txt_labelbutton);
		txt_LabelName.setText(labelName);
		v.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				v.setVisibility(View.GONE);
				int ii=position;
				Buckets bucket = bucketList_Data.get(position);
				for (int i = 0; i < bucket.getBucketLabels().size(); i++) {
					if (bucket.getBucketLabels().get(i).getLabelName().equals(labelName)) {
						bucketList_Data.get(position).getBucketLabels().remove(i);
						long result=DBHelper.DeleteBucketLabel(context, position, labelName);
//						Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show();
						break;
					}
				}
			}
		});
		LabelContainer.addView(v);
	}

}

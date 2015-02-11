package com.cobra;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.cobra.db.DBAdapter;
import com.cobra.views.BucketFiring;

public class TempClass extends Activity {

	private RelativeLayout contentFrame;
	BucketFiring bucketFiring;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_temp);

		contentFrame = (RelativeLayout) findViewById(R.id.content_frame);

		DBAdapter db = new DBAdapter(this.getApplicationContext());

		try {
			db.createDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
		bucketFiring = new BucketFiring(this, this,null);
		this.contentFrame.addView(bucketFiring);
	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed();

//		if (!bucketFiring.IsDataUpdated()) {
//			bucketFiring.SaveDialog();
//		} else
//			finish();
		// Otherwise defer to system default behavior.
	}
}

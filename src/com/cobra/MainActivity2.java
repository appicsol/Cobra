package com.cobra;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity2 extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);
		
		Intent intent=new Intent(MainActivity2.this, MainActivity.class);
		startActivity(intent);
	}
}

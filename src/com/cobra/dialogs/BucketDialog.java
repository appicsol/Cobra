package com.cobra.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.cobra.R;
import com.cobra.classes.Buckets;

public class BucketDialog extends Dialog {

	private Buckets bucket;
	private Button btn_Update;
	private Button btn_Close;
	private EditText bucketName;
	private String name;

	public BucketDialog(Context context,Buckets bucket) {
		super(context);
		this.bucket=bucket;

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_bucket);
		
		 bucketName=(EditText)findViewById(R.id.edi_bucket_name);
		bucketName.setText(bucket.getBucketName());
		
		btn_Update=(Button)findViewById(R.id.btn_update);
		btn_Close=(Button)findViewById(R.id.btn_close);
	
	}
	
	public BucketDialog(Context context,String name) {
		super(context);
		this.name=name;

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_bucket);
		
		 bucketName=(EditText)findViewById(R.id.edi_bucket_name);
		bucketName.setText(name);
		
		btn_Update=(Button)findViewById(R.id.btn_update);
		btn_Close=(Button)findViewById(R.id.btn_close);
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}

	public Button getCloseButton(){
		return btn_Close;
	}
	
	public Button getUpdateButton(){
		return btn_Update;
	}
	
	public String getName(){
		if(!bucketName.getText().toString().equals(""))
			return bucketName.getText().toString();
		return null;
	}
}

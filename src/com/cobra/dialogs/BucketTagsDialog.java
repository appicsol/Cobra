package com.cobra.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.TagLostException;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.cobra.R;
import com.cobra.appClass;
import com.cobra.classes.Buckets;
import com.cobra.db.DBHelper;

public class BucketTagsDialog extends Dialog {

	private Button btn_Add;
	private Button btn_Close;
	private EditText bucketName;
	private String name;
	private ListView tagsList;
	private Context context;
	public BucketTagsDialog(Context context) {
		super(context);
		this.context=context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_bucket_tags);
		
		 bucketName=(EditText)findViewById(R.id.edit_bucket_name);
		
		btn_Add=(Button)findViewById(R.id.btn_addtag);
		btn_Close=(Button)findViewById(R.id.btn_close);
		tagsList=(ListView)findViewById(R.id.list_tags);

//		array=appClass.getTagsArrayFromSharedPreferences(context);
		array=DBHelper.getLabels(context); 
		adapter=new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, array);
		tagsList.setAdapter(adapter);
	}
	ArrayAdapter<String> adapter;
	String[] array;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}

	public Button getCloseButton(){
		return btn_Close;
	}
	
	public Button getAddButton(){
		return btn_Add;
	}
	
	public String getName(){
		if(bucketName!=null && !bucketName.getText().toString().equals(""))
			return bucketName.getText().toString();
		return null;
	}
	
	public ListView getTagsList(){
		return tagsList;
	}
	
	public void UpdateTagArray(){
		array=DBHelper.getLabels(context);//appClass.getTagsArrayFromSharedPreferences(context);
		if(adapter!=null){
			adapter.notifyDataSetChanged();
		}
	}
	
}

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

public class BucketTimeDialog extends Dialog {

	private Button btn_Add;
	private Button btn_Close;
	private EditText edit_Time;
	private String name;
	private ListView timeList;
	private Context context;
	public BucketTimeDialog(Context context) {
		super(context);
		this.context=context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_bucket_time);
		
		edit_Time=(EditText)findViewById(R.id.edit_time);
		
		btn_Add=(Button)findViewById(R.id.btn_addtime);
		btn_Close=(Button)findViewById(R.id.btn_close);
		timeList=(ListView)findViewById(R.id.list_time);

//		array=appClass.getTagsArrayFromSharedPreferences(context);
		array=DBHelper.getTime(context); 
		adapter=new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, array);
		timeList.setAdapter(adapter);
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
	
	public String getNewTime(){
		if(edit_Time!=null && !edit_Time.getText().toString().equals(""))
			return edit_Time.getText().toString();
		return null;
	}
	
	public ListView getTimeList(){
		return timeList;
	}
	
	public void UpdateTimeArray(){
		array=DBHelper.getLabels(context);
		if(adapter!=null){
			adapter.notifyDataSetChanged();
		}
	}
	
}

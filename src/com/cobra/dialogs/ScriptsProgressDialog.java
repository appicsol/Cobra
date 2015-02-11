package com.cobra.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.SparseArray;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cobra.R;
import com.cobra.api.Cobra;
import com.cobra.api.CobraDataTags.EventDataTag;
import com.cobra.api.CobraDataTags.ScriptIndexTag;
import com.cobra.interfaces.ScriptListener;

public class ScriptsProgressDialog extends Dialog {

	private Button btn_Close;
	private String name;
	// private int totalScripts;
	// private int totalEvents;
	// private int currentScript;
	// private int currentEvent;
	private String Message;
	private TextView txt_Message;
	private Cobra cobra;
	Boolean Stop = false;

	public ScriptsProgressDialog(Context context, Cobra cobra) {
		super(context);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_scripts);
		btn_Close = (Button) findViewById(R.id.btn_ok);
		txt_Message = (TextView) findViewById(R.id.text_message2);
		this.cobra = cobra;
		Message = "0/0 Scripts and 0/0 events are loaded.";
		txt_Message.setText(Message);
		// this.cobra.addScriptListener(this.listener);
		// totalScripts = cobra.getScriptsData().size();

		// for (int i = 0; i < totalScripts; i++) {
		// ScriptIndexTag script = cobra.getScriptsData().get(i);
		// totalEvents = script.numEvents;
		// SparseArray<EventDataTag> events = cobra.getEventsData()
		// .get(i);
		// UpdateMessage(totalScripts,(i+1),totalEvents,events.size());
		// }
		new MessageUpdate().execute();
	}

	public String UpdateMessage(int totalScripts, int currentScript,
			int totalEvents, int currentEvent) {
		Message = currentScript + "/" + totalScripts + " Scripts and "
				+ currentEvent + "/" + totalEvents + " events are loaded.";
		return Message;
	}

	public Button getCloseButton() {
		return btn_Close;
	}

	// ScriptListener listener = new ScriptListener() {
	//
	// @Override
	// public void OnScriptLoad(int script_index, int eventNumber) {
	// try {
	// ScriptIndexTag script = cobra.getScriptsData()
	// .get(script_index);
	// String msg = UpdateMessage(totalScripts, script_index,
	// script.numEvents, eventNumber);
	// txt_Message = (TextView) findViewById(R.id.text_message2);
	// txt_Message.setText(msg);
	// Toast.makeText(getContext(), "asd", Toast.LENGTH_SHORT).show();
	// cobra.addScriptListener(listener);
	// } catch (Exception e) {
	// // TODO: handle exception
	// int i = 0;
	// i++;
	// }
	// }
	// };

	int LastStoredEvents = 0;
	int TriesOfLastStoredEvents = 0;

	private class MessageUpdate extends AsyncTask<Void, Void, Void> {

		int totalScripts;
		int totalEvents;
		int CurrentScript = 0;
		int CurrentEvent = 0;

		MessageUpdate() {
			totalScripts = cobra.getScriptsData().size();
			totalEvents = cobra.getScriptsData().get(CurrentEvent).numEvents;
			CurrentEvent = cobra.getEventsData().get(CurrentScript).size();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub

			if (totalEvents == CurrentEvent)
				Stop = true;
			
			if (LastStoredEvents == CurrentEvent) {
				TriesOfLastStoredEvents++;
				if (TriesOfLastStoredEvents == 3)
					Stop = true;
			} else {
				String msg = UpdateMessage(totalScripts, (CurrentScript + 1),
						totalEvents, CurrentEvent);
				LastStoredEvents = CurrentEvent;
				txt_Message.setText(msg);
			}
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (!Stop)
				new MessageUpdate().execute();
			else
				dismiss();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}
}

package com.cobra.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.cobra.R;
import com.cobra.classes.Buckets;

public class ClearBucketDialog extends Dialog {

	private Button btn_Clear;
	private Button btn_Close;

	public ClearBucketDialog(Context context) {
		super(context);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_clear_buckets);

		btn_Clear = (Button) findViewById(R.id.btn_clear);
		btn_Close = (Button) findViewById(R.id.btn_close);

	}

	public Button getCloseButton() {
		return btn_Close;
	}

	public Button getClearButton() {
		return btn_Clear;
	}

}

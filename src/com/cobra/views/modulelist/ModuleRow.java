package com.cobra.views.modulelist;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.provider.Settings.Global;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cobra.R;
import com.cobra.appClass;
import com.cobra.api.Cobra.ModuleType;
import com.cobra.api.CobraDataTags.ChannelDataTag;
import com.cobra.api.CobraDataTags.ModuleDataTag;
import com.cobra.classes.ChannelCues;
import com.cobra.classes.Constants;

public class ModuleRow extends FrameLayout {
	private final String TAG;
	private TextView tvDevice, tvAddress, tvChannel, tvKeyPosition, tvMode,
			tvSignal, tvBattery1, tvBattery2;
	private LinearLayout cuesParent;
	private List<TextView> tvCues;

	private ModuleDataTag dataTag;
	private LinearLayout content;
	private ChannelDataTag channelDataTag;
	private LinearLayout parentLayout;
	public static appClass globV;
	private LinearLayout row_layout;
	private static boolean HaveExtraCues;

	public ModuleRow(int position, Context context, ModuleDataTag tag,
			int currentChannel, ChannelDataTag channelDataTag,
			ArrayList<String> lightStatus) {
		super(context);

		
		globV = (appClass) context.getApplicationContext();
		TAG = ModuleRow.class.getName();
		content = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.modulerow, null);
		row_layout = (LinearLayout) content.findViewById(R.id.layout_row_data);
		this.addView(content);

		tvDevice = (TextView) content.findViewById(R.id.tvDevice);
		tvAddress = (TextView) content.findViewById(R.id.tvAddress);
		tvChannel = (TextView) content.findViewById(R.id.tvRemoteChannel);
		tvKeyPosition = (TextView) content.findViewById(R.id.tvKeyPosition);
		tvMode = (TextView) content.findViewById(R.id.tvMode);
		tvSignal = (TextView) content.findViewById(R.id.tvSignal);
		tvBattery1 = (TextView) content.findViewById(R.id.tvBattery1);
		tvBattery2 = (TextView) content.findViewById(R.id.tvBattery2);

		tvCues = new ArrayList<TextView>();
		cuesParent = (LinearLayout) content.findViewById(R.id.cuesParentLayout);
		ArrayList<View> children = getAllChildren(cuesParent);

		for (View v : children) {
			if (v instanceof TextView) {
				TextView tv = (TextView) v;
				tvCues.add(tv);
			}
		}
		updateViewData(position, tag, currentChannel, channelDataTag,
				lightStatus);
	}

	private ArrayList<View> getAllChildren(View v) {

		if (!(v instanceof ViewGroup)) {
			ArrayList<View> viewArrayList = new ArrayList<View>();
			viewArrayList.add(v);
			return viewArrayList;
		}

		ArrayList<View> result = new ArrayList<View>();

		ViewGroup vg = (ViewGroup) v;
		for (int i = 0; i < vg.getChildCount(); i++) {

			View child = vg.getChildAt(i);

			ArrayList<View> viewArrayList = new ArrayList<View>();
			viewArrayList.add(v);
			viewArrayList.addAll(getAllChildren(child));

			result.addAll(viewArrayList);
		}
		return result;
	}

	private void updateViewData(int position, final ModuleDataTag tag,
			final int currentChannel, final ChannelDataTag channelDataTag,
			final ArrayList<String> lightStatus) {



		this.dataTag = tag;
		this.channelDataTag = channelDataTag;
		// new Handler().postDelayed(new Runnable() {
		// @Override
		// public void run() {
		tvAddress.setText(String.format("A%02d", tag.modID));
		tvChannel.setText(String.format("%02d", tag.currentChannel));

		// These are hidden for some module types and must be unhidden
		cuesParent.setVisibility(VISIBLE);
		tvMode.setVisibility(VISIBLE);
		tvSignal.setVisibility(VISIBLE);
		tvBattery1.setVisibility(VISIBLE);
		tvBattery2.setVisibility(VISIBLE);
		tvKeyPosition.setVisibility(VISIBLE);

		if (tag.keyPos) {
			tvKeyPosition.setText("TEST");
			tvKeyPosition.setBackground(ModuleRow.this.getContext()
					.getResources()
					.getDrawable(R.drawable.modulerow_signal_back_green));
			tvKeyPosition.setTextColor(Color.WHITE);
		} else {
			tvKeyPosition.setText("ARM");
			tvKeyPosition.setBackground(ModuleRow.this.getContext()
					.getResources()
					.getDrawable(R.drawable.modulerow_signal_back_red));
			tvKeyPosition.setTextColor(Color.WHITE);
		}

		if (!tag.armed) {
			tvMode.setText("TEST");
			tvMode.setBackground(ModuleRow.this.getContext().getResources()
					.getDrawable(R.drawable.modulerow_signal_back_green));
			tvMode.setTextColor(Color.WHITE);
		} else {
			tvMode.setText("ARM");
			tvMode.setBackground(ModuleRow.this.getContext().getResources()
					.getDrawable(R.drawable.modulerow_signal_back_red));
			tvMode.setTextColor(Color.WHITE);
		}

		tvSignal.setText("-" + tag.linkQuality);
		if (tag.linkQuality >= 70) {
			tvSignal.setBackground(ModuleRow.this.getContext().getResources()
					.getDrawable(R.drawable.modulerow_signal_back_red));
			tvSignal.setTextColor(Color.WHITE);
		} else if (tag.linkQuality > 60) {
			tvSignal.setBackground(ModuleRow.this.getContext().getResources()
					.getDrawable(R.drawable.modulerow_signal_back_orange));
			tvSignal.setTextColor(Color.BLACK);
		} else {
			tvSignal.setBackground(ModuleRow.this.getContext().getResources()
					.getDrawable(R.drawable.modulerow_signal_back_green));
			tvSignal.setTextColor(Color.WHITE);
		}

		tvBattery1.setText("" + tag.batteryLevel1);
		if (tag.batteryLevel1 >= 0 && tag.batteryLevel1 <= 1) {
			tvBattery1.setBackground(ModuleRow.this.getContext().getResources()
					.getDrawable(R.drawable.modulerow_signal_back_red));
			tvBattery1.setTextColor(Color.WHITE);
		} else if (tag.batteryLevel1 >= 2 && tag.batteryLevel1 <= 4) {
			tvBattery1.setBackground(ModuleRow.this.getContext().getResources()
					.getDrawable(R.drawable.modulerow_signal_back_orange));
			tvBattery1.setTextColor(Color.BLACK);
		} else {
			// tvBattery1.setBackground(null);
			tvBattery1.setBackground(ModuleRow.this.getContext().getResources()
					.getDrawable(R.drawable.modulerow_signal_back_white));
			tvBattery1.setTextColor(Color.BLACK);
		}
		tvBattery2.setText("" + tag.batteryLevel2);
		if (tag.batteryLevel2 >= 0 && tag.batteryLevel2 <= 1) {
			tvBattery2.setBackground(ModuleRow.this.getContext().getResources()
					.getDrawable(R.drawable.modulerow_signal_back_red));
			tvBattery2.setTextColor(Color.WHITE);
		} else if (tag.batteryLevel2 >= 2 && tag.batteryLevel2 <= 4) {
			tvBattery2.setBackground(ModuleRow.this.getContext().getResources()
					.getDrawable(R.drawable.modulerow_signal_back_orange));
			tvBattery2.setTextColor(Color.BLACK);
		} else {
			// tvBattery2.setBackground(null);
			tvBattery2.setBackground(ModuleRow.this.getContext().getResources()
					.getDrawable(R.drawable.modulerow_signal_back_white));
			tvBattery2.setTextColor(Color.BLACK);
		}

		int cueCont = tag.testResults;
		long scriptCues = channelDataTag.scriptCues;
		ArrayList<String> cue_List = lightStatus;

		if (tag.modType == ModuleType.NINETY_M_SUB) {
			// tvMode.setText("");
			tvSignal.setText("");
			tvDevice.setText("");
			tvAddress.setText("");
			tvBattery1.setText("");
			tvBattery2.setText("");
			// tvKeyPosition.setText("");

			// tvMode.setVisibility(INVISIBLE);
			tvSignal.setVisibility(INVISIBLE);
			tvBattery1.setVisibility(INVISIBLE);
			tvBattery2.setVisibility(INVISIBLE);
			// tvKeyPosition.setVisibility(INVISIBLE);
		} else if (tag.modType == ModuleType.NINETY_M_MAIN) {
			tvDevice.setText("90M");
		} else if (tag.modType == ModuleType.AUDIOBOX) {
			tvDevice.setText("AB");
			tvBattery2.setText("N/A");
			tvBattery2.setBackground(null);
			tvKeyPosition.setText("ON");

			if (tag.armed) {
				tvKeyPosition.setBackground(ModuleRow.this.getContext()
						.getResources()
						.getDrawable(R.drawable.modulerow_signal_back_red));
				tvKeyPosition.setTextColor(Color.WHITE);
			} else {
				tvKeyPosition.setBackground(ModuleRow.this.getContext()
						.getResources()
						.getDrawable(R.drawable.modulerow_signal_back_green));
				tvKeyPosition.setTextColor(Color.WHITE);
			}

			cuesParent.setVisibility(INVISIBLE);
		} else {
			tvDevice.setText("18M");
		}

		HaveExtraCues = false;
		for (int i = 0; i < 18; i++) {
			TextView tv = tvCues.get(i);
			if (i == 10) {
				int a = 1;
				a++;
			}

			boolean script_cue_stauts = ChannelCues
					.getCue18MString(i, cue_List);

			boolean module_cue_stauts = ChannelCues.getCue18M(i, cueCont);

			tv.setBackground(ModuleRow.this.getContext().getResources()
					.getDrawable(R.drawable.modulerow_cue_default));
			tv.setTextColor(Color.BLACK);

			if (script_cue_stauts && module_cue_stauts) {
				tv.setBackground(ModuleRow.this.getContext().getResources()
						.getDrawable(R.drawable.modulerow_cue_green));
				tv.setTextColor(Color.WHITE);
			} else if (script_cue_stauts) {
				tv.setBackground(ModuleRow.this.getContext().getResources()
						.getDrawable(R.drawable.modulerow_cue_red));
				tv.setTextColor(Color.WHITE);
				HaveExtraCues = true;
			} else if (module_cue_stauts) {
				tv.setBackground(ModuleRow.this.getContext().getResources()
						.getDrawable(R.drawable.modulerow_cue_yellow));
				HaveExtraCues = true;
				tv.setTextColor(Color.BLACK);

			}
		}

		setHaveExtraCues(HaveExtraCues);
		ModuleRow.this.setActive(currentChannel, tag);
	}



	public static void updateViewData(final Context context,
			final int module_UI_ListIndex, final ModuleListItem item,
			final ModuleUIRow row, final ModuleDataTag tag,
			final int currentChannel, ChannelDataTag channelDataTag,
			final ArrayList<String> lightStatus) {


		row.setChannel(tag.currentChannel);
		row.setAddress(tag.modID);
		row.setDevice(tag.modType);
		row.setKey_Pos(tag.keyPos);
		row.setMode(tag.armed);
		row.setModuleid(tag.modID);
		row.setPower_1(tag.batteryLevel1);
		row.setPower_2(tag.batteryLevel2);
		row.setSignal(tag.linkQuality);

		row.setModuleListItem(item);

		row.getTvAddress().setText(String.format("A%02d", tag.modID));
		row.getTvChannel().setText(String.format("%02d", tag.currentChannel));

		// These are hidden for some module types and must be unhidden

		row.getCuesParent().setVisibility(VISIBLE);
		row.getTvMode().setVisibility(VISIBLE);
		row.getTvSignal().setVisibility(VISIBLE);
		row.getTvBattery1().setVisibility(VISIBLE);
		row.getTvBattery2().setVisibility(VISIBLE);
		row.getTvKeyPosition().setVisibility(VISIBLE);

		if (tag.keyPos) {
			row.getTvKeyPosition().setText("TEST");
			row.getTvKeyPosition().setBackground(
					context.getResources().getDrawable(
							R.drawable.modulerow_signal_back_green));
			row.getTvKeyPosition().setTextColor(Color.WHITE);
		} else {
			row.getTvKeyPosition().setText("ARM");
			row.getTvKeyPosition().setBackground(
					context.getResources().getDrawable(
							R.drawable.modulerow_signal_back_red));
			row.getTvKeyPosition().setTextColor(Color.WHITE);
		}

		if (!tag.armed) {
			row.getTvMode().setText("TEST");
			row.getTvMode().setBackground(
					context.getResources().getDrawable(
							R.drawable.modulerow_signal_back_green));
			row.getTvMode().setTextColor(Color.WHITE);
		} else {
			row.getTvMode().setText("ARM");
			row.getTvMode().setBackground(
					context.getResources().getDrawable(
							R.drawable.modulerow_signal_back_red));
			row.getTvMode().setTextColor(Color.WHITE);
		}

		row.getTvSignal().setText("-" + tag.linkQuality);
		if (tag.linkQuality >= 70) {
			row.getTvSignal().setBackground(
					context.getResources().getDrawable(
							R.drawable.modulerow_signal_back_red));
			row.getTvSignal().setTextColor(Color.WHITE);
		} else if (tag.linkQuality > 60) {
			row.getTvSignal().setBackground(
					context.getResources().getDrawable(
							R.drawable.modulerow_signal_back_orange));
			row.getTvSignal().setTextColor(Color.BLACK);
		} else {
			row.getTvSignal().setBackground(
					context.getResources().getDrawable(
							R.drawable.modulerow_signal_back_green));
			row.getTvSignal().setTextColor(Color.WHITE);
		}

		row.getTvBattery1().setText("" + tag.batteryLevel1);
		if (tag.batteryLevel1 >= 0 && tag.batteryLevel1 <= 1) {
			row.getTvBattery1().setBackground(
					context.getResources().getDrawable(
							R.drawable.modulerow_signal_back_red));
			row.getTvBattery1().setTextColor(Color.WHITE);
		} else if (tag.batteryLevel1 >= 2 && tag.batteryLevel1 <= 4) {
			row.getTvBattery1().setBackground(
					context.getResources().getDrawable(
							R.drawable.modulerow_signal_back_orange));
			row.getTvBattery1().setTextColor(Color.BLACK);
		} else {
			// row.getTvBattery1().setBackground(null);
			row.getTvBattery1().setBackground(
					context.getResources().getDrawable(
							R.drawable.modulerow_signal_back_white));
			row.getTvBattery1().setTextColor(Color.BLACK);

		}

		row.getTvBattery2().setText("" + tag.batteryLevel2);
		if (tag.batteryLevel2 >= 0 && tag.batteryLevel2 <= 1) {
			row.getTvBattery2().setBackground(
					context.getResources().getDrawable(
							R.drawable.modulerow_signal_back_red));
			row.getTvBattery2().setTextColor(Color.WHITE);
		} else if (tag.batteryLevel2 >= 2 && tag.batteryLevel2 <= 4) {
			row.getTvBattery2().setBackground(
					context.getResources().getDrawable(
							R.drawable.modulerow_signal_back_orange));
			row.getTvBattery2().setTextColor(Color.BLACK);
		} else {
			// row.getTvBattery2().setBackground(null);
			row.getTvBattery2().setBackground(
					context.getResources().getDrawable(
							R.drawable.modulerow_signal_back_white));
			row.getTvBattery2().setTextColor(Color.BLACK);
		}

		// continuity info on module 4 bytes
		int cueCont = tag.testResults;
		// continuity info on script side made by Android while downloading
		// scripts
		ArrayList<String> cue_List = lightStatus;

		if (tag.modType == ModuleType.NINETY_M_SUB) {
			// tvMode.setText("");
			row.getTvSignal().setText("");
			row.getTvDevice().setText("");
			row.getTvAddress().setText("");
			row.getTvBattery1().setText("");
			row.getTvBattery2().setText("");
			// tvKeyPosition.setText("");

			// tvMode.setVisibility(INVISIBLE);
			row.getTvSignal().setVisibility(INVISIBLE);
			row.getTvBattery1().setVisibility(INVISIBLE);
			row.getTvBattery2().setVisibility(INVISIBLE);
			// tvKeyPosition.setVisibility(INVISIBLE);
		} else if (tag.modType == ModuleType.NINETY_M_MAIN) {
			row.getTvDevice().setText("90M");
		} else if (tag.modType == ModuleType.AUDIOBOX) {
			row.getTvDevice().setText("AB");
			row.getTvBattery2().setText("N/A");
			row.getTvBattery2().setBackground(null);
			row.getTvKeyPosition().setText("ON");

			if (tag.armed) {
				row.getTvKeyPosition().setBackground(
						context.getResources().getDrawable(
								R.drawable.modulerow_signal_back_red));
				row.getTvKeyPosition().setTextColor(Color.WHITE);

			} else {
				row.getTvKeyPosition().setBackground(
						context.getResources().getDrawable(
								R.drawable.modulerow_signal_back_green));

				row.getTvKeyPosition().setTextColor(Color.WHITE);
			}

			row.getCuesParent().setVisibility(INVISIBLE);
		} else {
			row.getTvDevice().setText("18M");
		}

		HaveExtraCues = false;
		for (int i = 0; i < 18; i++) {
			try {
				TextView tv = row.getTvCues().get(i);

				Boolean script_cue_stauts = ChannelCues.getCue18MString(i,
						cue_List);

				Boolean module_cue_stauts = ChannelCues.getCue18M(i, cueCont);

				tv.setBackground(context.getResources().getDrawable(
						R.drawable.modulerow_cue_default));
				tv.setTextColor(Color.BLACK);

				if (script_cue_stauts && module_cue_stauts) {
					tv.setBackground(context.getResources().getDrawable(
							R.drawable.modulerow_cue_green));
					tv.setTextColor(Color.WHITE);

				} else if (script_cue_stauts) {
					tv.setBackground(context.getResources().getDrawable(
							R.drawable.modulerow_cue_red));
					tv.setTextColor(Color.WHITE);
					HaveExtraCues = true;
				} else if (module_cue_stauts) {
					tv.setBackground(context.getResources().getDrawable(
							R.drawable.modulerow_cue_yellow));
					tv.setTextColor(Color.BLACK);
					HaveExtraCues = true;
				}
			} catch (Exception e) {
				Toast.makeText(context, "exception: " + e.getMessage(),
						Toast.LENGTH_LONG).show();
			}
		}
		if (globV.getModuleUI_List() != null
				&& globV.getModuleUI_List().size() > module_UI_ListIndex) {
			globV.getModuleUI_List().get(module_UI_ListIndex)
					.setHaveExtraCues(HaveExtraCues);
		}
		//
		// for (int i = 0; i < 18; i++) {
		// TextView tv = row.getTvCues().get(i);
		//
		// if (i != 0 && (i % 6) == 0) {
		// cue_List = cue_List >> 2;
		// // scriptCues = scriptCues >> 2;
		// cueCont = cueCont >> 2;
		// }
		//
		// Boolean status=globV.getChannelCueStatus(currentChannel, i);
		// tv.setBackground(context.getResources().getDrawable(
		// R.drawable.modulerow_cue_default));
		//
		// if ((cue_List & 0x1) == 0x1)
		// tv.setBackground(context.getResources().getDrawable(
		// R.drawable.modulerow_cue_red));
		//
		// if ((cueCont & 0x1) == 0x1)
		// tv.setBackground(context.getResources().getDrawable(
		// R.drawable.modulerow_cue_green));
		//
		// lightStatus = lightStatus >> 1;
		// cue_List = cue_List >> 1;
		// cueCont = cueCont >> 1;
		// }

		setActive(row, context, currentChannel, tag);
		// }
		// }, 10);
	}

	public static void setActive(ModuleUIRow row, Context context,
			int currentChannel, ModuleDataTag tag) {

		if (tag.currentChannel == currentChannel) {
			row.getContent().setBackground(
					context.getResources().getDrawable(
							R.drawable.modulerow_row_active));
			// else if ((tag.keyPos && tag.armed) || tag.batteryLevel1 > 9
			// || tag.batteryLevel2 > 9) {
			// row.getContent().setBackground(
			// context.getResources().getDrawable(
			// R.drawable.modulerow_row_invalid));
			// }
		} else
			row.getContent().setBackground(
					context.getResources()
							.getDrawable(R.drawable.modulerow_row));

	}

	public static void setActive(ModuleUIRow row, Context context,
			boolean isActive) {

		if (isActive)
			row.getContent().setBackground(
					context.getResources().getDrawable(
							R.drawable.modulerow_row_active));

		else
			row.getContent().setBackground(
					context.getResources()
							.getDrawable(R.drawable.modulerow_row));

	}

	public void setActive(int currentChannel, ModuleDataTag tag) {

		if (tag.currentChannel == currentChannel)
			row_layout.setBackground(getResources().getDrawable(
					R.drawable.modulerow_row_active));
		// else if ((tag.keyPos && tag.armed) || tag.batteryLevel1 > 9
		// || tag.batteryLevel2 > 9) {
		// row_layout.setBackground(getResources().getDrawable(
		// R.drawable.modulerow_row_invalid));
		// }
		else
			row_layout.setBackground(getResources().getDrawable(
					R.drawable.modulerow_row));

	}

	public Boolean getHaveExtraCues() {
		return HaveExtraCues;
	}

	public void setHaveExtraCues(Boolean haveExtraCues) {
		HaveExtraCues = haveExtraCues;
	}

	public int getAddress() {
		return dataTag.modID;
	}

	public TextView getTvDevice() {
		return tvDevice;
	}

	public void setTvDevice(TextView tvDevice) {
		this.tvDevice = tvDevice;
	}

	public TextView getTvAddress() {
		return tvAddress;
	}

	public void setTvAddress(TextView tvAddress) {
		this.tvAddress = tvAddress;
	}

	public TextView getTvChannel() {
		return tvChannel;
	}

	public void setTvChannel(TextView tvChannel) {
		this.tvChannel = tvChannel;
	}

	public TextView getTvKeyPosition() {
		return tvKeyPosition;
	}

	public void setTvKeyPosition(TextView tvKeyPosition) {
		this.tvKeyPosition = tvKeyPosition;
	}

	public TextView getTvMode() {
		return tvMode;
	}

	public void setTvMode(TextView tvMode) {
		this.tvMode = tvMode;
	}

	public TextView getTvSignal() {
		return tvSignal;
	}

	public void setTvSignal(TextView tvSignal) {
		this.tvSignal = tvSignal;
	}

	public TextView getTvBattery1() {
		return tvBattery1;
	}

	public void setTvBattery1(TextView tvBattery1) {
		this.tvBattery1 = tvBattery1;
	}

	public TextView getTvBattery2() {
		return tvBattery2;
	}

	public void setTvBattery2(TextView tvBattery2) {
		this.tvBattery2 = tvBattery2;
	}

	public LinearLayout getCuesParent() {
		return cuesParent;
	}

	public void setCuesParent(LinearLayout cuesParent) {
		this.cuesParent = cuesParent;
	}

	public List<TextView> getTvCues() {
		return tvCues;
	}

	public void setTvCues(List<TextView> tvCues) {
		this.tvCues = tvCues;
	}

	public ModuleDataTag getDataTag() {
		return dataTag;
	}

	public void setDataTag(ModuleDataTag dataTag) {
		this.dataTag = dataTag;
	}

	public LinearLayout getContent() {
		return row_layout;
	}

	public void setContent(LinearLayout content) {
		this.row_layout = content;
	}

	public ChannelDataTag getChannelDataTag() {
		return channelDataTag;
	}

	public void setChannelDataTag(ChannelDataTag channelDataTag) {
		this.channelDataTag = channelDataTag;
	}

	public LinearLayout getParentLayout() {
		return parentLayout;
	}

	public void setParentLayout(LinearLayout parentLayout) {
		this.parentLayout = parentLayout;
	}

}

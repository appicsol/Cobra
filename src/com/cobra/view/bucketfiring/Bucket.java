package com.cobra.view.bucketfiring;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cobra.MainActivity;
import com.cobra.R;
import com.cobra.appClass;
import com.cobra.api.Cobra;
import com.cobra.api.CobraFlags;
import com.cobra.api.Cobra.CobraEvent;
import com.cobra.api.Cobra.ICobraEventListener;
import com.cobra.classes.BucketLabels;
import com.cobra.classes.Buckets;
import com.cobra.classes.Constants;
import com.cobra.classes.GenerateBackground;
import com.cobra.classes.GenerateRandomColor;
import com.cobra.classes.Modules;
import com.cobra.classes.MySpinner;
import com.cobra.db.DBHelper;
import com.cobra.dialogs.BucketTagsDialog;
import com.cobra.dialogs.BucketTimeDialog;
import com.cobra.views.BucketFiring;
import com.cobra.views.ModulesContainer;
import com.cobra.views.PersistentHeader;

/**
 * Child class of {@link MainActivity} This class is used to create New bucket
 * very first bucket on position "-1" its creating a bucket that allow user to
 * create new bucket when user drop module on it {@link #btn_AddTag allow user
 * to add new tags at runtime} {@link BucketFiring#array contains array of tags
 * that are used in labels} {@link #cobraEventListener maintain UI based on ARM
 * and TEST mode on 18R2}
 */
public class Bucket extends BucketFiring {

	public Bucket(final Activity activity, final Context context,
			final int position, final String bucketName, final Cobra cobra) {
		super(context);

		RelativeLayout Content_Holder = null;
		if (position == -1) {
			CreateBucket_AddMore(context);
		} else {
			final View view = View.inflate(context, R.layout.list_item_bucket,
					null);
			bucketList_Data.get(position).setView(view);
			final TextView txt_Bucket_Name = (TextView) view
					.findViewById(R.id.txt_buckerName);
			txt_Bucket_Name.setText(bucketName);

			final LinearLayout layout_FiringDetailContainer = (LinearLayout) view
					.findViewById(R.id.layout_firing_container);
			ModulesContainer ModuleContainer = (ModulesContainer) view
					.findViewById(R.id.modulecontainer);

			Content_Holder = (RelativeLayout) view
					.findViewById(R.id.content_holder);
			Content_Holder.setBackground(GenerateBackground
					.Background(activity));

			final Button btn_Fire = (Button) view.findViewById(R.id.btn_fire);
			final Button btn_Pause = (Button) view.findViewById(R.id.btn_pause);
			MySpinner spinner = (MySpinner) view
					.findViewById(R.id.bucket_spinner);

			TextView btn_AddTag = (TextView) view.findViewById(R.id.btn_addtag);
			final ModulesContainer LabelContainer = (ModulesContainer) view
					.findViewById(R.id.labelcontainer);

			int RadiusRightSide = 25;
			txt_Bucket_Name
					.setBackground(GenerateBackground.Background(activity,
							Constants.WHITE, Constants.WHITE, 0, false, 0.2f));

			RadiusRightSide = 25;
			btn_AddTag.setBackground(GenerateBackground.Background(activity,
					Constants.GERY_LIGHT, RadiusRightSide));

			final LinearLayout time_layout = (LinearLayout) view
					.findViewById(R.id.layout_time);

			final TextView time_value = (TextView) view
					.findViewById(R.id.time_text);
			time_value.setText(""
					+ bucketList_Data.get(position).getBucketTime());

			time_layout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					final BucketTimeDialog dialog = new BucketTimeDialog(
							context);
					array_time = DBHelper.getTime(context);
					Is_ShellUpdate_Enable = true;
					dialog.UpdateTimeArray();
					dialog.show();
					dialog.getWindow()
							.setSoftInputMode(
									WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
					dialog.getCloseButton().setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							});
					dialog.getAddButton().setOnClickListener(
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									String newTime = dialog.getNewTime();
									Boolean IsFound = false;
									for (int i = 0; i < array_time.length; i++) {
										if (newTime.equals(array_time[i])) {
											IsFound = true;
											break;
										}
									}
									if (newTime != null) {
										time_value.setText(newTime);
										bucketList_Data
												.get(position)
												.setBucketTime(
														Integer.parseInt(newTime));
										DBHelper.UpdateBucketTime(context,
												bucketName, newTime);
									}
									if (!IsFound) {
										DBHelper.insertTime(context, newTime);
									} else {
										Toast.makeText(context,
												"Already Exists",
												Toast.LENGTH_LONG).show();
									}
									dialog.dismiss();
								}
							});

					dialog.getTimeList().setOnItemClickListener(
							new OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int pos, long id) {

									int i = position;

									time_value.setText(array_time[pos]);
									DBHelper.UpdateBucketTime(context,
											bucketList_Data.get(position)
													.getBucketName(),
											array_time[pos]);
									bucketList_Data
											.get(position)
											.setBucketTime(
													Integer.parseInt(array_time[pos]));
									dialog.dismiss();
								}
							});
				}
			});

			btn_AddTag.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// spnr_AddTag.performClick();
					final BucketTagsDialog dialog = new BucketTagsDialog(
							context);
					// array = DBHelper.getLabels(context);//
					// appClass.getTagsArrayFromSharedPreferences(context);
					array_labels = DBHelper.getLabels(context);
					Is_ShellUpdate_Enable = true;
					dialog.UpdateTagArray();
					dialog.show();
					dialog.getWindow()
							.setSoftInputMode(
									WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
					dialog.getCloseButton().setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							});
					dialog.getAddButton().setOnClickListener(
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									String tagName = dialog.getName();
									if (tagName != null
											&& !bucketList_Data.get(position)
													.IsLabelExists(tagName)) {

										new Label(activity, context,
												LabelContainer, position,
												tagName);

										// Label_Data label=new Label_Data();
										// label.setLabel_Name(tagName);
										// label_Array.add(label);
										DBHelper.insertLabel(context, tagName);
										DBHelper.InsertBucketLabel(context,
												position, tagName);
									} else
										Toast.makeText(context,
												"Already exists",
												Toast.LENGTH_LONG).show();
									dialog.dismiss();
								}
							});

					dialog.getTagsList().setOnItemClickListener(
							new OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int pos, long id) {

									int i = position;
									Buckets bucket = null;
									try {
										bucket = bucketList_Data.get(position);

									} catch (Exception e) {
										bucket = bucketList_Data
												.get(position - 1);
									}
									if (!bucket
											.IsLabelExists(array_labels[pos])) {

										new Label(activity, context,
												LabelContainer, position,
												array_labels[pos]);
										DBHelper.InsertBucketLabel(context,
												position, array_labels[pos]);
									}

									else
										Toast.makeText(context,
												"Already exists",
												Toast.LENGTH_LONG).show();
									dialog.dismiss();
								}
							});
				}
			});

			// -----------------------------------------------------------------------------------------

			String[] spinneritems = new String[] { Constants.BUCKET_STATUS_SEQ,
					Constants.BUCKET_STATUS_RANDOM };
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
					android.R.layout.simple_dropdown_item_1line,
					android.R.id.text1, spinneritems);
			spinner.setAdapter(adapter);

			spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int pos, long id) {
					// TODO Auto-generated method stub
					if (pos == 0) {
						bucketList_Data.get(position).setBucketStatus(
								Constants.BUCKET_STATUS_SEQ);
						DBHelper.UpdateBucketStatus(context, bucketName,
								Constants.BUCKET_STATUS_SEQ);
					}
					if (pos == 1) {
						bucketList_Data.get(position).setBucketStatus(
								Constants.BUCKET_STATUS_RANDOM);
						DBHelper.UpdateBucketStatus(context, bucketName,
								Constants.BUCKET_STATUS_RANDOM);
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub

				}
			});

			String bucketStatus = bucketList_Data.get(position)
					.getBucketStatus();
			if (bucketStatus == null) {
				bucketStatus = Constants.BUCKET_STATUS_SEQ;
				DBHelper.UpdateBucketStatus(context, bucketName, bucketStatus);
			}
			if (bucketStatus.equals(Constants.BUCKET_STATUS_SEQ))
				spinner.setSelection(0);
			else if (bucketStatus.equals(Constants.BUCKET_STATUS_RANDOM))
				spinner.setSelection(1);

			final TextView fired_text = (TextView) view
					.findViewById(R.id.fired_text);

			final TextView remain_text = (TextView) view
					.findViewById(R.id.remain_text);

			fired_text.setText(getFiredCues(position, true));
			remain_text.setText(getFiredCues(position, false));
			bucketList_Data.get(position).setFiredCues(fired_text);
			bucketList_Data.get(position).setRemainCues(remain_text);
			bucketList_Data.get(position).set_BtnFired(btn_Fire);
			bucketList_Data.get(position).set_BtnPause(btn_Pause);
			btn_Fire.setBackground(GenerateBackground.Background(activity,
					Constants.RED_DARK, false, 6f, 6f, 0.5f, 0.5f));

			spinner.setBackground(GenerateBackground.Background(activity,
					Constants.WHITE, false, 3.4f, 3.4f, 0.5f, 0.5f));
			btn_Pause.setBackground(GenerateBackground.Background(activity,
					Constants.GREEN, false, 4.8f, 4.8f, 0.5f, 0.5f));
			// (activity,Constants.RED_DARK, Constants.BLACK, 255, false,
			// 0.5f));
			btn_Pause.setVisibility(View.GONE);
			bucketList_Data.get(position).setPaused(false);
			btn_Fire.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					if (cobra.getMode() == Cobra.MODE_ARMED) {
						ArrayList<String> moduleAddressses = new ArrayList<String>();
						for (int i = 0; i < draggedModules_List.size(); i++) {
							String DM_BktID = draggedModules_List.get(i)
									.getBucketId();

							if (DM_BktID.equals("" + position)) {

								moduleAddressses.add(draggedModules_List.get(i)
										.getModuleAddress());
							}

						}
						if (moduleAddressses.size() > 0) {

							HandleBucketView(view, false);

							btn_Fire.setVisibility(View.GONE);
							btn_Pause.setVisibility(View.VISIBLE);
							btn_Pause.setEnabled(true);
							btn_Fire.setEnabled(true);
							bucketList_Data.get(position).setPaused(true);

							new TimelyCueFiring(context, position,
									moduleAddressses, cobra).execute();
						} else {
							Toast.makeText(context, "No module to fire",
									Toast.LENGTH_LONG).show();

						}

					} else {
						Toast.makeText(context, "Remote is on test mode",
								Toast.LENGTH_LONG).show();
					}

				}
			});

			btn_Pause.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					bucketList_Data.get(position).setPaused(false);

					HandleBucketView(view, true);
					btn_Pause.setVisibility(View.GONE);
					btn_Fire.setVisibility(View.VISIBLE);
				}

			});
			// -----------------------------------------------------------------------------------------

			Button btn_RandomFire = (Button) view
					.findViewById(R.id.btn_randomfire);
			btn_RandomFire.setBackground(GenerateBackground.Background(
					activity, Constants.WHITE, true, -1));
			// ----------------------------------------------------------------------------------------

			GenerateRandomColor.ClearRGB_Range();

			Moduleslist_UI.add(ModuleContainer);
			linearList.addView(view, bucketList_UI.size());
			bucketList_UI.add(bucketList_UI.size(), Content_Holder);

			for (int j = 0; j < BucketLabels_List.size(); j++) {
				BucketLabels bucketLabel = BucketLabels_List.get(j);

				if (bucketLabel.getBucketID().equals("" + position)) {
					String LabelName = array_labels[GetLabelIndex(bucketLabel
							.getLabelName())];
					bucketList_Data.get(position).IsLabelExists(LabelName);
					CreateStoredLabels(activity, context, LabelContainer,
							position, LabelName);
				}
			}

			if (cobra != null) {
				if (cobra.getMode() == Cobra.MODE_ARMED) {
					layout_FiringDetailContainer.setVisibility(View.VISIBLE);
				} else {
					layout_FiringDetailContainer.setVisibility(View.GONE);
				}

				cobra.AddBucketFiringListener(new ICobraEventListener() {
					@Override
					public void onDeviceDataChange(CobraEvent event) {
						// TODO Auto-generated method stub
						switch (event.getEventType()) {
						case Cobra.EVENT_TYPE_STATUS_CHANGE:
							if (cobra.getMode() == Cobra.MODE_ARMED) {
								layout_FiringDetailContainer
										.setVisibility(View.VISIBLE);
							} else if (cobra.getMode() == Cobra.MODE_TEST) {
								layout_FiringDetailContainer
										.setVisibility(View.GONE);
							}
							break;

						default:
							break;
						}
					}
				});
			}

		}

	}

	public static BroadcastReceiver BucketReciever = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				int event = -1;
				if (intent.hasExtra(appClass.TAG_EVENT)) {
					event = intent.getExtras().getInt(appClass.TAG_EVENT);
				}
				switch (event) {
				case Cobra.EVENT_TYPE_CHANNEL_DATA_CHANGE:
					String channel=intent.getExtras().getString("channel");
					String firedCues=intent.getExtras().getString("firedCues");
					String scriptCues=intent.getExtras().getString("scriptCues");
					
					Toast.makeText(context, "channel: "+channel+"& firedCues:"+firedCues+" & scriptCues: "+scriptCues, Toast.LENGTH_SHORT).show();
					break;

				default:
					break;
				}
			} catch (Exception e) {

			}
		}
	};

	private void HandleBucketView(View view, Boolean status) {
		for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
			View temp_view = ((ViewGroup) view).getChildAt(i);
			if (temp_view.getId() == R.id.labelcontainer) {
				for (int i1 = 0; i1 < ((ViewGroup) temp_view).getChildCount(); i1++) {
					View temp_view1 = ((ViewGroup) temp_view).getChildAt(i1);
					temp_view1.setEnabled(status);
				}
			}
			if (temp_view.getId() == R.id.modulecontainer) {
				for (int i1 = 0; i1 < ((ViewGroup) temp_view).getChildCount(); i1++) {
					View temp_view1 = ((ViewGroup) temp_view).getChildAt(i1);
					temp_view1.setEnabled(status);
				}
			}
			if (temp_view.getId() == R.id.layout_firing_container) {
				for (int i1 = 0; i1 < ((ViewGroup) temp_view).getChildCount(); i1++) {
					View temp_view1 = ((ViewGroup) temp_view).getChildAt(i1);

					temp_view1.setEnabled(status);
				}
			}
			if (temp_view.getId() == R.id.layout_time) {
				for (int i1 = 0; i1 < ((ViewGroup) temp_view).getChildCount(); i1++) {
					View temp_view1 = ((ViewGroup) temp_view).getChildAt(i1);
					temp_view1.setEnabled(status);
				}
			}
		}
	}

	private int GetLabelIndex(String LabelName) {
		for (int i = 0; i < array_labels.length; i++) {
			if (array_labels[i].equals(LabelName))
				return i;
		}
		return 0;
	}

	private void CreateStoredLabels(Activity activity, Context context,
			ModulesContainer LabelContainer, int bucketID, String labelName) {
		new Label(activity, context, LabelContainer, bucketID, labelName);
	}

	private String getFiredCues(int bucket_id, Boolean IsFiredCues) {
		int fired_cues = 0;
		int remaining_cues = 0;
		for (int i = 0; i < draggedModules_List.size(); i++) {
			if (draggedModules_List.get(i).getBucketId().equals("" + bucket_id)) {
				for (int j = 0; j < moduleList_Data.size(); j++) {

					String M_Adr = "" + moduleList_Data.get(j).getAddress();
					String DM_Adr = draggedModules_List.get(i)
							.getModuleAddress();
					Boolean IsAnyCueAvailable = moduleList_Data.get(j)
							.getModuleCues().IsAnyCueAvailable();

					if (M_Adr.equals(DM_Adr) && IsAnyCueAvailable) {

						fired_cues += moduleList_Data.get(j).getModuleCues()
								.getFiredCues();
						remaining_cues += (18 - fired_cues);
					}
				}
				break;
			}
		}
		if (IsFiredCues)
			return "" + fired_cues;
		else
			return "" + remaining_cues;
	}

	private void HandleFiredCuesOnModule(Context context, String moduleAddress,
			int fired_cue, Modules modules) {
		DBHelper.UpdateCue(context, moduleAddress, fired_cue,
				Constants.CUE_STATE_FIRED);
		switch (fired_cue) {
		case 1:
			modules.getModuleCues().setCue_1(Constants.CUE_STATE_FIRED);
			break;
		case 2:
			modules.getModuleCues().setCue_2(Constants.CUE_STATE_FIRED);
			break;
		case 3:
			modules.getModuleCues().setCue_3(Constants.CUE_STATE_FIRED);
			break;
		case 4:
			modules.getModuleCues().setCue_4(Constants.CUE_STATE_FIRED);
			break;
		case 5:
			modules.getModuleCues().setCue_5(Constants.CUE_STATE_FIRED);
			break;
		case 6:
			modules.getModuleCues().setCue_6(Constants.CUE_STATE_FIRED);
			break;
		case 7:
			modules.getModuleCues().setCue_7(Constants.CUE_STATE_FIRED);
			break;
		case 8:
			modules.getModuleCues().setCue_8(Constants.CUE_STATE_FIRED);
			break;
		case 9:
			modules.getModuleCues().setCue_9(Constants.CUE_STATE_FIRED);
			break;
		case 10:
			modules.getModuleCues().setCue_10(Constants.CUE_STATE_FIRED);
			break;
		case 11:
			modules.getModuleCues().setCue_11(Constants.CUE_STATE_FIRED);
			break;
		case 12:
			modules.getModuleCues().setCue_12(Constants.CUE_STATE_FIRED);
			break;
		case 13:
			modules.getModuleCues().setCue_13(Constants.CUE_STATE_FIRED);
			break;
		case 14:
			modules.getModuleCues().setCue_14(Constants.CUE_STATE_FIRED);
			break;
		case 15:
			modules.getModuleCues().setCue_15(Constants.CUE_STATE_FIRED);
			break;
		case 16:
			modules.getModuleCues().setCue_16(Constants.CUE_STATE_FIRED);
			break;
		case 17:
			modules.getModuleCues().setCue_17(Constants.CUE_STATE_FIRED);
			break;
		case 18:
			modules.getModuleCues().setCue_18(Constants.CUE_STATE_FIRED);
			break;
		default:
			break;
		}
	}

	private class TimelyCueFiring extends AsyncTask<Integer, Void, Integer> {

		private int list_id = 0;
		private int module_id = 0;
		private int total_fired_cues = 0;
		private int total_remaining_cues = 0;
		private int CueNo = 0;
		private Context context;
		private ArrayList<String> module_addresses;
		private int Random_Module_Index;
		private Cobra cobra;
		private Boolean IsRandom = false;

		public TimelyCueFiring(Context context, int list_id,
				ArrayList<String> module_addresses, Cobra cobra) {
			try {
				this.context = context;
				this.list_id = list_id;
				this.module_addresses = module_addresses;
				this.cobra = cobra;
				Random random = new Random();
				if (module_addresses.size() > 0)
					Random_Module_Index = random.nextInt(module_addresses
							.size());
				if (bucketList_Data.get(list_id).getBucketStatus()
						.equals(Constants.BUCKET_STATUS_SEQ))
					IsRandom = false;
				else
					IsRandom = true;

				if (!IsRandom)
					Random_Module_Index = 0;
				for (int j = 0; j < moduleList_Data.size(); j++) {
					String M_Adr = "" + moduleList_Data.get(j).getAddress();
					String DM_Adr = this.module_addresses
							.get(Random_Module_Index);
					if (M_Adr.equals(DM_Adr)) {
						module_id = j;
						break;
					}
				}
			} catch (Exception e) {
				int i = 0;
				i++;
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			try {
				total_fired_cues = Integer.parseInt(bucketList_Data
						.get(list_id).getFiredCues().getText().toString());
				total_remaining_cues = Integer.parseInt(bucketList_Data
						.get(list_id).getRemainCues().getText().toString());
			} catch (Exception e) {
				int i = 0;
				i++;
			}
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			try {
				Boolean IsCueAvailable = moduleList_Data.get(module_id)
						.getModuleCues().IsAnyCueAvailable();

				if (IsCueAvailable) {

					total_fired_cues++;
					total_remaining_cues--;

					if (IsRandom)
						CueNo = moduleList_Data.get(module_id).getModuleCues()
								.getRandomCue();
					else
						CueNo = moduleList_Data.get(module_id).getModuleCues()
								.getNextAvailableCue();
					int a = (int) Math.pow(2, (CueNo - 1));
					int Channel = moduleList_Data.get(module_id).getChannel();
					cobra.fireCues(a, (byte) Channel);
					if (moduleList_Data.get(module_id).getModuleCues()
							.IsLastCue())
						this.module_addresses.remove(Random_Module_Index);
					return 1;
				} else {
					this.module_addresses.remove(Random_Module_Index);
				}
			} catch (Exception e) {
				int i = 0;
				i++;
			}
			return 0;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				if (result == 1) {
					bucketList_Data.get(list_id).getFiredCues()
							.setText("" + total_fired_cues);
					bucketList_Data.get(list_id).getRemainCues()
							.setText("" + total_remaining_cues);
					HandleFiredCuesOnModule(this.context, ""
							+ moduleList_Data.get(module_id).getAddress(),
							CueNo, moduleList_Data.get(module_id));

					TOTAL_FIRED_CUES++;
					TOTAL_REMAINING_CUES--;
					UpdateCuesData();
				}

				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (module_addresses.size() > 0) {
							new TimelyCueFiring(context, list_id,
									module_addresses, cobra).execute();
						} else {
							HandleBucketView(bucketList_Data.get(list_id)
									.getView(), true);

							bucketList_Data.get(list_id).get_BtnFired()
									.setVisibility(View.VISIBLE);
							bucketList_Data.get(list_id).get_BtnPause()
									.setVisibility(View.GONE);
							bucketList_Data.get(list_id).setPaused(false);
						}
					}
				}, bucketList_Data.get(list_id).getBucketTime());
			} catch (Exception e) {
				int i = 0;
				i++;
			}
		}

	}

}

package com.cobra.views;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.NumberPicker.Formatter;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cobra.MainActivity;
import com.cobra.R;
import com.cobra.R.color;
import com.cobra.appClass;
import com.cobra.api.Cobra;
import com.cobra.api.CobraDataTags.EventDataTag;
import com.cobra.api.CobraDataTags.ScriptIndexTag;
import com.cobra.api.CobraDataTags.ScriptPingTag;
import com.cobra.dialogs.DialogBuy;
import com.cobra.dialogs.DialogWrongScript;
import com.cobra.showcontrol.interfaces.OnWrongScriptListener;
//import android.widget.ListView;
//import com.cobra.api.Cobra.CobraEvent2;

public class ShowControls extends Fragment {

	/**
	 * The current state of the script running on the 18R2 (paused, playing, or
	 * stopped)
	 */
	private ScriptState state = ScriptState.STOPPED;

	/** The past two ping times for determining the script state on start-up */
	private long previousElapsedTime[] = new long[2];

	/** The script duration in tenths of a second. */
	private long scriptDurationTenths = 0;
	private ArrayList<EventRow> eventRows;
	private List<String> scriptSpinnerItems;

	private Chronometer timeElapsed;
	private Chronometer nextEventTime;
	private TextView timeRemaining, scriptChTextView, scriptButTextView,
			scriptDMTextView, scriptRCTextView, scriptAudioFileTextView,
			scriptLengthTextView;
	private TextView scriptStateTextView;
	private static View rootView;
	private ImageView playPauseButton, stopButton;
	private Spinner scriptSpinner;
	private Button jump, jumpTo;
	private Boolean IsStepScript = false;
	private static NumberPicker npHours, npMinutes, npSeconds, npTenths;
	// private ListView eventList;
	private LinearLayout linear_eventList;
	// private ArrayAdapter<EventRow> eventArrayAdapter;
	private ArrayList<EventRow> eventArrayAdapter;

	private int nextEventIndex = -1;
	private int lastFiredEventIndex = -1;

	private ArrayList<ScriptIndexTag> scripts;
	private SparseArray<SparseArray<EventDataTag>> events;
	// private SparseArray<SparseArray<EventDataTag>> tempEvents;
	/** Int to hold the index of the currently selected script in the Spinner. */
	private int spinnerPosition;
	private ArrayList<String> scriptList;

	private Cobra myCobra;

	private int previousEventToChangeInWhite = -1;
	private int previousEventIndex = -1;
	private int currentEventIndex;

	private int lastIndexFired = 0;

	private ScriptPingTag pingTag;
	private Context mContext;
	private Formatter npFormatter, npFormatterTenths;
	// private Boolean IsJumpPressed = false;

	// boolean JUST_JUMP = false;

	private OnWrongScriptListener onWrongScriptListener_inShowControls;

	private ScrollView scrollView;
	/**
	 * When the script has a STEP, the timeIndex of that STEP event is the
	 * previous non-STEP event's timeIndex plus 1 plus this constant
	 */
	private static long STEP_CONST = 1048576;

	private boolean JUST_JUMPED = false;

	private enum ScriptState {
		STOPPED, PLAYING, PAUSED
	}

	private Activity activity;

	appClass globV;

	private int lastColor = Color.LTGRAY;

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		this.activity = getActivity();

		globV = (appClass) getActivity().getApplicationContext();

		rootView = View.inflate(getActivity(), R.layout.showcontrols, null);

		if (MainActivity.scriptStatus != 1) { // condition when there is no
												// scripts loaded

			scriptSpinner = (Spinner) rootView.findViewById(R.id.scriptSpinner);
			scriptChTextView = (TextView) rootView
					.findViewById(R.id.scriptChTextView);
			scriptButTextView = (TextView) rootView
					.findViewById(R.id.scriptButTextView);
			scriptDMTextView = (TextView) rootView
					.findViewById(R.id.scriptDMTextView);
			scriptRCTextView = (TextView) rootView
					.findViewById(R.id.scriptRCTextView);
			scriptAudioFileTextView = (TextView) rootView
					.findViewById(R.id.scriptAudioFileTextView);
			timeElapsed = (Chronometer) rootView
					.findViewById(R.id.currentTimeChronometer);
			nextEventTime = (Chronometer) rootView
					.findViewById(R.id.nextEventChronometer);
			timeRemaining = (TextView) rootView
					.findViewById(R.id.timeRemainingTextView);
			scriptLengthTextView = (TextView) rootView
					.findViewById(R.id.scriptLengthTextView);

			ArrayList<String> scriptList = getScriptList();

			ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
					getActivity(), R.layout.show_control_spinner_item,
					scriptList);

			// set one item of "No Scripts"
			spinnerArrayAdapter
					.setDropDownViewResource(android.R.layout.simple_list_item_1);
			scriptSpinner.setAdapter(spinnerArrayAdapter);
			scriptSpinner.setClickable(false);

			scriptChTextView.setText("");
			scriptButTextView.setText("");
			scriptDMTextView.setText("");
			scriptRCTextView.setText("");
			scriptAudioFileTextView.setText("");
			timeElapsed.setText("");
			nextEventTime.setText("");
			timeRemaining.setText("");
			scriptLengthTextView.setText("");

			return rootView;
		}

		mContext = getActivity();
		timeElapsed = (Chronometer) rootView
				.findViewById(R.id.currentTimeChronometer);
		nextEventTime = (Chronometer) rootView
				.findViewById(R.id.nextEventChronometer);

		timeRemaining = (TextView) rootView
				.findViewById(R.id.timeRemainingTextView);
		playPauseButton = (ImageView) rootView
				.findViewById(R.id.PlayPauseButton);
		stopButton = (ImageView) rootView.findViewById(R.id.StopButton);
		scriptSpinner = (Spinner) rootView.findViewById(R.id.scriptSpinner);
		// eventList = (ListView) rootView.findViewById(R.id.listView1);
		linear_eventList = (LinearLayout) rootView
				.findViewById(R.id.linear_list);
		scrollView = (ScrollView) rootView.findViewById(R.id.scrollView2);

		playPauseButton.setOnClickListener(playPauseButtonListener);
		stopButton.setOnClickListener(stopButtonListener);
		scriptChTextView = (TextView) rootView
				.findViewById(R.id.scriptChTextView);
		scriptButTextView = (TextView) rootView
				.findViewById(R.id.scriptButTextView);
		scriptDMTextView = (TextView) rootView
				.findViewById(R.id.scriptDMTextView);
		scriptRCTextView = (TextView) rootView
				.findViewById(R.id.scriptRCTextView);
		scriptAudioFileTextView = (TextView) rootView
				.findViewById(R.id.scriptAudioFileTextView);
		scriptLengthTextView = (TextView) rootView
				.findViewById(R.id.scriptLengthTextView);
		scriptStateTextView = (TextView) rootView
				.findViewById(R.id.scriptStateTextView);
		jumpTo = (Button) rootView.findViewById(R.id.jumpToButton);
		btn_AutoScroll = (Button) rootView.findViewById(R.id.btn_autoscroll);

		btn_AutoScroll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// if (appClass.isDemoMode) {// DEMO
				// showDialogBuy();
				// return;
				// }

				if (IsAutoScrollEnable) {
					btn_AutoScroll.setText("Enable Auto Scroll");
					IsAutoScrollEnable = false;
				} else {
					btn_AutoScroll.setText("Disable Auto Scroll");
					IsAutoScrollEnable = true;
				}
			}
		});

		globV.setOnWrongScriptListener(onWrongScriptListener);
		myCobra = globV.getCobra();
		onWrongScriptListener_inShowControls = globV.getOnWrongScriptListener();

		try {
			events = globV.getCobra().getEventsData();
			scripts = globV.getCobra().getScriptsData();
		} catch (Exception e) {
		}

		int key = 0;

		// LayoutInflater inflater = (LayoutInflater) getActivity()
		// .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View npView = inflater.inflate(R.layout.numberpicker, null);
		npHours = (NumberPicker) npView.findViewById(R.id.npHours);
		npMinutes = (NumberPicker) npView.findViewById(R.id.npMinutes);
		npSeconds = (NumberPicker) npView.findViewById(R.id.npSeconds);
		npTenths = (NumberPicker) npView.findViewById(R.id.npTenths);
		// disables the keyboard from popping up when the numberPicker is shown
		npHours.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		npMinutes
				.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		npSeconds
				.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		npTenths.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		npHours.getChildAt(0).clearFocus();

		npFormatter = new NumberPicker.Formatter() {
			@Override
			public String format(int value) {
				return String.format("%02d", value);
			}
		};

		npFormatterTenths = new NumberPicker.Formatter() {
			@Override
			public String format(int value) {
				return String.format("%01d", value);
			}
		};

		npHours.setWrapSelectorWheel(true);
		npMinutes.setWrapSelectorWheel(true);
		npSeconds.setWrapSelectorWheel(true);
		npTenths.setWrapSelectorWheel(true);

		npHours.setFormatter(npFormatter);
		npMinutes.setFormatter(npFormatter);
		npSeconds.setFormatter(npFormatter);
		npTenths.setFormatter(npFormatterTenths);

		// this try-catch is a solution to the android bug of the NumberPicker's
		// formatter not formatting the first element in the numberPicker

		try {
			Method method = npHours.getClass().getDeclaredMethod(
					"changeValueByOne", boolean.class);
			method.setAccessible(true);
			method.invoke(npHours, true);

		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {

		}

		try {
			Method method = npMinutes.getClass().getDeclaredMethod(
					"changeValueByOne", boolean.class);
			method.setAccessible(true);
			method.invoke(npMinutes, true);

		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {

		}

		ArrayList<String> scriptList = getScriptList();

		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
				getActivity(), R.layout.show_control_spinner_item, scriptList);
		spinnerArrayAdapter
				.setDropDownViewResource(android.R.layout.simple_list_item_1);

		// demo mode set hide sppinner list
		// if (appClass.isDemoMode) {// DEMO
		//
		// scriptSpinner.setOnTouchListener(new View.OnTouchListener() {
		// @Override
		// public boolean onTouch(View view, MotionEvent motionEvent) {
		// showDialogBuy();
		// return false;
		//
		// }
		// });
		//
		// } else {
		scriptSpinner.setAdapter(spinnerArrayAdapter);
		scriptSpinner.setLayoutMode(Spinner.MODE_DROPDOWN);
		scriptSpinner.setOnItemSelectedListener(scriptSpinnerItemListener);
		// }

		eventRows = new ArrayList<EventRow>();

		eventArrayAdapter = new ArrayList<EventRow>();
		new ListLoader().execute();

		final AlertDialog.Builder jumpToPopup = new AlertDialog.Builder(
				getActivity());
		jumpToPopup.setTitle("Jump To...");
		// jumpToPopup.setMessage("Pick a time to jump to:");// \n
		// \t\t\t\t\t\t\thrs
		// : min : sec
		jumpToPopup.setView(npView);
		// Add the buttons
		jumpToPopup.setPositiveButton("Jump",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						// Jump with time

						is18R2Play = true;

						long hr = TimeUnit.HOURS.toMillis((long) npHours
								.getValue());
						long min = TimeUnit.MINUTES.toMillis((long) npMinutes
								.getValue());
						long sec = TimeUnit.SECONDS.toMillis((long) npSeconds
								.getValue());
						long tenths = ((hr + min + sec) / 100l)
								+ (long) npTenths.getValue();

						switch (state) {

						case STOPPED:

							if (myCobra.isArmed()) {

								firstStartTime = tenths;

								// clearLastFiredEvent(last_green_index,
								// last_red_index);

								int position = -1;

								for (int i = 0; i < eventArrayAdapter.size(); i++) {
									if (tenths <= eventArrayAdapter.get(i)
											.getEventTime()) {
										position = i;
										break;
									}
								}

								if (position != -1) {

									jumpColor(position, eventArrayAdapter,
											Color.GREEN);

									clearLastFiredEvent_Onjump(position,
											eventArrayAdapter,
											last_green_index, last_red_index);

									eventArrayAdapter.get(position)
											.setBackgroundColor(Color.GREEN);

									myCobra.jumpToEvent(events.get(
											spinnerPosition).get(position).eventIndex);

									firstStartIndex = position;

									last_green_index = position;

									last_received_index = position;
								}

								// update times
								if (position < eventArrayAdapter.size()
										&& events.get(spinnerPosition).get(
												position) != null) {
									long hr1 = TimeUnit.MILLISECONDS
											.toHours((events.get(
													spinnerPosition).get(
													position).timeIndex * 100l));

									if (hr1 > 24) {
										int i = 0;
										i++;
										setChronometerTimes_WhileJumped(
												0,
												events.get(spinnerPosition)
														.get(position).eventIndex);
									} else {
										setChronometerTimes_WhileJumped(
												events.get(spinnerPosition)
														.get(position).timeIndex,
												events.get(spinnerPosition)
														.get(position).eventIndex);
									}
								}

								// //
							} else {
								Toast.makeText(getActivity(),
										"Remote is in TEST mode",
										Toast.LENGTH_SHORT).show();
							}

							break;
						case PLAYING:

							if (pingTag != null && tenths < currentTime) {
								Toast.makeText(
										getActivity(),
										"To re-fire an already fired event, please stop and re-start the script.",
										Toast.LENGTH_SHORT).show();
							} else {
								if (myCobra.isArmed()) {
									if (state == ScriptState.STOPPED) {
										myCobra.startScript(
												scripts.get(spinnerPosition).scriptID,
												true);
									}

									myCobra.jumpToTime(tenths);
								} else {
									Toast.makeText(getActivity(),
											"Remote is in TEST mode",
											Toast.LENGTH_SHORT).show();
								}
							}

							break;
						case PAUSED:

							boolean check_isfired = false;

							if (firedTimeList != null
									&& firedTimeList.size() > 0) {

								if (tenths <= eventArrayAdapter.get(
										firedTimeList.size()).getEventTime()) {
									check_isfired = true;

								}

							}

							if ((tenths > eventArrayAdapter.get(
									eventArrayAdapter.size() - 1)
									.getEventTime())
									|| (tenths < eventArrayAdapter.get(0)
											.getEventTime())) {

								check_isfired = true;

							}

							if (check_isfired) {
								Toast.makeText(
										getActivity(),
										"To re-fire an already fired event, please stop and re-start the script.",
										Toast.LENGTH_SHORT).show();
							} else {
								if (myCobra.isArmed()) {
									if (state == ScriptState.STOPPED) {
										myCobra.startScript(
												scripts.get(spinnerPosition).scriptID,
												true);
									}
									// //

									// clearLastFiredEvent(last_green_index,
									// last_red_index);

									int position = -1;

									for (int i = 0; i < eventArrayAdapter
											.size(); i++) {
										if (tenths <= eventArrayAdapter.get(i)
												.getEventTime()) {
											position = i;
											break;
										}
									}

									if (position != -1) {

										jumpColor(position, eventArrayAdapter,
												Color.GREEN);

										clearLastFiredEvent_Onjump(position,
												eventArrayAdapter,
												last_green_index,
												last_red_index);

										eventArrayAdapter
												.get(position)
												.setBackgroundColor(Color.GREEN);

										last_green_index = position;
									}

									// //
									myCobra.jumpToTime(tenths);

									// update times

									if (position < eventArrayAdapter.size()) {

										long hr1 = TimeUnit.MILLISECONDS
												.toHours((events.get(
														spinnerPosition).get(
														position).timeIndex * 100l));

										if (hr1 > 24) {
											int i = 0;
											i++;
											setChronometerTimes_WhileJumped(
													0,
													events.get(spinnerPosition)
															.get(position).eventIndex);
										} else {
											setChronometerTimes_WhileJumped(
													events.get(spinnerPosition)
															.get(position).timeIndex,
													events.get(spinnerPosition)
															.get(position).eventIndex);
										}
									}

								} else {
									Toast.makeText(getActivity(),
											"Remote is in TEST mode",
											Toast.LENGTH_SHORT).show();
								}
							}

						}

					}
				});
		jumpToPopup.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				});
		final AlertDialog dialog2 = jumpToPopup.create();
		jumpTo.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (state == ScriptState.PLAYING) {
					Toast.makeText(
							getActivity(),
							"Unable to jump while script is playing. Please pause the script to jump to a specific event.",
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (myCobra.isArmed()) {
					if (!IsStepScript) {
						dialog2.show();
						npHours.setSelected(false);
						npHours.clearFocus();

					} else {
						Toast.makeText(getActivity(),
								"Jump To Time is disabled for STEP scripts",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getActivity(), "Remote is in TEST mode",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		IntentFilter filter = new IntentFilter(appClass.RECIEVER_SHOW_CONTROLS);
		getActivity().registerReceiver(receiver, filter);

		// here set view content visibility if app is in demo mode
		// if (appClass.isDemoMode) {// DEMO
		// linear_eventList.setVisibility(View.GONE);
		//
		// scriptSpinner.setSelection(scriptList.size() - 1);
		// }

		return rootView;
	}

	public EventRow getView(final int position) {

		EventRow convertView = new EventRow(getActivity());

		if (events.get(spinnerPosition).get(position) != null) {

			ArrayList<String> cuesListArray = getCuesArray(events.get(
					spinnerPosition).get(position).cueList);

			// appClass.writeIntoLog(cuesListArray, position+"");

			int Visibility = -1;

			// ///

			String temp = "";

			for (int s = 0; s < cuesListArray.size(); s++) {
				temp = temp + cuesListArray.get(s) + ", ";
			}

			// ////

			if (position > 59) {
				int e = 0;
				e++;
			}

			if (cuesListArray.size() > 1) {
				Visibility = View.GONE;

				appClass.setAN188Log(position
						+ " :Time: "
						+ tenthsToTimeFormat((events.get(spinnerPosition))
								.get(position).shiftedTimeIndex) + " Channel: "
						+ (events.get(spinnerPosition)).get(position).channel
						+ " cue: " + temp);

			} else {

				appClass.setAN188Log(position
						+ " :TimeSHOW: "
						+ tenthsToTimeFormat((events.get(spinnerPosition))
								.get(position).shiftedTimeIndex) + " Channel: "
						+ (events.get(spinnerPosition)).get(position).channel
						+ " cue: " + temp);

				Visibility = View.VISIBLE;
			}
			((TextView) convertView.findViewById(R.id.scriptDescTextView))
					.setText(events.get(spinnerPosition).get(position).eventDescription);

			((TextView) convertView.findViewById(R.id.jumpToEventButton))
					.setVisibility(Visibility);

			((TextView) convertView.findViewById(R.id.scriptDescTextView))
					.setVisibility(Visibility);

			ListView cuesList = (ListView) convertView
					.findViewById(R.id.listView1);

			cuesList.setDivider(null);
			cuesList.setDividerHeight(0);

			// cuesList.setFocusable(false);
			// cuesList.setClickable(false);
			// cuesList.setFocusableInTouchMode(false);

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					getActivity(), android.R.layout.simple_list_item_1,
					android.R.id.text1, cuesListArray) {

				@Override
				public View getView(final int position1, View convertView,
						ViewGroup parent) {
					if (convertView == null) {

						convertView = new EventRow(this.getContext());

					}

					if (position1 == 0) {
						((Button) convertView
								.findViewById(R.id.jumpToEventButton))
								.setVisibility(RelativeLayout.VISIBLE);
						((Button) convertView
								.findViewById(R.id.jumpToEventButton))
								.setEnabled(true);
					} else {
						((Button) convertView
								.findViewById(R.id.jumpToEventButton))
								.setVisibility(RelativeLayout.INVISIBLE);
						((Button) convertView
								.findViewById(R.id.jumpToEventButton))
								.setEnabled(false);
					}

					if (events.get(spinnerPosition).get(position) != null) {
						((TextView) convertView
								.findViewById(R.id.scriptDescTextView))
								.setText(events.get(spinnerPosition).get(
										position).eventDescription);
						((TextView) convertView.findViewById(R.id.tvCh))
								.setText((events.get(spinnerPosition))
										.get(position).channel + "");

						if (events.get(spinnerPosition).get(position).timeIndex < STEP_CONST) {

							// ///////

							// appClass.setAN188Log(position
							// + " :sub_Time: "
							// + ShowControls.tenthsToTimeFormat((events
							// .get(spinnerPosition))
							// .get(position).shiftedTimeIndex));

							((TextView) convertView.findViewById(R.id.tvTime))
									.setText(tenthsToTimeFormat((events
											.get(spinnerPosition))
											.get(position).shiftedTimeIndex));

							// //check if there is same time event
							int pos = position - 1;
							if (pos > 0
									&& (events.get(spinnerPosition))
											.get(position).timeIndex == (events
											.get(spinnerPosition)).get(pos).timeIndex) {

								((Button) convertView
										.findViewById(R.id.jumpToEventButton))
										.setVisibility(View.INVISIBLE);

							}
							// //

						} else {
							((TextView) convertView.findViewById(R.id.tvTime))
									.setText("STEP");
							IsStepScript = true;
						}

						// for resolving crash index out of bound
						ArrayList<String> cuesListArray = null;

						if (position > -1
								&& position < events.get(spinnerPosition)
										.size())

							cuesListArray = getCuesArray(events.get(
									spinnerPosition).get(position).cueList);

						if (cuesListArray != null && position1 > -1
								&& position1 < cuesListArray.size())
							((TextView) convertView.findViewById(R.id.tvCues))
									.setText(cuesListArray.get(position1));

					}

					((Button) convertView.findViewById(R.id.jumpToEventButton))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {

									if (state == ScriptState.PLAYING) {
										Toast.makeText(
												getActivity(),
												"Unable to jump while script is playing. Please pause the script to jump to a specific event.",
												Toast.LENGTH_SHORT).show();
										return;
									} else {

										onJumpClickProcess(position);
									}
								}
							});

					return convertView;
				}

			};

			if (cuesListArray.size() > 1) {
				cuesList.setAdapter(adapter);
				setListViewHeightBasedOnChildren(cuesList);
				cuesList.setVisibility(View.VISIBLE);

			} else
				cuesList.setVisibility(View.GONE);

			if (cuesListArray.size() > 0) {

				((TextView) convertView.findViewById(R.id.tvCues))
						.setText(cuesListArray.get(0));

				((TextView) convertView.findViewById(R.id.tvCues))
						.setVisibility(Visibility);

				((TextView) convertView.findViewById(R.id.tvCh))
						.setText((events.get(spinnerPosition)).get(position).channel
								+ "");
				((TextView) convertView.findViewById(R.id.tvCh))
						.setVisibility(Visibility);
				// a timeIndex of 1048576 or greater represents a STEP
				if (events.get(spinnerPosition).get(position).timeIndex < STEP_CONST) {

					// appClass.setAN188Log(position
					// + " :Time: "
					// + ShowControls.tenthsToTimeFormat((events
					// .get(spinnerPosition)).get(position).shiftedTimeIndex));

					((TextView) convertView.findViewById(R.id.tvTime))
							.setText(tenthsToTimeFormat((events
									.get(spinnerPosition)).get(position).shiftedTimeIndex));
					convertView.setEventTime((events.get(spinnerPosition))
							.get(position).shiftedTimeIndex);

					// //check if there is same time event
					int pos = position - 1;
					if (pos > 0
							&& (events.get(spinnerPosition)).get(position).timeIndex == (events
									.get(spinnerPosition)).get(pos).timeIndex) {

						((Button) convertView
								.findViewById(R.id.jumpToEventButton))
								.setVisibility(View.INVISIBLE);

					}
					// //

				} else {
					((TextView) convertView.findViewById(R.id.tvTime))
							.setText("STEP");
					convertView.setEventTime(0);
					IsStepScript = true;
				}

				((TextView) convertView.findViewById(R.id.tvTime))
						.setVisibility(Visibility);

				((Button) convertView.findViewById(R.id.jumpToEventButton))
						.setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {

								if (state == ScriptState.PLAYING) {
									Toast.makeText(
											getActivity(),
											"Unable to jump while script is playing. Please pause the script to jump to a specific event.",
											Toast.LENGTH_SHORT).show();
									return;
								} else {

									onJumpClickProcess(position);
								}

							}
						});

			} else {
				// ((TextView) convertView.findViewById(R.id.tvCues))
				// .setVisibility(View.GONE);
				//
				// ((TextView) convertView.findViewById(R.id.tvCh))
				// .setVisibility(View.GONE);
				//
				// ((TextView) convertView.findViewById(R.id.tvTime))
				// .setVisibility(View.GONE);

				// nousePos.add(position);

				// convertView.setTag("unuse");

				// convertView.setLayoutParams(new LayoutParams(0, 0));

			}

		} else {
			((TextView) convertView.findViewById(R.id.scriptDescTextView))
					.setText("N/A");
			((TextView) convertView.findViewById(R.id.tvCues)).setText("??");
			((TextView) convertView.findViewById(R.id.tvCh)).setText("??");
			((TextView) convertView.findViewById(R.id.tvTime))
					.setText("??:??:??.?");
		}

		// /////check if there is same time event
		doColor(position, convertView, events.get(spinnerPosition));
		// ////////////

		if (position == nextEventIndex) {
			convertView.setBackgroundColor(Color.GREEN);

		} else if (position == lastFiredEventIndex) {

			convertView.setBackgroundColor(Color.RED);

		}

		// ////

		TextView tv_time = (TextView) convertView.findViewById(R.id.tvTime);
		TextView tv_channel = (TextView) convertView.findViewById(R.id.tvCh);
		TextView tv_cue = (TextView) convertView.findViewById(R.id.tvCues);
		Button jum_to = (Button) convertView
				.findViewById(R.id.jumpToEventButton);

		if (tv_time.getVisibility() == View.GONE
				&& tv_channel.getVisibility() == View.GONE
				&& tv_cue.getVisibility() == View.GONE) {
			jum_to.setVisibility(View.GONE);
		}

		// ////

		return (EventRow) convertView;
	}

	ArrayList<Integer> nousePos = new ArrayList<Integer>();

	public void onJumpClickProcess(final int position) {

		boolean firedCheck = false;

		// Button Jump Condition

		switch (state) {

		case PLAYING:

			if (position <= last_received_index) {

				firedCheck = true;

			}

			break;
		case PAUSED:

			if ((eventArrayAdapter.get(position).getTag() + "").equals("fired")) {

				firedCheck = true;

			} else if ((position - 1) > 0
					&& (eventArrayAdapter.get(position - 1).getTag() + "")
							.equals("fired")) {

				firedCheck = true;

			}

		}

		if (firedCheck) {
			Toast.makeText(
					activity,
					"To re-fire an already fired event, please stop and re-start the script.",
					Toast.LENGTH_LONG).show();
		} else {

			if (!myCobra.isArmed()) {
				Toast.makeText(getActivity(), "Remote is in TEST mode",
						Toast.LENGTH_SHORT).show();
				return;
			}

			AlertDialog.Builder jumpToEventPopup = new AlertDialog.Builder(
					getActivity());
			jumpToEventPopup.setTitle("Jump To...");
			if ((events.get(spinnerPosition)).get(position).timeIndex < STEP_CONST) {
				jumpToEventPopup.setMessage("Jump to "
						+ tenthsToTimeFormat((events.get(spinnerPosition))
								.get(position).timeIndex) + "?");
			} else {
				jumpToEventPopup.setMessage("Jump to STEP?");
			}
			jumpToEventPopup.setPositiveButton("Jump",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int id) {

							// Jump, with button

							if (myCobra.isArmed()) {

								jumpColor(position, eventArrayAdapter,
										Color.GREEN);

								is18R2Play = true;

								// this is in
								// zero case
								if ((position - 1) < 0) {

									if (state == ScriptState.STOPPED) {

										isDirectJumped = true;

										firstStartIndex = position;

										// myCobra.startScript(
										// scripts.get(spinnerPosition).scriptID,
										// true);
									} else {
										isDirectJumped = false;
									}

									if (firstStartIndex > 0) {
										firstStartIndex = position;
									}

									clearLastFiredEvent_Onjump(position,
											eventArrayAdapter,
											last_green_index, last_red_index);

									JUST_JUMPED = true;

									if (!isPauseFiring) {
										myCobra.jumpToEvent(events.get(
												spinnerPosition).get(position).eventIndex);
									} else {

										// myCobra.startScript(last_received_index,
										// last_green_index);
										// myCobra.pauseScript();

										red_index = true;

										eventArrayAdapter
												.get(position)
												.setBackgroundColor(Color.GREEN);

										myCobra.jumpToEvent(events.get(
												spinnerPosition).get(position).eventIndex);

										last_green_index = position;
									}

									if (state == ScriptState.STOPPED) {
										// /
										red_index = true;
										eventArrayAdapter
												.get(position)
												.setBackgroundColor(Color.GREEN);
										myCobra.jumpToEvent(events.get(
												spinnerPosition).get(position).eventIndex);

										last_green_index = firstStartIndex;

										last_received_index = position;

										// /
									}

									// update
									// times

									long hr = TimeUnit.MILLISECONDS
											.toHours((events.get(
													spinnerPosition).get(
													position).timeIndex * 100l));

									if (hr > 24) {
										int i = 0;
										i++;
										setChronometerTimes_WhileJumped(
												0,
												events.get(spinnerPosition)
														.get(position).eventIndex);
									} else {
										setChronometerTimes_WhileJumped(
												events.get(spinnerPosition)
														.get(position).timeIndex,
												events.get(spinnerPosition)
														.get(position).eventIndex);
									}

									return;
								}

								if ((position - 1) > -1
										&& position != last_green_index
										&& !(eventArrayAdapter
												.get(position - 1).getTag() + "")
												.equals("fired")) {

									if (state == ScriptState.STOPPED) {

										isDirectJumped = true;

										firstStartIndex = position;

										// myCobra.startScript(
										// scripts.get(spinnerPosition).scriptID,
										// true);
									} else {
										isDirectJumped = false;
									}

									if (firstStartIndex > 0) {
										firstStartIndex = position;
									}

									clearLastFiredEvent_Onjump(position,
											eventArrayAdapter,
											last_green_index, last_red_index);

									JUST_JUMPED = true;

									if (!isPauseFiring) {
										myCobra.jumpToEvent(events.get(
												spinnerPosition).get(position).eventIndex);
									} else {

										// myCobra.resumeScript();
										// myCobra.pauseScript();

										red_index = true;

										eventArrayAdapter
												.get(position)
												.setBackgroundColor(Color.GREEN);

										myCobra.jumpToEvent(events.get(
												spinnerPosition).get(position).eventIndex);

										last_green_index = position;
									}

									if (state == ScriptState.STOPPED) {
										// /
										red_index = true;
										eventArrayAdapter
												.get(position)
												.setBackgroundColor(Color.GREEN);
										myCobra.jumpToEvent(events.get(
												spinnerPosition).get(position).eventIndex);

										last_green_index = firstStartIndex;

										last_received_index = position;

										// /
									}

									// update
									// times

									long hr = TimeUnit.MILLISECONDS
											.toHours((events.get(
													spinnerPosition).get(
													position).timeIndex * 100l));

									if (hr > 24) {
										int i = 0;
										i++;
										setChronometerTimes_WhileJumped(
												0,
												events.get(spinnerPosition)
														.get(position).eventIndex);
									} else {
										setChronometerTimes_WhileJumped(
												events.get(spinnerPosition)
														.get(position).timeIndex,
												events.get(spinnerPosition)
														.get(position).eventIndex);
									}

								}

							} else {
								Toast.makeText(getActivity(),
										"Remote is in TEST mode",
										Toast.LENGTH_SHORT).show();
							}
						}

					});
			jumpToEventPopup.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
						}
					});
			jumpToEventPopup.create();
			jumpToEventPopup.show();

		}
		// }

	}

	public void doColor(int position, EventRow convertView,
			SparseArray<EventDataTag> eventArray) {

		int pos = position - 1;
		if (pos > -1
				&& eventArray.get(position).timeIndex == eventArray.get(pos).timeIndex
				&& eventArray.get(position).timeIndex < STEP_CONST) {

			convertView.setBackgroundColor(lastColor);

		} else {
			if (position == 0) {
				convertView.setBackgroundColor(Color.WHITE);
				lastColor = Color.WHITE;
			} else {

				if (lastColor == Color.LTGRAY) {
					convertView.setBackgroundColor(Color.WHITE);
					lastColor = Color.WHITE;
				} else if (lastColor == Color.WHITE) {
					convertView.setBackgroundColor(Color.LTGRAY);
					lastColor = Color.LTGRAY;
				}
			}

		}
	}

	public void doColorList(int position, ArrayList<EventRow> convertView,
			ArrayList<EventRow> eventArray) {

		// if (convertView.get(position).getTag() != null) {
		// if (convertView.get(position).getTag().equals("unuse")) {
		// return;
		// }
		// }

		// boolean unuseEsixt = false;
		// if (nousePos.size() > 0) {
		// for (int s = 0; s < nousePos.size(); s++) {
		// if (position == nousePos.get(s)) {
		// unuseEsixt = true;
		// break;
		// }
		// }
		// }

		int pos = position - 1;

		// if (unuseEsixt) {
		//
		// pos = pos - 1;
		// }

		if (pos > -1
				&& eventArray.get(position).getEventTime() == eventArray.get(
						pos).getEventTime()
				&& eventArray.get(position).getEventTime() > 0) {

			convertView.get(position).setBackgroundColor(lastColor);

		} else {
			if (position == 0) {
				convertView.get(position).setBackgroundColor(Color.WHITE);
				lastColor = Color.WHITE;
			} else {

				if (lastColor == Color.LTGRAY) {
					convertView.get(position).setBackgroundColor(Color.WHITE);
					lastColor = Color.WHITE;
				} else if (lastColor == Color.WHITE) {
					convertView.get(position).setBackgroundColor(Color.LTGRAY);
					lastColor = Color.LTGRAY;
				}
			}

		}

	}

	public void jumpColor(int position, ArrayList<EventRow> eventArray,
			int color) {

		if ((position + 1) >= eventArray.size()) {
			return;
		}

		same_green = new ArrayList<Integer>();

		if (eventArray.get(position).getEventTime() > 0) {

			if (position == eventArray.size() - 1) {
				eventArray.get(position).setBackgroundColor(color);
				same_green.add(position);
				return;
			}

			int pos = position;

			while ((pos + 1) < eventArray.size()
					&& eventArray.get(++pos).getEventTime() == eventArray.get(
							position).getEventTime()) {
				eventArray.get(pos).setBackgroundColor(color);
				same_green.add(pos);
			}
		}

	}

	private int lastReceivedIndex = -1;
	private int IndexRepeatedTimes = 0;
	private int ScriptCallsAfterStop = 0;

	private int last_received_index = -1;

	private boolean dialog_wrong_script_call_check = true;

	private long currentTime = 0;

	protected boolean IsAutoScrollEnable = true;
	private Button btn_AutoScroll;

	protected boolean isDirectJumped = false;
	public int firstStartIndex = 0;
	public long firstStartTime = 0;

	public boolean is18R2Play = false;

	public boolean is18R2PlayAfterJump = false;

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			// if (appClass.isDemoMode) {
			// return;
			// }

			/*
			 * Check if user stop the scripts but 18R2 do not acknowledge it.
			 */
			if (IsStopPressed) {
				ScriptCallsAfterStop++;
			}

			// checks if the remote is in test mode but playing a script and
			// stops if it is

			if (intent.hasExtra("stopscripts")) {
				// Toast.makeText(context, "stoping...", Toast.LENGTH_SHORT)
				// .show();
				if (state != ScriptState.STOPPED) {
					stopScript(false);
					UpdateUIAfterStoppingScripts();
				}

			} else {
				try {

					long elapsedTime = 0;
					int scriptIndex = 0;
					int eventIndex = 0;
					int index = 0;

					index = intent.getExtras().getInt("index");
					elapsedTime = intent.getExtras().getLong("elapsedTime");
					scriptIndex = intent.getExtras().getInt("scriptIndex");
					eventIndex = intent.getExtras().getInt("eventIndex");

					String scriptName = scripts.get(scriptIndex).scriptName;

					int spinnerIndex = scriptSpinner.getSelectedItemPosition();
					if (scriptIndex != spinnerIndex) {
						// Toast.makeText(
						// context,
						// "Data=" + elapsedTime + "," + eventIndex + ","
						// + index + "show control check="
						// + MainActivity.showControlCheck,
						// Toast.LENGTH_SHORT).show();

						// check dialog show
						if (dialog_wrong_script_call_check) {

							if (MainActivity.showControlCheck) {

								// Toast.makeText(context, "Wrong Script!",
								// Toast.LENGTH_SHORT).show();

								showDialog_WrongScript(scriptIndex, scriptName);
							} else {
								onWrongScriptListener
										.onWrongScriptContinue(scriptIndex);
							}
						} else {
							return;
						}

						// scriptSpinner.setSelection(scriptIndex);
					}

					/*
					 * If last data and current data is same, it means 18R2 has
					 * paused scripts data if we receive same index 3 times then
					 * we are assuming that scripts are paused.
					 */
					if (lastReceivedIndex == index) {
						IndexRepeatedTimes++;
						if (IndexRepeatedTimes == 3) {
							setScriptState(ScriptState.PAUSED);
						}

					} else {
						// isPauseFiring = false;
						IndexRepeatedTimes = 0;
						lastReceivedIndex = index;
					}

					ScriptPingTag tempPingTag = new ScriptPingTag(index,
							elapsedTime, scriptIndex, eventIndex);
					pingTag = tempPingTag;

					currentEventIndex = pingTag.eventIndex - 1;

					// Fix the script state to match the remote
					if (previousElapsedTime[0] != 0
							&& previousElapsedTime[1] != 0) {
						// If three time values in a row are the same
						if ((previousElapsedTime[0] == previousElapsedTime[1] && previousElapsedTime[1] == pingTag.elapsedTime)
								|| previousElapsedTime[1] == (pingTag.elapsedTime - 1)) {
							setScriptState(ScriptState.PAUSED);
						} else {

							setScriptState(ScriptState.PLAYING);
						}
					}

					previousElapsedTime[1] = previousElapsedTime[0];
					previousElapsedTime[0] = pingTag.elapsedTime;

					if (last_received_index == -1) {
						last_received_index = pingTag.eventIndex;
					}

					currentTime = pingTag.elapsedTime;

					highlightNextEvent(pingTag.eventIndex);

					if (pingTag.scriptIndex == spinnerPosition) {
						long hr = TimeUnit.MILLISECONDS
								.toHours((pingTag.elapsedTime * 100l));

						if (hr > 24) {
							int i = 0;
							i++;

							setChronometerTimes(0, pingTag.eventIndex);
						} else {

							setChronometerTimes(pingTag.elapsedTime,
									pingTag.eventIndex);
						}

						if (last_received_index != pingTag.eventIndex) {

							// this pause condition for to break to red color
							// event with pause

							// make two variables one for jumpTo and one for
							// play
							// if non of above variable are true but 18r2 is
							// sending resume or play is pause to false

							if (!is18R2Play) {// || !isJumping

								isPauseAter = false;
								isPause = false;

								isPauseFiring = false;
							} else {

							}

							if (!isPauseFiring) {

								highlightLastFiredEvent(last_received_index,
										pingTag.eventIndex);

							}

							last_received_index = pingTag.eventIndex;

							if (IsAutoScrollEnable)
								AutoScrollScrollView(pingTag.eventIndex - 1);

						}

					}

					previousEventIndex = pingTag.eventIndex - 1;

					if ((pingTag.eventIndex == scripts.get(spinnerPosition).numEvents)) {
						stopScript(false);
						UpdateUIAfterStoppingScripts();
					}
				} catch (Exception e) {
					// TODO: handle exception
					int i = 0;
					i++;
				}
			}

		}

	};

	public void showDialog_WrongScript(final int scriptIndex, String scriptName) {

		FragmentTransaction ft = (getActivity()).getSupportFragmentManager()
				.beginTransaction();

		Fragment prev = (getActivity()).getSupportFragmentManager()
				.findFragmentByTag("dialog");

		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		DialogFragment newFragment = DialogWrongScript.newInstance(1,
				scriptName, scriptIndex, preScriptName);
		newFragment.show(ft, "dialog");

		dialog_wrong_script_call_check = false;

	}

	OnWrongScriptListener onWrongScriptListener = new OnWrongScriptListener() {

		@Override
		public void onWrongScriptStop() {

			myCobra.stopScript();

			UpdateUIAfterStoppingScripts();

			dialog_wrong_script_call_check = true;

		}

		@Override
		public void onWrongScriptContinue(int scriptIndex) {
			scriptSpinner.setSelection(scriptIndex);

			dialog_wrong_script_call_check = true;

		}

		@Override
		public void onWrongScriptClose() {
			// TODO Auto-generated method stub

			dialog_wrong_script_call_check = true;
		}

	};
	int old_y = 0;
	int new_y = 0;

	private void AutoScrollScrollView(int eventIndex) {
		// TODO Auto-generated method stub
		if (eventIndex > 0) {
			old_y = (int) eventArrayAdapter.get(eventIndex - 1).getY();
		}

		new_y = (int) eventArrayAdapter.get(eventIndex).getY();
		scrollView.post(new Runnable() {
			public void run() {
				scrollView.scrollTo(old_y, new_y);
			}
		});
	}

	/**
	 * Converts tenths of a second to a CharSequence in the format HH:MM:SS.S
	 * 
	 * @param is
	 *            the number of tenths of a second
	 * @return the CharSequence in the format HH:MM:SS.S
	 */
	public static CharSequence tenthsToTimeFormat(long t) {
		t = t * 100l;
		long hr = TimeUnit.MILLISECONDS.toHours(t);
		long min = TimeUnit.MILLISECONDS.toMinutes(t
				- TimeUnit.HOURS.toMillis(hr));
		long sec = TimeUnit.MILLISECONDS.toSeconds(t
				- TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min));
		long ms = TimeUnit.MILLISECONDS.toMillis(t
				- TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min)
				- TimeUnit.SECONDS.toMillis(sec));
		long tenths = ms / 100l;
		return String.format("%02d:%02d:%02d.%01d", hr, min, sec, tenths);
	}

	private void UpdateArrayAdapter() {
		eventArrayAdapter.clear();
		eventArrayAdapter = new ArrayList<EventRow>();
		for (int i = 0; i < eventRows.size(); i++) {
			eventArrayAdapter.add(getView(i));

			// if((eventArrayAdapter.get(i).getTag() + "").equals("same")){
			// ((Button) eventArrayAdapter.get(i).co//
			// findViewById(R.id.jumpToEventButton))
			// }
		}

	}

	private void NotifyDataSetChanged() {
		if (linear_eventList != null) {
			linear_eventList.removeAllViews();
			for (int i = 0; i < eventArrayAdapter.size(); i++) {
				linear_eventList.addView(eventArrayAdapter.get(i));
			}
		}
	}

	private Boolean LoadScriptsInParts(int start, int end) {
		if (linear_eventList != null) {
			if (end <= eventArrayAdapter.size()) {
				for (int i = start; i < end; i++) {
					linear_eventList.addView(eventArrayAdapter.get(i));
				}
				return true;
			} else {
				end = eventArrayAdapter.size();
				for (int i = start; i < end; i++) {
					linear_eventList.addView(eventArrayAdapter.get(i));
				}
				return false;
			}
		}
		return false;
	}

	/**
	 * Adds a new EventRow object to the eventRows ArrayList
	 */
	public void addEventRow() {
		EventRow row = new EventRow(getActivity());
		row.setBackground(getActivity().getResources().getDrawable(
				R.drawable.modulerow_signal_back_red));
		eventRows.add(row);
	}

	Boolean IsStopPressed = false;

	public boolean isPause = false;

	public boolean isPauseAter = false;

	public boolean isPauseFiring = false;

	private boolean red_index = false;

	OnClickListener playPauseButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {

			// if (appClass.isDemoMode) {// DEMO
			// showDialogBuy();
			// return;
			// }

			isSetchromoTime = false;

			if (appClass.Remote_Mode == Cobra.MODE_ARMED) {

				if (firstStartIndex > 0) {

					myCobra.startScript(scripts.get(spinnerPosition).scriptID,
							false);

					myCobra.jumpToEvent(events.get(spinnerPosition).get(
							firstStartIndex).eventIndex);

					firstStartIndex = 0;

					// if (red_index) {
					// red_index = false;
					// new Handler().postDelayed(new Runnable() {
					// @Override
					// public void run() {
					// eventArrayAdapter.get(last_green_index)
					// .setBackgroundColor(Color.RED);
					// }
					// }, 100);
					// }

					return;
				}

				switch (state) {
				case STOPPED:

					if (scripts == null || scripts.size() == 0) {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								getActivity());
						builder.setTitle("Scripts Error!");
						builder.setMessage("No scripts are loaded. Please load script onto remote.");
						builder.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// appClass.IsAppForcedClosed=false;
										// finish();
									}
								});
						builder.show();
						return;
					}

					// already jumped with time

					// if (firstStartTime != 0) {
					//
					// myCobra.startScript(scripts.get(spinnerPosition).scriptID,
					// false);
					//
					//
					// myCobra.jumpToTime(firstStartTime);
					//
					//
					//
					// firstStartTime = 0;
					// return;
					// }

					myCobra.startScript(scripts.get(spinnerPosition).scriptID,
							false);
					Toast.makeText(v.getContext(), "Start", Toast.LENGTH_SHORT)
							.show();

					isPause = false;

					isPauseFiring = false;

					firstStartIndex = 0;
					break;

				case PAUSED:

				// if (firstStartIndex > 0) {
				//
				// Toast.makeText(v.getContext(), "firt jump start!",
				// Toast.LENGTH_SHORT).show();
				//
				// // myCobra.jumpToEvent(events.get(spinnerPosition).get(
				// // firstStartIndex).eventIndex);
				//
				// // myCobra.resumeScript();
				//
				// myCobra.startScript(
				// scripts.get(firstStartIndex).scriptID, false);
				//
				// } else
				{

					is18R2Play = true;

					myCobra.resumeScript();

					is18R2PlayAfterJump = true;

					isPauseAter = false;

					isPause = false;

					isPauseFiring = false;

					// rrr
					// if (red_index) {
					// red_index = false;
					// eventArrayAdapter.get(last_green_index)
					// .setBackgroundColor(Color.RED);
					// }

					Toast.makeText(v.getContext(), "Resume", Toast.LENGTH_SHORT)
							.show();
				}

					firstStartIndex = 0;
					break;

				case PLAYING:

					isPause = false;

					isPauseFiring = true;

					// setScriptState(ScriptState.PAUSED);
					myCobra.pauseScript();

					Toast.makeText(v.getContext(), "Pause", Toast.LENGTH_SHORT)
							.show();
					break;

				default:
					break;
				}
			} else {
				Toast.makeText(v.getContext(), "Remote is in TEST mode",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	OnClickListener stopButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {

			// if (appClass.isDemoMode) {// DEMO
			// showDialogBuy();
			// return;
			// }

			if (state == ScriptState.STOPPED) {
				Toast.makeText(getActivity(), "Already Stopped",
						Toast.LENGTH_SHORT).show();
				return;
			}

			final AlertDialog.Builder stopPopup = new AlertDialog.Builder(
					getActivity());
			stopPopup.setTitle("Stop");
			stopPopup.setMessage("Are you sure you want to stop the script?");
			// Add the buttons
			stopPopup.setPositiveButton("Stop",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// User clicked Stop button
							stopScript(true);
							IsStopPressed = true;
							ScriptCallsAfterStop = 0;
							new Handler().postDelayed(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									if (ScriptCallsAfterStop < 3) {

										UpdateUIAfterStoppingScripts();
									}
								}
							}, 1500);
						}
					});
			stopPopup.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
						}
					});
			final AlertDialog dialog3 = stopPopup.create();
			dialog3.show();

		}
	};

	private long totalTime = 0;

	private String preScriptName;
	AdapterView.OnItemSelectedListener scriptSpinnerItemListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {

			// here set view content visibility

			// if (appClass.isDemoMode) {// DEMO
			//
			// Toast.makeText(getActivity(), "Is demo mode!", 500).show();
			// return;
			// }

			try {

				IsStepScript = false;
				spinnerPosition = pos;
				// eventList.clearChoices();
				if (linear_eventList != null)
					linear_eventList.removeAllViews();

				timeElapsed.setText("00:00:00.0");
				setChronometerTimes(0, 0);
				last_received_index = -1;
				scriptChTextView
						.setText(scripts.get(spinnerPosition).triggerChannel
								+ "");
				scriptButTextView.setText(getTriggerButton1(spinnerPosition));
				scriptDMTextView.setText(getTriggerButton2(spinnerPosition));
				scriptRCTextView
						.setText(scripts.get(spinnerPosition).endChannel + "");

				ScriptIndexTag script = scripts.get(spinnerPosition);
				int numEvents = script.numEvents;

				preScriptName = scripts.get(spinnerPosition).scriptName;

				SparseArray<EventDataTag> event = events.get(spinnerPosition);
				EventDataTag eventTag = event.get(numEvents - 1);
				// EventDataTag eventTag=event.get(1300-1);
				long timeIndex = eventTag.timeIndex;

				if (timeIndex < STEP_CONST) {
					// if (events.get(spinnerPosition).get(
					// (scripts.get(spinnerPosition).numEvents) - 1).timeIndex <
					// STEP_CONST) {
					scriptDurationTenths = events.get(spinnerPosition).get(
							(scripts.get(spinnerPosition).numEvents) - 1).timeIndex;
				} else {
					// This is a step
					scriptDurationTenths = events.get(spinnerPosition).get(
							(scripts.get(spinnerPosition).numEvents) - 1).timeIndex
							- STEP_CONST;
				}
				timeRemaining
						.setText(tenthsToTimeFormat(scripts.get(pos).scriptLength));

				scriptLengthTextView.setText(tenthsToTimeFormat(scripts
						.get(pos).scriptLength));

				totalTime = scripts.get(pos).scriptLength;

				setAllNumberPickerMaxMin(events.get(spinnerPosition).get(
						(scripts.get(spinnerPosition).numEvents) - 1).timeIndex);

				npHours.setMinValue(0);

				if (scripts.get(spinnerPosition).audioFilename.isEmpty()) {
					scriptAudioFileTextView.setText("None");

				} else {

					String origonalText = scripts.get(spinnerPosition).audioFilename;

					String text = "";
					if (origonalText != "" && origonalText != null
							&& origonalText.length() > 0) {
						for (int i = 1; i < origonalText.length(); i++) {

							if (origonalText.charAt(i) > 31
									|| origonalText.charAt(i) < 178 && i != 0) {

								text = text + origonalText.charAt(i);
							}
						}

						if (text == "-1" || text.contains("-1") || text == "") {
							scriptAudioFileTextView.setText("None");

						} else {
							scriptAudioFileTextView.setText(text);

						}
					} else {
						scriptAudioFileTextView.setText("None");
					}
				}

				eventArrayAdapter.clear();
				eventRows.clear();
				// eventArrayAdapter.notifyDataSetChanged();
				linear_eventList.removeAllViews();
				for (int i = 0; i < scripts.get(spinnerPosition).numEvents; i++) {
					addEventRow();
				}
				new ListLoader().execute();
				// UpdateArrayAdapter();
				// NotifyDataSetChanged();
				// eventArrayAdapter.notifyDataSetChanged();
			} catch (Exception e) {
				Toast.makeText(getActivity(), "crashed : in script spinner",
						Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};

	private long last_elapsed_time = 0;

	private int last_event_index_for_which_incremented = -1;

	/**
	 * Converts the 32 bit number to a comma separated string of the cues that
	 * will be fired
	 * 
	 * @param cueList
	 *            is the 32 bit number which determines which cues are going to
	 *            be fired by setting the corresponding bit
	 * @return the cue string
	 */
	private static String getCueString(long cueList) {
		if (cueList == 0l) {
			return "XX";
		}
		int bit = 1;
		String cues = "";
		// checks bit by bit, from the LSB to the MSB, if any bit is set; if so,
		// it adds the corresponding cue to the string
		for (int j = 1; j < 19; j++) {
			if ((cueList & bit) == bit) {
				if (j < 10) {
					cues += "0";
				}
				cues += j + ",";
			}
			bit = bit * 2;
		}
		if (cues.length() > 0) {
			cues = cues.substring(0, cues.length() - 1);
		}
		return cues;
	}

	private static int getNoOfCuesInEvent(long cueList) {
		if (cueList == 0l) {
			return 1;
		}
		int bit = 1;
		int noOfCues = 0;
		// checks bit by bit, from the LSB to the MSB, if any bit is set; if so,
		// it adds the corresponding cue to the string
		for (int j = 1; j < 19; j++) {
			if ((cueList & bit) == bit) {
				noOfCues++;
			}
			bit = bit * 2;
		}
		return noOfCues;
	}

	private static ArrayList<String> getCuesArray(long cues) {

		ArrayList<String> cuesList = new ArrayList<String>();
		if (cues == 0l) {
			cuesList.add("" + 1);
			return cuesList;
		}
		int bit = 1;

		// checks bit by bit, from the LSB to the MSB, if any bit is set; if so,
		// it adds the corresponding cue to the string
		for (int j = 1; j < 19; j++) {
			if ((cues & bit) == bit) {
				cuesList.add("" + j);
			}
			bit = bit * 2;
		}

		return cuesList;
	}

	/**
	 * Gets the script list.
	 * 
	 * @return a string ArrayList with the script information
	 */
	public ArrayList<String> getScriptList() {
		ArrayList<String> list = new ArrayList<String>();
		if (scripts != null && scripts.size() > 0) {
			for (int i = 0; i < scripts.size(); i++) {
				// list.add(scripts.get(i).scriptName+" - Trig. Ch: " +
				// scripts.get(i).triggerChannel + " - Trig. Cue: " +
				// getTriggerButton1(i));

				// Show only script name
				list.add("Script: " + scripts.get(i).scriptName + " ("
						+ scripts.get(i).numEvents + " Events)");
			}
		} else {
			list.add("No Scripts");
		}
		return list;
	}

	private class ScriptInfoContainer {
		private String scriptName;
		private int numEvents;

		public String getScriptName() {
			return scriptName;
		}

		public void setScriptName(String scriptName) {
			this.scriptName = scriptName;
		}

		public int getNumEvents() {
			return numEvents;
		}

		public void setNumEvents(int numEvents) {
			this.numEvents = numEvents;
		}

	}

	/**
	 * Gets the trigger button1 string.
	 * 
	 * @param i
	 *            is the script index
	 * @return the trigger button1 string
	 */
	public String getTriggerButton1(int i) {
		// triggerButton2 index 18 is the "AUTO FIRE" button on the remote
		if (scripts.get(i).triggerButton1 == 18) {
			return "AUTO FIRE";
		}
		// triggerButton2 index 18 is the "STEP" button on the remote
		else if (scripts.get(i).triggerButton1 == 19) {
			return "STEP";
		} else {
			// triggerButton2 index 0 is the "1" button on the remote
			return (scripts.get(i).triggerButton1 + 1) + "";
		}
	}

	/**
	 * Gets the trigger button2 string.
	 * 
	 * @param i
	 *            is the script index
	 * @return the trigger button2 string
	 */
	public String getTriggerButton2(int i) {
		// if the element returned by the scripts get() function does not have a
		// triggerButton2, it returns -1
		if (scripts.get(i).triggerButton2 == 255) {
			return "None";
		}
		// triggerButton2 index 18 is the "AUTO FIRE" button on the remote
		else if (scripts.get(i).triggerButton2 == 18) {
			return "AUTO FIRE";
		}
		// triggerButton2 index 18 is the "STEP" button on the remote
		else if (scripts.get(i).triggerButton2 == 19) {
			return "STEP";
		} else {
			// triggerButton2 index 0 is the "1" button on the remote
			return (scripts.get(i).triggerButton2 + 1) + "";
		}
	}

	/**
	 * Sets all of the number picker max and min values.
	 * 
	 * @param t
	 *            is the time index of the last event.
	 */
	public static void setAllNumberPickerMaxMin(long t) {
		if (t > STEP_CONST) {
			t -= STEP_CONST;
		}
		t = t * 100l;
		long hr = TimeUnit.MILLISECONDS.toHours(t);
		long min = TimeUnit.MILLISECONDS.toMinutes(t
				- TimeUnit.HOURS.toMillis(hr));
		long sec = TimeUnit.MILLISECONDS.toSeconds(t
				- TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min));
		long ms = TimeUnit.MILLISECONDS.toMillis(t
				- TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min)
				- TimeUnit.SECONDS.toMillis(sec));
		long tenths = ms / 100l;

		if (hr > 0l) {
			npHours.setMaxValue((int) hr);
			npMinutes.setMaxValue(59);
			npSeconds.setMaxValue(59);
			npTenths.setMaxValue(9);
		} else if (min > 0l) {
			npHours.setMaxValue(0);
			npMinutes.setMaxValue((int) min);
			npSeconds.setMaxValue(59);
			npTenths.setMaxValue(9);
		} else if (sec > 0l) {
			npHours.setMaxValue(0);
			npMinutes.setMaxValue(0);
			npSeconds.setMaxValue((int) sec);
			npTenths.setMaxValue(9);
		} else {
			npHours.setMaxValue(0);
			npMinutes.setMaxValue(0);
			npSeconds.setMaxValue(0);
			if (tenths > 0)
				npTenths.setMaxValue((int) tenths);
			else
				tenths = 0;
		}
		npHours.setMinValue(0);
		npMinutes.setMinValue(0);
		npSeconds.setMinValue(0);
		npTenths.setMinValue(0);
	}

	/**
	 * Highlight a single event green.
	 * 
	 * @param eventIndex
	 *            event number to highlight
	 */

	public int last_green_index = -1;

	private int last_red_index = -1;

	private void highlightNextEvent(int eventIndex) {
		try {
			if (eventArrayAdapter.size() > eventIndex) {

				long LasteventTime = eventArrayAdapter.get(eventIndex)
						.getEventTime();
				long CurrentEventTime = 0;
				int currentIndex = eventIndex;
				Boolean keepRunning = true;

				if (nextEventIndex != eventIndex) {
					nextEventIndex = eventIndex;

					while (keepRunning) {
						CurrentEventTime = eventArrayAdapter.get(currentIndex)
								.getEventTime();
						long timeindex = eventArrayAdapter.get(currentIndex)
								.getEventTime();

						if (LasteventTime == CurrentEventTime) {

							eventArrayAdapter.get(currentIndex)
									.setBackgroundColor(Color.GREEN);

							last_green_index = currentIndex;
							currentIndex++;

							if (LasteventTime == 0)
								keepRunning = false;
							if (eventArrayAdapter.size() == currentIndex)
								keepRunning = false;

						} else {
							keepRunning = false;
						}
					}
					// eventArrayAdapter.notifyDataSetChanged();
				}
			}
		} catch (Exception e) {
			int i = 0;
			i++;
		}
	}

	/**
	 * Highlight a single event red.
	 * 
	 * @param eventIndex
	 *            event number to highlight
	 * @param currentEventIndex2
	 */

	private ArrayList<Long> firedTimeList = new ArrayList<Long>();

	private void highlightLastFiredEvent(int eventIndex, int currentEventToFire) {
		if (eventIndex >= 0 && eventIndex < eventArrayAdapter.size()) {

			if (previousEventToChangeInWhite == -1) {
				previousEventToChangeInWhite = eventIndex;
			}
			if (previousEventToChangeInWhite != eventIndex) {
				clearLastFiredEvent(previousEventToChangeInWhite,
						currentEventToFire);
				previousEventToChangeInWhite = eventIndex;
			}

			try {
				long LasteventTime = eventArrayAdapter.get(eventIndex)
						.getEventTime();
				long CurrentEventTime = 0;
				int currentIndex = eventIndex;
				Boolean keepRunning = true;

				if (eventIndex >= 0) {

					while (keepRunning) {
						CurrentEventTime = eventArrayAdapter.get(currentIndex)
								.getEventTime();
						if (LasteventTime == CurrentEventTime) {
							if (JUST_JUMPED == true) {
							} else

								// This log is important for color red
								appClass.setPlayScriptLog(eventIndex + " != "
										+ currentEventToFire);

							eventArrayAdapter.get(currentIndex)
									.setBackgroundColor(Color.RED);

							last_red_index = currentIndex;
							currentIndex++;
							if (LasteventTime == 0) {
								keepRunning = false;
							}
							if (eventArrayAdapter.size() == currentIndex)
								keepRunning = false;

						} else {
							keepRunning = false;
						}
					}

				}
			} catch (Exception e) {
				Toast.makeText(getActivity(), "Crashed : " + e.getMessage(),
						Toast.LENGTH_SHORT).show();
				int i = 0;
				i++;

				appClass.setPlayScriptLog("Crashed from where color.red!");

			}

			// / giving gray color to all previous events
			if (!isPauseFiring) {
				int a = 0;
				for (a = last_received_index - 1; a >= 0; a--) {

					if (JUST_JUMPED == true) {

						eventArrayAdapter.get(a).setBackgroundColor(Color.GRAY);
						eventArrayAdapter.get(a).setTag("fired");
						if (a - 1 >= 0
								&& (eventArrayAdapter.get(a - 1).getTag() + "")
										.equals("fired"))
							break;
						continue;
					}
					if ((eventArrayAdapter.get(a).getTag() + "")
							.equals("fired"))
						break;

					eventArrayAdapter.get(a).setBackgroundColor(Color.GRAY);

					eventArrayAdapter.get(a).setTag("fired");

					firedTimeList.add(eventArrayAdapter.get(a).getEventTime());

				}
				JUST_JUMPED = false;
			}
			// /

		}
	}

	private void clearLastFiredEvent(int eventIndex, int currentEventToFire) {

		try {
			long LasteventTime = eventArrayAdapter.get(eventIndex)
					.getEventTime();
			long CurrentEventTime = 0;
			int currentIndex = eventIndex;
			boolean keepRunning = true;

			if (eventIndex >= 0) {

				while (keepRunning) {
					CurrentEventTime = eventArrayAdapter.get(currentIndex)
							.getEventTime();
					if (LasteventTime == CurrentEventTime
							&& currentIndex != currentEventToFire
							&& currentIndex != last_red_index) {

						doColorList(currentIndex, eventArrayAdapter,
								eventArrayAdapter);

						currentIndex++;

						if (eventArrayAdapter.size() == currentIndex)
							keepRunning = false;

					} else {
						keepRunning = false;
					}
				}

				// eventArrayAdapter.notifyDataSetChanged();
			}
		} catch (Exception e) {
			int i = 0;
			i++;
		}
	}

	ArrayList<Integer> same_green;

	private void clearLastFiredEvent_Onjump(int position,
			ArrayList<EventRow> eventArray, int last_green, int last_red) {

		if (position > -1) {

			int start = last_red + 1;
			for (int i = start; i < eventArray.size(); i++) {
				int indexNotToClear = -1;
				if (same_green != null && same_green.size() > 0) {
					for (int j = 0; j < same_green.size(); j++) {
						if (i == same_green.get(j)) {
							indexNotToClear = i;
							break;
						}
					}
				}
				if (indexNotToClear != -1) {
					continue;
				}

				doColorList(i, eventArray, eventArray);

			}
		}

	}

	/**
	 * Remove the red and green highlights on the event list for the last event
	 * fired and the next event fired.
	 */
	private void clearEventHighlights() {
		// eventArrayAdapter.notifyDataSetChanged();
		scrollView.post(new Runnable() {
			public void run() {
				scrollView.scrollTo(0, 0);
			}
		});
		lastFiredEventIndex = -1;
		nextEventIndex = -1;
		last_received_index = -1;
		if (eventArrayAdapter != null) {

			for (int i = 0; i < eventArrayAdapter.size(); i++) {

				doColorList(i, eventArrayAdapter, eventArrayAdapter);

				eventArrayAdapter.get(i).setTag("");

			}

		}
	}

	/**
	 * Set the time of the chronometer views
	 * 
	 * @param elapsedTime
	 *            is the elapsed time of the script
	 * @param eventIndex
	 */
	private void setChronometerTimes(long elapsedTime, int eventIndex) {

		if (!isSetchromoTime) {
			long eventTime = 0;
			if ((eventIndex) < eventArrayAdapter.size()) {

				eventTime = eventArrayAdapter.get(eventIndex).getEventTime();

				if (eventIndex - 1 >= 0) {
					long checkForScriptTime = eventArrayAdapter.get(
							eventIndex - 1).getEventTime();

					if (checkForScriptTime == 0) {
						// last_elapsed_time = elapsedTime;
					}

				}

				if (eventIndex - 1 >= 0) {
					long checkForScriptTime = eventArrayAdapter.get(
							eventIndex - 1).getEventTime();

					if (checkForScriptTime == 0
							&& eventArrayAdapter.get(eventIndex).getEventTime() > 0
							&& last_event_index_for_which_incremented != eventIndex) {
						// last_current_time += elapsedTime;
						last_event_index_for_which_incremented = eventIndex;
						last_elapsed_time = elapsedTime;
					}
				}

				if (eventTime != 0)
					nextEventTime.setText(tenthsToTimeFormat(eventTime
							- (elapsedTime - last_elapsed_time)));
				else
					nextEventTime.setText(tenthsToTimeFormat(0));
			}
			timeElapsed.setText(tenthsToTimeFormat(elapsedTime));

			timeRemaining
					.setText(tenthsToTimeFormat(totalTime - (elapsedTime)));

		}
	}

	private boolean isSetchromoTime = false;

	private void setChronometerTimes_WhileJumped(long elapsedTime,
			int eventIndex) {

		isSetchromoTime = true;

		if (eventIndex == 0) {

			long eventTime = 0;

			eventTime = eventArrayAdapter.get(eventIndex + 1).getEventTime();

			if (eventTime != 0)
				nextEventTime.setText(tenthsToTimeFormat(eventTime
						- (elapsedTime - last_elapsed_time)));
			else
				nextEventTime.setText(tenthsToTimeFormat(0));

			timeElapsed.setText(tenthsToTimeFormat(elapsedTime));

			timeRemaining
					.setText(tenthsToTimeFormat(totalTime - (elapsedTime)));

			return;
		}

		long eventTime = 0;
		if ((eventIndex + 1) < eventArrayAdapter.size()) {// eventIndex

			eventTime = eventArrayAdapter.get(eventIndex + 1).getEventTime();

			if (eventIndex - 1 >= 0) {
				long checkForScriptTime = eventArrayAdapter.get(eventIndex - 1)
						.getEventTime();

				if (checkForScriptTime == 0) {
					// last_elapsed_time = elapsedTime;
				}

			}

			if (eventIndex - 1 >= 0) {
				long checkForScriptTime = eventArrayAdapter.get(eventIndex - 1)
						.getEventTime();

				if (checkForScriptTime == 0
						&& eventArrayAdapter.get(eventIndex).getEventTime() > 0
						&& last_event_index_for_which_incremented != eventIndex) {
					// last_current_time += elapsedTime;
					last_event_index_for_which_incremented = eventIndex;
					last_elapsed_time = elapsedTime;
				}
			}

			if (eventTime != 0)
				nextEventTime.setText(tenthsToTimeFormat(eventTime
						- (elapsedTime - last_elapsed_time)));
			else
				nextEventTime.setText(tenthsToTimeFormat(0));
		}
		timeElapsed.setText(tenthsToTimeFormat(elapsedTime));

		timeRemaining.setText(tenthsToTimeFormat(totalTime - (elapsedTime)));

	}

	// When scripts stops or starts or pauses this function changes UI
	private void setScriptState(ScriptState s) {
		state = s;
		switch (state) {
		case PAUSED:
			scriptStateTextView.setText("Paused");
			playPauseButton.setImageResource(R.drawable.play_button);
			scriptStateTextView.setTextColor(getResources().getColor(
					R.color.black_showcontrols));

			scriptStateTextView
					.setBackgroundResource(R.color.light_yellow_showcontrols);
			timeElapsed
					.setBackgroundResource(R.color.light_yellow_showcontrols);
			timeElapsed.setTextColor(getResources().getColor(
					R.color.black_showcontrols));

			nextEventTime
					.setBackgroundResource(R.color.light_yellow_showcontrols);
			nextEventTime.setTextColor(getResources().getColor(
					R.color.black_showcontrols));

			if (scriptSpinner.getSelectedView() != null)
				scriptSpinner.getSelectedView().setEnabled(false);
			scriptSpinner.setEnabled(false);

			break;
		case PLAYING:
			appClass.IsScriptPlaying = true;
			scriptStateTextView.setText("Playing");
			playPauseButton.setImageResource(R.drawable.pause_button);
			scriptStateTextView.setTextColor(getResources().getColor(
					R.color.black_showcontrols));
			scriptStateTextView
					.setBackgroundResource(R.color.light_green_showcontrols); // Scott
			// Green
			timeElapsed.setBackgroundResource(R.color.light_green_showcontrols);
			timeElapsed.setTextColor(getResources().getColor(
					R.color.black_showcontrols));

			nextEventTime
					.setBackgroundResource(R.color.light_green_showcontrols);
			nextEventTime.setTextColor(getResources().getColor(
					R.color.black_showcontrols));

			if (scriptSpinner.getSelectedView() != null)
				scriptSpinner.getSelectedView().setEnabled(false);
			scriptSpinner.setEnabled(false);
			break;
		case STOPPED:
		default:
			scriptStateTextView.setText("Stopped");
			playPauseButton.setImageResource(R.drawable.play_button);
			scriptStateTextView.setTextColor(getResources().getColor(
					R.color.white_showcontrols));
			scriptStateTextView
					.setBackgroundResource(R.color.light_red_showcontrols);
			timeElapsed.setBackgroundResource(R.color.light_red_showcontrols);
			timeElapsed.setTextColor(getResources().getColor(
					R.color.white_showcontrols));

			nextEventTime.setBackgroundResource(R.color.light_red_showcontrols);
			nextEventTime.setTextColor(getResources().getColor(
					R.color.white_showcontrols));

			if (scriptSpinner.getSelectedView() != null)
				scriptSpinner.getSelectedView().setEnabled(true);
			scriptSpinner.setEnabled(true);
			break;
		}
	}

	/**
	 * Set all UI to their proper values If sendStopCommand is true send a
	 * command to the remote to stop the script
	 */
	public void stopScript(boolean sendStopCommand) {
		if (sendStopCommand) {
			myCobra.stopScript();
			Toast.makeText(getActivity(), "Stop", Toast.LENGTH_SHORT).show();
		}
		// UpdateUIAfterStoppingScripts();
	}

	private void UpdateUIAfterStoppingScripts() {

		isSetchromoTime = false;

		last_green_index = -1;

		last_red_index = -1;

		firstStartIndex = 0;

		isPauseFiring = false;

		is18R2Play = false;

		last_received_index = -1;

		isDirectJumped = false;

		firedTimeList = new ArrayList<Long>();

		last_elapsed_time = 0;
		last_event_index_for_which_incremented = -1;
		// firedEvent.clear();
		setScriptState(ScriptState.STOPPED);
		previousElapsedTime[0] = 0;
		previousElapsedTime[1] = 0;
		setChronometerTimes(0, 0);
		// dialog_wrong_script_call_check = true;
		clearEventHighlights();
		// eventList.clearChoices();
		// eventList.smoothScrollToPositionFromTop(0, 175, 50);
		// clearEventHighlights();
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;

		for (int i = 0; i < listAdapter.getCount(); i++) {
			try {
				View listItem = listAdapter.getView(i, null, listView);
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			} catch (Exception ex) {
			}
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);

	}

	// ProgressDialog pd;

	private class ListLoader extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			// NotifyDataSetChanged();

			if (eventArrayAdapter != null && eventArrayAdapter.size() > 0)
				nextEventTime.setText(tenthsToTimeFormat(eventArrayAdapter.get(
						0).getEventTime()));

			linear_eventList.setVisibility(View.VISIBLE);
			// pd.dismiss();
			new ScriptListViewLoader(0, 30, true).execute();

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			linear_eventList.setVisibility(View.GONE);

			// pd = new ProgressDialog(getActivity());
			// pd.setTitle("Please wait");
			// pd.setMessage("Retrieving script data...");
			// pd.show();

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					UpdateArrayAdapter();
				}
			});

			return null;
		}

	}

	private class ScriptListViewLoader extends AsyncTask<Void, Void, Void> {

		// ProgressDialog pd;
		int start = 0;
		int end = 10;
		Boolean status = true;

		public ScriptListViewLoader(int start, int end, Boolean status) {
			this.start = start;
			this.end = end;
			this.status = status;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (this.status) {
				new ScriptListViewLoader(this.end, (this.end + 30), this.status)
						.execute();
			}
			// linear_eventList.setVisibility(View.VISIBLE);
			// pd.dismiss();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			this.status = LoadScriptsInParts(start, end);
			if (eventArrayAdapter.size() <= 30) {

				if (MainActivity.showControlCheck) {
					Toast.makeText(
							getActivity(),
							"Loading " + eventArrayAdapter.size() + " of "
									+ eventArrayAdapter.size() + ".",
							Toast.LENGTH_SHORT).show();
				}
			} else if (start > 0)
				if (MainActivity.showControlCheck) {

					Toast.makeText(
							getActivity(),
							"Loading " + start + " of "
									+ eventArrayAdapter.size() + ".",
							Toast.LENGTH_SHORT).show();
				}
			// pd = new ProgressDialog(getContext());
			// pd.setTitle("Loading scripts please wait...");
			// pd.show();

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(1500);
			} catch (Exception e) {

			}
			return null;
		}

	}

	public void showDialogBuy() {

		if (!appClass.is_show_buy_dialog) {
			appClass.is_show_buy_dialog = true;

			FragmentTransaction ft = ((FragmentActivity) getActivity())
					.getSupportFragmentManager().beginTransaction();

			Fragment prev = ((FragmentActivity) getActivity())
					.getSupportFragmentManager().findFragmentByTag("dialog");

			if (prev != null) {
				ft.remove(prev);
			}
			ft.addToBackStack(null);
			// Create and show the dialog.
			DialogFragment newFragment = DialogBuy.newInstance(1, "", 1);
			newFragment.show(ft, "dialog");
		}

	}

}
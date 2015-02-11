package com.cobra.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.AsyncTask;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cobra.R;
import com.cobra.TempClass;
import com.cobra.appClass;
import com.cobra.api.Cobra;
import com.cobra.api.Cobra.CobraEvent;
import com.cobra.api.Cobra.ICobraEventListener;
import com.cobra.classes.BucketLabels;
import com.cobra.classes.BucketModules;
import com.cobra.classes.Buckets;
import com.cobra.classes.Constants;
import com.cobra.classes.GenerateBackground;
import com.cobra.classes.Label_Data;
import com.cobra.classes.Modules;
import com.cobra.classes.MyCustomDragListener;
import com.cobra.db.DBHelper;
import com.cobra.dialogs.ClearBucketDialog;
import com.cobra.interfaces.CustomListViewListener;
import com.cobra.interfaces.OnTaskComplete;
import com.cobra.view.bucketfiring.Bucket;
import com.cobra.view.bucketfiring.Module;
import com.cobra.view.bucketfiring.ModuleViews;

/**
 * Parent class {@link MainActivity}
 */

/**
 * Child classes extends from this class
 * {@link MyCustomDragListener, Module, Bucket, Label}
 * For creating new Module create object of {@link Module class}
 * For creating new Bucket create object of {@link Bucket class} 
 * for creating new Label create object of {@link Label class}
 */

/**
 * Data classes
 * {@link Buckets, BucketModules, DBHelper}
 */

/**
 * Bucket Color class {@link GenerateBackground}
 */

/**
 * Other Classes {@link MyCustomDragListener}
 */
public class BucketFiring extends LinearLayout {

	protected boolean Is_ShellUpdate_Enable = false;

	private Activity activity;
	protected Context context;
	private Button btn_ARM;
	private Button btn_ClearBuckets;
	private Button btn_AddBucket;
	private AlertDialog.Builder builder = null;
	private Boolean IsDataUpdated = true;
	private View rootView;
	private Cobra cobra = null;
	private static TextView txt_Cues = null;
	private LinearLayout layout_TimeDetails;

	// /** contains information about the cues that are fired from bucket "Fire"
	// button.
	// * {@link Bucket}*/
	// protected static int LastCueToFire=0;

	/** layout that contain buckets */
	protected static LinearLayout linearList;
	/** Main layout(parent layout) of bucket firing */
	protected static LinearLayout layout;
	/**
	 * used to calculate height of action bar. calculate height under
	 * {@link BucketFiring#LoadAll()}
	 */
	protected static float actionbarHeight;
	/**
	 * {custom layout @link ModulesUnderBucket that contains modules on left
	 * side}
	 */
	protected static ModulesContainer gridLayout;
	/** scroll view applies on {@link #layout} */
	protected static ScrollView scrollerView;
	/**
	 * {this array contains label data that we will get from stored preferences
	 * in @link Bucket class}
	 */
	// protected static ArrayList<Label_Data> label_Array = null;
	protected static String[] array_labels = null;
	protected static String[] array_time = null;
	/**
	 * { DraggedModule stores id of module that was dragged last time in @link
	 * Module class under #linear.setOnTouchListener}
	 */
	protected static int DraggedModule = 0;

	/**
	 * contains data of buckets. we are storing data in this list under
	 * {@link #GetData() and #AddNewBucket()}
	 */
	protected static ArrayList<Buckets> bucketList_Data = null;
	/**
	 * contains list of bucket UI and maintain under {@link Bucket class}
	 */
	protected static List<RelativeLayout> bucketList_UI = new ArrayList<RelativeLayout>();
	/**
	 * contains list of modules data and maintain under {@link #GetData() }
	 * 
	 */
	protected static ArrayList<Modules> moduleList_Data;
	/**
	 * contains modules stored in each seperate bucket. maintain under
	 * {@link #customListViewListener}
	 */
	protected static List<ModulesContainer> Moduleslist_UI = new ArrayList<ModulesContainer>();
	/** contains number of modules and create under {@link Module} */
	protected static List<View> modules_UI = new ArrayList<View>();

	protected static HashMap<Integer, ModuleViews> modules_HashMap = new HashMap<Integer, ModuleViews>();
	/**
	 * contains those modules that are being dragged. maintain under
	 * {@link #customListViewListener and #GetData()}
	 */
	protected static ArrayList<BucketModules> draggedModules_List;
	/**
	 * contains list of labels under each bucket
	 */
	protected static ArrayList<BucketLabels> BucketLabels_List;
	/**
	 * contains data of buckets that are newely created under
	 * {@link #AddNewBucket() and #customListViewListener}
	 */
	protected static ArrayList<Buckets> NewBucketList = new ArrayList<Buckets>();
	/**
	 * its a UI of bucket that allow us to create new bucket when drag and drop
	 * on it. handling via {@link #customListViewListener} and create via
	 * {@link BucketFiring#Bucket_AddMore_UI}
	 */
	protected static RelativeLayout Bucket_AddMore_UI = null;

	/**
	 * Helping constructor under child classes {@link Bucket , Module , Label ,
	 * MyCustomDragListener}
	 */
	public BucketFiring(Context context) {
		super(context);
	}

	appClass globV;

	public BucketFiring(Context context, Activity activity, Cobra cobra) {
		// public BucketFiring(Context context, TempClass activity) {
		super(context);
		if (cobra != null) {
			this.cobra = cobra;
			// this.cobra.AddBucketFiringListener(cobraEventListener);
		}

		this.context = context;
		this.activity = activity;
		globV = (appClass) this.activity.getApplicationContext();

		IntentFilter intentFilter = new IntentFilter(
				appClass.RECIEVER_PERSISTENT_HEADER);
		activity.registerReceiver(broadcastReceiver, intentFilter);
		LoadAll();
		
		BucketReceiver=Bucket.BucketReciever;
		IntentFilter intentFilter2 = new IntentFilter(
				appClass.RECIEVER_BUCKETFIRING_BUCKET);
		activity.registerReceiver(BucketReceiver, intentFilter2);
	}

	/**
	 * we are calling this function when class first loads and when user click
	 * on {@link BucketFiring#btn_ClearBuckets} to re initiallize all data its
	 * getting fresh data from data base using {@link #GetData()} and creating
	 * modules using class {@link Module} under loop
	 * 
	 * */
	public void LoadAll() {
		ClearAll();
		rootView = View.inflate(context, R.layout.bucket_firing, this);
		layout = (LinearLayout) rootView.findViewById(R.id.layout);
		// layout.setOnDragListener(dragListener);

		Resources r = getResources();
		actionbarHeight = TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 55, r.getDisplayMetrics());

		gridLayout = (ModulesContainer) rootView.findViewById(R.id.grid);
		gridLayout.setBackgroundResource(R.drawable.round_corner);
		int padding = (int) GetLength(1.33f, 'h');
		gridLayout.setPadding(padding, padding, padding, padding);
		linearList = (LinearLayout) rootView.findViewById(R.id.linear_list);
		scrollerView = (ScrollView) rootView.findViewById(R.id.scrollView1);
		txt_Cues = (TextView) rootView.findViewById(R.id.cues_text);

		TOTAL_FIRED_CUES = 0;
		TOTAL_REMAINING_CUES = 0;
//		 DBHelper.DeleteBucketModule(context);
//		 DBHelper.DeleteBuckets(context);
//		 DBHelper.DeleteModules(context);
		DBHelper.ClearCue(context);
		// for (int i = 12; i < 40; i++) {
		// DBHelper.insertModule(context, "A" + i, (i - 12), "");
		// }
		// //
//		 DBHelper.DeleteBucketLabel_ALL(context);
		if (DBHelper.getLabels(context) == null) {
			for (int i = 0; i < 8; i++) {
				DBHelper.insertLabel(context, (i + 1) + "\"");
			}
		}
//		 DBHelper.DeleteTime(context);
		if (DBHelper.getTime(context) == null) {
			for (int i = 500; i < 4000; i++) {
				DBHelper.insertTime(context, i + "");
				i += 499;

			}
		}
		GetData();
		for (int i = 0; i < moduleList_Data.size(); i++) {
			// CreateModules(i, gridLayout, true);
			new Module(activity, context, i, gridLayout, true);
		}

		ArrangeBuckets();
		btn_AddBucket = (Button) rootView.findViewById(R.id.btn_addbucket);
		btn_AddBucket.setOnClickListener(ButtonAddBucketListener);
		btn_ClearBuckets = (Button) rootView
				.findViewById(R.id.btn_clearbuckets);
		btn_ClearBuckets.setOnClickListener(ButtonClearBucketsListener);
		layout_TimeDetails = (LinearLayout) rootView
				.findViewById(R.id.layout_time_details_container);
		btn_ARM = (Button) rootView.findViewById(R.id.btn_ARM);
		btn_ARM.setOnClickListener(ButtonARMListener);
		if (cobra != null)
			Handle_ARM_Button(cobra.getMode());
		new MyCustomDragListener(context, activity, customListViewListener);
	}

	/**
	 * {@link #ArrangeBuckets() arrange arrange buckets and modules according to
	 * the last stored data in database}
	 * 
	 **/
	private void ArrangeBuckets() {
		for (int i = 0; i < moduleList_Data.size(); i++) {
			for (int j = 0; j < draggedModules_List.size(); j++) {
				if (moduleList_Data.get(i).getAddress()
						.equals(draggedModules_List.get(j).getModuleAddress())) {
					if (draggedModules_List.get(j).getBucketId().equals("-1")) {
						modules_UI.get(i).setVisibility(View.VISIBLE);
					} else {
						modules_UI.get(i).setVisibility(View.GONE);
						// CreateModules(i, Moduleslist_UI.get(Integer
						// .parseInt(draggedModules_List.get(j)
						// .getBucketId())), false);
						BucketModules bm = draggedModules_List.get(j);
						int index = Integer.parseInt(bm.getBucketId());
						// last index is based on NEW BUCKET UI
						if (index <= Moduleslist_UI.size() - 1) {
							ModulesContainer mub = Moduleslist_UI.get(index);
							Update_FiredRemain_CuesUnderBucket_WhenDragged_In(
									i, index);
							new Module(activity, context, i, mub, false);
						}
					}
					break;
				}

			}
		}
	}

	/**
	 * {@link BucketFiring#CreateBucket_AddMore(Context) create a bucket area
	 * that allow use to create new buckets when user drop any module on it and
	 * it lies at the end of all buckets}
	 **/
	public void CreateBucket_AddMore(Context context) {
		View view = View.inflate(context, R.layout.list_item_new_bucket, null);
		Bucket_AddMore_UI = (RelativeLayout) view
				.findViewById(R.id.content_holder);
		Bucket_AddMore_UI.setId(-1);
		linearList.addView(view);
	}

	/**
	 * {@link #AddNewBucket() create new buckets when user drop any module in
	 * MyCustomDragListener#MyCustomDragListener(Context, Activity,
	 * CustomListViewListener) or #btn_AddBucket}
	 * */
	private void AddNewBucket() {
		IsDataUpdated = false;
		Buckets NewBucket = new Buckets();
		NewBucket.setBucketName("" + bucketList_Data.size() + 1);
		NewBucketList.add(NewBucket);
		bucketList_Data.add(NewBucket);
		DBHelper.insertBucket(getContext(), "" + bucketList_Data.size(),
				Constants.BUCKET_DEFAULT_TIME_IN_MS,
				Constants.BUCKET_STATUS_SEQ);
		int position = bucketList_Data.size() - 1;
		new Bucket(activity, context, position, "" + bucketList_Data.size(),
				cobra);
	}

	/**
	 * {@link #GetData() get last stored data from database that we consider as
	 * latest data}
	 */
	private void GetData() {
		gridLayout.removeAllViews();
		moduleList_Data = DBHelper.getModules(context);

		{
			globV.getModuleList();
		}
		array_labels = DBHelper.getLabels(context);
		array_time = DBHelper.getTime(context);
		BucketLabels_List = DBHelper.getBucketLabels(context);
		SetModuleStatus();
		// for (int i = 0; i < moduleList_Data.size(); i++) {
		// CreateModules(i, gridLayout, true);
		// }

		bucketList_Data = DBHelper.getBuckets(context);
		for (int i = -1; i < bucketList_Data.size(); i++) {
			if (i == -1) {
				// CreateListItem(i, null);
				new Bucket(activity, context, i, null, cobra);
			} else {
				// CreateListItem(i, bucketList_Data.get(i).getBucketName());
				new Bucket(activity, context, i, bucketList_Data.get(i)
						.getBucketName(), cobra);
			}
		}

		draggedModules_List = DBHelper.getBucketModules(context);

	}

	/**
	 * {@link #SetModuleStatus() check currently active module and set them as
	 * active. otherwise module is considered as inactive}
	 * {@link appClass.moduleList contains modules that are currently active or
	 * connected with 18R2} basically we are doing this to change the color of
	 * module. Active modules color is white and inActive modules color is dark
	 * grey
	 */
	private void SetModuleStatus() {
		// if (moduleList_Data != null && appClass.moduleList != null) {
		// if (moduleList_Data.size() == 0) {
		// for (int i = 0; i < appClass.moduleList.size(); i++) {
		// DBHelper.insertModule(context, appClass.moduleList.get(i)
		// .getAddress(), appClass.moduleList.get(i)
		// .getChannel(), "");
		// moduleList_Data = DBHelper.getModules(context);
		// }
		// return;
		// }
		//
		// Boolean IsActiveModuleIsInDataBase = false;
		//
		// for (int i = 0; i < appClass.moduleList.size(); i++) {
		// for (int j = 0; j < moduleList_Data.size(); j++) {
		// Modules DbModule = moduleList_Data.get(j);
		// Modules ActiveModule = appClass.moduleList.get(i);
		// if (ActiveModule.getAddress().equals(DbModule.getAddress())) {
		// IsActiveModuleIsInDataBase = true;
		// DbModule.setAcive(true);
		// break;
		// }
		// }
		// if (!IsActiveModuleIsInDataBase) {
		// DBHelper.insertModule(context, appClass.moduleList.get(i)
		// .getAddress(), appClass.moduleList.get(i)
		// .getChannel(), "");
		// }
		// }
		// }
	}

	BroadcastReceiver BucketReceiver;
	
	/**
	 * {@link #cobraEventListener listening 18R2. we are using this listener to
	 * listen ARM and TEST buttons on 18R2. by doing this we will be able to
	 * handle UI changes on tablet.} {@link #Handle_ARM_Button() handling the UI
	 * changes based on ARM and TEST buttons}
	 */
	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				int event = -1;
				int mode = -1;
				if (intent.hasExtra(appClass.TAG_EVENT)) {
					event = intent.getExtras().getInt(appClass.TAG_EVENT);
				}
				if (intent.hasExtra("mode")) {
					mode = intent.getExtras().getInt("mode");
				}
				switch (event) {
				case Cobra.EVENT_TYPE_STATUS_CHANGE:
					Handle_ARM_Button(mode);
					break;
				case PersistentHeader.EVENT_TYPE_HEADER_INFO_CHANGE:
					int modid = -1;
					int currentChannel = -1;
					if (intent.hasExtra("modID")
							&& intent.hasExtra("currentChannel")) {
						modid = intent.getExtras().getInt("modID");
						currentChannel = intent.getExtras().getInt(
								"currentChannel");
						if ((modid != -2 || modid != -1)
								&& currentChannel != -1) {
							for (int i = 0; i < moduleList_Data.size(); i++) {
								if (moduleList_Data.get(i).getAddress()
										.contains("" + modid)) {
									moduleList_Data.get(i).setChannel(
											currentChannel);
								}
							}

							ModuleViews Current_ModuleView = null;
							ModuleViews Pre_ModuleView = null;

							if (globV.getModuleList().containsKey(modid)) {
								Current_ModuleView = globV.getModuleList().get(
										modid);
							}
							if (modules_HashMap.containsKey(modid)) {
								Pre_ModuleView = modules_HashMap.get(modid);
							}

							if (Current_ModuleView != null
									&& Pre_ModuleView != null) {
								int pre_channel = Pre_ModuleView
										.getModuleChannel();
								int current_channel = Current_ModuleView
										.getModuleChannel();
								if (pre_channel != current_channel) {
									Pre_ModuleView
											.setModuleChannel(current_channel);

									DBHelper.UpdateModuleChannel(getContext(),
											modid, currentChannel);
									if (Pre_ModuleView.getBucketAreaChannel() != null) {
										Pre_ModuleView
												.getBucketAreaChannel()
												.setText(
														"Ch. : "
																+ currentChannel);
									}
									if (Pre_ModuleView.getModuleAreaChannel() != null) {
										Pre_ModuleView
												.getModuleAreaChannel()
												.setText(
														"Ch. : "
																+ currentChannel);
									}
								}
							}

							// if(modules_HashMap.containsKey(modid)){
							// ModuleViews
							// moduleView=modules_HashMap.get(modid);
							// DBHelper.UpdateModuleChannel(getContext(), modid,
							// currentChannel);
							// if(moduleView.getBucketAreaChannel()!=null){
							// moduleView.getBucketAreaChannel().setText("Ch. : "+currentChannel);
							// }
							// if(moduleView.getModuleAreaChannel()!=null){
							// moduleView.getModuleAreaChannel().setText("Ch. : "+currentChannel);
							// }
							// }
						}
					}

					break;
				default:
					break;
				}
			} catch (Exception e) {
				int i = 0;
				i++;
			}
		}
	};

	/**
	 * {@link #customListViewListener listening from where module is dragged and
	 * where it is dropped} ModuleDraggedInToTheList returns the dragged module
	 * id and list index on which module is dropped NewBucket returns dragged
	 * module id and create new bucket then dragged module is added into newly
	 * created bucket ModuleDraggedOutFromList returns the dragged module id and
	 * tells that module is dragged out from the bucket. so it should move into
	 * modules area.
	 */
	CustomListViewListener customListViewListener = new CustomListViewListener() {
		@Override
		public void ModuleDraggedInToTheList(int DraggedModule, int i) {
			// TODO Auto-generated method stub
			int pre_list_id = GetDraggedModuleListId(moduleList_Data.get(
					DraggedModule).getAddress());
			if (!bucketList_Data.get(i).IsPaused()) {
				if (pre_list_id != -1 && pre_list_id != i) {
					Update_FiredRemain_CuesUnderBucket_WhenDragged_Out(DraggedModule);
				}

				UpdateDraggedModule(moduleList_Data.get(DraggedModule)
						.getAddress(), i);
				DBHelper.UpdateBucketModule(getContext(),
						moduleList_Data.get(DraggedModule).getAddress(), "" + i);
				// CreateModules(DraggedModule, Moduleslist_UI.get(i), false);
				new Module(activity, context, DraggedModule,
						Moduleslist_UI.get(i), false);
				if (pre_list_id != i) {
					Update_FiredRemain_CuesUnderBucket_WhenDragged_In(
							DraggedModule, i);
				}
			} else {
				Toast.makeText(getContext(),
						"Please \"Pause\" this bucket first", Toast.LENGTH_LONG)
						.show();
			}
		}

		@Override
		public void NewBucket(int DraggedModule) {
			// TODO Auto-generated method stub
			// CreateListItem(bucketList_Data.size(), "" +
			// bucketList_Data.size());
			AddNewBucket();
			int ListIndex = Moduleslist_UI.size();
			if (ListIndex != 0) {
				ListIndex -= 1;
			}
			int pre_listid = GetDraggedModuleListId(moduleList_Data.get(
					DraggedModule).getAddress());
			if (pre_listid == -1) {
				Update_FiredRemain_CuesUnderBucket_WhenDragged_In(
						DraggedModule, ListIndex);
			} else {
				Update_FiredRemain_CuesUnderBucket_WhenDragged_Out(DraggedModule);
				Update_FiredRemain_CuesUnderBucket_WhenDragged_In(
						DraggedModule, ListIndex);
			}

			UpdateDraggedModule(
					moduleList_Data.get(DraggedModule).getAddress(), ListIndex);
			DBHelper.UpdateBucketModule(getContext(),
					moduleList_Data.get(DraggedModule).getAddress(), ""
							+ ListIndex);
			// CreateModules(DraggedModule, Moduleslist_UI.get(index), false);
			new Module(activity, context, DraggedModule,
					Moduleslist_UI.get(ListIndex), false);

		}

		@Override
		public void ModuleDraggedOutFromList(int DraggedModule) {
			// TODO Auto-generated method stub
			Update_FiredRemain_CuesUnderBucket_WhenDragged_Out(DraggedModule);

			DBHelper.UpdateBucketModule(getContext(),
					moduleList_Data.get(DraggedModule).getAddress(), "-" + 1);
			UpdateDraggedModule(
					moduleList_Data.get(DraggedModule).getAddress(), -1);
			moduleList_Data.get(DraggedModule).getModuleCues()
					.InitializeCuesUI(context, modules_UI.get(DraggedModule));
			modules_UI.get(DraggedModule).setVisibility(View.VISIBLE);

		}
	};

	protected static int TOTAL_FIRED_CUES = 0;
	protected static int TOTAL_REMAINING_CUES = 0;

	public static void UpdateCuesData() {
		if (txt_Cues != null)
			txt_Cues.setText(TOTAL_FIRED_CUES + " Fired, "
					+ TOTAL_REMAINING_CUES + " Remain");
	}

	private void Update_FiredRemain_CuesUnderBucket_WhenDragged_Out(int modue_id) {
		int list_id = GetDraggedModuleListId(moduleList_Data.get(DraggedModule)
				.getAddress());
		if (list_id != -1) {
			int fired_cues_of_module = moduleList_Data.get(DraggedModule)
					.getModuleCues().getFiredCues();
			int remaining_cues_of_module = 18 - fired_cues_of_module;

			int fired_cues_of_bucket = Integer.parseInt(bucketList_Data
					.get(list_id).getFiredCues().getText().toString());
			int remaining_cues_of_bucket = Integer.parseInt(bucketList_Data
					.get(list_id).getRemainCues().getText().toString());

			bucketList_Data
					.get(list_id)
					.getFiredCues()
					.setText("" + (fired_cues_of_bucket - fired_cues_of_module));
			bucketList_Data
					.get(list_id)
					.getRemainCues()
					.setText(
							""
									+ (remaining_cues_of_bucket - remaining_cues_of_module));

			TOTAL_FIRED_CUES -= fired_cues_of_module;
			TOTAL_REMAINING_CUES -= remaining_cues_of_module;
			UpdateCuesData();
		}
	}

	private void Update_FiredRemain_CuesUnderBucket_WhenDragged_In(
			int modue_id, int list_id) {

		int fired_cues_of_module = moduleList_Data.get(modue_id)
				.getModuleCues().getFiredCues();
		int remaining_cues_of_module = 18 - fired_cues_of_module;

		int fired_cues_of_bucket = Integer.parseInt(bucketList_Data
				.get(list_id).getFiredCues().getText().toString());
		int remaining_cues_of_bucket = Integer.parseInt(bucketList_Data
				.get(list_id).getRemainCues().getText().toString());

		bucketList_Data.get(list_id).getFiredCues()
				.setText("" + (fired_cues_of_bucket + fired_cues_of_module));
		bucketList_Data
				.get(list_id)
				.getRemainCues()
				.setText(
						""
								+ (remaining_cues_of_bucket + remaining_cues_of_module));

		TOTAL_FIRED_CUES += fired_cues_of_module;
		TOTAL_REMAINING_CUES += remaining_cues_of_module;
		UpdateCuesData();
	}

	/**
	 * {@link #ResetAll() basically } move all modules from buckets to modules
	 * area then remove all buckets then delete all buckets from database then
	 * call {@link #LoadAll() to recreate UI}
	 */
	public void ResetAll() {
		for (int i = 0; i < moduleList_Data.size(); i++) {
			DBHelper.UpdateBucketModule(getContext(), moduleList_Data.get(i)
					.getAddress(), "-" + 1);
			UpdateDraggedModule(moduleList_Data.get(i).getAddress(), -1);
			modules_UI.get(i).setVisibility(View.VISIBLE);
		}

		linearList.removeAllViews();
		DBHelper.DeleteBuckets(context);
		LoadAll();
	}

	/**
	 * {@link #ButtonARMListener handling ARM and TEST button on 18R2}
	 */
	View.OnClickListener ButtonARMListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (cobra.getMode() == Cobra.MODE_ARMED) {
				cobra.setTestMode();
			} else if (cobra.getMode() == Cobra.MODE_TEST) {
				cobra.setArmMode();
			}
			// TODO Auto-generated method stub
		}
	};

	/**
	 * {@link #UpdateDraggedModule(String, int) contains modules information
	 * that were dragged and stored on database last time}
	 */
	public void UpdateDraggedModule(String ModuleAddress, int ListId) {
		Boolean Is_Found = false;
		for (int i = 0; i < draggedModules_List.size(); i++) {
			if (draggedModules_List.get(i).getModuleAddress()
					.equals(ModuleAddress)) {
				draggedModules_List.get(i).setBucketId("" + ListId);
				Is_Found = true;
				break;
			}
		}
		if (!Is_Found) {
			BucketModules bucketModule = new BucketModules();
			bucketModule.setBucketId("" + ListId);
			bucketModule.setModuleAddress(ModuleAddress);
			draggedModules_List.add(bucketModule);
		}
	}

	public int GetDraggedModuleListId(String ModuleAddress) {
		for (int i = 0; i < draggedModules_List.size(); i++) {
			if (draggedModules_List.get(i).getModuleAddress()
					.equals(ModuleAddress)) {
				return Integer.parseInt(draggedModules_List.get(i)
						.getBucketId());
			}
		}
		return -1;
	}

	/**
	 * {@link #ButtonAddBucketListener used to add new bucket}
	 */
	View.OnClickListener ButtonAddBucketListener = new OnClickListener() {
		@Override
		public void onClick(View v) {

			AddNewBucket();
		}
	};

	/**
	 * {@link #ButtonClearBucketsListener used to remove all buckets } it calls
	 * {@link #ResetAll()}
	 */
	View.OnClickListener ButtonClearBucketsListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			final ClearBucketDialog dialog = new ClearBucketDialog(context);
			dialog.getCloseButton().setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});

			dialog.getClearButton().setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// if (bucketList_UI.size() == 1) {
					if (bucketList_UI.size() == 0) {
						Toast.makeText(context, "No buckets found",
								Toast.LENGTH_LONG).show();
						dialog.dismiss();
						return;
					}
					ResetAll();
					dialog.dismiss();
				}
			});
			dialog.show();
		}
	};

	/**
	 * {@link #Handle_ARM_Button()} handle UI changed This function is being
	 * called by {@link #cobraEventListener}
	 */
	private void Handle_ARM_Button(int mode) {
		if (btn_ARM != null) {
			if (mode == Cobra.MODE_ARMED) {
				layout_TimeDetails.setVisibility(View.VISIBLE);
				btn_ARM.setVisibility(View.VISIBLE);
				btn_ARM.setText("DISARM");
				btn_ARM.setBackground(GenerateBackground.Background(activity,
						Constants.GREEN, Constants.BLACK, 255, true, 0.5f));
			} else if (mode == Cobra.MODE_TEST) {

				layout_TimeDetails.setVisibility(View.GONE);
				btn_ARM.setVisibility(View.VISIBLE);
				btn_ARM.setText("ARM");
				btn_ARM.setBackground(GenerateBackground.Background(activity,
						Constants.RED_DARK, Constants.BLACK, 255, true, 0.5f));
			} else {
				layout_TimeDetails.setVisibility(View.GONE);
				btn_ARM.setVisibility(View.GONE);
			}

		}
	}

	/**
	 * {@link #SaveDialog() manually save data into the database.} It calls
	 * {@link UpdateDataBase Async class} Its currently not in use
	 */
	public void SaveDialog() {
		if (builder == null) {
			builder = new AlertDialog.Builder(getContext());
			builder.setTitle("SAVE");
			builder.setMessage("Do you want to save changes?");
			builder.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							new UpdateDataBase(new OnTaskComplete() {

								@Override
								public void OnTaskComplete() {
									// TODO Auto-generated method stub
									Toast.makeText(context, "Changes Saved",
											Toast.LENGTH_SHORT).show();
									appClass.IsAppForcedClosed = false;
									activity.finish();
								}
							}).execute();
						}
					});
			builder.setNegativeButton("No",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							appClass.IsAppForcedClosed = false;
							activity.finish();
						}
					});
		}
		builder.show();
	}

	/**
	 * {@link #UpdateDatabase() manually save data into the database.} It is
	 * called by {@link UpdateDataBase Async class} Its currently not in use
	 */
	private void UpdateDatabase() {

		for (int i = 0; i < NewBucketList.size(); i++) {
			DBHelper.insertBucket(getContext(), NewBucketList.get(i)
					.getBucketName(), Constants.BUCKET_DEFAULT_TIME_IN_MS,
					Constants.BUCKET_STATUS_SEQ);
		}
		NewBucketList = new ArrayList<Buckets>();
		for (int i = 0; i < draggedModules_List.size(); i++) {
			DBHelper.UpdateBucketModule(getContext(), draggedModules_List
					.get(i).getModuleAddress(), draggedModules_List.get(i)
					.getBucketId());
		}
		IsDataUpdated = true;
	}

	/**
	 * {@link #IsDataUpdated() tells that database is updated with latest
	 * changes or not} It is called by {@link TempClass or MainActivity class}
	 * Its currently not in use
	 */
	public Boolean IsDataUpdated() {
		return IsDataUpdated;
	}

	/**
	 * return length in pixels it is used to maintain UI
	 * 
	 * @param percent
	 *            percentage based on current device
	 * @param dimensions
	 *            for calculati+ng length based on height or width
	 * @return
	 */
	public float GetLength(float percent, char dimensions) {
		Display display = activity.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int screen_width = size.x;
		int screen_height = size.y;
		if (dimensions == 'w') {
			float temp = percent / 100 * screen_width;
			return temp;
		} else {
			float temp = percent / 100 * screen_height;
			return temp;
		}
	}

	/**
	 * {@link #ClearAll() reinitialize all objects}
	 */
	public void ClearAll() {
		array_time = null;
		array_labels = null;
		BucketLabels_List = null;
		bucketList_Data = new ArrayList<Buckets>();
		bucketList_UI = new ArrayList<RelativeLayout>();
		moduleList_Data = new ArrayList<Modules>();
		Moduleslist_UI = new ArrayList<ModulesContainer>();
		modules_UI = new ArrayList<View>();
		draggedModules_List = new ArrayList<BucketModules>();
		NewBucketList = new ArrayList<Buckets>();
		Bucket_AddMore_UI = null;
		DraggedModule = 0;
	}

	/**
	 * {@link UpdateDataBase Async class update database manually} just to show
	 * progress dialog make it sleep for few seconds. after sleep call
	 * {@link #UpdateDatabase()} currently not in use
	 * 
	 */
	public class UpdateDataBase extends AsyncTask<Void, Void, Void> {

		ProgressDialog pd;
		OnTaskComplete listener;

		public UpdateDataBase(OnTaskComplete listener) {
			// TODO Auto-generated constructor stub
			this.listener = listener;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pd = new ProgressDialog(getContext());
			pd.setMessage("Saving Changes...");
			pd.show();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			if (pd != null)
				pd.dismiss();
			if (listener != null)
				listener.OnTaskComplete();
			super.onPostExecute(result);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(1200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			UpdateDatabase();
			return null;
		}

	}

}
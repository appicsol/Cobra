package com.cobra.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.cobra.classes.BucketLabels;
import com.cobra.classes.BucketModules;
import com.cobra.classes.Buckets;
import com.cobra.classes.Constants;
import com.cobra.classes.Label_Data;
import com.cobra.classes.Modules;
import com.cobra.classes.ModuleCues;
import com.cobra.classes.Time_Data;

public class DBHelper {
	private static final Object lock = new Object();

	public static long insertModule(Context context, String address,
			int channel, String temp) {
		synchronized (lock) {
			ContentValues values = new ContentValues();
			values.put(DBConstants.MODULE_FIELD_ADDRESS, address);
			values.put(DBConstants.MODULE_FIELD_CHANNEL, channel);
			values.put(DBConstants.MODULE_FIELD_TEMP, temp);
			values.put(DBConstants.MODULE_FIELD_CUE_1,
					Constants.CUE_STATE_AVAILABLE);
			values.put(DBConstants.MODULE_FIELD_CUE_2,
					Constants.CUE_STATE_AVAILABLE);
			values.put(DBConstants.MODULE_FIELD_CUE_3,
					Constants.CUE_STATE_AVAILABLE);
			values.put(DBConstants.MODULE_FIELD_CUE_4,
					Constants.CUE_STATE_AVAILABLE);
			values.put(DBConstants.MODULE_FIELD_CUE_5,
					Constants.CUE_STATE_AVAILABLE);
			values.put(DBConstants.MODULE_FIELD_CUE_6,
					Constants.CUE_STATE_AVAILABLE);
			values.put(DBConstants.MODULE_FIELD_CUE_7,
					Constants.CUE_STATE_AVAILABLE);
			values.put(DBConstants.MODULE_FIELD_CUE_8,
					Constants.CUE_STATE_AVAILABLE);
			values.put(DBConstants.MODULE_FIELD_CUE_9,
					Constants.CUE_STATE_AVAILABLE);
			values.put(DBConstants.MODULE_FIELD_CUE_10,
					Constants.CUE_STATE_AVAILABLE);
			values.put(DBConstants.MODULE_FIELD_CUE_11,
					Constants.CUE_STATE_AVAILABLE);
			values.put(DBConstants.MODULE_FIELD_CUE_12,
					Constants.CUE_STATE_AVAILABLE);
			values.put(DBConstants.MODULE_FIELD_CUE_13,
					Constants.CUE_STATE_AVAILABLE);
			values.put(DBConstants.MODULE_FIELD_CUE_14,
					Constants.CUE_STATE_AVAILABLE);
			values.put(DBConstants.MODULE_FIELD_CUE_15,
					Constants.CUE_STATE_AVAILABLE);
			values.put(DBConstants.MODULE_FIELD_CUE_16,
					Constants.CUE_STATE_AVAILABLE);
			values.put(DBConstants.MODULE_FIELD_CUE_17,
					Constants.CUE_STATE_AVAILABLE);
			values.put(DBConstants.MODULE_FIELD_CUE_18,
					Constants.CUE_STATE_AVAILABLE);
			DBAdapter.openDataBase(context);
			long result = DBAdapter.db.insert(DBConstants.TABLE_MODULE, null,
					values);
			DBAdapter.closeDataBase();
			return result;
		}
	}

	public static long UpdateModuleChannel(Context context, int modID,
			int currentChannel) {
		try {
			synchronized (lock) {
				ContentValues values = new ContentValues();
				values.put(DBConstants.MODULE_FIELD_CHANNEL, ""
						+ currentChannel);

				DBAdapter.openDataBase(context);
				long result = DBAdapter.db.update(DBConstants.TABLE_MODULE,
						values, DBConstants.MODULE_FIELD_ADDRESS + "=\""
								+ modID + "\"", null);
				DBAdapter.closeDataBase();
				return result;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
	}

	public static Boolean InsertOrUpdateModule(Context context, int modID,
			int currentChannel){
		long response=UpdateModuleChannel( context,  modID,
				 currentChannel);
		if(response<=0){
			long response2=insertModule(context, ""+modID, currentChannel, "");
			if(response2>0){
				return true;
			}
		}
		return false;
	}
	
	public static long DeleteModules(Context context) {
		synchronized (lock) {
			DBAdapter.openDataBase(context);
			long result = DBAdapter.db.delete(DBConstants.TABLE_MODULE, null,
					null);
			DBAdapter.closeDataBase();
			return result;
		}
	}

	public static ArrayList<Modules> getModules(Context context) {
		synchronized (lock) {
			ArrayList<Modules> moduleList = new ArrayList<Modules>();

			String[] columns = new String[] { DBConstants.MODULE_FIELD_ADDRESS,
					DBConstants.MODULE_FIELD_CHANNEL,
					DBConstants.MODULE_FIELD_TEMP,
					DBConstants.MODULE_FIELD_CUE_1,
					DBConstants.MODULE_FIELD_CUE_2,
					DBConstants.MODULE_FIELD_CUE_3,
					DBConstants.MODULE_FIELD_CUE_4,
					DBConstants.MODULE_FIELD_CUE_5,
					DBConstants.MODULE_FIELD_CUE_6,
					DBConstants.MODULE_FIELD_CUE_7,
					DBConstants.MODULE_FIELD_CUE_8,
					DBConstants.MODULE_FIELD_CUE_9,
					DBConstants.MODULE_FIELD_CUE_10,
					DBConstants.MODULE_FIELD_CUE_11,
					DBConstants.MODULE_FIELD_CUE_12,
					DBConstants.MODULE_FIELD_CUE_13,
					DBConstants.MODULE_FIELD_CUE_14,
					DBConstants.MODULE_FIELD_CUE_15,
					DBConstants.MODULE_FIELD_CUE_16,
					DBConstants.MODULE_FIELD_CUE_17,
					DBConstants.MODULE_FIELD_CUE_18 };
			DBAdapter.openDataBase(context);
			if (DBAdapter.db == null)
				return null;
			Cursor c = DBAdapter.db.query(true, DBConstants.TABLE_MODULE,
					columns, null, null, null, null, null, null);

			try {
				if (c.getCount() == 0) {
					c.close();
					DBAdapter.closeDataBase();
					return moduleList;
				}

				c.moveToFirst();
				do {
					Modules module = new Modules();
					module.setAddress(c.getString(c
							.getColumnIndex(DBConstants.MODULE_FIELD_ADDRESS)));
					int channel = Integer.parseInt(c.getString(c
							.getColumnIndex(DBConstants.MODULE_FIELD_CHANNEL)));
					module.setChannel(channel);

					ModuleCues cues = new ModuleCues(context);
					cues.setCue_1(c.getInt(c
							.getColumnIndex(DBConstants.MODULE_FIELD_CUE_1)));
					cues.setCue_2(c.getInt(c
							.getColumnIndex(DBConstants.MODULE_FIELD_CUE_2)));
					cues.setCue_3(c.getInt(c
							.getColumnIndex(DBConstants.MODULE_FIELD_CUE_3)));
					cues.setCue_4(c.getInt(c
							.getColumnIndex(DBConstants.MODULE_FIELD_CUE_4)));
					cues.setCue_5(c.getInt(c
							.getColumnIndex(DBConstants.MODULE_FIELD_CUE_5)));
					cues.setCue_6(c.getInt(c
							.getColumnIndex(DBConstants.MODULE_FIELD_CUE_6)));
					cues.setCue_7(c.getInt(c
							.getColumnIndex(DBConstants.MODULE_FIELD_CUE_7)));
					cues.setCue_8(c.getInt(c
							.getColumnIndex(DBConstants.MODULE_FIELD_CUE_8)));
					cues.setCue_9(c.getInt(c
							.getColumnIndex(DBConstants.MODULE_FIELD_CUE_9)));
					cues.setCue_10(c.getInt(c
							.getColumnIndex(DBConstants.MODULE_FIELD_CUE_10)));
					cues.setCue_11(c.getInt(c
							.getColumnIndex(DBConstants.MODULE_FIELD_CUE_11)));
					cues.setCue_12(c.getInt(c
							.getColumnIndex(DBConstants.MODULE_FIELD_CUE_12)));
					cues.setCue_13(c.getInt(c
							.getColumnIndex(DBConstants.MODULE_FIELD_CUE_13)));
					cues.setCue_14(c.getInt(c
							.getColumnIndex(DBConstants.MODULE_FIELD_CUE_14)));
					cues.setCue_15(c.getInt(c
							.getColumnIndex(DBConstants.MODULE_FIELD_CUE_15)));
					cues.setCue_16(c.getInt(c
							.getColumnIndex(DBConstants.MODULE_FIELD_CUE_16)));
					cues.setCue_17(c.getInt(c
							.getColumnIndex(DBConstants.MODULE_FIELD_CUE_17)));
					cues.setCue_18(c.getInt(c
							.getColumnIndex(DBConstants.MODULE_FIELD_CUE_18)));

					module.setModuleCues(cues);
					module.setAcive(false);
					moduleList.add(module);
				} while (c.moveToNext());
				c.close();
				DBAdapter.closeDataBase();
				return moduleList;
			} catch (Exception e) {
				System.out.println("Exception:   " + e.toString());
			} finally {
				if (!c.isClosed())
					c.close();
				DBAdapter.closeDataBase();
			}
			return null;
		}
	}

	public static long insertBucket(Context context, String bucketName,
			int bucketTime, String bucketStatus) {
		synchronized (lock) {
			ContentValues values = new ContentValues();
			values.put(DBConstants.BUCKET_FIELD_NAME, bucketName);
			values.put(DBConstants.BUCKET_FIELD_TIME, bucketTime);
			values.put(DBConstants.BUCKET_FIELD_STATUS, bucketStatus);

			DBAdapter.openDataBase(context);
			long result = DBAdapter.db.insert(DBConstants.TABLE_BUCKET, null,
					values);
			DBAdapter.closeDataBase();
			return result;
		}
	}

	public static long DeleteBuckets(Context context) {
		synchronized (lock) {
			DBAdapter.openDataBase(context);
			long result = DBAdapter.db.delete(DBConstants.TABLE_BUCKET, null,
					null);
			DBAdapter.closeDataBase();
			return result;
		}
	}

	public static long UpdateBucketTime(Context context, String bucketName,
			String bucketTime) {
		try {
			synchronized (lock) {
				ContentValues values = new ContentValues();
				values.put(DBConstants.BUCKET_FIELD_TIME, bucketTime);

				DBAdapter.openDataBase(context);
				long result = DBAdapter.db.update(DBConstants.TABLE_BUCKET,
						values, DBConstants.BUCKET_FIELD_NAME + "=\""
								+ bucketName + "\"", null);
				DBAdapter.closeDataBase();
				return result;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
	}

	public static long UpdateBucketStatus(Context context, String bucketName,
			String bucketStatus) {
		try {
			synchronized (lock) {
				ContentValues values = new ContentValues();
				values.put(DBConstants.BUCKET_FIELD_STATUS, bucketStatus);

				DBAdapter.openDataBase(context);
				long result = DBAdapter.db.update(DBConstants.TABLE_BUCKET,
						values, DBConstants.BUCKET_FIELD_NAME + "=\""
								+ bucketName + "\"", null);
				DBAdapter.closeDataBase();
				return result;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
	}

	public static ArrayList<Buckets> getBuckets(Context context) {
		synchronized (lock) {
			ArrayList<Buckets> bucketList = new ArrayList<Buckets>();
			String[] columns = new String[] { DBConstants.BUCKET_FIELD_NAME,
					DBConstants.BUCKET_FIELD_TIME,
					DBConstants.BUCKET_FIELD_STATUS };
			String orderby = DBConstants.BUCKET_FIELD_ID;
			DBAdapter.openDataBase(context);
			if (DBAdapter.db == null)
				return null;
			Cursor c = DBAdapter.db.query(true, DBConstants.TABLE_BUCKET,
					columns, null, null, null, null, orderby + " ASC", null);

			try {
				if (c.getCount() == 0) {
					c.close();
					DBAdapter.closeDataBase();
					return bucketList;
				}

				c.moveToFirst();
				do {
					Buckets bucket = new Buckets();
					bucket.setBucketName(c.getString(c
							.getColumnIndex(columns[0])));

					bucket.setBucketTime(c.getInt(c.getColumnIndex(columns[1])));

					bucket.setBucketStatus(c.getString(c
							.getColumnIndex(columns[2])));

					bucketList.add(bucket);
				} while (c.moveToNext());
				c.close();
				DBAdapter.closeDataBase();
				return bucketList;
			} catch (Exception e) {
				System.out.println("Exception:   " + e.toString());
			} finally {
				if (!c.isClosed())
					c.close();
				DBAdapter.closeDataBase();
			}
			return null;
		}
	}

	public static ArrayList<BucketModules> getBucketModules(Context context) {
		synchronized (lock) {
			ArrayList<BucketModules> bucket_module_List = new ArrayList<BucketModules>();
			String[] columns = new String[] {
					DBConstants.BUCKET_MODULE_FIELD_BUCKET_ID,
					DBConstants.BUCKET_MODULE_FIELD_MODULE_ADDRESS };
			String orderby = DBConstants.BUCKET_MODULE_FIELD_ID;
			DBAdapter.openDataBase(context);
			if (DBAdapter.db == null)
				return null;
			Cursor c = DBAdapter.db.query(true,
					DBConstants.TABLE_BUCKET_MODULE, columns, null, null, null,
					null, orderby + " ASC", null);

			try {
				if (c.getCount() == 0) {
					c.close();
					DBAdapter.closeDataBase();
					return bucket_module_List;
				}

				c.moveToFirst();
				do {
					BucketModules bucket_module = new BucketModules();
					bucket_module.setBucketId(c.getString(c
							.getColumnIndex(columns[0])));
					bucket_module.setModuleAddress(c.getString(c
							.getColumnIndex(columns[1])));
					bucket_module_List.add(bucket_module);
				} while (c.moveToNext());
				c.close();
				DBAdapter.closeDataBase();
				return bucket_module_List;
			} catch (Exception e) {
				System.out.println("Exception:   " + e.toString());
			} finally {
				if (!c.isClosed())
					c.close();
				DBAdapter.closeDataBase();
			}
			return null;
		}
	}

	public static long UpdateBucketModule(Context context,
			String moduleAddress, String bucketID) {
		try {
			synchronized (lock) {
				ContentValues values = new ContentValues();
				values.put(DBConstants.BUCKET_MODULE_FIELD_BUCKET_ID, bucketID);

				DBAdapter.openDataBase(context);
				long result = DBAdapter.db.update(
						DBConstants.TABLE_BUCKET_MODULE, values,
						DBConstants.BUCKET_MODULE_FIELD_MODULE_ADDRESS + "=\""
								+ moduleAddress + "\"", null);
				if (result == 0) {
					result = InsertBucketModule(context, moduleAddress,
							bucketID);
				}
				DBAdapter.closeDataBase();
				return result;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
	}

	public static long InsertBucketModule(Context context,
			String moduleAddress, String bucketID) {
		synchronized (lock) {
			ContentValues values = new ContentValues();
			values.put(DBConstants.BUCKET_MODULE_FIELD_MODULE_ADDRESS,
					moduleAddress);
			values.put(DBConstants.BUCKET_MODULE_FIELD_BUCKET_ID, bucketID);
			DBAdapter.openDataBase(context);
			long result = DBAdapter.db.insert(DBConstants.TABLE_BUCKET_MODULE,
					null, values);
			DBAdapter.closeDataBase();
			return result;
		}
	}

	public static long DeleteBucketModule(Context context) {
		synchronized (lock) {
			DBAdapter.openDataBase(context);
			long result = DBAdapter.db.delete(DBConstants.TABLE_BUCKET_MODULE,
					null, null);
			DBAdapter.closeDataBase();
			return result;
		}
	}

	public static long insertLabel(Context context, String labelname) {
		synchronized (lock) {
			ContentValues values = new ContentValues();
			values.put(DBConstants.LABEL_FIELD_NAME, labelname);

			DBAdapter.openDataBase(context);
			long result = DBAdapter.db.insert(DBConstants.TABLE_LABEL, null,
					values);
			DBAdapter.closeDataBase();
			return result;
		}
	}

	public static String[] getLabels(Context context) {
		synchronized (lock) {
			ArrayList<Label_Data> labelList = new ArrayList<Label_Data>();
			String[] columns = new String[] { DBConstants.LABEL_FIELD_NAME };
			String orderby = DBConstants.LABEL_FIELD_ID;
			DBAdapter.openDataBase(context);
			if (DBAdapter.db == null)
				return null;
			Cursor c = DBAdapter.db.query(true, DBConstants.TABLE_LABEL,
					columns, null, null, null, null, orderby + " ASC", null);

			try {
				if (c.getCount() == 0) {
					c.close();
					DBAdapter.closeDataBase();
					return null;
				}

				c.moveToFirst();
				do {
					Label_Data label = new Label_Data();
					label.setLabel_Name(c.getString(c
							.getColumnIndex(columns[0])));
					labelList.add(label);
				} while (c.moveToNext());
				c.close();
				DBAdapter.closeDataBase();
				String[] labelArray = new String[labelList.size()];
				for (int i = 0; i < labelList.size(); i++) {
					labelArray[i] = labelList.get(i).getLabelName();
				}
				return labelArray;
			} catch (Exception e) {
				System.out.println("Exception:   " + e.toString());
			} finally {
				if (!c.isClosed())
					c.close();
				DBAdapter.closeDataBase();
			}
			return null;
		}
	}

	public static long insertTime(Context context, String time) {
		synchronized (lock) {
			ContentValues values = new ContentValues();
			values.put(DBConstants.TIME_FIELD_TIME, time);

			DBAdapter.openDataBase(context);
			long result = DBAdapter.db.insert(DBConstants.TABLE_TIME, null,
					values);
			DBAdapter.closeDataBase();
			return result;
		}
	}

	public static String[] getTime(Context context) {
		synchronized (lock) {
			ArrayList<Time_Data> timeList = new ArrayList<Time_Data>();
			String[] columns = new String[] { DBConstants.TIME_FIELD_TIME };
			String orderby = DBConstants.TIME_FIELD_ID;
			DBAdapter.openDataBase(context);
			if (DBAdapter.db == null)
				return null;
			Cursor c = DBAdapter.db.query(true, DBConstants.TABLE_TIME,
					columns, null, null, null, null, orderby + " ASC", null);
			try {
				if (c.getCount() == 0) {
					c.close();
					DBAdapter.closeDataBase();
					return null;
				}

				c.moveToFirst();
				do {
					Time_Data time = new Time_Data();
					time.setTime(c.getString(c.getColumnIndex(columns[0])));
					timeList.add(time);
				} while (c.moveToNext());
				c.close();
				DBAdapter.closeDataBase();
				String[] timeArray = new String[timeList.size()];
				for (int i = 0; i < timeList.size(); i++) {
					timeArray[i] = timeList.get(i).getTime();
				}
				return timeArray;
			} catch (Exception e) {
				System.out.println("Exception:   " + e.toString());
			} finally {
				if (!c.isClosed())
					c.close();
				DBAdapter.closeDataBase();
			}
			return null;
		}
	}

	public static long DeleteTime(Context context) {
		try {
			synchronized (lock) {
				DBAdapter.openDataBase(context);
				long result = DBAdapter.db.delete(DBConstants.TABLE_TIME, null,
						null);
				DBAdapter.closeDataBase();
				return result;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
	}

	public static long InsertBucketLabel(Context context, int bucketID,
			String LabelName) {
		synchronized (lock) {
			ContentValues values = new ContentValues();
			values.put(DBConstants.BUCKET_LABEL_FIELD_BUCKET_ID, bucketID);
			values.put(DBConstants.BUCKET_LABEL_FIELD_LABEL_NAME, LabelName);
			DBAdapter.openDataBase(context);
			long result = DBAdapter.db.insert(DBConstants.TABLE_BUCKET_LABEL,
					null, values);
			DBAdapter.closeDataBase();
			return result;
		}
	}

	public static ArrayList<BucketLabels> getBucketLabels(Context context) {
		synchronized (lock) {
			ArrayList<BucketLabels> bucket_label_List = new ArrayList<BucketLabels>();
			String[] columns = new String[] {
					DBConstants.BUCKET_LABEL_FIELD_BUCKET_ID,
					DBConstants.BUCKET_LABEL_FIELD_LABEL_NAME };
			String orderby = DBConstants.BUCKET_LABEL_FIELD_ID;
			DBAdapter.openDataBase(context);
			if (DBAdapter.db == null)
				return null;
			Cursor c = DBAdapter.db.query(true, DBConstants.TABLE_BUCKET_LABEL,
					columns, null, null, null, null, orderby + " ASC", null);

			try {
				if (c.getCount() == 0) {
					c.close();
					DBAdapter.closeDataBase();
					return bucket_label_List;
				}

				c.moveToFirst();
				do {
					BucketLabels bucket_label = new BucketLabels();
					bucket_label.setBucketID(c.getString(c
							.getColumnIndex(columns[0])));
					bucket_label.setLabelName(c.getString(c
							.getColumnIndex(columns[1])));
					bucket_label_List.add(bucket_label);
				} while (c.moveToNext());
				c.close();
				DBAdapter.closeDataBase();
				return bucket_label_List;
			} catch (Exception e) {
				System.out.println("Exception:   " + e.toString());
			} finally {
				if (!c.isClosed())
					c.close();
				DBAdapter.closeDataBase();
			}
			return null;
		}
	}

	public static long DeleteBucketLabel(Context context, int bucketID,
			String LabelName) {
		try {
			synchronized (lock) {
				DBAdapter.openDataBase(context);
				String query = "delete from " + DBConstants.TABLE_BUCKET_LABEL
						+ " where " + DBConstants.BUCKET_LABEL_FIELD_LABEL_NAME
						+ "=\'" + LabelName + "\' AND "
						+ DBConstants.BUCKET_LABEL_FIELD_BUCKET_ID + "="
						+ bucketID;
				long result = DBAdapter.db.delete(
						DBConstants.TABLE_BUCKET_LABEL,
						DBConstants.BUCKET_LABEL_FIELD_LABEL_NAME + "=\'"
								+ LabelName + "\' AND "
								+ DBConstants.BUCKET_LABEL_FIELD_BUCKET_ID
								+ "=" + bucketID, null);

				DBAdapter.closeDataBase();
				return result;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
	}

	public static long DeleteBucketLabel_ALL(Context context) {
		try {
			synchronized (lock) {
				DBAdapter.openDataBase(context);
				long result = DBAdapter.db.delete(
						DBConstants.TABLE_BUCKET_LABEL, null, null);
				DBAdapter.closeDataBase();
				return result;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
	}

	public static long UpdateCue(Context context, String moduleAddress,
			int cue_id, int cue_status) {
		try {
			synchronized (lock) {
				ContentValues values = new ContentValues();
				if (cue_id == 1)
					values.put(DBConstants.MODULE_FIELD_CUE_1, cue_status);
				else if (cue_id == 2)
					values.put(DBConstants.MODULE_FIELD_CUE_2, cue_status);
				else if (cue_id == 3)
					values.put(DBConstants.MODULE_FIELD_CUE_3, cue_status);
				else if (cue_id == 4)
					values.put(DBConstants.MODULE_FIELD_CUE_4, cue_status);
				else if (cue_id == 5)
					values.put(DBConstants.MODULE_FIELD_CUE_5, cue_status);
				else if (cue_id == 6)
					values.put(DBConstants.MODULE_FIELD_CUE_6, cue_status);
				else if (cue_id == 7)
					values.put(DBConstants.MODULE_FIELD_CUE_7, cue_status);
				else if (cue_id == 8)
					values.put(DBConstants.MODULE_FIELD_CUE_8, cue_status);
				else if (cue_id == 9)
					values.put(DBConstants.MODULE_FIELD_CUE_9, cue_status);
				else if (cue_id == 10)
					values.put(DBConstants.MODULE_FIELD_CUE_10, cue_status);
				else if (cue_id == 11)
					values.put(DBConstants.MODULE_FIELD_CUE_11, cue_status);
				else if (cue_id == 12)
					values.put(DBConstants.MODULE_FIELD_CUE_12, cue_status);
				else if (cue_id == 13)
					values.put(DBConstants.MODULE_FIELD_CUE_13, cue_status);
				else if (cue_id == 14)
					values.put(DBConstants.MODULE_FIELD_CUE_14, cue_status);
				else if (cue_id == 15)
					values.put(DBConstants.MODULE_FIELD_CUE_15, cue_status);
				else if (cue_id == 16)
					values.put(DBConstants.MODULE_FIELD_CUE_16, cue_status);
				else if (cue_id == 17)
					values.put(DBConstants.MODULE_FIELD_CUE_17, cue_status);
				else if (cue_id == 18)
					values.put(DBConstants.MODULE_FIELD_CUE_18, cue_status);

				DBAdapter.openDataBase(context);
				long result = DBAdapter.db.update(DBConstants.TABLE_MODULE,
						values, DBConstants.MODULE_FIELD_ADDRESS + "=\""
								+ moduleAddress + "\"", null);
				DBAdapter.closeDataBase();
				return result;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
	}
	
	public static long ClearCue(Context context) {
		try {
			synchronized (lock) {
				ContentValues values = new ContentValues();
					values.put(DBConstants.MODULE_FIELD_CUE_1, Constants.CUE_STATE_AVAILABLE);
					values.put(DBConstants.MODULE_FIELD_CUE_2, Constants.CUE_STATE_AVAILABLE);
					values.put(DBConstants.MODULE_FIELD_CUE_3, Constants.CUE_STATE_AVAILABLE);
					values.put(DBConstants.MODULE_FIELD_CUE_4, Constants.CUE_STATE_AVAILABLE);
					values.put(DBConstants.MODULE_FIELD_CUE_5, Constants.CUE_STATE_AVAILABLE);
					values.put(DBConstants.MODULE_FIELD_CUE_6, Constants.CUE_STATE_AVAILABLE);
					values.put(DBConstants.MODULE_FIELD_CUE_7, Constants.CUE_STATE_AVAILABLE);
					values.put(DBConstants.MODULE_FIELD_CUE_8, Constants.CUE_STATE_AVAILABLE);
					values.put(DBConstants.MODULE_FIELD_CUE_9, Constants.CUE_STATE_AVAILABLE);
					values.put(DBConstants.MODULE_FIELD_CUE_10, Constants.CUE_STATE_AVAILABLE);
					values.put(DBConstants.MODULE_FIELD_CUE_11, Constants.CUE_STATE_AVAILABLE);
					values.put(DBConstants.MODULE_FIELD_CUE_12, Constants.CUE_STATE_AVAILABLE);
					values.put(DBConstants.MODULE_FIELD_CUE_13, Constants.CUE_STATE_AVAILABLE);
					values.put(DBConstants.MODULE_FIELD_CUE_14, Constants.CUE_STATE_AVAILABLE);
					values.put(DBConstants.MODULE_FIELD_CUE_15, Constants.CUE_STATE_AVAILABLE);
					values.put(DBConstants.MODULE_FIELD_CUE_16, Constants.CUE_STATE_AVAILABLE);
					values.put(DBConstants.MODULE_FIELD_CUE_17, Constants.CUE_STATE_AVAILABLE);
					values.put(DBConstants.MODULE_FIELD_CUE_18, Constants.CUE_STATE_AVAILABLE);

				DBAdapter.openDataBase(context);
				long result = DBAdapter.db.update(DBConstants.TABLE_MODULE,
						values, DBConstants.MODULE_FIELD_ADDRESS + ">\""
								+ (-1) + "\"", null);
				DBAdapter.closeDataBase();
				return result;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
	}
}

package com.cobra.db;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Message;

public class DBAdapter extends SQLiteOpenHelper {

	/** The Android's default system path of your application database. */
	private static String DB_PATH = "/data/data/com.cobra/databases/";

	private final static String DB_NAME = "com.cobra.DB.sqlite";
	public static SQLiteDatabase db;
	private static Context myContext;

	private static int current_version;

//	private static int next_version = 3;
	//including Bucket_time, Bucket_status column in bucket table
	private static int next_version = 4;
	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 */

	public DBAdapter(Context context) {
		super(context, DB_NAME, null, 4);
		DBAdapter.myContext = context;
		DB_PATH = "/data/data/"
				+ myContext.getApplicationContext().getPackageName()
				+ "/databases/";
	}

	public void deleteDB() {
		myContext.deleteDatabase(DB_NAME);
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public void createDataBase() throws IOException {
		boolean dbExist = checkDataBase(true);
		if (!dbExist) {
			this.getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	private static void updateDatabase(SQLiteDatabase db, int current_version2,
			int next_version2) {
		if (current_version2 == 0) {
			// update db to version 1 here
			current_version2 = 1;
		}
		if (current_version2 == 1) {
			try {
				// update db to version 2 here
				String altertable1 = "ALTER TABLE "
						+ DBConstants.TABLE_BUCKET_LABEL + " ADD COLUMN "
						+ DBConstants.BUCKET_LABEL_FIELD_LABEL_NAME
						+ "	 VARCHAR";
				db.execSQL(altertable1);
			} catch (Exception ex) {
			}
			current_version2 = 2;
		}

		if (current_version2 == 2) {
			try {
				String alter_tbl_module_cue_1 = "ALTER TABLE "
						+ DBConstants.TABLE_MODULE + " ADD COLUMN "
						+ DBConstants.MODULE_FIELD_CUE_1 + "	 int";
				db.execSQL(alter_tbl_module_cue_1);
				
				String alter_tbl_module_cue_2 = "ALTER TABLE "
						+ DBConstants.TABLE_MODULE + " ADD COLUMN "
						+ DBConstants.MODULE_FIELD_CUE_2 + "	 int";
				db.execSQL(alter_tbl_module_cue_2);
				
				String alter_tbl_module_cue_3 = "ALTER TABLE "
						+ DBConstants.TABLE_MODULE + " ADD COLUMN "
						+ DBConstants.MODULE_FIELD_CUE_3 + "	 int";
				db.execSQL(alter_tbl_module_cue_3);
				
				String alter_tbl_module_cue_4 = "ALTER TABLE "
						+ DBConstants.TABLE_MODULE + " ADD COLUMN "
						+ DBConstants.MODULE_FIELD_CUE_4 + "	 int";
				db.execSQL(alter_tbl_module_cue_4);
				
				String alter_tbl_module_cue_5 = "ALTER TABLE "
						+ DBConstants.TABLE_MODULE + " ADD COLUMN "
						+ DBConstants.MODULE_FIELD_CUE_5 + "	 int";
				db.execSQL(alter_tbl_module_cue_5);
				
				String alter_tbl_module_cue_6 = "ALTER TABLE "
						+ DBConstants.TABLE_MODULE + " ADD COLUMN "
						+ DBConstants.MODULE_FIELD_CUE_6 + "	 int";
				db.execSQL(alter_tbl_module_cue_6);
				
				String alter_tbl_module_cue_7 = "ALTER TABLE "
						+ DBConstants.TABLE_MODULE + " ADD COLUMN "
						+ DBConstants.MODULE_FIELD_CUE_7 + "	 int";
				db.execSQL(alter_tbl_module_cue_7);
				
				String alter_tbl_module_cue_8 = "ALTER TABLE "
						+ DBConstants.TABLE_MODULE + " ADD COLUMN "
						+ DBConstants.MODULE_FIELD_CUE_8 + "	 int";
				db.execSQL(alter_tbl_module_cue_8);
				
				String alter_tbl_module_cue_9 = "ALTER TABLE "
						+ DBConstants.TABLE_MODULE + " ADD COLUMN "
						+ DBConstants.MODULE_FIELD_CUE_9 + "	 int";
				db.execSQL(alter_tbl_module_cue_9);
				
				String alter_tbl_module_cue_10 = "ALTER TABLE "
						+ DBConstants.TABLE_MODULE + " ADD COLUMN "
						+ DBConstants.MODULE_FIELD_CUE_10 + "	 int";
				db.execSQL(alter_tbl_module_cue_10);
				
				String alter_tbl_module_cue_11 = "ALTER TABLE "
						+ DBConstants.TABLE_MODULE + " ADD COLUMN "
						+ DBConstants.MODULE_FIELD_CUE_11 + "	 int";
				db.execSQL(alter_tbl_module_cue_11);
				
				String alter_tbl_module_cue_12 = "ALTER TABLE "
						+ DBConstants.TABLE_MODULE + " ADD COLUMN "
						+ DBConstants.MODULE_FIELD_CUE_12 + "	 int";
				db.execSQL(alter_tbl_module_cue_12);
				
				String alter_tbl_module_cue_13 = "ALTER TABLE "
						+ DBConstants.TABLE_MODULE + " ADD COLUMN "
						+ DBConstants.MODULE_FIELD_CUE_13 + "	 int";
				db.execSQL(alter_tbl_module_cue_13);
				
				String alter_tbl_module_cue_14 = "ALTER TABLE "
						+ DBConstants.TABLE_MODULE + " ADD COLUMN "
						+ DBConstants.MODULE_FIELD_CUE_14 + "	 int";
				db.execSQL(alter_tbl_module_cue_14);
				
				String alter_tbl_module_cue_15 = "ALTER TABLE "
						+ DBConstants.TABLE_MODULE + " ADD COLUMN "
						+ DBConstants.MODULE_FIELD_CUE_15 + "	 int";
				db.execSQL(alter_tbl_module_cue_15);
				
				String alter_tbl_module_cue_16 = "ALTER TABLE "
						+ DBConstants.TABLE_MODULE + " ADD COLUMN "
						+ DBConstants.MODULE_FIELD_CUE_16 + "	 int";
				db.execSQL(alter_tbl_module_cue_16);
				
				String alter_tbl_module_cue_17 = "ALTER TABLE "
						+ DBConstants.TABLE_MODULE + " ADD COLUMN "
						+ DBConstants.MODULE_FIELD_CUE_17 + "	 int";
				db.execSQL(alter_tbl_module_cue_17);
				
				String alter_tbl_module_cue_18 = "ALTER TABLE "
						+ DBConstants.TABLE_MODULE + " ADD COLUMN "
						+ DBConstants.MODULE_FIELD_CUE_18 + "	 int";
				db.execSQL(alter_tbl_module_cue_18);

			} catch (Exception ex) {
			}
			current_version2 = 3;
		}
		if (current_version2 == 3) {
			try {
				String createquery = "CREATE TABLE "+DBConstants.TABLE_TIME+" ("
						+ DBConstants.TIME_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
						+ DBConstants.TIME_FIELD_TIME 
						+ "	 VARCHAR);";
				db.execSQL(createquery);
				
				String alterquery1 = "ALTER TABLE "
						+ DBConstants.TABLE_BUCKET + " ADD COLUMN "
						+ DBConstants.BUCKET_FIELD_TIME
						+ "	 int";
				db.execSQL(alterquery1);
				
				String alterquery2 = "ALTER TABLE "
						+ DBConstants.TABLE_BUCKET + " ADD COLUMN "
						+ DBConstants.BUCKET_FIELD_STATUS
						+ "	 VARCHAR";
				db.execSQL(alterquery2);
			} catch (Exception ex) {
				int i=0;
				i++;
			}
			current_version2 = 4;
		}
		db.setVersion(next_version2);
	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);

		} catch (SQLiteException e) {
			// database does't exist yet.
			System.out.print("SQLiteException   " + e.toString());
		}
		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	private static boolean checkDataBase(boolean isOld) {
		SQLiteDatabase checkDB = null;
		// File f = new File(DB_PATH + DB_NAME);
		// // File dbFile = getDatabasePath(DB_PATH + DB_NAME);
		// String ff = f.getAbsolutePath();

		try {

			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);
			current_version = checkDB.getVersion();
			if (next_version > current_version) {
				updateDatabase(checkDB, current_version, next_version);
			}

			String path = checkDB.getPath();
			path += "";

		} catch (SQLiteException e) {
			// database does't exist yet.
			System.out.print("SQLiteException   " + e.toString());
		}
		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? true : false;

	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {
		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DB_NAME);
		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;
		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);
		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	public synchronized static SQLiteDatabase openDataBase(Context myContext) {
		try {
			// Open the database
			String myPath = "/data/data/"
					+ myContext.getApplicationContext().getPackageName()
					+ "/databases/" + DB_NAME;
			if (db != null && db.isOpen()) {
				return db;
			}
			boolean is = checkDataBase(true);
			db = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);

			if (!db.isOpen()) {
				// Alert user that unable to open databse for XYZ reason
				// Close the app after that
				progressHandler1.sendEmptyMessage(0);
			}
			return db;
		} catch (Exception e) {
			System.out.println("Data Base Open Exception:  " + e.getMessage());
			// msgg = e.getMessage();
			// progressHandler.sendEmptyMessage(0);
		}
		return null;
	}

	public synchronized static void closeDataBase() throws SQLException {
		try {
			if (db != null && db.isOpen())
				db.close();
		} catch (Exception e) {
			System.out.println("no database connected to close");
		}
	}

	@Override
	public synchronized void close() {
		// if(db != null && db.isOpen())
		// db.close();
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	static String msgg = "";
	static Handler progressHandler1 = new Handler() {
		public void handleMessage(Message msg) {
			AlertDialog.Builder alert = new AlertDialog.Builder(myContext);
			alert.setMessage("Database not open");
			alert.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog1,
								int whichButton) {
							System.exit(1);
							return;
						}
					}).create().show();
			return;
		}
	};
}

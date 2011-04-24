/**********************************************************************************************************************************************************************
****** AUTO GENERATED FILE BY ANDROID SQLITE HELPER SCRIPT BY FEDERICO PAOLINELLI. ANY CHANGE WILL BE WIPED OUT IF THE SCRIPT IS PROCESSED AGAIN. *******
**********************************************************************************************************************************************************************/
package com.whiterabbit.checkers.boards;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class CheckersStorage{
    
    private static final String TAG = "CheckersStorage";
    public static final long FOREVER_TIME = 0;

    private static final String DATABASE_NAME = "CheckersStorageDb.db";
    private static final int DATABASE_VERSION =21;


    // Variable to hold the database instance
    protected SQLiteDatabase mDb;
    // Context of the application using the database.
    private final Context mContext;
    // Database open/upgrade helper
    private MyDbHelper mDbHelper;
    
    public CheckersStorage(Context context) {
        mContext = context;
        mDbHelper = new MyDbHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    public CheckersStorage open() throws SQLException { 
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
                                                     
    public void close() {
        mDb.close();
    }

	// -------------- BOARD DEFINITIONS ------------

	public static final String BOARD_TABLE = "Board";
	public static final String BOARD_NAME_KEY = "Name";
	protected static final int BOARD_NAME_COLUMN = 1;
	public static final String BOARD_DUMP_KEY = "Dump";
	protected static final int BOARD_DUMP_COLUMN = 2;
	public static final String BOARD_SAVEDDATE_KEY = "SavedDate";
	protected static final int BOARD_SAVEDDATE_COLUMN = 3;
	public static final String BOARD_WIDTH_KEY = "Width";
	protected static final int BOARD_WIDTH_COLUMN = 4;
	public static final String BOARD_HEIGTH_KEY = "Heigth";
	protected static final int BOARD_HEIGTH_COLUMN = 5;
	public static final String BOARD_SCORE_KEY = "Score";
	protected static final int BOARD_SCORE_COLUMN = 6;
	public static final String BOARD_TIME_KEY = "Elapsed";
	protected static final int BOARD_TIME_COLUMN = 7;
	public static final String BOARD_ROW_ID = "_id";

	// -------------- BOARDSCORE DEFINITIONS ------------

	public static final String BOARDSCORE_TABLE = "BoardScore";
	public static final String BOARDSCORE_NAME_KEY = "Name";
	protected static final int BOARDSCORE_NAME_COLUMN = 1;
	public static final String BOARDSCORE_MAXSCORE_KEY = "MaxScore";
	protected static final int BOARDSCORE_MAXSCORE_COLUMN = 2;
	public static final String BOARDSCORE_MINTIME_KEY = "MinTime";
	protected static final int BOARDSCORE_MINTIME_COLUMN = 3;
	public static final String BOARDSCORE_ROW_ID = "_id";



	// -------- TABLES CREATION ----------

	// Board CREATION 
	private static final String DATABASE_BOARD_CREATE = "create table " + BOARD_TABLE + " (" + 
				 BOARD_ROW_ID + " integer primary key autoincrement" + ", " + 
				 BOARD_NAME_KEY + " text  " + ", " + 
				 BOARD_DUMP_KEY + " text  " + ", " + 
				 BOARD_SAVEDDATE_KEY + " integer  " + ", " + 
				 BOARD_WIDTH_KEY + " integer  " + ", " + 
				 BOARD_HEIGTH_KEY + " integer  " + ", " + 
				 BOARD_SCORE_KEY + " integer  " + ", " +
				 BOARD_TIME_KEY + " integer  " + ");";


	// BoardScore CREATION 
	private static final String DATABASE_BOARDSCORE_CREATE = "create table " + BOARDSCORE_TABLE + " (" + 
				 BOARDSCORE_ROW_ID + " integer primary key autoincrement" + ", " + 
				 BOARDSCORE_NAME_KEY + " text  " + ", " + 
				 BOARDSCORE_MAXSCORE_KEY + " integer  " + ", " +
				 BOARDSCORE_MINTIME_KEY + " integer  " +
				 ");";




	// -------------- BOARD HELPERS ------------------
	public long addBoard(String Name, String Dump, Date SavedDate, Long Width, Long Heigth, Long Score, Long elapsed)
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put(BOARD_NAME_KEY, Name);
		contentValues.put(BOARD_DUMP_KEY, Dump);
		contentValues.put(BOARD_SAVEDDATE_KEY, SavedDate.getTime());
		contentValues.put(BOARD_WIDTH_KEY, Width);
		contentValues.put(BOARD_HEIGTH_KEY, Heigth);
		contentValues.put(BOARD_SCORE_KEY, Score);
		contentValues.put(BOARD_TIME_KEY, elapsed);
		return mDb.insert(BOARD_TABLE, null, contentValues);
	
	}

	public long updateBoard(long rowIndex, String Name, String Dump, Date SavedDate, Long Width, Long Heigth, Long Score, Long elapsed)
	{
		String where = BOARD_ROW_ID + " = " + rowIndex;
		ContentValues contentValues = new ContentValues();
		contentValues.put(BOARD_NAME_KEY, Name);
		contentValues.put(BOARD_DUMP_KEY, Dump);
		contentValues.put(BOARD_SAVEDDATE_KEY, SavedDate.getTime());
		contentValues.put(BOARD_WIDTH_KEY, Width);
		contentValues.put(BOARD_HEIGTH_KEY, Heigth);
		contentValues.put(BOARD_SCORE_KEY, Score);
		contentValues.put(BOARD_TIME_KEY, elapsed);
		return mDb.update(BOARD_TABLE, contentValues, where, null);
	
	}

	public boolean removeBoard(Long rowIndex)
	{
		return mDb.delete(BOARD_TABLE, BOARD_ROW_ID + " = " + rowIndex, null) > 0;
	}

	public boolean removeAllBoard()
	{
		return mDb.delete(BOARD_TABLE, null, null) > 0;
	}

	public Cursor getAllBoard()
	{
		return mDb.query(BOARD_TABLE, new String[] {
					BOARD_ROW_ID,
					BOARD_NAME_KEY,
					BOARD_DUMP_KEY,
					BOARD_SAVEDDATE_KEY,
					BOARD_WIDTH_KEY,
					BOARD_HEIGTH_KEY,
					BOARD_SCORE_KEY,
					BOARD_TIME_KEY}, null, null, null, null, null);
	}

	public Cursor getBoard(long rowIndex)
	{
		Cursor res = mDb.query(BOARD_TABLE, new String[] {
					BOARD_ROW_ID,
					BOARD_NAME_KEY,
					BOARD_DUMP_KEY,
					BOARD_SAVEDDATE_KEY,
					BOARD_WIDTH_KEY,
					BOARD_HEIGTH_KEY,
					BOARD_SCORE_KEY,
					BOARD_TIME_KEY}, BOARD_ROW_ID + " = " + rowIndex, null, null, null, null);
		if(res != null){
			res.moveToFirst();
		}
		return res;
	}

	// -------------- BOARDSCORE HELPERS ------------------
	public long addBoardScore(String Name, Long MaxScore, Long MinTime)
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put(BOARDSCORE_NAME_KEY, Name);
		contentValues.put(BOARDSCORE_MAXSCORE_KEY, MaxScore);
		contentValues.put(BOARDSCORE_MINTIME_KEY, MinTime);
		return mDb.insert(BOARDSCORE_TABLE, null, contentValues);
	
	}

	public long updateBoardScore(long rowIndex, String Name, Long MaxScore, Long MinTime)
	{
		String where = BOARDSCORE_ROW_ID + " = " + rowIndex;
		ContentValues contentValues = new ContentValues();
		contentValues.put(BOARDSCORE_NAME_KEY, Name);
		contentValues.put(BOARDSCORE_MAXSCORE_KEY, MaxScore);
		contentValues.put(BOARDSCORE_MINTIME_KEY, MinTime);
		return mDb.update(BOARDSCORE_TABLE, contentValues, where, null);
	
	}

	public boolean removeBoardScore(Long rowIndex)
	{
		return mDb.delete(BOARDSCORE_TABLE, BOARDSCORE_ROW_ID + " = " + rowIndex, null) > 0;
	}

	public boolean removeAllBoardScore()
	{
		return mDb.delete(BOARDSCORE_TABLE, null, null) > 0;
	}

	public Cursor getAllBoardScore()
	{
		return mDb.query(BOARDSCORE_TABLE, new String[] {
					BOARDSCORE_ROW_ID,
					BOARDSCORE_NAME_KEY,
					BOARDSCORE_MINTIME_KEY,
					BOARDSCORE_MAXSCORE_KEY}, null, null, null, null, null);
	}

	public Cursor getBoardScore(long rowIndex)
	{
		Cursor res = mDb.query(BOARDSCORE_TABLE, new String[] {
					BOARDSCORE_ROW_ID,
					BOARDSCORE_NAME_KEY,
					BOARDSCORE_MAXSCORE_KEY,BOARDSCORE_MINTIME_KEY}, BOARDSCORE_ROW_ID + " = " + rowIndex, null, null, null, null);
		if(res != null){
			res.moveToFirst();
		}
		return res;
	}

	private static void alterDump(SQLiteDatabase db){
		String alter = String.format("alter table %s add column %s;", CheckersStorage.BOARD_TABLE, CheckersStorage.BOARD_TIME_KEY);
        db.execSQL(alter);
        
        String alter1 = String.format("alter table %s add column %s;", CheckersStorage.BOARDSCORE_TABLE, CheckersStorage.BOARDSCORE_MINTIME_KEY);
		db.execSQL(alter1);

	}


    private static class MyDbHelper extends SQLiteOpenHelper {
    
        public MyDbHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // Called when no database exists in disk and the helper class needs
        // to create a new one. 
        @Override
        public void onCreate(SQLiteDatabase db) {      
            db.execSQL(DATABASE_BOARD_CREATE);
			db.execSQL(DATABASE_BOARDSCORE_CREATE);
			
        }

        // Called when there is a database version mismatch meaning that the version
        // of the database on disk needs to be upgraded to the current version.
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Log the version upgrade.
            Log.w(TAG, "Upgrading from version " + 
                        oldVersion + " to " +
                        newVersion + ", which will destroy all old data");
            
            // Upgrade the existing database to conform to the new version. Multiple 
            // previous versions can be handled by comparing _oldVersion and _newVersion
            // values.
            //if(oldVersion == 7 && newVersion == 8){
            	alterDump(db);
            /*}else{
	            // The simplest case is to drop the old table and create a new one.
	            db.execSQL("DROP TABLE IF EXISTS " + BOARD_TABLE + ";");
				db.execSQL("DROP TABLE IF EXISTS " + BOARDSCORE_TABLE + ";");
	            // Create a new one.
	            onCreate(db);
            }*/

        }
    }
     
    /** Dummy object to allow class to compile */
}
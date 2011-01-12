/**********************************************************************************************************************************************************************
****** AUTO GENERATED FILE BY ANDROID SQLITE HELPER SCRIPT BY FEDERICO PAOLINELLI. ANY CHANGE WILL BE WIPED OUT IF THE SCRIPT IS PROCESSED AGAIN. *******
**********************************************************************************************************************************************************************/
package com.fede.checkers.boards;
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
    private static final String DATABASE_NAME = "CheckersStorageDb.db";
    private static final int DATABASE_VERSION = 1;


    // Variable to hold the database instance
    SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private myDbHelper dbHelper;
    public CheckersStorage(Context _context) {
        context = _context;
        dbHelper = new myDbHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    public CheckersStorage open() throws SQLException { 
        db = dbHelper.getWritableDatabase();
        return this;
    }
                                                     
    public void close() {
        db.close();
    }

	// -------------- BOARD DEFINITIONS ------------

	 static final String BOARD_TABLE = "Board";
	 static final String BOARD_NAME_KEY = "Name";
	 static final int BOARD_NAME_COLUMN = 1;
	 static final String BOARD_DUMP_KEY = "Dump";
	 static final int BOARD_DUMP_COLUMN = 2;
	 static final String BOARD_SAVEDDATE_KEY = "SavedDate";
	 static final int BOARD_SAVEDDATE_COLUMN = 3;
	 static final String BOARD_WIDTH_KEY = "Width";
	 static final int BOARD_WIDTH_COLUMN = 4;
	 static final String BOARD_HEIGTH_KEY = "Heigth";
	 static final int BOARD_HEIGTH_COLUMN = 5;
	 static final String BOARD_ROW_ID = "_id";



	// -------- TABLES CREATION ----------

	// Board CREATION 
	private static final String DATABASE_BOARD_CREATE = "create table " + BOARD_TABLE + " (" + BOARD_ROW_ID + " integer primary key autoincrement"+ ", " + 
				 BOARD_NAME_KEY + " text "+ ", " + 
				 BOARD_DUMP_KEY + " text "+ ", " + 
				 BOARD_SAVEDDATE_KEY + " integer "+ ", " + 
				 BOARD_WIDTH_KEY + " integer "+ ", " + 
				 BOARD_HEIGTH_KEY + " integer " + ");";




	// -------------- BOARD HELPERS ------------------
	public long addBoard(String Name,String Dump,Date SavedDate,Long Width,Long Heigth)
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put(BOARD_NAME_KEY, Name);
		contentValues.put(BOARD_DUMP_KEY, Dump);
		contentValues.put(BOARD_SAVEDDATE_KEY, SavedDate.getTime());
		contentValues.put(BOARD_WIDTH_KEY, Width);
		contentValues.put(BOARD_HEIGTH_KEY, Heigth);
		return db.insert(BOARD_TABLE, null, contentValues);
	
	}

	public long updateBoard(long _rowIndex, String Name,String Dump,Date SavedDate,Long Width,Long Heigth)
	{
		String where = BOARD_ROW_ID+ " = " + _rowIndex;
		ContentValues contentValues = new ContentValues();
		contentValues.put(BOARD_NAME_KEY, Name);
		contentValues.put(BOARD_DUMP_KEY, Dump);
		contentValues.put(BOARD_SAVEDDATE_KEY, SavedDate.getTime());
		contentValues.put(BOARD_WIDTH_KEY, Width);
		contentValues.put(BOARD_HEIGTH_KEY, Heigth);
		return db.update(BOARD_TABLE, contentValues, where, null);
	
	}

	public boolean removeBoard(Long _rowIndex)
	{
		return db.delete(BOARD_TABLE, BOARD_ROW_ID+ "=" + _rowIndex, null) > 0;
	}

	public boolean removeAllBoard(Long _rowIndex)
	{
		return db.delete(BOARD_TABLE, null, null) > 0;
	}

	public Cursor getAllBoard()
	{
		return db.query(BOARD_TABLE, new String[] {BOARD_ROW_ID,
					BOARD_NAME_KEY,
					BOARD_DUMP_KEY,
					BOARD_SAVEDDATE_KEY,
					BOARD_WIDTH_KEY,
					BOARD_HEIGTH_KEY}, null, null, null, null, null);
	}

	public Cursor getBoard(long _rowIndex)
	{
		Cursor res = db.query(BOARD_TABLE, new String[] {BOARD_ROW_ID,
					BOARD_NAME_KEY,
					BOARD_DUMP_KEY,
					BOARD_SAVEDDATE_KEY,
					BOARD_WIDTH_KEY,
					BOARD_HEIGTH_KEY}, BOARD_ROW_ID + " = " + _rowIndex, null, null, null, null);
		if(res != null){
			res.moveToFirst();
		}
		return res;
	}




    private static class myDbHelper extends SQLiteOpenHelper {
        public myDbHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // Called when no database exists in disk and the helper class needs
        // to create a new one. 
        @Override
        public void onCreate(SQLiteDatabase _db) {      
            _db.execSQL(DATABASE_BOARD_CREATE);
		
        }

        // Called when there is a database version mismatch meaning that the version
        // of the database on disk needs to be upgraded to the current version.
        @Override
        public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
          // Log the version upgrade.
          Log.w("TaskDBAdapter", "Upgrading from version " + 
                                 _oldVersion + " to " +
                                 _newVersion + ", which will destroy all old data");
            
          // Upgrade the existing database to conform to the new version. Multiple 
          // previous versions can be handled by comparing _oldVersion and _newVersion
          // values.

          // The simplest case is to drop the old table and create a new one.
          _db.execSQL("DROP TABLE IF EXISTS " + BOARD_TABLE + ";");
		
          // Create a new one.
          onCreate(_db);
        }
      }
     
      /** Dummy object to allow class to compile */
}
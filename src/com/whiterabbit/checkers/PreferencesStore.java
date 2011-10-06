package com.whiterabbit.checkers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesStore {
	
	
	public static final String PREF_NAME = "com.whiterabbit.pegdroid.prefs";
	public static final String COUNT = "Count";
	
	
	public static Long getCount(Context c)
	{
		int mode = Activity.MODE_PRIVATE;
		SharedPreferences mySharedPreferences = c.getSharedPreferences(PREF_NAME, mode);		
		return mySharedPreferences.getLong(COUNT, 0);
	}
	
	public static void setCount(Long startTime, Context c)
	{
		int mode = Activity.MODE_PRIVATE;
		SharedPreferences mySharedPreferences = c.getSharedPreferences(PREF_NAME, mode);		
		SharedPreferences.Editor editor = mySharedPreferences.edit();	
		editor.putLong(COUNT, startTime);
		editor.commit();

	}

	
}

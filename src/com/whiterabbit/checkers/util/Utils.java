package com.whiterabbit.checkers.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.immersion.uhl.Launcher;

public class Utils {
	public static final void vibrate(Launcher launcher, int effect, Context c){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
		if(prefs.getBoolean("PREF_CHECK_BOX_VIBRATION", true)){
			try{
				launcher.play(Launcher.IMPACT_METAL_66);
			}catch(Exception e){
				
			}
		}
	}
	
	public static final void playButtonPressed(Launcher launcher, Context c){
		vibrate(launcher, Launcher.SHARP_CLICK_33, c);
	}

}

/*******************************************************************************
 * Copyright 2011 Federico Paolinelli
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.whiterabbit.checkers;

import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.util.DisplayMetrics;
 

public class Constants {
 
    public static final int BALL_SIZE = 64;
    public static final int BALL_DISTANCE = BALL_SIZE + BALL_SIZE / 10;
    
    public static final int HOLE_LAYER = 0;
    public static final int BALL_LAYER = 1;
    public static final int SCORE_LAYER = 2;
    
    public static final int CAMERA_WIDTH = 480;
    public static final int ADMOB_HEIGHT = 40;
    
    
    public static final String BOARD_NAME_INTENT = "com.checkers.BOARD_NAME";
    public static final String BOARD_RESTORE_INTENT = "com.checkers.BOARD_RESTORE";
    
    public static int getHeight(int width, BaseGameActivity g){
    	return 720;
    	/*
        DisplayMetrics dm = new DisplayMetrics();       
    
        g.getWindowManager().getDefaultDisplay().getMetrics(dm);         
        return width * dm.heightPixels/dm.widthPixels;*/
    }
    
    
    
    public static int getOffset(int width, BaseGameActivity g){
        DisplayMetrics dm = new DisplayMetrics();       
        
        g.getWindowManager().getDefaultDisplay().getMetrics(dm);         
        return width * ADMOB_HEIGHT /dm.widthPixels;
    }
}

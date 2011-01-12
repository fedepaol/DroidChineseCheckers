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
package com.fede.checkers.boards;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;


public class CheckersDbHelper extends CheckersStorage {
    public static final String PREF_NAME = "Preferences";
    public static final String LAST_BOARD_USED = "LastBoard";
    
    public CheckersDbHelper(Context context) {
        super(context);
    }
    
    public Cursor getSavedBoard(String name){
       Cursor res = db.query(BOARD_TABLE, new String[] {BOARD_ROW_ID,
                    BOARD_NAME_KEY,
                    BOARD_DUMP_KEY,
                    BOARD_SAVEDDATE_KEY,
                    BOARD_WIDTH_KEY,
                    BOARD_HEIGTH_KEY}, BOARD_NAME_KEY + " = '" + name + "'", null, null, null, null);
        if(res != null){
            res.moveToFirst();
        }
        
        return res;
    }
    
    
    public boolean removeBoard(String name)
    {
        return db.delete(BOARD_TABLE, BOARD_NAME_KEY+ "= '" + name + "'", null) > 0;
    }
    
    public static void setLastBoardUsed(String boardName, Context ctx){
        int mode = Context.MODE_PRIVATE;
        SharedPreferences mySharedPreferences = ctx.getSharedPreferences(PREF_NAME, mode);        
        SharedPreferences.Editor editor = mySharedPreferences.edit();   
        editor.putString(LAST_BOARD_USED, boardName);
        editor.commit();
    }
    
    public static void resetLastBoard(Context ctx){
        setLastBoardUsed("", ctx);
    }
    
    
    

}

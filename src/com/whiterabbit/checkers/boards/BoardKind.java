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
package com.whiterabbit.checkers.boards;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;


public abstract class BoardKind {
    String mSavedDump;
    Date mSavedDate;
    Long mSavedScore;
    long mMaxScore = 0;
    Long rowId;
    Boolean mLoaded = false;
    long mBallNumber = 0;
    
    public String getDump(){
    	return getDumpFromMap(getMap());
    }
    
    public int getWidth(){        
            return getWidthFromMap(getMap());
    }
    
    
    public int getHeight(){
        return getHeightFromMap(getMap());
    }
    
    
    public abstract int getImageResource();
    public abstract String getName();
    
    public void saveDump(String dump, long score, Context ctx){
        CheckersDbHelper db = new CheckersDbHelper(ctx);
        db.open();
        Cursor saved = db.getSavedBoard(getName());
        if(saved.getCount() > 0){
            db.updateBoard(saved.getLong(0), 
                    getName(), dump, new Date(), Long.valueOf(getWidth()), 
                    Long.valueOf(getHeight()),
                    Long.valueOf(score));
        }else{
            db.addBoard(getName(), dump, new Date(), Long.valueOf(getWidth()), 
                    Long.valueOf(getHeight()),
                    Long.valueOf(score));
        }
        db.close();
        saved.close();        
    }   
    
    public String getSavedDump(){
        return mSavedDump;
    }
    
    public Date getSavedDate(){
        return mSavedDate;
    }
    
    public Long getMaxScore(){
        return mMaxScore;
    }
    
    public Long getSavedScore(){
        return mSavedScore;
    }
    
    /**
     * Loads saved instance from sql lite
     * @param ctx
     * @param forceLoad forces load even if the istance was already loaded
     * @return
     */
    public Boolean load(Context ctx, boolean forceLoad){
        if(mLoaded && !forceLoad){    // if already loaded doesn't need to load it again
            return true;
        }
        CheckersDbHelper db = new CheckersDbHelper(ctx);
        db.open();
        
        mMaxScore = Long.valueOf(db.getBoardMaxScore(getName()));

        Cursor saved = db.getSavedBoard(getName());
        if(saved.getCount() > 0){
            rowId = saved.getLong(0);
            mSavedDump =  saved.getString(CheckersStorage.BOARD_DUMP_COLUMN);
            mLoaded = true;
            mSavedDate = new Date(saved.getInt(CheckersStorage.BOARD_SAVEDDATE_COLUMN)); 
            mSavedScore = saved.getLong(CheckersStorage.BOARD_SCORE_COLUMN); 
            saved.close();
            db.close();
            return true;
        }
        saved.close();
        db.close();
        return false;       
    }
    
    /**
     * Loads saved instance from sql lite
     * @param ctx
     * @return
     */
    public Boolean load(Context ctx){
        return load(ctx, false);
    }
    
    /**
     * Deletes saved instance
     * @param ctx
     */
    public void delete(Context ctx){
        CheckersDbHelper db = new CheckersDbHelper(ctx);
        db.open();
        db.removeBoard(this.getName());
        db.close();
    }
    
    
    /** 
     * Converts a char matrix into a board. Deserializes board
     * @param map
     * @return
     */
    String getDumpFromMap(char[][] map) {
        StringBuffer res = new StringBuffer();
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[i].length; j++){
                res.append(map[i][j]);
            }
        }    
        return res.toString();
    }

    
    /** 
     * Returns the number of balls from a map
     * @param map
     * @return
     */
    long getBallNumber() {
    	if(mBallNumber != 0){
    		return mBallNumber;
    	}
    	
    	int res = 0;
    	char[][] map = getMap();
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[i].length; j++){
                if(map[i][j] == '1'){
                	res++;
                }
            }
        }    
        mBallNumber = res;
        return res;
    }
    
    
    /** 
     * Returns remaining balls from the given score
     * @param score
     * @return
     */
    public long getRemainingBallsFromScore(long score){
    	return getBallNumber() - score;
    }
    
    
    /**
     * Returns min number of balls 
     * @return
     */
    public long getMinRemainingBalls(){
    	return getBallNumber() - this.getMaxScore();
    }
    
    
    public int getWidthFromMap(char[][] map) {
        return map[0].length;   // Here I am assuming squared maps
    }
    
    public int getHeightFromMap(char[][] map) {
        return map.length;  
    }
    
    /** ADD HERE NEW BOARDS CREATION */
    /** 
     *  Build method. Returns a table form the given name
     */
    public static BoardKind getBoardFromName(String name){
        if(name.equals(BoardClassicExtended.NAME)){
            return new BoardClassicExtended();
        }
        if(name.equals(BoardClassic.NAME)){
            return new BoardClassic();
        }
        if(name.equals(BoardStar.NAME)){
            return new BoardStar();
        }
        if(name.equals(BoardClassicEng.NAME)){
            return new BoardClassicEng();
        }
        /*if(name.equals(BoardS.NAME)){
            return new BoardS();
        }*/
        if(name.equals(BoardAsymmetrical.NAME)){
            return new BoardAsymmetrical();
        }
        
        return null;
    }
    
    public static ArrayList<BoardKind> getAllBoards(){
        ArrayList<BoardKind> res = new ArrayList<BoardKind>();
        res.add(getBoardFromName(BoardClassicExtended.NAME));
        res.add(getBoardFromName(BoardAsymmetrical.NAME));
        res.add(getBoardFromName(BoardClassic.NAME));
        res.add(getBoardFromName(BoardClassicEng.NAME));
        res.add(getBoardFromName(BoardStar.NAME));
        //res.add(getBoardFromName(BoardS.NAME));

        return res;
    }
    
    
    abstract char[][] getMap();
    
    /**
     * Returns scoreloop mode
     * @return
     */
    public abstract int getMode();
    
    
    public abstract String getAchievement();
    
    
}

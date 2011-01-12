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
package com.fede.checkers.board;
import com.fede.checkers.Constants;
import com.fede.checkers.board.BoardCell.CantFillException;
import com.fede.checkers.boards.BoardType;
import com.fede.checkers.ui.CheckersSpriteFactory;
import com.fede.checkers.ui.ChineseCheckers;

import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;


/**
 * Checkers board
 * @author fede
 *
 */
public class AndEngineBoard {  
    public class CantMoveException extends Exception{
        
    }
    
    
    private final ChineseCheckers mGame;
    private  float mVerticalPadding = 0;
    private  float mHOrizontalPadding = 0;
    private float mDistanceFromBottom = 50;
    private final float mYDistance, mXDistance;
    
    private  BoardCell[][] mBoard;
    private  Long mWidth;
    private  Long mHeight;
    private final Scene mScene;

    
    /**
     * Returns a board cell built up from the given kind
     * @param x board position
     * @param y board position
     * @param kind char kind
     * @return new board cell object
     */
    private BoardCell fillCellFromChar(int x, int y, char kind){
        switch (kind){
        case '0':
            return new FilledBoardCell(x,y);
        case '1':
            return new FillableBoardCell(x,y, true);
        case 'X':
            return new FillableBoardCell(x,y,false);
        }
        return new FilledBoardCell(x,y);
    }
    
    
    /**
     * Builds board from serialized string
     * @param dump  the dump
     * @param f sprite factory
     * @param width board width
     * @param height board height
     */
    public void buildFromString(String dump, CheckersSpriteFactory f, int width, int height){
        mBoard = new BoardCell[width][];
        for(int i = 0; i < width; i++){
            mBoard[i] = new BoardCell[height];
            for(int j = 0; j < height; j++){
                char c = dump.charAt(i * width + j);
                mBoard[i][j] = fillCellFromChar(i, j, c);
                mBoard[i][j].buildSprites(f, mScene, this);
            }
        }  
    }
    
    

    /**
     * Getter
     * @return
     */
    public Long getWidth() {
        return mWidth;
    }
    
    /**
     * Getter
     * @return
     */
    public Long getHeight() {
        return mHeight;
    }
    
    /**
     * Returns canvas position from board position
     */
    public float getYCoordFromBoardX(int x){
        return (mYDistance * x) + mVerticalPadding - mDistanceFromBottom;
    }
    
    /**
     * Returns canvas position from board position
     */
    public float getXCoordFromBoardY(int y){
        return (mXDistance * y) + mHOrizontalPadding;
    }
    
    /**
     * Returns board position from canvas position
     */
    public int getBoardYfromXCoord(float x){
        return (int)((x - mHOrizontalPadding + (mXDistance / 2)) / mXDistance ); 
    }
    
    /**
     * Returns board position from canvas position
     */
    public int getBoardXfromYCoord(float y){
        return (int)((y- mVerticalPadding + mDistanceFromBottom + (mYDistance / 2)) / mYDistance )  ; 
    }
    
    
    /**
     * Moving ball event
     * @param startX    starting board position
     * @param startY    starting board position
     * @param destX     destination board position
     * @param destY     destination board position
     * @throws CantMoveException
     * @throws CantFillException
     */
    public void moveBall(int startX, int startY, int destX, int destY) throws CantMoveException, CantFillException{
        BoardCell startCell = mBoard[startX][startY];
        BoardCell releaseCell = mBoard[destX][destY];
        
        int middleX = 0, middleY = 0;
        
        if(startX == destX){
            middleX = startX;
            int diff = destY - startY;
            if(Math.abs(diff) != 2){
                throw new CantMoveException();
            }
            middleY = startY + diff/2;
        }
        
        if(startY == destY){
            middleY = startY;
            int diff = destX - startX;
            if(Math.abs(diff) != 2){
                throw new CantMoveException();
            }
            middleX = startX + diff/2;
        }
        BoardCell middleCell = mBoard[middleX][middleY];
        
        if(!startCell.hasBall() || !middleCell.hasBall() || releaseCell.hasBall()){
            throw new CantMoveException();
        }
        startCell.getBall();
        middleCell.getBall();
        releaseCell.putBall();
        
        releaseCell.setBallSprite(startCell.getBallSprite());
        startCell.eraseBallSprite();
        
        final Sprite toDel = middleCell.getBallSprite();
        middleCell.eraseBallSprite();
        
        mGame.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {                
                mScene.unregisterTouchArea(toDel);
                mScene.getTopLayer().removeEntity(toDel);                
            }
        });
        
        if(checkStall()){
            mGame.onGameStall();
        }
        
    }
    

    
    
    
    public AndEngineBoard(int width, int height, BoardType b, Boolean buildFromSaved, Scene s, CheckersSpriteFactory f, ChineseCheckers game){
        mScene = s;
        mGame = game;
        mHeight = Long.valueOf(b.getHeight());
        mWidth = Long.valueOf(b.getWidth());
        
        mYDistance = Constants.BALL_DISTANCE;
        mXDistance = Constants.BALL_DISTANCE;
        
        mVerticalPadding = (height - mYDistance * (mWidth - 1)) / 2;  
        mHOrizontalPadding = (width - mXDistance * (mHeight - 1)) / 2;
        

        if(buildFromSaved && b.load(game)){
            this.buildFromString(b.getSavedDump(), f, b.getWidth(), b.getHeight());
        }else{
            this.buildFromString(b.getDump(), f, b.getWidth(), b.getHeight());
        }

        
    }
    
    /**
     * Says if the ball can be moved from near cell to far cell
     * @return
     */
    private Boolean canMoveBall(BoardCell near, BoardCell far){
        return (near.hasBall() && !far.hasBall() && far.fillableBall());
    }
    
    /**
     * Says if the ball in i,j position can be moved
     */
    private Boolean canMove(int i, int j){
        BoardCell nearCell;
        BoardCell farCell;
        if(i > 1){
            nearCell = mBoard[i-1][j];
            farCell = mBoard[i-2][j];
            if(canMoveBall(nearCell, farCell)){
                return true;
            }
        }
        
        if(i < mWidth - 2){
            nearCell = mBoard[i + 1][j];
            farCell = mBoard[i + 2][j];
            if(canMoveBall(nearCell, farCell)){
                return true;
            }
        }
        
        if(j > 1){
            nearCell = mBoard[i][j - 1];
            farCell = mBoard[j][j - 2];
            if(canMoveBall(nearCell, farCell)){
                return true;
            }
        }
        
        if(j < mHeight - 2){
            nearCell = mBoard[i][j + 1];
            farCell = mBoard[i][j + 2];
            if(canMoveBall(nearCell, farCell)){
                return true;
            }
        }
        
        
        return false;
    }
    
    
    /**
     * Says if more moves are available
     * @return
     */
    public Boolean checkStall(){
        for(int i = 0; i < mBoard.length; i++){
            for(int j = 0; j < mBoard[i].length; j++){
                if(mBoard[i][j].hasBall() && canMove(i, j)){
                    return false;
                }
            }
        }
        return true;
    }
    
    
    /** 
     * Converts current board into string
     * @return  serialized string
     */
    public String serialize(){
        StringBuffer res = new StringBuffer();
        for(int i = 0; i < mBoard.length; i++){
            for(int j = 0; j < mBoard[i].length; j++){
                res.append(mBoard[i][j].getEncoding());
            }
        }
        return res.toString();
    }


    
    public ChineseCheckers getGame() {
        return mGame;
    }
    
    
    
}

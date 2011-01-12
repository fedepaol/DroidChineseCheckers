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
package com.fede.checkers.ui;

import com.fede.checkers.board.AndEngineBoard;
import com.fede.checkers.board.AndEngineBoard.CantMoveException;
import com.fede.checkers.board.BoardCell.CantFillException;

import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.region.TextureRegion;


public class BallSprite extends BoardSprite {
    private int baseBoardX, baseBoardY;
    
    public BallSprite(int pX, int pY, TextureRegion pTextureRegion, AndEngineBoard b) {
        super(pX, pY, pTextureRegion, b);
        baseBoardX = 0;
        baseBoardY = 0;
    }
    
    
    /** 
     * When the user releases the ball
     * @param t
     */
    private void handleReleaseSprite(TouchEvent t){
        int releaseBoardY = board.getBoardYfromXCoord(t.getX());
        int releaseBoardX = board.getBoardXfromYCoord(t.getY());
        
        // if something happens I restore the ball in its original place
        try {
            board.moveBall(baseBoardX, baseBoardY, releaseBoardX, releaseBoardY);
            
            setPosition(releaseBoardX, releaseBoardY);
        } catch (CantMoveException e) {
            setPosition(baseBoardX, baseBoardY);
        } catch (CantFillException e) {
            setPosition(baseBoardX, baseBoardY);
        }
        
         
        
        baseBoardX = 0;
        baseBoardY = 0;
    }
    
    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX,
            float pTouchAreaLocalY) {
        
        int action = pSceneTouchEvent.getAction(); 
        
        switch(action){
        case TouchEvent.ACTION_UP:
            handleReleaseSprite(pSceneTouchEvent);
        break;
        case TouchEvent.ACTION_DOWN:
            baseBoardX = board.getBoardXfromYCoord(getAngleY(this.mY));
            baseBoardY = board.getBoardYfromXCoord(getAngleX(this.mX));
        default:
            this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2, pSceneTouchEvent.getY() - this.getHeight() / 2);
        break;
        }
        
        
        
        
        return true;
    }
    
    private float getAngleX(float x){
        return x + this.getWidth() / 2;
    }
    
    private float getAngleY(float y){
        return y + this.getHeight() / 2;
    }

}

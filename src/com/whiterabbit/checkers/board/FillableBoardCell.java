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
package com.whiterabbit.checkers.board;


import com.whiterabbit.checkers.Constants;
import com.whiterabbit.checkers.exceptions.CantFillException;
import com.whiterabbit.checkers.ui.CheckersSpriteFactory;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;


public class FillableBoardCell extends BoardCell {
    private Boolean mFilled;
    private Sprite mSprite;
    

    public FillableBoardCell(int x, int y, Boolean filled) {
        super(x, y);
        mFilled = filled;
    }
    
    @Override
    public Boolean fillableBall(){
        return true;
    }

    @Override
    public void getBall() throws CantFillException {
        if(mFilled){
            mFilled = false;
            return;
        }
                
        throw new CantFillException("No ball here");
    }

    @Override
    public void putBall() throws CantFillException {
        if(mFilled){
            throw new CantFillException("Ball already here");
        }
        mFilled = true;
    }
    
    private float getScale(Sprite s, float newSize){
        float actualSize = s.getWidth();
        return newSize / actualSize;
    }

    @Override
    public void buildSprites(CheckersSpriteFactory f, Scene s, AndEngineBoard b, float size) {
        
        Sprite hole = f.getHoleSprite(x, y, b, size);
        s.getChildByIndex(Constants.HOLE_LAYER).attachChild(hole);
        float scale = getScale(hole, size);
        //hole.setScale(scale);
        
        if(mFilled){
            mSprite = f.getBallSprite(x, y, b, size);
            s.getChildByIndex(Constants.BALL_LAYER).attachChild(mSprite);
            //mSprite.setScale(scale);
            s.registerTouchArea(mSprite);
        }
    }

    @Override
    public Sprite getBallSprite() {
        return mSprite;
    }

    @Override
    public void eraseBallSprite() {
        mSprite = null;
    }

    @Override
    public void setBallSprite(Sprite b) {
        mSprite = b;        
    }
    
    @Override
    public Boolean hasBall(){
        return mFilled;
    }

    @Override
    public char getEncoding() {
        if(mFilled)
            return '1';
        else
            return 'X';
    }
    

    
}

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
package com.whiterabbit.checkers.ui;


import com.whiterabbit.checkers.board.AndEngineBoard;
import com.whiterabbit.checkers.board.AndEngineBoard.CantMoveException;
import com.whiterabbit.checkers.exceptions.CantFillException;
import org.andengine.engine.camera.Camera;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.util.GLState;


public class BallSprite extends BoardSprite {
    private enum BallState {
        STOPPED,
        MOVING
    }

    private int baseBoardX, baseBoardY;
    private BallState mState;
    
    public BallSprite(int pX, int pY, TextureRegion pTextureRegion, AndEngineBoard b, float width, float height) {
        super(pX, pY, pTextureRegion, b, width, height);
        baseBoardX = 0;
        baseBoardY = 0;
        mState = BallState.STOPPED;
    }

    @Override
    protected void preDraw(final GLState pGLState, final Camera pCamera)
    {
        super.preDraw(pGLState, pCamera);
        pGLState.enableDither();
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
            if(mState == BallState.MOVING){ // ball state needed because actionup was being called twice
                handleReleaseSprite(pSceneTouchEvent);
                mState = BallState.STOPPED;
            }
        break;
        case TouchEvent.ACTION_DOWN:
            if(mState == BallState.STOPPED){
                baseBoardX = board.getBoardXfromYCoord(getAngleY(this.mY));
                baseBoardY = board.getBoardYfromXCoord(getAngleX(this.mX));
                mState = BallState.MOVING;
            }

        default:
            if(mState == BallState.MOVING){
                this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2, pSceneTouchEvent.getY() - this.getHeight() / 2);
            }
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

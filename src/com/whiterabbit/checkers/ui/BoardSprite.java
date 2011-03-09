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

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

import com.whiterabbit.checkers.board.AndEngineBoard;


public class BoardSprite extends Sprite {
    AndEngineBoard board;
    public BoardSprite(int boardX, int boardY, TextureRegion pTextureRegion, AndEngineBoard b, float width, float height) {
        
        super(getCenteredX(b.getXCoordFromBoardY(boardY), width), 
              getCenteredY(b.getYCoordFromBoardX(boardX), height), 
              width, height,
              pTextureRegion);
        board = b;
    }
    
    
    public void setPosition(int boardX, int boardY){
        float yLocation = getCenteredY(board.getYCoordFromBoardX(boardX), this.getBaseHeight());
        float xLocation = getCenteredX(board.getXCoordFromBoardY(boardY), this.getBaseWidth());
        
        
        this.setPosition(xLocation, yLocation);
    }

    
    
    
    private static float getCenteredX(float x, float width){
        return x - width / 2;
    }
    
    private static float getCenteredY(float y, float height){
        return y - height / 2;
    }
    

    
}

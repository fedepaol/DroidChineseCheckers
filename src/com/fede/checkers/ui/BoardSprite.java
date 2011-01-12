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

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;


public class BoardSprite extends Sprite {
    AndEngineBoard board;
    public BoardSprite(int boardX, int boardY, TextureRegion pTextureRegion, AndEngineBoard b) { 
        super(getCenteredX(b.getXCoordFromBoardY(boardY), pTextureRegion), 
              getCenteredY(b.getYCoordFromBoardX(boardX), pTextureRegion),  
              pTextureRegion);
        board = b;
    }
    
    
    public void setPosition(int boardX, int boardY){
        float yLocation = getCenteredY(board.getYCoordFromBoardX(boardX), this.getTextureRegion());
        float xLocation = getCenteredX(board.getXCoordFromBoardY(boardY), this.getTextureRegion());
        
        BaseGameActivity game = board.getGame();
        this.setPosition(xLocation, yLocation);
    }

    
    
    
    private static float getCenteredX(float x, TextureRegion region){
        return x - region.getWidth() / 2;
    }
    
    private static float getCenteredY(float y, TextureRegion region){
        return y - region.getHeight() / 2;
    }
    

    
}

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

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

import android.content.Context;

import com.whiterabbit.checkers.Constants;
import com.whiterabbit.checkers.board.AndEngineBoard;


public class CheckersSpriteFactory {
    private TextureRegion mHoleRegion;
    private TextureRegion mBallRegion;

    
    public CheckersSpriteFactory(Engine e, Context ctx){
        Texture texture = new Texture(Constants.BALL_SIZE, Constants.BALL_SIZE * 2, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        
        
        mHoleRegion = TextureRegionFactory.createFromAsset(texture, ctx, "gfx/hole.png", 0, 0);
        mBallRegion = TextureRegionFactory.createFromAsset(texture, ctx, "gfx/ball.png", 0, Constants.BALL_SIZE);
        
        e.getTextureManager().loadTexture(texture);
        

        
    }
    

    /**
     * Returns a hole sprite 
     * @param x x position
     * @param y x position
     * @param b board to put the sprite on
     * @param size size of the sprite
     */
    public Sprite getHoleSprite(int x, int y, AndEngineBoard b, float size){
        return new BoardSprite(x,
                          y,
                          mHoleRegion, 
                          b, size, size);
    }
    
    /**
     * Returns a ball sprite 
     * @param x x position
     * @param y x position
     * @param b board to put the sprite on
     * @param size size of the sprite
     */
    public Sprite getBallSprite(int x, int y, AndEngineBoard b, float size){
        return new BallSprite(x,
                              y,                 
                              mBallRegion, b, size, size);
    }
}
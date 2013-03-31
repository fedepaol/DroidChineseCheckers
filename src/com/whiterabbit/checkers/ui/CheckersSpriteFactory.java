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

import android.content.Context;
import com.whiterabbit.checkers.Constants;
import com.whiterabbit.checkers.board.AndEngineBoard;
import org.andengine.engine.Engine;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;


public class CheckersSpriteFactory {
    private TextureRegion mHoleRegion;
    private TextureRegion mBallRegion;

    
    public CheckersSpriteFactory(Engine e, Context ctx){
        BitmapTextureAtlas texture =  new BitmapTextureAtlas(e.getTextureManager(), Constants.BALL_SIZE, Constants.BALL_SIZE * 2);

        mHoleRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(texture, ctx, "gfx/hole.png", 0, 0);
        mBallRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(texture, ctx, "gfx/ball.png", 0, Constants.BALL_SIZE);
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

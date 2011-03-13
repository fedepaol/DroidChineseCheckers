package com.whiterabbit.checkers.ui;

import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

public class BackArrowSprite extends AnimatedSprite {
	public interface BackInterface{
		public void onBackArrowPressed();
	}
	
	public static final int DISABLED_TILE = 0;
	public static final int ENABLED_TILE = 1;
	
	private boolean enabled = false;
	private TiledTextureRegion mTextureRegion;
	private BaseGameActivity mContext;
	private BackInterface mBack;

	public BackArrowSprite(float pX, float pY, float pWidth, float pHeight,
			TiledTextureRegion pTextureRegion, Texture pTexture, BaseGameActivity ctx, BackInterface back) {
		super(pX, pY, pWidth, pHeight, pTextureRegion);
		mTextureRegion = pTextureRegion;
		mContext = ctx;
		mBack = back;
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		
		if(!enabled)
			return false;
		
		if(pSceneTouchEvent.getAction() != TouchEvent.ACTION_UP)
			return true;
				
        mTextureRegion.setCurrentTileIndex(DISABLED_TILE);
        enabled = false;
		mBack.onBackArrowPressed();
	
		return true;
	}
	
	
	public void enable(){
		if(enabled)
			return;
		
		mContext.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
		        mTextureRegion.setCurrentTileIndex(ENABLED_TILE);
			}
		});
		
		
		enabled = true;
		
	}

}

package com.whiterabbit.checkers.ui;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.whiterabbit.checkers.Constants;
import com.whiterabbit.checkers.boards.BoardClassicExtended;
import com.whiterabbit.checkers.boards.BoardKind;
import com.whiterabbit.checkers.boards.CheckersDbHelper;


public class CheckersMainMenu extends BaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	GoogleAnalyticsTracker tracker;


	protected static final int MENU_OK = 0;
	protected static final int MENU_QUIT = MENU_OK + 1;

	// ===========================================================
	// Fields
	// ===========================================================

	protected Camera mCamera;

	protected Scene mMainScene;
	private Texture mPlayTexture;
	protected TextureRegion mPlayTextureRegion;
	
	protected TextureRegion mBackgroundTextureRegion;
	protected Texture mBackgroundTexture;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public Engine onLoadEngine() {
	    int width = Constants.CAMERA_WIDTH;
	    int height = Constants.getHeight(width, this); 
        this.mCamera = new Camera(0, 0, width, height);
	    
		return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT, new RatioResolutionPolicy(width, height), this.mCamera));
	}

	@Override
	public void onLoadResources() {
		
		this.mPlayTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mPlayTextureRegion = TextureRegionFactory.createFromAsset(this.mPlayTexture, this, "gfx/play_button.png", 0, 0);
		
		this.mBackgroundTexture = new Texture(1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mBackgroundTextureRegion = TextureRegionFactory.createFromAsset(this.mBackgroundTexture, this, "gfx/menu_back.png", 0, 0);
		this.mEngine.getTextureManager().loadTextures(this.mPlayTexture, this.mBackgroundTexture);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		

		/* Just a simple scene with an animated face flying around. */
		this.mMainScene = new Scene(2);
		this.mMainScene.getBottomLayer().addEntity(new Sprite(0, 0, mBackgroundTextureRegion));		

		Sprite playSprite = new Sprite(Constants.CAMERA_WIDTH - 200, Constants.getHeight(Constants.CAMERA_WIDTH, this) - 200, this.mPlayTextureRegion){

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				Intent i = new Intent(CheckersMainMenu.this, BoardsListActivity.class);		        
		        startActivity(i);
				return true;
			}
			
		};
        
		this.mMainScene.getTopLayer().addEntity(playSprite);
		this.mMainScene.registerTouchArea(playSprite);
        this.mMainScene.setTouchAreaBindingEnabled(true);

		return this.mMainScene;
	}

	@Override
	public void onLoadComplete() {

	}


	@Override
    protected void onCreate(Bundle pSavedInstanceState) {
        tracker = GoogleAnalyticsTracker.getInstance();

        tracker.start("UA-16514009-3", this);
        tracker.trackPageView("/main");
        tracker.dispatch();

        super.onCreate(pSavedInstanceState);
    }
    
    

    @Override
    protected void onDestroy() {
      super.onDestroy();
      // Stop the tracker when it is no longer needed.
      tracker.stop();
    }
    
	

	

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
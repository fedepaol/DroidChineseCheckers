package com.whiterabbit.checkers.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.bugsense.trace.BugSenseHandler;
import com.immersion.uhl.Launcher;
import com.whiterabbit.checkers.Constants;
import com.whiterabbit.checkers.R;
import com.whiterabbit.checkers.util.Utils;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;


public class CheckersMainMenu extends BaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================



	protected static final int MENU_OK = 0;
	protected static final int MENU_QUIT = MENU_OK + 1;
	static final int MENU_OPTIONS = Menu.FIRST;

	// ===========================================================
	// Fields
	// ===========================================================

	protected Camera mCamera;

	protected Scene mMainScene;
	private BitmapTextureAtlas mPlayTexture;
	protected ITextureRegion mPlayTextureRegion;

	protected ITextureRegion mBackgroundTextureRegion;
	protected BitmapTextureAtlas mBackgroundTexture;
	private Launcher mHapticsLauncher;
	
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
    public EngineOptions onCreateEngineOptions() {
        int width = Constants.CAMERA_WIDTH;
        int height = Constants.getHeight(width, this);
        this.mCamera = new Camera(0, 0, width, height);

        return new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(width, height), this.mCamera);
    }





	@Override
    protected void onCreate(Bundle pSavedInstanceState) {
        mHapticsLauncher = new Launcher(this);
        BugSenseHandler.initAndStartSession(this, getString(R.string.bugsensekey));
        super.onCreate(pSavedInstanceState);
    }
    


    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		int groupId = 0;
		int menuItemId = MENU_OPTIONS;
		int menuItemOrder = Menu.NONE;	 
		int menuItemText = R.string.options;
		
		menu.add(groupId, menuItemId, menuItemOrder, menuItemText).setIcon(android.R.drawable.ic_menu_preferences);
		return true;
	}
    
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		super.onOptionsItemSelected(item);
		switch(item.getItemId()){
			case MENU_OPTIONS:{
				Intent i = new Intent(this, PegDroidPrefs.class); 
				startActivity(i);
				break;
			}
		}

		return true;
	}


    @Override
    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception  {

        mPlayTexture =  new BitmapTextureAtlas(getTextureManager(), 512, 256);
        mPlayTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mPlayTexture, this, "gfx/play_button.png", 0, 0);
        mEngine.getTextureManager().loadTexture(mPlayTexture);


		this.mBackgroundTexture = new BitmapTextureAtlas(getTextureManager(), 1024, 1024);
		this.mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBackgroundTexture, this, "gfx/menu_back.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mBackgroundTexture);
        pOnCreateResourcesCallback.onCreateResourcesFinished();

    }



    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
		/* Just a simple scene with an animated face flying around. */
		this.mMainScene = new Scene();
        mMainScene.attachChild(new Entity());
        mMainScene.attachChild(new Entity());
		mMainScene.getChildByIndex(0).attachChild(new Sprite(0, 0, mBackgroundTextureRegion, getVertexBufferObjectManager()));

		Sprite playSprite = new Sprite(Constants.CAMERA_WIDTH - 200, Constants.getHeight(Constants.CAMERA_WIDTH, this) - 200, this.mPlayTextureRegion, getVertexBufferObjectManager()){

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.isActionUp()){
				    Utils.playButtonPressed(mHapticsLauncher, CheckersMainMenu.this);
				    Intent i = new Intent(CheckersMainMenu.this, BoardsListActivity.class);
		            startActivity(i);
				    return true;
                }
                return false;
			}

		};


		this.mMainScene.getChildByIndex(1).attachChild(playSprite);
		this.mMainScene.registerTouchArea(playSprite);
        this.mMainScene.setTouchAreaBindingOnActionDownEnabled(true);
        pOnCreateSceneCallback.onCreateSceneFinished(mMainScene);
	}

    @Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }


}
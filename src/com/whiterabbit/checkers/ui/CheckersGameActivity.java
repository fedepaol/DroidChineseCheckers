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




import java.io.IOException;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.SpriteBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.opengl.view.RenderSurfaceView;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Debug;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.immersion.uhl.Launcher;
import com.scoreloop.client.android.ui.OnScoreSubmitObserver;
import com.whiterabbit.checkers.Constants;
import com.whiterabbit.checkers.R;
import com.whiterabbit.checkers.board.AndEngineBoard;
import com.whiterabbit.checkers.board.BoardCell.CantFillException;
import com.whiterabbit.checkers.boards.BoardClassicExtended;
import com.whiterabbit.checkers.boards.BoardKind;
import com.whiterabbit.checkers.boards.CheckersDbHelper;
import com.whiterabbit.checkers.ui.BackArrowSprite.BackInterface;
import com.whiterabbit.checkers.util.Utils;



/**
 * Main activity class. It implements the real game
 * @author fede
 *
 */
public class CheckersGameActivity extends BaseGameActivity implements BackInterface, OnScoreSubmitObserver{
	
	private class PlaySoundTask extends AsyncTask<Void, Void, Void> {
	     protected void onProgressUpdate() {
	         
	     }

	     protected void onPostExecute() {
	         
	     }

		@Override
		protected Void doInBackground(Void... params) {
			Utils.vibrate(mHapticsLauncher, Launcher.IMPACT_METAL_66, CheckersGameActivity.this);
			return null ;
		}
	 }
	 
	

	GoogleAnalyticsTracker tracker;
	
	public static final String TITLE = "Title";
	public static final String MESSAGE = "Message";
	public static final String BOARD = "Board";
	public static final String MODE = "Mode";
	public static final String REMAINING_BALLS = "Balls";
	public static final String SECONDS = "Seconds";
	public static final String ACHIEVEMENT = "Achievement";
	
	
	static final int MENU_OPTIONS = Menu.FIRST;	

	

    private Camera mCamera;
    CheckersSpriteFactory mSpriteFactory;
    private BoardKind mBoardType;   // Board kind
    private Boolean mLoadSaved;     // Says if a saved instance must be loaded
    private AndEngineBoard mBoard;  
    private Boolean mFinishing;
    private int mCameraWidth = Constants.CAMERA_WIDTH;
    private int mCameraHeight = 0;
    private int mAdMobOffset = 0;
    private CheckersDbHelper db;
    private String mBoardName;
    private TimerHandler mTimerHandler;
    private long mSecondsPlayed;
    private Scene mScene;
    private Sound mMarbleSound;
    
    private Texture mFontTexture;
	private Font mFont;
	private ChangeableText mScoreText;
	private PlaySoundTask mSoundTask;



    private TextureRegion mBackgroundRegion;
    private TiledTextureRegion mBackArrowRegion;
    private Texture mBackArrowTexture;
    private BackArrowSprite mBackArrow;
    
    private Launcher mHapticsLauncher;

    public static void launch(Activity launcher, String board, Boolean restore){
        Intent i = new Intent(launcher, CheckersGameActivity.class);
        i.putExtra(Constants.BOARD_NAME_INTENT, board);
        i.putExtra(Constants.BOARD_RESTORE_INTENT, restore);
        launcher.startActivity(i);
    }
    
    public static void launch(Activity launcher, String board){
        launch(launcher, board, false);
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
    protected void onCreate(Bundle pSavedInstanceState) {
        mFinishing = false;
        Bundle extras = getIntent().getExtras();
        String boardName = extras != null ? extras.getString(Constants.BOARD_NAME_INTENT) 
                : BoardClassicExtended.NAME;
        
        mLoadSaved = extras != null ? extras.getBoolean(Constants.BOARD_RESTORE_INTENT, false) 
                : false;
        
        mBoardType = BoardKind.getBoardFromName(boardName);
        mBoardName = boardName;
        db = new CheckersDbHelper(this);
        tracker = GoogleAnalyticsTracker.getInstance();

        tracker.start("UA-16514009-3", this);
        tracker.trackPageView(mBoardName);
        tracker.dispatch();
        mSecondsPlayed = 0;
        mSoundTask = new PlaySoundTask();
        
        mTimerHandler = new TimerHandler(1.0f, true, new ITimerCallback() {
            public void onTimePassed(TimerHandler pTimerHandler) {
            	mSecondsPlayed++;
            	CheckersGameActivity.this.mScoreText.setText(getShortTimeFromSeconds(mSecondsPlayed));
        }});
        
        mHapticsLauncher = new Launcher(this);
        
        

        super.onCreate(pSavedInstanceState);
    }
    
    

    @Override
    protected void onDestroy() {
      super.onDestroy();
      // Stop the tracker when it is no longer needed.
      tracker.stop();
    }
    
    @Override
    public Engine onLoadEngine() {
        
        mCameraHeight = Constants.getHeight(mCameraWidth, this);
        mAdMobOffset = Constants.getOffset(mCameraWidth, this);
        this.mCamera = new Camera(0, 0, mCameraWidth, mCameraHeight);
        return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT, 
                new RatioResolutionPolicy(mCameraWidth, mCameraHeight), this.mCamera).setNeedsSound(true));
    }

    @Override
    public void onLoadResources() {
        
        mSpriteFactory = new CheckersSpriteFactory(mEngine, this); 
        Texture texture = new Texture(512 , 1024, TextureOptions.DEFAULT);  

        mBackgroundRegion  = TextureRegionFactory.createFromAsset(texture, this, "gfx/back.png", 0, 0);
        mEngine.getTextureManager().loadTexture(texture);
        
        SoundFactory.setAssetBasePath("mfx/");
        try {
            this.mMarbleSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "marble.ogg");
        } catch (final IOException e) {
            Debug.e("Error", e);
        }
        
        
        
        this.mBackArrowTexture = new Texture(128, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.mBackArrowRegion = TextureRegionFactory.createTiledFromAsset(this.mBackArrowTexture, this, "gfx/back_arrow.png", 0, 0,2,1);
        this.mBackArrowRegion.setCurrentTileIndex(BackArrowSprite.DISABLED_TILE);
		this.mEngine.getTextureManager().loadTexture(this.mBackArrowTexture);
		
		FontFactory.setAssetBasePath("font/");
		this.mFontTexture = new Texture(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		//this.mFont = FontFactory.createFromAsset(this.mFontTexture, this, "acme.ttf", 32, true, Color.WHITE);
		this.mFont = new Font(this.mFontTexture, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 36, true, Color.WHITE);
		
		
		this.mEngine.getTextureManager().loadTexture(this.mFontTexture);
		this.mEngine.getFontManager().loadFont(this.mFont);

    }

    @Override
    public Scene onLoadScene() {
        this.mEngine.registerUpdateHandler(new FPSLogger());

        final Scene scene = new Scene(3);
        
        Sprite back = new Sprite(0, 0, mBackgroundRegion);
        scene.setBackground(new SpriteBackground(back));
        
        mBoard = new AndEngineBoard(mCameraWidth, mCameraHeight, mBoardType, mLoadSaved, scene, mSpriteFactory, mAdMobOffset, this); 
        
        this.mBackArrow = new BackArrowSprite(mCameraWidth - 64 - 10, 10, 64, 64, mBackArrowRegion, mBackArrowTexture, this, this);
        scene.getLayer(Constants.SCORE_LAYER).addEntity(mBackArrow);
        scene.registerTouchArea(mBackArrow);
                
        scene.setTouchAreaBindingEnabled(true);
        
        mSecondsPlayed = mBoardType.getSavedTime();
        
        mScene = scene;
        scene.registerUpdateHandler(mTimerHandler);
        
        
        /* The ScoreText showing how many points the player scored. */
		this.mScoreText = new ChangeableText(15, 15, this.mFont, "", "00:00".length());
		this.mScoreText.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mScoreText.setAlpha(0.5f);
		scene.getLayer(Constants.SCORE_LAYER).addEntity(this.mScoreText);
        
        return scene;
    }

    @Override
    public void onLoadComplete() {

    }
    


    @Override
    protected void onPause() {
        db.close();
        if(!mFinishing){
        	stopTimer();	//otherwise I already stopped it
        	
        	if(mBoard.getScore() > 0){	// Only if I did a move
        		mBoardType.saveDump(mBoard.serialize(), mBoard.getScore(), mSecondsPlayed, this);
        		CheckersDbHelper.setLastBoardUsed(mBoardName, this);
        	}
        }
        super.onPause();
    }
    
    @Override
    protected void onResume() {
        db.open();
        mFinishing = false;
        if(!mLoadSaved){
        	mSecondsPlayed = 0;
        }
        super.onResume();
    }
    
    private void stopTimer(){
    	if(mScene != null && mTimerHandler != null)
    		mScene.unregisterUpdateHandler(mTimerHandler);
    }

    @Override
    protected void onRestart() {
        mLoadSaved = true;
        super.onRestart();
    }
    
    
    private int getOldScore(){
        return db.getBoardMaxScore(mBoardName).maxScore;         
    }
    
    
    private int getOldTime(){
        return db.getBoardMaxScore(mBoardName).minTime;         
    }
    
    /**
     * Behaviour to be performed when no more moves are available
     */
    public void onGameStall(long thisGameScore){ 
    	stopTimer();
    	Utils.vibrate(mHapticsLauncher, Launcher.SLOW_PULSE_100, this);
    	
        AlertDialog.Builder ad = new AlertDialog.Builder(this); 

        long oldScore = getOldScore();
        long oldTime = getOldTime();
        long secondsPlayed = mSecondsPlayed;
        
        StringBuffer title = new StringBuffer();
        StringBuffer message = new StringBuffer();
        setDialogMessage(title, message, thisGameScore, oldScore, secondsPlayed, oldTime, ad);
        
        
        Intent intent = new Intent(this, CheckersStallActivity.class);
        intent.putExtra(TITLE, title.toString());
        intent.putExtra(MESSAGE, message.toString());
        intent.putExtra(BOARD, mBoardType.getName());
        intent.putExtra(MODE, mBoardType.getMode());
        
        mFinishing = true;
        mBoardType.delete(this);
        if(isBetterResult(thisGameScore, oldScore, secondsPlayed, oldTime)){
        	db.setBoardMaxScore(mBoardName, thisGameScore, secondsPlayed);
        	long remainingBalls = mBoardType.getRemainingBallsFromScore(thisGameScore);
        	
        	intent.putExtra(REMAINING_BALLS, remainingBalls);
            intent.putExtra(SECONDS, secondsPlayed);
            intent.putExtra(ACHIEVEMENT, mBoardType.getAchievement());
        }
        
        startActivity(intent);
        finish();
        return;    
    }


    /** 
     * Tells if newscore is better than older
     * @param newScore
     * @param oldScore
     * @param newTime
     * @param oldTime
     * @return
     */
    private boolean isBetterResult(long newScore, long oldScore, long newTime, long oldTime){
    	if(newScore > oldScore)
    		return true;
    	    	
    	if(newScore == oldScore){
    		if(newTime < oldTime || oldTime == 0){
    			return true;
    		}
    	}
    	return false;
    }
    
    
    public static String getTimeFromSeconds(long seconds){
    	long minutes = seconds / 60;
    	long secs = seconds % 60;
    	if(minutes == 0){
    		return String.format("%d sec", secs);
    	}else{
    		return String.format("%d min %d sec", minutes, secs);
    	}
    }
    
    private String getShortTimeFromSeconds(long seconds){
    	long minutes = seconds / 60;
    	long secs = seconds % 60;
    	return String.format("%02d:%02d", minutes, secs);
    	
    }
    
    public Launcher getLauncher(){
    	return mHapticsLauncher;
    }
    
    /**
     * Sets board finished dialog message
     * @param newScore
     * @param oldScore
     * @param ad
     */
    private void setDialogMessage(StringBuffer title, StringBuffer message, long newScore, long oldScore, long newTime, long oldTime, AlertDialog.Builder ad){
        if(isBetterResult(newScore, oldScore, newTime, oldTime)){        	
        	long remainingBalls = mBoardType.getRemainingBallsFromScore(newScore);
        	
        	if(remainingBalls == 1 && newScore != oldScore){
        		title.append(getString(R.string.achievement));
        		message.append(String.format(getString(R.string.you_are_now_master_of), mBoardType.getName()));
        	}else{
        		title.append(getString(R.string.new_record));
        		
        		if(newScore > oldScore || oldTime == 0){
        			message.append(String.format(getString(R.string.no_more_moves_record), mBoardType.getRemainingBallsFromScore(newScore), 
            																	  mBoardType.getRemainingBallsFromScore(oldScore), getTimeFromSeconds(newTime)));
        		}else{	// newScore == oldScore
        			message.append(String.format(getString(R.string.no_more_moves_record_time), mBoardType.getRemainingBallsFromScore(newScore), 
							  getTimeFromSeconds(newTime), getTimeFromSeconds(oldTime)));
        		}
        	}
        }else{
            title.append(getString(R.string.stall));
            message.append(String.format(getString(R.string.no_more_moves), mBoardType.getRemainingBallsFromScore(newScore)));
        }
        
        ad.setTitle(title);
        ad.setMessage(message);
    }
    
    
    

    protected boolean showInCenter=true;
    protected boolean testMode=true;

    @Override
    protected void onSetContentView() {
        
        
        final RelativeLayout relativeLayout = new RelativeLayout(this);
        final FrameLayout.LayoutParams relativeLayoutLayoutParams = 
                            new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

        final AdView adView = new AdView(this, AdSize.BANNER, "a14d2ccfb20d9cd");

        adView.refreshDrawableState();
        adView.setVisibility(View.VISIBLE);
        AdRequest req = new AdRequest();

        adView.loadAd(req);

        

        this.mRenderSurfaceView = new RenderSurfaceView(this);
        mRenderSurfaceView.setRenderer(mEngine);

        final android.widget.RelativeLayout.LayoutParams surfaceViewLayoutParams = 
                               new RelativeLayout.LayoutParams(super.createSurfaceViewLayoutParams());
        surfaceViewLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        final android.widget.RelativeLayout.LayoutParams adViewLayoutParams=
                           new RelativeLayout.LayoutParams(super.createSurfaceViewLayoutParams());
        
        adViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        
        adViewLayoutParams.height=LayoutParams.WRAP_CONTENT;
        adViewLayoutParams.width=LayoutParams.FILL_PARENT;
        
        relativeLayout.addView(this.mRenderSurfaceView, surfaceViewLayoutParams);
        relativeLayout.addView(adView, adViewLayoutParams);

        
        this.setContentView(relativeLayout, relativeLayoutLayoutParams);    
    }

    protected boolean isShowInCenter() {
        return showInCenter;
    }

    protected void setShowInCenter(boolean showInCenter) {
        this.showInCenter = showInCenter;
    }

    protected boolean isTestMode() {
        return testMode;
    }

    protected void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }

    
    public void playMarbleSound()
    {
    	Utils.vibrate(mHapticsLauncher, Launcher.IMPACT_METAL_66, this);
        mMarbleSound.play();
        
    }
    
    
    public void onMove(){
    	mBackArrow.enable();
    }
    
    @Override
    public void onBackArrowPressed(){
    	try {
			mBoard.backMove();
		} catch (CantFillException e) {
			
		}
    }

	@Override
	public void onScoreSubmit(int status, Exception error) {
		// TODO Auto-generated method stub
		
	}

    

    


    

}

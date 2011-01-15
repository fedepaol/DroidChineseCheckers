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




import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.admob.android.ads.AdManager;
import com.admob.android.ads.AdView;
import com.fede.checkers.Constants;
import com.fede.checkers.R;
import com.fede.checkers.board.AndEngineBoard;
import com.fede.checkers.boards.BoardClassic;
import com.fede.checkers.boards.BoardType;
import com.fede.checkers.boards.CheckersDbHelper;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
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
import org.anddev.andengine.opengl.view.RenderSurfaceView;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import javax.microedition.khronos.opengles.GL10;

/**
 * Main activity class. It implements the real game
 * @author fede
 *
 */
public class ChineseCheckers extends BaseGameActivity {


    private Camera mCamera;
    CheckersSpriteFactory mSpriteFactory;
    private BoardType mBoardType;
    private Boolean mLoadSaved;
    private AndEngineBoard mBoard;
    private Boolean mFinishing;
    private int mCameraWidth = 265;
    private int mCameraHeight = 0;
    
    private ChangeableText mScoreText;    
    private Texture mFontTexture;
    private Font mFont;


    private TextureRegion mBackgroundRegion;

    
    
    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        mFinishing = false;
        Bundle extras = getIntent().getExtras();
        String boardName = extras != null ? extras.getString(Constants.BOARD_NAME_INTENT) 
                : BoardClassic.NAME;
        
        mLoadSaved = extras != null ? extras.getBoolean(Constants.BOARD_RESTORE_INTENT, false) 
                : false;
        this.mBoardType = BoardType.getBoardFromName(boardName);
       
        super.onCreate(pSavedInstanceState);
    }
    
    @Override
    public Engine onLoadEngine() {
        DisplayMetrics dm = new DisplayMetrics();       
        getWindowManager().getDefaultDisplay().getMetrics(dm); 
        
        
        mCameraHeight = mCameraWidth*dm.heightPixels/dm.widthPixels;
        
        this.mCamera = new Camera(0, 0, mCameraWidth, mCameraHeight);
        return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT, 
                new RatioResolutionPolicy(dm.widthPixels, dm.heightPixels), this.mCamera));
    }

    @Override
    public void onLoadResources() {
        
        mSpriteFactory = new CheckersSpriteFactory(mEngine, this); 
        Texture texture = new Texture(512 , 1024, TextureOptions.DEFAULT);  

        mBackgroundRegion  = TextureRegionFactory.createFromAsset(texture, this, "gfx/back.png", 0, 0);
        mEngine.getTextureManager().loadTexture(texture);
        
        FontFactory.setAssetBasePath("font/");
        this.mFontTexture = new Texture(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.mFont = FontFactory.createFromAsset(this.mFontTexture, this, "Plok.ttf", 32, true, Color.WHITE);


    }

    @Override
    public Scene onLoadScene() {
        this.mEngine.registerUpdateHandler(new FPSLogger());

        final Scene scene = new Scene(3);
        
        Sprite back = new Sprite(0, 0, mBackgroundRegion);
        scene.setBackground(new SpriteBackground(back));
        
        mBoard = new AndEngineBoard(mCameraWidth, mCameraHeight, mBoardType, mLoadSaved, scene, mSpriteFactory, this);        
        scene.setTouchAreaBindingEnabled(true);
        
        this.mScoreText = new ChangeableText(5, 5, this.mFont, "Score: 0", "Score: XXXX".length());
        this.mScoreText.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        this.mScoreText.setAlpha(0.5f);
        //scene.getLayer(Constants.SCORE_LAYER).addEntity(this.mScoreText);

        return scene;
    }

    @Override
    public void onLoadComplete() {

    }

    @Override
    protected void onPause() {
        if(!mFinishing){
            mBoardType.saveDump(mBoard.serialize(), mBoard.getScore(), this);
            CheckersDbHelper.setLastBoardUsed(mBoardType.getName(), this);
        }
        super.onPause();
    }


    @Override
    protected void onRestart() {
        mLoadSaved = true;
        super.onRestart();
    }
    
    
    /**
     * Behaviour to be performed when no more moves are available
     */
    public void onGameStall(){
        String button1String = getString(R.string.new_game); 
        String cancelString = getString(R.string.back);
        AlertDialog.Builder ad = new AlertDialog.Builder(this); 
        ad.setTitle(getString(R.string.stall));
        
        
        ad.setMessage(getString(R.string.no_more_moves));
        
        // Relaunch the same game
        ad.setPositiveButton(button1String,
                             new OnClickListener() { 
                                public void onClick(DialogInterface dialog, int arg1) {
                                    Intent i = new Intent(ChineseCheckers.this, ChineseCheckers.class);
                                    i.putExtra(Constants.BOARD_NAME_INTENT, mBoardType.getName());
                                    startActivity(i);
                                    mFinishing = true;
                                    ChineseCheckers.this.finish();
                                } });    
        
        
        // Returns to board list
        ad.setNegativeButton(cancelString,
                new OnClickListener() { 
                   public void onClick(DialogInterface dialog, int arg1) {
                       mFinishing = true;
                       ChineseCheckers.this.finish();
                   } });
        ad.show();
        mBoardType.delete(this);
        return;    
    }


    protected boolean showInCenter=true;
    protected boolean testMode=true;

    @Override
    protected void onSetContentView() {
        if(testMode){
            AdManager.setTestDevices( new String[] { 
                    AdManager.TEST_EMULATOR, 
                    "FDF5AA30500D9CBDE7CC910D1A529F77", 
                    } );
        }
        
        final RelativeLayout relativeLayout = new RelativeLayout(this);
        final FrameLayout.LayoutParams relativeLayoutLayoutParams = 
                            new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

        final AdView adView = new AdView(this);
        adView.refreshDrawableState();
        adView.setVisibility(View.VISIBLE);
        adView.requestFreshAd();
        adView.setRequestInterval(30);

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

    public void updateScore(long mScore) {
       // mScoreText.setText(String.valueOf(mScore));      
    }




    

}

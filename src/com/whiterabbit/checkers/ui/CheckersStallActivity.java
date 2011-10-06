package com.whiterabbit.checkers.ui;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.scoreloop.client.android.core.model.Achievement;
import com.scoreloop.client.android.core.model.Score;
import com.scoreloop.client.android.ui.LeaderboardsScreenActivity;
import com.scoreloop.client.android.ui.OnScoreSubmitObserver;
import com.scoreloop.client.android.ui.PostScoreOverlayActivity;
import com.scoreloop.client.android.ui.ScoreloopManagerSingleton;
import com.whiterabbit.checkers.PreferencesStore;
import com.whiterabbit.checkers.R;



public class CheckersStallActivity extends Activity implements OnScoreSubmitObserver{
	// ===========================================================
	// Constants
	// ===========================================================

	GoogleAnalyticsTracker tracker;
	String mTitle;
	String mMessage;
	String mBoardName;
	int mMode;
	
	private Button mTellAFriendButton;
	private Button mShareResultOnSocialButton;
	private Button mBackButton;
	private Button mPlayAgainButton;
	private Button mSeeScoresButton;
	
	
	private Long mRemaining;
	private Long mSeconds;
	private String mAchievement = "";
	
	private final int POST_SCORE = 0;


	@Override
    protected void onCreate(Bundle pSavedInstanceState) {
        tracker = GoogleAnalyticsTracker.getInstance();

        tracker.start("UA-16514009-3", this);
        
        
        Bundle extras = getIntent().getExtras();
        if(extras != null){
        	mTitle = extras.getString(CheckersGameActivity.TITLE);
        	mMessage= extras.getString(CheckersGameActivity.MESSAGE);
        	mMode = extras.getInt(CheckersGameActivity.MODE);
        	mBoardName = extras.getString(CheckersGameActivity.BOARD);

        	mRemaining = extras.getLong(CheckersGameActivity.REMAINING_BALLS, 0);
        	mSeconds = extras.getLong(CheckersGameActivity.SECONDS, 0);
        	mAchievement = extras.getString(CheckersGameActivity.ACHIEVEMENT);
        }
        
        setContentView(R.layout.result_layout);
        
        setupButtons();
        
        if(mRemaining > 0)
        	notifyScoreLoop(mRemaining, mSeconds);
        
        showVoteMeDialog();
        
        setupMessage(mTitle, mMessage);
        super.onCreate(pSavedInstanceState);
    }
    
	private void setupMessage(String title, String message){
		TextView titleView = (TextView) findViewById(R.id.result_title);
		titleView.setText(title);
		TextView messageView = (TextView) findViewById(R.id.result_message);
		messageView.setText(message);
	}
	
    private void setupButtons(){
    	mTellAFriendButton = (Button) findViewById(R.id.result_btn_share_with_a_friend);
    	mTellAFriendButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				tracker.trackPageView("/tellafriend");
		        tracker.dispatch();
				Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.check_game));
				shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.try_pegdroid));
				startActivity(Intent.createChooser(shareIntent, "Share pegdroid"));		
			}
    		
    	});
    	
    	
    	mShareResultOnSocialButton = (Button) findViewById(R.id.result_btn_share_social);
    	mShareResultOnSocialButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				tracker.trackPageView("/share");
		        tracker.dispatch();
				startActivityForResult(new Intent(CheckersStallActivity.this, PostScoreOverlayActivity.class), POST_SCORE);
			}
    		
    	});
    	
    	mShareResultOnSocialButton.setVisibility(View.INVISIBLE);
    	
    	mBackButton = (Button) findViewById(R.id.result_btn_return_to_list);
    	mBackButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				finish();
			}
    		
    	});
    	
    	mPlayAgainButton = (Button) findViewById(R.id.result_btn_playagain);
    	if(mPlayAgainButton != null){	// because in small layouts I removed this button
	    	mPlayAgainButton.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					CheckersGameActivity.launch(CheckersStallActivity.this, mBoardName);
					finish();
				}
	    		
	    	});
    	}
    	
    	mSeeScoresButton = (Button) findViewById(R.id.result_btn_scores);
    	mSeeScoresButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				final Intent intent = new Intent(CheckersStallActivity.this, LeaderboardsScreenActivity.class);
                intent.putExtra(LeaderboardsScreenActivity.MODE, mMode); 
                startActivity(intent);
                finish();
			}
    	});
    }

    @Override
    protected void onDestroy() {
      super.onDestroy();
      // Stop the tracker when it is no longer needed.
      tracker.stop();
    }
    
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {

          switch (requestCode) {

                 case POST_SCORE:
                            finish();
                            break;
                 default:
                            break;
         }
      }
    
    
    /**
     * Sends result to scoreloop
     * @param remainingBalls
     */
    private void notifyScoreLoop(long remainingBalls, long time){
    	Double scoreResult = Double.valueOf(remainingBalls);
    	Score s = new Score(scoreResult, null);
    	s.setMode(new Integer(mMode));
    	s.setMinorResult(Double.valueOf(time));
    	ScoreloopManagerSingleton.get().setOnScoreSubmitObserver(this);
		ScoreloopManagerSingleton.get().onGamePlayEnded(s);
    	
		if(remainingBalls == 1){
			Achievement _achievement;
			_achievement = ScoreloopManagerSingleton.get().getAchievement(mAchievement);
	        ScoreloopManagerSingleton.get().achieveAward(_achievement.getAward().getIdentifier(), true, true);
			
		}
    }

	@Override
	public void onScoreSubmit(int status, Exception error) {
		if(status == OnScoreSubmitObserver.STATUS_SUCCESS_SCORE){
			mShareResultOnSocialButton.setVisibility(View.VISIBLE);
		}
	}
	
	private void showVoteMeDialog(){
		Long c = PreferencesStore.getCount(this);
		PreferencesStore.setCount(c + 1, this);
		
		if(c == 30 || c == 70 || c == 110){	
			buildOkCancelDialog(getString(R.string.please_vote), getString(R.string.if_you_enjoyed),
					new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which) {
							try{
								Intent goToMarket =  new Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id=com.whiterabbit.checkers"));
								startActivity(goToMarket);
							}catch(ActivityNotFoundException e){
								return;
							}
						}
				
			}, this);
		 }
	}
	
	void buildOkCancelDialog(String title, String message, DialogInterface.OnClickListener okListener, Context context){
		AlertDialog.Builder ad = new AlertDialog.Builder(context);
		ad.setTitle(title);
		ad.setMessage(message);
		ad.setPositiveButton(R.string.yes_please, okListener);
		ad.setNegativeButton(R.string.no_thanks, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int i) {}});
		ad.show();
	}

}
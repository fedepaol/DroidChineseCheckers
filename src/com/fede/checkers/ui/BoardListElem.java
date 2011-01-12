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

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.fede.checkers.R;
import com.fede.checkers.boards.BoardType;


public class BoardListElem extends TableLayout {
    private TextView mName;
    private TextView mSaved;
    private TextView mSavedDate;
    private TextView mMaxScore;
    private ImageView mBoardImg;
    
    

    public BoardListElem(Context context) {
        super(context);
        String infService = Context.LAYOUT_INFLATER_SERVICE; 
        LayoutInflater li; 
        li = (LayoutInflater)getContext().getSystemService(infService); 
        li.inflate(R.layout.board_list_elem, this, true);
        
        
                
        mName = (TextView) findViewById(R.id.board_elem_name);
        mSaved = (TextView) findViewById(R.id.exists_saved);
        mSavedDate = (TextView) findViewById(R.id.board_elem_date);
        mMaxScore = (TextView) findViewById(R.id.max_score);
        mBoardImg = (ImageView) findViewById(R.id.BoardIcon);       
    }
    
    
    public void setFromBoard(BoardType board){
        mName.setText(board.getName());
        Boolean existsSaved = board.load(getContext());
        mSaved.setText(existsSaved? "Saved" : "Not Saved");
        if(existsSaved){
            mSavedDate.setText(board.getSavedDate().toLocaleString());;
        }else{
            mSavedDate.setText("");
        }
        mMaxScore.setText("0");
        mBoardImg.setImageResource(board.getImageResource());        
    }

}

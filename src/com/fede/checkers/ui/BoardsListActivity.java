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
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fede.checkers.Constants;
import com.fede.checkers.R;
import com.fede.checkers.boards.BoardType;

import java.util.ArrayList;
import java.util.List;




public class BoardsListActivity extends ListActivity {
    ArrayList<BoardType> mBoards;
    MyArrayAdapter mAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_list);
        mBoards = BoardType.getAllBoards();
        
        mAdapter = new MyArrayAdapter(this, R.layout.board_list_elem, mBoards);
        
        this.setListAdapter(mAdapter);
    }
    
    
    void showDialog(Boolean existsSaved, final BoardType board)
    {
        String button1String = getString(R.string.new_game); 
        String button2String = getString(R.string.restore_game); 
        String cancelString = getString(R.string.back);
        AlertDialog.Builder ad = new AlertDialog.Builder(this); 
        ad.setTitle(board.getName());
        
        if(existsSaved){
            ad.setMessage(getString(R.string.do_you_want_to_restore));
            ad.setNeutralButton(button2String,
                    new OnClickListener() { 
                       public void onClick(DialogInterface dialog, int arg1) {
                           Intent i = new Intent(BoardsListActivity.this, ChineseCheckers.class);
                           i.putExtra(Constants.BOARD_RESTORE_INTENT, true);
                           i.putExtra(Constants.BOARD_NAME_INTENT, board.getName());
                           startActivity(i);
                       } });
        }else{
            ad.setMessage(getString(R.string.starting_new_game));
        }
         
        ad.setPositiveButton(button1String,
                             new OnClickListener() { 
                                public void onClick(DialogInterface dialog, int arg1) {
                                    board.delete(BoardsListActivity.this);
                                    Intent i = new Intent(BoardsListActivity.this, ChineseCheckers.class);
                                    i.putExtra(Constants.BOARD_NAME_INTENT, board.getName());
                                    startActivity(i);
                                } });    
        
        
        
        ad.setNegativeButton(cancelString,
                new OnClickListener() { 
                   public void onClick(DialogInterface dialog, int arg1) {
                       // do nothing
                   } });
        ad.show();
        return;    
    }   
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        BoardType board = mBoards.get(position);       
        Boolean existsSaved = board.load(this);            
        showDialog(existsSaved, board);

        
        /*String message = String.format("%s\n%s\n%s", dateString, timeString, fullStatus);
        showDialog(message, shortStatus);*/
    }

 
    public class MyArrayAdapter extends ArrayAdapter<BoardType> {
        int resource;
        public MyArrayAdapter(Context context, 
                              int _resource,
                              List<BoardType> items) {
            super(context, _resource, items);
            resource = _resource;
        }
        
        @Override 
        public View getView(int position, View convertView, ViewGroup parent) {
            BoardListElem newView; 
            BoardType board = getItem(position);
            if (convertView == null) {
                newView = new BoardListElem(getContext()); 
                String inflater = Context.LAYOUT_INFLATER_SERVICE; 
                LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);
                newView.setFromBoard(board);
                vi.inflate(resource, newView, true);
            } else { 
                newView = (BoardListElem)convertView;
                newView.setFromBoard(board);
            }
            return newView;
        }
               
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }
    
}

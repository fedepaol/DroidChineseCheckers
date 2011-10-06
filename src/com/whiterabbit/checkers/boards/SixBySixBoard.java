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
package com.whiterabbit.checkers.boards;

import com.whiterabbit.checkers.R;




public class SixBySixBoard extends BoardKind {
    public final static String NAME = "6x6";
    
    
    private final char[][] map = {   
            
            {'1','1', '1', '1', '1','1'},
            {'1','1', '1', '1', '1','1'},
            {'1','1', '1', '1', 'X','1'},
            {'1','1', '1', '1', '1','1'},
            {'1','1', '1', '1', '1','1'},
            {'1','1', '1', '1', '1','1'}};


    @Override
    public String getName() {
        return NAME;
    }



    @Override
    public int getImageResource() {
        return R.drawable.sixbysix;
    }
    
	@Override
	char[][] getMap() {
		return map;
	}



	@Override
	public
	int getMode() {
		return 11;
	}



	@Override
	public String getAchievement() {
		return "com.pegdroid.6x6";
	}

}

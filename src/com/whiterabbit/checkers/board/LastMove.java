package com.whiterabbit.checkers.board;

public class LastMove {
	BoardCell lastStart;
	BoardCell lastDest;
	BoardCell lastEaten;
	
	boolean updated = false;
	
	
	public void update(BoardCell start, BoardCell dest, BoardCell eaten){
		lastStart = start;
		lastDest = dest;
		lastEaten = eaten;
		updated = true;
	}
	
	public void reset(){
		updated = false;
	}
	
	
	public BoardCell getLastStart(){
		return lastStart;
	}

	public BoardCell getLastDest(){
		return lastDest;
	}
	
	public BoardCell getLastEaten(){
		return lastEaten;
	}
	
	public boolean canMoveBack(){
		return updated;
	}
}

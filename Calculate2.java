package com.barracuda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Calculate2
{
	
	HashMap<Integer, int[]> boardMap = new HashMap<Integer, int[]>();
	void init(Map<String, Object> state)
	{
		GameState s = new GameState(state);
		
		for(int x = 0; x < 7; x++)
		{
			 for(int y = 0; y < 7; y++)
			 {
				 int[] coordinates = {x, y};
				 boardMap.put(s.board.get(x).get(y), coordinates);
			 }
		}
	}
	
	Integer get_bid(List<Integer> offer, Map<String, Object> state)
	{
		GameState gameState = new GameState(state);
		return 14;
	}
	
	Integer make_choice(List<Integer> offer, Map<String, Object> state)
	{
		 GameState s = new GameState(state);
		 
		 int result = offer.get(0);
		 
		 if (s.owned_squares.get(s.idx).size() == 0)//first move take middle
		 {
			 for (int x = 2; x < 5; x++)
			 {
				 for (int y = 2; y < 5; y++)
				 {
					 if (offer.contains(s.board.get(x).get(y)) && !s.owned_squares.get(~s.idx).contains(s.board.get(x).get(y)))
					 {
						 path(s.idx, x, y, s); 
						 return s.board.get(x).get(y);
					 }
				 }
			 }
			 int[] coor = boardMap.get(offer.get(0));
			 path(s.idx, coor[0], coor[1], s);
			 return offer.get(0);
		 }
		 else
		 {
			 for (int enemy : s.owned_squares.get(~s.idx))
			 {
				 if (s.idx == 0)//hori
				 {
					 for (int i = 0; i < westHori.size(); i++)
					 {
						 for (int o : offer) if (westHori.contains(o)) result = o;
						 if (westHori.get(i).contains(enemy)) 
						 {
							 westHori.remove(i); i--;
						 }
					 }
					 
					 for (int i = 0; i < eastHori.size(); i++)
					 {
						 for (int o : offer) if (eastHori.contains(o)) result = o;
						 if (eastHori.get(i).contains(enemy)) 
						 {
							 eastHori.remove(i); i--;
						 }
					 } 
				 }
				 else
				 {
					 for (int i = 0; i < northVert.size(); i++)
					 {
						 for (int o : offer) if (northVert.contains(o)) result = o;
						 if (northVert.get(i).contains(enemy)) 
						 {
							 northVert.remove(i); i--;
						 }
					 }
					 
					 for (int i = 0; i < southVert.size(); i++)
					 {
						 for (int o : offer) if (southVert.contains(o)) result = o;
						 if (southVert.get(i).contains(enemy)) 
						 {
							 southVert.remove(i); i--;
						 }
					 }
				 }
			 }
		 }
		 return (Integer) result;
	}
	
	void path(int direction, int x, int y, GameState s)
	{
		if (direction == 0)//hori
		{
			travWestHori(x, y, s);
			travEastHori(x, y, s);
		}
		else
		{
			travNorthVert(x, y, s);
			travSouthVert(x, y, s);
		}
	}
	
	void reset()
	{
		westHori = new ArrayList<ArrayList<Integer>>();
		singleRunWH = new ArrayList<Integer>();
		eastHori = new ArrayList<ArrayList<Integer>>();
		singleRunEH = new ArrayList<Integer>();
		northVert = new ArrayList<ArrayList<Integer>>();
		singleRunNV = new ArrayList<Integer>();
		southVert = new ArrayList<ArrayList<Integer>>();
		singleRunSV = new ArrayList<Integer>();
	}
	
	ArrayList<ArrayList<Integer>> westHori = new ArrayList<ArrayList<Integer>>();
	ArrayList<Integer> singleRunWH = new ArrayList<Integer>();
	void travWestHori(int x, int y, GameState s)
	{
		ArrayList<int[]> neighbors = westHori(x, y, s);
		for (int[] pos : neighbors)
		{
			singleRunWH.add(pos[0]);
			travWestHori(pos[1], pos[2], s);
		}
		southVert.add(singleRunWH);
	}
	
	ArrayList<int[]> westHori(int x, int y, GameState s)
	{
		ArrayList<int[]> neighbors = new ArrayList<int[]>();
		
		if (y+1 != 7) neighbors.add(new int[] {s.board.get(x).get(y+1), x-1, y+1});//west
		if ((x-1 != -1) && (y+1 != 7)) neighbors.add(new int[] {s.board.get(x-1).get(y+1), x-1, y+1});//northwest
		if ((x+1 != 7) && (y+1 != 7)) neighbors.add(new int[] {s.board.get(x+1).get(y+1), x+1, y+1});//southwest
		
		return neighbors;
	}
	
	ArrayList<ArrayList<Integer>> eastHori = new ArrayList<ArrayList<Integer>>();
	ArrayList<Integer> singleRunEH = new ArrayList<Integer>();
	void travEastHori(int x, int y, GameState s)
	{
		ArrayList<int[]> neighbors = eastHori(x, y, s);
		for (int[] pos : neighbors)
		{
			singleRunEH.add(pos[0]);
			travEastHori(pos[1], pos[2], s);
		}
		eastHori.add(singleRunEH);
	}
	
	ArrayList<int[]> eastHori(int x, int y, GameState s)
	{
		ArrayList<int[]> neighbors = new ArrayList<int[]>();
		
		if (y-1 != -1) neighbors.add(new int[] {s.board.get(x).get(y-1), x, y-1});//east
		if ((x-1 != -1) && (y-1 != -1)) neighbors.add(new int[] {s.board.get(x-1).get(y-1), x-1, y-1});//northeast
		if ((x+1 != 7) && (y-1 != -1)) neighbors.add(new int[] {s.board.get(x+1).get(y-1), x+1, y-1});//southeast
		
		return neighbors;
	}
	
	ArrayList<ArrayList<Integer>> northVert = new ArrayList<ArrayList<Integer>>();
	ArrayList<Integer> singleRunNV = new ArrayList<Integer>();
	void travNorthVert(int x, int y, GameState s)
	{
		ArrayList<int[]> neighbors = northVert(x, y, s);
		for (int[] pos : neighbors)
		{
			singleRunNV.add(pos[0]);
			travNorthVert(pos[1], pos[2], s);
		}
		northVert.add(singleRunNV);
	}

	ArrayList<int[]> northVert(int x, int y, GameState s)
	{
		ArrayList<int[]> neighbors = new ArrayList<int[]>();
		
		if (x-1 != -1) neighbors.add(new int[] {s.board.get(x-1).get(y), x-1, y});//north
		if ((x-1 != -1) && (y+1 != 7)) neighbors.add(new int[] {s.board.get(x-1).get(y+1), x-1, y+1});//northwest
		if ((x-1 != -1) && (y-1 != -1)) neighbors.add(new int[] {s.board.get(x-1).get(y-1), x-1, y-1});//northeast
		
		return neighbors;
	}
	
	ArrayList<ArrayList<Integer>> southVert = new ArrayList<ArrayList<Integer>>();
	ArrayList<Integer> singleRunSV = new ArrayList<Integer>();
	void travSouthVert(int x, int y, GameState s)
	{
		ArrayList<int[]> neighbors = southVert(x, y, s);
		for (int[] pos : neighbors)
		{
			singleRunSV.add(pos[0]);
			travSouthVert(pos[1], pos[2], s);
		}
		southVert.add(singleRunSV);
	}
	
	ArrayList<int[]> southVert(int x, int y, GameState s)
	{
		ArrayList<int[]> neighbors = new ArrayList<int[]>();
		
		if (x+1 != 7)  neighbors.add(new int[] {s.board.get(x+1).get(y), x+1, y});//south
		if ((x+1 != 7) && (y+1 != 7)) neighbors.add(new int[] {s.board.get(x+1).get(y+1), x+1, y+1});//southwest
		if ((x+1 != 7) && (y-1 != -1)) neighbors.add(new int[] {s.board.get(x+1).get(y-1), x+1, y-1});//southeast
		
		return neighbors;
	}
	
}
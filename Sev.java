package VertVSHori;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Sev
{
	ArrayList<ArrayList<Cube>> board = new ArrayList<ArrayList<Cube>>();
	HashMap<Integer, int[]> boardMap = new HashMap<Integer, int[]>();
	int credit1 = 7*7*2;
	int credit2 = 7*7*2;
	int turn = 1; 

	class Cube
	{
		int owner = 0;
		int value;
		Cube(int setValue) {value = setValue;}
	}
	
	
	Sev()
	{
		Random rand = new Random();
		ArrayList<Integer> used = new ArrayList<Integer>();
		for (int x = 0; x < 7; x++)
		{
			ArrayList<Cube> line = new ArrayList<Cube>();
			for (int y = 0; y < 7; y++)
			{
				int randNum;
				while (true) 
				{
					randNum = rand.nextInt(7*7);
					if (!used.contains(randNum)) {used.add(randNum); break;}
				}
				Cube new1 = new Cube(randNum);
				line.add(new1);
				
				int[] coordinates = {x, y};
				boardMap.put(randNum, coordinates);
			}
			board.add(line);
		}
		owned.add(new ArrayList<Cube>()); owned.add(new ArrayList<Cube>());
	}
	
	void print()
	{
		for (int x = 0; x < 7; x++)
		{
			System.out.print("| ");
			for (int y = 0; y < 7; y++)
			{
				if (board.get(x).get(y).owner != 0) 
				{
					if (board.get(x).get(y).owner == 1) System.out.print("** | ");
					else System.out.print("## | ");
				}
				else 
				{
					if (board.get(x).get(y).value < 10) System.out.print(0);
					System.out.print(board.get(x).get(y).value + " | ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}
	
	static void printNwrite(String p) throws IOException
	{
		write.write(p);
		System.out.println(p);
	}
	
	static FileWriter write;
	public static void main(String[] args) throws IOException
	{
		Sev x = new Sev();
		x.print();
		
		ArrayList<ArrayList<Integer>> offer = new ArrayList<ArrayList<Integer>>();//what is availible
		for (int i = 0; i < 50; i++)//0-6 7-13 14-20 21-27 28-34 35-41 42-48
		{
			if (i % 7 == 0) offer.add(new ArrayList<Integer>());
			offer.get(i / 7).add(i);
		}
		
		File dir = new File(".");
		File[] files = dir.listFiles();
		int w = 0;
		for (int f = 0; f < files.length; f++)//checks for sessions
		{
			if (files[f].getName().charAt(0)=='w')
			{
				w++;
				if (f+1 == files.length)
				{
					write = new FileWriter("w"+ w +".txt");
					break;
				}
			}
		}
		if (w == 0) write = new FileWriter("w"+ 0 +".txt");
		
		Scanner in = new Scanner(System.in);
		System.out.print("AI or Manual(A or M): "); 
		String inputChoice = in.next();
		
		int[] credit = {98, 98};
		for (int o = 0; o < 7; o++)
		{
			if (inputChoice.compareToIgnoreCase("M") == 0)
			{
				int[] bet = {0, 0};
				printNwrite("P0 Credits: " + credit[0]);
				printNwrite("Awaiting P1 Bet: ");
		        bet[0]  = Integer.valueOf(in.next());
		        printNwrite("P1 Credits: " + credit[1]);
		        printNwrite("Awaiting P2 Bet: ");
		        bet[1] = Integer.valueOf(in.next());
		        
				int winner = 0;
		        if (Integer.valueOf(bet[0]) > Integer.valueOf(bet[1])) winner = 0;
		        else if (Integer.valueOf(bet[0]) < Integer.valueOf(bet[1])) winner = 1;
		        else if (new Random().nextInt(2) == 0) winner = 0; //tie breaker
		        else winner = 1;
		        
		        credit[winner] -= Integer.valueOf(bet[winner]);
		        printNwrite("P" + winner + " WINS");
		        printNwrite("Choose between: " + Arrays.toString(offer.get(o).toArray()));
		        int pick = 0;
		        while (true)
		        {
		        	pick = Integer.valueOf(in.next());
		        	if (offer.get(o).contains(pick) && !owned.get(0).contains(pick) && !owned.get(1).contains(pick)) break;
		        	else System.out.println("Invalid Input");
		        }
		        int[] coor = x.boardMap.get(Integer.valueOf(pick));
				x.board.get(coor[0]).get(coor[1]).owner = winner+1;
				x.owned.get(0).add(x.board.get(coor[0]).get(coor[1]));
				x.print();
			}
			else 
			{
				int bet1 = x.takeBid1a();
				int bet2 = x.takeBid2a();
		
				if (bet1 < bet2) 
				{
					int[] coor = x.boardMap.get(x.choose(0, offer.get(o)));
					x.board.get(coor[0]).get(coor[1]).owner = 1;
					x.owned.get(0).add(x.board.get(coor[0]).get(coor[1]));
					x.credit1 -= bet1;
					x.print();
				}
				else
				{
					int[] coor = x.boardMap.get(x.choose(1, offer.get(o)));
					x.board.get(coor[0]).get(coor[1]).owner = 2;
					x.owned.get(1).add(x.board.get(coor[0]).get(coor[1]));
					x.credit2 -= bet2;
					x.print();
				}
				x.turn++;
			}
			if (o == 6) o=-1;//loops around
		}
	}
	
	//p2's 13 forces p1 to but 14 six times. p2 bets 15 at least 3 or more to block or win.
	int takeBid1a()
	{
		return 14;
	}
	int takeBid2a()
	{
		if (turn < 7) return 13;
		else 
		{
			if (credit2 >= 15) return 15;
			else return 1;
		}
	}
	
	//p1 lets p2 win with 13 six times thus 10 left. p1 wins with 11 seven times.
	int takeBid1b()
	{
		if (turn < 7) return 0;
		else return 11;
	}
	int takeBid2b()
	{
		if (turn < 7) return 13;
		else 
		{
			if (credit2 >= 15) return 15;
			else return 1;
		}
	}
	
	//p2 bets 1 six times. p1 bets 11 seventh time and p2 bets at least 12 seventh time FTW
	int takeBid1c()
	{
		if (turn < 7) return 0;
		else return 11;
	}
	int takeBid2c()
	{
		if (turn < 7) return 1;
		else return 12;//to 92
	}
	
	//p2 bets 1 six times but p1 bets 2 six times. p2 wins seventh with 92 but doesnt win. 
	int takeBid1d()
	{
		if (turn < 7) return 2;
		else return 1;//to 84
	}
	int takeBid2d()
	{
		if (turn < 7) return 1;
		else return 12;//to 92
	}
	
	int bet1e = 2;
	boolean win1e = true;
	int takeBid1e()
	{
		if (!win1e) {bet1e += 2; win1e = true; win2e = false;}
		return bet1e;
	}
	int bet2e = 1;
	boolean win2e = false;
	int takeBid2e()
	{
		if (!win2e) {bet2e += 2; win2e = true; win1e = false;}
		return bet2e;
	}
	
	int bet1f = 14;
	boolean win1f = true;
	int takeBid1f()
	{
		if (!win1f) {bet1f += 2; win1f = true; win2f = false;}
		return bet1f;
	}
	int bet2f = 15;
	boolean win2f = false;
	int takeBid2f()
	{
		if (!win2f) {bet2f += 2; win2f = true; win1f = false;}
		return bet2f;
	}
	
	static ArrayList<ArrayList<Cube>> owned = new ArrayList<ArrayList<Cube>>();
	int choose(int id, ArrayList<Integer> offer)
	{
		int result = 0;
		if (owned.get(id).size() == 0)//first move take middle
		{
			 for (int x = 2; x < 5; x++)
			 {
				 for (int y = 2; y < 5; y++)
				 {
					 if (offer.contains(board.get(x).get(y).value) 
					 && !owned.get(0).contains(board.get(x).get(y))
					 && !owned.get(1).contains(board.get(x).get(y)))
					 {
						 path(id, x, y); 
						 return board.get(x).get(y).value;
					 }
				 }
			 }
			 
			 for (int x = 0; x < 7; x++) //what does this do?
			 { 
				 for (int y = 0; y < 7; y++) 
				 {
					 if (board.get(x).get(y).value == offer.get(0))
					 {
						 path(id, x, y);
					 }
				 }
			 }
			 return offer.get(0);
		}
		else
		{
			if (id == 0)//hori
			{
				for (int i = 0; i < westHori.size(); i++)
				{
					for (int o : offer) if (westHori.get(i).contains(o) && board.get(boardMap.get(o)[0]).get(boardMap.get(o)[1]).owner != id) result = o;
					for (Cube enemy: owned.get(id^1)) if (westHori.get(i).contains(enemy)) {westHori.remove(i); i--;}
				}
					 
				for (int i = 0; i < eastHori.size(); i++)
				{
					for (int o : offer) if (eastHori.get(i).contains(o) && board.get(boardMap.get(o)[0]).get(boardMap.get(o)[1]).owner != id) result = o;
					for (Cube enemy: owned.get(id^1)) if (eastHori.get(i).contains(enemy)) {eastHori.remove(i); i--;}
				} 
			}
			else
			{//something to handle the chosen x's path.
				boolean once = true;
				ArrayList<ArrayList<Integer>> replaceNV = new ArrayList<ArrayList<Integer>>();
				for (int i = 0; i < northVert.size(); i++)
				{
					for (int o : offer) 
					{
						if (northVert.get(i).contains(o) && board.get(boardMap.get(o)[0]).get(boardMap.get(o)[1]).owner != id) 
						{
							if (once) {result = o; once = false;}
							northVert.get(i).remove((Integer)o);
							for (Object x : northVert.get(i))
							{
								if (x != null)
								{
									replaceNV.add(northVert.get(i));
									break;
								}
							}
						}
					}
					for (Cube enemy: owned.get(id^1)) if (northVert.get(i).contains(enemy)) {northVert.remove(i); i--;}
				}
				northVert.clear();
				northVert.addAll(replaceNV);
				
				ArrayList<ArrayList<Integer>> replaceSV = new ArrayList<ArrayList<Integer>>();
				for (int i = 0; i < southVert.size(); i++)
				{
					for (int o : offer) 
					{
						if (southVert.get(i).contains(o) && board.get(boardMap.get(o)[0]).get(boardMap.get(o)[1]).owner != id) 
						{
							if (once) {result = o; once = false;}
							southVert.get(i).remove((Integer)o);
							for (Object x : southVert.get(i))
							{
								if (x != null)
								{
									replaceSV.add(southVert.get(i));
									break;
								}
							}
						}
					}
					for (Cube enemy: owned.get(id^1)) if (southVert.get(i).contains(enemy)) {southVert.remove(i); i--;}
				}
				southVert.clear();
				southVert.addAll(replaceNV);
			}
		 }
		 return (Integer) result;
	}
	
	void path(int direction, int x, int y)
	{
		if (direction == 0)//hori
		{
			travWestHori(x, y);
			travEastHori(x, y);
		}
		else
		{
			travNorthVert(x, y);
			travSouthVert(x, y);
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
	void travWestHori(int x, int y)
	{
		ArrayList<int[]> neighbors = westHori(x, y);
		for (int[] pos : neighbors)
		{
			singleRunWH.add(pos[0]);
			travWestHori(pos[1], pos[2]);
		}
		if (x == 0)
		{
			ArrayList<Integer> clone = new ArrayList<Integer>(singleRunWH.size());
			clone.addAll(singleRunWH);
			westHori.add(clone);
		}
		if (singleRunWH.size() != 0) singleRunWH.remove(singleRunWH.size()-1);
	}
	
	ArrayList<int[]> westHori(int x, int y)
	{
		ArrayList<int[]> neighbors = new ArrayList<int[]>();
		
		if ((x-1 != -1) && (y+1 != 7)) neighbors.add(new int[] {board.get(x-1).get(y+1).value, x-1, y+1});//northwest
		if (y+1 != 7) neighbors.add(new int[] {board.get(x).get(y+1).value, x-1, y+1});//west
		if ((x+1 != 7) && (y+1 != 7)) neighbors.add(new int[] {board.get(x+1).get(y+1).value, x+1, y+1});//southwest
		
		return neighbors;
	}
	
	ArrayList<ArrayList<Integer>> eastHori = new ArrayList<ArrayList<Integer>>();
	ArrayList<Integer> singleRunEH = new ArrayList<Integer>();
	void travEastHori(int x, int y)
	{
		ArrayList<int[]> neighbors = eastHori(x, y);
		for (int[] pos : neighbors)
		{
			singleRunEH.add(pos[0]);
			travEastHori(pos[1], pos[2]);
		}
		if (x == 0)
		{
			ArrayList<Integer> clone = new ArrayList<Integer>(singleRunEH.size());
			clone.addAll(singleRunEH);
			eastHori.add(clone);
		}
		if (singleRunEH.size() != 0) singleRunEH.remove(singleRunEH.size()-1);
	}
	
	ArrayList<int[]> eastHori(int x, int y)
	{
		ArrayList<int[]> neighbors = new ArrayList<int[]>();
		
		if ((x+1 != 7) && (y-1 != -1)) neighbors.add(new int[] {board.get(x+1).get(y-1).value, x+1, y-1});//southeast
		if (y-1 != -1) neighbors.add(new int[] {board.get(x).get(y-1).value, x, y-1});//east
		if ((x-1 != -1) && (y-1 != -1)) neighbors.add(new int[] {board.get(x-1).get(y-1).value, x-1, y-1});//northeast
		
		return neighbors;
	}
	
	ArrayList<ArrayList<Integer>> northVert = new ArrayList<ArrayList<Integer>>();
	ArrayList<Integer> singleRunNV = new ArrayList<Integer>();
	void travNorthVert(int x, int y)
	{
		ArrayList<int[]> neighbors = northVert(x, y);
		for (int[] pos : neighbors)
		{
			singleRunNV.add(pos[0]);
			travNorthVert(pos[1], pos[2]);
		}
		if (x == 0)
		{
			ArrayList<Integer> clone = new ArrayList<Integer>(singleRunNV.size());
			clone.addAll(singleRunNV);
			northVert.add(clone);
		}
		if (singleRunNV.size() != 0) singleRunNV.remove(singleRunNV.size()-1);
	}

	ArrayList<int[]> northVert(int x, int y)
	{
		ArrayList<int[]> neighbors = new ArrayList<int[]>();
		
		if ((x-1 != -1) && (y-1 != -1)) neighbors.add(new int[] {board.get(x-1).get(y-1).value, x-1, y-1});//northeast
		if (x-1 != -1) neighbors.add(new int[] {board.get(x-1).get(y).value, x-1, y});//north
		if ((x-1 != -1) && (y+1 != 7)) neighbors.add(new int[] {board.get(x-1).get(y+1).value, x-1, y+1});//northwest
		
		return neighbors;
	}
	
	ArrayList<ArrayList<Integer>> southVert = new ArrayList<ArrayList<Integer>>();
	ArrayList<Integer> singleRunSV = new ArrayList<Integer>();
	void travSouthVert(int x, int y)
	{
		ArrayList<int[]> neighbors = southVert(x, y);
		for (int[] pos : neighbors)
		{
			singleRunSV.add(pos[0]);
			travSouthVert(pos[1], pos[2]);
		}
		if (x == 0)
		{
			ArrayList<Integer> clone = new ArrayList<Integer>(singleRunSV.size());
			clone.addAll(singleRunSV);
			southVert.add(clone);
		}
		if (singleRunSV.size() != 0) singleRunSV.remove(singleRunSV.size()-1);
	}
	
	ArrayList<int[]> southVert(int x, int y)
	{
		ArrayList<int[]> neighbors = new ArrayList<int[]>();
		
		if ((x+1 != 7) && (y+1 != 7)) neighbors.add(new int[] {board.get(x+1).get(y+1).value, x+1, y+1});//southwest
		if (x+1 != 7)  neighbors.add(new int[] {board.get(x+1).get(y).value, x+1, y});//south
		if ((x+1 != 7) && (y-1 != -1)) neighbors.add(new int[] {board.get(x+1).get(y-1).value, x+1, y-1});//southeast
		
		return neighbors;
	}
}

package VertVSHori;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import VertVSHori.Sev.Cube;

public class VertVSHori
{
	ArrayList<ArrayList<Sqr>> board = new ArrayList<ArrayList<Sqr>>();
	HashMap<Integer, int[]> sqr2coor = new HashMap<Integer, int[]>();
	ArrayList<ArrayList<Integer>> offer = new ArrayList<ArrayList<Integer>>();
	ArrayList<ArrayList<Sqr>> owned = new ArrayList<ArrayList<Sqr>>();
	
	class Sqr
	{
		int owner = 0;
		int value;
		Sqr(int setValue) {value = setValue;}
	}
	
	VertVSHori()
	{
		board.add(null);
		Random rand = new Random();
		ArrayList<Integer> used = new ArrayList<Integer>();
		for (int x = 1; x < 8; x++)
		{
			ArrayList<Sqr> line = new ArrayList<Sqr>();
			line.add(null);
			for (int y = 1; y < 8; y++)
			{
				int randNum;
				while (true) 
				{
					randNum = rand.nextInt(7*7);
					if (!used.contains(randNum)) {used.add(randNum); break;}
				}
				Sqr new1 = new Sqr(randNum);
				line.add(new1);
				
				int[] coordinates = {x, y};
				sqr2coor.put(randNum, coordinates);
			}
			board.add(line);
		}
		
		for (int i = 0; i < 50; i++)//0-6 7-13 14-20 21-27 28-34 35-41 42-48
		{
			if (i % 7 == 0) offer.add(new ArrayList<Integer>());
			offer.get(i / 7).add(i);
		}
		owned.add(new ArrayList<Sqr>()); owned.add(new ArrayList<Sqr>());
	}
	
	void print()
	{
		for (int x = 1; x < 8; x++)
		{
			System.out.print("| ");
			for (int y = 1; y < 8; y++)
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
	
	Scanner in = new Scanner(System.in);
	public static void main(String[] args)
	{
		VertVSHori vs = new VertVSHori();
		vs.print();
		
		//System.out.print("AI or Manual(A or M): "); 
		String inputChoice = "M";//in.next();
		if (inputChoice.compareToIgnoreCase("M") == 0)
		{
			vs.manual();
		}
	}
	
	int[] credit = {98, 98};
	void manual()
	{
		for (int o = 0; o < 7; o++)
		{
		int[] bet = {0, 0};
		
		System.out.println(Arrays.toString(offer.get(o).toArray()));
		
		//System.out.println("P0 Credits: " + credit[0]);
		System.out.println("Awaiting P0 Bet: ");
        bet[0]  = Integer.valueOf(in.next());
        
        for (int i = 0; i < 100; i++) System.out.println("----------------------------");
        
		System.out.println(Arrays.toString(offer.get(o).toArray()));
		print();
        //System.out.println("P1 Credits: " + credit[1]);
        System.out.println("Awaiting P1 Bet: ");
        bet[1] = Integer.valueOf(in.next());
        
        for (int i = 0; i < 100; i++) System.out.println("----------------------------");
        
		int winner = 0;
        if (Integer.valueOf(bet[0]) > Integer.valueOf(bet[1])) winner = 0;
        else if (Integer.valueOf(bet[0]) < Integer.valueOf(bet[1])) winner = 1;
        else if (new Random().nextInt(2) == 0) winner = 0; //tie breaker
        else winner = 1;
        
        print();
        
        int pick = 0;
        credit[winner] -= Integer.valueOf(bet[winner]);
        System.out.println("P" + winner + " WINS");
        System.out.println("Choose between: " + Arrays.toString(offer.get(o).toArray()));
        while (true)
        {
        	pick = Integer.valueOf(in.next());
        	if (offer.get(o).contains(pick) && !owned.get(0).contains(pick) && !owned.get(1).contains(pick)) break;
        	else System.out.println("Invalid Input");
        }
        int[] coor = sqr2coor.get(Integer.valueOf(pick));
		board.get(coor[0]).get(coor[1]).owner = winner+1;
		owned.get(0).add(board.get(coor[0]).get(coor[1]));
		print();
		if (o == 6) o=-1;
		}
	}

}

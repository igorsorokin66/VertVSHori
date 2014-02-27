package com.barracuda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Calculate
{
     List<Integer> path = new ArrayList<Integer>();
    HashMap<Integer, int[]> boardMap = new HashMap<Integer, int[]>();

     void init(Map<String, Object> state)
     {
         GameState gameState = new GameState(state);

         for(int a = 0; a < 7; ++a)
         {
             for(int b = 0; b < 7; ++b)
             {
                 int[] coordinates = {a, b};
                 boardMap.put(gameState.board.get(a).get(b), coordinates);
             }
         }

            if (gameState.turn == 0)
            {
                if (gameState.idx != 0)//vertical
                 {
                     for (int i = 0; i < 7; i++)
                     {
                         path.add(gameState.board.get(i).get(3));
                         gameState.toString();
                     }
                 }
                 else//horizontal
                 {
                     for (int save : gameState.board.get(3))
                     {
                         path.add(save);
                     }
                 }
            }
     }

    Integer get_bid(List<Integer> offer, Map<String, Object> state)
    {
        GameState gameState = new GameState(state);
        boolean horizontal = (gameState.idx == 0) ? true : false;

        if (horizontal)
        {
            //for (int check : path) //Exception
            for(int i = 0; i < path.size(); ++i)
            {
                int check = path.get(i);

                for (int check1 : gameState.owned_squares.get(1))
                {
                    if (check == check1)
                    {
                        path.remove((Integer) check);
                        int[] coordinates = boardMap.get(check);

                        if (gameState.board.get(coordinates[0]).get(coordinates[1]) == check)
                        {
                            if (coordinates[0]+1 != 7 && gameState.owned_squares.get(1).contains(gameState.board.get(coordinates[0]+1).get(coordinates[1])))
                            {
                                path.add(gameState.board.get(coordinates[0]-1).get(coordinates[1]));
                            }
                            else if(coordinates[0]-1 != -1)
                            {
                                path.add(gameState.board.get(coordinates[0]+1).get(coordinates[1]));
                            }
                        }
                    }
                }
            }
        }
        else
        {
            //for (int check : path) //Exception
            for(int i = 0; i < path.size(); ++i)
            {
                int check = path.get(i);

                for (int check1 : gameState.owned_squares.get(0))
                {
                    if (check == check1)
                    {
                        path.remove((Integer) check);

                        int[] coordinates = boardMap.get(check);
                        if (gameState.board.get(coordinates[0]).get(coordinates[1]) == check)
                        {
                            if (coordinates[1] +1 != 7 && gameState.owned_squares.get(0).contains(gameState.board.get(coordinates[0]).get(coordinates[1]+1)))
                            {
                                path.add(gameState.board.get(coordinates[0]).get(coordinates[1]-1));
                            }
                            else if(coordinates[1]-1 != -1)
                            {
                                path.add(gameState.board.get(coordinates[0]).get(coordinates[1]+1));
                            }
                        }
                    }
                }
            }
        }

        for (int check : offer)
        {
            if (path.contains(check)) return 14;
        }
        return 0;
    }

    Integer make_choice(List<Integer> offer, Map<String, Object> state)
    {
         GameState gameState = new GameState(state);

         List<Integer> availible = new ArrayList<Integer>(offer);
       for (int check : offer)
       {
         if (!gameState.owned_squares.get(0).contains(check)
         && !gameState.owned_squares.get(1).contains(check)) availible.add(check);
       }

       for (int check : path)
       {
         for (int check1: availible)
         {
             if (check == check1) {path.remove((Integer) check); return check;}
         }
       }

       int choice = availible.get((int) Math.random() % availible.size());

        return choice;

        //List<Integer> middle = new ArrayList<Integer>();

    }
}

import java.io.*;
import java.util.*;

public class Solution
{

    public static void main(String[] args) 
    {
       //input test values here
       System.out.println(canReach(3,-2,-1,-9));
    }
    
    public static boolean canReach(int x1, int y1, int x2, int y2)
    {   
        boolean x1Pos=x1>=0;
        boolean y1Pos=y1>=0;
        boolean x2Pos=x2>=0;
        boolean y2Pos=y2>=0;
        //both positive and any negative
        if(x1Pos && y1Pos)
        {
            if(!(x2Pos && y2Pos))return false;
            return canReachPositives(x1,y1,x2,y2);
        }
        //both negative
        else if(!x1Pos && !y1Pos)
        {
            //any positive
            if(x2Pos || y2Pos)return false;
            //turns negatives into positives
            //ignores Integer.MIN_VALUE cases (could have used long)
            x1=Math.abs(x1);
            y1=Math.abs(y1);
            x2=Math.abs(x2);
            y2=Math.abs(y2);
            return canReachPositives(x1,y1,x2,y2);
        }
        //if in form x1:- y1:+ I want the reverse
        else if(!x1Pos && y1Pos)
        {
            //if target is lower or greater than source cannot be done
            if(x2<x1 || y1>y2)return false;
            x1=x1 ^ y1;
            y1=x1 ^ y1;
            x1=x1 ^ y1;
            if(x2!=y2)
            {
                x2=x2^y2;
                y2=x2^y2;
                x2=x2^y2;
            }
        }
        return canReachMixed(x1,y1,x2,y2);
    }
    
    public static boolean canReachPositives(int x1,int y1, int x2, int y2)
    {
        ArrayList<int[]> al=new ArrayList<int[]>();
        //do not add duplicate paths to the list
        HashSet<String> uniqueStates=new HashSet<String>();
        al.add(new int[]{x1,y1});
        uniqueStates.add(x1+"-"+y1);
        int index=0;
        while(index < al.size())
        {
            int[] state=al.get(index);
            //check if matching
            if(state[0]==x2 && state[1]==y2)return true;
            
            int combine=state[0]+state[1];
            if(combine <= y2 && !uniqueStates.contains(state[0]+"-"+combine))
            {
                al.add(new int[]{state[0],combine});
                uniqueStates.add(state[0]+"-"+combine);
            }
            if(combine <= x2 && !uniqueStates.contains(combine+"-"+state[1]))
            {
                al.add(new int[]{combine,state[1]});
                uniqueStates.add(combine+"-"+state[1]);
            }
            index++;
        }
        //base
        return false;
    }
    
    //in form  x1:+ y1:-
    public static boolean canReachMixed(int x1,int y1,int x2, int y2)
    {
        ArrayList<int[]> al=new ArrayList<int[]>();
        HashSet<String> uniqueStates=new HashSet<String>();
        al.add(new int[]{x1,y1});
        uniqueStates.add(x1+"-"+y1);
        int index=0;
        //form x2:+ y2:-
        if(x2>=0 && x2<=x1 && y2<0 && y2>=y1)
        {
            while(index<al.size())
            {
                int[] state=al.get(index);
                if(state[0]==x2 && state[1]==y2)return true;
                int combine=state[0]+state[1];
                if(combine>=x2)al.add(new int[]{combine,state[1]});
                if(combine<=y2)al.add(new int[]{state[0],combine});
                index++;
            }
        }
        //form x2:+ y2:+
        else if(x2>=0 && y2>=0)
        {
            while(index<al.size())
            {
                int[] state=al.get(index);
                if(state[0]==x2 && state[1]==y2)return true;
                int combine=state[0]+state[1];
                if(combine>=0 && combine<=x2 && !uniqueStates.contains(combine+"-"+state[1]))
                {
                    al.add(new int[]{combine,state[1]});
                    uniqueStates.add(combine+"-"+state[1]);
                }
                if(combine>=0 && combine<=y2 && !uniqueStates.contains(combine+"-"+state[1]))
                {
                    al.add(new int[]{state[0],combine});
                    uniqueStates.add(state[0]+"-"+combine);
                }
                //must be unique
                else if(combine<0)al.add(new int[]{state[0],combine});
                index++;
            }
        }
        //form x2:- y2:-
        else if(x2<0 && y2<0)
        {
            while(index<al.size())
            {
                int[] state=al.get(index);
                if(state[0]==x2 && state[1]==y2)return true;
                int combine=state[0]+state[1];
                if(combine<0 && combine>=x2 && !uniqueStates.contains(combine+"-"+state[1]))
                {
                    al.add(new int[]{combine,state[1]});
                    uniqueStates.add(combine+"-"+state[1]);
                }
                if(combine<0 && combine>=y2 && !uniqueStates.contains(combine+"-"+state[1]))
                {
                    al.add(new int[]{state[0],combine});
                    uniqueStates.add(state[0]+"-"+combine);
                }
                //must be unique
                else if(combine>0)al.add(new int[]{combine,state[1]});
                index++;
            }
        }
        return false;
    }
}

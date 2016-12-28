/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * @author Alex Kappelmann
 */

import java.util.Queue;
import java.util.ArrayList;
import java.util.*;

public class CanVmisProb {
    //create a LIFO queue for unexplored valid States
    
    State SolutionState;
    int count = 0;
    
    ArrayList<State> actions = new ArrayList<>(); //List of the 5 possible moves
    
    public CanVmisProb() {
        
        actions.add(new State(1,0,1));
        actions.add(new State(2,0,1));
        actions.add(new State(0,1,1));
        actions.add(new State(0,2,1));
        actions.add(new State(1,1,1));
    }
    
    //returns an arrayList of the Solution Path or null if no solution is found
    public ArrayList<State> findSolution(State startState) {
        
        
       //State result = RecursiveDLS(startState,20,1);
       State result = IDS(startState, 20);
        if (result  != null) {
            System.out.println("\n***Solution was Found***");
            return getSolutionPath(result);
        }
        else 
            return null;
    }

    public State IDS(State root, int cutoff) {
        
        if (root.isSolution()) return root;
        
        State result = null;

        for (int i = 1; result == null; i++) {
            System.out.println("limit: "+i);
            result =  RecursiveDLS(root,i, 0);
            if (result != null && result.isSolution())
                return result;
        }
        return result;
    }
    
    public State RecursiveDLS(State cur,int limit, int depth) {
        
        System.out.print("count:"+count+" curLim: "+ depth+"  ");
        System.out.println(cur.toString());
        if (depth < limit) {
            State result, child;  
            for (State a: actions) {  
                child = cur.getChild(a);
                if (child != null && child.isValid()) {
                    count++;
                    if (child.isSolution()) return child;
                    if ((result = RecursiveDLS(child,limit, depth+1)) != null)
                        return result;
                     
                }
            }
        }
        return null;
    }           
    
    //Traces the solution's path back to the initial state and returns an
    //ArrayList of all the states that lead to the solution
    public ArrayList<State> getSolutionPath(State solutionState) {
        
        ArrayList<State> solutionPath = new ArrayList<>();
        solutionPath.add(solutionState);
        State par = solutionState.parent;
        
        while (par != null) {
            solutionPath.add(par);
            par = par.parent;
        }
        
        Collections.reverse(solutionPath);
        return solutionPath;   
    }
    public static void main(String[] args) {
        
        ArrayList<State> solution = new CanVmisProb().findSolution(new State(3,3,1));
        if (solution == null) System.out.println("No Solution Found");
        else {
        solution.stream().forEach((s) -> {
            System.out.println(s.toString());
        });
        }
    }
    
   /*********************************************************************
    * This class represents the different states of the Cannibals vs.   *
    * Missionaries Problem. The 'parent' variable is used to trace the  *
    * path of the solution back to the root, and the 'prevAct' variable * 
    * is used keep the search tree unidirectional, or to stop a state   *
    * from making its child state the same as its parent.               *
    *********************************************************************/
    public static class State {
        public int numMis;
        public int numCan;
        public int numBoats;
        public int depth;
        public State parent;
        public State prevAct;
        
        //used for the starting state and action states
        public State(int m, int c, int b) {
            numMis = m;
            numCan = c;
            numBoats = b;
            parent = null;
            depth = 0;
            prevAct = null;

        }
        //used for descendent states
        public State(int m, int c, int b, State p, int d) {
            numMis = m;
            numCan = c;
            numBoats = b;
            parent = p;
            depth = d;
            prevAct = null;
        }
        
        //checks if a State is valid
        public boolean isValid() {
            
            if ( numMis > 3 || numMis < 0 ||
                 numCan > 3 || numCan < 0 ||
                 (numMis < numCan && numMis > 0) ||
                 (3-numMis < 3-numCan && 3-numMis > 0) )
                 return false;
            
            else
                return true;
        }
        
        //returns a child state based on an action state
        public State getChild(State a) {
            
            if (prevAct != null && prevAct == a) return null;
          
            State newState;
            if(numBoats == 1) 
                newState = new State(numMis - a.numMis,
                                     numCan - a.numCan,
                                     numBoats - a.numBoats, 
                                     this, this.depth + 1);
            
            else 
                newState = new State(numMis + a.numMis,
                                     numCan + a.numCan,
                                     numBoats + a.numBoats,
                                     this, this.depth + 1);
            newState.prevAct = a;
            return newState;
        }
        
        public boolean isSolution() { return numMis == 0 && numCan == 0; }
        
        @Override
        public String toString() {
            if (parent != null) 
                return "par: <"+parent.numMis+", "+parent.numCan+", "+parent.numBoats+"> ---> "
                     + "chld: <"+numMis+", "+numCan+", "+numBoats+">  "+ "depth:"+depth;
          else
            return "root:<"+numMis+", "+numCan+", "+ numBoats+">";
        }
    }
}
        
        
    


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.states;

import java.awt.Graphics;
import java.util.HashMap;
import java.util.Stack;

public class StateMachine 
{
    private HashMap<String, IState> stateMap;
    private Stack<String> stateStack;
    
    public StateMachine()
    { 
        stateMap = new HashMap();
        stateStack = new Stack();
    }
    
    public void update(double elapsedTime)
    {
        if(!stateStack.empty())
        {
            stateMap.get(stateStack.peek()).update(elapsedTime);
        }
    }
    
    public void render(Graphics g)
    {
        if(!stateStack.empty())
        {
            stateMap.get(stateStack.peek()).render(g);
        }
    }
    
    public void change(String stateName)
    {
        String current = "";
        if(stateStack.size() >= 1)
        {
            current = stateStack.peek();
            if(!current.equals(stateName))
            {
                stateMap.get(stateStack.peek()).onExit();
                stateStack.pop();
            }
        }
        if(!current.equals(stateName))
        {
            stateStack.push(stateName);
            stateMap.get(stateStack.peek()).onEnter();
        }
    }
    
    public void push(String stateName)
    {
//        if(!stateStack.empty())
//        {
//            //stateMap.get(stateStack.peek()).OnExit();
//        }
        stateStack.push(stateName);
        stateMap.get(stateStack.peek()).onEnter();
        //System.out.println("StateMachine: "+stateName+" was pushed on the stack");
    }
    
    public String pop()
    {
        String poppedState = null;
        if(stateStack.size() > 1)
        {
            stateMap.get(stateStack.peek()).onExit();
            poppedState = stateStack.pop();
            //System.out.println("Statemachine: Pop(): returning to " + stateStack.peek());
            //System.out.println("StateMachine: "+poppedState+" was popped of the stack");
        }
        else
        {
            System.out.println("StateMachine: Not enough states on stack to pop");
        }
//        if(!stateStack.empty())
//        {
//            stateMap.get(stateStack.peek()).OnEnter();
//        }
        return poppedState;
    }
    
    public void add(String stateName, IState state)
    {
        stateMap.put(stateName, state);
        //System.out.println("StateMachine: " + stateName + " added to stateMap");
    }
    
    public String getCurrentName()
    {
        return stateStack.peek();
    }
    
    public IState getCurrentState()
    {
        return stateMap.get(stateStack.peek());
    }

    public IState getState(String stateName) 
    {
        return stateMap.get(stateName);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.states;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import pokemon2.main.Handler;

public class PlayerMenu implements IState
{
    private Handler handler;
    private long lastInput, coolDown;
    
    public PlayerMenu(Handler handler)
    {
        this.handler = handler;
        coolDown = 500;
    }

    @Override
    public void onEnter() 
    {
        System.out.println("Entering PlayerMenu State");
        lastInput = System.currentTimeMillis();
    }

    @Override
    public void onExit() 
    {
        System.out.println("Exiting PlayerMenu State");
    }

    @Override
    public void update(double elapsedTime) 
    {
        if(System.currentTimeMillis() - lastInput > coolDown)
        {
            if(handler.getKeyManager().keys[KeyEvent.VK_A])
            {
                lastInput = System.currentTimeMillis();
                handler.getPlayer().addMoney(10);
            }
            if(handler.getKeyManager().keys[KeyEvent.VK_ESCAPE])
            {
                handler.getStateMachine().pop();
            }
        }
        
    }

    @Override
    public void render(Graphics g) 
    {
        g.setColor(Color.WHITE);
        g.drawString(handler.getPlayer().getName(), 50 , 10);
        g.drawString("Money: " + Integer.toString(handler.getPlayer().getMoney()) + "$", 50, 30);
    }
    
}

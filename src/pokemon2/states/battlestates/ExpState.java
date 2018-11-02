/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.states.battlestates;

import java.awt.Color;
import java.awt.Graphics;
import pokemon2.assets.Assets;
import pokemon2.combat.Creature;
import pokemon2.customui.MessageBox;
import pokemon2.main.Handler;
import pokemon2.states.BattleState;
import pokemon2.states.IState;

public class ExpState implements IState
{
    private Handler handler;
    private BattleState battleState;
    private MessageBox messageBox;
    private int state;
    private long displayTime, lastTime;
    private int lastLevel;
    
    public ExpState(Handler handler, BattleState battleState)
    {
        this.handler = handler;
        this.battleState = battleState;
        this.messageBox = battleState.getMessageBox();
        displayTime = 2000;
    }

    @Override
    public void onEnter() 
    {
        System.out.println("Entering ExpState");
        state = 0;
        lastTime = System.currentTimeMillis() - displayTime;
    }

    @Override
    public void onExit() 
    {
        System.out.println("Exiting ExpState");
    }

    @Override
    public void update(double elapsedTime) 
    {
        switch(state)
        {
            case 0:
                if(System.currentTimeMillis() - lastTime > displayTime)
                {
                    if(!battleState.getExpList().isEmpty())
                    {
                        String[] split = battleState.getExpList().get(0).split(",");
                        for(Creature c: handler.getPlayer().getPokemonLeft())
                        {
                            if(c.getName().equals(split[0]))
                            {
                                lastLevel = c.getLevel();
                                c.addExperience(Integer.parseInt(split[1]));
                                messageBox.setText(c.getName() + " received " + split[1] + " experience.");
                                if(c.getLevel() > lastLevel)
                                {
                                    state = 1;
                                }
                                else
                                {
                                    battleState.getExpList().remove(0);
                                }
                            }
                        }
                        lastTime = System.currentTimeMillis();
                    }
                    else
                    {
                        battleState.getBattleStateMachine().pop();
                    }
                }
                break;
            case 1:
                if(System.currentTimeMillis() - lastTime > displayTime)
                {
                    String[] split = battleState.getExpList().get(0).split(",");
                    for(Creature c: handler.getPlayer().getPokemonLeft())
                    {
                        if(c.getName().equals(split[0]))
                        {
                            c.addExperience(Integer.parseInt(split[1]));
                            messageBox.setText(c.getName() + " leveled up!");
                            battleState.getExpList().remove(0);
                            state = 0;                                
                        }
                    }
                    lastTime = System.currentTimeMillis();
                }
                break;
        }
    }

    @Override
    public void render(Graphics g) 
    {
        g.drawImage(Assets.bbg[2], 50, 0, 500, 300, null);
        
        for(Creature c: battleState.getCreatures())
        {
            c.render(g);
        }
        
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(50, 300, 500, 100);
        g.setColor(Color.BLACK);
        g.drawRect(50, 300, 500, 100);
        
        g.setColor(Color.BLACK);
        g.drawString("CatchState", 100, 10);
        
        messageBox.render(g);
    }

}

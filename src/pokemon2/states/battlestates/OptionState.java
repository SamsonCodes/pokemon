/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.states.battlestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.Random;
import pokemon2.assets.Assets;
import pokemon2.combat.Creature;
import pokemon2.combat.Pokemon;
import pokemon2.customui.OptionPanel;
import pokemon2.main.Handler;
import pokemon2.states.BattleState;
import pokemon2.states.IState;

public class OptionState implements IState
{
    private Handler handler;
    private BattleState battleState;
    private OptionPanel optionPanel;
    private Creature[] creatures;
    private boolean escapeFail;
    private long failTime, displayTime;
    
    public OptionState(Handler handler, BattleState battleState)
    {
        this.handler = handler;
        this.battleState = battleState;
        creatures = battleState.getCreatures();
        displayTime = 3000;
    }

    @Override
    public void onEnter() 
    {
        if(!creatures[1].foughtWith.contains(creatures[0]))
        {
            creatures[1].foughtWith.add(creatures[0]);
        }
        System.out.println("Entering option state");
        setUpOptionPanel();
        
        escapeFail = false;
    }
    
    public void setUpOptionPanel()
    {
        String[] options;
            if(handler.getOpponent() == null)
                options = new String[4];
            else
                options = new String[3];
            options[0] = "Fight";
            options[1] = "Switch";
            options[2] = "Item";
            if(handler.getOpponent() == null)
                options[3] = "Run";        
//            options = new String[1];
//            options[0] = "Fight";
            optionPanel = new OptionPanel(handler, options, 50, 300, 150, 50, 2, 1);
    }

    @Override
    public void onExit() 
    {
        System.out.println("Exiting option state");
    }

    @Override
    public void update(double elapsedTime) 
    {
        //If the player has switched pokemon the program will proceed to the move state.
        //Then, only the AI move will be generated before proceeding to 
        //the execution state. So, basically, if you switch you don't get to attack.
        if(battleState.getMoved())
        {
            battleState.getBattleStateMachine().change(BattleState.MOVE);
        }
        else
        {
            if(!escapeFail)
            {
                optionPanel.tick();
                //When a choice has been made
                if(optionPanel.getChoice(0) != -1)
                {
                    switch(optionPanel.getChoice(0))
                    {
                        case 0:
                            battleState.getBattleStateMachine().change(BattleState.MOVE);
                            break;
                        case 1:
                            setUpOptionPanel();
                            battleState.getBattleStateMachine().push(BattleState.SWITCH);
                            break;
                        case 2:
                            setUpOptionPanel();
                            battleState.getBattleStateMachine().push(BattleState.ITEMS);
                            break;
                        case 3:
                            Random random = new Random();
                            if(random.nextDouble() < battleState.getCreatures()[0].getStats(Pokemon.SPEED) / (2 * battleState.getCreatures()[1].getStats(Pokemon.SPEED)))
                            {
                                battleState.exit();
                            }
                            else
                            {
                                battleState.getMessageBox().setText("Failed to get away!");
                                System.out.println("Failed to get away!");
                                failTime = System.currentTimeMillis();
                                escapeFail = true;
                            }
                            break;
                        default:
                            battleState.getBattleStateMachine().change(BattleState.MOVE);
                            break;
                    }
                }
            }
            else if(System.currentTimeMillis() - failTime > displayTime)
            {
                battleState.setMoved(true);
                battleState.getBattleStateMachine().change(BattleState.MOVE);
            }
        }
    }

    @Override
    public void render(Graphics g) 
    {
        g.drawImage(Assets.bbg[2], 50, 0, 500, 300, null);
        
        for(Creature c: creatures)
        {
            c.render(g);
        }
        
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(50, 300, 500, 100);
        g.setColor(Color.BLACK);
        g.drawRect(50, 300, 500, 100);
        
        g.setColor(Color.BLACK);
        g.drawString(BattleState.OPTION, 100, 10);
        
        if(!escapeFail)
        {
            optionPanel.render(g);
        }
        else
        {
            battleState.getMessageBox().render(g);
        }
    }

}

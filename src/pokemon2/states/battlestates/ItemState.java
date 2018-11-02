/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.states.battlestates;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import pokemon2.combat.Creature;
import pokemon2.combat.ItemObject;
import pokemon2.customui.OptionPanel;
import pokemon2.main.Handler;
import pokemon2.states.BattleState;
import pokemon2.states.IState;

public class ItemState implements IState
{
    private Handler handler;
    private BattleState battleState;
    private OptionPanel optionPanel;
    private boolean selectionPrompted = false;
    private ItemObject item;
    private String[] options, splitEffect;
    
    public ItemState(Handler handler, BattleState battleState)
    {
        this.handler = handler;
        this.battleState = battleState;
    }

    @Override
    public void onEnter() 
    {
        if(handler.getPlayer().getItems().isEmpty())
        {
            System.out.println("Player inventory empty!");
            battleState.getBattleStateMachine().pop();
        }
        else
        {
            System.out.println("Entering item state");
            options = new String[handler.getPlayer().getItems().size()+1];
            for(int i = 0; i < options.length - 1; i++)
            {
                options[i] = handler.getPlayer().getItems().get(i).getName();
            }
            options[options.length-1] = "Cancel";
            optionPanel = new OptionPanel(handler, options, 50, 0, 150, 50, 1, 1);
        }
        selectionPrompted = false;
    }
    
    @Override
    public void onExit() 
    {
        System.out.println("Exiting item state");
    }

    @Override
    public void update(double elapsedTime) 
    {
        if(selectionPrompted)
        {
            if(handler.getSelectionMenu().getSelectedCreature() != null)
            {
                Creature target = handler.getSelectionMenu().getSelectedCreature();
                switch(splitEffect[1])
                {
                    case "heal":
                        //System.out.println("Heal");
                        if(target.getCondition() != 1)
                        {
                            target.heal(Double.parseDouble(splitEffect[2]), 
                            battleState.getMessageBox());
                            handler.getPlayer().getItems().remove(item);
                            battleState.setMoved(true);
                            battleState.getBattleStateMachine().change(BattleState.MOVE);
                        }
                        break;
                    default:
                        //System.out.println("unknown");
                        break;
                }
            }
            battleState.getBattleStateMachine().pop();
        }
        else
        {
            optionPanel.tick();
            //When a choice has been made
            if(optionPanel.getChoice(0) != -1)
            {
                if(optionPanel.getChoice(0) == options.length - 1)
                {
                    handler.getBattleState().getBattleStateMachine().pop();
                }
                else
                {
                    item = handler.getPlayer().getItems().get(optionPanel.getChoice(0));
                    String effect = item.getEffect();
                    splitEffect = effect.split(", ");
                    switch(splitEffect[0])
                    {
                        case "target enemy":
                            System.out.println("Target enemy");
                            switch(splitEffect[1])
                            {
                                case "catch":
                                    System.out.println("Catch");
                                    if(handler.getOpponent() == null 
                                            && (handler.getBattleState().getCreatures()[1].getCondition() 
                                            <= Double.parseDouble(splitEffect[2]) ))
                                    {
                                        handler.getPlayer().getItems().remove(item);
                                        battleState.getBattleStateMachine().change("CatchState");
                                    }
                                    else
                                    {
                                        battleState.getBattleStateMachine().pop();
                                    }
                                    break;
                                default:
                                    System.out.println("Unknown");
                                    break;                           
                            }
                            break;
                        case "target own":
                            System.out.println("Target own");                        
                            selectionPrompted = true;
                            handler.getStateMachine().push("SelectionMenu");
                            break;
                        default:
                            System.out.println("Default");
                            break;
                    }
                }
            }
        }   
    }

    @Override
    public void render(Graphics g) 
    {
        if(!selectionPrompted)
            optionPanel.render(g);
    }

}

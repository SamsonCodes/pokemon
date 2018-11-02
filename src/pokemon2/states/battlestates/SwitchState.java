/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.states.battlestates;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import pokemon2.combat.Creature;
import pokemon2.customui.OptionPanel;
import pokemon2.main.Handler;
import pokemon2.states.BattleState;
import pokemon2.states.IState;

public class SwitchState implements IState
{
    private Handler handler;
    private BattleState battleState;
    private OptionPanel optionPanel;
    private Creature current;
    
    public SwitchState(Handler handler, BattleState battleState)
    {
        this.handler = handler;
        this.battleState = battleState;
    }

    @Override
    public void onEnter() 
    {
        System.out.println("Entering switch state");
        current = battleState.getCreatures()[0];
        String[] options = new String[handler.getPlayer().pokemonLeft()];
        for(int i = 0; i < options.length; i++)
        {
            options[i] = handler.getPlayer().getPokemonLeft()[i].getName();
        }
        optionPanel = new OptionPanel(handler, options, 50, 0, 150, 50, 1, 1);
    }    

    @Override
    public void onExit() 
    {
        System.out.println("Exiting switch state");
    }

    @Override
    public void update(double elapsedTime) 
    {
        optionPanel.tick();
        if(optionPanel.getChoice(0) != -1)
        {
            Creature newCreature = handler.getPlayer().getPokemonLeft()[optionPanel.getChoice(0)];
            battleState.setPlayer(newCreature);
            if(newCreature != current)
            {
                battleState.setMoved(true);
            }
            battleState.getBattleStateMachine().pop();
        }
    }

    @Override
    public void render(Graphics g) 
    {
        optionPanel.render(g);
    }
   
}

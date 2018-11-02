/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.states;

import java.awt.Graphics;
import pokemon2.combat.Creature;
import pokemon2.customui.OptionPanel;
import pokemon2.main.Handler;

public class SelectionMenu implements IState
{
    private Handler handler;
    private OptionPanel optionPanel;
    private Creature selectedCreature;
    private long enterTime, coolDown;
    private String[] options;
    
    public SelectionMenu(Handler handler)
    {
        this.handler = handler;
        coolDown = 500;
    }
    
    @Override
    public void onEnter() 
    {
        System.out.println("Entering SelectionMenu state");
        selectedCreature = null;
        
        options = new String[handler.getPlayer().pokemonLeft()+1];
        for(int i = 0; i < options.length-1; i++)
        {
            options[i] = handler.getPlayer().getPokemonLeft()[i].getName();
        }
        options[options.length-1] = "Cancel";
        optionPanel = new OptionPanel(handler, options, 50, 0, 150, 50, 1, 1);
        enterTime = System.currentTimeMillis();
    } 
    
    @Override
    public void onExit() 
    {
        System.out.println("Exiting SelectionMenu state");
    }

    @Override
    public void update(double elapsedTime) 
    {
        if(System.currentTimeMillis() - enterTime > coolDown)
        {
            optionPanel.tick();
            if(optionPanel.getChoice(0) != -1)
            {
                if(optionPanel.getChoice(0) == options.length - 1)
                {
                    handler.getStateMachine().pop();
                }
                else
                {
                    selectedCreature = handler.getPlayer().getPokemonLeft()[optionPanel.getChoice(0)];
                    handler.getStateMachine().pop();
                }
            }
        }
    }
    
    @Override
    public void render(Graphics g) 
    {
        optionPanel.render(g);
    }
    
    public Creature getSelectedCreature()
    {
        return selectedCreature;
    }
}

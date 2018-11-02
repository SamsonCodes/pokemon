/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.states;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import pokemon2.combat.Creature;
import pokemon2.customui.OptionPanel;
import pokemon2.main.Handler;

public class PokeMenu implements IState
{
    private Handler handler;
    private OptionPanel optionPanel;
    private boolean printed;
    private long lastInput, coolDown;
    
    public PokeMenu(Handler handler)
    {
        this.handler = handler;
    }
    
    @Override
    public void onEnter() 
    {
        System.out.println("Entering PokeMenu state");
        String[] options = new String[handler.getPlayer().pokemonLeft()];
        for(int i = 0; i < options.length; i++)
        {
            options[i] = handler.getPlayer().getPokemonLeft()[i].getName();
        }
        optionPanel = new OptionPanel(handler, options, 50, 0, 150, 50, 1, 2);
        printed = false;
        coolDown = 500;
        lastInput = System.currentTimeMillis();
    }

    @Override
    public void onExit() 
    {
        System.out.println("Exiting PokeMenu state");
    }

    @Override
    public void update(double elapsedTime) 
    {
        if(System.currentTimeMillis() - lastInput > coolDown)
        {
            optionPanel.tick();
            if(handler.getKeyManager().keys[KeyEvent.VK_ESCAPE])
            {
                handler.getStateMachine().pop();
            }
        }
        if(optionPanel.getChoice(1) != -1)
        {
            Creature[] pokemon = handler.getPlayer().getPokemon();
            Creature p1 = pokemon[optionPanel.getChoice(0)];
            pokemon[optionPanel.getChoice(0)] = pokemon[optionPanel.getChoice(1)];
            pokemon[optionPanel.getChoice(1)] = p1;
            String[] options = new String[handler.getPlayer().pokemonLeft()];
            for(int i = 0; i < options.length; i++)
            {
                options[i] = handler.getPlayer().getPokemonLeft()[i].getName();
            }
            optionPanel = new OptionPanel(handler, options, 50, 0, 150, 50, 1, 2);
            lastInput = System.currentTimeMillis();
        }
    }

    @Override
    public void render(Graphics g) 
    {
        optionPanel.render(g);
    }

}

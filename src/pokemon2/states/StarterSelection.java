/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.states;

import java.awt.Graphics;
import pokemon2.combat.Creature;
import pokemon2.customui.OptionPanel;
import pokemon2.main.Handler;
import pokemon2.main.Rpg;

public class StarterSelection implements IState
{
    private Handler handler;
    private OptionPanel optionPanel;
    
    public StarterSelection(Handler handler)
    {
        this.handler = handler;
    }

    @Override
    public void onEnter() 
    {
        System.out.println("Entering StarterSelection state");
        String[] options = {"Bulbasaur", "Charmander", "Squirtle"};
        optionPanel = new OptionPanel(handler, options, 50, 0, 150, 50, 1, 1);        
        handler.getMessageBox().setText("Choose your starter.");
    }

    @Override
    public void onExit() 
    {
        System.out.println("Exiting StarterSelection state");
    }

    @Override
    public void update(double elapsedTime) 
    {
        handler.getMessageBox().tick();
        optionPanel.tick();
        if(optionPanel.getChoice(0) != -1)
        {
            Creature starter = new Creature(handler, (optionPanel.getChoice(0) + 1), 1, 5);
            handler.getPlayer().addPokemon(starter);
            handler.getMessageBox().setActive(false);
            handler.getStateMachine().change(Rpg.WORLD);
        }
    }

    @Override
    public void render(Graphics g) 
    {
        handler.getMessageBox().render(g);
        optionPanel.render(g);
    }

}

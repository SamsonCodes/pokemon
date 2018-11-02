/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.states;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import pokemon2.combat.ItemObject;
import pokemon2.customui.OptionPanel;
import pokemon2.main.Handler;

public class ShopState implements IState
{
    private Handler handler;
    private String[] options;
    private OptionPanel optionPanel;
    private ArrayList<ItemObject> inventory;
    private long lastInput, coolDown;
    
    public ShopState(Handler handler)
    {
        this.handler = handler;
        coolDown = 500;
    }

    @Override
    public void onEnter() 
    {
        System.out.println("Entering ShopState");
        inventory = handler.getSalesman().getInventory();
        options = new String[inventory.size()];
        for(int i = 0; i < inventory.size(); i++)
        {
            options[i] = inventory.get(i).getName() 
                            + ": " + Integer.toString(inventory.get(i).getPrice()) + "$";
        }
        optionPanel = new OptionPanel(handler, options, 50, 0, 150, 50, 1, 1);    
        lastInput = System.currentTimeMillis();
    }

    @Override
    public void onExit() 
    {
        System.out.println("Exiting ShopState");
    }

    @Override
    public void update(double elapsedTime) 
    {
        if(System.currentTimeMillis() - lastInput > coolDown)
        {
            optionPanel.tick();
            if(optionPanel.getChoice(0) != -1)
            {
                lastInput = System.currentTimeMillis();
                ItemObject item = inventory.get(optionPanel.getChoice(0));
                if(handler.getPlayer().getMoney() >= item.getPrice())
                {
                    handler.getPlayer().addMoney(-item.getPrice());
                    System.out.println("bought " + item.getName());
                    handler.getSalesman().removeItem(item);
                }
                else
                {
                    handler.getMessageBox().setText("Can't afford that!", 2000);
                }
                inventory = handler.getSalesman().getInventory();
                options = new String[inventory.size()];
                for(int i = 0; i < inventory.size(); i++)
                {
                    options[i] = inventory.get(i).getName() 
                            + ": " + Integer.toString(inventory.get(i).getPrice()) + "$";
                }
                optionPanel = new OptionPanel(handler, options, 50, 0, 150, 50, 1, 1); 
            }
        }
        if(handler.getKeyManager().keys[KeyEvent.VK_ESCAPE])
        {
            handler.getStateMachine().pop();
        }
        handler.getMessageBox().tick();
    }

    @Override
    public void render(Graphics g) 
    {
        optionPanel.render(g);
        handler.getMessageBox().render(g);
        g.setColor(Color.WHITE);
        g.drawString(Integer.toString(handler.getPlayer().getMoney()) + "$", 250, 50);
    }

}

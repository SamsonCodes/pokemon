/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import pokemon2.assets.Animation;
import pokemon2.assets.Assets;
import pokemon2.combat.Creature;
import pokemon2.combat.ItemObject;
import pokemon2.customui.OptionPanel;
import pokemon2.main.Handler;
import pokemon2.main.Rpg;
import pokemon2.main.XMLReader;
import pokemon2.sound.BackgroundMusic;

public class MenuState implements IState
{
    private Handler handler;
    private Animation charizard, archeops, bulbasaur;
    private OptionPanel optionPanel;
    private Font titleFont = new Font("Arial", Font.BOLD, 40);
    private BackgroundMusic bgm;
    
    public MenuState(Handler handler)
    {
        this.handler = handler;
//        charizard = new Animation(20, Assets.charizard);
//        archeops = new Animation(20, Assets.archeops);
        bulbasaur = new Animation(40, Assets.bulbasaur);         
    }

    @Override
    public void onEnter() 
    {
        System.out.println("Entering menu state");
        handler.getBackgroundMusic().stopMusic();
        handler.getBackgroundMusic().setSongFile("opening");
        handler.getBackgroundMusic().start();
        String[] options = {"New Game", "Load Save"};
        optionPanel = new OptionPanel(handler, options, 225, 100, 150, 50, 1, 1);       
    }

    @Override
    public void onExit() 
    {
        System.out.println("Exiting menu state");
    }

    @Override
    public void update(double elapsedTime) 
    {
        optionPanel.tick();
//        charizard.tick();
//        archeops.tick();
        bulbasaur.tick();
        
        if(optionPanel.getChoice(0) != -1)
        {
            switch(optionPanel.getChoice(0))
            {
                case 0:
                    handler.setLoadFromSave(false);
                    handler.getStateMachine().change("StarterSelection");
                    break;
                case 1:
                    handler.setLoadFromSave(true);
                    //Pokemon
                    String playerData = XMLReader.getElement(handler.getSaveData().allData(), "playerData");
                    ArrayList<String> pokemonData = XMLReader.getElements(playerData, "creature");
                    Creature[] pokemon = new Creature[6];
                    for(int i = 0; i < pokemonData.size(); i++)
                    {
                        int index = Integer.parseInt(XMLReader.getElement(pokemonData.get(i), "index"));
                        pokemon[i] = new Creature(handler, index, 1, 5);
                        int level = Integer.parseInt(XMLReader.getElement(pokemonData.get(i), "level"));
                        int progress = Integer.parseInt(XMLReader.getElement(pokemonData.get(i), "progress"));
                        pokemon[i].setExperience(level, progress);
                        double health = Double.parseDouble(XMLReader.getElement(pokemonData.get(i), "health"));
                        pokemon[i].setStats(0, health);
                    }
                    handler.getPlayer().setCreatures(pokemon);
                    
                    //Money
                    int money = Integer.parseInt(XMLReader.getElement(playerData, "money"));
                    handler.getPlayer().setMoney(money);
                    
                    //Items
                    String[] itemSplit = XMLReader.getElement(playerData, "items").split("\t");
                    for(int i = 0; i < itemSplit.length - 1; i++)
                    {
                        handler.getPlayer().addItems(new ItemObject(handler, Integer.parseInt(itemSplit[i+1])));
                    }
                    handler.getStateMachine().change(Rpg.WORLD);
                    break;
            }
        }
    }

    @Override
    public void render(Graphics g) 
    {
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, handler.getFrameWidth(), handler.getFrameHeight());
        g.setColor(Color.BLACK);
        g.setFont(titleFont);
        g.drawString("Pokémon", 210, 80);
        //g.drawImage(charizard.getCurrentFrame(), Assets.charizard[0].getWidth(), 0, -Assets.charizard[0].getWidth(), Assets.charizard[0].getHeight(), null);
        //g.drawImage(archeops.getCurrentFrame(), 400, 0, Assets.archeops[0].getWidth(), Assets.archeops[0].getHeight(), null);
        g.drawImage(bulbasaur.getCurrentFrame(), 200, 200, -100, 100, null);
        g.drawImage(bulbasaur.getCurrentFrame(), 400, 200, 100, 100, null);
        optionPanel.render(g);
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.states;

import java.awt.Color;
import java.awt.Graphics;
import pokemon2.states.battlestates.ExecutionState;
import pokemon2.states.battlestates.OptionState;
import pokemon2.states.battlestates.MoveState;
import java.util.ArrayList;
import java.util.Random;
import pokemon2.combat.Action;
import pokemon2.combat.Creature;
import pokemon2.customui.MessageBox;
import pokemon2.main.Handler;
import pokemon2.sound.BackgroundMusic;
import pokemon2.states.battlestates.CatchState;
import pokemon2.states.battlestates.ExpState;
import pokemon2.states.battlestates.ItemState;
import pokemon2.states.battlestates.SwitchState;

public class BattleState implements IState
{
    private Handler handler;
    private Creature player, enemy;
    private Creature[] creatures;
    private ArrayList<Action> actions;
    private StateMachine battleStates;
    public final static String OPTION = "Option", MOVE = "Move",
            EXECUTION = "Execution", SWITCH = "Switch", ITEMS = "Items";
    private boolean exit;
    private MessageBox messageBox;
    public static int panelX = 50, panelY = 300, panelWidth = 500, panelHeight = 100;
    private boolean moved;
    private double catchChance;
    private ArrayList<String> expList;
    
    public BattleState(Handler handler)
    {
        this.handler = handler;
        messageBox = new MessageBox(handler, panelX, panelY, panelWidth, panelHeight, Color.LIGHT_GRAY);
    }
    
    @Override
    public void onEnter() 
    {
        if(handler.getPlayer().hasPokemonLeft())
        {
            exit = false;
            System.out.println("Entering battle state");    
            
            handler.getBackgroundMusic().stopMusic();
            BackgroundMusic bgm = new BackgroundMusic();
            handler.setBackgroundMusic(bgm);
            handler.getBackgroundMusic().setSongFile("battletheme");
            handler.getBackgroundMusic().start();
            
            player = handler.getPlayer().firstConscious();
            //player.printBaseStats();
            
            if(handler.getOpponent() == null)
            {
                enemy = handler.getEncounter();
                //enemy.printBaseStats();
            }
            
            else
            {
                enemy = handler.getOpponent().getPokemon()[0];
            }

            creatures = new Creature[2];
            creatures[0] = player;
            creatures[1] = enemy;

            actions = new ArrayList<>();

            battleStates = new StateMachine();
            battleStates.add(OPTION, new OptionState(handler, this));
            battleStates.add(MOVE, new MoveState(handler, this));
            battleStates.add(EXECUTION, new ExecutionState(handler, this));
            battleStates.add(SWITCH, new SwitchState(handler, this));
            battleStates.add(ITEMS, new ItemState(handler, this));
            battleStates.add("CatchState", new CatchState(handler, this));
            battleStates.add("ExpState", new ExpState(handler, this));
            
            //battleTheme = new BackgroundMusic("battletheme");
            //battleTheme.start();

            battleStates.change(OPTION);
        }
        else
        {
            exit = true;
        }
    }

    @Override
    public void onExit() 
    {
        System.out.println("Exiting battle state");
        for(Creature c: handler.getPlayer().getPokemon())
        {
            if(c != null)
            {
                c.restoreStats();
            }
        }
        if(!handler.getPlayer().hasPokemonLeft())
        {
            handler.setWorld(handler.getSpawnWorld());
            handler.getPlayer().setX(handler.getSpawnX());
            handler.getPlayer().setY(handler.getSpawnY());
            for(Creature c: handler.getPlayer().getPokemon())
            {
                if(c != null)
                    c.restoreHealth();
            }
        }
        if(handler.getOpponent() != null)
        {
            if(handler.getOpponent().hasPokemonLeft())
            {
                for(Creature c: handler.getOpponent().getPokemon())
                {
                    if(c != null)
                    {
                        c.restoreHealth();
                        c.restoreStats();
                    }
                }      
            }
            else
            {
                handler.getOpponent().getReward();
            }
        }
        handler.setOpponent(null);
        handler.setEncounter(null);
        handler.getBackgroundMusic().stopMusic();
        BackgroundMusic bgm = new BackgroundMusic();
        handler.setBackgroundMusic(bgm);
        handler.getBackgroundMusic().setSongFile("opening");
        handler.getBackgroundMusic().start();
    }

    @Override
    public void update(double elapsedTime) 
    {
        //Uncomment for selfdestruction option for testing purposes
//        if(handler.getKeyManager().keys[KeyEvent.VK_D])
//        {
//            creatures[0].damage(999999999, false);
//        }
        //---
        if(!exit)
            battleStates.update(elapsedTime);
        else
        {
            handler.getStateMachine().pop();
        }
    }

    @Override
    public void render(Graphics g) 
    {        
        battleStates.render(g);
    }
    
    public ArrayList<Action> getActions()
    {
        return actions;
    }
    
    public Creature[] getCreatures()
    {
        return creatures;
    }
    
    public StateMachine getBattleStateMachine()
    {
        return battleStates;
    }
    
    public MessageBox getMessageBox()
    {
        return messageBox;
    }
    
    public void exit()
    {
        exit = true;
    }

    public void setPlayer(Creature creature) 
    {
        creatures[0] = creature;
    }
    
    public void setMoved(boolean b)
    {
        moved = b;
    }
    
    public boolean getMoved()
    {
        return moved;
    }
    
    public double getCatchChance()
    {
        return catchChance;
    }
    
    public void setCatchChance(double c)
    {
        catchChance = c;
    }
    
    public ArrayList<String> getExpList()
    {
        return expList;
    }
    
    public void setExpList(ArrayList<String> expList)
    {
        this.expList = expList;
    }
}

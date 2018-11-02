/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.states.battlestates;

import java.awt.Color;
import pokemon2.customui.OptionPanel;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;
import pokemon2.assets.Assets;
import pokemon2.combat.Action;
import pokemon2.combat.Attack;
import pokemon2.combat.Creature;
import pokemon2.combat.Pokemon;
import pokemon2.main.Handler;
import pokemon2.states.BattleState;
import pokemon2.states.IState;

public class MoveState implements IState 
{
    private Handler handler;
    private BattleState battleState;
    private String[] options;
    private OptionPanel optionPanel;
    private Creature[] creatures;
    private ArrayList<Action> actions;
    private long coolDown, enterTime;
    
    public MoveState(Handler handler, BattleState battleState) 
    {
        this.handler = handler;
        this.battleState = battleState;
        this.creatures = battleState.getCreatures();
        this.actions = battleState.getActions();
        coolDown = 500;
    }

    @Override
    public void onEnter() 
    {
        System.out.println("Entering move state");
        int a = creatures[0].getAttackAmount();
        options = new String[a];
        for(int i = 1; i <= options.length; i++)
        {
            if(creatures[0].getAttacks(i-1) != null)
                options[i-1] = creatures[0].getAttacks(i-1).getName();            
        }
        optionPanel = new OptionPanel(handler, options, 50, 300, 150, 50, 2, 1);
        enterTime = System.currentTimeMillis();
    }

    @Override
    public void onExit() 
    {
        System.out.println("Exiting move state");
    }

    @Override
    public void update(double elapsedTime) 
    {
        //To prevent immediately reading mouse data after switching to this state from options
        if(!battleState.getMoved())
        {
            if(System.currentTimeMillis() - enterTime > coolDown)
            {
                optionPanel.tick();
                //When a choice has been made
                if(optionPanel.getChoice(0) != -1)
                {
                    //create action data for player
                    Attack attack1 = creatures[0].getAttacks(optionPanel.getChoice(0)); 
                    String[] effects1 = attack1.getEffects();
                    Creature[][] targets1 = new Creature[effects1.length][1];
                    for(int i = 0; i < effects1.length; i++)
                    {
                        String[] split = effects1[i].split(" ");
                        if(split[0].equals("Opp"))
                        {
                            targets1[i][0] = creatures[1];
                        }
                        else if(split[0].equals("Self"))
                        {
                            targets1[i][0] = creatures[0];
                        }
                        else
                        {
                            System.out.println("MovesState: error reading effect target");
                        }
                    }

                    //create action data for ai
                    Attack attack2 = creatures[1].getAttacks(AIMove()); 
                    String[] effects2 = attack2.getEffects();
                    Creature[][] targets2 = new Creature[effects2.length][1];
                    for(int i = 0; i < effects2.length; i++)
                    {
                        String[] split = effects2[i].split(" ");
                        if(split[0].equals("Opp"))
                        {
                            targets2[i][0] = creatures[0];
                        }
                        else if(split[0].equals("Self"))
                        {
                            targets2[i][0] = creatures[1];
                        }
                        else
                        {
                            System.out.println("MovesState: error reading effect target");
                        }
                    }


                    //adds actions to list based on speed
                    if(creatures[0].getStats(Pokemon.SPEED) > creatures[1].getStats(Pokemon.SPEED))
                    {
                        actions.add(new Action(battleState, creatures[0],
                                attack1, targets1));
                        actions.add(new Action(battleState, creatures[1],
                                attack2, targets2));
                    }
                    else
                    {
                        actions.add(new Action(battleState, creatures[1],
                                attack2, targets2));
                        actions.add(new Action(battleState, creatures[0],
                                attack1, targets1));                    
                    }

                    //change to execution
                    battleState.getBattleStateMachine().change(BattleState.EXECUTION);
                }
            }
        }
        //If the player switched creatures only the AI move will have to be 
        //generated
        else
        {
            //create action data for ai
            Attack attack2 = creatures[1].getAttacks(AIMove()); 
            String[] effects2 = attack2.getEffects();
            Creature[][] targets2 = new Creature[effects2.length][1];
            for(int i = 0; i < effects2.length; i++)
            {
                String[] split = effects2[i].split(" ");
                if(split[0].equals("Opp"))
                {
                    targets2[i][0] = creatures[0];
                }
                else if(split[0].equals("Self"))
                {
                    targets2[i][0] = creatures[1];
                }
                else
                {
                    System.out.println("MovesState: error reading effect target");
                }
            }
            actions.add(new Action(battleState, creatures[1],
                        attack2, targets2));
           
            //change to execution
            battleState.getBattleStateMachine().change(BattleState.EXECUTION);
        }
    }
    
    public int AIMove()
    {
        Random random = new Random();
        int i = random.nextInt(creatures[1].getAttackAmount());
        return i;
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
        g.drawString(BattleState.MOVE, 100, 10);
        
        if(!battleState.getMoved())
            optionPanel.render(g);
    }

}

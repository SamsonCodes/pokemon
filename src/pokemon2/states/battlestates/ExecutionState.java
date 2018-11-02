/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.states.battlestates;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import pokemon2.assets.Assets;
import pokemon2.combat.Action;
import pokemon2.combat.Attack;
import pokemon2.combat.Creature;
import pokemon2.combat.Pokemon;
import pokemon2.customui.MessageBox;
import pokemon2.main.Handler;
import pokemon2.sound.WavPlayer;
import pokemon2.states.BattleState;
import pokemon2.states.IState;

public class ExecutionState implements IState 
{
    private Handler handler;
    private BattleState battleState;
    long lastTime, pauseTime;
    MessageBox messageBox;    
    private ArrayList<Action> actions;
    private Creature[] creatures;
    private int state;
    private int currentEffect;
    private int currentTarget;
    private boolean soundPlayed;
    
    public ExecutionState(Handler handler, BattleState battleState) 
    {
        this.handler = handler; 
        this.battleState = battleState;
        this.actions = battleState.getActions();
        this.creatures = battleState.getCreatures();
    }

    @Override
    public void onEnter() 
    {
        System.out.println("Entering execution state");
        pauseTime = 0;
        lastTime = System.currentTimeMillis();
        messageBox = battleState.getMessageBox();
        //messageBox.setText("Text will appear here");
        currentTarget = 0;
        currentEffect = 0;
        state = 0;
    }

    @Override
    public void onExit() 
    {
        System.out.println("Exiting execution state");        
        battleState.setMoved(false);
    }

    @Override
    public void update(double elapsedTime) 
    {
        //if it is again time to perform actions
        if(System.currentTimeMillis() - lastTime > pauseTime)
        {
            lastTime = System.currentTimeMillis();
            //if there are actions left to perform
            if(!actions.isEmpty())
            {                    
                Creature actor = actions.get(0).getActor();
                //if the creature that is supposed to perform the actor is still conscious
                if(actor.isConscious())
                {
                    //execute the action
                    Attack attack = actions.get(0).getAttack();            
                    String[] effects = attack.getEffects();
                    ArrayList<Creature> allTargets = actions.get(0).getAllTargets();
                    switch(state)
                    {
                        //First part: print the attack that is used
                        case 0:
                        {
                            state = 1;
                            soundPlayed = false; //used for state 1
                            pauseTime = 3000;
                            messageBox.setText(actor.getName() + " used " + attack.getName());
                            System.out.println(actor.getName() + " used " + attack.getName());
                            break;
                        }
                        
                        //Second part: execute all the effects on all their respective targets
                        case 1:
                        {
                            pauseTime = 1000;
                            String effect = effects[currentEffect];                            
                            Creature[] targets = actions.get(0).getTargets(currentEffect);
                            String[] effectParts = effect.split(" ");
                            switch(effectParts[1])
                            {
                                case "Phy":
                                {
                                    //System.out.println("Physical");
                                    if(actor.feelingLucky())
                                    {                                        
                                        if(!soundPlayed)
                                        {
                                            if(attack.getSoundId() != -1)
                                            {
                                                handler.getWavPlayer().playSound(WavPlayer.soundNames[attack.getSoundId()]);
                                            }
                                            soundPlayed = true;
                                        }
                                        double damage = Integer.parseInt(effectParts[3]);
                                        //System.out.println(actor.getName() + " base damage = " + damage);
                                        double multiplier = multiplier(effectParts[2], targets[currentTarget].getTypes()[0])
                                                *multiplier(effectParts[2], targets[currentTarget].getTypes()[1]);
                                        //System.out.println("Multiplier = " + multiplier);
                                        //System.out.println("ActorStats");
                                        //actor.printStats();
                                        //System.out.println(actor.getName() + " ATT is " + actor.getStats(Pokemon.ATTACK));
                                        double actualDamage = multiplier*actor.getStats(Pokemon.ATTACK)*damage;
                                        //System.out.println("actual damage is " + actualDamage);
                                        targets[currentTarget].damage(actualDamage, false);  
                                        if(multiplier < 1)
                                        {
                                            if(multiplier == 0)
                                            {
                                                messageBox.setText("It has no effect on " + targets[currentTarget].getName());
                                            }
                                            else
                                            {
                                                messageBox.setText("It's not very effective...");
                                                handler.getWavPlayer().playSound("wailingpeep");
                                            }
                                        }
                                        else if (multiplier > 1)
                                        {
                                            messageBox.setText("It's super effective!");
                                            handler.getWavPlayer().playSound("spacelaunchexplosive");
                                        }
                                    }
                                    else
                                    {
                                        soundPlayed = true;
                                        messageBox.setText(actor.getName() + " missed!");
                                        //System.out.println(actor.getName() + " missed!");
                                    }
                                    break;
                                }

                                case "Spe":
                                {
                                    //System.out.println("Special");
                                    if(actor.feelingLucky())
                                    {                                        
                                        if(!soundPlayed)
                                        {
                                            if(attack.getSoundId() != -1)
                                            {
                                                handler.getWavPlayer().playSound(WavPlayer.soundNames[attack.getSoundId()]);
                                            }
                                            soundPlayed = true;
                                        }
                                        double damage = Integer.parseInt(effectParts[3]);
                                        //System.out.println(actor.getName() + " base damage = " + damage);
                                        double multiplier = multiplier(effectParts[2], targets[currentTarget].getTypes()[0])
                                                *multiplier(effectParts[2], targets[currentTarget].getTypes()[1]);
                                        //System.out.println("Multiplier = " + multiplier);
                                        //System.out.println("ActorStats");
                                        //actor.printStats();
                                       //System.out.println(actor.getName() + " S_ATT is " + actor.getStats(Pokemon.S_ATTACK));
                                        double actualDamage = multiplier*actor.getStats(Pokemon.S_ATTACK)*damage;
                                        //System.out.println("actual damage is " + actualDamage);
                                        targets[currentTarget].damage(actualDamage, true);  
                                        if(multiplier < 1)
                                        {
                                            if(multiplier == 0)
                                            {
                                                messageBox.setText("It has no effect on " + targets[currentTarget].getName());
                                            }
                                            else
                                            {
                                                messageBox.setText("It's not very effective...");
                                                handler.getWavPlayer().playSound("wailingpeep");
                                            }
                                        }
                                        else if (multiplier > 1)
                                        {
                                            messageBox.setText("It's super effective!");                                            
                                            handler.getWavPlayer().playSound("spacelaunchexplosive");
                                        }
                                    }
                                    else
                                    {
                                        soundPlayed = true;
                                        messageBox.setText(actor.getName() + " missed!");
                                        //System.out.println(actor.getName() + " missed!");
                                    }
                                    break;
                                }
                                case "Inc":
                                {
                                    double percentage = Integer.parseInt(effectParts[3]);
                                    int stat = 0;
                                    for(int i = 0; i < Pokemon.statNames.length; i++)
                                    {
                                        if(effectParts[2].equals(Pokemon.statNames[i]))
                                        {
                                            stat = i;
                                        }
                                    }
                                    targets[currentTarget].changeStat(stat, percentage, messageBox);                                
                                    break;
                                }
                                case "Red":
                                {
                                    double percentage = -Integer.parseInt(effectParts[3]);
                                    int stat = 0;
                                    for(int i = 0; i < Pokemon.statNames.length; i++)
                                    {
                                        if(effectParts[2].equals(Pokemon.statNames[i]))
                                        {
                                            stat = i;
                                        }
                                    }
                                    targets[currentTarget].changeStat(stat, percentage, messageBox);                                
                                    break;
                                }
                                case "Heal":
                                {
                                    double percentage = Integer.parseInt(effectParts[2]);
                                    targets[currentTarget].heal(percentage, messageBox);
                                    break;
                                }
                            }
                            currentTarget++;
                            
                            //If all the targets have been looped through
                            if(currentTarget >= targets.length)
                            {
                                currentTarget = 0;
                                currentEffect++;
                                //If all effects have been looped through
                                if(currentEffect >= effects.length)
                                {
                                    currentEffect = 0;
                                    //If any creatures fainted change to state to 2 else remove this action and change to 0
                                    boolean someoneFainted = false;
                                    for(Creature t: allTargets)
                                    {
                                        if(!t.isConscious())
                                        {
                                            someoneFainted = true;
                                        }
                                    }
                                    if(someoneFainted)
                                    {
                                        state = 2;
                                    }
                                    else
                                    {
                                        actions.remove(actions.get(0));
                                        state = 0;
                                    }
                                }
                            }
                            break;
                        }
                            
                        /*Third (optional) part: in case one or several creatures fainted,
                         * print so for all those creatures, then remove action and switch to 0
                         * */
                        case 2:
                        {
                            if(currentTarget <= allTargets.size() - 1)
                            {
                                boolean exitLoop = false;
                                boolean fainted = false;
                                do
                                {
                                    if(!allTargets.get(currentTarget).isConscious())
                                    {
                                        fainted = true;
                                        exitLoop = true;
                                    }
                                    else
                                    {
                                        currentTarget++;
                                        if(currentTarget == allTargets.size())
                                        {
                                            currentTarget = allTargets.size()-1;
                                            exitLoop = true;
                                        }
                                    }
                                } while(!exitLoop);
                                if(fainted)
                                {
                                    messageBox.setText(allTargets.get(currentTarget).getName() + " fainted!");
                                    System.out.println(allTargets.get(currentTarget).getName() + " fainted!");
                                    
                                    if(allTargets.get(currentTarget).getPosition() == 2)
                                    {
                                        battleState.setExpList(allTargets.get(currentTarget).createDistributionList());
                                        battleState.getBattleStateMachine().push("ExpState");
                                        if(handler.getOpponent() != null)
                                        {
                                            Creature firstConscious = handler.getOpponent().firstConscious();
                                            if(firstConscious != null)
                                                creatures[1] = firstConscious;
                                            else
                                                System.out.println("Trainer out of pokemon!");
                                        }
                                    }
                                    currentTarget++;
                                    pauseTime = 3000;
                                }
                                else
                                {
                                    actions.remove(actions.get(0));
                                    pauseTime = 0;
                                    currentTarget = 0;
                                    state = 0;
                                }
                            }
                            else
                            {
                                actions.remove(actions.get(0));
                                pauseTime = 0;
                                currentTarget = 0;
                                state = 0;
                            }
                            break;
                        }
                    }            
                }
                //if the creature has fainted remove the action
                else
                {
                    actions.remove(actions.get(0));
                }
            }
            //if all actions are performed
            else
            {
                //if the player has pokemon left and the enemy is still concious, start new turn
                if(handler.getPlayer().hasPokemonLeft() && creatures[1].isConscious())
                {
                    if(!creatures[0].isConscious())
                    {
                        battleState.getBattleStateMachine().push(BattleState.SWITCH);
                    }
                    else
                    {
                        battleState.getBattleStateMachine().change(BattleState.OPTION);
                    }
                }
                //if not exit the battle
                else
                {
                    battleState.exit();
                }
            }
        }
    }
    
    public double multiplier(String attackType, String defenderType)
    {
        double multiplier = 1;
        switch(attackType)
        {
            case "GRA":
                switch(defenderType)
                {
                    case "GRA":
                        multiplier = 0.5;
                        break;
                    case "FIR":
                        multiplier = 0.5;
                        break;
                    case "WAT":
                        multiplier = 2;
                        break;
                    case "BUG":
                        multiplier = 0.5;
                        break;
                    case "FLY":
                        multiplier = 0.5;
                        break;
                    case "ELE":
                        break;
                    case "ROC":
                        multiplier = 2;
                        break;
                    case "POI":
                        multiplier = 0.5;
                        break;
                    case "GRO":
                        multiplier = 2;
                        break;
                    case "FIG":
                        break;
                    case "NOR":
                        break;
                    case "GHO":
                        break;
                    case "DAR":
                        break;
                    case "STE":
                        multiplier = 0.5;
                        break;
                    case "DRA":
                        multiplier = 0.5;
                        break;
                    case "ICE":
                        break;
                    case "PSY":
                        break;
                }
                break;
            case "FIR":
                switch(defenderType)
                {
                    case "GRA":
                        multiplier = 2;
                        break;
                    case "FIR":
                        multiplier = 0.5;
                        break;
                    case "WAT":
                        multiplier = 0.5;
                        break;
                    case "BUG":
                        multiplier = 2;
                        break;
                    case "FLY":
                        break;
                    case "ELE":
                        break;
                    case "ROC":
                        multiplier = 0.5;
                        break;
                    case "POI":
                        break;
                    case "GRO":
                        break;
                    case "FIG":
                        break;
                    case "NOR":
                        break;
                    case "GHO":
                        break;
                    case "DAR":
                        break;
                    case "STE":
                        multiplier = 2;
                        break;
                    case "DRA":
                        multiplier = 0.5;
                        break;
                    case "ICE":
                        multiplier = 2;
                        break;
                    case "PSY":
                        break;
                }
                break;
            case "WAT":
                switch(defenderType)
                {
                    case "GRA":
                        multiplier = 0.5;
                        break;
                    case "FIR":
                        multiplier = 2;
                        break;
                    case "WAT":
                        multiplier = 0.5;
                        break;
                    case "BUG":
                        break;
                    case "FLY":
                        break;
                    case "ELE":
                        break;
                    case "ROC":
                        multiplier = 2;
                        break;
                    case "POI":
                        break;
                    case "GRO":
                        multiplier = 2;
                        break;
                    case "FIG":
                        break;
                    case "NOR":
                        break;
                    case "GHO":
                        break;
                    case "DAR":
                        break;
                    case "STE":
                        break;
                    case "DRA":
                        multiplier = 0.5;
                        break;
                    case "ICE":
                        break;
                    case "PSY":
                        break;
                }
                break;
            case "BUG":
                switch(defenderType)
                {
                    case "GRA":
                        multiplier = 2;
                        break;
                    case "FIR":
                        multiplier = 0.5;
                        break;
                    case "WAT":
                        break;
                    case "BUG":
                        break;
                    case "FLY":
                        multiplier = 0.5;
                        break;
                    case "ELE":
                        break;
                    case "ROC":
                        break;
                    case "POI":
                        multiplier = 0.5;
                        break;
                    case "GRO":
                        break;
                    case "FIG":
                        multiplier = 0.5;
                        break;
                    case "NOR":
                        break;
                    case "GHO":
                        multiplier = 0.5;
                        break;
                    case "DAR":
                        multiplier = 2;
                        break;
                    case "STE":
                        multiplier = 0.5;
                        break;
                    case "DRA":
                        break;
                    case "ICE":
                        break;
                    case "PSY":
                        multiplier = 2;
                        break;
                }
                break;
            case "FLY":
                switch(defenderType)
                {
                    case "GRA":
                        multiplier = 2;
                        break;
                    case "FIR":
                        break;
                    case "WAT":
                        break;
                    case "BUG":
                        multiplier = 2;
                        break;
                    case "FLY":
                        break;
                    case "ELE":
                        multiplier = 0.5;
                        break;
                    case "ROC":
                        multiplier = 0.5;
                        break;
                    case "POI":
                        break;
                    case "GRO":
                        break;
                    case "FIG":
                        multiplier = 2;
                        break;
                    case "NOR":
                        break;
                    case "GHO":
                        break;
                    case "DAR":
                        break;
                    case "STE":
                        multiplier = 0.5;
                        break;
                    case "DRA":
                        break;
                    case "ICE":
                        break;
                    case "PSY":
                        break;
                }
                break;
            case "ELE":
                switch(defenderType)
                {
                    case "GRA":
                        multiplier = 0.5;
                        break;
                    case "FIR":
                        break;
                    case "WAT":
                        multiplier = 2;
                        break;
                    case "BUG":
                        break;
                    case "FLY":
                        multiplier = 2;
                        break;
                    case "ELE":
                        break;
                    case "ROC":
                        break;
                    case "POI":
                        break;
                    case "GRO":
                        multiplier = 0; // !!!
                        break;
                    case "FIG":
                        break;
                    case "NOR":
                        break;
                    case "GHO":
                        break;
                    case "DAR":
                        break;
                    case "STE":
                        break;
                    case "DRA":
                        multiplier = 0.5;
                        break;
                    case "ICE":
                        break;
                    case "PSY":
                        break;
                }
                break;
            case "ROC":
                switch(defenderType)
                {
                    case "GRA":
                        break;
                    case "FIR":
                        multiplier = 2;
                        break;
                    case "WAT":
                        break;
                    case "BUG":
                        multiplier = 2;
                        break;
                    case "FLY":
                        multiplier = 2;
                        break;
                    case "ELE":
                        break;
                    case "ROC":
                        multiplier = 0.5;
                        break;
                    case "POI":
                        break;
                    case "GRO":
                        multiplier = 0.5;
                        break;
                    case "FIG":
                        break;
                    case "NOR":
                        break;
                    case "GHO":
                        break;
                    case "DAR":
                        break;
                    case "STE":
                        multiplier = 0.5;
                        break;
                    case "DRA":
                        break;
                    case "ICE":
                        multiplier = 2;
                        break;
                    case "PSY":
                        break;
                }
                break;
            case "POI":
                switch(defenderType)
                {
                    case "GRA":
                        multiplier = 2;
                        break;
                    case "FIR":
                        break;
                    case "WAT":
                        break;
                    case "BUG":
                        break;
                    case "FLY":
                        break;
                    case "ELE":
                        break;
                    case "ROC":
                        multiplier = 0.5;
                        break;
                    case "POI":
                        multiplier = 0.5;
                        break;
                    case "GRO":
                        multiplier = 0.5;
                        break;
                    case "FIG":
                        break;
                    case "NOR":
                        break;
                    case "GHO":
                        multiplier = 0.5;
                        break;
                    case "DAR":
                        break;
                    case "STE":
                        multiplier = 0; // !!!
                        break;
                    case "DRA":
                        break;
                    case "ICE":
                        break;
                    case "PSY":
                        break;
                }
                break;
            case "GRO":
                switch(defenderType)
                {
                    case "GRA":
                        multiplier = 0.5;
                        break;
                    case "FIR":
                        multiplier = 2;
                        break;
                    case "WAT":
                        break;
                    case "BUG":
                        multiplier = 0.5;
                        break;
                    case "FLY":
                        multiplier = 0; // !!!
                        break;
                    case "ELE":
                        multiplier = 2;
                        break;
                    case "ROC":
                        multiplier = 2;
                        break;
                    case "POI":
                        multiplier = 2;
                        break;
                    case "GRO":
                        break;
                    case "FIG":
                        break;
                    case "NOR":
                        break;
                    case "GHO":
                        break;
                    case "DAR":
                        break;
                    case "STE":
                        multiplier = 2;
                        break;
                    case "DRA":
                        break;
                    case "ICE":
                        break;
                    case "PSY":
                        break;
                }
                break;
            case "FIG":
                switch(defenderType)
                {
                    case "GRA":
                        break;
                    case "FIR":
                        break;
                    case "WAT":
                        break;
                    case "BUG":
                        multiplier = 0.5;
                        break;
                    case "FLY":
                        multiplier = 0.5;
                        break;
                    case "ELE":
                        break;
                    case "ROC":
                        multiplier = 2;
                        break;
                    case "POI":
                        multiplier = 0.5;
                        break;
                    case "GRO":
                        break;
                    case "FIG":
                        break;
                    case "NOR":
                        multiplier = 2;
                        break;
                    case "GHO":
                        multiplier = 0; // !!!
                        break;
                    case "DAR":
                        multiplier = 2;
                        break;
                    case "STE":
                        multiplier = 2;
                        break;
                    case "DRA":
                        break;
                    case "ICE":
                        multiplier = 2;
                        break;
                    case "PSY":
                        multiplier = 0.5;
                        break;
                }
                break;
            case "NOR":
                switch(defenderType)
                {
                    case "GRA":
                        break;
                    case "FIR":
                        break;
                    case "WAT":
                        break;
                    case "BUG":
                        break;
                    case "FLY":
                        break;
                    case "ELE":
                        break;
                    case "ROC":
                        multiplier = 0.5;
                        break;
                    case "POI":
                        break;
                    case "GRO":
                        break;
                    case "FIG":
                        break;
                    case "NOR":
                        break;
                    case "GHO":
                        multiplier = 0; // !!!
                        break;
                    case "DAR":
                        break;
                    case "STE":
                        multiplier = 0.5;
                        break;
                    case "DRA":
                        break;
                    case "ICE":
                        break;
                    case "PSY":
                        break;
                }
                break;
            case "GHO":
                switch(defenderType)
                {
                    case "GRA":
                        break;
                    case "FIR":
                        break;
                    case "WAT":
                        break;
                    case "BUG":
                        break;
                    case "FLY":
                        break;
                    case "ELE":
                        break;
                    case "ROC":
                        break;
                    case "POI":
                        break;
                    case "GRO":
                        break;
                    case "FIG":
                        break;
                    case "NOR":
                        multiplier = 0; // !!!
                        break;
                    case "GHO":
                        multiplier = 2;
                        break;
                    case "DAR":
                        multiplier = 0.5;
                        break;
                    case "STE":
                        multiplier = 0.5;
                        break;
                    case "DRA":
                        break;
                    case "ICE":
                        break;
                    case "PSY":
                        multiplier = 2;
                        break;
                }
                break;
            case "DAR":
                switch(defenderType)
                {
                    case "GRA":
                        break;
                    case "FIR":
                        break;
                    case "WAT":
                        break;
                    case "BUG":
                        break;
                    case "FLY":
                        break;
                    case "ELE":
                        break;
                    case "ROC":
                        break;
                    case "POI":
                        break;
                    case "GRO":
                        break;
                    case "FIG":
                        multiplier = 0.5;
                        break;
                    case "NOR":
                        break;
                    case "GHO":
                        multiplier = 2;
                        break;
                    case "DAR":
                        multiplier = 0.5;
                        break;
                    case "STE":
                        multiplier = 0.5;
                        break;
                    case "DRA":
                        break;
                    case "ICE":
                        break;
                    case "PSY":
                        multiplier = 2;
                        break;
                }
                break;
            case "STE":
                switch(defenderType)
                {
                    case "GRA":
                        break;
                    case "FIR":
                        multiplier = 0.5;
                        break;
                    case "WAT":
                        multiplier = 0.5;
                        break;
                    case "BUG":
                        break;
                    case "FLY":
                        break;
                    case "ELE":
                        multiplier = 0.5;
                        break;
                    case "ROC":
                        multiplier = 2;
                        break;
                    case "POI":
                        break;
                    case "GRO":
                        break;
                    case "FIG":
                        break;
                    case "NOR":
                        break;
                    case "GHO":
                        break;
                    case "DAR":
                        break;
                    case "STE":
                        multiplier = 0.5;
                        break;
                    case "DRA":
                        break;
                    case "ICE":
                        multiplier = 2;
                        break;
                    case "PSY":
                        break;
                }
                break;
            case "DRA":
                switch(defenderType)
                {
                    case "GRA":
                        break;
                    case "FIR":
                        break;
                    case "WAT":
                        break;
                    case "BUG":
                        break;
                    case "FLY":
                        break;
                    case "ELE":
                        break;
                    case "ROC":
                        break;
                    case "POI":
                        break;
                    case "GRO":
                        break;
                    case "FIG":
                        break;
                    case "NOR":
                        break;
                    case "GHO":
                        break;
                    case "DAR":
                        break;
                    case "STE":
                        multiplier = 0.5;
                        break;
                    case "DRA":
                        multiplier = 2;
                        break;
                    case "ICE":
                        break;
                    case "PSY":
                        break;
                }
                break;
            case "ICE":
                switch(defenderType)
                {
                    case "GRA":
                        multiplier = 2;
                        break;
                    case "FIR":
                        multiplier = 0.5;
                        break;
                    case "WAT":
                        multiplier = 0.5;
                        break;
                    case "BUG":
                        break;
                    case "FLY":
                        multiplier = 2;
                        break;
                    case "ELE":
                        break;
                    case "ROC":
                        break;
                    case "POI":
                        break;
                    case "GRO":
                        multiplier = 2;
                        break;
                    case "FIG":
                        break;
                    case "NOR":
                        break;
                    case "GHO":
                        break;
                    case "DAR":
                        break;
                    case "STE":
                        multiplier = 0.5;
                        break;
                    case "DRA":
                        multiplier = 2;
                        break;
                    case "ICE":
                        multiplier = 0.5;
                        break;
                    case "PSY":
                        break;
                }
                break;
            case "PSY":
                switch(defenderType)
                {
                    case "GRA":
                        break;
                    case "FIR":
                        break;
                    case "WAT":
                        break;
                    case "BUG":
                        break;
                    case "FLY":
                        break;
                    case "ELE":
                        break;
                    case "ROC":
                        break;
                    case "POI":
                        multiplier = 2;
                        break;
                    case "GRO":
                        break;
                    case "FIG":
                        multiplier = 2;
                        break;
                    case "NOR":
                        break;
                    case "GHO":
                        break;
                    case "DAR":
                        multiplier = 0; // !!!
                        break;
                    case "STE":
                        multiplier = 0.5;
                        break;
                    case "DRA":
                        break;
                    case "ICE":
                        break;
                    case "PSY":
                        multiplier = 0.5;
                        break;
                }
                break;
        }
        return multiplier;
    }

    @Override
    public void render(Graphics g) 
    {
        g.drawImage(Assets.bbg[2], 50, 0, 500, 300, null);
        
        for(Creature c: battleState.getCreatures())
        {
            c.render(g);
        }
        
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(50, 300, 500, 100);
        g.setColor(Color.BLACK);
        g.drawRect(50, 300, 500, 100);
        
        g.setColor(Color.BLACK);
        g.drawString(BattleState.EXECUTION, 100, 10);
        
//        g.setColor(Color.YELLOW);
//        g.drawOval(handler.getMouseManager().x-5, handler.getMouseManager().y-5, 10, 10);
        
        messageBox.render(g);
    }

}

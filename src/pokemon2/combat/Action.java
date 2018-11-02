/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.combat;

import java.util.ArrayList;
import pokemon2.states.BattleState;

public class Action 
{
    private BattleState battleState;
    private Creature actor;
    private Attack attack;
    private Creature[][] targets;
    
    public Action(BattleState battleState, Creature actor, Attack attack, Creature[][] targets)
    {
        this.battleState = battleState;
        this.actor = actor;
        this.attack = attack;
        this.targets = targets;
    }
    
    public Creature getActor()
    {
        return actor;
    }
    
    public ArrayList<Creature> getAllTargets()
    {
        ArrayList<Creature> allTargets = new ArrayList<>();
        for(Creature[] tArray: targets)
        {
            for(Creature target: tArray)
            {
                if(allTargets.isEmpty())
                {
                    allTargets.add(target);
                }
                else
                {
                    if(!allTargets.contains(target))
                    {
                        allTargets.add(target);
                    }
                }    
            }
        }
        return allTargets;
    }
    
    public Creature[] getTargets(int effect)
    {
        return targets[effect];
    }
    
    public Attack getAttack()
    {
        return attack;
    }
}

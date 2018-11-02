/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.states.battlestates;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import pokemon2.assets.Assets;
import pokemon2.combat.Creature;
import pokemon2.customui.MessageBox;
import pokemon2.main.Handler;
import pokemon2.states.BattleState;
import pokemon2.states.IState;

public class CatchState implements IState
{
    private Handler handler;
    private BattleState battleState;
    private int state;
    private long lastTime, pauseTime;
    private MessageBox messageBox;
    private Random random;
    private boolean succes;
    private Creature daCatch;
    
    public CatchState(Handler handler, BattleState battleState)
    {
        this.handler = handler;
        this.battleState = battleState;
        this.messageBox = battleState.getMessageBox();
        random = new Random();
        pauseTime = 0;
    }

    @Override
    public void onEnter() 
    {
        System.out.println("Entering CatchState");        
        lastTime = System.currentTimeMillis();
        battleState.setCatchChance(1);
        succes = false;
        state = 0;
    }

    @Override
    public void onExit() 
    {
        System.out.println("Exiting CatchState");
    }

    @Override
    public void update(double elapsedTime) 
    {
        if(System.currentTimeMillis() - lastTime > pauseTime)
        {
            lastTime = System.currentTimeMillis();
            switch(state)
            {
                case 0:
                    messageBox.setText("Trying to catch pokemon");
                    pauseTime = 3000;
                    state = 1;
                    break;
                case 1:
                    double d = random.nextDouble();
                    if(d < battleState.getCatchChance())
                    {
                        messageBox.setText("Succes!!!");
                        daCatch = handler.getBattleState().getCreatures()[1];
                        daCatch.setActive(false);
                        succes = true;
                    }
                    else
                    {
                        messageBox.setText("Failed!!!");
                    }
                    state = 2;
                    break;
                case 2:
                    if(succes == true)
                    {
                        handler.getPlayer().addPokemon(daCatch.catchCopy());
                        battleState.exit();
                    }
                    else
                    {
                        messageBox.setText("");
                        messageBox.setActive(false);
                        battleState.setMoved(true);
                        battleState.getBattleStateMachine().change(BattleState.MOVE);
                    }
                    break;
                    
            }
        }
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
        g.drawString("CatchState", 100, 10);
        
        messageBox.render(g);
    }

}

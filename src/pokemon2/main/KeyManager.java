/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener
{
    public boolean[] keys;
    public boolean up, down, left, right, space;
    public boolean one, two, three;
    private Rpg rpg;
    
    KeyManager(Rpg rpg)
    {
        this.rpg = rpg;
        keys = new boolean[256];
    }
    
    public void tick()
    {
        up = keys[KeyEvent.VK_UP];
        down = keys[KeyEvent.VK_DOWN];
        left = keys[KeyEvent.VK_LEFT];
        right = keys[KeyEvent.VK_RIGHT];
        space = keys[KeyEvent.VK_SPACE];
        one = keys[KeyEvent.VK_1];
        two = keys[KeyEvent.VK_2];
        three = keys[KeyEvent.VK_3];
    }
    
    @Override
    public void keyTyped(KeyEvent ke) 
    {        
        
    }

    @Override
    public void keyPressed(KeyEvent ke) 
    {
//        if(ke.getKeyCode() == KeyEvent.VK_UP)
//        {
//            keys[KeyEvent.VK_UP]=true;
//            keys[KeyEvent.VK_DOWN]=false;
//            keys[KeyEvent.VK_LEFT]=false;
//            keys[KeyEvent.VK_RIGHT]=false;
//        }
//        if(ke.getKeyCode() == KeyEvent.VK_DOWN)
//        {
//            keys[KeyEvent.VK_UP]=false;
//            keys[KeyEvent.VK_DOWN]=true;
//            keys[KeyEvent.VK_LEFT]=false;
//            keys[KeyEvent.VK_RIGHT]=false;
//        }
//        if(ke.getKeyCode() == KeyEvent.VK_LEFT)
//        {
//            keys[KeyEvent.VK_UP]=false;
//            keys[KeyEvent.VK_DOWN]=false;
//            keys[KeyEvent.VK_LEFT]=true;
//            keys[KeyEvent.VK_RIGHT]=false;
//        }
//        if(ke.getKeyCode() == KeyEvent.VK_RIGHT)
//        {
//            keys[KeyEvent.VK_UP]=false;
//            keys[KeyEvent.VK_DOWN]=false;
//            keys[KeyEvent.VK_LEFT]=false;
//            keys[KeyEvent.VK_RIGHT]=true;
//        }
//        else
        if(!keys[ke.getKeyCode()])
        {
            keys[ke.getKeyCode()] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) 
    {
        if(keys[ke.getKeyCode()])
        {
            keys[ke.getKeyCode()]=false;
        }
        
    }
}

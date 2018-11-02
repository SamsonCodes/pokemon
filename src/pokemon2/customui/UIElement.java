/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.customui;

import java.awt.Graphics;
import pokemon2.main.Handler;

public abstract class UIElement 
{
    int x, y, width, height;
    boolean active;
    Handler handler;
    
    public UIElement(Handler handler)
    {
        this.handler = handler;
    }
    
    public abstract void tick();
    public abstract void render(Graphics g);
    
    public void setActive(boolean b)
    {
        active = b;
    }
    
    public boolean getActive()
    {
        return active;
    }
}

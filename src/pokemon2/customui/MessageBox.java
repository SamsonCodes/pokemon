/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.customui;

import java.awt.Color;
import java.awt.Graphics;
import pokemon2.main.Handler;

public class MessageBox extends UIElement
{
    String text;
    boolean timer;
    long displayTime, startTime;
    private Color color;
    
    public MessageBox(Handler handler, Color color)
    {
        super(handler);        
        this.color = color;
        width = handler.getFrameWidth();
        height = handler.getFrameHeight()/4;
        x = 0;
        y = 3*handler.getFrameHeight()/4;
        text = "Messages will appear here";
        active = false;
    }
    
    public MessageBox(Handler handler, int x, int y, int width, int height, Color color)
    {
        super(handler);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        active = false;
    }
    
    public void setText(String text)
    {
        active = true;
        timer = false;
        this.text = text;
    }
    
    public void setText(String text, long time)
    {
        this.text = text;
        timer = true;
        active = true;
        startTime = System.currentTimeMillis();
        displayTime = time;
    }
    
    @Override
    public void tick()
    {
        if(active && timer)
        {
            if(System.currentTimeMillis()-startTime > displayTime)
            {
                timer = false;
                active = false;
            }
        }
        else
        {
            timer = false;
        }
    }
    
    @Override
    public void render(Graphics g)
    {
        if(active)
        {
            g.setColor(color);
            g.fillRect(x, y, width, height);
            
            g.setColor(Color.BLACK);
            g.drawRect(x+5, y+5, width - 10, height - 10);
            g.drawString(text, x+20, y+25);
        }
    }
}

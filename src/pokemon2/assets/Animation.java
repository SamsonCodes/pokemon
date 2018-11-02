/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.assets;

import java.awt.image.BufferedImage;

public class Animation 
{
    private int period, index;
    private long timer, lastTime;
    private BufferedImage[] frames;
    
    public Animation(int speed, BufferedImage[] frames)
    {
        this.period = speed;
        this.frames = frames;
        index = 0;
        timer = 0;
        lastTime = System.currentTimeMillis();        
    }
    
    public void tick()
    {
        timer += System.currentTimeMillis()-lastTime;
        lastTime = System.currentTimeMillis();
        if(timer>period)
        {
            index++;
            timer = 0;
            if(index >= frames.length)
                index = 0;
        }
    }
    
    public BufferedImage getCurrentFrame()
    {
        return frames[index];
    }
    
    public BufferedImage getFrame(int i)
    {
        return frames[i];
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.graphics;

import pokemon2.main.Handler;
import pokemon2.entities.Entity;
import pokemon2.world.Tile;

public class Camera 
{
    private Handler handler;
    private int xOfset;
    private int yOfset;
    
    public Camera(Handler handler, int xOfset, int yOfset)
    {
        this.handler = handler;
        this.xOfset = xOfset;
        this.yOfset = yOfset;
    }
    
    public void checkBlankSpace()
    {
        if(xOfset < 0)
        {
            xOfset = 0;
        }
        else if(xOfset > handler.getWorld().getWidth() * Tile.SIZE - handler.getFrameWidth())
        {
            xOfset = handler.getWorld().getWidth() * Tile.SIZE - handler.getFrameWidth();
        }
        if(yOfset < 0)
        {
            yOfset = 0;
        }
        else if(yOfset > handler.getWorld().getHeight() * Tile.SIZE - handler.getFrameHeight())
        {
            yOfset = handler.getWorld().getHeight() * Tile.SIZE - handler.getFrameHeight();
        }
    }
    
    public void centerOnEntity(Entity e)
    {
        xOfset = e.getX() + e.getWidth()/2 - handler.getFrameWidth()/2;
        yOfset = e.getY() + e.getHeight()/2 - handler.getFrameHeight()/2;
        checkBlankSpace();
    }
    
    public void move(int xAmt, int yAmt)
    {
        xOfset += xAmt;
        yOfset += yAmt;
        checkBlankSpace();
    }

    public int getxOfset() {
        return xOfset;
    }

    public void setXOfset(int xOfset) {
        this.xOfset = xOfset;
    }

    public int getyOfset() {
        return yOfset;
    }

    public void setYOfset(int yOfset) {
        this.yOfset = yOfset;
    }
}

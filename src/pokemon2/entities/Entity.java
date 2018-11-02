/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.entities;

//Anything that has to be drawn in the world

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import pokemon2.main.Handler;
import pokemon2.world.Tile;

public abstract class Entity 
{
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected Handler handler;
    protected Rectangle bounds;
    protected boolean active;
    protected String name;
    
    public Entity(Handler handler, int x, int y, int width, int height, String name)
    {
        this.handler = handler;
        this.name = name;
        this.x = x*Tile.SIZE;
        this.y = y*Tile.SIZE;
        this.width = width*Tile.SIZE;
        this.height = height*Tile.SIZE;
        bounds = new Rectangle(0, 0, this.width, this.height);
        active = true;
    }
    
    public abstract void tick();
    public abstract void render(Graphics g);
    public abstract void activate(); //Consider using an interface as not all entities need this function
    public abstract String createSaveData();
    //Getters and setters    
    
    public boolean checkEntityCollisions(int xOfset, int yOfset)
    {
        for(Entity e: handler.getWorld().getEntityManager().getEntities())
        {
            if(e.equals(this))
                continue;
            if(e.getBounds(0,0).intersects(getBounds(xOfset, yOfset)))
                return true;
        }
        return false;
    }
    
    public Rectangle getBounds(int xOfset, int yOfset)
    {
        Rectangle r = new Rectangle(x+bounds.x+xOfset, y + bounds.y+yOfset,
                bounds.width, bounds.height);
        return r;
    }
    
    public void drawBounds(Graphics g)
    {
        g.setColor(Color.red);
        g.drawRect(x+bounds.x - handler.getxOfset(), y+bounds.y - handler.getyOfset(),
                bounds.width, bounds.height);
    }
    
    public boolean isActive()
    {
        return active;
    }
    
    public void setActive(boolean b)
    {
        active = b;
    }
    
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x*Tile.SIZE;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y*Tile.SIZE;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width*Tile.SIZE;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height*Tile.SIZE;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }
}

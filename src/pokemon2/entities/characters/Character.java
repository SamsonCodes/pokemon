/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.entities.characters;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import pokemon2.assets.Animation;
import pokemon2.assets.Assets;
import pokemon2.main.Handler;
import pokemon2.entities.Entity;
import pokemon2.world.Tile;

public abstract class Character extends Entity
{
    //public static final int STANDARD_WIDTH = 50, STANDARD_HEIGHT = 50, STANDARD_SPEED = 5;
    
    protected int xMove, yMove, speed;
    protected String facing;
    protected boolean moving, moveStarted, moveAnimation;
    protected int lastX, lastY;
    protected boolean arrived;
    protected boolean frozen;
    protected long moveCooldown, lastMove, lastMoveTime, lastArrival;
    protected int[] xRoute, yRoute;
    protected int currentDestination;
    protected Animation animDown, animUp, animLeft, animRight;
    
    boolean messagePrinted;
    
    public Character(Handler handler, int x, int y, String name, int imageId)
    {
        super(handler, x, y, 1, 1, name);
        animDown = new Animation(150, Assets.npcs_down[imageId]);
        animUp = new Animation(150, Assets.npcs_up[imageId]);
        animLeft = new Animation(150, Assets.npcs_left[imageId]);
        animRight = new Animation(150, Assets.npcs_right[imageId]);
        lastX = x/Tile.SIZE;
        lastY = y/Tile.SIZE;
        this.speed = 3;
        facing = "down";
        moving = false;
        moveStarted = false;
        messagePrinted = false;
        moveCooldown = 0;
        lastMove = System.currentTimeMillis();
        lastArrival = System.currentTimeMillis();
    }
    
    public Character(Handler handler, int x, int y, String name, int imageId, int[] xRoute, int[] yRoute)
    {
        super(handler, x, y, 1, 1, name);
        animDown = new Animation(150, Assets.npcs_down[imageId]);
        animUp = new Animation(150, Assets.npcs_up[imageId]);
        animLeft = new Animation(150, Assets.npcs_left[imageId]);
        animRight = new Animation(150, Assets.npcs_right[imageId]);
        lastX = x/Tile.SIZE;
        lastY = y/Tile.SIZE;
        this.speed = 3;
        facing = "down";
        moving = false;
        moveStarted = false;
        messagePrinted = false;
        
        this.xRoute = xRoute;
        this.yRoute = yRoute;
        currentDestination = 0;
        
        moveCooldown = 0;
        lastMove = System.currentTimeMillis();
        lastArrival = System.currentTimeMillis();
    }
    
    public Character(Handler handler, int x, int y, String name, 
            BufferedImage[] down, BufferedImage[] up, BufferedImage[] left, BufferedImage[] right)
    {
        super(handler, x, y, 1, 1, name);
        animDown = new Animation(150, down);
        animUp = new Animation(150, up);
        animLeft = new Animation(150, left);
        animRight = new Animation(150, right);
        lastX = x/Tile.SIZE;
        lastY = y/Tile.SIZE;
        this.speed = 3;
        facing = "down";
        moving = false;
        moveStarted = false;
        messagePrinted = false;
        moveCooldown = 0;
        lastMove = System.currentTimeMillis();
        lastArrival = System.currentTimeMillis();
    }
    
    @Override
    public void tick()
    {
        animDown.tick();
        animUp.tick();
        animLeft.tick();
        animRight.tick();
        if(moving)
        {
            lastMoveTime = System.currentTimeMillis();
            if(!moveAnimation)
            {
                moveAnimation = true;
                //System.out.println(name + ": moveAnimation = true");
            }
        }
        else
        {
            if(System.currentTimeMillis() - lastMoveTime > 100 && moveAnimation)
            {
                moveAnimation = false;
                //System.out.println(name + ": moveAnimation = false");
            }
        }
        followRoute();
    }
    
    @Override
    public void render(Graphics g) 
    {
        g.drawImage(getCurrentAnimationFrame(), x - handler.getxOfset(), y - handler.getyOfset(),
                width,height,null);
    }
    
    public BufferedImage getCurrentAnimationFrame()
    {
        if(facing.equals("right") && moveAnimation)
            return animRight.getCurrentFrame();
        
        else if(facing.equals("left") && moveAnimation)
            return animLeft.getCurrentFrame();
        
        else if(facing.equals("up") && moveAnimation)
            return animUp.getCurrentFrame();
        
        else if(facing.equals("down") && moveAnimation)
            return animDown.getCurrentFrame(); 
        
        else if(facing.equals("right"))
            return animRight.getFrame(0);
        
        else if(facing.equals("left"))
            return animLeft.getFrame(0);
        
        else if(facing.equals("up"))
            return animUp.getFrame(0);
        
        else if(facing.equals("down"))
            return animDown.getFrame(0);  
        
        return null;
    }
    
    public void followRoute()
    {
        if(xRoute != null && yRoute != null)
        {
            if(System.currentTimeMillis() - lastArrival > 1000)
            if(x/Tile.SIZE < xRoute[currentDestination])
            {
                setMoveCommand("right");
            }
            else if(x/Tile.SIZE > xRoute[currentDestination])
            {
                setMoveCommand("left");
            }
            else if(y/Tile.SIZE < yRoute[currentDestination])
            {
                setMoveCommand("down");
            }
            else if(y/Tile.SIZE > yRoute[currentDestination])
            {
                setMoveCommand("up");
            }
            else
            {
                currentDestination++;
                if(currentDestination >= yRoute.length)
                {
                    currentDestination = 0;
                }
                lastArrival = System.currentTimeMillis();
            }
            finishMoveCommand();
        }
    }
    
    public void freezeMovement(boolean b)
    {
        frozen = b;
    }
    
    public boolean isFrozen()
    {
        return frozen;
    }
    
    public void turn(String direction)
    {
        facing = direction;
    }  
    
    public void setMoveCommand(String command)
    {   
        if(System.currentTimeMillis() - lastMove > moveCooldown)
        {
            lastMove = System.currentTimeMillis();
            if(!frozen)
            {
                if(onTile())
                {            
                    xMove = 0;
                    yMove = 0;  
                    moving = false;
                    if(!command.equals(""))
                    {
                        facing  = command;
                    }
                    if(command.equals("up"))
                    {
                        yMove = - speed;                
                    }            
                    else if(command.equals("down"))
                        yMove = speed;

                    else if(command.equals("left"))
                        xMove = - speed;

                    else if(command.equals("right"))
                        xMove = speed;
                }
                if(xMove != 0 || yMove != 0)
                {
                    moving = true;
                }    
            }
        }
    }
    
    public void finishMoveCommand()
    {
        if(moving)
        {
            if(!onTile() |! moveStarted)
            {
                switch(facing)
                {
                    case "up":
                        yMove = -speed;
                        break;
                    case "down":
                        yMove = speed;
                        break;
                    case "left":
                        xMove = -speed;
                        break;
                    case "right":
                        xMove = speed;
                        break;
                }
                int currentX = x;
                int currentY = y;
                move();
                if(!moveStarted && (currentX != x || currentY != y))
                {
                    moveStarted = true;
                    //System.out.println(name + " departed from " + currentX/Tile.SIZE + ", " + currentY/Tile.SIZE);
                }                
                else if(onTile() && moveStarted)
                {                    
                    //System.out.println(name + " arrived at " + x/Tile.SIZE + ", " + y/Tile.SIZE);
                    clearAllExcept(x/Tile.SIZE, y/Tile.SIZE);
                    xMove = 0;
                    yMove = 0;
                    moving = false;
                    moveStarted = false;
                    messagePrinted = false;
                }
            }
        }
    }    
    
    public boolean onTile()
    {        
        return ((x % Tile.SIZE == 0) && (y % Tile.SIZE == 0));
    }
    
    public void move()
    {   
        if(!checkEntityCollisions(0, yMove))
            moveY();
        if(!checkEntityCollisions(xMove, 0))
            moveX();
        
    }
    
    public void moveX()
    {
        int ty = y/Tile.SIZE;
        if(xMove > 0)
        {            
            int tx = (int) (x + bounds.width + xMove - 1)/Tile.SIZE;            
            if(!collisionWithTile(tx,ty)
                    && (reservedBy(tx, ty) == this || reservedBy(tx, ty) == null))
            {
                if(!messagePrinted)
                {
                    messagePrinted = true;                    
                    //System.out.println(name + " decided that tile " + tx + ", " + ty + " was free");
                }
                if(reservedBy(tx, ty) == null)
                {
                    reserveTile(tx, ty);
                    lastX = x/Tile.SIZE;
                    lastY = y/Tile.SIZE;
                }
                else if(reservedBy(tx, ty) == this)
                {
                    x+=xMove;
                }
            }
            else
            {
                x = (tx - 1) * Tile.SIZE;
            }
        }
        else if(xMove < 0)
        {            
            int tx = (int) (x+xMove)/Tile.SIZE;
            if(!collisionWithTile(tx, ty)
                    && (reservedBy(tx, ty) == this || reservedBy(tx, ty) == null))
            {
                if(!messagePrinted)
                {
                    messagePrinted = true;                    
                    //System.out.println(name + " decided that tile " + tx + ", " + ty + " was free");
                }
                if(reservedBy(tx, ty) == null)
                {
                    reserveTile(tx, ty);
                    lastX = x/Tile.SIZE;
                    lastY = y/Tile.SIZE;
                }
                else if(reservedBy(tx, ty) == this)
                {
                    x+=xMove;
                }
            }
            else
            {
                x = (tx + 1) * Tile.SIZE;
            }
        }
    }
    
    public void moveY()
    {
        int tx = x/Tile.SIZE;
        if(yMove > 0)
        {            
            int ty = (int) (y + bounds.height + yMove - 1)/Tile.SIZE;
            if(!collisionWithTile(tx, ty)
                    && (reservedBy(tx, ty) == this || reservedBy(ty, ty) == null))
            {
                if(!messagePrinted)
                {
                    messagePrinted = true;                    
                    //System.out.println(name + " decided that tile " + tx + ", " + ty + " was free");
                }
                if(reservedBy(tx, ty) == null)
                {
                    reserveTile(tx, ty);
                    lastX = x/Tile.SIZE;
                    lastY = y/Tile.SIZE;
                }
                else if(reservedBy(tx, ty) == this)
                {
                    y+=yMove;
                }
            }
            else
            {
                y = (ty - 1) * Tile.SIZE;
            }
        }
        else if(yMove < 0)
        {            
            int ty = (y+yMove)/Tile.SIZE;
            if(!collisionWithTile(tx, ty)
                    && (reservedBy(tx, ty) == this || reservedBy(tx, ty) == null))
            {
                if(!messagePrinted)
                {
                    messagePrinted = true;                    
                    //System.out.println(name + " decided that tile " + tx + ", " + ty + " was free");
                }
                if(reservedBy(tx, ty) == null)
                {
                    reserveTile(tx, ty);
                    lastX = x/Tile.SIZE;
                    lastY = y/Tile.SIZE;
                }
                else if(reservedBy(tx, ty) == this)
                {
                    y+=yMove;
                }
            }
            else
            {
                y = (ty + 1) * Tile.SIZE;
            }
            
        }
    }
    
    protected boolean collisionWithTile(int x, int y)
    {
        //System.out.println(x+"\t"+y+"\t");
        return handler.getWorld().getTileType(0, x, y).isSolid();
    }
    
    protected boolean isEncounterTile(int x, int y)
    {
        return handler.getWorld().getTileType(0, x, y).isEncounter();
    }
    
    protected Entity reservedBy(int x, int y)
    {        
        return handler.getWorld().reservedBy(x, y);
    }
    
    protected void reserveTile(int x, int y)
    {
            //System.out.println(name + " reserved tile " + x + ", " + y);
            handler.getWorld().reserve(x, y, this);
    }
    
    protected void clearAllExcept(int x, int y)
    {        
        //System.out.println(name + " freed tile " + x + ", " + y);
        handler.getWorld().clearAllExcept(x, y, this);
    }
    
    public boolean getNextLine()
    {
        return false;
    }

    public int getxMove() {
        return xMove;
    }

    public void setxMove(int xMove) {
        this.xMove = xMove;
    }

    public int getyMove() {
        return yMove;
    }

    public void setyMove(int yMove) {
        this.yMove = yMove;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}

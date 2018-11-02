/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.entities.characters;

import java.awt.Graphics;
import pokemon2.assets.Assets;
import pokemon2.combat.Creature;
import pokemon2.main.Handler;
import pokemon2.main.XMLReader;
import pokemon2.world.Tile;

public class Healer extends Npc
{
    public Healer(Handler handler, int x, int y, String name)
    {
        super(handler, x, y, name, 2);
        dialogue = new String[1];
        dialogue[0] = "I will heal your pokemon for you.";
        currentLine = 0;
    }
    
    @Override
    public void activate()
    {
        switch(handler.getPlayer().facing)
        {
            case "up":
                facing = "down";
                break;
            case "down":
                facing = "up";
                break;
            case "left":
                facing = "right";
                break;
            case "right":
                facing = "left";
                break;
        }
        if(currentLine < dialogue.length)
        {            
            handler.getMessageBox().setText(name+": " + dialogue[currentLine]);
            currentLine++;
            handler.getPlayer().setMovementLocked(true);
        }
        else
        {
            currentLine = 0;
            handler.getMessageBox().setActive(false);
            for(Creature c: handler.getPlayer().getPokemon())
            {
                if(c != null)
                    c.restoreHealth();
            }
            handler.getWavPlayer().playSound("chime_up");
            handler.setSpawnX(x/Tile.SIZE);
            handler.setSpawnY((y/Tile.SIZE)+1);
            handler.setSpawnWorld(handler.getWorld());
            handler.getPlayer().setMovementLocked(false);
        }
    }

    @Override
    public void tick() 
    {
        super.tick();
    }

    @Override
    public void render(Graphics g) 
    {
        int image;
        switch(facing)
        {
            case "up":
                image = 1;
                break;
            case "down":
                image = 0;
                break;
            case "left":
                image = 2;
                break;
            case "right":
                image = 3;
                break;
            default:
                image = 0;
                break;                
        }
        g.drawImage(Assets.npcs[2][image], x - handler.getxOfset(), y - handler.getyOfset(),
                width, height, null);
    }
    
    @Override
    public String createSaveData() 
    {
        
        String s = "<entity>";
        s+="<type>Healer</type>"; 
        s+= "<name>" + name + "</name>"; 
        s+= "<coordinates>" + x/Tile.SIZE + "," + y/Tile.SIZE + "</coordinates>";
        if(xRoute != null && yRoute != null)
        {
            s+= "<xRoute>";
            for(int i = 0; i < xRoute.length; i++)
            {
                s += xRoute[i];
                if(i != xRoute.length - 1)
                    s += ",";
            }
            s+= "</xRoute>";
            s+= "<yRoute>";
            for(int i = 0; i < yRoute.length; i++)
            {
                s += yRoute[i];
                if(i != yRoute.length - 1)
                    s += ",";
            }
            s+= "</yRoute>";
            s+= "<currentDestination>" + currentDestination + "</currentDestination>";
        }
        s+="</entity>";
        return s;
    }
    
    public static Healer createFromSave(Handler handler, String data)
    {
        String[] coordinates = XMLReader.getElement(data, "coordinates").split(",");
        int x = Integer.parseInt(coordinates[0]);
        int y = Integer.parseInt(coordinates[1]);
        String name = XMLReader.getElement(data, "name");
        Healer healer = new Healer(handler, x, y, name);
        if(!XMLReader.getElement(data, "xRoute").equals(""))
        {
            String[] xRouteStrings = XMLReader.getElement(data, "xRoute").split(",");
            String[] yRouteStrings = XMLReader.getElement(data, "yRoute").split(",");
            int[] xRoute = new int[xRouteStrings.length];
            int[] yRoute = new int[xRouteStrings.length];
            for(int i = 0; i < xRouteStrings.length; i++)
            {
                xRoute[i] = Integer.parseInt(xRouteStrings[i]);
                yRoute[i] = Integer.parseInt(yRouteStrings[i]);
            }
            int currentDestination = Integer.parseInt(XMLReader.getElement(data, "currentDestination"));
            healer.xRoute = xRoute;
            healer.yRoute = yRoute;
            healer.currentDestination = currentDestination;
        }
        return healer;
    }
}

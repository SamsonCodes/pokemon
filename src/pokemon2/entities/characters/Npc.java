/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.entities.characters;

import java.awt.Color;
import java.awt.Graphics;
import pokemon2.assets.Assets;
import pokemon2.main.Handler;
import pokemon2.main.XMLReader;
import pokemon2.world.Tile;

public class Npc extends Character
{
    protected String[] dialogue;
    protected int currentLine;
    protected boolean dialogueStarted;
    private int imageId;
    
    public Npc(Handler handler, int x, int y, String name, int imageId)
    {
        super(handler, x,y,name, imageId);
        this.imageId = imageId;
        dialogue = new String[1];
        dialogue[0] = "Hello.";
        currentLine = 0;
    }
    
    public Npc(Handler handler, int x, int y, String name, int imageId, int[] xRoute, int[] yRoute)
    {
        super(handler, x,y,name, imageId, xRoute, yRoute);
        dialogue = new String[1];
        dialogue[0] = "Hello.";
        currentLine = 0;
    }
    
    public Npc(Handler handler, int x, int y, String name, String[] dialogue, int imageId)
    {
        super(handler, x,y,name, imageId);
        this.dialogue = dialogue;
        currentLine = 0;
    }
    
    public void setDialogue(String[] dialogue)
    {
        this.dialogue = dialogue;
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
        }
        else
        {
            currentLine = 0;
            handler.getMessageBox().setActive(false);
        }
    }

    @Override
    public void tick() 
    {
        super.tick();
    }

    @Override
    public String createSaveData() 
    {
        String s = "<entity>";
        s+="<type>Npc</type>"; 
        s+= "<name>" + name + "</name>"; 
        s+= "<imageId>" + imageId + "</imageId>";
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
    
    public static Npc createFromSave(Handler handler, String data)
    {
        String[] coordinates = XMLReader.getElement(data, "coordinates").split(",");
        int x = Integer.parseInt(coordinates[0]);
        int y = Integer.parseInt(coordinates[1]);
        String name = XMLReader.getElement(data, "name");
        int imageId = Integer.parseInt(XMLReader.getElement(data, "imageId"));
        Npc npc = new Npc(handler, x, y, name, imageId);
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
            npc.xRoute = xRoute;
            npc.yRoute = yRoute;
            npc.currentDestination = currentDestination;
        }
        return npc;
    }
}

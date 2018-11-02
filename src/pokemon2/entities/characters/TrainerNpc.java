/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.entities.characters;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import pokemon2.assets.Assets;
import pokemon2.combat.Creature;
import pokemon2.main.Handler;
import pokemon2.main.Rpg;
import pokemon2.main.XMLReader;
import pokemon2.world.Tile;

public class TrainerNpc extends Npc
{
    private Creature[] pokemon;
    private Rectangle activationRange;
    private Rectangle vision;
    private long updateTime, lastTime;
    private boolean hunting, activated;
    private String heading;
    private boolean freezeDirection;
    private int reward;
    
    public TrainerNpc(Handler handler, int x, int y, String name, Creature c, int imageId)
    {
        super(handler, x, y, name, imageId);
        dialogue = new String[1];
        dialogue[0] = "Fight me!";
        pokemon = new Creature[3];
        pokemon[0] = c;
        //yMove = speed;
        //updateTime = 3000;
        //lastTime = System.currentTimeMillis();
        //hunting = true;
        //facing = "right";
        //heading = "up";
        activated = false;
        reward = 100;
    }
    
    public TrainerNpc(Handler handler, int x, int y, String name, Creature[] c, int imageId)
    {
        super(handler, x, y, name, imageId);
        dialogue = new String[1];
        dialogue[0] = "Fight me!";
        pokemon = c;
        //yMove = speed;
        //updateTime = 3000;
        //lastTime = System.currentTimeMillis();
        //hunting = true;
        //facing = "right";
        //heading = "up";
        activated = false;
    }
    
    @Override
    public void tick()
    {  
        super.tick();
        if(!hasPokemonLeft())
        {
           setActive(false);
        }
    }
    
    @Override
    public void activate()
    {
//        if(!activated)
//        {
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
//        }
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
            handler.setOpponent(this);
//            if(freezeDirection)
//            {
//                heading = "";
//                freezeDirection = false;
//            }
//            if(frozen)
//                freezeMovement(false);
//            if(handler.getPlayer().isFrozen())
//                handler.getPlayer().freezeMovement(false);
//            if(hunting)
//            {
//                hunting = false;
//                activated = false;
//                handler.getStateMachine().Change(Rpg.BATTLE);
//            }
            handler.getStateMachine().push(Rpg.BATTLE);
            handler.getPlayer().setMovementLocked(false);
        }        
    }
    
    public Creature[] getPokemon()
    {
        return pokemon;
    }
    
    public boolean hasPokemonLeft()
    {
        boolean left = false;
        for(Creature c: pokemon)
        {
            if(c != null)
            {
                if(c.isConscious())
                {
                    left = true;
                    continue;
                }
            }
        }
        return left;
    }
    
    public void drawVision(Graphics g)
    {
        g.setColor(Color.GREEN);
        g.drawRect(vision.x - handler.getxOfset(), vision.y - handler.getyOfset(), vision.width, vision.height);
    }
    
    public void drawActivationRange(Graphics g)
    {
        g.setColor(Color.YELLOW);
        g.drawRect(activationRange.x - handler.getxOfset(), activationRange.y - handler.getyOfset(), activationRange.width, activationRange.height);
    }
    
    public Creature firstConscious()
    {
        for(Creature c: pokemon)
        {
            if(c != null)
            {
                if(c.isConscious())
                {
                    return c;
                }
            }
        }
        return null;
    }
    
    public void getReward()
    {
        handler.getPlayer().addMoney(reward);
        System.out.println("Player received " + reward + "$ for winning!");
    }
    
    @Override
    public String createSaveData() 
    {
        String s = "<entity>";
        s+="<type>TrainerNpc</type>"; 
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
        s+= "<creatures>";
        for(int i = 0; i < pokemon.length; i++)
        {
            if(pokemon[i] != null)
            {
                s+="<creature><index>" + pokemon[i].getIndex() + "</index><level>" 
                        + pokemon[i].getLevel() + "</level></creature>";
            }
        }
        s+="</creatures>";
        s+="</entity>";
        return s;
    }
    
    public static TrainerNpc createFromSave(Handler handler, String data)
    {
        String[] coordinates = XMLReader.getElement(data, "coordinates").split(",");
        int x = Integer.parseInt(coordinates[0]);
        int y = Integer.parseInt(coordinates[1]);
        String name = XMLReader.getElement(data, "name");
        String creatures = XMLReader.getElement(data, "creatures");
        ArrayList<String> creatureData = XMLReader.getElements(creatures, "creature");
        Creature[] pokemon = new Creature[creatureData.size()];
        for(int i = 0; i < creatureData.size(); i++)
        {
            String s = creatureData.get(i);
            int index = Integer.parseInt(XMLReader.getElement(s, "index"));
            int level = Integer.parseInt(XMLReader.getElement(s, "level"));
            Creature c = new Creature(handler, index, 2, level);
            pokemon[i] = c;
        }
        TrainerNpc tNpc = new TrainerNpc(handler, x, y, name, pokemon, 3);
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
            tNpc.xRoute = xRoute;
            tNpc.yRoute = yRoute;
            tNpc.currentDestination = currentDestination;
        }
        return tNpc;
    }
}

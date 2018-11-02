/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.entities.stationaries;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import pokemon2.main.Handler;
import pokemon2.main.XMLReader;
import pokemon2.world.Tile;
import pokemon2.world.World;

public class Portal extends Stationary
{
    private World destination;
    private int destinationX, destinationY;
    
    public Portal(Handler handler, int x, int y, World destination, int destinationX, int destinationY, String name)
    {
        super(handler, x, y, 1, 1, name);
        this.destination = destination;
        this.destinationX = destinationX;
        this.destinationY = destinationY;
        bounds = new Rectangle(0,0,0,0); 
    }

    @Override
    public void tick() {
        if(handler.getPlayer().getX() == x 
                && handler.getPlayer().getY() == y)
            activate();
    }

    @Override
    public void render(Graphics g) {
//        g.setColor(Color.yellow.darker());
//        g.fillRect(x-handler.getxOfset(), y-handler.getyOfset(),
//                width,height);
//        drawBounds(g);
    }

    @Override
    public void activate() 
    {
        handler.setWorld(destination);
        handler.getPlayer().setX(destinationX);
        handler.getPlayer().setY(destinationY);
    }
    
    @Override
    public String createSaveData() 
    {
        String s = "<entity>";
        s+="<type>Portal</type>"; 
        s+= "<name>" + name + "</name>"; 
        s+= "<coordinates>" + x/Tile.SIZE + "," + y/Tile.SIZE + "</coordinates>";
        s+= "<destination>" + destination.getName() + "</destination>";
        s+= "<destcoord>" + destinationX + "," + destinationY + "</destcoord>";
        s+="</entity>";
        return s;
    }
    
    public static Portal createFromSave(Handler handler, String data)
    {
        String[] coordinates = XMLReader.getElement(data, "coordinates").split(",");
        int x = Integer.parseInt(coordinates[0]);
        int y = Integer.parseInt(coordinates[1]);
        String name = XMLReader.getElement(data, "name");
        String destName = XMLReader.getElement(data, "destination");
        World destination = handler.getWorld();
        for(World world: handler.getWorldManager().getWorlds())
        {
            if(destName.equals(world.getName()))
            {
                destination = world;
            }
        }
        String[] destCoord = XMLReader.getElement(data, "destcoord").split(",");
        int destX = Integer.parseInt(destCoord[0]);
        int destY = Integer.parseInt(destCoord[1]);
        Portal portal  = new Portal(handler, x, y, destination, destX, destY, name);
        return portal;
    }

}

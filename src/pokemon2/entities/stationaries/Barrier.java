/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.entities.stationaries;

import java.awt.Graphics;
import pokemon2.main.Handler;
import pokemon2.main.XMLReader;
import pokemon2.world.Tile;

public class Barrier extends Stationary
{
    public Barrier(Handler handler, int x, int y, String name)
    {
        super(handler, x, y, 1, 1, name);
    }

    @Override
    public void tick() {
    }

    @Override
    public void render(Graphics g) 
    {
        //drawBounds(g);
    }

    @Override
    public void activate() 
    {
        
    }
    
    @Override
    public String createSaveData() 
    {
        String s = "<entity>";
        s+="<type>Barrier</type>"; 
        s+= "<name>" + name + "</name>"; 
        s+= "<coordinates>" + x/Tile.SIZE + "," + y/Tile.SIZE + "</coordinates>";
        s+="</entity>";
        return s;
    }
    
    public static Barrier createFromSave(Handler handler, String data)
    {
        String[] coordinates = XMLReader.getElement(data, "coordinates").split(",");
        int x = Integer.parseInt(coordinates[0]);
        int y = Integer.parseInt(coordinates[1]);
        String name = XMLReader.getElement(data, "name");
        Barrier barrier = new Barrier(handler, x, y, name);
        return barrier;
    }
    
}

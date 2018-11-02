/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.entities.stationaries;

import java.awt.Color;
import java.awt.Graphics;
import pokemon2.combat.ItemObject;
import pokemon2.assets.Assets;
import pokemon2.main.Handler;
import pokemon2.main.XMLReader;
import pokemon2.world.Tile;

public class Item extends Stationary
{
    //public static final int STANDARD_WIDTH = 50, STANDARD_HEIGHT = 50;
    private final int id;
    
    public Item(Handler handler, int x, int y, int id, String name)
    {
        super(handler, x, y, 1, 1, name);
        this.id = id;
    }
    
    @Override
    public void activate()
    {
        ItemObject item = new ItemObject(handler, id);
        handler.getPlayer().addItems(item);
        handler.getMessageBox().setText(handler.getPlayer().getName()+" picked up "+item.getName(), 3000);
        active = false;
    }

    @Override
    public void tick() {}

    @Override
    public void render(Graphics g) 
    {
        g.drawImage(Assets.item, x+5-handler.getCamera().getxOfset(), y+1-handler.getCamera().getyOfset(),
                width, height, null);
        //drawBounds(g);
    }

    public int getId() {
        return id;
    }
    
    @Override
    public String createSaveData() 
    {
        String s = "<entity>";
        s+="<type>Item</type>"; 
        s+= "<name>" + name + "</name>"; 
        s+= "<coordinates>" + x/Tile.SIZE + "," + y/Tile.SIZE + "</coordinates>";
        s+= "<index>" + id + "</index>";
        s+="</entity>";
        return s;
    }
    
    public static Item createFromSave(Handler handler, String data)
    {
        String[] coordinates = XMLReader.getElement(data, "coordinates").split(",");
        int x = Integer.parseInt(coordinates[0]);
        int y = Integer.parseInt(coordinates[1]);
        String name = XMLReader.getElement(data, "name");
        int index = Integer.parseInt(XMLReader.getElement(data, "index"));
        Item item = new Item(handler, x, y, index, name);
        return item;
    }
}

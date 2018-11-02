/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.entities.characters;

import java.util.ArrayList;
import pokemon2.combat.ItemObject;
import pokemon2.main.Handler;
import pokemon2.main.XMLReader;
import pokemon2.world.Tile;

public class Salesman extends Npc
{
    private ArrayList<ItemObject> inventory;
    
    public Salesman(Handler handler, int x, int y, String name)
    {
        super(handler, x, y, name, 4);
        dialogue = new String[1];
        dialogue[0] = "You wanna buy, yes?";
        currentLine = 0;
        inventory = new ArrayList<>();
        inventory.add(new ItemObject(handler, 0));
        inventory.add(new ItemObject(handler, 1));
    }
    
    public Salesman(Handler handler, int x, int y, String name, ArrayList<ItemObject> inventory)
    {
        super(handler, x, y, name, 4);
        dialogue = new String[1];
        dialogue[0] = "You wanna buy, yes?";
        currentLine = 0;
        this.inventory = inventory;
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
            handler.getPlayer().setMovementLocked(false);
            
            handler.setSalesman(this);
            handler.getStateMachine().push("ShopState");
        }
    }
    
    @Override
    public void tick()
    {
        super.tick();
    }
    
    public ArrayList<ItemObject> getInventory()
    {
        return inventory;
    }
    
    public void removeItem(ItemObject item)
    {
        if(inventory.contains(item))
        {
            inventory.remove(item);
        }
    }
    
    @Override
    public String createSaveData() 
    {
        String s = "<entity>";
        s+="<type>Salesman</type>"; 
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
        s+= "<inventory>";
        for(int i = 0; i < inventory.size(); i ++)
        {
            s += inventory.get(i).getId();
            if(i != inventory.size() - 1)
                s+=",";
        }
        s+="</inventory>";
        s+="</entity>";
        //System.out.println(XMLReader.getElement(s, "inventory"));
        return s;
    }
    
    public static Salesman createFromSave(Handler handler, String data)
    {
        String[] coordinates = XMLReader.getElement(data, "coordinates").split(",");
        int x = Integer.parseInt(coordinates[0]);
        int y = Integer.parseInt(coordinates[1]);
        String name = XMLReader.getElement(data, "name");
        ArrayList<ItemObject> inventory = new ArrayList<>();
        if(!XMLReader.getElement(data, "inventory").equals(""))
        {
            String[] itemIndices = XMLReader.getElement(data, "inventory").split(",");
            for(String itemId: itemIndices)
            {
                ItemObject iObject = new ItemObject(handler, Integer.parseInt(itemId));
                inventory.add(iObject);
            }
        }
        Salesman salesman = new Salesman(handler, x, y, name, inventory);
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
            salesman.xRoute = xRoute;
            salesman.yRoute = yRoute;
            salesman.currentDestination = currentDestination;
        }
        return salesman;
    }
}

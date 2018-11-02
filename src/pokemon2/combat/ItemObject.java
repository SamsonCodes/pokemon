/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.combat;

import pokemon2.customui.MessageBox;
import pokemon2.main.Handler;

public class ItemObject 
{
    private Handler handler;
    private int id;
    private String name;
    private int price;
    
    public ItemObject(Handler handler, int id)
    {
        this.handler = handler;
        this.id = id; 
        switch(id)
        {
            case 0:
                this.name = "Pokeball";
                this.price = 50;
                break;
            case 1:
                this.name = "Potion";
                this.price = 50;
                break;
            default:
                this.name = "Item"+id;
                break;
        }  
    }
    
    public String getName()
    {
        return name;
    }
    
    public int getId()
    {
        return id;
    }
    
    public String getEffect()
    {
        switch(id)
        {
            case 0:
                return "target enemy, catch, 0.5";
            case 1:
                return "target own, heal, 50";
            default:
                return "unknown";
        }
    }
    
    public int getPrice()
    {
        return price;
    }
}

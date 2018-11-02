/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.world;

import pokemon2.entities.Entity;

public class TileLayer 
{
    private int[] tiles;
    private Entity[] reserved;
    
    public TileLayer(int[] tiles)
    {
        this.tiles = tiles;
        reserved = new Entity[tiles.length];
    }

    int getIndex(int i) 
    {
        return tiles[i];
    }
    
    void reserve(int i, Entity e)
    {
        if(i < reserved.length && i >= 0)
        {
            if(e != null)
            {
                reserved[i] = e;
            }
            else
            {
                System.out.println("reserve: null entity not acceptable!!!");
            }
        }
        else
        {
            System.out.println("reserve: index out of bounds!!!: " +  i);
        }
    }
    
    void clearAllExcept(int i, Entity e)
    {
        if(e != null)
        {
            for(int q = 0; q < reserved.length; q++)
            {
                if(q != i)
                {
                    if(reserved[q] == e)
                    {
                        reserved[q] = null;
                    }
                }
            }
        }
    }
    
    Entity reservedBy(int i)
    {
        if(i < reserved.length && i > 0)
            return reserved[i];
        System.out.println("reservedBy: index out of out of bounds!!!: " + i);
        return null;
    }
    
    
    
    
}

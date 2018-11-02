/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import pokemon2.assets.Assets;

public class Tile 
{
    
    public static Tile[] tileTypes = new Tile[1+Assets.tiles1.length
            +Assets.tiles2.length+Assets.tiles3.length+Assets.tiles4.length]; 
            
    
    public static final int SIZE = 48;
    protected boolean solid;
    protected boolean encounter;
    protected double encounterChance;
    protected int[] species;
    protected int minLvl, maxLvl;
    
    protected BufferedImage texture;
    //protected final int id;
    
    Tile(BufferedImage texture)
    {
        this.texture = texture;
        solid = false;    
        encounter = false;
    }
    
    public static void init()
    { 
        tileTypes[0] = new Tile(null);
        for(int i = 0; i < Assets.tiles1.length; i++)
        {
            tileTypes[i+1] = new Tile(Assets.tiles1[i]);
        }
        for(int i = 0; i < Assets.tiles2.length; i++)
        {
            tileTypes[i+1+Assets.tiles1.length] = new Tile(Assets.tiles2[i]);
        }
        for(int i = 0; i < Assets.tiles3.length; i++)
        {
            tileTypes[i+1+Assets.tiles1.length+Assets.tiles2.length] = new Tile(Assets.tiles3[i]);
        }
        for(int i = 0; i < Assets.tiles4.length; i++)
        {
            tileTypes[i+1+Assets.tiles1.length+Assets.tiles2.length + Assets.tiles3.length] = new Tile(Assets.tiles4[i]);
        }
        
        //barriers
        tileTypes[12].solid = true;
        
        //path 1 patches
        tileTypes[11].encounter = true;
        tileTypes[11].encounterChance = 0.10;
        tileTypes[11].species = new int[]{4, 5, 11, 14, 17};
        tileTypes[11].minLvl = 2;
        tileTypes[11].maxLvl = 4;
        
        //path 2 patches
        tileTypes[9].encounter = true;
        tileTypes[9].encounterChance = 0.15;  
        tileTypes[9].species = new int[]{6, 7, 8, 9, 15};
        tileTypes[9].minLvl = 7;
        tileTypes[9].maxLvl = 9;
        
        //path 3 patches
        tileTypes[10].encounter = true;
        tileTypes[10].encounterChance = 0.20;
        tileTypes[10].species = new int[]{10, 12, 13, 16};
        tileTypes[10].minLvl = 11;
        tileTypes[10].maxLvl = 13;
        
    }
    
    public void tick()
    {
        
    }
    
    public void render(Graphics g, int x, int y)
    {
        g.drawImage(texture, x, y, SIZE, SIZE, null);
    }
    
    public boolean isSolid()
    {
        return solid;
    }
    
    public boolean isEncounter()
    {
        return encounter;
    }
    
    public double getEncounterChance()
    {
        return encounterChance;
    }
    
    public int getMinLvl()
    {
        return minLvl;
    }
    
    public int getMaxLvl()
    {
        return maxLvl;
    }
    
    public int[] getSpecies()
    {
        return species;
    }
    
//    public int getId()
//    {
//        return id;
//    }
}

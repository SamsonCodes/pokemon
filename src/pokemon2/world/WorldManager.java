/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.world;

import java.util.ArrayList;
import pokemon2.main.Handler;

public class WorldManager 
{
    private Handler handler;
    private ArrayList<World> worlds;
    
    
    public WorldManager(Handler handler)
    {
        this.handler = handler;
        worlds = new ArrayList<>();
    }

    public void addWorld(World world)
    {
        worlds.add(world);
    }
    
    public ArrayList<World> getWorlds()
    {
        return worlds;
    }
    
}

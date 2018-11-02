/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.entities;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import pokemon2.main.Handler;

public class EntityManager 
{
    private ArrayList<Entity> entities;
    private ArrayList<Entity> garbage;
    private Handler handler;
    private String name;
//    private Comparator<Entity> renderSorter = new Comparator<Entity>() {
//
//        @Override
//        public int compare(Entity a, Entity b) 
//        {
//            if(a.getY() < b.getY())
//                return -1;
//            return 1;
//        }
//    };
    
    public EntityManager(Handler handler, String name)
    {
        this.handler = handler;
        this.name = name;
        entities = new ArrayList<>();
        garbage = new ArrayList<>();
        entities.add(handler.getPlayer());
    }
    
    public void tick()
    {
        Iterator<Entity> itr = entities.iterator();
        while(itr.hasNext())
        {
            Entity e = itr.next();
            if(!e.isActive())
            {
                garbage.add(e);
                System.out.println("EntityManager: " + e.getName() + " was removed from the world.");
                itr.remove();
            }
            else
                e.tick();
        }
        //Collections.sort(entities, renderSorter);
    }
    
    public void render(Graphics g)
    {
        for(Entity e: entities)
        {
            e.render(g);
        }
    }
    
    public ArrayList<Entity> getEntities()
    {
        return entities;
    }
    
    public ArrayList<Entity> getGarbage()
    {
        return garbage;
    }
    
    public void addEntity(Entity e)
    {
        entities.add(e);
    }
    
    public void empty()
    {
        entities.clear();
        entities.add(handler.getPlayer());
    }

    public Handler getHandler() {
        return handler;
    }
    
    public String getName()
    {
        return name;
    }
}

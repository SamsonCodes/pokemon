/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.states;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import pokemon2.combat.Creature;
import pokemon2.entities.Entity;
import pokemon2.main.Handler;
import pokemon2.entities.EntityManager;
import pokemon2.entities.characters.Healer;
import pokemon2.entities.characters.Npc;
import pokemon2.entities.characters.Salesman;
import pokemon2.entities.characters.TrainerNpc;
import pokemon2.entities.stationaries.Barrier;
import pokemon2.entities.stationaries.Item;
import pokemon2.entities.stationaries.Portal;
import pokemon2.main.XMLReader;
import pokemon2.world.Tile;
import pokemon2.world.World;
import pokemon2.world.WorldManager;

public class WorldState implements IState
{
    private Handler handler;
    private World startTown;
    private EntityManager startTownManager;
    private World[] paths, towns;
    private EntityManager[] pathManagers, townManagers;
    private WorldManager worldManager;
    private TrainerNpc[] trainers;
    private String[] pathFiles = {"waterpath.tmx", "grasspath.tmx", "rockpath.tmx"}; 
    private String[] townFiles = {"watertown.tmx", "grasstown.tmx", "rocktown.tmx"}; 
    private Creature[] tCreatures;
    private int[] tIndex = {3, 1, 2};
    private int[] tLevel = {10, 13, 16}; 
    
    private long lastKeyPress, keyCooldown;
    
    private boolean firstEntry;
    
    public WorldState(Handler handler)
    {
        this.handler = handler;     
        
//        cave2Manager = new EntityManager(handler);
//        String[] dialogue3 = {"What?", "A reward?", "What do you think this is?", "A roleplaying game?", " Hah!"}; 
//        cave2Manager.addEntity(new Npc(handler, 17,13, "Brad", dialogue3));        
//        cave2Manager.addEntity(new Item(handler, 18,18,0));
//        cave2 = new World(handler, cave2Manager, "cavelevel2.tmx");
//         
//        caveManager = new EntityManager(handler);
//        String[] dialogue2 = {"My friend has gone upstairs but I'm afraid to follow him..."};
//        caveManager.addEntity(new Npc(handler, 15,3, "Timothy", dialogue2)); 
//        caveManager.addEntity(new Portal(handler, 14, 2, cave2, 14, 3));    
//        cave = new World(handler, caveManager, "cave.tmx");
//        
//        initialManager = new EntityManager(handler);
//        initialManager.addEntity(new Portal(handler, 1, 23, cave, 9, 18));
//        String[] dialogue1 = {"Greetings traveler!","There is a cave west of here.", "You should check it out."};
//        initialManager.addEntity(new Npc(handler, 20, 26, "Barry", dialogue1));
//        initialManager.addEntity(new TrainerNpc(handler, 22, 35, "Tyler"));
//        initialManager.addEntity(new Item(handler, 36,45,1));
//        initialWorld = new World(handler, initialManager, "Oril City.tmx");
//        caveManager.addEntity(new Portal(handler, 9, 19, initialWorld, 1,24));        
//        cave2Manager.addEntity(new Portal(handler, 14, 2, cave, 14,3));
//        
//        centerManager = new EntityManager(handler);
//        center = new World(handler, centerManager, "pokecenter.tmx");
        
        startTownManager = new EntityManager(handler, "startTownManager");
        startTown = new World(handler, startTownManager, "tiletown.tmx");        
        
        int amount = pathFiles.length;
        townManagers = new EntityManager[amount];
        pathManagers = new EntityManager[amount];
        towns = new World[amount];
        paths = new World[amount];
        
        for(int i = 0; i < amount; i++)
        {
            townManagers[i] = new EntityManager(handler, "townManager" + Integer.toString(i));
            towns[i] = new World(handler, townManagers[i], townFiles[i]);
            pathManagers[i] = new EntityManager(handler, "pathManager" + Integer.toString(i));
            paths[i] = new World(handler, pathManagers[i], pathFiles[i]);
        }
        
        startTownManager.addEntity(new Portal(handler, 10, 0, paths[0], 9, 18, "portal0"));
        startTownManager.addEntity(new Barrier(handler, 9, 19, "barrier0"));
        startTownManager.addEntity(new Item(handler, 9, 13, 0, "item0"));
        startTownManager.addEntity(new Item(handler, 8, 13, 1, "item1"));
        startTownManager.addEntity(new Salesman(handler, 7, 13, "salesman0"));
        int[] xRoute = {7, 12};
        int[] yRoute = {11, 11};
        startTownManager.addEntity(new Npc(handler, 7, 11, "npc1", 0, xRoute, yRoute));
        trainers = new TrainerNpc[amount];
        tCreatures = new Creature[amount];
        for(int i = 0; i < amount; i++)
        {
            pathManagers[i].addEntity(new Portal(handler, 10, 0, towns[i], 9, 18, "portal1"));
            townManagers[i].addEntity(new Portal(handler, 9, 19, paths[i], 10, 1, "portal2"));
            townManagers[i].addEntity(new Healer(handler, 9, 12, "healer" + Integer.toString(i)));
            townManagers[i].addEntity(new Item(handler, 10, 17, 0, "item" + Integer.toString(2+i)));
            townManagers[i].addEntity(new Item(handler, 9, 17, 1, "item" + Integer.toString(3+i)));
            townManagers[i].addEntity(new Salesman(handler, 8, 17, "salesman" + (i+1)));
            tCreatures[i] = new Creature(handler, tIndex[i], 2, tLevel[i]);
            trainers[i] = new TrainerNpc(handler, 10, 1, "Trainer"+i, tCreatures[i], 3);
            townManagers[i].addEntity(trainers[i]);
            
            if(i > 0)
            {
                pathManagers[i].addEntity(new Portal(handler, 9, 19, towns[i-1], 10, 1, "portal3"));
            }
            else
            {
                pathManagers[i].addEntity(new Portal(handler, 9, 19, startTown, 10, 1, "portal00"));
            }
            if(i < amount - 1)
            {
                townManagers[i].addEntity(new Portal(handler, 10, 0, paths[i+1], 9, 18, "portal4"));
            }
            else
            {
                townManagers[i].addEntity(new Barrier(handler, 10, 0, "barrier2"));
            }
        }
        
        worldManager = new WorldManager(handler);
        worldManager.addWorld(startTown);
        for(World world: paths)
        {
            worldManager.addWorld(world);
        }
        for(World world: towns)
        {
            worldManager.addWorld(world);
        }
        
        keyCooldown = 500;
        lastKeyPress = System.currentTimeMillis();
        
        handler.setWorld(startTown);
        handler.getPlayer().setX(9);
        handler.getPlayer().setY(18);
        handler.setSpawnWorld(startTown);
        handler.setSpawnX(9);
        handler.setSpawnY(18);
        
        handler.setWorldManager(worldManager);
        
        firstEntry = true;
    }

    @Override
    public void onEnter() 
    {
        System.out.println("Entering world state");
        
        if(firstEntry && handler.isLoadFromSave())
        {
            System.out.println("Loading world data from save");
            
            //clears all entities from worlds
            for(World world: worldManager.getWorlds())
            {
                world.getEntityManager().empty();
            }
            
            //sets the initial world
            String playerData = XMLReader.getElement(handler.getSaveData().allData(), "playerData");
            String worldName = XMLReader.getElement(playerData, "world");
            for(World world: worldManager.getWorlds())
            {
                if(worldName.equals(world.getName()))
                {
                    handler.setWorld(world);
                }
            }
            String[] coordString = XMLReader.getElement(playerData, "coordinates").split(",");
            handler.getPlayer().setX(Integer.parseInt(coordString[0]));
            handler.getPlayer().setY(Integer.parseInt(coordString[1]));

            //sets the initial respawn world
            worldName = XMLReader.getElement(playerData, "spawnWorld");
            for(World world: worldManager.getWorlds())
            {
                if(worldName.equals(world.getName()))
                {
                    handler.setSpawnWorld(world);
                }
            }
            String[] spawnCoord = XMLReader.getElement(playerData, "coordinates").split(",");
            handler.setSpawnX(Integer.parseInt(spawnCoord[0]));
            handler.setSpawnY(Integer.parseInt(spawnCoord[1]));
            
            //load entities
            String worldData = XMLReader.getElement(handler.getSaveData().allData(), "worldData");
            ArrayList<String> worlds = XMLReader.getElements(worldData, "world");
            for(String world: worlds)
            {
                String nameOfWorld = XMLReader.getElement(world, "name");
                World currentWorld = null;
                for(World theWorld: handler.getWorldManager().getWorlds())
                {
                    if(theWorld.getName().equals(nameOfWorld))
                    {
                        currentWorld = theWorld;
                    }
                }
                if(currentWorld == null)
                {
                    System.out.println("Error while loading: nameOfWorld not recognized: " + nameOfWorld);
                }
                else
                {
                    ArrayList<String> entities = XMLReader.getElements(world, "entity");
                    Entity entity = null;
                    for(String entityData: entities)
                    {
                        switch(XMLReader.getElement(entityData, "type"))
                        {
                            case "Healer":
                                entity = Healer.createFromSave(handler, entityData);
                                break;
                            case "Npc":
                                entity = Npc.createFromSave(handler, entityData);
                                break;
                            case "Salesman":
                                entity = Salesman.createFromSave(handler, entityData);
                                break;
                            case "TrainerNpc":
                                entity = TrainerNpc.createFromSave(handler, entityData);
                                break;
                            case "Barrier":
                                entity = Barrier.createFromSave(handler, entityData);
                                break;
                            case "Item":
                                entity = Item.createFromSave(handler, entityData);
                                break;
                            case "Portal":
                                entity = Portal.createFromSave(handler, entityData);
                                break;
                            default:
                                System.out.println("WorldState: Error while loading: unknown entity type" 
                                        + XMLReader.getElement(entityData, "type") );
                                break;
                        }
                        currentWorld.getEntityManager().addEntity(entity);
                    }
                }
            }
            firstEntry = false;
        }
        
    }

    @Override
    public void onExit() 
    {
        System.out.println("Exiting world state");
    }

    @Override
    public void update(double elapsedTime) 
    {
        //Save game data by pressing S
        if(System.currentTimeMillis() - lastKeyPress > keyCooldown)
        {
            if(handler.getKeyManager().keys[KeyEvent.VK_S])
            {
                lastKeyPress = System.currentTimeMillis();
                ArrayList<String> data = new ArrayList<>();
                
                //Player data
                data.add(handler.getPlayer().createSaveData());
                
                //World data
                data.add("<worldData>");
                for(World world: worldManager.getWorlds())
                {
                    data.add("<world>");
                    data.add("<name>" + world.getName() + "</name>");
                    data.add("<entities>");
                    for(int i = 0; i < world.getEntityManager().getEntities().size(); i++)
                    {
                        if(world.getEntityManager().getEntities().get(i) != handler.getPlayer())
                        {
                            data.add(world.getEntityManager().getEntities().get(i).createSaveData());
                        }
                    }
                    data.add("</entities>");
                    data.add("</world>");
                }
                data.add("</worldData>");
                
                handler.getSaveData().saveData(data);
                
                System.out.println("Progress Saved!");
                handler.getMessageBox().setText("Progress Saved!", 1000);
            }
            else if(handler.getKeyManager().keys[KeyEvent.VK_M])
            {
                lastKeyPress = System.currentTimeMillis();
                handler.getStateMachine().push("PokeMenu");
            }
            else if(handler.getKeyManager().keys[KeyEvent.VK_I])
            {
                lastKeyPress = System.currentTimeMillis();
                handler.getStateMachine().push("PlayerMenu");
            }
            
        }
        handler.getWorld().tick(); 
        handler.getMessageBox().tick();
//        for(int i = 0; i < trainers.length; i++)
//        {
//            if(!trainers[i].hasPokemonLeft())
//            {
//                trainers[i].setActive(false);
//            }
//        }
    }

    @Override
    public void render(Graphics g) 
    { 
        handler.getWorld().render(g);
        handler.getMessageBox().render(g);
//        for(int i = 0; i < 8; i ++)
//        g.drawImage(Assets.crystalTiles[i], i*16, 50, null);
    }

}

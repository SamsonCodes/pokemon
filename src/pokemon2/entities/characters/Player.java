/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.entities.characters;

import pokemon2.main.Handler;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import pokemon2.combat.ItemObject;
import pokemon2.assets.Animation;
import pokemon2.assets.Assets;
import pokemon2.combat.Creature;
import pokemon2.entities.Entity;
import pokemon2.main.Rpg;
import pokemon2.world.Tile;

public class Player extends Character
{
    private long lastActivate, activateCooldown = 250;
    
    private Creature[] pokemon;
    private ArrayList<ItemObject> items;
    private boolean[][] checkedForEncounter;
    private Random random;
    private boolean hasMoved;
    private boolean movementLocked;
    private int money;
    
    public Player(Handler handler, int x, int y, String name)
    {
        super(handler, x, y, name, Assets.player_down, Assets.player_up, 
                Assets.player_left, Assets.player_right);
        lastActivate = System.currentTimeMillis() - activateCooldown;
//        bounds.x = 12;
//        bounds.width = 24;
//        bounds.y = 24;
//        bounds.height = 24;
        
        
        random = new Random();
        pokemon = new Creature[6];
//        pokemon[0] = new Creature(handler, 1, 1, 5);
//        pokemon[1] = new Creature(handler, 2, 1, 5);
//        pokemon[2] = new Creature(handler, 3, 1, 5);
        items = new ArrayList();
        checkedForEncounter = new boolean[50][50];
        hasMoved = false;
        movementLocked = false;
        money = 50;
    }
    
    @Override
    public void activate()
    {
        if(System.currentTimeMillis() - lastActivate > activateCooldown)
        {
            handler.getMessageBox().setActive(false);
            lastActivate = System.currentTimeMillis();                            
            int arWidth = Tile.SIZE;
            int arHeight = Tile.SIZE;
            Rectangle ar = new Rectangle(0,0, arWidth,arHeight);            
            if(facing.equals("up"))
            {
                ar.x = x+width/2 - arWidth/2;
                ar.y = y - arWidth;
            }
            else if(facing.equals("down"))
            {
                ar.x = x+width/2 - arWidth/2;
                ar.y = y+height;
            }
            else if(facing.equals("left"))
            {
                ar.x = x - arWidth;
                ar.y = y;
            }
            else if(facing.equals("right"))
            {
                ar.x = x + arWidth;
                ar.y = y;
            }
            for(Entity e: handler.getWorld().getEntityManager().getEntities())
            {
                if(e.equals(this))
                    continue;
                if(ar.intersects(e.getBounds(0, 0)))
                {
                    e.activate();
                }
            }            
        }
    }

    
    @Override
    public void tick() 
    { 
        super.tick();
        if(moving)
        {
            if(!hasMoved)
                hasMoved = true;
            //lastMoveTime = System.currentTimeMillis();
            //moveAnimation = true;
        }
//        else
//        {
//            if(System.currentTimeMillis() - lastMoveTime > 100)
//            {
//                moveAnimation = false;
//            }
//        }
        if(!movementLocked)
        {
            String command = "";
            if(handler.getKeyManager().up)
            {
                command = "up";
            }
            else if(handler.getKeyManager().down)
            {
                command = "down";
            }
            else if(handler.getKeyManager().left)
            {
                command = "left";
            }
            else if(handler.getKeyManager().right)
            {
                command = "right";
            }
            setMoveCommand(command);
            finishMoveCommand();

            handler.getCamera().centerOnEntity(this);        
        }
        if(handler.getKeyManager().space)
        {
            activate();
        }
        
        //Check for wild encounter
        if(hasMoved && onTile() && isEncounterTile(x/Tile.SIZE, y/Tile.SIZE) &! checkedForEncounter[x/Tile.SIZE][y/Tile.SIZE])
        {
            if(!checkedForEncounter[x/Tile.SIZE][y/Tile.SIZE])
            {
                checkedForEncounter[x/Tile.SIZE][y/Tile.SIZE] = true;
                for(int j = 0; j < checkedForEncounter[0].length; j++)
                {
                    for(int i = 0; i < checkedForEncounter.length; i++)
                    {
                        if(i != x/Tile.SIZE || j != y/Tile.SIZE)
                        {
                            checkedForEncounter[i][j] = false;
                        }
                    }
                }
                if(random.nextDouble() < handler.getWorld().getTileType(0, x/Tile.SIZE, y/Tile.SIZE).getEncounterChance())
                {
                    Tile theTile = handler.getWorld().getTileType(0, x/Tile.SIZE, y/Tile.SIZE);
                    handler.setEncounter(new Creature(handler,
                            theTile.getSpecies()[random.nextInt(theTile.getSpecies().length)], 
                            2, theTile.getMinLvl() + random.nextInt(theTile.getMaxLvl() - theTile.getMinLvl())));
                    handler.getStateMachine().push(Rpg.BATTLE);
                }
            }            
        }
    }
    
    @Override
    public void render(Graphics g) 
    {
        g.drawImage(getCurrentAnimationFrame(), x + 8 - handler.getxOfset(), y - handler.getyOfset(),
                width,height,null);
    }
    
    public void addItems(ItemObject... items)
    {
        for(ItemObject i: items)
        {
            System.out.println(i.getName() + " was added to inventory");
        }
        this.items.addAll(Arrays.asList(items));
        int amount = 0;
        for(ItemObject item: this.items)
        {
            amount++;
        }
        System.out.println("Inventory now contains " + amount + " items.");
    }
    
    public int getMoney()
    {
        return money;
    }
    
    public void addMoney(int amount)
    {
        money += amount;
    }
    
    public Creature[] getPokemon()
    {
        return pokemon;
    }
    
    public ArrayList<ItemObject> getItems()
    {
        return items;
    }
    
    public boolean hasPokemonLeft()
    {
        boolean left = false;
        for(Creature c: pokemon)
        {
            if(c != null)
            {
                if(c.isConscious())
                {
                    left = true;
                    continue;
                }
            }
        }
        return left;
    }
    
    public int pokemonLeft()
    {
        int amount = 0;
        for(Creature c: pokemon)
        {
            if(c != null)
            {
                if(c.isConscious())
                {
                    amount++;
                }
            }
        }
        return amount;
    }
    
    public Creature firstConscious()
    {
        for(Creature c: pokemon)
        {
            if(c != null)
            {
                if(c.isConscious())
                {
                    return c;
                }
            }
        }
        return null;
    }
    public Creature[] getPokemonLeft()
    {
        Creature[] cLeft = new Creature[pokemonLeft()];
        int index = 0;
        for(int i = 0; i < pokemon.length; i++)
        {
            if(pokemon[i] != null)
            {
                if(pokemon[i].isConscious())
                {
                    cLeft[index] = handler.getPlayer().getPokemon()[i];
                    index++;
                }
            }
        }
        return cLeft;
    }
    
    public void setCreatures(Creature[] pokemon)
    {
        this.pokemon = pokemon;
    }
    
    public void setMovementLocked(boolean b)
    {
        movementLocked = b;
    }

    public void addPokemon(Creature creature)     
    {
        System.out.println("Player: Adding pokemon");
        for(int i = 0; i < pokemon.length; i++)
        {
            if(pokemon[i] == null)
            {
                pokemon[i] = creature;
                pokemon[i].setPosition(1);
                return;
            }
        }
    }
    
    @Override
    public String createSaveData() 
    {
        String s = "";
        s += "<playerData>";
        s += "<world>" + handler.getWorld().getName() + "</world>";
        s += "<coordinates>" + handler.getPlayer().getX()/Tile.SIZE + "," 
                + handler.getPlayer().getY()/Tile.SIZE + "</coordinates>";
        s += "<spawnWorld>" + handler.getSpawnWorld().getName() + "</spawnWorld>";
        s += "<spawnCoord>" + handler.getSpawnX() + "," + handler.getSpawnY()
                + "</spawnCoord>";
        s += "<money>" + money + "</money>";
        
        s += "<pokemonData>";
        for(int i = 0; i < handler.getPlayer().getPokemon().length; i++)
        {
            if(handler.getPlayer().getPokemon()[i] != null)
            {
                s+= handler.getPlayer().getPokemon()[i].createSaveData();
            }
        }
        s += "</pokemonData>";
        
        s += "<items>";
        for(int i = 0; i < handler.getPlayer().getItems().size(); i++)
        {
            s += items.get(i).getId(); 
            if(i != items.size() - 1)
            {
                s += ",";
            }
        }
        s += "</items>";
        
        s += "</playerData>";
        return s;
    }

    public void setMoney(int money) 
    {
        this.money = money;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.world;

import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import pokemon2.entities.Entity;
import pokemon2.main.Handler;
import pokemon2.entities.EntityManager;

public class World 
{
    private int width, height, spawnX, spawnY;
    private ArrayList<TileLayer>  tileLayers = new ArrayList();
    private final static String PATH = "C:\\Users\\Samson\\Documents\\NetBeansProjects\\Pokemon2\\src\\tmx\\";
    private BufferedReader bufferedReader;
    private Handler handler;
    private String name;
    
    private EntityManager entityManager;
    
    public World(Handler handler, EntityManager em, String name)
    {
        this.handler = handler;    
        
        entityManager = em;
        this.name = name;
        loadTmx(PATH+name);
        
        handler.getPlayer().setX(spawnX*Tile.SIZE);
        handler.getPlayer().setY(spawnY*Tile.SIZE);
//        System.out.println("World: width = " + width);
//        System.out.println("World: height = " + height);
    }
    
    public int getWidth()
    {
        return width;
    }
    
    public int getHeight()
    {
        return height;
    }
    
    public Entity reservedBy(int x, int y)
    {
        return tileLayers.get(0).reservedBy(y*width+x);
    }
    
    public void reserve(int x, int y, Entity e)
    {
        tileLayers.get(0).reserve(y*width+x, e);
    }
    
    public void clearAllExcept(int x, int y, Entity e)
    {
        tileLayers.get(0).clearAllExcept(y*width+x, e);
    }
    
    public Tile getTileType(int l, int x, int y)
    {
        if(x < 0 || y < 0 || x >= width || y >= height)
            return Tile.tileTypes[0];
        Tile t = Tile.tileTypes[tileLayers.get(l).getIndex(y*width+x)];
        if(t == null)
            return Tile.tileTypes[0];
        return t;
    }
    
    public int getPlayerX()
    {
        return spawnX;
    }
    
    public int getPlayerY()
    {
        return spawnY;
    }
    
    private void loadTmx(String path)
    {
        try 
        { 
            FileReader fileReader = new FileReader(path);
            bufferedReader = new BufferedReader(fileReader);
            String line;
            int j = 0;      
            int jStart = 0;
            width = 0; 
            int[] tileLayer = null;
            boolean newLayer = false;
            while((line = bufferedReader.readLine()) != null) 
            {
                
                //splits line in individual Strings by tab entries
                String[] words = line.split(" ");
                //remove any empty strings caused by multiple tabs             
                                   
                for(int i = 0; i < words.length; i++)
                {
                    //Set map size
                    if(words[0].startsWith("<map")&&words[i].startsWith("width"))
                    {
                        width = Integer.parseInt(words[i].replaceAll("\\D+",""));
                    }
                    if(words[0].startsWith("<map")&&words[i].startsWith("height"))
                    {
                        height =  Integer.parseInt(words[i].replaceAll("\\D+",""));
                        
                    }
                    //Read: if line starts with number --> create map layer
                    if(!words[0].replaceAll("\\D+","").isEmpty())
                    {                        
                        if(jStart == 0)
                        {
                            jStart = j;
                            tileLayer = new int[width*height];
                            newLayer = true;
                            //System.out.println("jStart set to "+j);
                        }
                            
                        String[] numbers = words[i].split(",");
                        for(int q = 0; q < numbers.length; q++)
                        {
                            //System.out.println("("+width+") < width*height = "+(((j-jStart)*width+q) < width*height));
                            if(j-jStart < height)
                            {
                                tileLayer[(j-jStart)*width+q] = Integer.parseInt(numbers[q]);  
                                //System.out.print(q+","+(j-jStart)+"="+numbers[q]+ "\t"+(j-jStart)+"\t");
                            }                            
                        }                        
                    }
                    else if(newLayer)
                    {
                        jStart = 0;
                        tileLayers.add(new TileLayer(tileLayer));
                        //System.out.println("Layer added");
                        tileLayer = new int[width*height];
                        newLayer = false;
                    }
                }  
                //System.out.println("");
                j++;
            }
            spawnX = width/2;
            spawnY = height/2;
        } 
        catch (IOException ex)
        {
            System.out.println("Error reading file "+ path);
        }
    }
    
    public void tick()
    {
        getEntityManager().tick();
    }
    
    public void render(Graphics g)
    {
        int xStart = Math.max(0, handler.getCamera().getxOfset() / Tile.SIZE);
        int yStart = Math.max(0, handler.getCamera().getyOfset() / Tile.SIZE);;
        int xEnd = Math.min(width, (handler.getCamera().getxOfset()+handler.getFrameWidth()) / Tile.SIZE+1);
        int yEnd = Math.min(height, (handler.getCamera().getyOfset()+handler.getFrameHeight()) / Tile.SIZE+1);
        int x = xStart, y = yStart;
        //System.out.println(tileLayers.size());
        for(int l = 0; l < tileLayers.size(); l++)
        {      
            for(int j = yStart; j < yEnd; j++)
            {
                for(int i = xStart; i < xEnd; i++)
                {
                    if(Tile.tileTypes[tileLayers.get(l).getIndex(j*width+i)]!= null)
                        Tile.tileTypes[tileLayers.get(l).getIndex(j*width+i)].render(g, i*Tile.SIZE-handler.getCamera().getxOfset(),
                            j*Tile.SIZE-handler.getCamera().getyOfset());
                    else
                        System.out.println("null exception!!!!");
                }
            }
        }
//        for(int j = yStart; j < yEnd; j++)
//        {
//            for(int i = xStart; i < xEnd; i++)
//            {
//                if(tileLayers.get(0).reservedBy(j*width + i) != null)
//                { 
//                    if(tileLayers.get(0).reservedBy(j*width + i) == handler.getPlayer())
//                        g.setColor(Color.BLUE);
//                    else
//                        g.setColor(Color.RED);
//                    g.drawOval(i*Tile.SIZE-handler.getCamera().getxOfset(),
//                        j*Tile.SIZE-handler.getCamera().getyOfset(), Tile.SIZE, Tile.SIZE);
//                }
//            }
//        }
        getEntityManager().render(g);        
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
    
    public String getName()
    {
        return name;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.main;

import pokemon2.combat.Creature;
import pokemon2.customui.MessageBox;
import pokemon2.entities.characters.Player;
import pokemon2.entities.characters.Salesman;
import pokemon2.entities.characters.TrainerNpc;
import pokemon2.graphics.Camera;
import pokemon2.math.Calculator;
import pokemon2.sound.BackgroundMusic;
import pokemon2.sound.WavPlayer;
import pokemon2.states.BattleState;
import pokemon2.states.SelectionMenu;
import pokemon2.states.StateMachine;
import pokemon2.world.World;
import pokemon2.world.WorldManager;

public class Handler 
{
    private Rpg rpg;
    private World world;
    private Creature encounter;
    private World spawnWorld;
    private int spawnX, spawnY;
    private boolean loadFromSave;
    private Salesman salesMan;
    private WorldManager worldManager;
    
    public Handler(Rpg rpg)
    {
        this.rpg = rpg;
    }
    
    public Player getPlayer()
    {
        return rpg.getPlayer();
    }
    
    public double getDistance(double x1, double y1, double x2, double y2)
    {
        return rpg.getCalculator().distance(x1, y1, x2, y2);
    }
    public int getxOfset()
    {
        return rpg.getCamera().getxOfset();
    }
    
    public int getyOfset()
    {
        return rpg.getCamera().getyOfset();
    }
    
    public String getTitle()
    {
        return rpg.TITLE;
    }
    
    public Calculator getCalculator()
    {
        return rpg.getCalculator();
    }
    
    public Gui getGui()
    {
        return rpg.getGui();
    }
    
    public int getFrameWidth()
    {
        return rpg.WIDTH;
    }
    
    public int getFrameHeight()
    {
        return rpg.HEIGHT;
    }
    
    public KeyManager getKeyManager()
    {
        return rpg.getKeyManager();
    }
    
    public MouseManager getMouseManager()
    {
        return rpg.getMouseManager();
    }
    
    public Camera getCamera()
    {
        return rpg.getCamera();
    }
    
    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }
    
    public MessageBox getMessageBox()
    {
        return rpg.getMessageBox();
    }
    
    public StateMachine getStateMachine()
    {
        return rpg.getStateMachine();
    }
    
    public BattleState getBattleState()
    {
        return rpg.getBattleState();
    }
    
    public WavPlayer getWavPlayer()
    {
        return rpg.getWavPlayer();
    }
    
    public void setOpponent(TrainerNpc trainer)
    {
        rpg.setOpponent(trainer);
    }
    
    public TrainerNpc getOpponent()
    {
        return rpg.getOpponent();
    }
    
    public void setEncounter(Creature encounter)
    {
        this.encounter = encounter;
    }
    
    public Creature getEncounter()
    {
        return encounter;
    }

    public World getSpawnWorld() {
        return spawnWorld;
    }

    public void setSpawnWorld(World spawnWorld) {
        this.spawnWorld = spawnWorld;
    }

    public int getSpawnX() {
        return spawnX;
    }

    public void setSpawnX(int spawnX) {
        this.spawnX = spawnX;
    }

    public int getSpawnY() {
        return spawnY;
    }

    public void setSpawnY(int spawnY) {
        this.spawnY = spawnY;
    }
    
    public SaveData getSaveData()
    {
        return rpg.getSaveData();
    }
    
    public boolean isLoadFromSave()
    {
        return loadFromSave;
    }
    
    public void setLoadFromSave(boolean b)
    {
        loadFromSave = b;
    }
    
    public BackgroundMusic getBackgroundMusic()
    {
        return rpg.getBackgroundMusic();
    }
    
    public SelectionMenu getSelectionMenu()
    {
        return rpg.getSelectionMenu();
    }

    public void setBackgroundMusic(BackgroundMusic bgm) 
    {
        rpg.setBackgroundMusic(bgm);
    }
    
    public void setSalesman(Salesman salesMan)
    {
        this.salesMan = salesMan;
    }
    
    public Salesman getSalesman()
    {
        return salesMan;
    }
    
    public void setWorldManager(WorldManager worldManager)
    {
        this.worldManager = worldManager;
    }
    
    public WorldManager getWorldManager()
    {
        return worldManager;
    }
}

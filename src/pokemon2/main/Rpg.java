/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import pokemon2.states.MenuState;
import pokemon2.states.BattleState;
import pokemon2.states.WorldState;
import pokemon2.states.StateMachine;
import pokemon2.customui.MessageBox;
import pokemon2.entities.characters.Player;
import pokemon2.entities.Entity;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import pokemon2.assets.Assets;
import pokemon2.combat.Data;
import pokemon2.entities.characters.TrainerNpc;
import pokemon2.graphics.Camera;
import pokemon2.math.Calculator;
import pokemon2.sound.BackgroundMusic;
import pokemon2.sound.WavPlayer;
import pokemon2.states.PlayerMenu;
import pokemon2.states.PokeMenu;
import pokemon2.states.SelectionMenu;
import pokemon2.states.ShopState;
import pokemon2.states.StarterSelection;
import pokemon2.world.Tile;

public class Rpg implements Runnable
{
    public final String TITLE = "Pokemon";
    public final int WIDTH = 600, HEIGHT = 400;
    public final static String MENU = "Menu", WORLD = "World", BATTLE = "Battle";
    private StateMachine stateMachine;
    private Gui gui;
    
    private Thread thread;
    
    private boolean isRunning = false;
    private double startTime, elapsedTime;
    
    private KeyManager keyManager;
    private MouseManager mouseManager;
    
    private ArrayList<Entity> layer1;
    private Player player;
    
    private Camera camera;
    private Calculator calculator;    
    private MessageBox messageBox;
    
    private Handler handler;
    
    private BattleState battleState;
    private SelectionMenu selectionMenu;
    
    private long switchCooldown, switchLast;
    
    private WavPlayer wavPlayer;
    private BackgroundMusic backgroundMusic;
    
    private TrainerNpc opponent;
    private SaveData saveData;
    
    Rpg(){}
    
    private void init()
    {
        Assets.init();          
        Tile.init();
        Data.init();
        
        handler = new Handler(this);
        
        camera = new Camera(handler, 0, 0);        
        
        messageBox = new MessageBox(handler, Color.WHITE);
        
        saveData = new SaveData();
        
        player = new Player(handler, 0, 0, "Player");
        
        wavPlayer = new WavPlayer();
        
        startTime = 0;
        elapsedTime = 0;
        calculator = new Calculator();
        
        stateMachine = new StateMachine();
        stateMachine.add(MENU, new MenuState(handler));
        stateMachine.add(WORLD, new WorldState(handler));
        battleState = new BattleState(handler);
        stateMachine.add(BATTLE, battleState);
        stateMachine.add("PokeMenu", new PokeMenu(handler));
        selectionMenu = new SelectionMenu(handler);
        stateMachine.add("SelectionMenu", selectionMenu);
        stateMachine.add("StarterSelection", new StarterSelection(handler));
        stateMachine.add("PlayerMenu", new PlayerMenu(handler));
        stateMachine.add("ShopState", new ShopState(handler));        
        
        gui = new Gui(handler);
        keyManager = new KeyManager(this);
        mouseManager = new MouseManager();
        gui.getFrame().addKeyListener(keyManager);
        gui.getFrame().addMouseListener(mouseManager);
        gui.getCanvas().addMouseListener(mouseManager);
        gui.getCanvas().addMouseMotionListener(mouseManager);
        
        backgroundMusic = new BackgroundMusic();
    }
    
    @Override
    public void run()
    {
        isRunning = true;
        stateMachine.push(MENU);
        gui.run();
        startTime = System.currentTimeMillis();
        
        int fps = 60;
        double timePerTick = 1000000000 / fps;
        double delta = 0;
        long now;
        long lastTime = System.nanoTime();
        long timer = 0;
        int ticks = 0;
        switchCooldown = 1000;
        switchLast = System.currentTimeMillis() - switchCooldown;
        
//        ArrayList<String> test = new ArrayList<>();
//        test.add("<entity>");
//        test.add("<creature>blabla</creature>");
//        test.add("<creature>bliep</creature>");
//        test.add("<creature>bloep</creature>");
//        test.add("</entity>");
////        for(String s : XMLReader.getElements(test, "creature"))
////            System.out.println(s);
//        System.out.println(XMLReader.getElement(test, "entity"));
        
        while(isRunning)
        {
            //getTime
            now = System.nanoTime();
            delta += (now-lastTime) / timePerTick;
            timer+= (now - lastTime);
            lastTime = now;
            if(delta >= 1)
            {
                elapsedTime = System.nanoTime()-startTime;
                keyManager.tick();
                
                //Develepor shortcuts for switching between states in game
                if(System.currentTimeMillis() - switchLast > switchCooldown)
                {                    
                    if(keyManager.one)
                    {
                        stateMachine.change(MENU);
                        switchLast = System.currentTimeMillis();
                    }
                    else if(keyManager.two)
                    {
                        stateMachine.change(WORLD);
                        switchLast = System.currentTimeMillis();
                    }
                    else if(keyManager.three)
                    {
                        stateMachine.change(BATTLE);
                        switchLast = System.currentTimeMillis();
                    }
                }
                
                stateMachine.update(elapsedTime);
                render();
                ticks++;
                delta--;
            }
            
            if(timer >= 1000000000)
            {
                //System.out.println("Fps: "+ticks);
                ticks = 0;
                timer = 0;
            }
        }      
    }
    
    public synchronized void start()
    {
        if(isRunning)
            return;
        thread = new Thread(this);
        thread.start();
    }
    
    public synchronized void stop()
    {
        if(!isRunning)
            return;
        isRunning = false;
        try {        
            thread.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Rpg.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void render()
    {
        BufferStrategy bs = handler.getGui().getCanvas().getBufferStrategy();        
        if(bs == null)
        {
            handler.getGui().getCanvas().createBufferStrategy(2);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.clearRect(0, 0, handler.getFrameWidth(), handler.getFrameHeight());
        
//        g.setColor(Color.WHITE);
//        g.fillRect(50, 50, 100, 100);
        stateMachine.render(g);
        
        bs.show();
        g.dispose();
    }
    
    public StateMachine getStateMachine() {
        return stateMachine;
    }

    public Gui getGui() {
        return gui;
    }

    public KeyManager getKeyManager() {
        return keyManager;
    }
    
    public MouseManager getMouseManager() {
        return mouseManager;
    }

    public Player getPlayer() {
        return player;
    }

    public Camera getCamera() {
        return camera;
    }
    
    public Calculator getCalculator() {
        return calculator;
    }
    
    public MessageBox getMessageBox() 
    {
        return messageBox;
    }
    
    public BattleState getBattleState()
    {
        return battleState;
    }
    
    public WavPlayer getWavPlayer()
    {
        return wavPlayer;
    }
    
    public void setOpponent(TrainerNpc trainer)
    {
        opponent = trainer;
    }
    
    public TrainerNpc getOpponent()
    {
        return opponent;
    }
    
    public SaveData getSaveData()
    {
        return saveData;
    }
    
    public BackgroundMusic getBackgroundMusic()
    {
        return backgroundMusic;
    }
    
    public void setBackgroundMusic(BackgroundMusic bgm)
    {
        backgroundMusic = bgm;
    }
    
    public SelectionMenu getSelectionMenu()
    {
        return selectionMenu;
    }
    
    public static void main (String[] args)
    {
        Rpg rpg  = new Rpg();
        rpg.init();
        rpg.start();
    }
}

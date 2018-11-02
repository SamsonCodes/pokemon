/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.sound;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class BackgroundMusic extends Thread
{
    Player player;
    BufferedInputStream bis;
    private String fileName;
    
    public void play()
    {
        try
        {
            File file = new File("C:\\Users\\Samson\\Documents\\NetBeansProjects\\Pokemon2\\src\\songs\\" + fileName + ".mp3");
            FileInputStream fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            try
            {
                player = new Player(bis);
                player.play();                
            }
            catch(JavaLayerException jle)
            {
                System.out.println("Bgm: jlayer exception");
                System.err.println(jle.getMessage());
            }
        }
        catch(IOException e)
        {
            System.out.println("Bgm: ioexception");
            System.err.println(e.getMessage());
        }
    }
    public void stopMusic(){
        //System.out.println("Bgm: stopping music");
        if(player!=null)
        {
            //System.out.println("Bgm: closing player");
            player.close();
        }
        else 
        {
           // System.out.println("Bgm: player is null!");
        }
    }
    
    @Override
    public void run()
    {
        play();
    }
    
    public void setSongFile(String s)
    {
        fileName = s;
    }
}

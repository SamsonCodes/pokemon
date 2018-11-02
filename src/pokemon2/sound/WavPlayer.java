/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.sound;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class WavPlayer 
{
    public static final String[] soundNames = {"ballhit", "bang", "bang2", "blurp",
        "bong", "bubbles", "chime_down", "chime_up", "crunch", "elecpulse", "photongun",
        "spacelaunchexplosive", "wailingpeep"};
    
    public static final String PATH = "C:\\Users\\Samson\\Documents\\NetBeansProjects\\Pokemon2\\src\\sounds\\";
    
    public void playSound(String name)
    {
        AudioInputStream audioInputStream;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File(PATH + name + ".wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            FloatControl gainControl = 
                (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(6.0f);
            clip.start();
        } 
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) 
        {
            System.out.println("Wav Player: Error playing sound!");
            Logger.getLogger(WavPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}

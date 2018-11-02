/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.combat;

import pokemon2.customui.MessageBox;

public class Attack 
{
    int index;
    String name;
    String[] effects;
    String element;  
    int soundId;
    int projectile;
    
    public Attack(String[] data)
    {
        index = Integer.parseInt(data[0]);
        name = data[1];
        effects = data[2].split(", ");
        if(!data[3].equals("-"))
            soundId = Integer.parseInt(data[3]);
        else
            soundId = -1;
    }
    
    public int getSoundId()
    {
        return soundId;
    }
    
    public String[] getEffects()
    {
        return effects;
    }
    
    public String getName()
    {
        return name;
    }
}

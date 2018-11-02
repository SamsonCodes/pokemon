/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.combat;

public class ScriptLine 
{
   private String message;
   private long displayTime;
   private int healthChange;
   
   public ScriptLine(String message, long displayTime, int healthChange)
   {
       this.message = message;
       this.displayTime = displayTime;
       this.healthChange = healthChange;
   }
   
   public String getMessage()
   {
       return message;
   }
   
   public long getDisplayTime()
   {
       return displayTime;
   }
   
   public int getHealthChange()
   {
       return healthChange;
   }
}

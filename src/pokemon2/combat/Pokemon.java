/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.combat;

public class Pokemon
{
    private String name;
    private int index;
    
    private int[] baseStats;
    public final static int HITPOINTS = 0, ATTACK = 1, DEFENSE = 2, S_ATTACK = 3, S_DEFENSE = 4,
            SPEED = 5, ACCURACY = 6;
    
    private String[] types;
    
    public final static String[] statNames = {"HP", "ATT", "DEF", "S_ATT", "S_DEF", "SPE", "ACC"};
    
    private int[] moves;
    
    public Pokemon(String[] stats)
    {
        this.index = Integer.parseInt(stats[0]);
        this.name = stats[1];
        
        baseStats = new int[7];
        baseStats[HITPOINTS] = Integer.parseInt(stats[2]);
        baseStats[ATTACK] = Integer.parseInt(stats[3]);
        baseStats[DEFENSE] = Integer.parseInt(stats[4]);        
        baseStats[S_ATTACK] = Integer.parseInt(stats[5]);
        baseStats[S_DEFENSE] = Integer.parseInt(stats[6]);
        baseStats[SPEED] = Integer.parseInt(stats[7]);
        baseStats[ACCURACY] = 100;
        
        types = new String[2];
        types[0] = stats[8];
        types[1] = stats[9];
        
        moves = new int[4];
        for(int i = 0; i < moves.length; i++)
        {
            moves[i] = Integer.parseInt(stats[i+10]);
        }
    }
    
    public void printStats()
    {
        System.out.println(name);
        String[] headers = {"Hitpoints", "Attack   ", "Defense  ", "S_Attack", "S_Defense", "Speed     ", "Accuracy"};
        if(headers.length == baseStats.length)
        {
            for(int i = 0; i < baseStats.length; i++)
            {
                System.out.println(headers[i] + "\t=\t" + baseStats[i]);
            }
            System.out.println();
        }
        else
        {
            System.out.println("Pokemon: printStats(): Header array doesn't match stat array in length!");
        }
    }
    
    public String getName()
    {
        return name;
    }
    
    public int getIndex()
    {
        return index;
    }
    
    public int[] getBaseStats()
    {        
        return baseStats;
    }
    
    public String[] getTypes()
    {
        return types;
    }
    
    public int getMoves(int i)
    {
        return moves[i];
    }
}

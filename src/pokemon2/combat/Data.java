/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.combat;

public class Data 
{
    private static StatReader pokeData = new StatReader("miniStats.txt",14);
    private static StatReader attackData = new StatReader("miniAttacks.txt", 6);
    private static final int P_AMOUNT = 17;
    private static Pokemon[] pokemon = new Pokemon[P_AMOUNT];
    private static final int A_AMOUNT = 21;
    private static Attack[] attacks = new Attack[A_AMOUNT];
    
    public static void init()
    {
        for(int i = 1; i <= P_AMOUNT; i++)
        {
            pokemon[i-1] = new Pokemon(pokeData.getStats(i));
        }
        for(int i = 1; i <= A_AMOUNT; i++)
        {
            attacks[i-1] = new Attack(attackData.getStats(i));
        }
    }
    
    public static Pokemon getPokemon(int id)
    {
        return pokemon[id-1];
    }
    
    public static Attack getAttack(int id)
    {
        return attacks[id-1];
    }
    
    public static int getPokemonAmount()
    {
        return P_AMOUNT;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.combat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class StatReader 
{
    private String fileName;
    private BufferedReader bufferedReader;
    private int length;
    
    public StatReader(String theName, int length)
    {
        fileName = "C:\\Users\\Samson\\Documents\\NetBeansProjects\\Pokemon2\\src"
                + "\\stats\\"+theName;
        this.length = length;
    }
    
    public void printAll()
    {
        try 
        { 
            FileReader fileReader = new FileReader(fileName);
            bufferedReader = new BufferedReader(fileReader);
            String line;
            while((line = bufferedReader.readLine()) != null) 
            {
                //splits line in individual Strings by tab entries
                String[] words = line.split("\t");
                //remove any empty strings caused by multiple tabs
                for(int i = 0; i < words.length; i++)
                {
                    if(words[i].equals(""))
                    {
                        for(int j = i; j < words.length-1; j++)
                            words[j]=words[j+1];
                    }
                }
                for(int i = 1; i<=words.length; i++)
                {
                    System.out.print((i-1)+"="+words[i-1]+" ");
                }
                System.out.println();
            }  
        } 
        catch (IOException ex)
        {
            System.out.println("Error reading file "+ fileName);
        }
    }
    
    //Gets the stats of the line that starts with given index
    public String[] getStats(int index)
    {
        String[] stats = new String[length];
        try 
        { 
            FileReader fileReader = new FileReader(fileName);
            bufferedReader = new BufferedReader(fileReader);
            String line;
            while((line = bufferedReader.readLine()) != null) 
            {  
                //splits line in individual Strings by tab entries
                String[] words = line.split("\t");
                //remove any empty strings caused by multiple tabs
                for(int i = 0; i < words.length; i++)
                {
                    if(words[i].equals(""))
                    {
                        for(int j = i; j < words.length-1; j++)
                            words[j]=words[j+1];
                    }
                } 
                if(Integer.parseInt(words[0]) == index)
                {                    
                    System.arraycopy(words, 0, stats, 0, stats.length);
                }
                //System.out.println("length = " + stats.length);
            }  
        } 
        catch (IOException ex)
        {
            System.out.println("Error reading file "+ fileName);
        }
        return stats;
    }
    
    //Returns the nth stat from the line that begins with given index id
    public String get(int id, int n)
    {
        String theStat = "notFound";
        try 
        { 
            FileReader fileReader = new FileReader(fileName);
            bufferedReader = new BufferedReader(fileReader);
            String line;
            while((line = bufferedReader.readLine()) != null) 
            {
                //splits line in individual Strings by tab entries
                String[] words = line.split("\t");
                //remove any empty strings caused by multiple tabs
                for(int i = 0; i < words.length; i++)
                {
                    if(words[i].equals(""))
                    {
                        for(int j = i; j < words.length-1; j++)
                            words[j]=words[j+1];
                    }
                }
                if(Integer.parseInt(words[0])==id)
                {
                    theStat = words[n];
                }
            }  
        } 
        catch (IOException ex)
        {
            System.out.println("Error reading file "+ fileName);
        }
        return theStat;
    }
}

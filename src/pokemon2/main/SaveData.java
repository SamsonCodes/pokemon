/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SaveData 
{
    public SaveData(){}
    
    public void saveData(ArrayList<String> data)
    {
        try{
            PrintWriter writer = new PrintWriter("pokemonsave.txt", "UTF-8");
            for(String line: data)
            {
                writer.println(line);
            }
            writer.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SaveData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SaveData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void printData()
    {
        try 
        { 
            FileReader fileReader = new FileReader("pokemonsave.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while((line = bufferedReader.readLine()) != null) 
            {
                System.out.println(line);
            }
        } 
        catch (IOException ex)
        {
            System.out.println("Error reading file");
        }
    }
    
    public String readData(int lineNumber, int wordIndex)
    {
        String dataRequested = "";
        try 
        { 
            FileReader fileReader = new FileReader("pokemonsave.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            int currentLine = 0;
            while((line = bufferedReader.readLine()) != null) 
            {
                currentLine++;
                if(currentLine == lineNumber)
                {
                    String[] words = line.split("\t");
                    if(words.length > wordIndex)
                    {
                        dataRequested = words[wordIndex];
                    }
                }
            }
        } 
        catch (IOException ex)
        {
            System.out.println("Error reading file");
        }
        return dataRequested;
    }
    
    public ArrayList<String> allData()
    {
        ArrayList<String> collectedData = new ArrayList<>();
        try 
        { 
            FileReader fileReader = new FileReader("pokemonsave.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while((line = bufferedReader.readLine()) != null) 
            {
                collectedData.add(line);
            }
            return collectedData;
        } 
        catch (IOException ex)
        {
            System.out.println("Error reading file");
        }
        return null;
    }
    
    public ArrayList<String> entityData()
    {
        ArrayList<String> collectedData = new ArrayList<>();
        try 
        { 
            FileReader fileReader = new FileReader("pokemonsave.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            boolean beginFound = false;
            boolean endFound = false;
            while((line = bufferedReader.readLine()) != null) 
            {
                if(line.equals("</worldData>"))
                {
                    endFound = true;
                }
                if(beginFound &! endFound)
                {
                    collectedData.add(line);
                }
                if(line.equals("<worldData>"))
                {
                    beginFound = true;
                }
            }
            return collectedData;
        } 
        catch (IOException ex)
        {
            System.out.println("Error reading file");
        }
        return null;
    }
    
    public String getItemString()
    {
        try 
        { 
            FileReader fileReader = new FileReader("pokemonsave.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while((line = bufferedReader.readLine()) != null) 
            {
                String[] split = line.split("\t");
                if(split[0].equals("items"))
                {
                    return line;
                }
            }
        } 
        catch (IOException ex)
        {
            System.out.println("Error reading file");
        }
        return null;
    }
    
}

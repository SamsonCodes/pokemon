/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.combat;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import pokemon2.assets.Assets;
import pokemon2.customui.MessageBox;
import pokemon2.main.Handler;

public class Creature extends CombatEntity
{
    String name;
    int index;
    String[] types = new String[2];
    private double[] baseStats, stats;
    private Attack[] attacks = new Attack[4];
    boolean conscious;  
    private BufferedImage image;
    private int level;
    private int experience, experienceReq, progress;
    public ArrayList<Creature> foughtWith;
    private int position;
    private Font nameFont = new Font("Arial", Font.BOLD, 12);
    private DecimalFormat df = new DecimalFormat("0.##");
    private boolean active;
    
    public Creature(Handler handler, int id, int position, int level)
    {        
        name = Data.getPokemon(id).getName();
        index = id;
        types = Data.getPokemon(id).getTypes();
        this.position = position;
        
        this.handler = handler;
        
        this.level = level;
        experience = (int) Math.pow(level, 3);
        experienceReq = (int) (Math.pow(level+1,3)-Math.pow(level,3));
        progress = (int) (experience - Math.pow(level,3)); 
        
        baseStats = new double[7];
        int[] dataBaseStats = Data.getPokemon(id).getBaseStats();
        
        baseStats[Pokemon.HITPOINTS] = ((0.25 + (level/23.0))*dataBaseStats[Pokemon.HITPOINTS]);
        
        for(int i = Pokemon.ATTACK; i <= Pokemon.SPEED; i++)
        {
            baseStats[i] = (0.1+level/50.0)*dataBaseStats[i];
        }
        
        baseStats[Pokemon.ACCURACY] = 90;
        
        stats = new double[baseStats.length];
        System.arraycopy(baseStats, 0, stats, 0, baseStats.length);
        
        for(int i = 0; i < 4; i++)
        {
            int attackId = Data.getPokemon(id).getMoves(i);
            if(attackId != 0)
                attacks[i] = Data.getAttack(attackId);
        }
        
        foughtWith = new ArrayList<>();
        
        conscious = true;
        active = true;
    }
    
    public void setLevel(int lvl)
    {
        level = lvl;
        experience = (int) Math.pow(level, 3);
        experienceReq = (int) (Math.pow(level+1,3)-Math.pow(level,3));
        progress = (int) (experience - Math.pow(level,3)); 
        System.out.println(name + " level set to " + level + ".");
        //Recalculate and reset stats
        int[] dataBaseStats = Data.getPokemon(index).getBaseStats();
        baseStats[Pokemon.HITPOINTS] = (0.25 + (level/23.0))*dataBaseStats[Pokemon.HITPOINTS];

        for(int i = Pokemon.ATTACK; i <= Pokemon.SPEED; i++)
        {
            baseStats[i] = (0.1+level/50.0)*dataBaseStats[i];
        }

        stats[Pokemon.HITPOINTS] = baseStats[Pokemon.HITPOINTS];
        restoreStats();
    }
    public void setExperience(int level, int progress)
    {
        this.level = level;
        this.progress = progress;
        this.experience = (int) (Math.pow(level, 3) + progress);
        experienceReq = (int) (Math.pow(level+1,3)-Math.pow(level,3));
        System.out.println(name + " level set to " + level + ".");
        //Recalculate and reset stats
        int[] dataBaseStats = Data.getPokemon(index).getBaseStats();
        baseStats[Pokemon.HITPOINTS] = (0.25 + (level/23.0))*dataBaseStats[Pokemon.HITPOINTS];

        for(int i = Pokemon.ATTACK; i <= Pokemon.SPEED; i++)
        {
            baseStats[i] = (0.1+level/50.0)*dataBaseStats[i];
        }

        stats[Pokemon.HITPOINTS] = baseStats[Pokemon.HITPOINTS];
        restoreStats();
    }
    
    @Override
    public void tick() 
    {
        
    }
    
    @Override
    public void render(Graphics g) 
    {
        if(active)
        {
            switch(position)
            {
                case 1: 
                    image = Assets.pokemon[index-1][1];
                    x = 40;
                    y = 120;
                    size = 288;
                    break;
                case 2:
                    image = Assets.pokemon[index-1][2];
                    x = 355;
                    y = 65;
                    size = 128;
                    break;
            }
            g.drawImage(image, x, y, size, size, null);
            int barX = 0, barY = 0;
            switch(position)
            {
                case 1: 
                    barX = x + 90;
                    barY = y + 40;
                    break;
                case 2: 
                    barX = x + 20;
                    barY = y;
                    break;
            }
            int length = (int) Math.ceil(100*stats[Pokemon.HITPOINTS] / baseStats[Pokemon.HITPOINTS]);

            g.setColor(Color.RED);
            g.fillRect(barX, barY, 100, 10);

            g.setColor(Color.GREEN);
            g.fillRect(barX, barY, length, 10);

            g.setColor(Color.BLACK);
            g.drawRect(barX, barY, 100, 10);

            int eBarX = 0, eBarY = 0;
            switch(position)
            {
                case 1: 
                    eBarX = x + 90;
                    eBarY = y + 50;
                    break;
                case 2: 
                    eBarX = x + 20;
                    eBarY = y + 10;
                    break;
            }
            int eLength = (int) Math.ceil(100*progress / experienceReq);


            g.setColor(Color.BLUE);
            g.fillRect(eBarX, eBarY, eLength, 5);

    //        g.setColor(Color.BLACK);
    //        g.drawRect(eBarX, eBarY, 100, 10);

            Font currentFont = g.getFont();
            g.setFont(nameFont);
            g.setColor(Color.BLACK);
            g.drawString(name + " lvl" + level, barX, barY - 20);
            g.setFont(currentFont);
        }
    }
    
    public void damage(double rawDamage, boolean special)
    {
        //printStats();
        double actualDamage;
        if(!special)
        {
            //System.out.println("Physical");
            actualDamage = rawDamage / (stats[Pokemon.DEFENSE]);
            //System.out.println(df.format(rawDamage) + "/" + df.format(stats[Pokemon.DEFENSE]) + " = " + df.format(actualDamage));
        }
        else
        {
            //System.out.println("Special");
            actualDamage = rawDamage / (stats[Pokemon.S_DEFENSE]);
            //System.out.println(df.format(rawDamage) + "/" + df.format(stats[Pokemon.S_DEFENSE]) + " = " + df.format(actualDamage));
        }
        //System.out.println("Creature: rawDamage on " + name + " is " + df.format(rawDamage));
        stats[Pokemon.HITPOINTS] -= actualDamage; 
        System.out.println("Creature: " + name + " takes " + df.format(actualDamage) + " damage. Hp = " + df.format(stats[Pokemon.HITPOINTS]));
        if(stats[Pokemon.HITPOINTS] <= 0)
        {
            stats[Pokemon.HITPOINTS] = 0;
            conscious = false;
            System.out.println("Creature: " + name + " fainted");
        }
    } 
    
    public void heal(double percentage, MessageBox messageBox)
    {
        double amount = (percentage/100.0)*baseStats[Pokemon.HITPOINTS];
        if(stats[Pokemon.HITPOINTS]+amount > baseStats[Pokemon.HITPOINTS])
        {
            amount = baseStats[Pokemon.HITPOINTS] - stats[Pokemon.HITPOINTS];
        }
        stats[Pokemon.HITPOINTS] += amount;
        System.out.println(name + " health restored by " + df.format(amount) + ". Hp = " + df.format(stats[Pokemon.HITPOINTS]));
        if(amount > 0)
        {
            handler.getWavPlayer().playSound("chime_up");
            messageBox.setText(name + "'s health was restored");
        }
        else
        {
            messageBox.setText(name + " is already at full health!");
        }
    }
    
    public void changeStat(int stat, double percentage, MessageBox messageBox)
    {
        double amount = (percentage/100.0)*baseStats[stat];
        boolean minimal = false;
        boolean maximal = false; 
        double minimum = 0.5;
        double maximum = 20.0;
        if((stats[stat] + amount) < minimum*baseStats[stat])
        {
            amount = minimum*baseStats[stat] - stats[stat];
            minimal = true;
        }
        if((stats[stat] + amount) > maximum*baseStats[stat])
        {
            amount = maximum*baseStats[stat] - stats[stat];
            maximal = true;
        }
        stats[stat] += amount;
        if(amount > 0)
        {
            handler.getWavPlayer().playSound("chime_up");
            messageBox.setText(name + "'s " + Pokemon.statNames[stat] + " increased");
        }
        else if (amount < 0)
        {
            handler.getWavPlayer().playSound("chime_down");
            messageBox.setText(name + "'s " + Pokemon.statNames[stat] + " decreased");
        }
        else
        {
            if(maximal)
            {
                messageBox.setText(name + "'s " + Pokemon.statNames[stat] + " won't go any higher.");
            }
            else if(minimal)
            {
                messageBox.setText(name + "'s " + Pokemon.statNames[stat] + " won't go any lower.");
            }
        }
        System.out.println("Creature: " + name + "'s " + Pokemon.statNames[stat] + " increased by " 
                + df.format(amount) + ". " + Pokemon.statNames[stat] + " = " + df.format(stats[stat]));
    }
    
    public void distributePoints()
    {
        System.out.println(name + " distributing points.");
        int totalExperience = (level*statSum())/3;
        int experiencePer;
        
        if(!foughtWith.isEmpty())
        {
            Iterator<Creature> itr = foughtWith.iterator();
            while(itr.hasNext())
            {
                Creature c = itr.next();
                if(!c.isConscious())
                {
                    itr.remove();
                }
            }
            if(!foughtWith.isEmpty())
            {
                experiencePer = totalExperience/foughtWith.size();
                for(Creature c: foughtWith)
                {
                    c.addExperience(experiencePer);
                }
            }
            else
            {
                System.out.println("NO CONSCIOUS CREATURES TO DISTRIBUTE EXPIERENCE TO!!!");
            }
        }
        else
        {
            System.out.println("NO CREATURES TO DISTRIBUTE EXPIERENCE TO!!!");
        }
        
    }
    
    public ArrayList<String> createDistributionList()
    {
        ArrayList<String> list = new ArrayList<>();
        
        System.out.println(name + " distributing points.");
        int totalExperience = (level*statSum())/3;
        int experiencePer;
        
        if(!foughtWith.isEmpty())
        {
            Iterator<Creature> itr = foughtWith.iterator();
            while(itr.hasNext())
            {
                Creature c = itr.next();
                if(!c.isConscious())
                {
                    itr.remove();
                }
            }
            if(!foughtWith.isEmpty())
            {
                experiencePer = totalExperience/foughtWith.size();
                for(Creature c: foughtWith)
                {
                    list.add(c.getName() + "," + experiencePer);
                }
            }
            else
            {
                System.out.println("NO CONSCIOUS CREATURES TO DISTRIBUTE EXPIERENCE TO!!!");
            }
        }
        else
        {
            System.out.println("NO CREATURES TO DISTRIBUTE EXPIERENCE TO!!!");
        }
        
        return list;
    }
    
    private int statSum()
    {
        int sum = 0;
        for(int i = Pokemon.HITPOINTS; i <= Pokemon.SPEED; i++)
        {
            sum+=baseStats[i];
        }
        return sum;
    }

    public void addExperience(int exp) 
    {
        if(level < 100)
        {
            int lastLevel = level;
            experience+=exp;            
            System.out.println(name + " received " + exp + " exp.");            
            level = (int) Math.floor(Math.pow(experience, 1/3.0));
            if(level>=100)
            {
                level = 100;
                experienceReq = 0;
                progress = 0;
            }
            else
            {
                experienceReq = (int) (Math.pow(level+1,3)-Math.pow(level,3));
                progress = (int) (experience - Math.pow(level,3)); 
            }
            if(level>lastLevel)
            {
                handler.getWavPlayer().playSound("chime_up");
                System.out.println(name + " leveled up to " + level + ".");
                //Recalculate and reset stats
                int[] dataBaseStats = Data.getPokemon(index).getBaseStats();
                double currentRatio = stats[Pokemon.HITPOINTS]/baseStats[Pokemon.HITPOINTS];
                baseStats[Pokemon.HITPOINTS] = (0.25 + (level/23.0))*dataBaseStats[Pokemon.HITPOINTS];
        
                for(int i = Pokemon.ATTACK; i <= Pokemon.SPEED; i++)
                {
                    baseStats[i] = (0.1+level/50.0)*dataBaseStats[i];
                }
                
                stats[Pokemon.HITPOINTS] = (int) (currentRatio*baseStats[Pokemon.HITPOINTS]);
                restoreStats();
            }
        }        
    }
    
    public Creature catchCopy()
    {
        active = false;
        Creature daCatch = new Creature(handler, index, 1, level);
        daCatch.setStats(Pokemon.HITPOINTS, stats[Pokemon.HITPOINTS]);
        return daCatch;
    }
    
    public boolean feelingLucky()
    {
        Random random = new Random();
        return ((random.nextInt(100)+1) <= stats[Pokemon.ACCURACY]);
    }
    
    public void restoreStats()
    {
        for(int i = 0; i < stats.length; i++)
        {
            if(i != Pokemon.HITPOINTS)
            {
                stats[i] = baseStats[i];
            }
        }
        System.out.println(name + "'s stats were restored to their default values");       
    }
    
    public void restoreHealth()
    {
        stats[Pokemon.HITPOINTS] = baseStats[Pokemon.HITPOINTS];
        conscious = true;
        System.out.println(name + " was restored to full health");
    }
    
    public void printStats()
    {
        System.out.println(name);
        String[] headers = {"Hitpoints", "Attack   ", "Defense  ", "S_Attack",  "S_Defense", "Speed     ", "Accuracy"};
        if(headers.length == stats.length)
        {
            for(int i = 0; i < stats.length; i++)
            {
                System.out.println(headers[i] + "\t=\t" + stats[i]);
            }
            System.out.println();
        }
        else
        {
            System.out.println("Pokemon: printStats(): Header array doesn't match stat array in length!");
        }
    }
    
    public void printBaseStats()
    {
        System.out.println(name);
        String[] headers = {"Hitpoints", "Attack   ", "Defense  ", "S_Attack",  "S_Defense", "Speed     ", "Accuracy"};
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
            System.out.println("Pokemon: printBaseStats(): Header array doesn't match stat array in length!");
        }
    }
    
    public int getAttackAmount()
    {
        int amount = 0;
        for(int i = 0; i < 4; i++)
        {
            if(attacks[i] != null)
            {
                amount++;
            }
        }
        return amount;
    }
    
    public Attack[] getAttacks()
    {
        return attacks;
    }
    
    public Attack getAttacks(int i)
    {
        return attacks[i];
    }
    
    public boolean isConscious()
    {
        return conscious;
    }
    
    public double getStats(int i)
    {
        return stats[i];
    }
    
    public int getIndex()
    {
        return index;
    }
    
    public String getName()
    {
        return name;
    }
    
    public String[] getTypes()
    {
        return types;
    }
    
    public int getLevel()
    {
        return level;
    }
    
    public int getExperience()
    {
        return experience;
    }
    
    public int getProgress()
    {
        return progress;
    }

    public void setStats(int i, double value) 
    {
        if(i == 0)
        {
            if(value <= 0)
            {
                value = 0;
                conscious = false;
            }
        }
        stats[i] = value;
    }
    
    public int getPosition()
    {
        return position;
    }
    
    public void setPosition(int i)
    {
        position = i;
    }
    
    public double getCondition()
    {
        return (stats[Pokemon.HITPOINTS]/baseStats[Pokemon.HITPOINTS]);
    }
    
    public void setActive(boolean b)
    {
        active = b;
    }
    
    public String createSaveData()
    {
        String s = "";
        s += "<creature>";
        s += "<index>" + index + "</index><level>" + level + "</level>";
        s += "<progress>" + progress + "</progress><health>" + stats[0] + "</health>";
        s += "</creature>";
        return s;
    }
}

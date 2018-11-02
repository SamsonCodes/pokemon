/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.assets;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Assets 
{
    
    public static BufferedImage holdingPokeball;
    public static BufferedImage item;
    public static BufferedImage[][] pokemon;
    
    public static BufferedImage button, background;
    
    public static BufferedImage[] bbg;
    
    public static BufferedImage[] player_down, player_up, player_left, player_right,
            charizard, archeops, bulbasaur;
    public static BufferedImage[] tiles1, tiles2, tiles3, tiles4;    
    
    public static BufferedImage[][] npcs, npcs_down, npcs_up, npcs_left, npcs_right;
    
    private final static String PATH = "C:\\Users\\Samson\\Documents\\NetBeansProjects\\Pokemon2\\src\\images\\";
    
   
    public static void init()
    {
        //Trainer sheet
        SpriteSheet trainersheet = new SpriteSheet(PATH+"trainerSheet.png");      
        int trainerX = 30, trainerY = 30;
        int trainerWidth = 22, trainerHeight = 22; 
        item = trainersheet.crop(10*trainerX, 2*trainerY, trainerWidth, trainerHeight);
        holdingPokeball = trainersheet.crop(10*trainerX, 1*trainerY, trainerWidth, trainerHeight);
        
        pokemon = new BufferedImage[17][3];
        for(int i = 0; i < pokemon.length; i ++)
        {
            try 
            {
                pokemon[i][0] = ImageIO.read(new File(PATH+"pokemon\\pic" + (i+1)  + ".png"));
                pokemon[i][1] = ImageIO.read(new File(PATH+"pokemon\\pic" + (i+1)  + "b.png"));
                pokemon[i][2] = ImageIO.read(new File(PATH+"pokemon\\pic" + (i+1)  + "f.png")); 
            } 
            catch (IOException ex)
            {
                System.out.println("Error loading image");
            }
        }
        
        //Player animations
        player_down = new BufferedImage[4];        
        player_down[0] = trainersheet.crop(0,0,trainerWidth,trainerHeight);
        player_down[1] = trainersheet.crop(0,5*trainerY,trainerWidth,trainerHeight);
        player_down[2] = trainersheet.crop(0,0,trainerWidth,trainerHeight);
        player_down[3] = trainersheet.crop(0,trainerY,trainerWidth,trainerHeight);
        
        player_up = new BufferedImage[4];
        player_up[0] = trainersheet.crop(2*trainerX,0,trainerWidth,trainerHeight);
        player_up[1] = trainersheet.crop(2*trainerX,trainerY,trainerWidth,trainerHeight);
        player_up[2] = trainersheet.crop(2*trainerX,0,trainerWidth,trainerHeight);
        player_up[3] = trainersheet.crop(2*trainerX,5*trainerY,trainerWidth,trainerHeight);
        
        player_left = new BufferedImage[4];
        player_left[0] = trainersheet.crop(trainerX,0,trainerWidth,trainerHeight);
        player_left[1] = trainersheet.crop(trainerX,trainerY,trainerWidth,trainerHeight);
        player_left[2] = trainersheet.crop(trainerX,0,trainerWidth,trainerHeight);
        player_left[3] = trainersheet.crop(trainerX,5*trainerY,trainerWidth,trainerHeight);
        
        player_right = new BufferedImage[4];
        player_right[0] = trainersheet.crop(3*trainerX,0,trainerWidth,trainerHeight);
        player_right[1] = trainersheet.crop(3*trainerX,trainerY,trainerWidth,trainerHeight);
        player_right[2] = trainersheet.crop(3*trainerX,0,trainerWidth,trainerHeight);
        player_right[3] = trainersheet.crop(3*trainerX,5*trainerY,trainerWidth,trainerHeight);
        
        //Tilesheet 1
        SpriteSheet sheet1 = new SpriteSheet(PATH+"tileset-advance.png");
        int tiles1size = 16;
        int tiles1columns = 8;
        int tiles1rows = 998;
        tiles1 = new BufferedImage[tiles1columns*tiles1rows];
        for(int i = 0; i < tiles1.length; i++)
        {
            tiles1[i] = sheet1.crop((i%tiles1columns)*tiles1size, (i/tiles1columns)*tiles1size, tiles1size, tiles1size);
        }
        
        //Tilesheet 2
        SpriteSheet sheet2 = new SpriteSheet(PATH+"tileset-advance2.png");
        int tiles2size = 16;
        int tiles2columns = 88;
        int tiles2rows = 69; 
        tiles2 = new BufferedImage[tiles2columns*tiles2rows];
        for(int i = 0; i < tiles2.length; i++)
        {
            tiles2[i] = sheet2.crop((i%tiles2columns)*tiles2size, (i/tiles2columns)*tiles2size, tiles2size, tiles2size);
        }
        
        //Tilesheet 3
        SpriteSheet sheet3 = new SpriteSheet(PATH+"tileset-advance3.png");
        int tiles3space = 17;
        int tiles3size = 16; 
        tiles3 = new BufferedImage[1708];
        for(int i = 0; i < tiles3.length; i ++)
        {           
            tiles3[i] = sheet3.crop((i%61)*tiles3space, (i/61)*tiles3space, tiles3size, tiles3size);
        }
        
        SpriteSheet sheet4 = new SpriteSheet(PATH+"innenraum.png");
        int tiles4space = 16;
        int tiles4size = 16; 
        tiles4 = new BufferedImage[1426];
        for(int i = 0; i < tiles4.length; i ++)
        {           
            tiles4[i] = sheet4.crop((i%31)*tiles4space, (i/31)*tiles4space, tiles4size, tiles4size);
        }
        //Button
        try 
        {
            button = ImageIO.read(new File(PATH+"button2.png"));
        } 
        catch (IOException ex)
        {
            System.out.println("Error loading image");
        }
        
        //Background
        try 
        {
            background = ImageIO.read(new File(PATH+"background.png"));
        } 
        catch (IOException ex)
        {
            System.out.println("Error loading image");
        }
        
        //Battle Backgrounds
        SpriteSheet bbgSheet = new SpriteSheet(PATH+"pokebattle.png");
        int x0 = 0;
        int y0 = 0;
        int xStep = 243;
        int yStep = 135;
        int xWidth = 4;
        int yWidth = 4;
        bbg = new BufferedImage[15];
        for(int i = 0; i < bbg.length; i ++)
        {
            bbg[i] = bbgSheet.crop((i%xWidth)*xStep, (i/xWidth)*yStep + 21, xStep, yStep - 21);
        }
        
        //Pokemon "gif" animations
        charizard = new BufferedImage[62];
        for(int i = 0; i < charizard.length; i++)
        {
            try 
            {
                charizard[i] = ImageIO.read(new File(PATH+"charizard\\frame_" + i  + "_delay-0.03s.png"));
            } 
            catch (IOException ex)
            {
                System.out.println("Error loading image");
            }
        }
        archeops = new BufferedImage[38];
        for(int i = 0; i < archeops.length; i++)
        {
            try 
            {
                archeops[i] = ImageIO.read(new File(PATH+"archeops\\frame_" + i  + "_delay-0.04s.png"));
            } 
            catch (IOException ex)
            {
                System.out.println("Error loading image");
            }
        }
        
        bulbasaur = new BufferedImage[50];
        for(int i = 0; i < bulbasaur.length; i++)
        {
            try 
            {
                bulbasaur[i] = ImageIO.read(new File(PATH+"bulbasaur\\frame_" + i  + "_delay-0.1s.png"));
            } 
            catch (IOException ex)
            {
                System.out.println("Error loading image");
            }
        }
        
        SpriteSheet npcSheet = new SpriteSheet(PATH + "emeraldnpcs.png");
        npcs = new BufferedImage[5][12];
        int[] npcX = {144, 275, 410, 410, 275};
        int[] npcY = {159, 160, 184, 137, 299};
        int[] npcWidth = {22, 22, 19, 19, 20, 20, 20, 20, 20, 20, 20, 20};
        int npcHeight = 22;
        int[] distance = {0, 20, 40, 40, 57, 57, 75, 75, 92, 92, 110, 110};
        for(int j = 0; j < npcs.length; j++)
        {
            for(int i = 0; i < npcs[0].length; i++)
            {
                npcs[j][i] = npcSheet.crop(npcX[j] + distance[i], npcY[j], npcWidth[i], npcHeight);     
                if(i == 3 || i == 5 || i == 7 || i == 9 || i == 11)
                {
                    // Flip the image horizontally
                    AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
                    tx.translate(-npcs[j][i].getWidth(null), 0);
                    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                    npcs[j][i] = op.filter(npcs[j][i], null);
                }
            }
        }
        npcs_down = new BufferedImage[5][4];
        for(int i = 0; i < npcs.length; i++)
        {
            npcs_down[i][0] = npcs[i][0];
            npcs_down[i][1] = npcs[i][4];
            npcs_down[i][2] = npcs[i][0];
            npcs_down[i][3] = npcs[i][5];
        }
        npcs_up = new BufferedImage[5][4];
        for(int i = 0; i < npcs.length; i++)
        {
            npcs_up[i][0] = npcs[i][1];
            npcs_up[i][1] = npcs[i][6];
            npcs_up[i][2] = npcs[i][1];
            npcs_up[i][3] = npcs[i][7];
        }
        npcs_left = new BufferedImage[5][4];
        for(int i = 0; i < npcs.length; i++)
        {
            npcs_left[i][0] = npcs[i][2];
            npcs_left[i][1] = npcs[i][8];
            npcs_left[i][2] = npcs[i][2];
            npcs_left[i][3] = npcs[i][10];
        }
        npcs_right = new BufferedImage[5][4];
        for(int i = 0; i < npcs.length; i++)
        {
            npcs_right[i][0] = npcs[i][3];
            npcs_right[i][1] = npcs[i][9];
            npcs_right[i][2] = npcs[i][3];
            npcs_right[i][3] = npcs[i][11];
        }
//        for(int i = 0; i < npcs[0].length; i++)
//        {
//            npcs[0][i] = npcSheet.crop(144 + distance[i], 159, npcWidth[i], npcHeight);     
//            if(i == 3)
//            {
//                // Flip the image horizontally
//                AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
//                tx.translate(-npcs[0][i].getWidth(null), 0);
//                AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
//                npcs[0][i] = op.filter(npcs[0][i], null);
//            }
//        }
//        for(int i = 0; i < npcs[0].length; i++)
//        {
//            npcs[1][i] = npcSheet.crop(275 + distance[i], 160, npcWidth[i], npcHeight);
//            if(i == 3)
//            {
//                // Flip the image horizontally
//                AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
//                tx.translate(-npcs[1][i].getWidth(null), 0);
//                AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
//                npcs[1][i] = op.filter(npcs[1][i], null);
//            }
//        }
//        for(int i = 0; i < npcs[0].length; i++)
//        {
//            npcs[2][i] = npcSheet.crop(410 + distance[i], 184, npcWidth[i], npcHeight);
//            if(i == 3)
//            {
//                // Flip the image horizontally
//                AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
//                tx.translate(-npcs[2][i].getWidth(null), 0);
//                AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
//                npcs[2][i] = op.filter(npcs[2][i], null);
//            }
//        }
//        for(int i = 0; i < npcs[0].length; i++)
//        {
//            npcs[3][i] = npcSheet.crop(410 + distance[i], 137, npcWidth[i], npcHeight);
//            if(i == 3)
//            {
//                // Flip the image horizontally
//                AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
//                tx.translate(-npcs[3][i].getWidth(null), 0);
//                AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
//                npcs[3][i] = op.filter(npcs[3][i], null);
//            }
//        }
//        for(int i = 0; i < npcs[0].length; i++)
//        {
//            npcs[4][i] = npcSheet.crop(275 + distance[i], 299, npcWidth[i], npcHeight);
//            if(i == 3)
//            {
//                // Flip the image horizontally
//                AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
//                tx.translate(-npcs[4][i].getWidth(null), 0);
//                AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
//                npcs[4][i] = op.filter(npcs[4][i], null);
//            }
//        }
    }
}

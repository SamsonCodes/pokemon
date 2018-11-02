/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.combat;

import java.awt.Graphics;
import pokemon2.main.Handler;

public abstract class CombatEntity 
{
    protected int x, y, size, height;
    protected Handler handler;
    
    public abstract void tick();
    public abstract void render(Graphics g); 

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return size;
    }

    public void setWidth(int width) {
        this.size = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}

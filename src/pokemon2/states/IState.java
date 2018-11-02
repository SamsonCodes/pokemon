/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.states;

import java.awt.Graphics;

public interface IState 
{
    public void onEnter();
    public void onExit();
    public void update(double elapsedTime);
    public void render(Graphics g);
}

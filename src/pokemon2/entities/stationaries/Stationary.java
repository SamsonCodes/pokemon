/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.entities.stationaries;

import pokemon2.main.Handler;
import pokemon2.entities.Entity;

public abstract class Stationary extends Entity
{

    public Stationary(Handler handler, int x, int y, int width, int height, String name)
    {
        super(handler, x, y, width, height, name);
    }
}

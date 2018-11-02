/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.math;

public class Calculator 
{
    public double distance(double x1, double y1, double x2, double y2)
    {
        return Math.sqrt((Math.pow(x2 - x1, 2)+ Math.pow(y2 - y1,2)));
    }
}

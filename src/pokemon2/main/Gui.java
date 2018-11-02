/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemon2.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;

public class Gui extends JFrame implements Runnable
{
    private JFrame frame;
    private Handler handler;
    private Canvas canvas;
    
    
    Gui(Handler handler)
    {
        this.handler = handler;
        frame = new JFrame(handler.getTitle());
        frame.setSize(handler.getFrameWidth(), handler.getFrameHeight()); 
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        createView();
    }
    
    @Override
    public void run()
    {
        frame.setVisible(true);
    }
    
    private void createView()
    {
        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(handler.getFrameWidth(), handler.getFrameHeight()));
        canvas.setFocusable(false);
        canvas.setBackground(Color.BLACK);
        frame.add(canvas);
        
        frame.pack();
    }

    public Canvas getCanvas() 
    {
        return canvas;
    }
    
    public JFrame getFrame()
    {
        return frame;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.rain_2d.util;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

/**
 *
 * @author Abhijeet
 */
public class Log extends JFrame
{
    boolean init = false;
    
    protected static Log log;
    static JTextArea logSpace;
    JScrollPane pane;
    
    private Log()
    {
        this.init = true;
        super.setSize(300, 300);
        logSpace = new JTextArea(5, 20);
        pane = new JScrollPane(logSpace);
        super.add(pane);
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        DefaultCaret caret = (DefaultCaret)logSpace.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        super.setLocationRelativeTo(null);
        //super.setVisible(true);
    }
    
    private static Log getLog()
    {
        return log;
    }
    
    public static void init()
    {
        log = new Log();
    }
    
    public static void log(String str)
    {
        getLog().logSpace.append(str + "\n");
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Engine.GameWindow;

/**
 *
 * @author Brian
 */
public class Main extends GameWindow {
    
    public Main() 
    {
        setRunner(new Linx());
    }
    
    public static void main(String[] args)
    {
        (new Main()).startProgram();
    }
}

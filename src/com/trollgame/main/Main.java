package com.trollgame.main;

import com.trollgame.core.Game;
import javax.swing.SwingUtilities;

public class Main {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Game::new);
    }
}

package main;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class GameWindow {
	
	private JFrame window;
	
	private Dimension screenSize;
	
	public GameWindow(GamePanel game_panel){
		window = new JFrame();
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		window.add(game_panel);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocation((int)(screenSize.getWidth() - Game.GAME_WIDTH) / 2, (int)(screenSize.getHeight() - Game.GAME_HEIGHT) / 2);
		window.setResizable(false);
		window.pack();
		window.setVisible(true);
	}
}

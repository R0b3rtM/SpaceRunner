package main;

import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class GamePanel extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private Game game;
	
	public GamePanel(Game game) {
		
		this.game = game;
		
		set_panel_size();
		
	}
	
	private void set_panel_size() {
		Dimension dim = new Dimension(GAME_WIDTH, GAME_HEIGHT);
		
		setMinimumSize(dim);
		setMaximumSize(dim);
		setPreferredSize(dim);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		game.render(g);
	}

}

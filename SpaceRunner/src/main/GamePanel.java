package main;

import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import inputs.KeyBoardInputs;
import inputs.MouseInputs;

public class GamePanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private Game game;
	
	public GamePanel(Game game) {
		
		this.game = game;
		
		KeyBoardInputs key_board_inputs = new KeyBoardInputs(this);
		MouseInputs mouse_inputs = new MouseInputs();
		set_panel_size();
		
		addKeyListener(key_board_inputs);
		addMouseListener(mouse_inputs);
		addMouseMotionListener(mouse_inputs);
		
	}
	
	public Game getGame() {
		return game;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		game.render(g);
	}
	
	private void set_panel_size() {
		Dimension dim = new Dimension(GAME_WIDTH, GAME_HEIGHT);
		
		setMinimumSize(dim);
		setMaximumSize(dim);
		setPreferredSize(dim);
	}

}

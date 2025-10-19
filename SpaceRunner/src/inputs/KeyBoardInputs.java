package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.Game;
import main.GamePanel;

public class KeyBoardInputs implements KeyListener{
	
	private GamePanel game_panel;
	
	public KeyBoardInputs(GamePanel game_panel) {
		this.game_panel = game_panel;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(game_panel.getGame().getGameState() == Game.MENU_STATE) {
			game_panel.getGame().setPause(false);
		}
		
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			game_panel.getGame().getPlayer().setJump(true);
			break;
		case KeyEvent.VK_SPACE:
			game_panel.getGame().getPlayer().tryShoot();
			break;
		case KeyEvent.VK_ENTER:
			game_panel.getGame().getPlayer().hurt();
			break;
		case KeyEvent.VK_R:
			if(game_panel.getGame().getGameState() == Game.DEATH_STATE)
				game_panel.getGame().restartGame();
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			game_panel.getGame().getPlayer().setJump(false);
			break;
		}
	}

}

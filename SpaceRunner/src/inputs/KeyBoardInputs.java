package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			game_panel.getGame().getPlayer().setJump(true);
			break;
		case KeyEvent.VK_SPACE:
			game_panel.getGame().getPlayer().tryShoot();
			break;
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

package items;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import utilities.LoadAssets;

public class AlienBall {
	
	private BufferedImage sprite;
	private Dimension last_player_pos;
	
	private float x, y;
	private float gravity = 0.04f;
	
	public AlienBall(float x, float y, Dimension last_player_pos) {
		this.last_player_pos = last_player_pos;
		this.x = x;
		this.y = y;
		
		sprite = LoadAssets.LoadSpriteImg(LoadAssets.BALL_SPRITE);
	}
	
	public void updateBall() {
		
	}
	
	public void renderBall(Graphics g) {
		g.drawImage(sprite, (int)x, (int)y, Game.TILES_SIZE, Game.TILES_SIZE, null);
	}
}

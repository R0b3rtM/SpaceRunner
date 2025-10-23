package utilities;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.Game;
import static utilities.Constants.HUDConstants.*;

public class HUD {
	
	private static final int ANIM_FRAMES = 5;
	
	private BufferedImage heart, coin, reload_circle;
	private BufferedImage[] reload_circle_anim;
	private Font game_font;
	private int hud_index = MAIN_HUD;
	
	private int anim_tick = 0, anim_state = 0;
	
	public HUD() {
		heart = LoadAssets.LoadSpriteImg(LoadAssets.HEART_SPRITE);
		coin = LoadAssets.LoadSpriteImg(LoadAssets.COIN_SPRITE);
		animInit();
		
		LoadAssets.LoadFont();
		game_font = LoadAssets.LoadFont();
		
	}
	
	public void renderHUD(Graphics g, int player_lives, int player_coins) {
		// Display coin icon
		g.drawImage(coin, Game.GAME_WIDTH - Game.TILES_SIZE, 0, Game.TILES_SIZE, Game.TILES_SIZE, null);
		
		renderLives(g, player_lives);
		renderCoins(g, player_coins);
		
		// Render the right HUD
		switch (hud_index) {
		case MAIN_HUD:
			// Main menu display
			startMenu(g);
			break;
			
		case PLAY_HUD:
			// Play menu display
			g.drawImage(reload_circle_anim[anim_state], 0, Game.GAME_HEIGHT - Game.TILES_SIZE, Game.TILES_SIZE, Game.TILES_SIZE, null);
			break;
			
		case DEATH_HUD:
			// Death menu display
			resetMenu(g);
			break;
		}
	}
	
	public void setHUD(int index) {
		hud_index = index;
	}
	
	public void reloadCircleAnim(int anim_speed) {
		if(anim_tick >= anim_speed/(ANIM_FRAMES-1.5f)) {
			anim_state++;
			anim_tick = 0;
			if(anim_state >= ANIM_FRAMES)
				anim_state = ANIM_FRAMES-1;
		}
		anim_tick++;
			
	}
	
	public void resetCircleAnim() {
		anim_state = 0;
	}
	
	private void resetMenu(Graphics g) {
		// Measure string width
		String reset = "Press R to restart";
		FontMetrics fm = g.getFontMetrics();
		int textWidth = fm.stringWidth(reset);
		//Show the text in the middle of the screen
		g.drawString(reset, Game.GAME_WIDTH/2 - textWidth/2, Game.GAME_HEIGHT/2);
	}
	
	private void startMenu(Graphics g) {
		// Measure string width
		String reset = "Press any key to start";
		FontMetrics fm = g.getFontMetrics();
		int textWidth = fm.stringWidth(reset);
		//Show the text in the middle of the screen
		g.drawString(reset, Game.GAME_WIDTH/2 - textWidth/2, Game.GAME_HEIGHT/2);
	}
	
	private void renderLives(Graphics g, int player_lives) {
		for(int i=0; i<player_lives; i++) {
			g.drawImage(heart, i*Game.TILES_SIZE, 0, Game.TILES_SIZE, Game.TILES_SIZE, null);
		}
	}
	
	private void renderCoins(Graphics g, int player_coins) {
		String text = String.valueOf(player_coins);
		g.setFont(game_font);
		g.setColor(Color.WHITE);
		
		// Measure string width
		FontMetrics fm = g.getFontMetrics();
		int textWidth = fm.stringWidth(text);

		// Set the position so that the text’s right edge stays fixed
		int rightEdgeX = Game.GAME_WIDTH - Game.TILES_SIZE - 10;
		int draw_x = rightEdgeX - textWidth;
		int draw_y = Game.TILES_SIZE - 20;
		
		g.drawString(Integer.toString(player_coins), draw_x, draw_y);
	}
	
	private void animInit() {
		reload_circle = LoadAssets.LoadSpriteImg(LoadAssets.RELOAD_CIRCLE_SPRITE);
		reload_circle_anim = new BufferedImage[ANIM_FRAMES];
		
		reload_circle_anim[0] = LoadAssets.GetSubSprite(Game.TILES_DEFAULT_SIZE, 0, Game.TILES_DEFAULT_SIZE, Game.TILES_DEFAULT_SIZE, reload_circle);
		for(int i=0; i<ANIM_FRAMES-1; i++) {
			reload_circle_anim[i+1] = LoadAssets.GetSubSprite(i*Game.TILES_DEFAULT_SIZE, Game.TILES_DEFAULT_SIZE, Game.TILES_DEFAULT_SIZE, Game.TILES_DEFAULT_SIZE, reload_circle);
		}
	}
}

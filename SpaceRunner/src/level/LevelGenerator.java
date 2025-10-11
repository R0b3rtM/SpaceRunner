package level;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import main.Game;
import utilities.LoadSprite;

public class LevelGenerator {
	
	private Random rnd;
	private BufferedImage[] level_tiles;
	private Platform head;
	private Platform tail;
	
	private static final int MAX_PLT_SIZE = 4;
	private static final int MIN_PLT_SIZE = 2;
	
	private float level_speed = 0.5f;
	private int plt_spwn_sec = 4;
	private int update_tick = 0;
	
	public LevelGenerator() {
		rnd = new Random();
		levelInit();
		
	}
	
	public void update() {
		
		// Platform generation.
		if(update_tick >= plt_spwn_sec * Game.UPS_SET) {
			update_tick = 0;
			spawnPlatform();
		}
		update_tick++;
		
		// Platform movement.
		movePlatforms();
	}

	public void render(Graphics g) {
		//drawAllTiles_debug(g);
		for(Platform curr_plt = head; curr_plt != null;curr_plt = curr_plt.getNext()) {
			renderPlatform(g, curr_plt);
		}
				
	}
	
	private void movePlatforms() {
		
		// Passed platforms remover.
		if(head != null && head.getX() <= (0 - head.getSize()*Game.TILES_SIZE)) {
			Platform tmp = head;
			head = tmp.getNext();
			tmp = null;
		}
		
		// Move every platform on the list by the level speed.
		for(Platform curr_plt = head; curr_plt != null;curr_plt = curr_plt.getNext()) {
			curr_plt.updatePosX(level_speed);
		}
		
	}
	
	private void spawnPlatform() {
		
		// Generate a platform.
		int new_size = rnd.nextInt(MAX_PLT_SIZE) + MIN_PLT_SIZE;
		Platform new_plt = new Platform(Game.GAME_WIDTH - (new_size * Game.TILES_SIZE), rnd.nextInt(Game.GAME_HEIGHT/2) + 200, new_size, level_tiles);
		
		// Add platform to a linked list.
		if(head == null) {
			head = new_plt;
			tail = new_plt;
		} else {
			tail.setNext(new_plt);
			tail = new_plt;
		}
		
	}
	
	private void levelInit() {
		BufferedImage tiles_sprite = LoadSprite.LoadSpriteImg(LoadSprite.TILES_SPRITE);
		int tiles_in_width = tiles_sprite.getWidth()/Game.TILES_DEFAULT_SIZE;
		int tiles_in_height = tiles_sprite.getHeight()/Game.TILES_DEFAULT_SIZE;
		int tiles_index = 0;
		level_tiles = new BufferedImage[tiles_in_width * tiles_in_height];
		
		for(int j=0; j<tiles_in_height; j++) {
			for(int i=0; i<tiles_in_width; i++) {
				level_tiles[tiles_index] = LoadSprite.GetSubSprite(i*Game.TILES_DEFAULT_SIZE, j*Game.TILES_DEFAULT_SIZE, tiles_sprite);
				tiles_index++;
			}
		}
	}
	
	private void renderPlatform(Graphics g, Platform plt) {
		for(int i=0; i<plt.getSize(); i++) {
			g.drawImage(plt.getPltTile(i), (int)plt.getX() + (i * Game.TILES_SIZE), (int)plt.getY(), Game.TILES_SIZE, Game.TILES_SIZE, null);
		}
	}
	
	private void drawAllTiles_debug(Graphics g) {
		for(int i=0; i<level_tiles.length; i++) {
			g.drawImage(level_tiles[i], i*Game.TILES_SIZE, 0, Game.TILES_SIZE, Game.TILES_SIZE, null);
		}
	}
}

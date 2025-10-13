package level;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import main.Game;
import utilities.LoadSprite;

public class LevelGenerator {
	
	private Random rnd;
	private BufferedImage[] level_tiles;
	private ChunkPlatform collision_plt;
	
	private ChunkPlatform head_chunk;
	private ChunkPlatform tail_chunk;
	private FloorPlatform head_floor;
	private FloorPlatform tail_floor;
	
	private static final int MAX_PLT_SIZE = 4;
	private static final int MIN_PLT_SIZE = 2;
	
	private float level_speed = 1f;
	private int floor_cnt = 0;
	private int plt_spwn_sec = 4;
	private int update_tick = 0;
	
	public LevelGenerator() {
		rnd = new Random();
		levelInit();
	}
	
	public ChunkPlatform getCollisionPlt() {
		if(collision_plt == null)
			collision_plt = head_chunk;
		
		return collision_plt;
	}
	
	public void setCollisionPlt() {
		collision_plt = collision_plt.getNext();
	}
	
	public void update() {
		
		// Platform chunks spawn.
		if(update_tick >= plt_spwn_sec * Game.UPS_SET) {
			update_tick = 0;
			spawnChunk();
		}
		update_tick++;
		
		// Platform movement.
		movePlatforms();
	}

	public void render(Graphics g) {
		//drawAllTiles_debug(g);
		
		// Render platform chunks.
		for(ChunkPlatform curr_plt = head_chunk; curr_plt != null;curr_plt = curr_plt.getNext()) {
			renderPlatform(g, curr_plt);
		}
		
		// Render floor.
		for(FloorPlatform curr_plt = head_floor; curr_plt != null;curr_plt = curr_plt.getNext()) {
			//renderPlatform(g, curr_plt);
			for(int i=0; i<curr_plt.getSize(); i++) {
				g.drawImage(curr_plt.getPltTile(i), (int)curr_plt.getX() + (i * Game.TILES_SIZE), (int)curr_plt.getY(), Game.TILES_SIZE, Game.TILES_SIZE, null);
			}
		}
	}
	
	private void movePlatforms() {
		
		// Passed platforms remover.
		if(head_chunk != null && head_chunk.getX() <= (0 - head_chunk.getSize()*Game.TILES_SIZE)) {
			ChunkPlatform tmp = head_chunk;
			head_chunk = tmp.getNext();
			tmp = null;
		}
		
		// Move every platform chunk on the list by the level speed.
		for(ChunkPlatform curr_chunk = head_chunk; curr_chunk != null;curr_chunk = curr_chunk.getNext()) {
			curr_chunk.updatePosX(level_speed);
		}
		
		if(floor_cnt<2)
			spawnFloor();
		
		// Passed floor remover.
		if(head_floor.getX() <= (0 - head_floor.getSize()*Game.TILES_SIZE)) {
			FloorPlatform tmp = head_floor;
			head_floor = tmp.getNext();
			tmp = null;
			floor_cnt--;
		}
		
		// Move every platform floor on the list by the level speed.
		for(FloorPlatform curr_floor = head_floor; curr_floor != null; curr_floor = curr_floor.getNext()) {
			curr_floor.updatePosX(level_speed);
		}
		
	}
	
	private void spawnChunk() {
		
		// Generate a platform.
		int new_size = rnd.nextInt(MAX_PLT_SIZE) + MIN_PLT_SIZE;
		int new_pos = (int)(rnd.nextInt(Game.GAME_HEIGHT/2) + 200);
		ChunkPlatform new_plt = new ChunkPlatform(Game.GAME_WIDTH - (new_size * Game.TILES_SIZE), new_pos, new_size, level_tiles);
		System.out.println(new_pos/Game.TILES_SIZE);
		// Add platform to a linked list.
		if(head_chunk == null) {
			head_chunk = new_plt;
			tail_chunk = new_plt;
		} else {
			tail_chunk.setNext(new_plt);
			tail_chunk = new_plt;
		}
		
	}
	
	private void spawnFloor() {
		
		FloorPlatform new_plt = new FloorPlatform(Game.GAME_WIDTH, Game.GAME_HEIGHT - Game.TILES_SIZE, Game.TILES_IN_WIDTH, level_tiles);
		
		// Add platform to a linked list.
		tail_floor.setNext(new_plt);
		tail_floor = new_plt;
		floor_cnt++;
	}
	
	private void levelInit() {
		BufferedImage tiles_sprite = LoadSprite.LoadSpriteImg(LoadSprite.TILES_SPRITE);
		int tiles_in_width = tiles_sprite.getWidth()/Game.TILES_DEFAULT_SIZE;
		int tiles_in_height = tiles_sprite.getHeight()/Game.TILES_DEFAULT_SIZE;
		int tiles_index = 0;
		
		level_tiles = new BufferedImage[tiles_in_width * tiles_in_height];
		
		for(int j=0; j<tiles_in_height; j++) {
			for(int i=0; i<tiles_in_width; i++) {
				level_tiles[tiles_index] = LoadSprite.GetSubSprite(i*Game.TILES_DEFAULT_SIZE, j*Game.TILES_DEFAULT_SIZE, Game.TILES_DEFAULT_SIZE, Game.TILES_DEFAULT_SIZE, tiles_sprite);
				tiles_index++;
			}
		}
		
		// First chunk spawn
		spawnChunk();
		
		// Floor spawn
		head_floor = new FloorPlatform(0, Game.GAME_HEIGHT - Game.TILES_SIZE, Game.TILES_IN_WIDTH, level_tiles);
		tail_floor = head_floor;
		floor_cnt++;

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

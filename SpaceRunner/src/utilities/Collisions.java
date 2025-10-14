package utilities;

import java.awt.geom.Rectangle2D;

import entities.Player;
import level.ChunkPlatform;
import level.LevelGenerator;
import main.Game;

public class Collisions {
	
	public static float getEntityVerticalePos(Rectangle2D.Float hit_box, float air_speed) {
		int curr_tile = (int)(hit_box.y / Game.TILES_SIZE);
		
		if(air_speed > 0) {
			//Falling
			int tile_y_pos = curr_tile * Game.TILES_SIZE;
			
			return tile_y_pos + Game.TILES_SIZE;
		}else {
			//Jumping
			return curr_tile * Game.TILES_SIZE;
		}
	}
	
	public static boolean isOnGround(Player player, ChunkPlatform plt) {
		Rectangle2D.Float hit_box = player.getHitBox();
		
		// Check if the player on the floor of the level
		if(isOnFloor(hit_box))
			return true;
		
		//Check if the player on a platform
		if (isCollide(hit_box.x, hit_box.y + hit_box.height, plt.getX(), plt.getY(), plt.getSize()))
			return true;
		
		return false;
	}
	
	public static ChunkPlatform getPlatform(Player player, LevelGenerator level_gen) {
		
		Rectangle2D.Float hit_box = player.getHitBox();
		ChunkPlatform plt = level_gen.getCollisionPlt();
		
		int curr_plt_pos = (int)(plt.getX() / Game.TILES_SIZE);
		int curr_hitbox_pos = (int)(hit_box.getX() / Game.TILES_SIZE);
		
		if(curr_hitbox_pos > curr_plt_pos + (int)(plt.getSize()) - 1) {
			level_gen.setCollisionPlt();
			plt = level_gen.getCollisionPlt();
		}
		
		return plt;

	}
	
	public static boolean isCollide(float x, float y, float plt_x, float plt_y, int size) {
		int tile_x = (int)(x / Game.TILES_SIZE);
		int tile_y = (int)(y / Game.TILES_SIZE);
		
		int plt_tile_x = (int)(plt_x / Game.TILES_SIZE);
		int plt_tile_y = (int)(plt_y / Game.TILES_SIZE);
		
		if(tile_x >= plt_tile_x && tile_x < plt_tile_x + size) {
			if(tile_y == plt_tile_y) {
				return true;
			}
		}
		
		return false;
	}
	
	// Checks if the player is on the floor of the level
	private static boolean isOnFloor(Rectangle2D.Float hit_box) {
		int bottom_y = (int)((hit_box.y + hit_box.height) / Game.TILES_SIZE);
		int level_floor = (int)((Game.GAME_HEIGHT - Game.TILES_DEFAULT_SIZE) / Game.TILES_SIZE);
		
		if(bottom_y == level_floor)
			return true;
		
		return false;
	}
	
}

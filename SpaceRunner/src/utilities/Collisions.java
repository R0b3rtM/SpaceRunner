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
			
			return tile_y_pos;
		}else {
			//Jumping
			return curr_tile * Game.TILES_SIZE;
		}
	}
	
	public static boolean isOnFloor(Rectangle2D.Float hit_box) {
		int curr_tile = (int)((hit_box.getY() + hit_box.getHeight()) / Game.TILES_SIZE);
		
		if(curr_tile == (int)((Game.GAME_HEIGHT - Game.TILES_DEFAULT_SIZE) / Game.TILES_SIZE)) {
			return true;
		}
		
		return false;
	}
	
	public static boolean isOnPlatform(Player player, LevelGenerator level_gen) {
		Rectangle2D.Float hit_box = player.getHitBox();
		ChunkPlatform plt = level_gen.getCollisionPlt();
		int curr_plt_pos = (int)(plt.getX() / Game.TILES_SIZE);
		int curr_hitbox_pos = (int)(hit_box.getX() / Game.TILES_SIZE);
		
		if(curr_hitbox_pos > curr_plt_pos + (int)(plt.getSize()) - 1) {
			level_gen.setCollisionPlt();
			plt = level_gen.getCollisionPlt();
		}
		
		if(isCollideTop(hit_box, plt))
			player.setAirSpeed();
			
		
		return isCollideBottom(hit_box, plt);
	}
	
	public static boolean isCollideTop(Rectangle2D.Float hit_box, ChunkPlatform plt) {
		
		int curr_tile_x = (int)(hit_box.getX() / Game.TILES_SIZE);
		int curr_tile_y = (int)(hit_box.getY() / Game.TILES_SIZE);
		
		int plt_tile_x = (int)(plt.getX() / Game.TILES_SIZE);
		int plt_tile_y = (int)(plt.getY() / Game.TILES_SIZE);
		int plt_size = plt.getSize();
		
		if(curr_tile_x >= plt_tile_x && curr_tile_x <= plt_tile_x + plt_size) {
			if(plt_tile_y == curr_tile_y) {
				return true;
			}
		}
		
		return false;
	}
	
	private static boolean isCollideBottom(Rectangle2D.Float hit_box, ChunkPlatform plt) {
		
		int curr_tile_x = (int)((hit_box.getX()) / Game.TILES_SIZE);
		int curr_tile_y = (int)((hit_box.getY() + hit_box.height) / Game.TILES_SIZE);
		
		int plt_tile_x = (int)(plt.getX() / Game.TILES_SIZE);
		int plt_tile_y = (int)(plt.getY() / Game.TILES_SIZE);
		int plt_size = plt.getSize();
		
		if(curr_tile_x >= plt_tile_x && curr_tile_x <= plt_tile_x + plt_size) {
			if(plt_tile_y == curr_tile_y) {
				return true;
			}
		}
		
		return false;
	}
	
}

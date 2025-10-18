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
			int tile_y_pos = (curr_tile + 1) * Game.TILES_SIZE;
			
			return tile_y_pos;
		}else {
			//Jumping
			return curr_tile * Game.TILES_SIZE;
		}
	}
	
	// Checks if the player is on the floor of the level
	public static boolean isOnFloor(Rectangle2D.Float hit_box) {
		int bottom_y = (int)((hit_box.y + hit_box.height) / Game.TILES_SIZE);
		int level_floor = (int)((Game.GAME_HEIGHT - Game.TILES_DEFAULT_SIZE) / Game.TILES_SIZE);
		
		if(bottom_y == level_floor)
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
	
	public static boolean isBottomCollision(Rectangle2D.Float playerBox, ChunkPlatform plt) {
	    float player_top = playerBox.y;
	    float player_bottom = playerBox.y + playerBox.height;
	    float player_left = playerBox.x;
	    float player_right = playerBox.x + playerBox.width;

	    float plt_top = plt.getY();
	    float plt_bottom = plt.getY() + Game.TILES_SIZE;
	    float plt_left = plt.getX();
	    float plt_right = plt.getX() + plt.getSize() * Game.TILES_SIZE;

	    // Overlapping horizontally
	    boolean horizontallyAligned = player_right > plt_left && player_left < plt_right;

	    // Player’s top near platform’s bottom
	    boolean verticallyTouching = player_top <= plt_bottom + 2 && player_bottom > plt_bottom;

	    return horizontallyAligned && verticallyTouching;
	}
	
	public static boolean isTopCollision(Rectangle2D.Float playerBox, ChunkPlatform plt) {
	    float player_top = playerBox.y;
	    float player_bottom = playerBox.y + playerBox.height;
	    float player_left = playerBox.x;
	    float player_right = playerBox.x + playerBox.width;

	    float plt_top = plt.getY();
	    float plt_bottom = plt.getY() + Game.TILES_SIZE;
	    float plt_left = plt.getX();
	    float plt_right = plt.getX() + plt.getSize() * Game.TILES_SIZE;

	    // Overlapping horizontally
	    boolean horizontallyAligned = player_right > plt_left && player_left < plt_right;

	    // Player’s bottom near platform’s top
	    boolean verticallyTouching = player_bottom >= plt_top - 2 && player_top < plt_top;

	    return horizontallyAligned && verticallyTouching;
	}
	
	public static boolean isLeftCollision(Rectangle2D.Float playerBox, ChunkPlatform plt) {
	    float player_top = playerBox.y;
	    float player_bottom = playerBox.y + playerBox.height;
	    float player_left = playerBox.x;
	    float player_right = playerBox.x + playerBox.width;

	    float plt_top = plt.getY();
	    float plt_bottom = plt.getY() + Game.TILES_SIZE;
	    float plt_left = plt.getX();
	    float plt_right = plt.getX() + plt.getSize() * Game.TILES_SIZE;

	    boolean horizontallyAligned = player_right >= plt_left && player_left < plt_left;
	    boolean verticallyAligned = player_bottom > plt_top && player_top < plt_bottom;

	    return horizontallyAligned && verticallyAligned;
	}
	
}

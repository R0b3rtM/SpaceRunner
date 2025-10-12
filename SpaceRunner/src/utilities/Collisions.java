package utilities;

import java.awt.geom.Rectangle2D;

import level.ChunkPlatform;
import level.LevelGenerator;
import main.Game;

public class Collisions {
	
	public static boolean isOnFloor(Rectangle2D.Float hit_box) {
		
		if(hit_box.getY() + hit_box.getHeight() >= Game.GAME_HEIGHT - Game.TILES_SIZE) {
			return true;
		}
		
		return false;
	}
	
	public static boolean platformCollision(Rectangle2D.Float hit_box, LevelGenerator level_gen) {
		
		ChunkPlatform plt = level_gen.getCollisionPlt();
		if(hit_box.getX() > plt.getX() + (plt.getSize() * Game.TILES_SIZE)) {
			level_gen.setCollisionPlt();
			plt = level_gen.getCollisionPlt();
		}
		
		return isCollide(hit_box, plt);
	}
	
	public static boolean isCollide(Rectangle2D.Float hit_box, ChunkPlatform plt) {
		
		float bottom_horizontal = (float) (hit_box.getX() + hit_box.getWidth());
		float bottom_verticle = (float) (hit_box.getY() + hit_box.getHeight());
		System.out.println(plt.getY()+" | "+ (plt.getY() + Game.TILES_SIZE));
		// If the HitBox is in between two points of the platform
		if(bottom_horizontal >= plt.getX() && bottom_horizontal <= plt.getX() + (plt.getSize() * Game.TILES_SIZE)) {
			// If the HitBox is in the platform height
			if(bottom_verticle >= plt.getY() && bottom_verticle <= plt.getY() + Game.TILES_SIZE) {
				return true;
			}
		}
		
		return false;
	}
}

package level;

import java.awt.image.BufferedImage;

public class Platform {
	
	protected BufferedImage[] level_plts;
	protected BufferedImage[] plt_tiles;
	
	protected int size;
	protected float x;
	protected float y;
	
	public Platform(float x, float y, int size, BufferedImage[] level_plts) {
		this.size = size;
		this.x = x;
		this.y = y;
		this.level_plts = level_plts;
		plt_tiles = new BufferedImage[size];
	}
	
	public int getSize() {
		return this.size;
	}
	
	public BufferedImage getPltTile(int i) {
		return plt_tiles[i];
	}
	
	public float getX() {
		return this.x;
	}
	
	public float getY() {
		return this.y;
	}
	
	public void updatePosX(float x) {
		this.x -= x;
	}
	
}

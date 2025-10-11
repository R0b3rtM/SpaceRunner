package level;

import java.awt.image.BufferedImage;

public class Platform {
	
	private Platform next_plt = null;
	
	private BufferedImage[] level_plts;
	private BufferedImage[] plt_tiles;
	
	private int size;
	private float x;
	private float y;
	
	public Platform(float x, float y, int size, BufferedImage[] level_plts) {
		this.size = size;
		this.x = x;
		this.y = y;
		this.level_plts = level_plts;
		plt_tiles = new BufferedImage[level_plts.length];
		
		platformInit();
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
	
	public Platform getNext() {
		return next_plt;
	}
	
	public void setNext(Platform plt) {
		next_plt = plt;
	}
	
	private void platformInit() {
		//The first and last tiles of the platform.
		plt_tiles[0] = level_plts[4];
		plt_tiles[size-1] = level_plts[5];
		
		if(size > 2) {
			for(int i=1; i<size-1; i++) {
				plt_tiles[i] = level_plts[2];
			}
		}
		
	}
}

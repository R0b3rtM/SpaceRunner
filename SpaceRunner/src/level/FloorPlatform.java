package level;

import java.awt.image.BufferedImage;

public class FloorPlatform extends Platform{
	
	private FloorPlatform next_plt = null;
	
	public FloorPlatform(float x, float y, int size, BufferedImage[] level_plts) {
		super(x, y, size, level_plts);
		
		floorInit();
	}
	
	public FloorPlatform getNext() {
		return next_plt;
	}
	
	public void setNext(FloorPlatform plt) {
		this.next_plt = plt;
	}
	
	private void floorInit() {
		for(int i=0; i<size; i++) {
			plt_tiles[i] = level_plts[1];
		}
	}
}

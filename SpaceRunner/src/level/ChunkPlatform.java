package level;

import java.awt.image.BufferedImage;

public class ChunkPlatform extends Platform{
	
	private ChunkPlatform next_plt = null;
	
	public ChunkPlatform(float x, float y, int size, BufferedImage[] level_plts) {
		super(x, y, size, level_plts);
		
		platformInit();
	}
	
	public ChunkPlatform getNext() {
		return next_plt;
	}
	
	public void setNext(ChunkPlatform plt) {
		this.next_plt = plt;
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

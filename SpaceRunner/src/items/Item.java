package items;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import utilities.LoadAssets;

import static utilities.Constants.ItemsConstants.*;

public class Item {
	
	private Item nextItem = null;
	private BufferedImage sprite;
	private int x, y, type;
	
	private int anim_speed = 10, anim_tick = 0;
	private int max_y_pos = -5, anim_pos = 0;
	private boolean up_anim = true;
	
	public Item(int x, int y, int type) {
		this.x = x;
		this.y = y;
		this.type = type;
		
		init();
	}
	
	public void setNextItem(Item nextItem) {
		this.nextItem = nextItem;
	}
	
	public Item getNextItem() {
		return nextItem;
	}
	
	public int getItemType() {
		return this.type;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public void itemPosUpdate(int x) {
		this.x -= x;
	}
	
	public void itemAnimUpdate() {
		if(anim_tick >= anim_speed) {
			anim_tick = 0;
			if(up_anim)
				anim_pos--;
			else
				anim_pos++;
			
			if(anim_pos > max_y_pos * (-1))
				up_anim = true;
			else if(anim_pos < max_y_pos)
				up_anim = false;
			
			y += anim_pos;
		}
		
		anim_tick++;
	}
	
	public void renderItem(Graphics g) {
		g.drawImage(sprite, x, y, Game.TILES_SIZE, Game.TILES_SIZE, null);
	}
	
	private void init() {
		switch(type) {
		case HEART_ID:
			// Set heart sprite
			sprite = LoadAssets.LoadSpriteImg(LoadAssets.HEART_SPRITE);
			break;
		case COIN_ID:
			// Set coin sprite
			sprite = LoadAssets.LoadSpriteImg(LoadAssets.COIN_SPRITE);
			break;
		}
	}
}

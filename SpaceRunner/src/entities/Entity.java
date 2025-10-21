package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import main.Game;
import utilities.LoadAssets;

public class Entity {
	
	protected Rectangle2D.Float hit_box;
	protected BufferedImage[][] entity;
	protected String sprite_url;
	
	protected int entity_anim = 0, anim_state = 0, anim_tick, anim_speed = 30;
	
	protected int x,y;
	protected int lives;
	
	protected boolean entity_died;
	
	public Entity(int x, int y, int lives) {
		this.x = x;
		this.y = y;
		this.lives = lives;
	}
	
	public Rectangle2D.Float getHitBox() {
		return hit_box;
	}
	
	protected void hitBoxInit(int x, int y, int width, int height) {
		hit_box = new Rectangle2D.Float(x, y, width, height);
	}
	
	protected void drawHitBox(Graphics g) {
		g.setColor(Color.RED);
		g.drawRect((int)hit_box.x, (int)hit_box.y, (int)hit_box.width, (int)hit_box.height);
	}
	
	protected void animInit(int anim_amount, int anim_frames) {
		BufferedImage entity_sprite = LoadAssets.LoadSpriteImg(sprite_url);
		
		for(int j=0; j<anim_amount; j++) {
			for(int i=0; i<anim_frames; i++) {
				entity[j][i] = LoadAssets.GetSubSprite(i * (Game.TILES_DEFAULT_SIZE * 2), j * (Game.TILES_DEFAULT_SIZE * 2),Game.TILES_DEFAULT_SIZE * 2, Game.TILES_DEFAULT_SIZE * 2, entity_sprite);
			}
		}
	}
}

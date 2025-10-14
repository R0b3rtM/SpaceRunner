package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import level.LevelGenerator;
import main.Game;

public class Entity {
	protected float x,y;
	
	protected Rectangle2D.Float hit_box;
	protected LevelGenerator level_gen;
	
	public Entity(int x, int y, LevelGenerator level_gen) {
		this.x = x;
		this.y = y;
		this.level_gen = level_gen;
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
}

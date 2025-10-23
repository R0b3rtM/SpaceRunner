package items;

import java.awt.geom.Rectangle2D;

import entities.Player;
import main.Game;

import static utilities.Constants.EnemyConstants.*;
import static utilities.Collisions.*;
import static utilities.Constants.ItemsConstants.*;

public class AlienBall extends Item{
	
	private Player player;
	
	private int gravity = 1;
	private float air_speed_x = 2.75f;
	
	private boolean destroy_ball;
	
	public AlienBall(int x, int y, Player player) {
		super(x, y, BALL_ID);
		this.player = player;
		
		air_speed_x += (DEF_Y_POS + (int)(y/Game.TILES_SIZE) + (int)((-1) * (player.getHitBox().y/Game.TILES_SIZE))) * 0.25;
	}
	
	public void updateBall() {
		x -= air_speed_x;
		y += gravity;
		
		Rectangle2D.Float ball_box = new Rectangle2D.Float(x, y, Game.TILES_SIZE/2, Game.TILES_SIZE/2);
		if(isOnFloor(ball_box)) 
			destroy_ball = true;
		if(itemCollision(player.getHitBox(), this)) {
			player.hurt();
			destroy_ball = true;
		}
	}
	
	public boolean getBallStatus() {
		return destroy_ball;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
}

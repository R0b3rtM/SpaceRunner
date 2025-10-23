package entities;

import static utilities.Constants.EnemyConstants.*;
import static utilities.Collisions.*;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import items.AlienBall;
import main.Game;
import utilities.LoadAssets;
import utilities.Shootable;
import utilities.ShootingHandler;

public class Enemy extends Entity implements Shootable{
	
	private ShootingHandler shoot_handler;
	private Player player;
	private AlienBall ball;
	
	private int draw_offset_width = (int)(13 * Game.SCALE);
	private int draw_offset_height = (int)(5 * Game.SCALE);
	
	private int default_x_pos = Game.GAME_WIDTH - Game.TILES_SIZE * DEF_X_POS;
	private int default_y_pos = Game.TILES_SIZE * DEF_Y_POS;
	private boolean reached_def_pos = false;
	
	private float verticle_fly_speed = 0.5f;
	private float horizontal_fly_speed = 1f;
	private float fall_speed = 2f;
	
	private int shoot_time = Game.UPS_SET * 5;
	
	public Enemy(int x, int y, Player player) {
		super(x, y, 1);
		
		this.player = player;
		entity = new BufferedImage[ANIM_AMOUNT][ANIM_FRAMES];
		shoot_handler = new ShootingHandler(shoot_time, this, null);
		sprite_url = LoadAssets.ALIEN_SPRITE;
		entity_anim = IDLE_ANIM;
		
		animInit(ANIM_AMOUNT, ANIM_FRAMES);
		animSet(IDLE_ANIM);
		hitBoxInit(x, y, (int)(64 * Game.SCALE), (int)(66 * Game.SCALE));
	}
	
	public void update() {
		animUpdate();
		posChange();
		enemyDeath();
		shoot_handler.shootUpdate();
		shoot_handler.tryShoot();
		System.out.println((int)((player.getHitBox().y/Game.TILES_SIZE)) - (int)(hit_box.y/Game.TILES_SIZE));
		if(ball != null) {
			ball.updateBall();
			if(ball.getBallStatus())
				ball = null;
		}
	}
	
	public void render(Graphics g) {
		// Enemy render
		g.drawImage(entity[entity_anim][anim_state], (int)hit_box.x - draw_offset_width, (int)hit_box.y - draw_offset_height, Game.TILES_SIZE * 3, Game.TILES_SIZE * 3, null);
		//drawHitBox(g);
		
		if(ball != null) {
			ball.renderItem(g);
		}
	}
	
	@Override
	public void hurt() {
		lives--;
		
		// Play hurt animation
	}
	
	@Override
	public void shoot() {
		// Spawn alien ball
		if(ball == null) {
			ball = new AlienBall((int)hit_box.x, (int)hit_box.y, player);
		}
		
	}
	
	private void enemyDeath() {
		if(!entity_died && lives <= 0) {
			animSet(DEATH_ANIM);
			entity_died = true;
		}
	}
	
	private void posChange() {
		// Adjust to default X and Y position
		if(!entity_died) {
			if(!reached_def_pos) {
				
				if(hit_box.x > default_x_pos) {
					hit_box.x -= horizontal_fly_speed;
				} else if(hit_box.x < default_x_pos){
					hit_box.x += horizontal_fly_speed;
				}
				
				if(hit_box.y < default_y_pos){
					hit_box.y += verticle_fly_speed;
				}

				if((int)(hit_box.x/Game.TILES_SIZE) == (int)(default_x_pos/Game.TILES_SIZE) )
					reached_def_pos = true;
			} else {
				// escape from player's Y position
				int player_y_pos = (int)(player.getHitBox().y / Game.TILES_SIZE);
				int enemy_bottom = (int)((hit_box.y + hit_box.height) / Game.TILES_SIZE);
				if(enemy_bottom > player_y_pos) {
					hit_box.y -= verticle_fly_speed;
				} else {
					if(enemy_bottom < (int)(default_y_pos/Game.TILES_SIZE))
						hit_box.y += verticle_fly_speed;
				}
				
			}
		} else {
			hit_box.y += fall_speed;
		}
		
	}
	
	private void animUpdate() {

		if(anim_tick >= anim_speed) {
			
			anim_tick = 0;
			
			if(entity_anim != DEATH_ANIM) {
				anim_state++;
				if(anim_state >= ANIM_FRAMES)
					anim_state = 0;
			}else {
				anim_state++;
				if(anim_state > 1)
					anim_state = 0;
			}
			
		}
		anim_tick++;
	}
	
	private void animSet(int anim) {
		
		// Avoid setting other animations while player died or reseting the same animation
		if(anim == entity_anim || entity_died)
			return;
		
		// Avoid setting other animations while in start menu
//		if(entity_anim == IDLE_ANIM)
//			return;
		
		entity_anim = anim;
		anim_state = 0;
	}
}

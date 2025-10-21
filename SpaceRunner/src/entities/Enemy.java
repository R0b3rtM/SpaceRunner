package entities;

import static utilities.Constants.EnemyConstants.*;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import utilities.LoadAssets;

public class Enemy extends Entity{
	
	private Player player;
	
	private int draw_offset_width = (int)(9 * Game.SCALE);
	private int draw_offset_height = (int)(4 * Game.SCALE);
	
	private int default_x_pos = Game.GAME_WIDTH - Game.TILES_SIZE * 6;
	private int default_y_pos = Game.TILES_SIZE * 7;
	private boolean reached_def_pos = false;
	
	private float verticle_fly_speed = 0.5f;
	private float horizontal_fly_speed = 1f;
	
	public Enemy(int x, int y, Player player) {
		super(x, y, MAX_LIVES);
		
		this.player = player;
		entity = new BufferedImage[ANIM_AMOUNT][ANIM_FRAMES];
		sprite_url = LoadAssets.ALIEN_SPRITE;
		entity_anim = IDLE_ANIM;
		
		animInit(ANIM_AMOUNT, ANIM_FRAMES);
		animSet(IDLE_ANIM);
		hitBoxInit(x, y, (int)(44 * Game.SCALE), (int)(45 * Game.SCALE));
	}
	
	public void update() {
		animUpdate();
		posChange();
	}
	
	public void render(Graphics g) {
		// Enemy render
		g.drawImage(entity[entity_anim][anim_state], (int)hit_box.x - draw_offset_width, (int)hit_box.y - draw_offset_height, Game.TILES_SIZE * 3, Game.TILES_SIZE * 3, null);
		//drawHitBox(g);
	}
	
	private void posChange() {
		// Adjust to default X and Y position
		if(!reached_def_pos) {
			if(hit_box.x > default_x_pos) {
				hit_box.x -= horizontal_fly_speed;
			} else if(hit_box.x < default_x_pos){
				hit_box.x += horizontal_fly_speed;
			}
			
			if(hit_box.y > default_y_pos) {
				hit_box.y -= verticle_fly_speed;
			} else if(hit_box.y < default_y_pos){
				hit_box.y += verticle_fly_speed;
			}
			
			if((int)(hit_box.x/Game.TILES_SIZE) == (int)(default_x_pos/Game.TILES_SIZE) && (int)(hit_box.y/Game.TILES_SIZE) == (int)(default_y_pos/Game.TILES_SIZE))
				reached_def_pos = true;
		} else {
			// escape from player's Y position
			int player_y_pos = (int)(player.getHitBox().y / Game.TILES_SIZE);
			int enemy_bottom = (int)((hit_box.y + hit_box.height) / Game.TILES_SIZE);
			if(enemy_bottom >= player_y_pos) {
				hit_box.y -= verticle_fly_speed;
			} else {
				if(enemy_bottom < (int)(default_y_pos/Game.TILES_SIZE))
					hit_box.y += verticle_fly_speed;
			}
		}
	}
	
	private void animUpdate() {

		if(anim_tick >= anim_speed) {
			anim_state++;
			anim_tick = 0;
		
			if(anim_state >= ANIM_FRAMES)
					anim_state = 0;
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

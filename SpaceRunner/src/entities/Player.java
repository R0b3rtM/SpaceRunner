package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import level.LevelGenerator;
import main.Game;
import utilities.LoadSprite;
import static utilities.Constants.PlayerConstants.*;
import static utilities.Collisions.*;

public class Player extends Entity{
	
	private BufferedImage[][] player;
	private int player_anim = RUN_ANIM, anim_state = 0, anim_tick, anim_speed = 30;
	private int draw_offset_width = (int)(16 * Game.SCALE);
		
	private boolean jump, inAir;
	private float jump_force = -4f * Game.SCALE, air_speed = 0, gravity = 0.04f;
	private float curr_plt_y, curr_plt_x;
	
	public Player(float x, float y, LevelGenerator level_gen) {
		super(x, y, level_gen);
		player = new BufferedImage[ANIM_AMOUNT][ANIM_FRAMES];
		
		hitBoxInit(x, y, (int)(30 * Game.SCALE), (int)(64 * Game.SCALE));
		anim_set();
	}
	
	public void update() {
		anim_update();
		pos_update();
	}
	
	public void render(Graphics g) {
		g.drawImage(player[player_anim][anim_state], (int)hit_box.x - draw_offset_width, (int)hit_box.y, Game.TILES_SIZE * 2, Game.TILES_SIZE * 2, null);
		drawHitBox(g);
	}
	
	public void setJump(boolean state) {
		jump = state;
	}
	
	private void pos_update() {
		
		if(!platformCollision(hit_box, level_gen)) {
			air_speed += gravity;
			inAir = true;
		}else {
			inAir = false;
			air_speed = 0;
		}
		
		if(!isOnFloor(hit_box)) {
			air_speed += gravity;
			inAir = true;
		}else {
			inAir = false;
			air_speed = 0;
		}
		
		if(jump) {
			
			jump();
		}
		
		hit_box.y += air_speed;
		
	}
	
	private void jump() {
		if(inAir)
			return;
		inAir = true;
		air_speed = jump_force;
	}
	
	private void anim_update() {
		
		if(anim_tick >= anim_speed) {
			anim_state++;
			anim_tick = 0;
			
			if(anim_state >= ANIM_FRAMES)
				anim_state = 0;
		}
		anim_tick++;
	}
	
	private void anim_set() {
		BufferedImage player_sprite = LoadSprite.LoadSpriteImg(LoadSprite.PLAYER_SPRITE);
		
		for(int j=0; j<ANIM_AMOUNT; j++) {
			for(int i=0; i<ANIM_FRAMES; i++) {
				player[j][i] = LoadSprite.GetSubSprite(i * (Game.TILES_DEFAULT_SIZE * 2), j * (Game.TILES_DEFAULT_SIZE * 2),Game.TILES_DEFAULT_SIZE * 2, Game.TILES_DEFAULT_SIZE * 2, player_sprite);
			}
		}
	}
	
	protected void drawHitBox(Graphics g) {
		g.setColor(Color.RED);
		g.drawRect((int)hit_box.x, (int)hit_box.y, (int)hit_box.width, (int)hit_box.height);
	}
}

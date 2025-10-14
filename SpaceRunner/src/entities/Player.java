package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import level.ChunkPlatform;
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
	private float jump_force = -4f * Game.SCALE, air_speed = 0, gravity = 0.1f, fall_speed = 0.1f;
	
	public Player(int x, int y, LevelGenerator level_gen) {
		super(x, y, level_gen);
		player = new BufferedImage[ANIM_AMOUNT][ANIM_FRAMES];
		
		hitBoxInit(x, y, (int)(30 * Game.SCALE), (int)(64 * Game.SCALE));
		animInit();
	}
	
	public void update() {
		animUpdate();
		posUpdate();
	}
	
	public void render(Graphics g) {
		g.drawImage(player[player_anim][anim_state], (int)hit_box.x - draw_offset_width, (int)hit_box.y, Game.TILES_SIZE * 2, Game.TILES_SIZE * 2, null);
		//drawHitBox(g);
	}
	
	public void setJump(boolean state) {
		jump = state;
	}
	
	private void posUpdate() {
		ChunkPlatform plt = getPlatform(this, level_gen);
		
		if(!isOnGround(this, plt)) {
			inAir = true;	
			animSet(JUMP_ANIM);
			
			//Check collision with left side of platform or with bottom of platform
			if(isCollide(hit_box.x, hit_box.y, plt.getX(), plt.getY(), plt.getSize()) || isCollide(plt.getY(), plt.getX() - 1, hit_box.y, hit_box.x, (int)hit_box.height)) {
				air_speed = fall_speed;
				hit_box.y = getEntityVerticalePos(hit_box, air_speed);
			}else {
				air_speed += gravity;
			}
		}
		else {
			inAir = false;
			air_speed = 0;
			animSet(RUN_ANIM);
			hit_box.y = getEntityVerticalePos(hit_box, air_speed);
		}
		
		if(jump)
			jump();
		
		hit_box.y += air_speed;
		
	}
	
	private void jump() {
		if(inAir)
			return;
		inAir = true;
		air_speed = jump_force;
	}
	
	private void animSet(int anim) {
		if(anim == player_anim)
			return;
		anim_state = 0;
		player_anim = anim;
	}
	
	private void animUpdate() {

		if(anim_tick >= anim_speed) {
			anim_state++;
			anim_tick = 0;
			
			if(anim_state >= ANIM_FRAMES && player_anim != JUMP_ANIM)
				anim_state = 0;
			else if(player_anim == JUMP_ANIM)
				anim_state = ANIM_FRAMES-1;
		}
		anim_tick++;
	}
	
	private void animInit() {
		BufferedImage player_sprite = LoadSprite.LoadSpriteImg(LoadSprite.PLAYER_SPRITE);
		
		for(int j=0; j<ANIM_AMOUNT; j++) {
			for(int i=0; i<ANIM_FRAMES; i++) {
				player[j][i] = LoadSprite.GetSubSprite(i * (Game.TILES_DEFAULT_SIZE * 2), j * (Game.TILES_DEFAULT_SIZE * 2),Game.TILES_DEFAULT_SIZE * 2, Game.TILES_DEFAULT_SIZE * 2, player_sprite);
			}
		}
	}
}

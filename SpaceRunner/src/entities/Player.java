package entities;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import level.ChunkPlatform;
import level.LevelGenerator;

import main.Game;
import utilities.HUD;
import utilities.LoadSprite;
import utilities.Shootable;
import utilities.ShootingHandler;

import static utilities.Constants.PlayerConstants.*;
import static utilities.Collisions.*;

public class Player extends Entity implements Shootable{
	
	private BufferedImage[][] player;
	private ShootingHandler shoot_handler;
	private HUD hud;
	
	private int player_anim = RUN_ANIM, anim_state = 0, anim_tick, anim_speed = 30;
	private int draw_offset_width = (int)(16 * Game.SCALE);
		
	private boolean jump, inAir;
	private float jump_force = -4f * Game.SCALE, air_speed = 0, gravity = 0.1f, fall_speed = 0.5f;
	private float laser_alpha = 0, laser_vanish_speed = 0.01f;
	
	private int shoot_time = Game.UPS_SET * 2;
	private int player_lives = 3;
	
	public Player(int x, int y, LevelGenerator level_gen) {
		super(x, y, level_gen);
		player = new BufferedImage[ANIM_AMOUNT][ANIM_FRAMES];
		shoot_handler = new ShootingHandler(shoot_time, this);
		
		hitBoxInit(x, y, (int)(30 * Game.SCALE), (int)(64 * Game.SCALE));
		animInit();
		
		hud = new HUD();
	}
	
	public void update() {
		animUpdate();
		posUpdate();
		updateLaser();
		shoot_handler.shootUpdate();
	}
	
	public void render(Graphics g) {
		// Player render
		g.drawImage(player[player_anim][anim_state], (int)hit_box.x - draw_offset_width, (int)hit_box.y, Game.TILES_SIZE * 2, Game.TILES_SIZE * 2, null);
		//drawHitBox(g);
		
		hud.renderHUD(g, player_lives);
		
		// Laser bean render
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.WHITE);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, laser_alpha));
		g2.fillRect((int)(hit_box.x + hit_box.width), (int)(hit_box.y + hit_box.height/2), Game.GAME_WIDTH, 3);
		
	}
	
	public void hurt() {
		player_lives--;
	}
	
	public void setJump(boolean state) {
		jump = state;
	}
	
	public void tryShoot() {
		shoot_handler.tryShoot();
	}
	
	@Override
	public void shoot() {
		System.out.println("Shoot!");
		laser_alpha = 1f;
	}
	
	private void updateLaser() {
		if(laser_alpha <= 0.1f) {
			laser_alpha = 0f;
		}else
			laser_alpha -= laser_vanish_speed;
	}
	
	private void posUpdate() {
		ChunkPlatform plt = getPlatform(this, level_gen);
		
		if(inAir) {
			animSet(JUMP_ANIM);
			air_speed += gravity;
		}
		
		if(jump)
			jump();
		
		hit_box.y += air_speed;
		
		if(!isOnFloor(hit_box)) {
			
			if (isTopCollision(hit_box, plt)) {
			    // Player lands on top of platform
				hit_box.y = plt.getY() - Game.TILES_SIZE * 2;
				air_speed = 0;
			    inAir = false;
			    animSet(RUN_ANIM);
			}
			else if (isBottomCollision(hit_box, plt)) {
			    // Player hits bottom of platform while jumping
				hit_box.y = plt.getY() + Game.TILES_SIZE + 1;
			    air_speed += fall_speed;
			}
			else if (isLeftCollision(hit_box, plt)) {
			    // Player collides with left side of platform
			    level_gen.levelStop();
			    air_speed += gravity;
			}
			else {
			    level_gen.levelStart();
			    inAir = true;
			}
		}
		else {
			inAir = false;
			hit_box.y = (int)(Game.GAME_HEIGHT - hit_box.height - Game.TILES_SIZE);
			if(!jump) {
				air_speed = 0;
				animSet(RUN_ANIM);
			}
		}
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

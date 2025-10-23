package entities;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import items.Item;
import level.ChunkPlatform;
import level.LevelGenerator;

import main.Game;
import utilities.Constants;
import utilities.LoadAssets;
import utilities.Shootable;
import utilities.ShootingHandler;

import static utilities.Constants.PlayerConstants.*;
import static utilities.Collisions.*;

public class Player extends Entity implements Shootable{
	
	private ShootingHandler shoot_handler;
	private Game game;
	private LevelGenerator level_gen;
		
	private int draw_offset_width = (int)(16 * Game.SCALE);
		
	private boolean jump, inAir;
	private float jump_force = -4f * Game.SCALE, air_speed = 0, gravity = 0.1f, fall_speed = 0.5f;
	private float laser_alpha = 0, laser_vanish_speed = 0.01f;
	
	private int shoot_time = Game.UPS_SET * 2;
	private int coins = 0;
	
	public Player(int x, int y, Game game) {
		super(x, y, MAX_LIVES);
		
		entity = new BufferedImage[ANIM_AMOUNT][ANIM_FRAMES];
		sprite_url = LoadAssets.PLAYER_SPRITE;
		shoot_handler = new ShootingHandler(shoot_time, this, game);
		
		this.game = game;
		this.level_gen = game.getLevelGen();
		
		animInit(ANIM_AMOUNT, ANIM_FRAMES);
		animSet(IDLE_ANIM);
		hitBoxInit((int)x, (int)y, (int)(30 * Game.SCALE), (int)(64 * Game.SCALE));
	}
	
	public void update() {
		animUpdate();
		itemsCollect();
		playerDeath();
		posUpdate();
		updateLaser();
		
		shoot_handler.shootUpdate();
	}
	
	public void render(Graphics g) {
		// Player render
		g.drawImage(entity[entity_anim][anim_state], (int)hit_box.x - draw_offset_width, (int)hit_box.y, Game.TILES_SIZE * 2, Game.TILES_SIZE * 2, null);
		//drawHitBox(g);
		// Laser bean render
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.WHITE);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, laser_alpha));
		g2.fillRect((int)(hit_box.x + hit_box.width), (int)(hit_box.y + hit_box.height/2), Game.GAME_WIDTH, 3);
		
	}
	
	public void playerReset() {
		lives = MAX_LIVES;
		entity_died = false;
		animSet(IDLE_ANIM);
	}
	
	public int getCoins() {
		return coins;
	}
	
	public void addCoin() {
		coins++;
	}
	
	public void addLives() {
		if(lives >= MAX_LIVES)
			return;
		
		lives++;
	}
	
	public boolean getCondition() {
		return entity_died;
	}
	
	public void setJump(boolean state) {
		jump = state;
	}
	
	public void tryShoot() {
		if(!entity_died)
			shoot_handler.tryShoot();
	}
	
	@Override
	public void shoot() {
		laser_alpha = 1f;
		
		Enemy curr_enemy = game.getEnemy();
		int gun_nuzzle_height = (int)(hit_box.y + hit_box.height/2);
		
		if(curr_enemy == null) return;
		if(gun_nuzzle_height <= (int)(curr_enemy.getHitBox().y + curr_enemy.getHitBox().height) && gun_nuzzle_height >= (int)(curr_enemy.getHitBox().y)) {
			curr_enemy.hurt();
		}
	}
	
	private void animUpdate() {

		if(anim_tick >= anim_speed) {
			anim_state++;
			anim_tick = 0;
			
			if(anim_state >= ANIM_FRAMES) {
				if(entity_anim == JUMP_ANIM || entity_anim == DEATH_ANIM) {
					anim_state = ANIM_FRAMES-1;
				}else {
					anim_state = 0;
				}
			}	
		}
		anim_tick++;
	}
	
	private void animSet(int anim) {
		
		// Avoid setting other animations while player died or reseting the same animation
		if(anim == entity_anim || entity_died)
			return;
		
		// Avoid setting other animations while in start menu
		if(entity_anim == IDLE_ANIM && level_gen.getPause())
			return;
		
		entity_anim = anim;
		anim_state = 0;
	}
	
	private void playerDeath() {
		if(entity_died) return;
		if(lives <= 0) {
			// Pause the game
			level_gen.setPause(true);
			animSet(DEATH_ANIM);
			entity_died = true;
		}
	}
	
	private void updateLaser() {
		if(laser_alpha <= 0.1f) {
			laser_alpha = 0f;
		}else
			laser_alpha -= laser_vanish_speed;
	}
	
	private void itemsCollect() {
		Item curr_item = level_gen.getHeadItem();
		if(curr_item == null) return;
		if(itemCollision(hit_box, curr_item)) {
			switch (curr_item.getItemType()) {
			case Constants.ItemsConstants.HEART_ID:
				addLives();
				break;
			case Constants.ItemsConstants.COIN_ID:
				addCoin();
				break;
			}
			level_gen.collectHeadItem();
		}
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
		if(inAir || entity_died)
			return;
		inAir = true;
		air_speed = jump_force;
	}
	

}

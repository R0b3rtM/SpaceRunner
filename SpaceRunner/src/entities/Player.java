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
import utilities.HUD;
import utilities.LoadSprite;
import utilities.Shootable;
import utilities.ShootingHandler;

import static utilities.Constants.PlayerConstants.*;
import static utilities.Collisions.*;

public class Player extends Entity implements Shootable{
	
	private BufferedImage[][] player;
	private ShootingHandler shoot_handler;
	
	private int player_anim = IDLE_ANIM, anim_state = 0, anim_tick, anim_speed = 30;
	private int draw_offset_width = (int)(16 * Game.SCALE);
		
	private boolean jump, inAir;
	private float jump_force = -4f * Game.SCALE, air_speed = 0, gravity = 0.1f, fall_speed = 0.5f;
	private float laser_alpha = 0, laser_vanish_speed = 0.01f;
	
	private int shoot_time = Game.UPS_SET * 2;
	private int lives = 2, coins = 0;
	private boolean player_died;
	
	public Player(int x, int y, LevelGenerator level_gen) {
		super(x, y, level_gen);
		player = new BufferedImage[ANIM_AMOUNT][ANIM_FRAMES];
		shoot_handler = new ShootingHandler(shoot_time, this);
		
		hitBoxInit((int)x, (int)y, (int)(30 * Game.SCALE), (int)(64 * Game.SCALE));
		animInit();
	}
	
	public void update() {
		animUpdate();
		itemsCollect();
		playerDeath();
		posUpdate();
		
		// Player logic and HUD set
		if(!player_died) {
			if(!level_gen.getPause()) {
				updateLaser();
				shoot_handler.shootUpdate();
			}
		}
	}
	
	public void render(Graphics g) {
		// Player render
		g.drawImage(player[player_anim][anim_state], (int)hit_box.x - draw_offset_width, (int)hit_box.y, Game.TILES_SIZE * 2, Game.TILES_SIZE * 2, null);
		//drawHitBox(g);
		// Laser bean render
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.WHITE);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, laser_alpha));
		g2.fillRect((int)(hit_box.x + hit_box.width), (int)(hit_box.y + hit_box.height/2), Game.GAME_WIDTH, 3);
		
	}
	
	public void playerReset() {
		lives = 3;
		player_died = false;
		animSet(IDLE_ANIM);
	}
	
	public int getCoins() {
		return coins;
	}
	
	public int getLives() {
		return lives;
	}
	
	public void hurt() {
		lives--;
	}
	
	public void addCoin() {
		coins++;
	}
	
	public void addLives() {
		if(lives == 3)
			return;
		
		lives++;
	}
	
	public boolean getCondition() {
		return player_died;
	}
	
	public void setJump(boolean state) {
		jump = state;
	}
	
	public void tryShoot() {
		if(!player_died)
			shoot_handler.tryShoot();
	}
	
	@Override
	public void shoot() {
		System.out.println("Shoot!");
		laser_alpha = 1f;
	}
	
	private void playerDeath() {
		if(player_died) return;
		if(lives <= 0) {
			// Pause the game
			level_gen.setPause(true);
			animSet(DEATH_ANIM);
			player_died = true;
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
		if(inAir || player_died)
			return;
		inAir = true;
		air_speed = jump_force;
	}
	
	private void animSet(int anim) {
		
		// Avoid setting other animations while player died or reseting the same animation
		if(anim == player_anim || player_died)
			return;
		
		// Avoid setting other animations while in start menu
		if(player_anim == IDLE_ANIM && level_gen.getPause())
			return;
		
		player_anim = anim;
		anim_state = 0;
	}
	
	private void animUpdate() {

		if(anim_tick >= anim_speed) {
			anim_state++;
			anim_tick = 0;
			
			if(anim_state >= ANIM_FRAMES) {
				if(player_anim == JUMP_ANIM || player_anim == DEATH_ANIM) {
					anim_state = ANIM_FRAMES-1;
				}else {
					anim_state = 0;
				}
			}	
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

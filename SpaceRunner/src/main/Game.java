package main;

import java.awt.Color;
import java.awt.Graphics;

import entities.Player;
import level.LevelGenerator;
import utilities.HUD;

import static utilities.Constants.HUDConstants.*;

public class Game implements Runnable{
	
	private GameWindow game_window;
	private GamePanel game_panel;
	private LevelGenerator level_gen;
	private Player player;
	private HUD hud;
	
	public static final int FPS_SET = 120;
	public static final int UPS_SET = 200;
	
	private Thread game_thread;
	
	public final static int TILES_DEFAULT_SIZE = 32;
	public final static float SCALE = 2f;
	public final static int TILES_IN_WIDTH = 26;
	public final static int TILES_IN_HEIGHT = 14;
	public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
	public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
	public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;
	public final static Color GAME_BACKGROUND = new Color(38, 35, 66);
	
	public final static int MENU_STATE = 0;
	public final static int PLAY_STATE = 1;
	public final static int DEATH_STATE = 2;
	private int game_state = MENU_STATE;
	
	public Game() {
		level_gen = new LevelGenerator();
		player = new Player(200, 200, level_gen);
		game_panel = new GamePanel(this);
		game_window =  new GameWindow(game_panel);
		
		game_panel.requestFocus();
		
		game_panel.setBackground(GAME_BACKGROUND);
		start_game();
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void render(Graphics g) {
		level_gen.render(g);
		
		if(hud != null) {
			setGameHUD();
			hud.renderHUD(g, player.getLives(), player.getCoins());
		}
		
		player.render(g);
	}
	
	public void setPause(boolean state) {
		level_gen.setPause(state);
	}
	
	public int getGameState() {
		return game_state;
	}
	
	public void restartGame() {
		level_gen.levelReset();
		player.playerReset();
	}
	
	private void update() {
		level_gen.update();
		player.update();
	}
	
	private void start_game() {
		hud = new HUD();
		
		game_thread = new Thread(this);
		game_thread.start();
	}
	
	private void setGameHUD() {
		if(!player.getCondition()) {
			if(level_gen.getPause()) {
				hud.setHUD(MAIN_HUD);
				game_state = MENU_STATE;
			}else {
				hud.setHUD(PLAY_HUD);
				game_state = PLAY_STATE;
			}
		}else {
			hud.setHUD(DEATH_HUD);
			game_state = DEATH_STATE;
		}
	}

	@Override
	public void run() {
		double time_per_frame = 1000000000.0 / FPS_SET;
		double time_per_update = 1000000000.0 / UPS_SET;
		
		long last_time_check = System.nanoTime();
		long curr_time = System.nanoTime();
		
		double deltaU = 0;
		double deltaF = 0;
		int frames = 0;
		int updates = 0;
		
		long last_check = System.currentTimeMillis();
		
		while(true) {
			curr_time = System.nanoTime();
			
			deltaU += (curr_time - last_time_check) / time_per_update;
			deltaF += (curr_time - last_time_check) / time_per_frame;
			last_time_check = curr_time;
			
			// One update cycle
			if(deltaU >= 1) {
				update();
				updates++;
				deltaU--;
			}
			
			// One frame cycle
			if(deltaF >= 1) {
				game_panel.repaint();
				frames++;
				deltaF--;
			}
			
			// One second past
			if(System.currentTimeMillis() - last_check >= 1000) {
				last_check = System.currentTimeMillis();
				System.out.println("FPS: " + frames + " | " + " UPS: " + updates);
				frames = 0;
				updates = 0;
			}
		}
		
	}

}

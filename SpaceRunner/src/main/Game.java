package main;

import java.awt.Graphics;

import level.LevelGenerator;

public class Game implements Runnable{
	
	private GameWindow game_window;
	private GamePanel game_panel;
	private LevelGenerator level_gen;
	
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
	
	public Game() {
		level_gen = new LevelGenerator();
		game_panel = new GamePanel(this);
		game_window =  new GameWindow(game_panel);
		
		start_game();
	}
	
	private void update() {
		level_gen.update();
	}
	
	public void render(Graphics g) {
		level_gen.render(g);
	}
	
	private void start_game() {
		game_thread = new Thread(this);
		game_thread.start();
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

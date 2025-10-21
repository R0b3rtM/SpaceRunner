package level;

import java.util.Random;

import entities.Enemy;
import entities.Player;
import main.Game;

public class EnemyGenerator {
	
	private Game game;
	private Player player;
	private Random rnd = new Random();
	private Enemy curr_enemy;
	
	private int min_spawn_sec = 5;
	private int spawn_index, spawn_tick = 0;
	
	public EnemyGenerator(Game game) {
		this.game = game;
		this.player = game.getPlayer();
		setRandomSpawnSec();
	}
	
	public void update() {
		
		if(spawn_tick >= spawn_index) {
			spawn_tick = 0;
			setRandomSpawnSec();
			spawnEnemy();
			System.out.println("Enemy spawned");
		}
		
		// Start spawn time if no enemy exists
		if(curr_enemy == null && game.getGameState() != Game.MENU_STATE && game.getGameState() != Game.DEATH_STATE)
			spawn_tick++;
	}
	
	public Enemy getEnemy() {
		return curr_enemy;
	}
	
	public void enemyReset() {
		curr_enemy = null;
	}
	
	private void setRandomSpawnSec() {
		this.spawn_index = (rnd.nextInt(min_spawn_sec) + min_spawn_sec) * Game.UPS_SET;
	}
	
	private void spawnEnemy() {
		curr_enemy = new Enemy(Game.GAME_WIDTH, 0, player);
	}
}

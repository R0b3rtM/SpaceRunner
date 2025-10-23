package utilities;

import main.Game;

public class ShootingHandler {
	
	private Game game;
	private int shoot_tick = 0, shoot_time;
	private boolean shoot_allow = false;
	private Shootable owner;
	
	public ShootingHandler(int shoot_time, Shootable owner, Game game) {
		this.shoot_time = shoot_time;
		this.owner = owner;
		this.game = game;
	}
	
	public void shootUpdate() {
		if(shoot_tick >= shoot_time) {
			shoot_tick = 0;
			// Shooting allowed
			shoot_allow = true;
		}
		if(shoot_allow == false) {
			shoot_tick++;
		}
		if(game != null)
			game.getHUD().reloadCircleAnim(shoot_time);
		
	}
	
	public void tryShoot() {
		if(shoot_allow) { 
			shoot_allow = false;
			if(game != null)
				game.getHUD().resetCircleAnim();
			owner.shoot();
		}
	}
	
}

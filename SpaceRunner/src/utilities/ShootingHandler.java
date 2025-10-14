package utilities;

public class ShootingHandler {
	
	private int shoot_tick = 0, shoot_time;
	private boolean shoot_allow = false;
	private Shootable owner;
	
	public ShootingHandler(int shoot_time, Shootable owner) {
		this.shoot_time = shoot_time;
		this.owner = owner;
	}
	
	public void shootUpdate() {
		if(shoot_tick >= shoot_time) {
			shoot_tick = 0;
			// Shooting allowed
			shoot_allow = true;
		}
		if(shoot_allow == false) shoot_tick++;
	}
	
	public void tryShoot() {
		if(shoot_allow) { 
			shoot_allow = false;
			owner.shoot();
		}
	}
	
}

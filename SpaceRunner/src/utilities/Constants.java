package utilities;

public class Constants {
	public static class PlayerConstants {
		public static final int IDLE_ANIM = 0;
		public static final int RUN_ANIM = 1;
		public static final int JUMP_ANIM = 2;
		public static final int DEATH_ANIM = 3;
		
		public static final int ANIM_AMOUNT = 4;
		public static final int ANIM_FRAMES = 3;
		
		public static final int MAX_LIVES = 3;
	}
	
	public static class EnemyConstants {
		public static final int ATTACK_ANIM = 0;
		public static final int IDLE_ANIM = 1;
		public static final int DEATH_ANIM = 2;
		
		public static final int ANIM_AMOUNT = 3;
		public static final int ANIM_FRAMES = 4;
		
		public static final int MAX_LIVES = 5;
		public static final int DEF_Y_POS = 7;
		public static final int DEF_X_POS = 7;
	}
	
	public static class ItemsConstants {
		public static final int HEART_ID = 0;
		public static final int COIN_ID = 1;
		public static final int BALL_ID = 2;
	}
	
	public static class HUDConstants {
		public static final int MAIN_HUD = 0;
		public static final int PLAY_HUD = 1;
		public static final int DEATH_HUD = 2;
	}
}

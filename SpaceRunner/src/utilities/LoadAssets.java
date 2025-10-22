package utilities;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import main.Game;

public class LoadAssets {
	
	public static final String PLAYER_SPRITE = "Player.png";
	public static final String ALIEN_SPRITE = "Alien.png";
	public static final String TILES_SPRITE = "Tiles.png";
	public static final String HEART_SPRITE = "Heart.png";
	public static final String COIN_SPRITE = "Coin.png";
	public static final String BALL_SPRITE = "AlienBall.png";
	
	public static BufferedImage LoadSpriteImg(String sprite_url) {
		
		BufferedImage sprite = null;
		InputStream is = LoadAssets.class.getResourceAsStream("/" + sprite_url);
		
		try {
			sprite = ImageIO.read(is);
		} catch(IOException e) {
			System.out.println(e.getMessage());
		}
		
		return sprite;
	}
	
	public static BufferedImage GetSubSprite(int i, int j, int width, int height, BufferedImage sprite) {
		BufferedImage sub_sprite = sprite.getSubimage(i, j, width, height);
		return sub_sprite;
	}
	
	public static Font LoadFont() {
		
		try (InputStream is = LoadAssets.class.getResourceAsStream("/MedodicaRegular.otf")){
			if(is == null) {
				System.out.println("Failed to find font file.");
			}
			Font game_font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(48f);
			return game_font;
			
		} catch (IOException | FontFormatException e) {
			System.out.println(e.getMessage());
			return null;
		}
		
	}
}

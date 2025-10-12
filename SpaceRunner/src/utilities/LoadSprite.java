package utilities;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import main.Game;

public class LoadSprite {
	
	public static final String PLAYER_SPRITE = "Player.png";
	public static final String TILES_SPRITE = "Tiles.png";
	
	public static BufferedImage LoadSpriteImg(String sprite_url) {
		
		BufferedImage sprite = null;
		InputStream is = LoadSprite.class.getResourceAsStream("/" + sprite_url);
		
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
}

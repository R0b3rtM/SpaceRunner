package items;

import java.awt.Graphics;
import java.util.Random;

import main.Game;

public class ItemsGenerator {
	
	private Item head, tail;
	private int item_spawn_time = Game.UPS_SET * 5, spawn_tick = 0;
	
	private int max_y_pos = 13, min_y_pos = 10;
	private Random rnd = new Random();
	
	public ItemsGenerator() {
		this.head = null;
		this.tail = null;
	}
	
	public void removeHead() {
		Item temp = head;
		head = head.getNextItem();
		temp = null;
	}
	
	public Item getHead() {
		return head;
	}
	
	public void spawnItems() {
		if(spawn_tick >= item_spawn_time) {
			Item new_item = new Item(Game.GAME_WIDTH - Game.TILES_SIZE*2, (rnd.nextInt(max_y_pos - min_y_pos) + min_y_pos) * Game.TILES_SIZE, rnd.nextInt(2));
			if(head == null) {
				head = new_item;
				tail = head;
			}
			else {
				tail.setNextItem(new_item);
				tail = new_item;
			}
			spawn_tick = 0;	
		}
		spawn_tick++;
	}
	
	public void itemsMove(int level_speed) {
		if(head != null) {
			Item curr_item = head;
			if(curr_item.getX() < 0) {
				removeHead();
				curr_item = head;
			}
			for(;curr_item != null ; curr_item = curr_item.getNextItem()) {
				curr_item.itemPosUpdate(level_speed);
			}
		}
	}
	
	public void itemsAnim() {
		if(head != null) {
			Item curr_item = head;
			for(;curr_item != null ; curr_item = curr_item.getNextItem()) {
				curr_item.itemAnimUpdate();
			}
		}
	}
	
	public void itemsRender(Graphics g) {
		if(head != null) {
			Item curr_item = head;
			for(;curr_item != null ; curr_item = curr_item.getNextItem()) {
				curr_item.renderItem(g);
			}
		}
	}

	public void itemsRest() {
		if(head != null) {
			//Delete all the chunk platforms
			while(head != null) {
				Item tmp = head.getNextItem();
				head = null;
				head = tmp;	
			}
		}
	}
}

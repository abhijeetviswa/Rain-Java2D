package org.me.rain_2d.level;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import org.me.rain_2d.entity.mob.NPC;
import org.me.rain_2d.graphics.Screen;
import org.me.rain_2d.graphics.textures.Texture;

public class MapNpc extends NPC
{
	int x, y;
	int moveTimer;
	int health;
	boolean shouldRenderHealth;
	long tmr1000;
	
	public MapNpc(int x, int y, Texture sheet, Level level)
	{
		super(sheet);
		this.x = x;
		this.y = y;
		this.level = level;
	}

	public void update(long tick)
	{
		moveTimer++;
		if (moveTimer % 13 == 0) {
			int i;
			Random random = new Random();
			i = random.nextInt(4);
			if (i == 0) {
				move(-1, 0);
			} else if (i == 1) {
				move(0, -1);
			} else if (i == 2) {
				move(1, 0);
			} else if (i == 3) {
				move(1, 1);
			}
		} else {
			move(0, 0);
		}
		if (tick > tmr1000){
			shouldRenderHealth = false;
		}
	}

	public void render(Screen screen)
	{
		// Render the sprite
		super.render(screen);
		
		// Create a buffered image with black as background
		BufferedImage img = new BufferedImage(15, 5, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) img.getGraphics();
		g.fillRect(0, 0, 15, 5);
		g.dispose();
	}

	public int getHealth()
	{
		return health;
	}

	public void setHealth(int val)
	{
		health = val;
		shouldRenderHealth = true;
		tmr1000 = System.currentTimeMillis();
	}
	
	public void stopRenderingHealth(){
		shouldRenderHealth = false;
	}

}

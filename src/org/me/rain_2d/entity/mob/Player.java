package org.me.rain_2d.entity.mob;

import org.me.rain_2d.graphics.textures.Screen;
import org.me.rain_2d.graphics.textures.Texture;
import org.me.rain_2d.input.Keyboard;
import org.me.rain_2d.level.Level;

public class Player extends Mob
{
	private Keyboard input;

	public Player(Keyboard input, Texture sheet, Level level)
	{
		this.input = input;
		this.sheet = sheet;
		this.level = level;
	}

	public Player(int x, int y, Keyboard input, Level level)
	{
		this.x = x;
		this.y = y;
		this.input = input;
		this.level = level;
	}

	public void update()
	{
		int xa = 0, ya = 0;
		if (!moving) {
			if (input.up) ya++;
			if (input.left) xa--;
			if (input.down) ya--;
			if (input.right) xa++;
			if (xa != 0 || ya != 0) {
				if (ya < 0) dir = 0;
				if (xa < 0) dir = 1;
				if (xa > 0) dir = 2;
				if (ya > 0) dir = 3;
			}
		}
		move(xa, ya);
	}

	public void render(Screen screen)
	{
		screen.renderMob(x, y, xOffset, yOffset, getSprite());
	}
	


}

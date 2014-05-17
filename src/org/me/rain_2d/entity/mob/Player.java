package org.me.rain_2d.entity.mob;

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
		float speed = 1.0f; // Default speed 
		if (input.shift) speed = 2.0f;
		if (!moving)
		{
			if (input.up) ya++;
			if (input.left) xa--;
			if (input.down) ya--;
			if (input.right) xa++;
			if (xa != 0 || ya != 0)
			{
				if (ya < 0) dir = 0;
				if (xa < 0) dir = 1;
				if (xa > 0) dir = 2;
				if (ya > 0) dir = 3;
			}
			// Attack another character
			if (attacking)
			{
				attacking = false;
				step = 0;
			}

			if (input.ctrl)
			{
				attacking = true;
				step = 1;
			}
		}
		move(xa, ya, speed);
	}

}

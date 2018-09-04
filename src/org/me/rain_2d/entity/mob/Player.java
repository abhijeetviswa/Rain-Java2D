package org.me.rain_2d.entity.mob;

import org.me.rain_2d.graphics.textures.Texture;
import org.me.rain_2d.input.Keyboard;
import org.me.rain_2d.input.Mouse;
import org.me.rain_2d.level.Level;

public class Player extends Mob
{
	private Keyboard keyboard;
	private Mouse mouse;

	private boolean rightMouseDown = false;
	
	public Player(Keyboard keyboard, Mouse mouse, Texture sheet, Level level)
	{
		this.keyboard = keyboard;
		this.mouse = mouse;
		this.sheet = sheet;
		this.level = level;
	}

	public Player(int x, int y, Keyboard keyboard, Mouse mouse, Level level)
	{
		this.x = x;
		this.y = y;
		this.keyboard = keyboard;
		this.mouse = mouse;
		this.level = level;
	}

	public void update()
	{
		int xa = 0, ya = 0;
		float speed = 1f; // Default speed 
		if (keyboard.shift && this.rightMouseDown){
			this.x = mouse.getTileX();
			this.y = mouse.getTileY();
		}else if(keyboard.shift){
			speed = 4.0f;
		}
		if (!moving)
		{
			if (keyboard.up) ya++;
			if (keyboard.left) xa--;
			if (keyboard.down) ya--;
			if (keyboard.right) xa++;
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

			if (keyboard.ctrl)
			{
				attacking = true;
				step = 1;
			}
		}
		move(xa, ya, speed);
		this.rightMouseDown = false;
		mouse.right = false;
	}
	
	public void setMouseRightDown(boolean val){
		this.rightMouseDown = val;
	}
}

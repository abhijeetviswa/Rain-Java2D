package org.me.rain_2d.entity.mob;

import org.me.rain_2d.entity.Entity;
import org.me.rain_2d.graphics.textures.Sprite;
import org.me.rain_2d.graphics.textures.Texture;
import org.me.rain_2d.level.Level;

public abstract class Mob extends Entity
{

	protected Texture sheet;
	protected int dir = 0; // 0 = South, 1 = West, 2= East, 3 = North
	protected boolean moving;

	protected int step = 0; // 0 = right, 1 = left

	public int xOffset, yOffset;

	protected Level level;

	public void mob(Texture sheet)
	{
		this.sheet = sheet;
	}

	public void move(int xa, int ya)
	{
		if (moving) {
			if (dir == 0) {
				yOffset += 3;
				if (yOffset >= 32) {
					y++;
					yOffset = 0;
					xOffset = 0;
					moving = false;
				}
			} else if (dir == 1) {
				xOffset -= 3;
				if (xOffset <= -32) {
					x--;
					yOffset = 0;
					xOffset = 0;
					moving = false;
				}
			} else if (dir == 2) {
				xOffset += 3;
				if (xOffset >= 32) {
					x++;
					yOffset = 0;
					xOffset = 0;
					moving = false;
				}
			} else if (dir == 3) {
				yOffset -= 3;
				if (yOffset <= -32) {
					y--;
					yOffset = 0;
					xOffset = 0;
					moving = false;
				}
			}

			if (xOffset == 0 && yOffset == 0) {
				changeAnim();
			}
		} else {
			if (ya == -1) { // Moving down
				if (collision(x, y + 1)) return;
				moving = true;
				dir = 0;

			}

			if (xa == -1) {
				if (collision(x - 1, y)) return;
				moving = true;
				dir = 1;
			}

			if (xa == 1) {
				if (collision(x + 1, y)) return;
				moving = true;
				dir = 2;
			}

			if (ya == 1) { // Moving Up
				if (collision(x, y - 1)) return;
				moving = true;
				dir = 3;
			}
		}
	}

	public void update()
	{

	}

	public void render()
	{

	}

	private boolean collision(int x, int y)
	{
		if (!(x > level.getWidth() || x < 0)) {
			if (!(y > level.getHeight() || y < 0)) {
				return false;
			}
		}
		return true;
	}

	public void setLevel(Level level)
	{

	}

	protected Sprite getSprite()
	{
		// return new Sprite(32, anim, dir, sheet);
		if (moving) {
			if (step == 0) return new Sprite(32, 0, dir, sheet);
			if (step == 1) return new Sprite(32, 2, dir, sheet);
		}
		return new Sprite(32, 1, dir, sheet);

	}

	protected void changeAnim()
	{
		if (step == 0) {
			step = 1;
		} else {
			step = 0;
		}
	}
}

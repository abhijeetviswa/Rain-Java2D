package org.me.rain_2d.entity.mob;

import org.me.rain_2d.entity.Entity;
import org.me.rain_2d.graphics.Screen;
import org.me.rain_2d.graphics.textures.Sprite;
import org.me.rain_2d.graphics.textures.Texture;
import org.me.rain_2d.level.Level;
import org.me.rain_2d.level.Tile;

public abstract class Mob extends Entity
{

	protected Texture sheet;
	protected int dir = 0; // 0 = South, 1 = West, 2= East, 3 = North
	protected boolean moving;
	protected boolean attacking;

	protected int step = 0; // 0 = right, 1 = left

	public float xOffset, yOffset;

	protected Level level;

	public void mob(Texture sheet)
	{
		this.sheet = sheet;
	}

	public void move(int xa, int ya)
	{
		this.move(xa, ya, 1.0f);
	}

	public void move(int xa, int ya, float speed)
	{
		if (moving)
		{
			if (dir == 0)
			{ // Down
				yOffset -= 1 * speed;
				// if (yOffset <= 0) return;
				if (yOffset <= -32)
				{
					y++;
					yOffset = 0;
					xOffset = 0;
					moving = false;
				}
			} else if (dir == 1)
			{ // Left
				xOffset += 1 * speed;
				if (xOffset >= 32)
				{
					x--;
					yOffset = 0;
					xOffset = 0;
					moving = false;
				}
			} else if (dir == 2)
			{ // Right
				xOffset -= 1 * speed;
				if (xOffset <= -32)
				{
					x++;
					yOffset = 0;
					xOffset = 0;
					moving = false;
				}
			} else if (dir == 3)
			{ // Up
				yOffset += 1 * speed;
				if (yOffset >= 32)
				{
					y--;
					yOffset = 0;
					xOffset = 0;
					moving = false;
				}
			}

			if (xOffset == 0 && yOffset == 0)
			{
				changeAnim();
			}
		} else
		{
			if (ya == -1)
			{ // Moving down
				if (collision(x, y + 1) || isOutOfBounds(x, y + 1)) return;

				moving = true;
				dir = 0;
				yOffset = 0 * -1;
				//yOffset = 0;
			}

			if (xa == -1)
			{ // Left
				if (collision(x - 1, y) || isOutOfBounds(x - 1, y)) { return; }
				moving = true;
				dir = 1;
				// xOffset = 32;
				xOffset = 0;
			}

			if (xa == 1)
			{ // Right
				if (collision(x + 1, y) || isOutOfBounds(x + 1, y)) { return; }
				moving = true;
				dir = 2;
				xOffset = 0;
				//xOffset = 0;
			}

			if (ya == 1)
			{ // Moving Up
				if (collision(x, y - 1) || isOutOfBounds(x, y - 1)) { return; }
				moving = true;
				dir = 3;
				// yOffset = 32;
				yOffset = 0;
			}
		}
	}

	public void update()
	{

	}

	public void render(Screen screen)
	{
		render(screen, false);
	}

	public void render(Screen screen, boolean renderHealth)
	{
		screen.renderMob(x, y, xOffset, yOffset, getSprite());
		if (renderHealth)
		{
			// screen.renderHealth(x, y, xOffset, yOffset);
		}
	}

	private boolean collision(int x, int y)
	{
		// Checks if tile is solid
		if (!isOutOfBounds(x, y))
		{
			Tile t = level.getLayer(0).getTile(x, y);
			if (t == null)
			{
				return true;
			} else
			{
				return t.isCollidable();
			}
		}
		return true;
	}

	private boolean isOutOfBounds(int x, int y)
	{
		// Checks if we are out-of-bounds
		if (!(x > level.getWidth() - 1 || x < 0))
		{
			if (!(y > level.getHeight() - 1 || y < 0)) { return false; }
		}
		return true;
	}

	public void setLevel(Level level)
	{

	}

	protected Sprite getSprite()
	{
		// return new Sprite(32, anim, dir, sheet);
		if (step == 0) { return new Sprite(32, 0, dir, sheet); }
		if (step == 1) { return new Sprite(32, 2, dir, sheet); }
		return new Sprite(32, 1, dir, sheet);

	}

	protected void changeAnim()
	{
		if (step == 0)
		{
			step = 1;
		} else
		{
			step = 0;
		}
	}
}

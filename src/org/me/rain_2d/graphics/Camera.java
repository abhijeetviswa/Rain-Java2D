package org.me.rain_2d.graphics;

import java.awt.Rectangle;
import org.me.rain_2d.Game;

public class Camera
{

	float x = 0, y = 0;

	private Rectangle TileView = new Rectangle();
	// private Rectangle2D.Float Camera = new Rectangle2D.Float();

	int width = 0, height = 0;

	/**
	 * The first tile to be rendered on the screen horizontally
	 */
	private final int START_VALUE_X;
	/**
	 * The first tile to be rendered on the screen vertically
	 */
	private final int START_VALUE_Y;

	/**
	 * The last tile to be rendered on the screen horizontally
	 */
	private final int END_VALUE_X;
	/**
	 * The last tile to be rendered on the screen vertically
	 */
	private final int END_VALUE_Y;

	/**
	 * The number of tiles rendered horizontally
	 */
	private final int MAP_MAX_X;
	/**
	 * The number of tiles rendered vertically
	 */
	private final int MAP_MAX_Y;

	public Camera(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		MAP_MAX_X = width / 32;
		MAP_MAX_Y = height / 32;

		START_VALUE_X = (MAP_MAX_X + 1) / 2;
		START_VALUE_Y = (MAP_MAX_Y + 1) / 2;

		END_VALUE_X = (MAP_MAX_X + 1) + 1;
		END_VALUE_Y = (MAP_MAX_Y + 1) + 1;
	}

	public void updateCamera(int x, int y, float xOffset, float yOffset, int mapMaxX, int mapMaxY)
	{
		int tx, ty, tw, th, txo, tyo;
		// mapMaxX--;
		// mapMaxY--;
		txo = (int) xOffset;
		tyo = (int) yOffset;

		tx = x - (((Game.winWidth / 32) + 1) / 2);
		ty = y - (((Game.winHeight / 32) + 1) / 2);

		if (tx < 0)
		{
			txo = 0;
			if (tx == -1)
			{
				if (xOffset > 0)
				{
					txo = (int) xOffset;
				}
			}
			tx = 0;
		}

		if (ty < 0)
		{
			tyo = 0;
			if (ty == -1)
			{
				if (yOffset > 0)
				{
					tyo = (int) yOffset;
				}
			}
			ty = 0;
		}
		if (!(mapMaxX == (Game.winWidth / 32)))
		{
			if (xOffset > 0) // Going to left or right ??
			{
				tw = tx + ((Game.winWidth / 32) + 1) + 1;
			} else
			{
				tw = tx + ((Game.winWidth / 32) + 1);
			}
		} else
		{
			tw = tx + ((Game.winWidth / 32) + 1);
		}

		if (!(mapMaxY == (Game.winHeight / 32)))
		{
			if (yOffset > 0)
			{
				th = ty + ((Game.winHeight / 32) + 1) + 1;
			} else
			{
				th = ty + ((Game.winHeight / 32) + 1);
			}
		} else
		{
			th = ty + ((Game.winHeight / 32) + 1);
		}

		if (tw > mapMaxX)
		{
			tw = mapMaxX;
			txo = 0;
			if (mapMaxX == (Game.winWidth / 32))
			{
				// We can view entire map in the screen
				tx = 0;
			} else
			{
				tx = tw - (Game.winWidth / 32);
			}
		}

		if (ty > mapMaxY)
		{
			ty = mapMaxY;
			tyo = 0;
			if (mapMaxY == (Game.winHeight / 32))
			{
				tw = 0;
			} else
			{
				ty = ty - (Game.winWidth / 32);
			}
		}

		// if (tw - 1 >= mapMaxX)
		// {
		// //if (txo < 0) txo = 0; // If we are moving towards the right
		// tw = mapMaxX;
		//
		// if (mapMaxX == (Game.winWidth / 32))
		// {
		// if (txo != 0)
		// {
		// tx = tw - (Game.winWidth / 32);
		// } else
		// {
		// tx = tw - (Game.winWidth / 32) - 1;
		// }
		// } else
		// {
		// tx = tw - (Game.winWidth / 32);
		// }
		// }//else {if (txo < 0) txo = 0;}

		// if (th - 1 > mapMaxY)
		// {
		// tyo = 32;
		// th = mapMaxY;
		//
		// if (mapMaxY == (Game.winHeight / 32))
		// {
		// if (tyo != 0)
		// {
		// ty = th - (Game.winHeight / 32);
		// } else
		// {
		// ty = th - (Game.winHeight / 32) - 1;
		// }
		// } else
		// {
		// ty = th - (Game.winHeight / 32);
		// }
		// }

		TileView.x = tx;
		TileView.y = ty;
		TileView.width = tw;
		TileView.height = th;
		this.x = txo;
		this.y = tyo;
		// this.x = 0;
		// this.y = 0;
	}
	/*
	 * public void updateCamera(int playerX, int playerY, float xOffset, float
	 * yOffset, int mapMaxX, int mapMaxY) {
	 * 
	 * if (true == true) { TileView.x = playerX; TileView.y = playerY;
	 * TileView.width = (800 / 32) + 1; TileView.height = (800 / 32) + 1; x =
	 * xOffset; y = yOffset; return; }
	 * 
	 * int tempOffX = (int) xOffset + 32; int tempOffY = (int) yOffset + 32;
	 * 
	 * int startX = playerX - START_VALUE_X; // The x coord to start rendering
	 * // from int startY = playerY - START_VALUE_Y; // The y coord to start
	 * rendering // from
	 * 
	 * // Make sure we don't render outside the maps if (startX < 0) { tempOffX
	 * = 0; if (startX == -1) { if (xOffset > 0) { tempOffX = (int) xOffset; } }
	 * startX = 0; } if (startY < 0) { tempOffY = 0; if (startY == -1) { if
	 * (yOffset > 0) { tempOffY = (int) yOffset; } } startY = 0; }
	 * 
	 * int endX = playerX + END_VALUE_X; int endY = playerY + END_VALUE_Y;
	 * 
	 * if (endX > mapMaxX) { endX = mapMaxX; if (endX ==
	 * Game.getGame().getLevel().getWidth() + 1) { if (xOffset < 0) { tempOffX =
	 * (int) xOffset + 32; } } startX = endX - MAP_MAX_X - 1; }
	 * 
	 * if (endY > mapMaxY) { endY = mapMaxY; if (endY ==
	 * Game.getGame().getLevel().getHeight() + 1) { if (yOffset < 0) { tempOffY
	 * = (int) yOffset + 32; } } startY = endY - MAP_MAX_Y - 1; }
	 * 
	 * TileView.x = startX; TileView.y = startY; TileView.width = endX;
	 * TileView.height = endY;
	 * 
	 * this.x = tempOffX; this.y = tempOffY; }
	 */

	public Rectangle getTileView()
	{
		return TileView;
	}

	public float getXOffset()
	{
		return x;
	}

	public float getYOffset()
	{
		return y;
	}

	public int getMapX(int x)
	{
		return ((x - this.TileView.x) * 32) + (int) getXOffset();
	}

	public int getMapY(int y)
	{
		return ((y - this.TileView.y) * 32) + (int) getYOffset();
	}

}

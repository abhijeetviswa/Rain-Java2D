package org.me.rain_2d.graphics;

import java.awt.Rectangle;

public class Camera
{
	int x = 0, y = 0;

	private Rectangle TileView = new Rectangle();

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

	public void updateCamera(int playerX, int playerY, int xOffset, int yOffset, int mapMaxX, int mapMaxY)
	{

		this.x = xOffset;
		this.y = yOffset;

		// Change the playerX and playerY depending upon the offset
		playerX += xOffset >> 5;
		playerY += yOffset >> 5;

		int startX = playerX - START_VALUE_X; // The x coord to start rendering
												// from
		int startY = playerY - START_VALUE_Y; // The y coord to start rendering
												// from

		// Make sure we don't render outside the maps
		if (startX < 0) startX = 0;
		if (startY < 0) startY = 0;

		int endX = playerX + END_VALUE_X;
		int endY = playerY + END_VALUE_Y;

		if (endX > mapMaxX)
		{
			endX = mapMaxX;

		}

		if (endY > mapMaxY)
		{
			endY = mapMaxY;
		}

		TileView.x = startX;
		TileView.y = startY;
		TileView.width = endX;
		TileView.height = endY;
	}

	public Rectangle getTileView()
	{
		return TileView;
	}

	public int getXOffset()
	{
		return x;
	}

	public int getYOffset()
	{
		return y;
	}

	public int getMapX(int x)
	{
		return ((x - getTileView().x) * 32) - getXOffset();
	}

	public int getMapY(int y)
	{
		return (y - getTileView().y) * 32 - getYOffset();
	}

}
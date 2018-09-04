package org.me.rain_2d.graphics;

import java.awt.Rectangle;
import org.me.rain_2d.Game;

public class Camera
{

	private final int SCREEN_WIDTH;
	private final int SCREEN_HEIGHT;
	private final int SCREEN_TILE_WIDTH;
	private final int SCREEN_TILE_HEIGHT;

	Rectangle view = new Rectangle();
	float xOffset, yOffset;

	public Camera(int width, int height, int screenTileWidth, int screenTileHeight)
	{
		this.SCREEN_WIDTH = width;
		this.SCREEN_HEIGHT = height;
		this.SCREEN_TILE_WIDTH = screenTileWidth;
		this.SCREEN_TILE_HEIGHT = screenTileHeight;
	}

	public void centerAround(int x, int y, float xOffset, float yOffset, int mapWidth, int mapHeight)
	{

		// Center the camera around x and y
		int left = x - (SCREEN_TILE_WIDTH / 2);
		int right = x + (SCREEN_TILE_WIDTH / 2) + 1;
		if (left < 0)
		{
			right += -1 * left;
			left = 0;
		} else if (right > mapWidth) // The map will have a minimum size = to the width of window... so hopefully both won't happen
		{
			left -= (right - mapWidth);
			right = mapWidth;
		}

		int top = y - (SCREEN_TILE_HEIGHT / 2);
		int bottom = y + (SCREEN_TILE_HEIGHT / 2) + 1;
		if (top < 0)
		{
			bottom += -1 * top;
			top = 0;
		} else
		{
			if (yOffset > 0) // is the center moving up? if so make the map move as well
			{
				top--;
			}
		}
		if (bottom > mapHeight) // The map will have a minimum size = to the width of window... so hopefully both won't happen
		{
			top -= (bottom - mapHeight);
			bottom = mapHeight;
			if (yOffset < 0) // we're moving down
			{
				//yOffset = 0;
			}
		} else
		{
			if (yOffset < 0) // is the center moving down?
			{
				if (!(bottom == mapHeight)) // if the bottom is the map then map musn't scroll
					bottom++;
				// if (bottom > mapHeight) bottom--;
			}
		}

		view.x = left;
		view.width = right;
		view.y = top;
		view.height = bottom;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	public Rectangle getTileView()
	{
		return view;
	}

	public float getXOffset()
	{
		return xOffset;
	}

	public float getYOffset()
	{
		return yOffset;
	}
}

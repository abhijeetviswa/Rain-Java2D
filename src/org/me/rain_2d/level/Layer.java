package org.me.rain_2d.level;

import org.me.rain_2d.graphics.Screen;

public class Layer
{
	Tile[][] tile;
	int width, height;

	public Layer(int width, int height)
	{
		this.width = width;
		this.height = height;
		this.tile = new Tile[width][height];
	}

	public Tile getTile(int x, int y)
	{
		if (x < 0 || y < 0 || x >= width - 1 || y >= height - 1)
		{
			return null;
		}
		if (tile[x][y] == null || tile[x][y].tex == null)
		{
			return new Tile(true);
		}
		return tile[x][y];
	}

	public void render(Screen screen)
	{
		for (int x = screen.getTileView().x; x <= screen.getTileView().width; x++)
		{
			for (int y = screen.getTileView().y; y <= screen.getTileView().height; y++)
			{
				Tile t = getTile(x, y);
				if (t != null)
				{
					t.render(x, y, screen);
				}
			}
		}
	}
}

package org.me.rain_2d.level;

import java.awt.Color;
import java.io.File;

import org.me.rain_2d.graphics.Screen;
import org.me.rain_2d.graphics.textures.Sprite;

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
		if (x < 0 || y < 0 || x >= width - 1 || y >= height - 1) {
			return null;
		}
		if (tile[x][y] == null || tile[x][y].tex == null) {
			return new Tile(true);
		}
		return tile[x][y];
	}

	public void render(Screen screen)
	{
		int x0 = screen.getXOffset() >> 5;
		int x1 = (screen.getXOffset() + screen.getWidth() + 32) >> 5;
		int y0 = screen.getYOffset() >> 5;
		int y1 = (screen.getYOffset() + screen.getHeight() + 32) >> 5;

		for (int x = x0; x < x1; x++) {
			for (int y = y0; y < y1; y++) {
				Tile t = getTile(x, y);
				if (t != null) {
					t.render(x << 5, y << 5, screen);
				} else {
					screen.renderTile(x << 5, y << 5, new Sprite(32, Color.black));
				}
			}
		}
	}
}

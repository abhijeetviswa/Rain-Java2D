package org.me.rain_2d.level;

import org.me.rain_2d.graphics.textures.Screen;
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
		if (x < 0 || y < 0 || x >= width || y >= height) {
			return null;
		}
		if (tile[x][y] == null || tile[x][y].tex == null) {
			return new Tile(true);
		}
		return tile[x][y];
	}

	public void render(Screen screen, int l)
	{
		int x0 = screen.getXOffset() >> 5;
		int x1 = (screen.getXOffset() + screen.getWidth() + 32) >> 5;
		int y0 = screen.getYOffset() >> 5;
		int y1 = (screen.getYOffset() + screen.getHeight() + 32) >> 5;

		for (int x = x0; x < x1; x++) {
			for (int y = y0; y < y1; y++) {
				if (x == 4 && y == 4 && l == 1){
					System.out.println("random");
				}
				Tile t = getTile(x, y);
				if (t != null) {
					t.render(x << 5, y << 5, screen);
				} else {
					screen.renderTile(x << 5, y << 5, new Sprite(32, 0));
				}
			}
		}
	}
}

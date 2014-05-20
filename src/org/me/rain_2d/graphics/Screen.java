package org.me.rain_2d.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import org.me.rain_2d.graphics.textures.Sprite;

public class Screen
{

	private int width, height;
	public int[] pixels;

	public final int MAP_SIZE = 64;
	public final int MAP_SIZE_MASK = MAP_SIZE - 1;

	private Camera camera;

	public Screen(int width, int height)
	{
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
		camera = new Camera(width / 2, height / 2, width, height);

	}

	public void renderTile(int xp, int yp, Sprite sprite)
	{
		// xp -= camera.getXOffset();
		// yp -= camera.getYOffset();
		for (int y = 0; y < 32; y++)
		{
			int ya = y + yp;
			for (int x = 0; x < 32; x++)
			{
				int xa = x + xp;
				if (xa < -32 || xa >= width || ya < 0 || ya >= height) break;
				if (xa < 0) xa = 0;
				int col = sprite.pixels[x + y * 32];
				if (col != 0) pixels[xa + ya * width] = col;
			}
		}
	}

	public void renderMob(int x, int y, int xOffset, int yOffset, Sprite sprite)
	{
		// x <<= 5;
		// y <<= 5;
		// x = ((x + xOffset) + camera.getXOffset());
		// y = ((y + yOffset) + camera.getYOffset());
		x = convertMapX(x) + xOffset;
		y = convertMapY(y) + yOffset;
		for (int yCounter = 0; yCounter < 32; yCounter++)
		{
			int ya = yCounter + y;
			for (int xCounter = 0; xCounter < 32; xCounter++)
			{
				int xa = xCounter + x;
				if (xa < -32 || xa >= width || ya < 0 || ya >= height) break;
				if (xa < 0) xa = 0;
				int col = sprite.pixels[xCounter + yCounter * 32];
				if (col != 0) pixels[xa + ya * width] = col;
			}
		}

	}

	public void renderPlayer(int x, int y, int xOffset, int yOffset, Sprite sprite)
	{
	}

	public void renderHealth(int x, int y, int xOffset, int yOffset)
	{
		x <<= 5;
		y <<= 5;
		x = ((x + xOffset) - camera.getXOffset());
		y = ((y + yOffset) - camera.getYOffset());
		x += 2;
		y += 36;

		int barWidth = 29;
		int barHeight = 4;

		int[] bg = new int[barWidth * barHeight];
		// Render the background of the bar (The black part)
		BufferedImage imageBG = new BufferedImage(barWidth, barHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) imageBG.getGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, 1, 1);
		g.dispose();
		int col = imageBG.getRGB(0, 0);
		Arrays.fill(bg, col);

		for (int yCounter = 0; yCounter < barHeight; yCounter++)
		{
			int ya = yCounter + y;
			for (int xCounter = 0; xCounter < barWidth; xCounter++)
			{
				int xa = xCounter + x;
				if (xa < -32 || xa >= width || ya < 0 || ya >= height) break;
				if (xa < 0) xa = 0;
				pixels[xa + ya * width] = bg[xCounter + yCounter * barWidth];
			}
		}

	}

	public void updateCamera(int playerX, int playerY, int xOffset, int yOffset, int mapMaxX, int mapMaxY)
	{
		camera.updateCamera(playerX, playerY, xOffset, yOffset, mapMaxX, mapMaxY);
	}

	public Rectangle getTileView()
	{
		return camera.getTileView();
	}

	public int getXOffset()
	{
		return camera.getXOffset();
	}

	public int getYOffset()
	{
		return camera.getYOffset();
	}

	public void clear()
	{
		this.pixels = new int[width * height];

	}

	public int getHeight()
	{
		return height;
	}

	public int getWidth()
	{
		return width;
	}

	public int convertMapX(int x)
	{
		return camera.getMapX(x);
	}

	public int convertMapY(int y)
	{
		return camera.getMapY(y);
	}
}

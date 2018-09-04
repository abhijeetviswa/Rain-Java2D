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
		camera = new Camera(width, height, width / 32, height / 32);

	}

	public void renderTile(float xp, float yp, float xOffset, float yOffset, Sprite sprite)
	{
		// xOffset = 0;
		// yOffset = 0;
		for (int y = (int) 0; y < 32; y++)
		{
			float ya = yp + y;
			for (int x = (int) 0; x < 32; x++)
			{
				float xa = x + xp;
				if ((xa + xOffset) < 0 || (xa + xOffset) >= width || (ya + yOffset) < 0 || (ya + yOffset) >= height) continue;
				//if ((y + yOffset) >= sprite.SIZE) continue;
				int col = sprite.pixels[x + y * sprite.SIZE];
				if (col != 0)
				{
					pixels[(int) ((xa + xOffset) + (ya + yOffset) * width)] = col;
				}
			}
		}
	}

	public void renderMob(int x, int y, float xOffset, float yOffset, Sprite sprite)
	{
		// if (yOffset > 0 || yOffset < 0) return;
		x = (int) (convertFromMapX(x));
		y = (int) (convertFromMapY(y));
		// xOffset = 0;
		// yOffset = 0;
		xOffset *= -1;
		yOffset *= -1;
		for (int yCounter = 0; yCounter < 32; yCounter++)
		{
			int ya = yCounter + y + (int) yOffset;
			for (int xCounter = 0; xCounter < 32; xCounter++)
			{
				int xa = xCounter + x + (int) xOffset;
				if (xa < -32 || xa >= width || ya < 0 || ya >= height) break;

				if (xa < 0)
				{
					xa = 0;
				}
				int col = sprite.pixels[xCounter + yCounter * 32];
				if (col != 0)
				{
					pixels[xa + ya * width] = col;
				}
			}
		}
	}

	public void renderPlayer(int x, int y, int xOffset, int yOffset, Sprite sprite)
	{}

	public void renderHealth(int x, int y, int xOffset, int yOffset)
	{
		x <<= 5;
		y <<= 5;
		// x = (int) (((x + xOffset) - camera.getXOffset()));
		// y = (int) (((y + yOffset) - camera.getYOffset()));
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
				if (xa < -32 || xa >= width || ya < 0 || ya >= height)
				{
					break;
				}
				if (xa < 0)
				{
					xa = 0;
				}
				pixels[xa + ya * width] = bg[xCounter + yCounter * barWidth];
			}
		}

	}

	public void updateCamera(int playerX, int playerY, float xOffset, float yOffset, int mapMaxX, int mapMaxY)
	{
		// camera.updateCamera(playerX, playerY, xOffset, yOffset, mapMaxX, mapMaxY);
		camera.centerAround(playerX, playerY, xOffset, yOffset, mapMaxX, mapMaxY);
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

	public Rectangle getTileView()
	{
		return camera.getTileView();
	}

	/**
	 * Return the pixel coordinates (on-screen) for the given map tile coordinate
	 */
	public int convertFromMapX(int x)
	{
		return (x - camera.getTileView().x) * 32;
	}

	/**
	 * Return the pixel coordinates (on-screen) for the given map tile coordinate
	 */
	public int convertFromMapY(int y)
	{
		return (y - camera.getTileView().y) * 32;
	}

	public float getXOffset()
	{
		return camera.getXOffset();
	}

	public float getYOffset()
	{
		return camera.getYOffset();
	}
}

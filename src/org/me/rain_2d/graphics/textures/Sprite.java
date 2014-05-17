package org.me.rain_2d.graphics.textures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class Sprite
{
	public final int SIZE;
	public int x, y;
	public int pixels[];
	public Texture sheet;

	public Sprite(int size, int x, int y, Texture sheet)
	{
		SIZE = size;
		pixels = new int[size * size];
		this.x = x >= 0 ? x * size : (x + 1) * size;
		this.y = y >= 0 ? y * size : (y + 1) * size;
		this.sheet = sheet;
		// Load the sheet or extends it's lifetime in memory
		this.sheet.load();
		load();
	}

	public Sprite(int size, Color colour)
	{
		SIZE = size;
		pixels = new int[size * size];
		setColour(colour);

	}

	public void setColour(Color colour)
	{
		BufferedImage img = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = img.createGraphics();
		g.setColor(colour);
		g.fillRect(0, 0, 1, 1);
		int col = img.getRGB(0, 0);
		Arrays.fill(pixels, col);
	}

	public void load()
	{
		for (int y = 0; y < SIZE; y++) {
			for (int x = 0; x < SIZE; x++) {
				pixels[x + y * SIZE] = sheet.pixels[((x + this.x)) + ((y + this.y)) * sheet.getWidth()];
			}
		}
	}
}

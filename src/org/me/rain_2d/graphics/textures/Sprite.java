package org.me.rain_2d.graphics.textures;


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
		this.x = x * size;
		this.y = y * size;
		this.sheet = sheet;
		// Load the sheet or extends it's lifetime in memory 
		this.sheet.load();
		load();
	}

	public Sprite(int size, int color)
	{
		SIZE = size;
		pixels = new int[size * size];
		setColour(color);
	}

	public void setColour(int colour)
	{
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = colour;
		}
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

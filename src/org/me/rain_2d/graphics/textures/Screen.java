package org.me.rain_2d.graphics.textures;

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
		xp -= camera.getXOffset();
		yp -= camera.getYOffset();
		for (int y = 0; y < 32; y++) {
			int ya = y + yp;
			for (int x = 0; x < 32; x++) {
				int xa = x + xp;
				if (xa < -32 || xa >= width || ya < 0 || ya >= height) break;
				if (xa < 0) xa = 0;
				int col = sprite.pixels[x + y * 32];
				pixels[xa + ya * width] = col;
			}
		}
	}

	public void renderMob(int x, int y, int xOffset, int yOffset, Sprite sprite)
	{
		x <<= 5;
		y <<= 5;
		x = ((x + xOffset) - camera.getXOffset());
		y = ((y + yOffset) - camera.getYOffset());

		for (int yCounter = 0; yCounter < 32; yCounter++) {
			int ya = yCounter + y;
			for (int xCounter = 0; xCounter < 32; xCounter++) {
				int xa = xCounter + x;
				if (xa < -32 || xa >= width || ya < 0 || ya >= height) break;
				if (xa < 0) xa = 0;
				int col = sprite.pixels[xCounter + yCounter * 32];
				if (col != 0xffffff) pixels[xa + ya * width] = col;
			}
		}

	}

	public void setOffset(int xOffset, int yOffset)
	{
		camera.setCameraOffset(xOffset, yOffset);
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
		pixels = new int[width * height];
	}

	public int getHeight()
	{
		return height;
	}

	public int getWidth()
	{
		return width;
	}
}

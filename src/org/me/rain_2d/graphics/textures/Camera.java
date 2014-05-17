package org.me.rain_2d.graphics.textures;

public class Camera
{
	int x = 0, y = 0;

	int width = 0, height = 0;

	public Camera(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void setCameraOffset(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public int getXOffset()
	{
		return x;
	}

	public int getYOffset()
	{
		return y;
	}

}
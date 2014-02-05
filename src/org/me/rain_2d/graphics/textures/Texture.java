package org.me.rain_2d.graphics.textures;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Texture
{

	public String path;
	private int SIZE_X, SIZE_Y;
	public int[] pixels;

	private long timer = 0;
	public boolean loaded = false;

	protected static int numTextures = 0;

	public Texture(String path)
	{
		this(path, true);
	}

	public Texture(String path, boolean shouldLoad)
	{
		this.path = path;
		if (shouldLoad) load();
	}

	public void load()
	{
		if (this.loaded) {
			timer = System.currentTimeMillis() + 15000;
			return;
		}
		if (!isLoaded()) {
			try {
				BufferedImage image = ImageIO.read(new File(path));
				int w = image.getWidth();
				int h = image.getHeight();
				SIZE_X = w;
				SIZE_Y = h;
				pixels = new int[w * h];
				image.getRGB(0, 0, w, h, pixels, 0, w);
				loaded = true;
				timer = System.currentTimeMillis() + 15000;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public int getWidth()
	{
		load();
		return SIZE_X;
	}

	public int getHeight()
	{
		load();
		return SIZE_Y;
	}

	public boolean isLoaded()
	{
		return loaded;
	}

	public void unLoad()
	{
		if (System.currentTimeMillis() > timer && loaded) {
			pixels = new int[0];
			System.out.println(path);
			loaded = false;
			timer = 0;
		}
	}

	public static void unLoadAll(TextureCacher texC)
	{
		for (Texture tex : texC.tex_tileset.values()) {
			tex.unLoad();
		}

		for (Texture tex : texC.tex_character.values()) {
			tex.unLoad();
		}

	}

}

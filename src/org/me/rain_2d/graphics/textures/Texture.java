package org.me.rain_2d.graphics.textures;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.me.rain_2d.Game;

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
		if (this.loaded)
		{
			timer = System.currentTimeMillis() + 60000;
		}
		if (!isLoaded())
		{
			try
			{
				// Read on based how we are running

				BufferedImage in = null;
				if (Game.runningOnJar())
				{
					in = ImageIO.read(getClass().getResourceAsStream("/" + path));
				} else
				{
					in = ImageIO.read(new File(path));
				}
				BufferedImage image = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);
				Graphics2D g = image.createGraphics();
				g.drawImage(in, 0, 0, null);
				g.dispose();

				int w = image.getWidth();
				int h = image.getHeight();
				SIZE_X = w;
				SIZE_Y = h;
				pixels = new int[w * h];
				image.getRGB(0, 0, w, h, pixels, 0, w);
				loaded = true;
				timer = System.currentTimeMillis() + 15000;
			} catch (Exception e)
			{
				System.err.println("Couldn't load file" + path);
				e.printStackTrace();
			}
		}

	}

	public BufferedImage getAlphaImage(File f) throws IOException
	{
		BufferedImage in = ImageIO.read(f);
		BufferedImage image = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		g.drawImage(in, 0, 0, null);
		g.dispose();
		return image;

	}

	public int getWidth()
	{
		if (!loaded) load();
		return SIZE_X;
	}

	public int getHeight()
	{
		if (!loaded) load();
		return SIZE_Y;
	}

	public boolean isLoaded()
	{
		return loaded;
	}

	public void unLoad()
	{
		if (System.currentTimeMillis() > timer && loaded)
		{
			pixels = new int[0];
			System.out.println(path);
			loaded = false;
			timer = 0;
		}
	}

	public static void unLoadAll(TextureCacher texC)
	{
		for (Texture tex : texC.tex_tileset.values())
		{
			tex.unLoad();
		}

		for (Texture tex : texC.tex_character.values())
		{
			tex.unLoad();
		}

	}

}

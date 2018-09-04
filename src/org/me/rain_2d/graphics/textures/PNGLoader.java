package org.me.rain_2d.graphics.textures;

import java.awt.Dimension;
import java.awt.image.ColorModel;
import java.awt.image.ImageConsumer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import com.sixlegs.image.png.PngImage;

class PNGLoader implements ImageConsumer
{
	private static PNGLoader inst = new PNGLoader();
	private int[] pixels;
	private boolean ret = false;

	private PNGLoader()
	{

	}

	public static PNGLoader getPNGLoader()
	{
		return inst;
	}

	public void LoadImageReturnArray(String path, int pixels[]) throws Exception
	{
		LoadImageReturnArray(new FileInputStream(new File(path)), pixels);
	}

	public void LoadImageReturnArray(InputStream stream, int pixels[]) throws Exception
	{
		this.pixels = pixels;
		PngImage png = new PngImage(stream);
		png.addConsumer(this);
		png.getEverything();
		
	}

	public Dimension GetPNGDimensions(String path)
	{
		try
		{
			return GetPNGDimensions(new FileInputStream(new File(path)));
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public Dimension GetPNGDimensions(InputStream stream)
	{
		PngImage png = new PngImage(stream);
		try
		{
			return new Dimension(png.getWidth(), png.getHeight());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public void setDimensions(int width, int height)
	{}

	public void setProperties(Hashtable<?, ?> props)
	{}

	public void setColorModel(ColorModel model)
	{}

	public void setHints(int hintflags)
	{}

	public void setPixels(int x, int y, int w, int h, ColorModel model, byte[] pixels, int off, int scansize)
	{}

	@Override
	public void setPixels(int x, int y, int w, int h, ColorModel model, int[] pixels, int off, int scansize)
	{
		System.arraycopy(pixels, 0, this.pixels, 0, w * h);
	}

	public void imageComplete(int status)
	{}
}

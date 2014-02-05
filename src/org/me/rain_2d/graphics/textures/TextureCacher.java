package org.me.rain_2d.graphics.textures;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;

import org.me.rain_2d.graphics.textures.Texture;

public class TextureCacher
{
	protected HashMap<String, Texture> tex_tileset = new HashMap<String, Texture>();
	protected HashMap<String, Texture> tex_character = new HashMap<String, Texture>();

	public Texture getTilesetTexture(String k)
	{
		if (tex_tileset.containsKey(k)) {
			return tex_tileset.get(k);
		} else {
			throw new IllegalArgumentException("(k = " + k + ") Make sure file exists!");
		}
	}

	public Texture getCharacterTexture(String k)
	{
		if (tex_character.containsKey(k)) {
			return tex_character.get(k);
		} else {
			throw new IllegalArgumentException("k = " + k + "/n Make sure file exists!");
		}
	}

	// *************
	// ***CACHING***
	// *************
	public TextureCacher cache()
	{
		try {
			cacheTilesets();
			cacheCharacters();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	private void cacheTilesets() throws URISyntaxException
	{
		File fol = new File(Texture.class.getResource("/textures/tilesets/").toURI());
		for (File f : fol.listFiles()) {
			if (f.getName().endsWith(".png")) {
				Texture tex = new Texture(f.getPath(), false);
				tex_tileset.put(f.getName().substring(0, f.getName().indexOf(".png")), tex);
			}
		}
	}

	private void cacheCharacters() throws URISyntaxException
	{

		File fol = new File(Texture.class.getResource("/textures/characters/").toURI());
		for (File f : fol.listFiles()) {
			if (f.getName().endsWith(".png")) {
				Texture tex = new Texture(f.getPath(), false);
				tex_character.put(f.getName().substring(0, f.getName().indexOf(".png")), tex);
			}
		}
	}

}

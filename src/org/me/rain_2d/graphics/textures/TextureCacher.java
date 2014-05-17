package org.me.rain_2d.graphics.textures;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.me.rain_2d.Game;

import sun.misc.Launcher;

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

	private void cacheTilesets() throws IOException, URISyntaxException
	{
		String path = "textures/tilesets";
		if (Game.runningOnJar()) { // Run with JAR file
			final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
			final JarFile jar = new JarFile(jarFile.getPath().replace("%20", " "));
			final Enumeration<JarEntry> entries = jar.entries(); // gives ALL
																	// entries
																	// in jar
			while (entries.hasMoreElements()) {
				final String name = entries.nextElement().getName();
				if (name.startsWith(path + "/")) { // filter according to the
													// path

					if (name.endsWith(".png")) {
						Texture tex = new Texture(name, false);
						tex_tileset.put(name.substring(path.length() + 1, name.indexOf(".png")), tex); // The +1 is for the forwardslash in the path
					}
				}
			}
			jar.close();
		} else { // Run with IDE
			File fol = new File(Texture.class.getResource("/" + path).toURI());
			for (File f : fol.listFiles()) {
				Texture tex = new Texture(f.getPath(), false);
				tex_tileset.put(f.getName().substring(0, f.getName().indexOf(".png")), tex);
			}
		}
	}

	private void cacheCharacters() throws IOException, URISyntaxException
	{

		String path = "textures/characters";
		if (Game.runningOnJar()) { // Run with JAR file
			final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
			final JarFile jar = new JarFile(jarFile.getPath().replace("%20", " "));
			final Enumeration<JarEntry> entries = jar.entries(); // gives ALL
																	// entries
																	// in jar
			while (entries.hasMoreElements()) {
				final String name = entries.nextElement().getName();
				if (name.startsWith(path + "/")) { // filter according to the
													// path
					if (name.endsWith(".png")) {
						Texture tex = new Texture(name, false);
						tex_character.put(name.substring(path.length() + 1, name.indexOf(".png")), tex);
					}
				}
			}
			jar.close();
		} else { // Run with IDE
			File fol = new File(Texture.class.getResource("/" + path).toURI());
			for (File f : fol.listFiles()) {
				Texture tex = new Texture(f.getPath(), false);
				tex_character.put(f.getName().substring(0, f.getName().indexOf(".png")), tex);
			}
		}
	}

}
